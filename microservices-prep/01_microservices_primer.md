# 01 — Microservices Primer (from the testing side)

Goal: stop freezing when they say "microservices." You need enough architecture vocabulary to
follow the conversation, plus a strong opinion on **how to test them**. You are allowed to say
"we tested services as a consumer"; you are not allowed to look blank.

> **Golden rule for this interview:** every architecture term below, know it in one line, and know
> *how it changes testing*. That's the QE angle that earns the job.

---

## 1. What microservices actually are (and aren't)

A microservice = an independently **deployable** service that owns a **single business capability**
and its **own data store**, communicating over the network (HTTP/gRPC/messaging).

| Aspect | Monolith | Microservices |
|---|---|---|
| Deploy | One artifact | Many, independently |
| Data | One shared DB | **Database per service** (no shared tables) |
| Calls | In-process method calls | Network calls (can fail, be slow, be partial) |
| Team | One big team | One team per service ("you build it, you run it") |
| Testing | End-to-end through one app | Contracts, component tests, few E2E |
| Failure | Usually all-or-nothing | **Partial failure is normal** — must be designed for |

**The sentence that shows you get it:**
> "The hard part of microservices isn't deployment, it's that every call can fail independently, and
> data is eventually consistent across services. That's what changes how you test."

### Key trade-offs to name
- **Pros:** independent deploy/scale, team autonomy, fault isolation, polyglot.
- **Cons:** distributed complexity, network unreliability, eventual consistency, debugging across
  services, harder E2E, duplicated effort ("just use a monolith until it hurts").

**Bonus:** "Distributed monolith" = microservices in name only; services still tightly coupled and
must deploy together. Interviewers love hearing you call that out as an anti-pattern.

---

## 2. The architecture pieces (one-liners)

Know these names and what they do — this is the vocabulary test.

| Piece | What it is | Testing implication |
|---|---|---|
| **API Gateway** | Single entry point; routing, auth, rate limiting (Kong, NGINX, Spring Cloud Gateway, AWS API GW) | Most E2E tests enter here, not at services directly |
| **Service discovery** | Services find each other by name (Eureka, Consul, Kubernetes DNS) | Tests need service names/URLs resolved per env |
| **Config server** | Centralized config per service/env (Spring Cloud Config) | Same code, different env — mirrors your `ConfigManager` |
| **BFF** (Backend for Frontend) | A service tailored to one client (web/mobile) | UI E2E often talks to a BFF, not raw services |
| **Service mesh** | Sidecar proxies handle mTLS, retries, traffic (Istio, Linkerd) | Resilience is partly infra — tests can assume retries exist |
| **Message broker** | Async communication (Kafka, RabbitMQ, SNS/SQS) | Async = eventual consistency → test with polling/await, not asserts |
| **Database per service** | No shared DB; each service owns its schema | Test data can't be inserted once for all services; no cross-service joins |
| **Saga / orchestration** | Distributed transaction across services | "Did the whole flow complete?" needs event/state checking |
| **Outbox pattern** | Reliably publish events alongside a DB write | Explains why some state changes appear with delay |

---

## 3. Communication styles (know the difference)

| Style | Example | Nature | How to test |
|---|---|---|---|
| **Synchronous REST/JSON** | GET /users/1 | Request/response, blocking | RestAssured/Karate, status + schema |
| **Synchronous RPC** | **gRPC + Protobuf** | Contract defined in `.proto`, binary, fast | `grpcurl`, Postman gRPC, Karate gRPC |
| **GraphQL** | POST /graphql | Client picks fields | Query-specific assertions; watch N+1 |
| **Asynchronous events** | Kafka topic | Fire-and-forget, eventual | Publish, then **poll until** the consumer state settles |

**REST vs gRPC one-liner:** "REST/JSON is human-readable and browser-friendly; gRPC is binary,
schema-strong via Protobuf, and faster for internal service-to-service calls. From a testing view,
gRPC gives you a hard contract you can validate against the `.proto`."

**Idempotency:** a retried request shouldn't create two records. Test it: send the same POST twice
with an idempotency key, assert one effect. Say this — it signals distributed-systems maturity.

---

## 4. The testing strategy for microservices (memorize this shape)

Classic "test pyramid" still applies, but for microservices it becomes a **honeycomb**: lots of
unit + component tests, a **thick middle of contract tests**, and very few slow E2E tests.

```
        /\        E2E / journey  (few, business-critical, through the gateway)
       /  \       -----------------------------------------------
      /    \      Contract tests  (THICK — the microservices superpower)
     /      \     -----------------------------------------------
    /        \    Component / integration (service + real deps via Testcontainers)
   /__________\   Unit tests (dev-owned, in-service, fast)
```

**Why E2E must shrink:** in a monolith one E2E run tests everything. In microservices the E2E path
crosses N services owned by N teams — it's slow, flaky, and a failure points at *who knows which
service*. So the framework's job is to **push coverage down** into contracts and component tests.

