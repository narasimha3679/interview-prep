# 04 — Azure DevOps and CI/CD

Two common stacks:

- **Azure Pipelines** — Azure-native CI/CD with environments, approvals, and deep Azure integration.
- **GitHub Actions** — common for GitHub-hosted repos; deploys to Azure via OIDC or a service
  connection.

The pipeline design is the same; the YAML and identity model differ.

---

## 1. Azure DevOps building blocks

| Component | Purpose |
|---|---|
| **Azure Repos** | Git repositories |
| **Azure Pipelines** | CI/CD |
| **Azure Boards** | Work items, sprints, dashboards |
| **Azure Artifacts** | Maven/npm/NuGet feeds |
| **Azure Test Plans** | Manual/exploratory test management |
| **Environments** | Deployment targets with approvals/checks |
| **Service connections** | Authenticated connection to Azure/other services |
| **Variable groups** | Shared config/secrets, optionally linked to Key Vault |
| **Agent pools** | Microsoft-hosted or self-hosted agents |

---

## 2. Pipeline stages

```text
Build
  ├── compile + unit
  ├── static analysis + dependency scan
  └── publish artifact

Test
  ├── component/integration (Testcontainers)
  ├── contract verification
  ├── API/GraphQL smoke
  └── UI smoke

DeployStaging
  ├── deploy artifact to App Service slot / Container Apps revision
  ├── wait for health
  ├── run release validation
  └── publish reports

DeployCanary
  ├── shift 5–10% traffic
  ├── synthetic/smoke checks
  ├── monitor App Insights + Azure Monitor
  └── approve or auto-promote

DeployProduction
  ├── shift 100%
  ├── post-deploy smoke
  └── monitor
```

Use `dependsOn` and `condition: succeeded()` to enforce order. Use stages for deploy boundaries,
jobs for parallel test groups.

---

## 3. Minimal Azure Pipelines YAML

```yaml
trigger:
  - main

pr:
  - main

pool:
  vmImage: ubuntu-latest

variables:
  MAVEN_CACHE_FOLDER: $(Pipeline.Workspace)/.m2/repository
  MAVEN_OPTS: '-Dmaven.repo.local=$(MAVEN_CACHE_FOLDER)'

stages:
  - stage: Build
    jobs:
      - job: BuildAndTest
        steps:
          - task: Cache@2
            inputs:
              key: 'maven | "$(Agent.OS)" | pom.xml'
              path: $(MAVEN_CACHE_FOLDER)

          - task: Maven@4
            inputs:
              mavenPomFile: pom.xml
              goals: verify
              publishJUnitResults: true
              testResultsFiles: '**/surefire-reports/TEST-*.xml'

          - publish: target
            artifact: drop

  - stage: DeployStaging
    dependsOn: Build
    condition: succeeded()
    jobs:
      - deployment: DeployStaging
        environment: staging
        strategy:
          runOnce:
            deploy:
              steps:
                - download: current
                  artifact: drop
                - script: ./scripts/deploy.sh staging
                - script: ./scripts/smoke.sh staging
```

---

## 4. Service connections and OIDC

### Service connection

A service connection stores the authentication between Azure Pipelines and Azure. Prefer
**workload identity federation** over a stored client secret.

- Create an app registration / managed identity.
- Federate it with the pipeline's issuer and subject.
- Grant the minimum role on the target resource group/subscription.
- Scope the service connection to that resource group.

### GitHub Actions OIDC

```yaml
permissions:
  id-token: write
  contents: read

steps:
  - uses: azure/login@v2
    with:
      client-id: ${{ secrets.AZURE_CLIENT_ID }}
      tenant-id: ${{ secrets.AZURE_TENANT_ID }}
      subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
```

No long-lived client secret in GitHub.

**Test focus:**

- The pipeline can deploy only to the intended resource group.
- The identity cannot read/write other environments.
- Removing the federation breaks the deploy (negative test).
- Secret rotation does not require a pipeline edit.

---

## 5. Environments and approvals

Azure Pipelines **environments** provide:

- Deployment history.
- Approval gates.
- Branch control.
- Business hours checks.
- Exclusive lock.
- Template checks.

Use them for:

- Staging → production promotion.
- Manual approval for high-risk releases.
- Restricting which branches can deploy.

**Interview line:** "Approvals are not a substitute for automated validation. I automate the
technical gates and use manual approval only for the business decision or the irreversible step."

---

## 6. Test reporting

Azure Pipelines:

```yaml
- task: PublishTestResults@2
  inputs:
    testResultsFormat: 'JUnit'
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
    failTaskOnFailedTests: true
```

Also publish:

- Cucumber JSON/HTML.
- Playwright HTML report.
- Extent/Allure report.
- Failure screenshots/traces.
- Coverage report.

**Rule:** if a test fails, the report must make the failure obvious without opening the raw log.

---

## 7. Deployment slots and traffic

### App Service slots

- Deploy to `staging`.
- Warm up.
- Run smoke.
- Swap to `production`.
- Rollback by swapping back.

Slot settings:

- Mark connection strings and secrets as **slot settings** so they don't swap.
- Use deployment slot-specific config for environment URLs.
- Warm up before swap with `applicationInitialization` or a health check.

### Container Apps revisions

- Deploy a new revision with no traffic.
- Validate the revision URL.
- Shift traffic gradually.
- Roll back by shifting weight to the previous revision.

### AKS

- Use a new Deployment revision.
- Wait for `rollout status`.
- Validate service/ingress.
- Roll back with `kubectl rollout undo` or GitOps revert.

---

## 8. Container registry

```bash
az acr build --registry myregistry --image listings:$(Build.BuildId) .
az acr login --name myregistry
docker push myregistry.azurecr.io/listings:$(Build.BuildId)
```

Rules:

- Tag images with commit SHA and semantic version, not `latest`.
- Scan images for vulnerabilities.
- Use managed identity/ACR integration for AKS/Container Apps pulls.
- Keep the artifact immutable; promote the digest.

---

## 9. Quality gates in Azure Pipelines

```yaml
- stage: DeployProduction
  dependsOn: DeployCanary
  condition: succeeded()
  jobs:
    - deployment: Production
      environment: production
      strategy:
        runOnce:
          preDeploy:
            steps:
              - script: ./scripts/check-release-gates.sh
          deploy:
            steps:
              - script: ./scripts/promote.sh production
```

`check-release-gates.sh` might verify:

- Contract `can-i-deploy`.
- Smoke pass rate.
- No new critical security findings.
- Error rate/latency within budget.
- Migration succeeded.
- Rollback artifact exists.

Make the gate output explicit so a human can see why it passed or failed.

---

## 10. Common Azure CI/CD mistakes

1. **Storing client secrets** instead of using workload identity federation.
2. **Granting Contributor at subscription scope** when resource-group scope is enough.
3. **Deploying from a PR branch** without environment protection.
4. **Rebuilding the artifact per environment.**
5. **Testing the staging slot but not the production URL after swap.**
6. **Forgetting slot settings**, so production secrets swap into staging.
7. **No warm-up**, so the first request after swap fails.
8. **No rollback artifact.**
9. **No concurrency lock**, so two deploys race.
10. **No cost cleanup** for ephemeral resource groups.
