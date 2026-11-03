# Glossary

Cross-topic vocabulary. Each entry is one line; follow the linked pack for depth.

---

## Testing and quality

| Term | Meaning |
|---|---|
| **Acceptance test** | A test that checks a requirement from the user/business perspective. |
| **Assertion** | A check that fails the test when the actual result differs from the expected result. |
| **Boundary value** | A value at the edge of an input range, where defects cluster. |
| **Contract test** | Verifies a provider still satisfies what a consumer needs, without running both. |
| **Coverage** | What code/requirements were exercised; a gap detector, not a quality target. |
| **E2E test** | A full journey through the real system; slow, expensive, few. |
| **Equivalence partitioning** | Split inputs into classes that should behave the same; test one per class. |
| **Escaped defect** | A defect found after release; a measure of test effectiveness. |
| **Flaky test** | Same code, different result; a trust problem, not a retry problem. |
| **Happy path** | The expected successful flow; necessary but not sufficient. |
| **Integration test** | Multiple real components wired together. |
| **Negative test** | A test of invalid input, failure, or forbidden behavior. |
| **Oracle** | How you know the result is correct. |
| **Pairwise testing** | Cover every pair of parameter values instead of the full Cartesian product. |
| **Property-based test** | Assert a property over generated inputs. |
| **Regression test** | Confirms existing behavior still works after a change. |
| **Risk-based testing** | Depth follows probability × impact. |
| **Smoke test** | Tiny go/no-go check after a deploy. |
| **State transition test** | Exercises valid/invalid moves between states. |
| **Test double** | A stub, mock, fake, or spy used in place of a real dependency. |
| **Test pyramid** | Many fast tests, fewer integration tests, very few E2E tests. |
| **Test strategy** | The risk map that decides what to test, where, and when. |
| **Unit test** | Isolates one class/function with collaborators replaced. |

---

## Automation

| Term | Meaning |
|---|---|
| **API client** | A class that performs HTTP calls; no assertions inside. |
| **Auto-waiting** | The tool waits for actionability/condition instead of a fixed sleep. |
| **Browser context** | Isolated cookies/storage/session inside a browser (Playwright). |
| **Driver** | The WebDriver/browser automation process. |
| **Fixture** | Setup/teardown + dependency injection for a test. |
| **Locator** | A selector used to find an element. |
| **Page Object Model** | One class per page/component; locators + user actions inside. |
| **Parallel-safe** | No shared mutable state; each test can run independently. |
| **Retry** | Re-run a failed test; a detector, not a fix. |
| **Suite** | A selected set of tests run together. |
| **Test data factory** | A helper that creates valid, unique test data. |
| **ThreadLocal** | Per-thread variable; used for drivers/context in parallel tests. |
| **Trace** | Replayable record of actions, DOM, and network on a UI test failure. |
| **Wait** | Explicit/implicit synchronization; prefer condition-based explicit waits. |

---

## API and protocols

| Term | Meaning |
|---|---|
| **GraphQL** | Typed query language; one endpoint, client-selected fields. |
| **gRPC** | RPC over HTTP/2 with Protobuf; unary and streaming calls. |
| **Idempotency** | A retried operation has the same effect as one operation. |
| **Idempotency key** | Client-supplied key used to deduplicate retried writes. |
| **JWT** | Signed token with claims; validate signature, expiry, audience, issuer, scope. |
| **OAuth 2.0 / OIDC** | Delegated authorization / identity layer; authorization code + PKCE. |
| **OpenAPI** | Machine-readable REST API contract. |
| **Pagination** | Splitting results into pages; offset or cursor/keyset. |
| **Protobuf** | Binary schema-driven serialization used by gRPC. |
| **REST** | Resource-oriented HTTP API style. |
| **Schema validation** | Assert the response matches a contract/schema, not just a few fields. |
| **SOAP** | XML-based messaging protocol; WSDL contract. |
| **Status code** | HTTP result code; 2xx/3xx/4xx/5xx. |
| **Webhook** | Provider-to-consumer HTTP callback; verify signature and replay protection. |

---

## Backend and data

