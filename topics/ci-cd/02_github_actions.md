# 02 — GitHub Actions

## 1. Core vocabulary

| Concept | Meaning |
|---|---|
| **Workflow** | A YAML file in `.github/workflows/` |
| **Event** | What triggers the workflow (`push`, `pull_request`, `schedule`, `workflow_dispatch`) |
| **Job** | A set of steps on one runner; jobs run in parallel by default |
| **Step** | A shell command or an action |
| **Action** | A reusable unit, e.g. `actions/checkout`, `actions/setup-java` |
| **Runner** | The machine executing a job (GitHub-hosted or self-hosted) |
| **Matrix** | A set of parameterized job variants |
| **Artifact** | Files uploaded from a job and downloaded later |
| **Cache** | Reused dependency directories between runs |
| **Environment** | A deployment target with protection rules and secrets |
| **Service container** | A Docker container available to all steps in a job |

---

## 2. Minimal Java workflow

```yaml
name: Java CI

on:
  pull_request:
  push:
    branches: [main]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Compile and unit test
        run: mvn -B -q verify

      - name: Upload test reports
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: surefire-reports
          path: target/surefire-reports/
          if-no-files-found: ignore
```

Key points:

- `permissions: contents: read` follows least privilege.
- `cache: maven` uses the lockfile-aware cache built into `setup-java`.
- `if: always()` uploads reports even when tests fail.
- `if-no-files-found: ignore` avoids a second failure when the report is missing.

---

## 3. Matrix builds

```yaml
jobs:
  test:
    strategy:
      fail-fast: false
      matrix:
        java: ['17', '21']
        browser: [chromium, firefox]
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: ${{ matrix.java }}
          cache: maven
      - run: mvn -B test -Dbrowser=${{ matrix.browser }}
```

`fail-fast: false` lets all variants finish so you see the full failure picture.

---

## 4. Service containers

Use a service container for a real dependency when Testcontainers is not practical:

```yaml
jobs:
  integration:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: app
        ports:
          - 5432:5432
        options: >-
          --health-cmd "pg_isready -U postgres"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    steps:
      - uses: actions/checkout@v4
      - run: mvn -B verify -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app
```

Testcontainers is usually better for local/CI parity; service containers are convenient when the
runner supports Docker.

---

## 5. Artifacts and reports

```yaml
      - name: Upload Playwright report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: playwright-report
          path: playwright-report/
          retention-days: 14

      - name: Upload traces
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: playwright-traces
          path: test-results/
          retention-days: 7
```

Rules:

- Upload on `if: always()` for reports.
- Upload traces/videos/screenshots on failure to save storage.
- Give artifacts a predictable name.
- Set retention based on cost and debugging needs.

---

## 6. Caching

```yaml
      - uses: actions/setup-node@v4
        with:
          node-version: 20
          cache: npm
          cache-dependency-path: package-lock.json

      - run: npm ci
```

Cache keys should include the lockfile hash. Do not cache `node_modules` directly; cache the
package manager's cache instead.

---

## 7. Concurrency

```yaml
concurrency:
  group: deploy-${{ github.ref }}
  cancel-in-progress: true
```

Use concurrency groups to:

- Cancel superseded PR runs.
- Prevent two deploys to the same environment.
- Serialize release jobs.

Do **not** cancel an in-progress production deploy unless the deploy is idempotent and safe to
abort.

---

## 8. Secrets and OIDC

### Repository/environment secrets

```yaml
      - name: Run tests with token
        env:
          API_TOKEN: ${{ secrets.API_TOKEN }}
        run: mvn -B test
```

Secrets are masked in logs, but they can still leak via command output. Never `echo` them.

### OIDC to a cloud provider

Prefer short-lived credentials over stored cloud keys:

```yaml
permissions:
  id-token: write
  contents: read

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
      - run: az deployment group create --resource-group rg-test --template-file infra/main.bicep
```

The cloud side must trust the GitHub OIDC issuer and restrict the subject to the repo/branch/
environment.

---

## 9. Reusable workflows and composite actions

### Reusable workflow

`.github/workflows/test-template.yml`:

```yaml
on:
  workflow_call:
    inputs:
      suite:
        required: true
        type: string

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: mvn -B test -Dsuite=${{ inputs.suite }}
```

Caller:

```yaml
jobs:
  smoke:
    uses: ./.github/workflows/test-template.yml
    with:
      suite: smoke
```

Use reusable workflows for organization-wide standards; use composite actions for repeated step
sequences.

---

## 10. Common pitfalls

1. **`pull_request` from a fork cannot read secrets.** Use `pull_request_target` carefully, or
   design the workflow so untrusted code does not get secrets.
2. **Expressions are not shell.** `${{ }}` is evaluated by Actions; quote values that may contain
   spaces or special characters.
3. **A failing step stops the job.** Use `if: always()` for cleanup and report upload.
4. **Artifacts are per-workflow-run.** Download them explicitly in downstream jobs.
5. **Caches are scoped by branch.** A cache created on `main` may not be visible to a PR branch.
6. **`ubuntu-latest` changes over time.** Pin `runs-on` if reproducibility matters.
7. **Workflow files on a branch can change permissions.** Review `.github/workflows/` changes like
   code.
8. **Long-running jobs hit limits.** Split or self-host; do not hide it with retries.
9. **No timeout.** Add `timeout-minutes` to jobs to avoid hung runners.
10. **Secrets in `run:` interpolation.** Pass secrets via `env:` and quote them.

---

## 11. Workflow checklist

- [ ] Triggers are intentional (`pull_request`, `push`, `schedule`, `workflow_dispatch`).
- [ ] `permissions` is least privilege.
- [ ] Jobs have `timeout-minutes`.
- [ ] Dependencies are cached with lockfile keys.
- [ ] Test reports upload on `always()`.
- [ ] Failure artifacts upload on failure.
- [ ] Deploy jobs use environments and approvals.
- [ ] Cloud auth uses OIDC, not long-lived keys.
- [ ] Concurrency prevents duplicate deploys.
- [ ] The workflow can be reproduced locally.
