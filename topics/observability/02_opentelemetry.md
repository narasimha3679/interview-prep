# 02 — OpenTelemetry

## 1. What it is

OpenTelemetry (OTel) is a vendor-neutral standard for traces, metrics, and logs. It defines:

- **API/SDK** — instrument your code.
- **Context propagation** — pass trace context across services.
- **Collector** — receive, process, and export telemetry.
- **Semantic conventions** — standard attribute names.

You instrument once and export to Jaeger, Tempo, Zipkin, Datadog, Azure Monitor, etc.

---

## 2. Trace context

The W3C Trace Context standard uses two headers:

```text
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
tracestate:  ...
```

- `trace-id` — shared by the whole trace.
- `parent-id` / `span-id` — current span.
- Flags — sampled or not.

The service must propagate these headers to downstream HTTP calls and message attributes.

**Test:** send a `traceparent`, assert the downstream service receives the same trace ID.

---

## 3. Spans

```java
Span span = tracer.spanBuilder("createOrder")
        .setAttribute("order.type", "marketplace")
        .startSpan();

try (Scope scope = span.makeCurrent()) {
    // work
    span.setStatus(StatusCode.OK);
} catch (Exception e) {
    span.recordException(e);
    span.setStatus(StatusCode.ERROR, e.getMessage());
    throw e;
} finally {
    span.end();
}
```

Good span attributes:

- HTTP method, route, status.
- DB system, statement (sanitized), rows.
- Messaging system, destination, message ID.
- RPC service/method.
- Error type/message.

Do not put secrets or PII in attributes.

---

## 4. Auto-instrumentation

Common agents/libraries instrument:

- HTTP clients/servers.
- JDBC.
- Kafka/RabbitMQ.
- Redis.
- gRPC.
- Spring Boot (Micrometer Tracing).
- Node.js/Express.
- Python requests/Flask.

**Testing implication:** auto-instrumentation is code. Verify the span exists, has the right name,
status, and attributes. A missing span can hide a dependency failure.

---

## 5. Collector pipeline

```text
App SDK -> OTel Collector -> exporter -> backend
```

Collector responsibilities:

- Batching.
- Sampling.
- Attribute processing/redaction.
- Routing to multiple backends.
- Retry/buffering.

**Test:** a failure trace reaches the backend with the expected attributes; PII is redacted.

---

## 6. Sampling

- **Head sampling** — decide at trace start; cheap but may miss rare errors.
- **Tail sampling** — decide at trace end; can keep all errors/slow traces, but needs buffering.

**Rule:** always keep error and slow traces; sample the happy path. If a test fails, the trace must
not be sampled away.

---

## 7. Testing OpenTelemetry

| Test | Assert |
|---|---|
| Context propagation | Downstream receives same trace ID |
| Span creation | Expected span name/attributes |
| Error status | Failed span has `ERROR` status and exception event |
| Dependency span | DB/HTTP/messaging span exists |
| Sampling | Error trace retained |
| Redaction | No secrets/PII in attributes |
| Collector | Trace reaches backend |

Use an in-memory exporter in unit tests:

```java
InMemorySpanExporter exporter = InMemorySpanExporter.create();
SdkTracerProvider provider = SdkTracerProvider.builder()
        .addSpanProcessor(SimpleSpanProcessor.create(exporter))
        .build();
```

Then assert spans without a real backend.

---

## 8. OpenTelemetry traps

1. **Creating spans but not propagating context.** The trace breaks at the first hop.
2. **Not setting error status.** The trace looks successful.
3. **High-cardinality attributes.** Backend cost/limits.
4. **PII in attributes.** Compliance risk.
5. **Sampling away failures.** You cannot debug what you did not keep.
6. **No collector.** Direct export is brittle and hard to route.
7. **Instrumenting only the entry service.** Downstream failures invisible.
8. **Not testing the instrumentation.** Broken telemetry is invisible until an incident.

---

## 9. OTel checklist

- [ ] Trace context propagates across HTTP and messaging.
- [ ] Entry and downstream spans exist.
- [ ] Failed spans have error status and exception events.
- [ ] Attributes use semantic conventions.
- [ ] No secrets/PII.
- [ ] Error/slow traces are retained.
- [ ] Collector routes to the backend.
- [ ] Instrumentation has tests.
- [ ] Test reports include trace IDs.
