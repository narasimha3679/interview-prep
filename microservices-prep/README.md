# Microservices / Framework-Team Interview Prep

This pack is for a **Sr QE / SDET** role where you will:

- work on a **framework/platform team**,
- **build and maintain the shared test framework that all QE teams consume**, and
- be expected to have **"dev knowledge"** and **microservices exposure**.

Your four existing projects (Karate, RestAssured+Cucumber, Playwright, Selenium+Cucumber) prove
you can **write** frameworks. This pack helps you **re-position** that work and **close the gap**
on microservices + platform-team language.

Read in this order:

1. `README.md` (this file) — strategy + cram sheet
2. `01_microservices_primer.md` — the architecture + testing knowledge you must be able to speak
3. `02_framework_as_a_product.md` — the actual job, in platform-team terms
4. `03_reframe_your_projects.md` — turn your 4 projects into platform-level evidence
5. `04_mock_qa.md` — rehearse out loud

---

## The one thing to get right: your story

You do **not** need to pretend you built microservices. That gets caught in 3 questions and loses
the room. What you need is a story that is **honest, specific, and sounds like a platform engineer**:

> "I build test automation *platforms*, not one-off test scripts. My job is to make it fast and
> safe for many QE teams to test their services without reinventing the wheel — one framework,
> versioned and documented, with good defaults and escape hatches.
>
> I know microservices from the **consumer side**: I test service APIs through the gateway, I think
> in terms of **contracts** and **environments**, and I design my frameworks the same way platform
> teams design services — **versioned, backwards-compatible, configurable per environment, and
> shipped through CI**."

That last line is the key move. Everything you already do (config externalization, reusable
clients, tags, parallel runs, CI reports) *is* platform engineering. You just currently describe it
as "my test framework." Reframe it as "an internal product with consumers."

### What they are really hiring for (read between the lines)

| They say | They mean |
|---|---|
| "framework team" | You write code that **other engineers depend on**. Backwards compatibility, versioning, docs, support. |
| "all QE teams will use it" | You need a **golden path**, not a bespoke solution. Adoption > cleverness. |
| "microservices" | You understand distributed-system testing: contracts, stubs, async, environments, resilience. |
| "dev knowledge" | You can read/write production-grade Java/TS, use Git, CI, containers, code review, design patterns. |
| "maintain" | You handle **flaky tests, breakages, upgrades, deprecations** — the unglamorous 80%. |

### The honest position on microservices

You tested the **APIs of microservices** through a gateway. Own that precisely and then show you
understand the *architecture* around it:

- "I've been a consumer of microservices — I tested their APIs, I understood the contract, I dealt
  with environments and auth tokens. I haven't built a service, but I've had to reason about
  service behavior: what happens on partial failure, eventual consistency, retries, and how a
  change in one service breaks a consumer. That's exactly the mindset a testing framework needs."

Never say "I know microservices" flatly. Say "I work against microservices" and then demonstrate
you understand the internals. Depth beats a false claim.

---

## Gap map

| Area | You today | Target for the interview | Where to learn it |
|---|---|---|---|
| Test automation (UI/API) | Strong | Strong | You're done. `03_reframe_your_projects.md` |
| Microservice architecture | Weak | Conversational, confident | `01_microservices_primer.md` |
| Microservice *testing* strategy | Weak | Can draw the pyramid, name the tools | `01_microservices_primer.md` |
| Contract testing (Pact) | Unknown | Explain consumer-driven contracts | `01` + `04` |
| Platform/framework-as-product | Implicit | Explicit, owns it | `02_framework_as_a_product.md` |
| Packaging/versioning (Maven BOM, semver) | Basic | Can design the release model | `02` |
| Containers / CI / ephemeral envs | Basic | Can explain Testcontainers + ephemeral env | `01`, `04` |
| Java/Spring dev depth | Medium | Can pass a "dev knowledge" screening | `04` |

---

## The 60-second cram sheet

If you read nothing else, be able to say these fluently.

**Your pitch (2 sentences):** platform engineer for test automation; microservices-aware from the
consumer side; build versioned, documented, reusable frameworks.

**Testing pyramid for microservices** (bottom → top, most tests here at the bottom):
1. **Unit** — in-service, dev-owned.
2. **Component / integration** — one service + its real deps via **Testcontainers** (DB, Kafka).
3. **Contract** — **Pact / consumer-driven contracts** — the single most important microservices
   test type; catches breaking API changes without spinning up everything.
4. **E2E / journey** — few, through the gateway, business-critical paths only.

**The killer line:** *"In a monolith you test the whole thing E2E. In microservices E2E is slow,
flaky, and owned by everyone — so the framework's job is to push testing down into contracts and
component tests and keep E2E small."*

**Tools to name-drop correctly:**
- Contract: **Pact** (+ **Pact Broker / PactFlow**), **Spring Cloud Contract**.
- Virtualization/stubs: **WireMock**, **MockServer**, Hoverfly.
- Integration: **Testcontainers**, Docker.
- Async: **Kafka** (Testcontainers Kafka, EmbeddedKafka), RabbitMQ — test **eventual consistency** with polling/awaitility.
- Resilience: **Resilience4j** (circuit breaker), chaos experiments.
- Observability: **OpenTelemetry**, **Jaeger/Zipkin**, **correlation IDs**.
- API: **RestAssured**, **Karate**, **Postman/Newman**, **grpcurl** for gRPC.
- UI: **Playwright** (your strength), Selenium.
- C-S: **gRPC + Protobuf**, **REST/OpenAPI**, **GraphQL**, **Kafka events**.

**Framework-as-product keywords:** golden path, opinionated defaults, escape hatches, semantic
versioning, **Maven BOM / Gradle platform**, backwards compatibility, deprecation policy, adoption
metrics, onboarding docs, flaky-test quarantine, support SLA, feedback loop.

**Dev keywords:** SOLID, DI, ThreadLocal (you use it in `DriverFactory`!), factory pattern, builder
pattern, Jackson serialization, Maven lifecycle, Docker layers, 12-factor app, idempotency,
HTTP status semantics, retries with backoff, circuit breaker, OpenAPI spec.

**Five questions to ask them** (see end of `04_mock_qa.md`) — asking good questions signals
platform thinking.

---

## How to use this pack in the time you have

- **10 min:** README + the cram sheet.
- **1 hour:** `01` and `02` once, slowly. Write the 2 testing-pyramid diagrams from memory.
- **1 hour:** `03` — rehearse each project pitch out loud, 2 minutes each.
- **2 hours:** `04` — answer each question out loud *before* reading the model answer.
- **Ongoing:** keep `04_mock_qa.md` open the morning of. Read the cram sheet in the parking lot.
