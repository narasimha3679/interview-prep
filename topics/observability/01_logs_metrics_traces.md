# 01 — Logs, Metrics, and Traces

## 1. Logs

**What:** timestamped events with context.

**Good log fields:**

```json
{
  "timestamp": "2026-11-03T10:15:30.123Z",
  "level": "ERROR",
  "service": "order-service",
  "traceId": "4bf92f3577b34da6a3ce929d0e0e4736",
  "spanId": "00f067aa0ba902b7",
  "correlationId": "test-run-123",
  "message": "Payment authorization failed",
  "orderId": "ord-123",
  "provider": "stripe",
  "status": 502,
  "durationMs": 1203
}
```

**Testing use:**

- Assert the error path logs a stable event/field.
- Assert no secrets/PII.
- Query by `correlationId` when a test fails.
- Check that a retry/failure was logged with the right context.

**Do not:** assert the full log string, log secrets, or use logs as the only signal.

---

## 2. Metrics

**What:** numeric time series with labels.

**Four golden signals:**

| Signal | Meaning | Example |
|---|---|---|
| **Latency** | Time to serve | p50/p95/p99 request duration |
| **Traffic** | Demand | requests/sec, messages/sec |
| **Errors** | Failure rate | 5xx %, failed events |
| **Saturation** | Resource pressure | CPU, memory, queue depth |

**Testing use:**

- Assert a metric is emitted with expected labels.
- Assert latency/error budget during canary.
- Assert queue depth/DLQ count.
- Assert business metric (orders created, payments captured).

**Rule:** metric labels must have low cardinality. Never label with user ID, order ID, or request ID;
use logs/traces for those.

---

## 3. Traces

**What:** a request's path across services, made of spans.

```text
Trace: checkout
├── API gateway (12ms)
├── order-service (85ms)
│   ├── order-db (8ms)
│   └── payment-service (60ms)
│       └── payment-provider (55ms)
└── notification-service (async)
```

Each span has:

- Name/operation.
- Start time and duration.
- Status (OK/error).
- Attributes (HTTP method, status, DB statement, messaging destination).
- Parent/child relationship.

**Testing use:**

- Assert the trace includes the expected downstream call.
- Assert the failing span has error status.
- Assert no unexpected retries/duplicate calls.
- Use trace duration to find N+1/slow dependencies.
- Link the test report to the trace ID.

---

## 4. Which pillar when

| Question | Use |
|---|---|
| Is the error rate elevated? | Metrics |
| Which service is slow? | Traces |
| Why did this request fail? | Logs + trace |
| Did the request reach the dependency? | Trace span |
| Is the queue backing up? | Metrics |
| What payload caused the error? | Logs (without PII) |
| Did the release regress latency? | Metrics + traces |
| Can I reproduce this exact request? | Correlation/trace ID |

**Order:** metrics → traces → logs.

---

## 5. Correlation IDs

Pass a unique ID from the test:

```java
String correlationId = "test-" + UUID.randomUUID();
given()
    .header("x-correlation-id", correlationId)
.when()
    .post("/api/orders")
.then()
    .statusCode(201);
```

Then query:

```text
logs:   correlationId = "test-..."
traces: traceId from the response header or log
```

Rules:

- Generate per test/request, not per suite.
- Propagate through HTTP headers and message attributes.
- Include in error responses (safe, non-sensitive).
- Do not use it as a metric label.

---

## 6. Synthetic checks

Synthetic checks are automated probes that run against production from outside.

Use for:

- Critical journey uptime.
- Login, search, checkout.
- Multi-region availability.
- Post-deploy validation.

Test the synthetic check itself: it should fail when the journey fails, and alert the right owner.

---

## 7. Observability checklist

- [ ] Every request has a correlation/trace ID.
- [ ] IDs propagate across HTTP and messaging.
- [ ] Errors include the ID in the response/log.
- [ ] Logs are structured and free of secrets/PII.
- [ ] Metrics cover latency, traffic, errors, saturation.
- [ ] Metric labels are low-cardinality.
- [ ] Traces include downstream calls and error status.
- [ ] Test reports link to the trace/log query.
- [ ] Synthetic checks cover critical journeys.
- [ ] Alerts have owners and runbooks.
