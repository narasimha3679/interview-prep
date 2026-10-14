# RestAssured + Cucumber + TestNG Learning Framework

Dummy **enterprise-shaped** API test framework for learning BDD automation with:

- **Java 17** + **Maven**
- **RestAssured** (HTTP client + assertions helpers)
- **Cucumber** (Gherkin features / BDD)
- **TestNG** (test runner)
- Sample API: [JSONPlaceholder](https://jsonplaceholder.typicode.com) (no API key required)
  - Optional: switch `baseUrl` / `apiKey` in `config.properties` to use [ReqRes](https://reqres.in) with your own key

---

## How to run

```bash
# All scenarios (except @wip)
mvn clean test

# Smoke only
mvn clean test -Dcucumber.filter.tags="@smoke"

# Negative tests only
mvn clean test -Dcucumber.filter.tags="@negative"
```

**Reports**

| Report | Path |
|--------|------|
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| Extent HTML | `target/extent-reports/ExtentReport.html` |

Requires **JDK 17+** and **Maven 3.9+**.

---

## What went where (and why)

```text
pom.xml / testng.xml
src/test/resources/
  config/config.properties     ← environment knobs (URL, timeouts, api key)
  features/users/*.feature     ← business-readable scenarios (no Java)
  payloads/*.json              ← request bodies as files
  cucumber.properties          ← Cucumber quiet settings
  extent.properties            ← ExtentReports output paths
src/test/java/com/framework/
  runners/TestRunner.java      ← TestNG ↔ Cucumber bridge
  hooks/Hooks.java             ← @Before/@After lifecycle
  stepdefinitions/            ← Given/When/Then glue + assertions
  api/BaseClient.java          ← RestAssured baseURI, headers, logging
  api/UserApi.java             ← endpoint methods ONLY (no asserts)
  models/                      ← request/response POJOs
  context/TestContext.java     ← share Response across step classes
  config/ConfigManager.java    ← read properties (+ system overrides)
  utils/JsonUtils.java         ← load payloads from classpath
```

### Layer map

| Layer | Responsibility | Why it exists |
|-------|----------------|---------------|
| **`.feature` files** | Describe behaviour in business language | Product/QA can read scenarios without Java; tags (`@smoke`) select suites |
| **`TestRunner`** | Starts Cucumber from TestNG | Maven Surefire discovers TestNG; Cucumber needs a runner entry point |
| **`Hooks`** | Reset state; attach failure details | Every scenario must start clean; failures need evidence in reports |
| **`stepdefinitions`** | Map Gherkin → Java; assert outcomes | Glue code: orchestration + assertions live here, not in API clients |
| **`UserApi` / `BaseClient`** | Perform HTTP calls | Reuse endpoints across steps; keep HTTP details out of Gherkin glue |
| **`TestContext`** | Hold last `Response` / scenario data | Cucumber instantiates step classes separately — shared state needs a holder |
| **`ConfigManager`** | Externalize URL/timeouts/keys | Same code, different env (local/CI) via properties or `-D` flags |
| **`models` + `payloads`** | Typed bodies vs file bodies | Learn both styles enterprises use |

### Request flow

```text
Feature line
  → Step definition (UserSteps / CommonSteps)
    → UserApi method
      → BaseClient.spec() (URL, headers, logging)
        → ReqRes API
    → store Response in TestContext
  → Then step asserts status / fields
```

---

## Separation rule (enterprise habit)

1. **Features** = behaviour only (no URLs, no JSONPath jargon if avoidable)
2. **Steps** = orchestration + assertions
3. **API clients** = HTTP only — **never** `Assert` inside `UserApi`
4. **Config/utils** = infrastructure reused by everything

---

## How to add a new endpoint (end-to-end)

1. Add method in `api/UserApi.java` (or a new `*Api` class).
2. Add/extend a `.feature` under `features/`.
3. Add step methods in `stepdefinitions` that call the API and store `Response` in `TestContext`.
4. Reuse `CommonSteps` for status/field checks when possible.
5. Run: `mvn test` or filter with a tag.

---

## Sample scenarios included

| Tag | Behaviour |
|-----|-----------|
| `@smoke @get` | GET user `1` → 200 + email present |
| `@negative @get` | GET user `999` → 404 |
| `@smoke @create` | POST from `payloads/create_user.json` → 201 |
| `@create` | POST inline POJO → 201 |
| `@update` | PUT user → 200 |
| `@delete` | DELETE user → 200 |

---

## Teaching notes (quick)

1. **`pom.xml` owns dependencies** — RestAssured, Cucumber, TestNG, Jackson, logging, Extent adapter. Version bumps happen in one place.
2. **Features stay free of Java** so non-developers can review acceptance criteria.
3. **`UserApi` ≠ step defs** — if you put asserts in the client, you cannot reuse the same call for positive and negative cases cleanly.
4. **`TestContext` exists** because `When` and `Then` are different methods (often different classes); they need a shared place for the last response.
5. **TestNG runner** loads features via `@CucumberOptions`; Surefire runs `testng.xml` → `TestRunner` → scenarios.
6. **Optional API key** — leave `apiKey` blank for JSONPlaceholder. For ReqRes, set `baseUrl=https://reqres.in/api` and `apiKey` (or `-Dapi.key=...`) from https://app.reqres.in/api-keys.
