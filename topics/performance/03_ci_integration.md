# 03 — CI Integration

## 1. What belongs in CI

| Stage | Test | Duration |
|---|---|---|
| PR | Smoke performance (1–5 users, 1 endpoint) | < 2 min |
| Merge | Small load test with thresholds | 5–10 min |
| Nightly | Full load/soak on staging | 30–120 min |
| Pre-release | Capacity/stress + resilience | Hours |
| Post-deploy | Synthetic latency/error checks | Minutes |

Do not run a full benchmark on every PR; it is slow, noisy, and expensive.

---

## 2. Performance budgets

Define thresholds as code:

```js
export const options = {
  thresholds: {
    http_req_duration: ['p(95)<300', 'p(99)<800'],
    http_req_failed: ['rate<0.001'],
    checks: ['rate>0.99'],
  },
};
```

A failed threshold fails the build. Store the baseline and compare:

```text
p95 must not regress > 10% vs the last main baseline
error rate must stay < 0.1%
throughput must be >= 200 req/s
```

---

## 3. Environment

- Dedicated performance environment or isolated namespace.
- Same instance size/config as production.
- Production-like data volume.
- No other tests running.
- Warm-up before measurement.
- Load generator outside the system under test.
- Record version, config, data size, and run parameters.

---

## 4. CI pipeline

```text
build
  -> deploy to perf environment
  -> warm up
  -> run smoke load
  -> run load profile
  -> assert thresholds
  -> publish report + metrics snapshot
  -> tear down
```

Artifacts:

- k6/JMeter/Gatling report.
- Latency/throughput/error graphs.
- Server CPU/memory/GC/DB metrics.
- Run parameters and version.
- Baseline comparison.

---

## 5. Handling noise

Performance runs are noisy. Reduce false alarms:

- Run on a stable environment.
- Use a fixed load profile.
- Warm up.
- Compare against a baseline with a tolerance.
- Run 2–3 times and take the median for critical gates.
- Separate "regression detected" from "environment was noisy."
- Do not auto-fail on a single outlier without context.

---

## 6. Performance regression workflow

1. Threshold fails.
2. Check the run was valid (environment, load generator, data).
3. Compare with baseline: latency, throughput, error rate, resources.
4. Profile the bottleneck (CPU, DB, GC, network).
5. Identify the change (commit range).
6. Fix or approve with evidence.
7. Update the baseline if the change is intentional.

---

## 7. CI checklist

- [ ] Smoke load on PR; full load nightly/pre-release.
- [ ] Thresholds are code and fail the build.
- [ ] Environment is isolated and production-like.
- [ ] Warm-up runs before measurement.
- [ ] Load generator is monitored.
- [ ] Server metrics are collected.
- [ ] Results are published as artifacts.
- [ ] Baseline comparison exists.
- [ ] Flaky/noisy runs are investigated, not ignored.
- [ ] Performance regressions have an owner.
