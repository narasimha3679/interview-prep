# 03 — Debugging Distributed Failures

## 1. The workflow

```text
1. Read the failing assertion and artifact.
2. Find the correlation/trace ID.
3. Check metrics: is this isolated or systemic?
4. Open the trace: which span failed or was slow?
5. Read the logs for that span/service.
6. Reproduce with the same inputs/environment.
7. Decide: product bug, test bug, environment, or dependency.
8. Fix and add a regression test at the right level.
```

Do not start by re-running the test. Start with the evidence.

---

## 2. Triage questions

| Question | Where to look |
|---|---|
| Is it one request or many? | Metrics/error rate |
| Which service failed? | Trace error span |
| Was it slow or wrong? | Span duration/status |
| Did the dependency respond? | Downstream span/log |
| Was the request authenticated? | Gateway/auth span/log |
| Was the payload invalid? | Logs/request body (sanitized) |
| Was it a deploy? | Release/version metadata |
| Was it a platform incident? | Cloud Service Health |

---

## 3. Common failure signatures

### Timeout

- Trace: upstream span long; downstream span missing/timeout.
- Check: timeout config, dependency latency, retries, circuit breaker.
- Test: stub a slow dependency and assert the timeout behavior.

### 5xx from upstream

- Trace: downstream span status 5xx.
- Check: upstream logs, deploy, rate limit, payload.
- Test: stub 5xx and assert error mapping/degradation.

### Auth failure

- Trace: gateway/auth span 401/403.
- Check: token expiry, scope, audience, clock skew, secret rotation.
- Test: expired/wrong-scope token.

### Data mismatch

- Trace: successful spans, wrong value.
- Check: serialization, DB read, cache staleness, eventual consistency.
- Test: poll to eventual state; assert schema.

### Intermittent only under load

- Metrics: latency/saturation/queue depth.
- Check: connection pool, thread pool, DB locks, retries, GC.
- Test: load test with realistic concurrency.

### Works in staging, fails in production

- Check: config, secrets, identity, network, data shape, version, feature flags, DNS/TLS.
- Compare artifact digest and environment settings.

---

## 4. Reading a trace

```text
POST /checkout                      200  1.2s
├── auth.validate                   200   12ms
├── cart.get                        200   25ms
├── inventory.reserve               200   80ms
├── payment.authorize               502  1.0s   <-- error
│   └── provider.charge             502  0.9s
└── order.create                    skipped
```

Interpretation:

- The failure is in payment authorization.
- The provider call is the slow/failing span.
- The order was not created (good: no partial order).
- Next: provider logs, retry policy, circuit breaker, idempotency.

---

## 5. Test-side evidence

Make every test produce:

| Test type | Evidence |
|---|---|
| API | Request/response, status, correlation ID, trace link |
| UI | Screenshot, video, trace, console/network logs |
| Mobile | Screenshot/video, device logs, app version |
| Performance | Latency percentiles, error rate, throughput, resource graphs |
| Release validation | Version, environment, metric snapshot, trace links |

If the evidence is not in the report, the failure will be re-run blindly.

---

## 6. Root-cause categories

| Category | Example | Fix |
|---|---|---|
| **Product bug** | wrong calculation | code fix + regression test |
| **Test bug** | bad wait/assertion | fix test |
| **Test data** | shared/stale data | unique/isolated data |
| **Environment** | config/network/quota | fix env/IaC |
| **Dependency** | upstream outage | retry/degrade/coordinate |
| **Platform** | cloud incident | wait/retry/failover |
| **Flaky infrastructure** | runner/device | stabilize infra |

Classify before fixing; otherwise you "fix" the wrong thing.

---

## 7. Debugging checklist

- [ ] Failure artifact captured.
- [ ] Correlation/trace ID available.
- [ ] Metrics checked for scope.
- [ ] Trace inspected for the failing span.
- [ ] Logs queried by ID.
- [ ] Environment/version compared.
- [ ] Root-cause category identified.
- [ ] Regression test added at the right level.
- [ ] Evidence linked in the ticket.
