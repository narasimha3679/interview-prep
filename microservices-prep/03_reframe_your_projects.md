# 03 — Reframing Your Four Projects as Platform Evidence

Your projects are good, but right now you describe them as "frameworks I built." In this interview
you must describe them as **reusable platforms with consumers and design trade-offs**. Same code,
higher-level story.

For each project: the **pitch**, the **design decisions that matter** (these prove dev thinking),
and the **"how I'd productionize this for the framework team"** line that connects it to the new job.

---

## The universal reframe (applies to all four)

Never lead with *"I used X to test Y."* Always lead with the **engineering decisions**:

> "I built a reusable automation framework. The interesting parts are how I externalized
> configuration per environment, kept test state isolated and parallel-safe, separated the HTTP/UI
> layer from the assertions, and wired it into CI with reports and failure artifacts. It was designed
> to be picked up by other engineers, not just run by me."

Then the interviewer's follow-ups are about **design**, which is what a framework-team role wants.

---

## 1. Karate API Framework (`karate dummy`)

**Pitch:**
> "A Maven/Java 17 Karate framework for API and microservice-style testing. Feature files double as
> executable specs — no step-definition glue. Multi-environment config, parallel JUnit runners,
> tagged suites for smoke vs regression, reusable callable features for auth and headers, fuzzy
> schema validation, and external test data. I purposely kept it runnable against public APIs so it
> demonstrates the patterns without needing a backend."

**Design decisions that prove dev thinking:**
- **`karate-config.js`** bootstraps per-scenario config — the single place for env URLs/timeouts.
  *Same pattern as Spring `@Configuration` or a Config Server.*
- **Runners are thin CI knobs** (`parallel(n)`, tags) — no business logic in them.
- **Callable features marked `@ignore`** = shared "API client" library, DRY.
- **Auth via `callonce` login helper → Bearer header → protected call** — the enterprise token
  pattern, and `callonce` is a deliberate performance choice to avoid repeated logins / rate limits.
- **Schema files + fuzzy match** (`#string`, `#number`) — contract-style validation that catches
  drift, not brittle full-payload equality.
- **External JSON data + Java helper** (`Java.type('helpers.DataGenerator')`) — dynamic payloads
  (random emails) for parallel-safe tests.

**Microservices angle to volunteer:**
> "This is the layer a QE team would use to test a microservice's API. If I extended it for the
> framework team, I'd add: (1) contract testing so provider changes are caught before E2E, (2) a
> WireMock wrapper so a service can be tested with its neighbors stubbed, and (3) Testcontainers so
> a team can spin up real infra per run instead of a shared env. And 'how would you add a new
> microservice' is trivial here — a new `features/<service>/` folder plus a base-URL entry in
> `karate-config.js`; no runner change."

**"Framework team" line:** *"This is essentially the API-testing golden path I'd hand a QE team —
and Karate's no-glue model means less code for teams to maintain."*

---

## 2. RestAssured + Cucumber + TestNG (`rest assured BDD`)

**Pitch:**
> "An enterprise-shaped API BDD framework: Cucumber features for business-readable scenarios,
> RestAssured for HTTP, TestNG as the runner, with a clear layer separation — features describe
> behavior, step definitions orchestrate and assert, API clients do HTTP only and never assert,
> and shared infrastructure handles config, payloads, and models."

**Design decisions that prove dev thinking:**
- **Strict layering rule:** *no `Assert` in the API client.* One client method serves both positive
  and negative tests. This is the separation-of-concerns answer interviewers want.
- **`BaseClient`** builds one reusable `RequestSpecification` — base URI, content type, timeouts
  (`http.connection.timeout` / `http.socket.timeout`), request/response logging, optional `x-api-key`.
  *Timeouts on every call is a resilience-minded decision — say it.*
- **`ConfigManager`** loads properties once, **system properties override file** (`-Dapi.key=...`).
  This is exactly how a framework lets CI/local differ from code. *This is the microservices/config-
  server pattern in miniature.*
- **`TestContext`** exists because Cucumber instantiates step classes separately — a shared state
  holder between `When` and `Then`. Shows you understand the runtime model, not just the API.
- **Models (POJOs) + file payloads** — you know both typed and file-based request bodies, which is
  what real codebases use.

**Microservices angle to volunteer:**
> "The layering here maps directly onto how you test a microservice: the client is your service
> adapter, the step defs are orchestration, and config is environment-driven so the same suite runs
> against local, CI, or a deployed env. For the framework team I'd turn `BaseClient` and
> `ConfigManager` into a published library with a BOM so teams don't each rebuild them."

**"Framework team" line:** *"This is the annotate-your-behavior style; the framework-team version
would be a starter project plus a shared client/config module, so a team writes features and never
touches HTTP plumbing."*

---

## 3. Playwright Framework (`Playwright Framework`)

**Pitch:**
> "A TypeScript Playwright framework with typed, per-test fixtures, page objects, data-driven
> negative login coverage, a full checkout journey, cross-browser projects, and trace-based failure
> debugging — all running in GitHub Actions and publishing an HTML report."

**Design decisions that prove dev thinking:**
- **Custom fixtures** (`tests/fixtures/test.ts`) — page objects are constructed per test against the
  isolated `Page`; no shared mutable state. *This is dependency injection. Say "DI."*