**Say this line:** *"E2E in microservices is the most expensive and least reliable test type, and
it's owned by everyone — which means no one. My job on the framework team is to make the lower
levels — contracts and component tests — so good that we need very few E2E."*

### Test type → what it means → tools

| Level | What you're testing | Typical tools |
|---|---|---|
| **Unit** | One class/function | JUnit, TestNG, Jest |
| **Component** | One service with its real infrastructure (DB, broker) but stubbed neighbors | **Testcontainers**, Spring Boot Test, WireMock for neighbors |
| **Contract (consumer-driven)** | "The provider still honors what I, the consumer, need" | **Pact**, Pact Broker/PactFlow, Spring Cloud Contract |
| **Integration** | Multiple real services wired together | Docker Compose, Testcontainers, Kubernetes namespaces |
| **E2E / journey** | A business flow across the system | Playwright/Selenium (UI), Karate/RestAssured (API), synthetic monitors |
| **Resilience / chaos** | Behavior under failure, latency, partition | Toxiproxy, Chaos Monkey/Chaos Mesh, Resilience4j tests |
| **Performance** | Load/latency | Gatling, k6, JMeter |
| **Security** | Authn/authz, injection | ZAP, Burp, contract-level auth tests |

---

## 5. Contract testing — the one you MUST be able to explain

This is the highest-signal microservices-testing concept. Learn it cold.

**Problem it solves:** In microservices, service A consumes service B's API. If the B team changes a
field, A breaks — and you only find out in a slow E2E run (or worse, in production). Contract
testing catches that *without* deploying both services together.

**Consumer-driven contract (CDC):**
1. The **consumer** (A) writes a test stating what it needs from the provider (B): which request,
   which fields in the response.
2. That test generates a **contract** (a JSON `pact` file) and, optionally, runs an in-process
   **mock provider** so the consumer test is fast and isolated.
3. The contract is published to a **broker** (Pact Broker / PactFlow).
4. The **provider** (B) runs the contract as a test — **provider verification**. If B can satisfy
   every consumer's contract, it's safe to deploy.
5. The broker does **`can-i-deploy`**: it tells CI whether this version of B is compatible with all
   deployed consumers before you release.

**One-line pitch:** *"Contract testing lets me verify that a provider still satisfies every
consumer without running both at once — it's the fast, reliable alternative to brittle E2E, and the
broker gates deployments."*

**Why it's a framework-team thing:** the framework team typically **owns the contract-testing
tooling and CI integration** (Pact is easiest to adopt when someone provides the broker, the
template, and the `can-i-deploy` gate). That's you.

**Pact vs Spring Cloud Contract:** Pact is polyglot and consumer-driven (consumer writes the
contract). Spring Cloud Contract is JVM-centric and can be provider-driven (provider defines the
contract, consumers get generated stubs). Both solve the same problem; name both.

**Contract vs Integration test:** a contract verifies **compatibility** (shape/behavior the consumer
depends on); an integration test verifies **real wiring** (does A actually talk to a running B). You
want many contracts, few integration tests.

---

## 6. Service virtualization & stubbing (how you test without the dependency)

When a dependency is down, slow, flaky, not built yet, or costs money to hit.

| Tool | Use |
|---|---|
| **WireMock / MockServer** | HTTP stubs, stateful mocks, record-and-replay real traffic |
| **Hoverfly** | Simulation + capture |
| **Pact mock provider** | Auto-generated from consumer contracts (best kind — never drifts) |
| **Testcontainers** | Real dependency in a container (better than a fake when feasible) |

**Judgment call to voice:** *"Prefer real dependencies in containers (Testcontainers) for your own
service's infra; stub the *neighbors* you don't own with WireMock or a Pact mock — that keeps the
test deterministic and on your side of the boundary."*

---

## 7. Testcontainers & ephemeral environments

**Testcontainers:** a library that starts real Docker containers (Postgres, Kafka, Redis, LocalStack)
from your test code, with lifecycle managed and cleaned up automatically.

- **Why it matters:** turns "we need a shared test DB" into "each test run gets a throwaway
  Postgres." Realistic + isolated + no manual setup.
- **Say:** *"Testcontainers replaces shared, stale test environments with disposable real
  infrastructure, per run or per test — it kills a whole class of flakiness and env contention."*

**Ephemeral (short-lived) environments:** instead of one shared "QA env" everyone fights over,
spin up a fresh environment per PR (Docker Compose / Kubernetes namespace / preview env), run tests,
tear it down. This is how modern microservice teams get **deterministic, parallel-safe** E2E.

**Why the framework team cares:** the framework must support both — point at a shared env *or* a
dynamically provisioned one — via **config, not code**. That's exactly the `ConfigManager` /
`karate-config.js` pattern you already use.

---

## 8. Testing async / event-driven systems (Kafka)

The trap: an async flow isn't done when the HTTP call returns. Data propagates via events.

**How to test it:**
1. Kick off the action (usually an API call).
2. **Poll / await** the outcome (a DB state, an API read, or a message on a topic) until it settles
   or a timeout hits. Use Awaitility, Playwright's `expect.poll`, or Karate's retry.
