# 04 — Flakiness and Suite Health

## 1. What flakiness is

A flaky test passes and fails on the same code. It is not "a test that found a real intermittent
bug" — though that is also valuable. Flakiness is non-determinism in the test or environment.

**Cost:** teams stop trusting red builds, ignore real failures, and slow down releases.

---

## 2. Common causes

| Cause | Example | Fix |
|---|---|---|
| **Fixed sleeps** | `Thread.sleep(5000)` | Wait for a condition/state |
| **Race condition** | Assert before async propagation | Poll to eventual state |
| **Shared state** | Tests mutate the same user/row | Unique data, isolation |
| **Order dependence** | Test B needs Test A's data | Independent setup |
| **Unstable locator** | Dynamic ID/class | Role/test-id/stable selector |
| **Animation** | Click during transition | Wait for actionability |
| **Environment** | Shared DB, rate limits, network | Ephemeral env, stubs |
| **Time** | Time zone, clock skew, date boundaries | Inject clock/fixed time |
| **Randomness** | Random data hits an edge | Seed or constrain |
| **Resource leak** | Ports, containers, files | Cleanup and health checks |
| **Test framework** | Parallel config, static state | Thread-local/isolated state |

---

## 3. Detection

- Track per-test history, not just the latest run.
- Compute flaky rate: tests that both passed and failed on the same commit/version.
- Re-run failures in a separate job to distinguish flake from real failure.
- Tag quarantined tests.
- Report retried-but-passed separately.

**Flaky rate formula (one option):**

```text
flaky tests / tests run
```

or, per test:

```text
(number of flip-flops) / (number of runs)
```

The exact formula matters less than consistent tracking over time.

---

## 4. Quarantine policy

1. **Detect** — a test fails then passes on the same code.
2. **Quarantine** — move to a non-blocking job; keep it visible.
3. **Assign** — named owner, ticket, deadline.
4. **Diagnose** — reproduce with logs/traces; identify the cause.
5. **Fix** — code, wait, data, or environment.
6. **Return** — only after a stability window.
7. **Delete** — if it no longer provides value.

Do not let quarantine become a graveyard. Track quarantine age.

---

## 5. Retries

Retries are a **detector**, not a fix.

Good uses:

- Detect infrastructure blips.
- Expose intermittent failures in CI.
- Keep a non-critical suite from blocking on a known transient.

Bad uses:

- Hiding a race condition.
- Making a red suite green.
- Avoiding root cause.

If a test needs retries to pass, it is not reliable.

---

## 6. Waiting correctly

| Tool | Correct wait |
|---|---|
| Selenium | `WebDriverWait` + expected condition |
| Playwright | Auto-waiting locators + `expect.poll` / web-first assertions |
| REST Assured | Poll with Awaitility, not `Thread.sleep` |
| Kafka/async | Poll for state/event with timeout |
| Database | Poll for row/state, not fixed delay |
| CI | Wait for health/readiness, not a fixed sleep |

**Rule:** wait for the **condition**, not for time.

---

## 7. Isolation

- Fresh browser context/page per test (Playwright).
- New driver/profile per test or thread (Selenium).
- Unique data per test.
- No shared mutable static state.
- No order dependence.
- Clean database/schema/namespace per run where possible.
- Reset auth/session between tests.

---

## 8. Suite health metrics

| Metric | Definition | Target direction |
|---|---|---|
| **First-run pass rate** | Pass without retries | Up |
| **Flaky rate** | Tests with both pass/fail on same code | Down |
| **Suite duration p50/p95** | Feedback time | Down |
| **Queue time** | Wait for runners | Down |
| **Escaped defects** | Bugs found after release | Down |
| **Quarantine age** | Time in quarantine | Down |
| **Mean time to fix failure** | Detection → fix | Down |
| **Coverage trend** | Direction, not absolute | Context |
| **Change failure rate** | Deploys causing incidents | Down |
| **MTTR** | Restore time | Down |

Avoid: test count, lines of test code, assertion count.

---

## 9. Debugging a flaky test

1. Read the failure and the artifact (screenshot, trace, log, response).
2. Check whether the product or the test is non-deterministic.
3. Reproduce locally with the same seed/data/environment.
4. Run in a loop to measure frequency.
5. Inspect timing, shared state, order, and environment.
6. Fix the cause; do not add a sleep.
7. Run a stability window (e.g., 20–50 runs) before returning.

---

## 10. Suite design for speed

- Run cheap tests first.
- Parallelize independent tests.
- Shard by module/tag.
- Cache dependencies, not test results.
- Use API setup instead of UI setup.
- Keep E2E small.
- Move slow regression to nightly.
- Measure p95, not just average.

---

## 11. Flakiness checklist

- [ ] No fixed sleeps.
- [ ] Every wait is condition-based.
- [ ] Tests are independent and order-free.
- [ ] Data is unique/isolated.
- [ ] Browser/session state is reset.
- [ ] Async tests poll to eventual state.
- [ ] Time/randomness are controlled.
- [ ] Flaky rate is tracked per test.
- [ ] Quarantine has an owner and deadline.
- [ ] Retries are reported, not hidden.
