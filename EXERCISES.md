# Exercise Bank

Practical tasks that force you to produce something, not just read. Each task has a “done when” check.
Use these with [`PROGRESS.md`](PROGRESS.md); a topic pack’s own exercises take priority when it has
them.

---

## Foundations

### Java

1. **LRU cache.** Implement an LRU cache with `LinkedHashMap` and a fixed capacity.
   *Done when:* eviction order is correct and a test covers get/put/evict.
2. **Thread-safe counter.** Write a counter that is correct under 10 threads. Then fix it with
   `AtomicInteger`.
   *Done when:* a test runs 10 threads × 10,000 increments and asserts the final value.
3. **Stream grouping.** Group a list of orders by status and compute total value per status.
   *Done when:* the output matches a hand-calculated expected map.
4. **equals/hashCode.** Create a `Money` value object used as a `HashMap` key.
   *Done when:* two equal amounts retrieve the same entry.
5. **CompletableFuture.** Call two independent APIs in parallel, combine the results, and add a
   timeout.
   *Done when:* a test proves both calls ran and a slow call times out.

### Collections

6. **HashMap internals.** Explain what happens on `put`, collision, treeification, and resize.
   *Done when:* you can draw it from memory.
7. **Fail-fast.** Reproduce `ConcurrentModificationException`, then fix it with `removeIf`.
   *Done when:* both versions are runnable and the difference is explained.
8. **Comparator.** Sort a list by two fields, nulls last.
   *Done when:* the test covers equal first fields and null values.

### SQL

9. **Top N per group.** Return the top 3 orders per user by total.
   *Done when:* the query uses a window function and handles ties deliberately.
10. **Duplicates.** Find duplicate emails and keep the latest row per email.
    *Done when:* the query returns one row per email.
11. **Keyset pagination.** Compare offset vs keyset pagination while inserting rows between pages.
    *Done when:* you can explain the skip/duplicate behavior.
12. **Query plan.** Run `EXPLAIN ANALYZE` on a filtered/sorted query and add an index.
    *Done when:* the plan changes from a sequential scan to an index scan.

### Coding

13. **Two Sum.** Implement the HashMap solution and state complexity.
14. **Longest substring.** Implement the sliding-window solution.
15. **Merge intervals.** Sort and merge overlapping intervals.
16. **Valid parentheses.** Use a stack and handle all bracket types.

---

## UI automation

17. **Page object.** Build a page object for a login page with role/test-id locators.
    *Done when:* a spec can log in without knowing selectors.
18. **Fix a flaky test.** Take a test with a fixed sleep and replace it with a condition wait.
    *Done when:* it passes 20 consecutive runs.
19. **Playwright fixture.** Create a typed fixture that injects two page objects.
    *Done when:* a spec uses both without constructing them.
20. **Mobile viewport.** Run a checkout flow at 390×844 and 320×568.
    *Done when:* the primary action is visible and tappable in both.
21. **Accessibility.** Run axe on a critical page and fix one serious violation.
    *Done when:* the scan is clean for that rule and a keyboard pass still works.
22. **Keyboard-only.** Complete a critical journey with Tab/Enter/Space only.
    *Done when:* focus is always visible and no trap exists.

---

## API automation

23. **RestAssured CRUD.** Write create → read → update → delete tests with schema validation.
    *Done when:* each test is independent and uses unique data.
24. **Auth matrix.** Test anonymous, owner, non-owner, admin, expired token.
    *Done when:* 401 vs 403 is correct for every case.
25. **Pagination boundaries.** Test first/last/beyond-end/invalid cursor.
    *Done when:* no duplicates or gaps across pages.
26. **GraphQL query.** Write a query with variables, a fragment, and an alias.
    *Done when:* the response shape matches the selection.
27. **GraphQL error.** Assert `data` and `errors[]` for a missing object.
    *Done when:* HTTP 200 alone is not treated as success.
28. **GraphQL nullability.** Force a non-null field failure and trace the null bubbling.
    *Done when:* you can name the parent that became null and why.
29. **gRPC unary.** Call a unary method and assert `OK` and `NOT_FOUND`.
30. **gRPC streaming.** Consume a server stream, assert order/count, then cancel it.

---

## Backend

