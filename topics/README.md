# Topics Library

The study packs, grouped by subject. These are independent tracks; pick the ones the role needs.
Start with the topic that matches the risk you are preparing for, then run the matching project in
[`../PROJECTS.md`](../PROJECTS.md).

For level, time, and prerequisites, see [`../CATALOG.md`](../CATALOG.md).

---

## 60-second theory cram

1. **Waits** — implicit is global/presence-only; explicit is targeted/condition-based. Never mix them.
2. **Locators** — role/test-id → id/name → CSS → XPath. XPath does text/ancestors/functions; CSS cannot.
3. **POM** — one class per page; locators + actions inside; tests hold behavior.
4. **UI elements** — dropdown `Select`, alert `switchTo().alert()`, windows `getWindowHandles()`, frame `switchTo().frame()`, hover `Actions`.
5. **Exceptions** — stale = re-locate; click intercepted = wait for actionability; no such element = not found.
6. **Framework layers** — config → driver/clients → pages → tests → data → reporting → CI.
7. **Parallel safety** — `ThreadLocal<WebDriver>` + `remove()`, per-test browser context, unique data.
8. **TestNG order** — suite → test → class → groups → method; setup in `@BeforeMethod`, quit in `@AfterMethod`.
9. **Assertions** — hard assert stops; soft assert reports at `assertAll()`.
10. **Reporting** — listeners are event hooks; forgetting `flush()` gives an empty report.
11. **Java OOP** — encapsulation, inheritance, polymorphism, abstraction; abstract = code + state, interface = contract.
12. **`==` vs `equals`** — reference vs content; `String` is immutable.
13. **Collections** — `Map` is not a `Collection`; `HashMap` buckets; `ConcurrentHashMap` for concurrency.
14. **RestAssured** — `given()` setup, `when()` call, `then()` assertions; specs are the POM of APIs.
15. **APIs** — status codes, auth, idempotency, pagination, schema, contract.
16. **GraphQL** — HTTP 200 can still contain `errors[]`; assert `data` and `errors`.
17. **Spring Boot** — controller → service → repository; DTOs at the boundary; test slices.
18. **CI/CD** — fail fast, build once, artifacts, gates, rollback.
19. **Cloud** — identity, config, network, quotas, cost, observability; Azure-first here.
20. **Testing** — risk decides depth; cheapest level that catches the defect.

---

## Foundations

| Topic | What it covers |
|---|---|
| [`java/`](java/) | OOP, collections, exceptions, streams, concurrency, generics, JVM, modern Java, patterns |
| [`collections/`](collections/) | Collections hierarchy, HashMap internals, comparators, concurrency traps |
| [`coding/`](coding/) | Java coding drills: strings, arrays, maps, streams, concurrency, SQL |
| [`sql/`](sql/) | JOINs, aggregation, ACID, window functions, indexes, query plans |
| [`typescript/`](typescript/) | TypeScript types, async/modules, Playwright patterns |

## UI automation

| Topic | What it covers |
|---|---|
| [`selenium/`](selenium/) | WebDriver, waits, locators, POM, exceptions, framework design |
| [`testng/`](testng/) | Lifecycle, parallel execution, assertions, listeners |
| [`cucumber/`](cucumber/) | BDD, data tables, reusable steps |
| [`mobile/`](mobile/) | Native/hybrid/mobile web, Appium, device farms |
| [`accessibility/`](accessibility/) | WCAG, axe, keyboard/screen-reader checks |

## API automation

| Topic | What it covers |
|---|---|
| [`restassured/`](restassured/) | Given/when/then, JSONPath, POJOs, auth, specs |
| [`graphql/`](graphql/) | Queries, mutations, errors, schema, auth, N+1 |
| [`grpc/`](grpc/) | Protobuf, unary/streaming RPC, deadlines, status codes |

## Backend and architecture

| Topic | What it covers |
|---|---|
| [`spring-boot/`](spring-boot/) | Spring core, REST design, JPA, testing, runnable mini-service |
| [`microservices/`](microservices/) | Testing theory overview: contracts, async, resilience, reframing your projects |
| [`observability/`](observability/) | Logs, metrics, traces, OpenTelemetry, failure diagnosis |

## Delivery and cloud

| Topic | What it covers |
|---|---|
| [`ci-cd/`](ci-cd/) | Pipeline design, GitHub Actions, release validation, quality gates |
| [`cloud/`](cloud/) | Cloud fundamentals, provider mapping, Azure, containers |
| [`cloud/azure/`](cloud/azure/) | AZ-900 refresh, Azure core, Azure for SDETs, Azure DevOps |
| [`cloud/containers/`](cloud/containers/) | Docker, Kubernetes, Testcontainers |
| [`performance/`](performance/) | Load types, metrics, k6/JMeter/Gatling, CI budgets |

## Quality

| Topic | What it covers |
|---|---|
| [`test-strategy/`](test-strategy/) | Test levels, design techniques, data, flakiness, metrics, system design |
| [`security/`](security/) | OWASP, authn/authz, API security, ZAP |
| [`ai-testing/`](ai-testing/) | Testing LLM/RAG/chatbot systems |
| [`ai-assisted-testing/`](ai-assisted-testing/) | Using LLMs to generate, triage, and maintain tests |
| [`interview/`](interview/) | Behavioral stories, project narratives, system design, mock Q&A |

---

## Pack conventions

Each substantial topic pack follows:

```text
topics/<topic>/
  README.md              # purpose, cram, file map, terms, traps, study plan
  01_<topic>.md          # core concepts
  02_<topic>.md          # deeper mechanics or examples
  03_qa.md               # spoken Q&A
  04_exercises.md        # optional practice tasks
  examples/              # optional runnable code
```

Small packs can be shorter. Cross-topic tasks live in [`../EXERCISES.md`](../EXERCISES.md). The
structure should be honest, not padded.

---

## Related

- [`../CATALOG.md`](../CATALOG.md) — level/time/prereqs
- [`../PROJECTS.md`](../PROJECTS.md) — runnable projects
- [`../STUDY_PLAN.md`](../STUDY_PLAN.md) — study sequences
- [`../EXERCISES.md`](../EXERCISES.md) — cross-topic practice bank
- [`../PROGRESS.md`](../PROGRESS.md) — tracker
- [`../GLOSSARY.md`](../GLOSSARY.md) — vocabulary
- [`../AGENTS.md`](../AGENTS.md) — conventions for future changes
