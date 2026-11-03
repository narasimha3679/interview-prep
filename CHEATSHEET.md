# Cheat Sheet

The highest-signal facts in this repository. Use this for the last hour before an interview, then go
back to the relevant pack for depth.

---

## 1. Java

1. `==` compares references; `.equals()` compares content when overridden.
2. `String` is immutable: string pool, security, thread safety, cached `hashCode`.
3. `StringBuilder` is mutable; use it in loops.
4. `ArrayList` is the default `List`; `LinkedList` wins for end inserts/removes, loses on `get(i)`.
5. `HashMap` = array of buckets + linked list/tree; default capacity 16, load factor 0.75.
6. Java 8+ converts a bucket to a red-black tree after 8 entries.
7. Override `equals()` and `hashCode()` together; equal objects must have equal hashes.
8. `ConcurrentHashMap` allows concurrent reads/writes; no `null` keys or values.
9. `ConcurrentModificationException` = structural change during iteration; use `removeIf` or iterator.
10. Checked exceptions must be declared/caught; unchecked are runtime.
11. `try-with-resources` closes `AutoCloseable` resources automatically.
12. Streams are lazy, single-use, and do not modify the source.
13. `map` is 1-to-1; `flatMap` is 1-to-many then flatten.
14. `Optional` is for return values, not fields/parameters; avoid `get()`.
15. `ThreadLocal` gives per-thread state; always `remove()` in pooled threads.
16. Executors must be shut down; bound queues and set timeouts on `Future.get`.
17. `CompletableFuture` defaults to the common pool; pass an executor for blocking work.
18. Generics are erased at runtime; `List<String>` and `List<Integer>` share a class.
19. PECS: Producer `extends`, Consumer `super`.
20. Records, sealed types, pattern matching, text blocks, and virtual threads are modern Java.
21. Heap is shared; stack is per-thread; metaspace holds class metadata.
22. G1 is the default collector on modern JDKs; measure before tuning.
23. `ThreadLocal`, static collections, unbounded caches, and listeners are common leak sources.
24. Factory, builder, strategy, adapter, decorator, DI, page object, repository are the useful patterns.
25. Prefer composition over inheritance for framework code.

---

## 2. Coding and SQL

26. Two Sum: HashMap of value → index, O(n).
27. Longest substring without repeats: sliding window + last-seen map, O(n).
28. Merge intervals: sort by start, scan and merge.
29. Group anagrams: sorted string or frequency key.
30. Top K frequent: HashMap + heap or bucket sort.
31. Binary search: `low <= high`, `mid = low + (high - low) / 2`.
32. Reverse linked list: prev/current/next pointers.
33. Valid parentheses: stack.
34. Always state complexity and edge cases: empty, one, duplicates, negative, overflow.
35. SQL: `WHERE` filters rows; `HAVING` filters groups.
36. `COUNT(*)` counts rows; `COUNT(column)` ignores NULL.
37. `NULL = NULL` is unknown; use `IS NULL`.
38. `NOT IN` with NULL can return no rows.
39. Window functions: `ROW_NUMBER`, `RANK`, `SUM OVER`, `PARTITION BY`.
40. Keyset pagination is stable under inserts; offset pagination can skip/duplicate.
41. Composite index leftmost prefix matters; check the query plan.
42. Transactions: know isolation levels and lost-update/deadlock behavior.
43. Use `EXPLAIN ANALYZE` on critical queries; compare estimated vs actual rows.
44. Parameterize queries; never concatenate input.

---

## 3. UI automation

45. Implicit wait is global and presence-only; explicit wait is targeted and condition-based.
46. Never mix implicit and explicit waits; timeouts compound unpredictably.
47. Locator priority: role/test-id → id/name → CSS → XPath.
48. XPath can match text/ancestors/functions; CSS cannot.
49. POM: one class per page; locators + actions inside; tests hold behavior.
50. Stale element = found then re-rendered; re-locate.
51. Click intercepted = something covers the element; wait for actionability.
52. `ThreadLocal<WebDriver>` for parallel Selenium; remove on quit.
53. Playwright creates a fresh browser context/page per test.
54. Playwright auto-waits and retries web-first assertions; no fixed sleeps.
55. Playwright fixtures are dependency injection for page objects.
56. Trace = replayable actions + DOM + network; capture on failure.
57. Accessible selectors: role, label, placeholder, test-id.
58. Cross-browser: Chromium, Firefox, WebKit; run all on nightly, one on PR.
59. Mobile: native vs hybrid vs mobile web; Appium for native/hybrid.
60. Appium contexts: `NATIVE_APP` vs `WEBVIEW_*`; switch explicitly.
61. Mobile test matrix: OS versions, manufacturers, screen sizes, locales, network.
62. Real devices for release confidence; emulators for fast feedback.
63. Test deep links, push, permissions, offline, background/foreground, install/upgrade.

---

## 4. REST and API automation

