# 03 — Project Stories

Lead with engineering decisions, not tools. The interviewer can ask "what tool?" later; they cannot
easily recover if you only list tools.

---

## Universal project pitch

> "I built a reusable automation framework, not a set of one-off scripts. The interesting parts are
> how I externalized configuration per environment, kept tests isolated and parallel-safe, separated
> transport from assertions, and wired it into CI with reports and failure artifacts. It was designed
> to be picked up by other engineers."

Then give the project-specific details.

---

## Story 1 — Selenium + Cucumber + TestNG

**Pitch:**
> "A Java 17 Selenium 4 framework with Cucumber BDD, TestNG, thread-safe driver management, page
> objects, explicit waits, Extent/Cucumber reporting, failure screenshots, and a nightly CI run."

**Design decisions:**

- `ThreadLocal<WebDriver>` for parallel safety; remove on quit to avoid leaks.
- Implicit wait zero; all waits explicit and condition-based.
- Page objects for selectors/actions; specs for behavior.
- Hooks for lifecycle; listener for screenshots/reporting.
- Config externalized; browser/headless via system properties.

**Trade-offs:**

- BDD adds a layer; useful when product/QA read scenarios, overhead when not.
- UI tests are limited to UI-specific risk; API setup for state.

**What I'd improve:**

- API fixtures for setup, sharding, flaky quarantine, trace/video artifacts.

---

## Story 2 — Playwright + TypeScript

**Pitch:**
> "A TypeScript Playwright framework with typed fixtures, page objects, cross-browser projects,
> data-driven negative login coverage, a checkout journey, trace/video on failure, and GitHub Actions
> reporting."

**Design decisions:**

- Fixtures as dependency injection; each test gets isolated page objects.
- Browser context per test for cookie/storage isolation.
- Auto-waiting/web-first assertions; no fixed sleeps.
- CI-aware config: retries only in CI, worker limits, `forbidOnly`.
- Trace on failure for replayable debugging.

**Trade-offs:**

- Cross-browser triples runtime; run all on nightly, one on PR.
- UI tests only for what the UI can prove; API setup for state.

**What I'd improve:**

- API fixtures, sharding, accessibility checks, visual regression.

---

## Story 3 — RestAssured + Cucumber + TestNG

**Pitch:**
> "An enterprise-shaped API BDD framework: Cucumber features, RestAssured HTTP, TestNG runner, clear
> layering — features describe behavior, steps orchestrate/assert, API clients do HTTP only, config
> and payloads are shared infrastructure."

**Design decisions:**

- No assertions in the API client; one method serves positive/negative tests.
- `RequestSpecification` with base URI, headers, timeouts, logging.
- `ConfigManager` with system-property overrides.
- `TestContext` to share the last response between step classes.
- POJOs + external JSON payloads.

**Trade-offs:**

- BDD overhead vs business readability.
- Shared context simplifies steps but must be reset per scenario.

**What I'd improve:**

- Contract tests, schema validation in CI, auth token reuse, parallel-safe data.

---

## Story 4 — Karate API framework

**Pitch:**
> "A Maven/Java Karate framework with Gherkin features, no step-definition glue, multi-env config,
> parallel runners, tags, callable auth/header features, fuzzy schema validation, and Java helpers
> for dynamic data."

**Design decisions:**

- Karate's DSL replaces step glue; feature file is executable.
- `karate-config.js` per environment; `-Dkarate.env`.
- Runners are thin CI knobs (tags/parallel).
- `callonce` login → Bearer header → protected call.
- Schema files for contract-style checks.

**Trade-offs:**

- Karate is fast to write but less flexible than Java step defs for complex custom logic.
- Public APIs keep the demo runnable; enterprise would point at internal services.

**What I'd improve:**

- Contract broker, WireMock for neighbors, Testcontainers for real infra, CI reporting.

---

## Story 5 — Small Spring Boot service

**Pitch:**
> "A small Spring Boot service with REST endpoints, DTOs, validation, a stable error model,
> pagination/filtering, JPA persistence, and MockMvc tests. I built it to understand the provider
> side, not just the consumer side."

**Design decisions:**

- Controller → service → repository; entities not exposed.
- Bean Validation at the boundary; global exception handler.
- Page size capped; `201 + Location`; `204` on delete.
- H2 for the demo; Testcontainers/Postgres would be the next step.

**Trade-offs:**

- H2 is fast but not production SQL parity.
- No auth/idempotency yet; those are deliberate extensions.

**What I'd improve:**

- Idempotency keys, Postgres/Testcontainers, Flyway, OpenAPI, auth, contract tests.

---

## How to present a framework in 2 minutes

1. **Problem:** what it replaces (one-off scripts, flaky suite, slow feedback).
2. **Shape:** layers and key components.
3. **Decisions:** 2–3 trade-offs you made.
4. **Evidence:** CI, reports, adoption, reliability.
5. **Next:** what you'd improve.

---

## Project story checklist

- [ ] 2-minute pitch.
- [ ] 3 design decisions with trade-offs.
- [ ] One failure/flakiness story.
- [ ] One metric/outcome.
- [ ] One "what I'd improve."
- [ ] Ready for "why this tool?" follow-ups.
