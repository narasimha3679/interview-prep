# 02 — Test Design

## 1. Start with risk

**Risk = probability × impact.**

Rank:

- **P0:** money, auth, data loss, safety, irreversible actions, regulatory exposure.
- **P1:** core business flow, checkout, search, order state.
- **P2:** secondary features, edge UI, internal tools.
- **P3:** cosmetic, low-traffic, easily reversible.

Depth follows risk. A P0 path gets positive, negative, boundary, concurrency, and resilience tests.
A P3 label change gets a visual check.

---

## 2. Equivalence partitioning

Divide inputs into classes that should behave the same. Test one representative per class.

Example: age field

| Class | Values | Expected |
|---|---|---|
| Invalid low | `< 0` | reject |
| Valid | `0–17`, `18–65`, `66+` | accept |
| Invalid high | `> 120` | reject |

Do not test every value in a class; test the class boundaries and one representative.

---

## 3. Boundary value analysis

Bugs cluster at boundaries.

For `1 ≤ quantity ≤ 99`:

- `0` (invalid)
- `1` (valid min)
- `2` (just above min)
- `98` (just below max)
- `99` (valid max)
- `100` (invalid)

Also test null, empty, whitespace, max length, Unicode, and numeric overflow where relevant.

---

## 4. Decision tables

Use when output depends on combinations of conditions.

Example: discount eligibility

| Member | Coupon | Order ≥ 100 | Discount |
|---|---|---|---|
| No | No | No | 0 |
| Yes | No | No | 5% |
| No | Yes | No | 10% |
| Yes | Yes | No | 15% |
| No | No | Yes | 5% |
| Yes | Yes | Yes | 20% |

Test each rule, plus one invalid/conflicting combination.

---

## 5. State transition testing

Use for orders, payments, subscriptions, listings, workflows.

```text
DRAFT -> ACTIVE -> SOLD
  |        |
  +-> REMOVED
```

Test:

- Every valid transition.
- Every invalid transition (SOLD → ACTIVE should fail).
- Duplicate events/retries.
- Concurrent transitions.
- Terminal states.

---

## 6. Pairwise / combinatorial testing

When many parameters combine, full Cartesian product is too large. Pairwise covers every pair of
values at least once.

Example: browser × OS × locale × payment method. Pairwise reduces combinations while catching most
interaction bugs.

Use full combinations only for high-risk interactions.

---

## 7. Negative testing

Test what should **not** happen:

- Missing/invalid auth.
- Wrong role/tenant.
- Invalid/malformed input.
- Boundary overflow.
- Duplicate submission.
- Out-of-order events.
- Dependency timeout/error.
- Partial failure.
- Rate limit exceeded.
- Unsupported content type.
- Injection strings.

Negative tests often find more real defects than happy paths.

---

## 8. Exploratory testing

Structured exploration complements automation:

- Charter: what area, what risk, time-box.
- Heuristics: boundaries, CRUD, interruptions, concurrency, configuration, time zones.
- Notes: what you tried, what you saw, what you'd automate.
- Debrief: bugs, risks, coverage gaps.

Automation checks known behavior; exploration discovers unknown behavior.

---

## 9. Property-based testing

Instead of examples, assert a property over generated inputs.

Examples:

- Reversing a list twice returns the original.
- Sorting is idempotent.
- Encoding then decoding returns the original value.
- Total = sum of line items + tax.
- Pagination returns each item exactly once.

Tools: jqwik, QuickTheories, Hypothesis (Python), fast-check (JS).

---

## 10. Test case anatomy

A good test case states:

```text
Title:       Reject bid below reserve price
Risk:        P0 — seller loses money / auction integrity
Level:       API
Precondition: active auction with reserve 100.00
Action:      POST /bids { amount: 99.99 }
Expected:    422 + error code BID_BELOW_RESERVE
             no bid persisted, auction state unchanged
Data:        auction-{unique}, bidder-{unique}
Cleanup:     delete auction/bid via API
```

Include the **oracle** (how you know), the **data**, and the **cleanup**.

---

## 11. Coverage

| Coverage type | What it tells you | Limit |
|---|---|---|
| Line/branch | Code executed | Not behavior verified |
| Requirement | Requirements mapped to tests | Requirements may be incomplete |
| Risk | Risks covered | Subjective ranking |
| API/schema | Contract surface covered | Not runtime behavior |
| Code path | Branches exercised | Combinatorial explosion |

Use coverage to find gaps, not to declare quality.

---

## 12. Design checklist

- [ ] Risks are ranked.
- [ ] Every P0/P1 risk has a test at the cheapest effective level.
- [ ] Equivalence classes and boundaries are covered.
- [ ] Decision tables cover combination rules.
- [ ] State transitions include invalid and concurrent paths.
- [ ] Negative and partial-failure cases exist.
- [ ] Test data is unique and isolated.
- [ ] Each test has a clear oracle.
- [ ] Exploratory charters complement automation.
- [ ] Coverage gaps are reviewed, not gamed.