64. RestAssured: `given()` = request setup incl. body; `when()` = method/endpoint; `then()` = assertions.
65. Specs are the POM of API testing: base URI, headers, timeouts, logging.
66. No assertions in API clients; clients do HTTP, steps/tests assert.
67. `TestContext` shares state between Cucumber steps; reset per scenario.
68. Config: externalize URL/timeouts/keys; system properties override files.
69. Status codes: 200 OK, 201 Created + Location, 204 No Content, 400 validation, 401 authn, 403 authz, 404 missing, 409 conflict, 429 rate limit, 5xx server.
70. GET/PUT/DELETE should be idempotent; POST is not.
71. Idempotency key: same key + same body → one effect; same key + different body → conflict.
72. Pagination: cap page size, test first/last/beyond-end/invalid cursor.
73. Schema validation catches drift; full-payload equality is brittle.
74. Contract testing verifies provider compatibility without running both services.
75. Testcontainers = real dependency in Docker; WireMock = stub a neighbor.
76. Async: poll to eventual state with timeout; test duplicates, ordering, DLQ.
77. Resilience: timeout, retry with backoff, circuit breaker, graceful degradation.

---

## 5. GraphQL and gRPC

78. GraphQL usually returns HTTP 200 with `errors[]` and partial `data`; assert both.
79. Query reads, mutation writes, subscription streams.
80. Use variables, not string concatenation.
81. Fragments reuse fields; aliases query the same field with different arguments.
82. Nullability: non-null failure bubbles to nearest nullable parent.
83. Breaking schema change: remove/rename field, change type, optional → required, remove enum value.
84. Safe change: add field, add optional argument, add enum value, deprecate.
85. N+1: nested query can cause one resolver call per parent; test query count/latency.
86. Auth can be field-level; test owner/non-owner/admin/tenant/expired token.
87. Cost controls: depth limit, complexity limit, pagination cap, persisted queries, rate limit.
88. gRPC uses HTTP/2 + Protobuf; `.proto` is the contract.
89. gRPC call types: unary, server streaming, client streaming, bidirectional.
90. gRPC status codes: `OK`, `INVALID_ARGUMENT`, `NOT_FOUND`, `UNAUTHENTICATED`, `PERMISSION_DENIED`, `RESOURCE_EXHAUSTED`, `DEADLINE_EXCEEDED`, `UNAVAILABLE`.
91. Always set a deadline; test timeout/cancellation.
92. Never reuse a Protobuf field number; reserve removed numbers.
93. Streaming tests must consume/close/cancel and bound with a timeout.

---

## 6. Spring Boot and backend

94. Layers: controller (HTTP) → service (business) → repository (persistence).
95. DTOs at the boundary; entities stay internal.
96. Bean Validation at the boundary; global handler returns a stable error model.
97. `@Transactional` on public service methods; unchecked exceptions roll back by default.
98. Lazy loading works inside the transaction; N+1 is a query-count problem.
99. Fix N+1 with `JOIN FETCH`, `@EntityGraph`, batch fetching, or DTO projection.
100. Use Flyway/Liquibase; expand/contract for zero-downtime migrations.
101. `@WebMvcTest` for controllers, `@DataJpaTest` for repositories, `@SpringBootTest` for wiring.
102. Testcontainers for real DB dialect/migrations; H2 is not production parity.
103. WireMock for downstream success/4xx/5xx/timeout/malformed.
104. Actuator health/readiness is a release-validation target.
105. A good API returns 201 + Location on create, 204 on delete, and field-level validation errors.
106. Idempotency, pagination caps, timeouts, retries, and auth are part of the backend contract.

---

## 7. CI/CD and release validation

107. Pipeline order: lint/compile/unit → component → contract → API → UI smoke → E2E.
108. Build once, deploy many; promote the same artifact/digest.
109. Every stage needs artifacts: reports, logs, traces, screenshots, video.
110. Cache dependencies with lockfile keys; don't cache `node_modules`.
111. Matrix for browsers/JDKs/OSes; `fail-fast: false` to see all failures.
112. Concurrency groups prevent duplicate deploys; cancel superseded PR runs.
113. OIDC/workload identity beats long-lived cloud secrets.
114. Release validation ≠ regression: validate the candidate in the target environment.
115. Canary = small traffic slice; blue-green = switch traffic; rollback = tested path.
116. Database migrations must be backward compatible during rolling deploy.
117. Gate on risk: contracts, smoke, migrations, security, error budget.
118. Coverage is a smell, not a target.
119. Flaky tests: detect, quarantine with owner/deadline, fix root cause; retries are a detector.
120. Track escaped defects, change failure rate, MTTR, flaky rate, p95 duration.

---

## 8. Cloud and Azure

