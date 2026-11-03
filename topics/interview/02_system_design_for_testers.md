# 02 — System Design for Testers

The question is usually: **"How would you test this system?"** They want to see how you reason, not
whether you memorized a tool list.

---

## The answer framework

```text
1. Clarify the architecture and data flow.
2. Name the top risks.
3. Place tests at the cheapest effective level.
4. Cover failure modes and non-functional risks.
5. State trade-offs and what you would not test E2E.
```

---

## Step 1 — Clarify

Ask:

- What are the components and who owns them?
- Sync or async? HTTP, gRPC, GraphQL, events?
- What is the source of truth?
- What are the auth/tenant boundaries?
- What is the deployment model?
- What is the SLO/risk?

Do not start listing tools before you understand the flow.

---

## Step 2 — Name the risks

Rank:

- **P0:** money, auth, data loss, safety, irreversible actions.
- **P1:** core flow, checkout, order state.
- **P2:** secondary features.
- **P3:** cosmetic.

Example: "For checkout, the top risks are duplicate charge, wrong total, lost order, oversell, and
auth bypass."

---

## Step 3 — Place tests

| Risk | Level |
|---|---|
| Calculation/business rule | Unit |
| Persistence/transaction | Component |
| Service compatibility | Contract |
| API behavior/auth | API |
| Visual/navigation/accessibility | UI |
| Critical journey | E2E (few) |
| Load/latency | Performance |
| Failure behavior | Resilience |

Say why: "I wouldn't test tax calculation through the UI; a unit test gives faster, more precise
feedback."

---

## Step 4 — Cover failure modes

For each dependency:

- Timeout.
- 4xx/5xx.
- Malformed response.
- Slow response.
- Duplicate/out-of-order event.
- Partial failure.
- Rate limit.
- Auth failure.

For each write:

- Idempotency.
- Transaction boundary.
- Duplicate detection.
- Compensating action.
- Audit trail.

---

## Step 5 — State trade-offs

Examples:

- "I'd keep E2E to three critical journeys; the rest is contract/component."
- "I'd stub the external payment provider for deterministic tests, then run a small sandbox
  integration suite."
- "I'd test the async path by polling to eventual state, not by sleeping."
- "I wouldn't load-test every PR; I'd run a smoke load on merge and full load nightly."

---

## Example answer: URL shortener

**Architecture:** API → database; cache; analytics events; redirect path.

**Risks:** wrong redirect, collision, expired link, abuse/rate limit, analytics loss, latency.

**Tests:**

- Unit: slug generation, collision handling, expiry.
- Component: DB uniqueness, cache invalidation.
- API: create/read/redirect, invalid slug, expired, auth, rate limit.
- Contract: analytics event schema.
- E2E: create → redirect → analytics event.
- Performance: redirect p99 under load.
- Resilience: cache down → DB fallback.

**Trade-off:** redirect is latency-critical; I'd cache aggressively and test the fallback, not just
the happy path.

---

## Example answer: marketplace checkout

**Risks:** duplicate charge, wrong total, oversell, lost order, auth bypass, tax/shipping error.

**Tests:**

- Unit: totals, discounts, tax, currency rounding.
- Component: inventory reservation concurrency, order state transitions.
- API: payment auth/decline/timeout, idempotency, auth.
- Contract: payment provider/event schemas.
- E2E: cart → payment → order confirmation (few).
- Async: order → fulfillment → notification, DLQ.
- Performance: peak checkout.
- Resilience: payment provider slow/down.

**Trade-off:** payment provider is external; I'd stub it for deterministic tests and run a small
sandbox integration suite for real wiring.

---

## Follow-up questions to expect

- "What would you automate first?"
- "What would you not test E2E?"
- "How would you test failure of X?"
- "How would you test data consistency?"
- "How would you test auth/tenant isolation?"
- "How would you test this at scale?"
- "How would you know it's safe to release?"

---

## System design checklist

- [ ] Clarified components/data flow.
- [ ] Ranked risks.
- [ ] Chose levels with rationale.
- [ ] Covered failure modes.
- [ ] Covered auth/tenant boundaries.
- [ ] Covered async/eventual consistency.
- [ ] Covered non-functional risks.
- [ ] Stated trade-offs.
- [ ] Defined release evidence.
