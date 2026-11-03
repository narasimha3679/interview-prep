# Observability for SDETs — Study Pack

How to use logs, metrics, and traces to diagnose failures and to make tests produce evidence instead
of noise.

**Last verified:** November 2026.

---

## 60-second cram

1. **Three pillars:** logs (what happened), metrics (how much/how fast), traces (where the request
   went).
2. **A correlation ID is the thread.** Pass it from the test through every service; query it when a
   test fails.
3. **Traces beat logs for distributed failures.** A trace shows which span was slow or failed; logs
   tell you why.
4. **Metrics detect, traces localize, logs explain.** Use them in that order.
5. **Tests should assert observability, not just behavior.** Did the request emit a metric? Did the
   trace include the downstream call? Did the log include the correlation ID?
6. **Cardinality kills metrics.** Do not put user IDs or request IDs in metric labels.
7. **Sample traces deliberately.** Keep errors and slow requests; sample the rest.
8. **A failure artifact should be one click away.** Screenshot/video for UI, trace ID/log link for
   API, metrics snapshot for release validation.

**The one line that shows depth:**

> "I don't just assert the response; I assert that the request is observable. If a test fails in CI, I
> want a correlation ID that takes me to the exact trace and the service that actually broke."

---

## File map

| File | What it gives you |
|---|---|
| `01_logs_metrics_traces.md` | The three pillars and how to use them |
| `02_opentelemetry.md` | Spans, context propagation, instrumentation |
| `03_debugging_distributed_failures.md` | A practical failure-diagnosis workflow |
| `04_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Log** | Timestamped event record | Assert key fields, not full text |
| **Metric** | Numeric time series | Assert emitted/within budget |
| **Trace** | Request path across services | Assert spans/dependencies |
| **Span** | One operation in a trace | Parent/child, duration, status |
| **Correlation ID** | ID shared by a request's events | Pass from test; query on failure |
| **Trace ID** | ID for a distributed trace | Link test failure to trace |
| **Cardinality** | Number of unique label values | Keep low; don't label with user/request IDs |
| **SLO** | Service-level objective | Release gate/error budget |
| **Error budget** | Allowed unreliability | Decide release/rollback |
| **Synthetic check** | Automated production probe | Post-deploy validation |

---

## Traps

1. **Logging without a correlation ID.** You cannot follow a request across services.
2. **Asserting exact log strings.** Brittle; assert structured fields.
3. **High-cardinality metric labels.** Metrics backend melts.
4. **Tracing only the entry service.** The downstream failure is invisible.
5. **No error status on spans.** The trace looks green while the request failed.
6. **Sampling away errors.** Always keep error/slow traces.
7. **PII in logs.** Compliance risk.
8. **No link from the test report to the trace.** Debugging takes longer than the fix.
9. **Dashboard without an owner.** Nobody looks at it.
10. **Alert without a runbook.** The on-call person cannot act.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, draw the three pillars for a request path.
- **30 minutes:** `02`, explain trace context propagation.
- **30 minutes:** `03`, walk a failure scenario end to end.
- **20 minutes:** `04`, answer out loud.
