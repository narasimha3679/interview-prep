# Selenium — Designing a Framework (capstone answer)

## The "shape first" opener (say this in the first 30 seconds)
> "I structure it in **layers**: build & config → driver management → page layer → test layer → test data → reporting/logging → CI/CD + integrations. Let me take you through each."

Never ramble the layers in random order. **State the shape, then walk it.**

---

## The 7 layers

### 0. Build & config
- Maven `pom.xml`: selenium, testng, cucumber, rest-assured, webdrivermanager, extentreports, log4j, poi.
- `config.properties` **per environment** — selected with `-Denv=qa`.
- `WebDriverManager` resolves driver binaries automatically.

### 1. Driver management
- `DriverFactory` creates the browser from a parameter (`browser`, `headless`).
- **`ThreadLocal<WebDriver>`** so parallel tests get their own session.
- `driver.quit()` in `@AfterMethod` / `AfterSuite`.

### 2. Page layer
- Page classes (POM) with **locators + actions**.
- Extend a `BasePage` holding waits/helpers.
- ⚠️ Selenium API calls live **here**, not in the base class.

### 3. Test / BDD layer
- Gherkin `.feature` files → step definitions → **runner**:
```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "stepdefinitions",
    tags = "@smoke",
    plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() { return super.scenarios(); }
}
```
- TestNG adds groups, parallel, assertions, listeners.

### 4. Test data
- External JSON/properties per env (accounts, URLs) — anything not generated on the fly.
- `Scenario Outline` / TestNG `DataProvider` for parameterization.

### 5. Reporting & logging
- ExtentReports + Cucumber HTML/JSON.
- **Screenshot on failure** (in `@AfterMethod` / listener).
- Log4j logs.

### 6. CI/CD
- GitHub Actions / Jenkins: **smoke on push/PR**, **regression on schedule**.
- Optional Docker / Selenium Grid for scale.

### 7. Integrations
- Test management (Zephyr / qTest) via **API token** in config, with an **on/off switch** to publish results.

---

## Terminology to get right
| Concept | Correct home |
|---|---|
| Selenium API calls | **Page / action classes** |
| Driver setup, teardown, waits | **Base class** |
| Wrapping Selenium + extra behavior | **Wrapper / Facade utility** |
| Cross-cutting behavior (screenshot, log, retry) | **Listeners** |

---

## Depth ladder
- **Parallel?** → TestNG `parallel` + **ThreadLocal driver**.
- **Multiple environments?** → `-Denv=qa` selects a config file; no code changes.
- **Smoke vs regression?** → **groups** (TestNG) / **tags** (Cucumber), chosen in runner or CI command.
- **Why TestNG + Cucumber?** → Cucumber = BDD/business layer; TestNG = execution control (data providers, parallel, listeners, reporting).

## Memorize
1. **"Layers: build/config → driver → pages → tests → data → reporting → CI + integrations."**
2. **"Driver is ThreadLocal for parallel. Locators live in page classes. Base class does setup/teardown."**
3. **"Tags drive smoke vs regression; `-Denv` drives environments; test-management upload is a config toggle."**