- **Page Object Model** — selectors + actions live together; specs describe outcomes. Locator changes
  are localized. *Directly parallels the API-client layering.*
- **Isolation model** — fresh browser **context** per test (not just per file), so cookies/storage
  don't leak. You can explain browser vs context.
- **Auto-waiting + web-first assertions** — no fixed sleeps. *Flakiness is a theme in this interview —
  connect it.*
- **CI-aware config** (`playwright.config.ts`): retries only in CI, worker limits, `forbidOnly`,
  trace/screenshot/video `on-failure`, `testIdAttribute: 'data-test'`, accessible selectors first.
- **Failure diagnostics** — trace = replayable actions + DOM + network. *This is the observability
  theme, on the UI side.*

**Microservices angle to volunteer:**
> "UI is the top of the pyramid, so I keep UI E2E small and push setup down. For microservices I'd
> add API fixtures to seed state instead of clicking through the UI for setup, and I'd point the same
> specs at an ephemeral environment per PR rather than a shared env."

**"Framework team" line:** *"This is publishable as an npm package or template; the fixtures are the
extension point teams compose with, and the config is where environments are injected."*

---

## 4. Selenium BDD Framework (`selenium BDD`)

**Pitch:**
> "A Java 17 Selenium 4 + Cucumber + TestNG framework with a thread-safe driver factory, page
> objects, explicit-wait utilities, Extent + Cucumber reporting, failure screenshots, and a nightly
> GitHub Actions pipeline that emails report artifacts."

**Design decisions that prove dev thinking:**
- **`DriverFactory` with `ThreadLocal<WebDriver>`** — one driver per thread, **parallel-safe**. *This
  is the standout dev detail. If they ask about threading/concurrency in Java, you have a real
  example: `ThreadLocal` isolates driver state per test thread, and `quitDriver()` removes it to
  prevent leaks. Bring this up unprompted.*
- **`switch` expressions / modern Java** — `DriverFactory` uses Java 17 switch expressions; you're on
  current language features.
- **Explicit waits only** — `implicitlyWait(Duration.ZERO)` deliberately, then explicit waits in
  `WaitUtils`. *You can explain the classic implicit+explicit wait bug this avoids.*
- **Layering** — `BasePage` → concrete pages → step defs → features, plus `Hooks` for lifecycle and
  `ConfigReader` for env config. Same separation story as the API framework.
- **Nightly CI + artifact email** — a real scheduled regression pipeline with reporting.

**Microservices angle to volunteer:**
> "Same pattern as the API frameworks: thread-safe shared infrastructure, env-driven config, layered
> design. The parallel-safety work here — ThreadLocal state — is exactly the discipline you need when
> a framework is shared across teams and runs in CI at scale."

**"Framework team" line:** *"The `DriverFactory` and `BasePage` are the reusable core; teams extend
with their own pages. I'd package that core so every team inherits parallel safety and reporting
instead of re-solving it."*

---

## Cross-cutting themes to reuse everywhere (these ARE dev knowledge)

1. **Configuration externalized per environment** — `ConfigManager`, `karate-config.js`,
   `ConfigReader`, `playwright.config.ts` + `.env`. *This is the config-server / 12-factor pattern.*
2. **Isolation & parallel safety** — `ThreadLocal` drivers, per-test Playwright contexts, unique test
   data, Cucumber `TestContext`. *No shared mutable state.*
3. **Separation of concerns** — features ≠ steps ≠ clients ≠ config. *SOLID's single-responsibility,
   applied.*
4. **Reuse via composition** — callable Karate features, page objects, fixtures, base classes.
5. **CI as a first-class citizen** — tags/profiles for smoke vs regression, reports as artifacts,
   fail-on-green gates, nightly regression.
6. **Diagnosability** — logs on failure, screenshots, traces, JSON/Extent reports. *Observability.*
7. **Design patterns actually used:** Factory (`DriverFactory`), Builder (`RequestSpecBuilder`),
   Context/Session (`TestContext`), Page Object, Template (runners), DI (Playwright fixtures).

---

## Your "first 90 days / first year" story (have this ready)

Interviewers love this because it proves you'll own the role, not just code.

- **First 30 days — understand the customers.** Sit with consuming QE teams. Map what they test
  (UI/API/services), what hurts (flakiness, setup, environment contention, onboarding time), and audit
  the current framework's adoption and gaps.
- **Next 30 — stabilize & document.** Fix the highest-pain reliability issues (flaky rate, timeouts),
  write the quickstart + cookbook so a new team can go green in an hour, set up the support channel.
- **Next 30 — deliver the golden path.** Ship the versioned starter template + shared modules (client,
  config, reporting), publish to the internal repo, and onboard the first one or two pilot teams.
- **Then — scale & evolve.** Instrument adoption/flakiness; add contract-testing and Testcontainers
  support for microservice teams; establish the release/deprecation process; keep a visible roadmap and
  feedback loop.

**Say it as:** *"First I'd understand the teams' pain, then stabilize and document, then ship the
golden path and onboard pilots, then scale with metrics and contract testing for the service teams."*
