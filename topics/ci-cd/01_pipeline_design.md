# 01 — Pipeline Design

## 1. The pipeline as a feedback system

A pipeline answers one question: **"Is this change safe to ship?"** Every stage buys confidence at
a cost in time and money. Design is the art of buying the most confidence per minute.

| Stage | Confidence it buys | Typical duration | Runs on |
|---|---|---|---|
| Lint / static analysis | Style, obvious defects, unsafe patterns | Seconds | Every commit |
| Compile / typecheck | Code integrates | Seconds–minutes | Every commit |
| Unit | Logic correctness | Seconds–minutes | Every commit |
| Component / integration | Service + real infra wiring | Minutes | Every PR |
| Contract | Provider/consumer compatibility | Minutes | Every PR / pre-deploy |
| API / GraphQL | Business behavior over HTTP | Minutes | Every PR or merge |
| UI smoke | Critical user journeys | Minutes | Merge / pre-deploy |
| Full regression | Broad coverage | Tens of minutes | Nightly / pre-release |
| Performance / resilience | Non-functional behavior | Minutes–hours | Nightly / pre-release |
| Release validation | The candidate works in the target env | Minutes | Pre-deploy |

**Rule:** cheap, deterministic, and broad go first. Expensive, flaky, and narrow go last.

---

## 2. Pipeline stages in detail

### Build

- Checkout the exact commit.
- Restore dependencies from a lockfile.
- Compile and typecheck.
- Run static analysis and dependency scanning.
- Produce a versioned artifact (jar, container image, bundle).

**Build once, deploy many.** The artifact that passed tests is the artifact that ships.

### Test

Split tests by **feedback time** and **risk**, not by folder convenience:

```text
unit          -> every commit
component     -> every PR
contract      -> every PR + pre-deploy
api/graphql   -> every PR or merge
ui smoke      -> merge + pre-deploy
ui regression -> nightly + pre-release
performance   -> nightly / pre-release
```

### Package and publish

- Tag the artifact with the commit SHA and semantic version.
- Publish to an artifact repository (Nexus, Artifactory, GitHub Packages, ACR, ECR).
- Sign or attest the artifact if your supply chain requires it.
- Keep the build metadata: JDK version, dependency lockfile, test report.

### Deploy

- Deploy to a staging or ephemeral environment.
- Run release validation against that environment.
- Promote the same artifact to production through a controlled gate.
- Record the deployed version, time, actor, and change set.

### Verify

- Smoke tests against the deployed environment.
- Synthetic checks on critical journeys.
- Error rate, latency, and saturation dashboards.
- Business metrics if available.
- Rollback trigger and owner.

---

## 3. Parallelism, caching, and artifacts

### Parallelism

- **Fan-out** independent test groups (API, UI, contract) and **fan-in** before deploy.
- Use a matrix for browsers, JDKs, OSes, or shards.
- Do not parallelize tests that share mutable state.
- Keep the critical path short; parallel jobs still cost runner minutes.

### Caching

| What to cache | Key | Invalidate when |
|---|---|---|
| Maven/Gradle dependencies | `pom.xml` / lockfile hash | Dependency change |
| npm packages | `package-lock.json` hash | Lockfile change |
| Playwright browsers | Playwright version | Playwright upgrade |
| Docker layers | Dockerfile + build context | Dockerfile change |
| Test reports | Not usually cached | — |

Never cache secrets, build outputs that must be fresh, or mutable test data.

### Artifacts

Always upload:

- Test reports (JUnit XML, HTML, Cucumber JSON, Allure).
- Failure screenshots, videos, traces.
- Logs from the service under test.
- Coverage reports.
- The built artifact and its checksum.
- Environment metadata and commit SHA.

Set retention deliberately: short for PR runs, longer for release candidates and nightly runs.

---

## 4. Environments

| Environment | Purpose | Data | Lifetime |
|---|---|---|---|
| Local | Fast inner loop | Synthetic | Developer-controlled |
| Ephemeral / preview | Per-PR integration | Seeded, isolated | Deleted after merge |
| Staging | Release validation | Production-like, anonymized | Persistent |
| Canary | Small production slice | Real | Short-lived |
| Production | Real users | Real | Permanent |

**Ephemeral environment pattern:**

```text
PR opened
  -> provision namespace/resource group
  -> deploy artifact
  -> seed data
  -> run integration/E2E
  -> publish report
  -> destroy on merge/close
```

This removes environment contention and makes failures attributable to one change.

---

## 5. Configuration and secrets

- **Config** is environment-specific and non-secret: URLs, timeouts, feature flags.
- **Secrets** are credentials: tokens, passwords, private keys.
- Inject both at runtime; never commit them.
- Prefer **short-lived credentials** via OIDC/workload identity over long-lived secrets.
- Mask secrets in logs; rotate them; scan the repo for accidental commits.
- Fail fast if a required config value is missing — do not silently use a default.

---

## 6. Branching and triggers

| Trigger | What runs |
|---|---|
| Pull request | Lint, compile, unit, component, contract, API smoke |
| Merge to main | Full PR set + package + deploy to staging |
| Tag / release branch | Release validation, full regression, security scan |
| Schedule (nightly) | Full regression, performance, resilience, dependency scan |
| Manual | Deploy, rollback, re-run a suite |

Keep PR feedback under ~15 minutes where possible. If the suite cannot fit, split it and move the
slow part to merge/nightly.

---

## 7. Failure handling

- **Fail the stage, not the whole pipeline silently.** Make the failing test and artifact obvious.
- **Retry only known-transient operations** (network fetch, flaky infrastructure), and report the
  retry count.
- **Quarantine flaky tests** with an owner and a deadline.
- **Do not auto-retry failed deployments** without checking idempotency.
- **Preserve the environment on failure** long enough to debug, then clean up.
- **Notify the owning team**, not a shared channel with no owner.

---

## 8. Pipeline metrics

| Metric | What it tells you |
|---|---|
| Lead time for changes | Commit → production |
| Deployment frequency | How often you can ship |
| Change failure rate | How often a deploy causes an incident |
| Mean time to restore (MTTR) | How fast you recover |
| Pipeline duration (p50/p95) | Developer feedback cost |
| Flaky rate | Trustworthiness of the suite |
| Queue time | Runner capacity problem |
| Escaped defects | Gaps in the test strategy |

Use these to improve the pipeline, not to rank people.

---

## 9. Design checklist

- [ ] The same artifact is promoted across environments.
- [ ] Every stage has a clear owner and failure message.
- [ ] PR feedback is fast enough that developers wait for it.
- [ ] Expensive tests are scheduled, not mandatory on every commit.
- [ ] Test reports and failure artifacts are always uploaded.
- [ ] Secrets come from the CI secret store or OIDC.
- [ ] Environments are isolated or ephemeral.
- [ ] Deployments are idempotent and have a rollback path.
- [ ] Flaky tests are visible and owned.
- [ ] The pipeline can be run locally with the same commands.
