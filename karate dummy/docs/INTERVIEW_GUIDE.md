# Interview Guide — What Went Where & Why

Study sheet for explaining this framework in a **Sr Automation Engineer** interview.

---

## 1. One-sentence pitch

> “I built a Maven/Java Karate API framework with Gherkin feature files, multi-env config, parallel JUnit runners, tagged suites, reusable callable features for auth/headers, schema validation, external test data, and Java helpers for dynamic payloads — against public APIs so the suite stays runnable without owning a backend.”

---

## 2. Karate vs Cucumber (the question you will get)

| Concern | Classic Cucumber + Rest Assured | Karate |
|---------|----------------------------------|--------|
| Spec language | Gherkin `.feature` | Same Gherkin `.feature` |
| Step glue | You write Java `@Given` / `@When` / `@Then` | **None** — Karate DSL is built in |
| HTTP client | Rest Assured (or similar) | Built into Karate |
| Assertions | Hamcrest / AssertJ / custom | Native `match` + fuzzy matchers |
| JSON handling | Manual parse / JsonPath | First-class JSON/XML |
| Reuse | Shared step defs / helpers | `call` / `callonce` features |
| Parallel | Surefire / JUnit config | `Runner.path(...).parallel(n)` |

**Key line:** Karate uses Cucumber’s *syntax*, not Cucumber’s *runtime glue model*. The feature file *is* the executable test.

Map folders to Cucumber thinking:

| This project | Cucumber equivalent |
|--------------|---------------------|
| `karate-config.js` | `cucumber.properties` + World / hooks config |
| `runners/*Runner.java` | `@CucumberOptions` / suite class |
| `features/**/*.feature` | Same — but also replaces step defs + Rest Assured calls |
| `features/common/*` | Shared step library / API “page objects” |
| `helpers/DataGenerator.java` | Utility classes injected into steps |
| `data/*.json` | External examples / test data files |
| `schemas/*.json` | Contract / schema assertion helpers |

---

## 3. Folder-by-folder tour

### `pom.xml`

- **`karate-junit5`** — engine + JUnit 5 integration.
- **`testResources` pointing at `src/test/java`** — copies `.feature` / `.js` / `.json` onto the classpath while excluding `.java`. Classic interview gotcha: without this, Karate cannot find features.
- **Surefire includes `*Runner.java`** — only runners execute; features are discovered by Karate.
- **Profiles `smoke` / `regression`** — CI selects suite without editing code.
- **`-Dkarate.env=...`** — system property read inside `karate-config.js`.

### `karate-config.js` (classpath root)

Runs **once per scenario** (Karate bootstraps config before each scenario). Returns a map (`jsonPlaceholderUrl`, `httpBinUrl`, timeouts) injected as variables into every feature.

Interview talking points:

- Single place for env URLs / timeouts / default SSL.
- Switch env with `-Dkarate.env=qa` — no hard-coded URLs in features.
- Analogous to a Spring `@Configuration` or Cucumber World bootstrap.

### `runners/`

Thin Java entry points — **no business logic**.

- `SmokeRunner` → `tags("@smoke")`, `parallel(2)`
- `RegressionRunner` → `tags("~@ignore")`, `parallel(4)`
- Assert `results.getFailCount() == 0` so Maven fails the build on scenario failures.

Interview: “Runners are the CI knobs; features are the product knowledge.”

### `features/users|posts|auth`

Domain-sliced packages (enterprise readability). Tags allow cross-cutting suite selection (`@smoke` across domains).

Patterns demonstrated:

- **Background** — shared `url` + `callonce` headers.
- **Scenario Outline** — data-driven ids (`getUsers.feature`).
- **`match` + schema file** — fuzzy contract check (`#string`, `#number`).
- **`@negative`** — intentional failure paths.
- **Lifecycle feature** — create → read → update → delete flow design.

### `features/common/`

Callable features tagged `@ignore` so runners skip them as standalone tests.

- `headers.feature` — `callonce` sets default headers once.
- `createUser.feature` — DRY create contract; callers pass `{ name, username, email }`.

Interview: “We treat common features like shared API clients — one place to change the create payload.”

### `features/auth/`

Enterprise auth pattern without needing a real IdP:

1. `loginHelper.feature` posts credentials (httpbin), returns `token`.
2. Consumer does `def auth = callonce read('...loginHelper.feature')`.
3. `configure headers = { Authorization: 'Bearer ' + auth.token }`.
4. Protected call hits httpbin `/bearer` and asserts `authenticated == true`.

Why httpbin (not ReqRes): ReqRes now requires an `x-api-key` after signup. For a teachable, zero-config demo, httpbin proves the **same callonce + Bearer pattern** companies use with OAuth/JWT.

### `helpers/DataGenerator.java`

Java interop:

```gherkin
* def DataGenerator = Java.type('helpers.DataGenerator')
* def email = DataGenerator.randomEmail()
```

Use when DSL is not enough: crypto, custom IDs, org-shared libraries.

### `data/` and `schemas/`

- External JSON payloads → maintainability (non-dev can edit data).
- Schema fuzzy match → catches contract drift without brittle full-payload equality.

---

## 4. Execution flow

```
mvn test -Dtest=SmokeRunner -Dkarate.env=dev
        │
        ▼
Surefire starts SmokeRunner
        │
        ▼
Runner.path("classpath:features").tags("@smoke").parallel(2)
        │
        ▼
For each matching scenario:
  karate-config.js → inject env URLs
  Background (headers callonce, url, …)
  Scenario steps (HTTP + match)
        │
        ▼
HTML + cucumber JSON under target/karate-reports
```

---

## 5. Likely interview follow-ups (and answers)

**How do you handle auth tokens across scenarios?**  
`callonce` login helper; stash token; `configure headers`. Prefer callonce over repeating login (speed + rate limits).

**How do you keep features DRY?**  
Callable features under `common/`, shared Background, external data files, Java helpers for generation.

**How do you run this in CI?**  
Maven + Surefire; profiles for smoke/regression; pass `-Dkarate.env=...`; publish `target/karate-reports`; fail on `getFailCount()`.

**Parallelism pitfalls?**  
Shared mutable state, dependent scenarios, rate-limited APIs. Prefer independent scenarios; use `callonce` carefully; isolate test data.

**Karate vs Rest Assured?**  
Karate wins on speed of writing API BDD and JSON match. Rest Assured + Cucumber wins when the org already standardizes on Java step defs / custom reporting / mixed UI+API glue. Many enterprises use Karate for API and Playwright/Selenium separately for UI.

**What’s new in Karate 2.x?**  
Java 21+, custom JS engine (no GraalJS), virtual threads, declarative auth. This demo stays on **1.5.2** because most interview discussions and corp stacks still reference the 1.x model.

**How would you add a new microservice?**  
Add `features/<service>/`, entry in `karate-config.js` for base URL, tags, optional common helpers — no runner change if path is already `classpath:features`.

---

## 6. Practice aloud (2-minute walkthrough)

1. “Entry point is a JUnit runner that selects tags and runs parallel.”  
2. “Config JS switches base URLs per environment.”  
3. “Features are Gherkin + Karate DSL — no step definition classes.”  
4. “Common callable features own headers and create-user so we don’t copy-paste.”  
5. “Auth is callonce login → Bearer header → protected call.”  
6. “Schema match and external JSON keep assertions maintainable.”  
7. “Java helper generates dynamic emails when we need uniqueness.”  
8. “Maven profiles map to smoke vs regression in the pipeline.”

If you can say that without notes, you are interview-ready on this framework.
