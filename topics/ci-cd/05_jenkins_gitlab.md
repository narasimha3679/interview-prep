# 05 — Jenkins, GitLab CI, and Azure Pipelines

The concepts are portable. Learn one tool deeply, then map the vocabulary.

| Concept | GitHub Actions | Jenkins | GitLab CI | Azure Pipelines |
|---|---|---|---|---|
| Workflow definition | `.github/workflows/*.yml` | `Jenkinsfile` | `.gitlab-ci.yml` | `azure-pipelines.yml` |
| Trigger | `on:` | `triggers`, webhooks | `rules`, `only/except` | `trigger`, `pr` |
| Unit of work | Job | Stage / node | Job | Job |
| Runner | GitHub-hosted / self-hosted | Agent / node | GitLab Runner | Microsoft-hosted / self-hosted |
| Reuse | Reusable workflow, composite action | Shared library | `include`, `extends` | Templates, extends |
| Secrets | Repository/environment secrets | Credentials plugin | CI/CD variables | Variable groups, Key Vault |
| Artifacts | `actions/upload-artifact` | `archiveArtifacts` | `artifacts:` | `PublishPipelineArtifact` |
| Cache | `actions/cache` | Plugin / workspace | `cache:` | `Cache@2` |
| Matrix | `strategy.matrix` | Matrix plugin / parallel | `parallel:matrix` | `strategy.matrix` |
| Environments | `environment:` | Folders / credentials | `environment:` | `environment:` |
| Approvals | Environment protection rules | Input step / plugin | Protected environments | Approvals and checks |
| Service container | `services:` | Docker agent | `services:` | `services:` |
| OIDC | `id-token: write` | Plugin-dependent | `id_tokens:` | Workload identity federation |

---

## Jenkins

### Declarative pipeline

```groovy
pipeline {
  agent any

  options {
    timeout(time: 30, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }

  stages {
    stage('Build') {
      steps {
        sh 'mvn -B -q -DskipTests package'
      }
    }

    stage('Test') {
      parallel {
        stage('API') {
          steps { sh 'mvn -B test -Dsuite=api' }
        }
        stage('UI smoke') {
          steps { sh 'mvn -B test -Dsuite=ui-smoke' }
        }
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
          archiveArtifacts artifacts: 'target/**', allowEmptyArchive: true
        }
      }
    }

    stage('Deploy staging') {
      when { branch 'main' }
      steps {
        sh './scripts/deploy.sh staging'
      }
    }
  }

  post {
    failure {
      mail to: 'team@example.com', subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
    }
  }
}
```

### Jenkins strengths and weaknesses

**Strengths:** extremely flexible, huge plugin ecosystem, self-hosted control, good for complex
legacy estates.

**Weaknesses:** plugin sprawl, maintenance burden, Groovy complexity, security patching, agent
management. Pipelines drift unless shared libraries are enforced.

### Jenkins interview line

> "Jenkins is powerful but expensive to own. I'd keep the pipeline logic in a shared library, pin
> plugins, run agents as ephemeral containers, and treat the Jenkinsfile as code with review and
> tests."

---

## GitLab CI

### Minimal pipeline

```yaml
stages:
  - build
  - test
  - deploy

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"

cache:
  key:
    files:
      - pom.xml
  paths:
    - .m2/repository

build:
  stage: build
  image: maven:3.9-eclipse-temurin-17
  script:
    - mvn -B -q -DskipTests package
  artifacts:
    paths:
      - target/*.jar
    expire_in: 7 days

test:
  stage: test
  image: maven:3.9-eclipse-temurin-17
  services:
    - name: postgres:16
      alias: postgres
  variables:
    POSTGRES_PASSWORD: postgres
    POSTGRES_DB: app
  script:
    - mvn -B verify
  artifacts:
    when: always
    reports:
      junit: target/surefire-reports/*.xml
    paths:
      - target/site/
    expire_in: 14 days

deploy:
  stage: deploy
  image: alpine:3.20
  script:
    - ./scripts/deploy.sh staging
  environment:
    name: staging
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
```

### GitLab strengths and weaknesses

**Strengths:** integrated SCM/CI/registry/security, good Kubernetes integration, clear environment
model.

**Weaknesses:** YAML complexity at scale, runner management, feature variance between tiers.

---

## Azure Pipelines

### Minimal pipeline

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
      - job: Maven
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
      - deployment: Deploy
        environment: staging
        strategy:
          runOnce:
            deploy:
              steps:
                - download: current
                  artifact: drop
                - script: ./scripts/deploy.sh staging
```

### Azure Pipelines strengths and weaknesses

**Strengths:** deep Azure integration, environments/approvals, test reporting, self-hosted agents,
Key Vault and workload identity support.

**Weaknesses:** YAML verbosity, task version drift, licensing for parallel jobs, complexity in
large organizations.

---

## Portability checklist

When moving a pipeline between tools, preserve:

- [ ] The same build command and artifact.
- [ ] The same test grouping and fail-fast behavior.
- [ ] The same report and artifact paths.
- [ ] The same secrets injection method.
- [ ] The same environment protection rules.
- [ ] The same concurrency/locking.
- [ ] The same timeout and retry policy.
- [ ] The same release validation steps.

The tool should be replaceable; the pipeline design should not be.
