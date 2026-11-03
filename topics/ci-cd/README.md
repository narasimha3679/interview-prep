# CI/CD for SDETs — Study Pack

How to design, wire, and trust automated test pipelines: from a pull-request build to a release
gate with canary validation and rollback.

**Last verified:** November 2026.

---

## 60-second cram

1. **Pipeline is a product.** It has users (developers, QE, release), an SLA (feedback time), and
   failure modes. A slow or flaky pipeline stops being used.
2. **Fail fast, cheapest first.** Unit → component → contract → API → UI smoke → E2E. Expensive
   tests never block every merge unless the risk justifies it.
3. **Every stage needs an artifact.** Test reports, logs, traces, screenshots, videos, and the
   exact build under test. A red build without evidence is noise.
4. **Release validation is not the same as regression.** Regression checks the product; release
   validation checks the *release candidate* in the environment it will ship to.
5. **Gate on risk, not on vanity metrics.** Coverage percentage is a weak gate; contract
   compatibility, critical-path smoke, error budget, and security findings are stronger.
6. **Flaky tests are pipeline defects.** Quarantine with an owner and a deadline; retries are a
   detector, not a fix.
7. **Secrets never live in test code.** Use the CI secret store, short-lived credentials, or
   workload identity federation.
8. **The pipeline must be reproducible.** Pin tool versions, cache dependencies, and use the same
   commands locally and in CI.

**The one line that shows depth:**

> "A good pipeline gives the fastest possible signal for the risk being changed, and it makes the
> expensive tests a deliberate release gate rather than a tax on every commit."

---

## Pipeline shape

```text
commit / PR
   │
   ├── lint + compile + unit                 (seconds)
   ├── component / integration               (minutes, Testcontainers)
   ├── contract verification                 (minutes, Pact / schema)
   ├── API + GraphQL + smoke UI              (minutes)
   ├── package + publish artifact            (minutes)
   ▼
release candidate
   ├── deploy to staging / canary            (minutes)
   ├── release validation: smoke + critical E2E + migrations
   ├── contract can-i-deploy gate
   ├── security / dependency scan
   ▼
promote 100% / rollback
   └── post-deploy synthetic checks + monitoring
```

The exact order depends on risk. A payment service puts contract and security checks earlier than
a read-only marketing site.

---

## File map

| File | What it gives you | Read when |
|---|---|---|
| `01_pipeline_design.md` | Stages, parallelism, caching, artifacts, environments | First |
| `02_github_actions.md` | GitHub Actions syntax, matrices, services, OIDC, pitfalls | Second |
| `03_release_validation.md` | Release gates, canary, blue-green, rollback, migrations | Before a release round |
| `04_quality_gates.md` | What to gate on, thresholds, flaky policy, metrics | When designing gates |
| `05_jenkins_gitlab.md` | Jenkins, GitLab CI, Azure Pipelines mapping | Tool comparison |
| `examples/github-actions-ci.yml` | A generic Java + Node CI workflow | Copy/adapt |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Pipeline** | Ordered automation from commit to deploy | Every stage must produce evidence |
| **Stage** | A logical phase (build, test, deploy) | Stages can run in parallel if independent |
| **Job** | A unit of work on a runner | Jobs are isolated; share via artifacts |
| **Runner/agent** | Machine executing a job | Clean runners prevent "works on my machine" |
| **Artifact** | Persisted output (jar, report, screenshot) | The only reliable way to debug a CI failure |
| **Cache** | Reused dependencies between runs | Speeds up builds; invalidate on lockfile change |
| **Matrix** | Parameterized job combinations | Use for browsers, JDKs, OSes, environments |
| **Gate** | A condition that must pass before promotion | Should be risk-based and owned |
| **Canary** | Small slice of traffic to a new version | Smoke and monitor before full rollout |
| **Blue-green** | Two identical environments, switch traffic | Instant rollback by switching back |
| **Rollback** | Return to the previous known-good version | Must be tested, not assumed |
| **OIDC** | Short-lived cloud credentials from CI | Removes long-lived secrets |

---

## Traps

1. **Running everything on every commit.** The suite becomes a tax; people bypass it. Split by tag
   and risk.
2. **No artifact retention.** A failed UI test with no screenshot, video, or trace is almost
   useless.
3. **Retrying flaky tests silently.** The pipeline goes green; the product is still unreliable.
   Report retried-but-passed separately.
4. **Shared mutable test environment.** Teams block each other and failures are unattributable.
   Prefer ephemeral environments or isolated namespaces.
5. **Deploying and testing the wrong artifact.** Build once, promote the same artifact; do not
   rebuild per environment.
6. **Secrets in logs or repo.** Mask secrets, use OIDC, and scan for leaks.
7. **No concurrency control.** Two deploys race; the second overwrites the first. Use concurrency
   groups and environment locks.
8. **Testing only the happy path in release validation.** The release gate should include
   migrations, auth, critical integrations, and rollback.

---

## How to study this pack

- **10 minutes:** README + cram sheet.
- **30 minutes:** `01_pipeline_design.md`, draw the pipeline from memory.
- **30 minutes:** `02_github_actions.md`, adapt the example workflow.
- **30 minutes:** `03_release_validation.md`, write a release checklist for a service you know.
- **20 minutes:** `04_quality_gates.md`, decide which gates you would actually enforce.
- **20 minutes:** `05_jenkins_gitlab.md`, map the concepts to another tool.

---

## Links

- Related: [`../cloud/`](../cloud/), [`../test-strategy/`](../test-strategy/),
  [`../observability/`](../observability/)
- Runnable examples: [`examples/github-actions-ci.yml`](examples/github-actions-ci.yml)