31. **Idempotency.** Add `Idempotency-Key` to the mini-service order creation.
    *Done when:* same key twice creates one order; same key + different body returns 409.
32. **Pagination cap.** Add a server-side max page size and test the boundary.
33. **Error model.** Add a stable error code and field errors for validation.
34. **Migration.** Add a Flyway migration and test it against a production-like schema.
35. **Testcontainers.** Run repository tests against PostgreSQL instead of H2.
    *Done when:* the same tests pass on both, and you can explain the difference.
36. **WireMock failure.** Stub a downstream timeout and assert the service degrades as designed.
37. **N+1.** Create a parent/child query and measure query count as parents grow.
    *Done when:* you can show the N+1 and the fix.

---

## Delivery and cloud

38. **Pipeline gates.** Build a CI pipeline with unit → API → UI smoke → artifact upload.
    *Done when:* a failing test blocks the build and the report is uploaded.
39. **Matrix.** Run the suite on two JDKs or two browsers with `fail-fast: false`.
40. **OIDC.** Write a GitHub Actions job that authenticates to Azure with OIDC, no client secret.
    *Done when:* you can explain the federated subject and minimum role.
41. **Bicep environment.** Provision a resource group with an App Service plan and app.
    *Done when:* you can deploy and delete it with two commands.
42. **Slot validation.** Deploy to a staging slot, smoke it, swap, then swap back.
    *Done when:* you can explain warm-up, slot settings, and rollback.
43. **Cost cleanup.** Add tags, a budget, and an expiry/delete step to a test environment.
44. **Canary.** Shift 10% traffic to a new revision, watch error rate/latency, then promote.
    *Done when:* you can state the rollback trigger.

---

## Quality

45. **Risk matrix.** For a checkout flow, list P0–P3 risks and the cheapest test level for each.
46. **Test design.** Design cases using equivalence partitioning, boundaries, and a decision table.
47. **Flaky policy.** Write a quarantine policy with owner, deadline, and evidence.
48. **Metrics.** Define first-run pass rate, flaky rate, p95 duration, and escaped defects.
49. **System design.** Answer “how would you test this architecture?” in five minutes.
50. **Release validation.** Write a pre-deploy/canary/post-deploy checklist for one service.

---

## Security and performance

51. **Auth matrix.** Build a table of endpoints × principals and test every cell.
52. **IDOR.** Swap an object ID and prove the API denies cross-user access.
53. **Rate limit.** Burst an endpoint and assert 429 with retry guidance.
54. **ZAP baseline.** Run a baseline scan against a local app and triage the findings.
55. **k6 smoke.** Write a k6 script with p95/error thresholds.
56. **Bottleneck.** Run a load test, collect CPU/DB/GC metrics, and name the first bottleneck.
57. **Soak.** Run a long test and look for memory growth or latency degradation.

---

## Observability

58. **Correlation ID.** Pass an ID from a test and find the request in logs/traces.
59. **Span assertion.** Use an in-memory exporter to assert span name, status, and attributes.
60. **Failure triage.** Given a trace, identify the failing span and the next diagnostic step.
61. **SLO.** Write one SLI/SLO and the error-budget policy for a critical endpoint.

---

## AI

62. **Golden set.** Build 20 questions with expected answers/sources for a RAG system.
63. **Retrieval test.** Assert the expected source appears in the top-k results.
64. **Faithfulness.** Score answers against retrieved evidence; calibrate a judge against humans.
65. **Prompt injection.** Add an indirect injection case and assert the guardrail holds.
66. **AI-assisted review.** Ask a model to review a test; accept/reject each suggestion with a reason.

---

## Interview

67. **2-minute pitch.** Present one project: problem → shape → decisions → evidence → next.
68. **STAR story.** Write and rehearse one reliability story under 90 seconds.
69. **Trade-off answer.** Explain why you did not test something E2E.
70. **Questions.** Prepare five questions about quality pain, release process, and success.

---

## How to use this bank

- Pick one task per study block.
- Write the expected result before running anything.
- Keep the artifact (test, query, diagram, checklist) in your notes.
- Mark it in [`PROGRESS.md`](PROGRESS.md).
- If a task exposes a gap, go back to the matching topic pack.