3. Assert the **eventual** state, and assert the **event payload/schema**.
4. Test **ordering and idempotency**: duplicate events shouldn't double-apply.

**Tools:** Testcontainers **Kafka**, Spring **Embedded Kafka**, `KafkaTemplate` + a consumer in the
test, `kafka-console-consumer` for manual checks.

**Say:** *"Async flows break the 'assert immediately' habit. I test the end state by polling to a
timeout, and I separately contract-test the event schema so producers can't silently change it."*

---

## 9. Resilience & failure testing (the stuff interviewers love)

Microservices must survive partial failure. You test **failure behavior**, not just happy paths.

- **Timeouts** — every network call needs one; test that a slow dependency doesn't hang the service.
- **Retries with backoff** — transient failures recover.
- **Circuit breaker** (Resilience4j) — after N failures, fail fast instead of hammering a dead dep.
- **Bulkheads** — one slow dep doesn't exhaust all threads.
- **Graceful degradation** — service returns a partial/cached response instead of 500.
- **Chaos testing** — inject latency/errors/partitions (Toxiproxy, Chaos Mesh) and assert the system
  degrades gracefully and recovers.

**Say:** *"A huge part of testing microservices is negative and resilience testing — what happens
when a dependency is slow or down. I'd use Toxiproxy or WireMock to inject latency and errors and
assert the service times out, retries, or degrades instead of cascading the failure."*

---

## 10. Test data across services (a real microservices pain)

Because **there's no shared DB**, you can't insert one fixture row and test everything.

Approaches:
- **API-driven setup** — create data via the service's own API in a `@Before`, delete in `@After`.
- **Per-test unique data** — random emails/IDs (you already do this in Karate `DataGenerator`) so
  parallel runs don't collide.
- **Test data service / factory** — a small helper library that knows how to seed each service.
- **Synthetic/seed endpoints** — some teams expose test-only endpoints (guard them!).
- **Isolation over cleanup** — fresh ephemeral DB per run beats "clean up after."

**Say:** *"Test data is harder in microservices because you can't reach across databases. I favor
creating data through APIs and making it unique per test so I never depend on order or cleanup —
that's what keeps parallel runs stable."*

---

## 11. Observability — read it, don't just assert it

A test failure in microservices often can't tell you *why*. Observability fills the gap.

- **Correlation / trace ID** — one ID that follows a request across all services. You can pass and
  assert a trace ID in API tests; on failure, use it to find the real cause in logs/traces.
- **Distributed tracing** — **OpenTelemetry** → Jaeger/Zipkin. Shows which service in the chain
  failed or was slow.
- **Structured logs + metrics** — the framework/CI should link to them from a failure.

**Say:** *"Testing microservices is as much about diagnosability as assertion. I'd capture the
correlation ID on every request so a CI failure links straight to the trace and the service that
actually broke."*

---

## 12. CI/CD for microservices (the framework lives here)

- **Per-service pipelines:** each service runs unit + component + **provider verification** before
  deploy.
- **Consumer pipeline:** runs consumer contract tests, publishes contracts, then `can-i-deploy`.
- **Deploy gate:** the Pact broker blocks a release if any consumer would break.
- **Ephemeral env per PR** for integration/E2E where needed.
- **Pipeline stages:** build → unit → component (Testcontainers) → contract → integration → E2E →
  (nightly) performance/resilience.
- **Fail fast:** cheap tests first; expensive E2E last; never gate every merge on a slow flaky E2E.

**The framework team's role:** provide the **shared CI templates/pipelines**, the test runner, the
reporting, the environment config, and the contract-broker integration — so a product team adds
"run the tests" with a few lines, not a bespoke setup.

---

## 13. Common microservices-testing pitfalls (know the trap, name it)

1. **E2E everything** — slow, flaky, unowned. Push tests down.
2. **Shared mutable test env** — teams block each other; replace with ephemeral envs/containers.
3. **Shared test database** — breaks database-per-service; use API-driven or per-run data.
4. **Asserting immediately on async flows** — must poll to eventual consistency.
5. **Hard-coded URLs/credentials** — everything must be env-injected (your `ConfigManager`).
6. **Test coupling / order dependence** — kills parallelism.
7. **No contracts** — provider changes silently break consumers.
8. **Only happy paths** — microservices fail in partial ways; test timeouts, retries, degradation.
9. **Snowflake suites** — every team writes their own framework; that's the problem the framework
   team exists to solve.
10. **No observability hook** — failures you can't diagnose get ignored.

---

## 14. If they ask a microservices question you don't know

Never bluff. Use this bridge, then reason out loud:

> "I haven't implemented that directly, but I've worked against services that do it. From a testing
> angle I'd want to know X and Y, and I'd verify it by Z. Can I reason through it?"

Then use the pyramid + contract + resilience + config-via-environment thinking from this document.
**Reasoning from first principles while being honest is a stronger answer than a memorized fact.**