121. IaaS = you manage OS/runtime; PaaS = you manage app/config; SaaS = provider manages all.
122. Shared responsibility: provider secures the cloud; you secure identity, config, data, access.
123. Region = geography; availability zone = isolated datacenter group; region pair = recovery pairing.
124. Multi-AZ = datacenter resilience; multi-region = disaster recovery.
125. Composite SLA multiplies dependent SLAs: 99.9% × 99.9% ≈ 99.8%.
126. Azure hierarchy: tenant → management groups → subscriptions → resource groups → resources.
127. App Service = web/API PaaS; Container Apps = containers without K8s; AKS = Kubernetes; Functions = serverless.
128. App Service slots: deploy to staging, warm up, smoke, swap, rollback by swapping back.
129. Container Apps revisions: traffic split/canary, rollback by shifting weight.
130. Managed identity = Azure-managed credential; service principal = you manage the credential.
131. Key Vault stores secrets/keys/certs; access via managed identity/RBAC, not code.
132. RBAC = who can act; Azure Policy = what can exist/configured.
133. Resource group per PR + tags + expiry + budget + auto-delete = cost control.
134. Service Bus = reliable queue/topic; Event Grid = lightweight events; Event Hubs = high-volume streams.
135. Application Insights + Log Analytics = traces/logs; pass a correlation ID from tests.
136. Emulators for the fast loop; real cloud for release validation.
137. Test what you can't test locally: managed identity, private endpoints, quotas, region failover.

---

## 9. Test strategy

138. Risk = probability × impact; depth follows risk.
139. Test portfolio: unit → component → contract → API → UI → E2E; push coverage down.
140. Unit isolates one class; component uses real infra and stubs neighbors; integration wires real services.
141. Contract verifies compatibility; integration verifies real wiring; E2E verifies a journey.
142. Smoke = go/no-go; sanity = change-focused; regression = broad existing behavior.
143. Design techniques: equivalence partitioning, boundaries, decision tables, state transitions, pairwise, negative, exploratory, property-based.
144. A test case needs setup, action, oracle, data, cleanup.
145. Isolation beats cleanup: unique data, fresh context, ephemeral environment.
146. No shared mutable state; no order dependence; no fixed sleeps.
147. Flakiness causes: sleeps, races, shared state, unstable locators, environment, time, randomness.
148. Metrics: first-run pass rate, flaky rate, p95 duration, escaped defects, change failure rate, MTTR.
149. System design answer: clarify architecture → rank risks → place tests → cover failure modes → state trade-offs.
150. For every network call: timeout, retry, circuit breaker, fallback, error mapping, trace.
151. For every write: idempotency, transaction boundary, duplicate detection, audit trail.

---

## 10. Security, performance, observability

152. OWASP #1 = broken access control; test horizontal, vertical, and tenant boundaries.
153. 401 = unauthenticated; 403 = authenticated but forbidden.
154. IDOR: swap object IDs; expect 403/404, not another user's data.
155. Mass assignment: send privileged fields (`role`, `status`, `ownerId`); expect ignored/rejected.
156. Rate limit expensive endpoints; test 429 and per-tenant scope.
157. No secrets/PII in code, logs, reports, or prompts.
158. ZAP catches generic issues; it cannot understand business logic or complex authz.
159. Performance: p95/p99, throughput, error rate, saturation; averages hide the tail.
160. Little's Law: concurrency = throughput × latency.
161. Load = expected; stress = beyond; soak = long; spike = sudden.
162. Find the bottleneck with resource metrics: CPU, memory/GC, DB, pool, network, queue.
163. Logs = what happened; metrics = how much/how fast; traces = where it went.
164. Metrics detect, traces localize, logs explain.
165. Correlation ID from the test → trace/log query on failure.
166. OpenTelemetry: spans, context propagation (`traceparent`), collector, semantic conventions.
167. Keep error/slow traces; sample the happy path.
168. SLO = reliability target; error budget = allowed failure.
169. A test failure without evidence is a re-run, not a diagnosis.
170. Synthetic checks validate critical journeys after deploy.

---

## 11. Interview

171. STAR: Situation, Task, Action, Result; under 90 seconds.
172. Lead with engineering decisions, not tools.
173. Name the trade-off and what you rejected.
174. Quantify only if true.
175. Have one “what I’d do differently” per story.
176. Answer structure: what → why → trade-off → example.
177. If you don’t know: “I haven’t done X; here’s how I’d reason about it.”
178. Ask 3–5 questions about quality pain, release process, architecture, and success.
179. Your project pitch: problem → shape → decisions → evidence → next.
180. The through-line: fastest reliable signal for the risk being changed.

---

## Last-minute revision

1. Read sections 4, 7, 8, and 9.
2. Say your 2-minute project pitch.
3. Answer five questions from [`topics/interview/04_mock_qa.md`](topics/interview/04_mock_qa.md).
4. Read the release-validation checklist in [`topics/ci-cd/03_release_validation.md`](topics/ci-cd/03_release_validation.md).
5. Breathe. You know more than you think; the goal is structure, not recitation.
