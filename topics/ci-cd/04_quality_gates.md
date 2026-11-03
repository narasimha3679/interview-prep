# 04 — Quality Gates

A quality gate is a condition that must be met before code merges or a release promotes. Good
gates are **risk-based, owned, and observable**. Bad gates are arbitrary numbers that teams learn
to game.

---

## 1. What to gate on

| Gate | Good use | Weak use |
|---|---|---|
| **Compile/typecheck** | Always | — |
| **Unit tests** | Always, but fast | Requiring 100% coverage |
| **Component/integration** | Services with real infra | Every commit if slow |
| **Contract compatibility** | Service-to-service interfaces | Only at release |
| **API/GraphQL smoke** | Every PR or merge | Full regression on every commit |
| **UI smoke** | Critical journeys | Full UI suite on every commit |
| **Security scan** | New critical/high findings | Blocking on every low finding |
| **Dependency scan** | Known exploited vulnerabilities | Ignoring the report |
| **Performance budget** | Latency/throughput regressions | One-off benchmarks |
| **Coverage** | Trend and gap detection | A hard percentage target |
| **Flaky rate** | Quarantine policy | Retrying until green |
| **Release validation** | Pre-deploy | Assuming staging equals production |

---

## 2. Coverage: use it as a smell, not a target

Coverage tells you what code was executed, not what behavior was verified.

Use it to:

- Find untested areas of high-risk code.
- Detect a sudden drop after a change.
- Compare modules, not individuals.

Do not:

- Set a single global percentage and call it quality.
- Write assertion-free tests to raise the number.
- Gate a release on coverage alone.

Better companions: mutation testing on critical modules, contract coverage, risk-based test
selection, and escaped-defect analysis.

---

## 3. Flaky-test policy

A flaky test is one that passes and fails on the same code.

Policy:

1. **Detect** — track per-test pass/fail history; compute flaky rate.
2. **Quarantine** — move to a non-blocking job; keep it visible.
3. **Assign** — a named owner and a fix deadline.
4. **Diagnose** — bad waits, shared state, order dependence, environment, or a real product race.
5. **Fix or delete** — do not keep a permanently quarantined test.
6. **Report retries** — show retried-but-passed separately; never hide them.

**Gate:** block merges on *new* flakiness in critical suites, not on a legacy flaky test that is
already owned.

---

## 4. Test impact analysis

Not every change needs every test. Test impact analysis maps changed code to relevant tests.

Simple version:

- Changed file → owning module → module's unit/component/contract tests.
- Changed API schema → contract + API tests.
- Changed shared library → all consumers' smoke tests.
- Changed infrastructure → deployment + release validation.

Advanced version: coverage maps, dependency graphs, and historical failure data.

**Trap:** impact analysis can miss integration effects. Keep a scheduled full run as a safety net.

---

## 5. Quality metrics that mean something

| Metric | Definition | Use |
|---|---|---|
| **Escaped defects** | Bugs found after release | Test effectiveness |
| **Change failure rate** | Deploys causing incidents | Release quality |
| **MTTR** | Time to restore service | Detection + recovery |
| **Flaky rate** | Failing then passing on same code | Suite trust |
| **Pipeline duration p95** | Slowest common path | Developer feedback |
| **Test suite pass rate** | First-run pass rate | Stability |
| **Coverage trend** | Direction, not absolute | Gap detection |
| **Mean time to feedback** | Commit → first test result | Inner loop |
| **Quarantine age** | How long flaky tests stay quarantined | Accountability |

Avoid vanity metrics: number of tests, lines of test code, number of assertions.

---

## 6. Gate design checklist

- [ ] The gate maps to a real risk.
- [ ] The threshold is justified and documented.
- [ ] The gate has an owner.
- [ ] The failure message tells the team what to do.
- [ ] The gate can be overridden with an explicit, audited decision.
- [ ] The gate is fast enough for its trigger.
- [ ] The gate does not depend on a shared mutable environment.
- [ ] The gate reports evidence (test report, scan, trace).
- [ ] The gate is reviewed when it stops catching real issues.

---

## 7. Example gate set

### Pull request

- Compile/typecheck passes.
- Unit tests pass.
- Component tests pass for changed services.
- Contract verification passes.
- No new critical/high security findings.
- Lint/static analysis passes.

### Merge to main

- All PR gates pass.
- API/GraphQL smoke passes.
- UI smoke passes for critical journeys.
- Artifact is published and versioned.

### Release candidate

- Full regression passes or has an approved exception.
- Contract `can-i-deploy` passes.
- Release validation on staging passes.
- Migration test passes.
- Rollback path verified.
- Security scan has no unresolved critical/high findings.

### Post-deploy

- Smoke and synthetic checks pass.
- Error rate and latency within budget.
- No new P1/P2 alerts.
- Business metric within expected range.