| Term | Meaning |
|---|---|
| **ACID** | Atomicity, Consistency, Isolation, Durability. |
| **Cache invalidation** | Removing/updating stale cached data after a write. |
| **DTO** | Data transfer object at the API boundary. |
| **Entity** | Persistent domain object mapped to a database table. |
| **Eventual consistency** | Replicas/consumers converge over time; poll, don’t assert immediately. |
| **Flyway/Liquibase** | Versioned database migration tools. |
| **JPA/Hibernate** | Java persistence API/ORM. |
| **N+1** | One query for parents plus one per parent for a child association. |
| **Outbox pattern** | Reliably publish events alongside a database write. |
| **Saga** | Distributed transaction coordinated by events/compensation. |
| **Transaction** | Unit of work that commits or rolls back atomically. |
| **Idempotent consumer** | A consumer that can process duplicate messages safely. |

---

## Delivery and cloud

| Term | Meaning |
|---|---|
| **Artifact** | Immutable build output promoted across environments. |
| **Blue-green** | Two identical environments; switch traffic for release/rollback. |
| **Canary** | Small traffic slice to a new version before full rollout. |
| **CI/CD** | Continuous integration / continuous delivery. |
| **Composite SLA** | End-to-end availability across dependent services (multiplied). |
| **Ephemeral environment** | Short-lived environment provisioned per PR/test run. |
| **IaC** | Infrastructure as code (Bicep/Terraform/CloudFormation). |
| **Managed identity** | Platform-issued workload identity; no stored credential. |
| **OIDC federation** | Short-lived CI credentials trusted by a cloud provider. |
| **PaaS/IaaS/SaaS** | Service models with different management boundaries. |
| **Region/AZ** | Geographic area / isolated datacenter group within it. |
| **Release validation** | Validate the release candidate in the target environment. |
| **Rollback** | Return to the previous known-good version; must be tested. |
| **Service principal** | App identity with a credential you manage. |
| **Shared responsibility** | Provider secures the cloud; you secure identity/config/data. |
| **SLO/SLI** | Objective/target and indicator used to measure reliability. |

---

## Observability and performance

| Term | Meaning |
|---|---|
| **Correlation ID** | ID propagated across services to tie a request together. |
| **Error budget** | Allowed unreliability before a release freeze. |
| **Log** | Timestamped event record. |
| **Metric** | Numeric time series; latency, traffic, errors, saturation. |
| **OpenTelemetry** | Vendor-neutral traces/metrics/logs standard. |
| **p95/p99** | 95th/99th percentile latency; tail experience. |
| **Span** | One operation in a distributed trace. |
| **Saturation** | Resource pressure (CPU, memory, queue, connections). |
| **Throughput** | Requests/messages processed per unit time. |
| **Trace** | Request path across services, made of spans. |
| **Little’s Law** | Concurrency = throughput × latency. |
| **Soak test** | Long-duration load test for leaks/degradation. |
| **Stress test** | Load beyond expected to find the breaking point. |

---

## Security

| Term | Meaning |
|---|---|
| **Authn/authz** | Who you are / what you may do. |
| **Broken access control** | Acting outside permissions; OWASP #1. |
| **IDOR** | Insecure direct object reference; swap an ID to test ownership. |
| **Mass assignment** | Client sets internal fields it should not control. |
| **RBAC** | Role-based access control. |
| **Rate limiting** | Cap requests per user/tenant/endpoint. |
| **SSRF** | Server fetches an attacker-controlled URL. |
| **XSS/CSRF** | Script injection / cross-site request forgery. |
| **ZAP** | DAST tool for baseline/full/API scans. |
| **SBOM** | Software bill of materials for dependencies. |

---

## AI

| Term | Meaning |
|---|---|
| **LLM** | Large language model; probabilistic text generation. |
| **RAG** | Retrieve relevant context, then generate an answer grounded in it. |
| **Embedding** | Vector representation used for semantic search. |
| **Hallucination** | Confident but unsupported/wrong output. |
| **Golden dataset** | Versioned questions + expected answers/sources for evaluation. |
| **LLM-as-judge** | Using a model to score outputs; calibrate against humans. |
| **Faithfulness/groundedness** | Whether the answer is supported by retrieved evidence. |
| **Prompt injection** | Untrusted content hijacks model instructions. |
| **Evaluation** | Systematic measurement of AI output quality/safety. |

---

## Interview

| Term | Meaning |
|---|---|
| **STAR** | Situation, Task, Action, Result. |
| **Trade-off** | What you chose and what you rejected. |
| **Oracle** | How you know the result is correct. |
| **Signal** | The useful information a test/pipeline produces. |
| **Flaky rate** | Share of tests that both pass and fail on the same code. |
| **Change failure rate** | Share of deploys that cause an incident. |
| **MTTR** | Mean time to restore service. |
