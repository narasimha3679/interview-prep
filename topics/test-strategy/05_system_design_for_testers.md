# 05 — System Design for Testers

When you are asked "how would you test this architecture?", do not list tools. Walk the request path,
name the failure modes, and place a test at the cheapest level that can catch each one.

---

## 1. The review framework

For each component, ask:

1. **What does it own?** Data, behavior, contract.
2. **Who calls it?** Sync/async, internal/external.
3. **What can fail?** Timeout, error, partial write, duplicate, stale read.
4. **How would I know?** Logs, metrics, traces, response, state.
5. **Where do I test it?** Unit, component, contract, API, E2E.
6. **What is the blast radius?** One request, one tenant, the whole system.

---

## 2. Reference architecture

```text
Client (web/mobile)
   │
   ▼
API Gateway / BFF
   │  auth, rate limit, routing
   ├──> Listing Service ──> Listing DB
   ├──> Search Service  ──> Search Index
   ├──> Order Service   ──> Order DB
   │        │
   │        └──> Payment Service ──> Payment Provider
   └──> Notification Service <── Event Bus <── Order/Payment events
```

Cross-cutting: identity provider, config, secret store, observability, CI/CD, test environments.

---

## 3. Component-by-component test map

### API gateway / BFF

**Risks:** auth bypass, routing error, rate-limit bypass, header loss, timeout, wrong service.

**Tests:**

- Route each path to the right service.
- 401 vs 403.
- Token validation/expiry.
- Rate limit per user/tenant.
- Header/request-ID propagation.
- Timeout and upstream 5xx mapping.
- CORS and content-type handling.

### Service API

**Risks:** validation, status codes, business rules, serialization, pagination, idempotency.

**Tests:** API-level positive/negative, boundary, auth, error model, contract.

### Service logic

**Risks:** calculation, state transition, concurrency, idempotency.

**Tests:** unit + component with real DB.

### Database

**Risks:** schema drift, constraints, migrations, N+1, isolation, deadlocks.

**Tests:** repository/component with Testcontainers; migration tests; query-plan checks for critical
queries.

### Cache

**Risks:** stale data, stampede, eviction, key collision, tenant leak.

**Tests:** cache hit/miss, TTL, invalidation after write, fallback when cache is down, key includes
tenant.

### Message broker

**Risks:** duplicate, out-of-order, poison message, DLQ, lag, schema drift.

**Tests:** publish/consume, idempotency, ordering, retry, DLQ, event schema.

### External provider

**Risks:** timeout, 5xx, rate limit, malformed response, duplicate charge.

**Tests:** WireMock/stub for success, failure, timeout, malformed, retry, circuit breaker.

### Notification

**Risks:** missed notification, duplicate, wrong recipient, template error.

**Tests:** event → notification, deduplication, opt-out, template rendering, provider failure.

### Identity

**Risks:** token forgery, expired token, wrong scope, tenant confusion, privilege escalation.

**Tests:** auth matrix, token refresh, revocation, scope/role checks, cross-tenant access.

### Observability

**Risks:** no trace, missing correlation ID, PII in logs, alert missing.

**Tests:** correlation ID propagation, log fields, metric emitted, trace spans, alert fires.

---

## 4. Failure-mode checklist

For every network call:

- [ ] Timeout set.
- [ ] Retry policy (idempotent only).
- [ ] Backoff/jitter.
- [ ] Circuit breaker.
- [ ] Fallback/degradation.
- [ ] Error mapping.
- [ ] Metric/log/trace.
- [ ] Test for slow, down, malformed, and partial responses.

For every write:

- [ ] Idempotency.
- [ ] Transaction boundary.
- [ ] Duplicate detection.
- [ ] Partial failure handling.
- [ ] Compensating action/outbox.
- [ ] Retry safety.
- [ ] Audit trail.

For every read:

- [ ] Auth/tenant scope.
- [ ] Staleness/consistency expectation.
- [ ] Pagination/filter limits.
- [ ] Cache behavior.
- [ ] Empty/not-found behavior.

---

## 5. Example: "How would you test checkout?"

**Risks:** wrong charge, duplicate charge, lost order, oversell, tax/shipping error, auth bypass,
partial failure.

**Test map:**

| Risk | Level | Test |
|---|---|---|
| Cart calculation | Unit | totals, discounts, tax, currency rounding |
| Inventory reservation | Component | concurrent reservations, oversell |
| Payment authorization | API + stub | success, decline, timeout, duplicate |
| Order state | Component/API | valid/invalid transitions, idempotency |
| Checkout journey | E2E (few) | cart → payment → order confirmation |
| Async fulfillment | Integration | event → fulfillment, DLQ, retry |
| Auth | API | guest vs user, cross-account access |
| Load | Performance | peak checkout, payment provider latency |

**Oracle:** order total, payment state, inventory count, order status, notification, audit record.

---

## 6. Example: "How would you test search?"

**Risks:** stale index, relevance regression, pagination gaps, filter mismatch, zero results,
localization, personalization leak.

**Tests:**

- Indexing pipeline: create/update/delete listing → search reflects it (poll).
- Query: term, filters, sort, pagination, no results.
- Relevance: golden query set with expected top-N (ranking metric).
- Isolation: tenant/personalization does not leak.
- Performance: p95 latency under load.
- Resilience: search index down → fallback/degradation.

---

## 7. Architecture review questions

1. What is the source of truth for each piece of data?
2. Which calls are synchronous vs asynchronous?
3. What happens if each dependency is slow, down, or returns bad data?
4. Where are the transaction boundaries?
5. How is idempotency enforced?
6. How does data propagate, and how stale can it be?
7. Where are auth and tenant boundaries enforced?
8. What is cached, and how is it invalidated?
9. How do you observe a single request end to end?
10. How do you deploy and roll back?
11. What is the blast radius of a failure?
12. Which risks are not covered by the current tests?

---

## 8. Interview structure

Use this sequence:

1. **Clarify the architecture** — components, data flow, sync/async.
2. **Name the top risks** — money, auth, data loss, availability.
3. **Place tests by level** — unit/component/contract/API/UI/E2E.
4. **Cover failure modes** — timeout, retry, duplicate, partial failure.
5. **Cover non-functional** — performance, security, observability.
6. **State the trade-offs** — what you would not test E2E and why.

**One-line close:**

> "I would not try to prove this entire architecture through E2E. I'd contract-test the interfaces,
> component-test each service with real infrastructure, and keep E2E for the few journeys where the
> integration itself is the risk."
