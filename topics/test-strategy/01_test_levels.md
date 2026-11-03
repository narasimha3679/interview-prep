# 01 — Test Levels

## 1. Unit tests

**What:** one class/function in isolation, with collaborators mocked or stubbed.

**Catches:** logic errors, boundary conditions, calculations, state transitions.

**Does not catch:** wiring, serialization, database dialect, network, auth filters.

**Rule:** fast, deterministic, developer-owned. If a unit test needs a database or a browser, it is
not a unit test.

---

## 2. Component tests

**What:** one service with its real infrastructure (database, broker, cache), neighbors stubbed.

**Catches:** persistence mapping, transactions, migrations, message handling, service logic with
real dependencies.

**Does not catch:** cross-service contract drift, full journey behavior.

**Tools:** Testcontainers, Spring Boot Test, WireMock, embedded Kafka.

**Why it matters:** it is the sweet spot for microservices — realistic enough to catch integration
bugs, isolated enough to be fast and attributable.

---

## 3. Integration tests

**What:** multiple real components wired together.

**Catches:** serialization between services, network config, auth propagation, contract mismatch,
eventual consistency.

**Does not catch:** everything at production scale; can be slow and flaky if overused.

**Rule:** keep the scope explicit. "Integration" that spins up the whole estate is E2E with a
different name.

---

## 4. Contract tests

**What:** verify that a provider still satisfies what each consumer needs, without running both.

**Catches:** breaking API/schema changes, missing fields, changed types, event schema drift.

**Does not catch:** real wiring, latency, auth, business behavior across services.

**Tools:** Pact, Spring Cloud Contract, GraphQL schema diff, OpenAPI diff, Avro/Protobuf schema
compatibility.

**Rule:** contract tests are the cheapest way to prevent cross-service breakage. They belong in the
provider's pipeline and the consumer's pipeline.

---

## 5. API tests

**What:** behavior through the service's HTTP/gRPC/GraphQL interface.

**Catches:** status codes, validation, auth, serialization, pagination, error model, business rules.

**Does not catch:** UI rendering, browser behavior, visual layout.

**Rule:** API tests are the workhorse for backend SDETs. They are faster and more stable than UI
tests and closer to the business behavior than unit tests.

---

## 6. UI tests

**What:** behavior through a browser or mobile app.

**Catches:** rendering, navigation, client-side validation, accessibility, integration with the
backend, user journeys.

**Does not catch:** backend logic already covered by API tests.

**Rule:** use UI tests for what only the UI can prove. Seed state through APIs; do not click through
setup.

---

## 7. End-to-end (E2E) tests

**What:** a full business journey across the real system.

**Catches:** integration failures that only appear when everything is wired together.

**Does not catch:** root cause. A failure tells you "somewhere in this path."

**Rule:** few, critical, business-level. Owned jointly, not by one team. Run on merge/nightly/pre-
release, not every commit.

---

## 8. Smoke, sanity, regression

| Type | Question | Scope | When |
|---|---|---|---|
| **Smoke** | Does the build work at all? | Tiny critical set | Every deploy |
| **Sanity** | Is this specific change sane? | Narrow, change-focused | After a fix |
| **Regression** | Did we break existing behavior? | Broad | Nightly/pre-release |
| **Acceptance** | Does it meet the requirement? | Business scenarios | Feature completion |

Smoke is not a small regression suite. It is a go/no-go check.

---

## 9. Non-functional levels

| Type | Question | Examples |
|---|---|---|
| **Performance** | Is it fast enough under load? | k6, JMeter, Gatling |
| **Resilience** | Does it degrade gracefully? | Toxiproxy, chaos experiments |
| **Security** | Can it be abused? | OWASP, ZAP, auth tests |
| **Accessibility** | Can everyone use it? | axe, manual keyboard/screen reader |
| **Compatibility** | Does it work across environments? | Browsers, devices, OSes |
| **Localization** | Does it work per locale? | Currency, dates, translations |

---

## 10. Choosing the level

Ask:

1. **What is the risk?** A wrong price is not the same as a wrong label.
2. **What can only this level catch?** If an API test can catch it, don't use UI.
3. **How fast is feedback?** The developer should know within minutes.
4. **How attributable is a failure?** A contract failure names the service; an E2E failure doesn't.
5. **How stable is the environment?** Shared UI environments are the flakiest.

**Decision table:**

| Risk | Best level |
|---|---|
| Calculation/business rule | Unit |
| Persistence/transaction | Component |
| Service compatibility | Contract |
| API behavior/auth | API |
| Visual/navigation/accessibility | UI |
| Critical business journey | E2E (few) |
| Load/latency | Performance |
| Failure behavior | Resilience |

---

## 11. Test level checklist

- [ ] Each risk has a level that can catch it.
- [ ] Unit tests are fast and isolated.
- [ ] Component tests use real infrastructure where it matters.
- [ ] Contracts cover every service-to-service interface.
- [ ] API tests cover auth, validation, errors, pagination.
- [ ] UI tests are limited to UI-specific risk.
- [ ] E2E is small, critical, and owned.
- [ ] Non-functional risks have a plan.
- [ ] Each level runs at the right trigger.
