# Cucumber / BDD — Where Data Comes From (and where @DataProvider fits)

## Say it right
> "In BDD, scenario data comes from **Scenario Outline `Examples`** or a **DataTable** — not `@DataProvider`. `@DataProvider` shows up in the **Cucumber runner's `scenarios()` override** (which makes scenarios run as TestNG tests and enables parallelism) — and in a **hybrid framework** for plain TestNG / REST Assured API tests."

---

## 1. Where BDD data comes from

### Scenario Outline + Examples (main one)
```gherkin
Scenario Outline: Login
  Given I login as "<user>" with "<pass>"
  Then I should see "<message>"

  Examples:
    | user  | pass   | message       |
    | admin | admin1 | Welcome       |
    | bad   | bad    | Invalid login |
```
**Each row runs as a separate scenario** → both appear individually in the report.

### DataTable in a step
```java
@When("I login with the following details")
public void login(DataTable table) {
    Map<String, String> data = table.asMap(String.class, String.class);
    loginPage.login(data.get("username"), data.get("password"));
}
```

### External files read inside the step
Same JSON/Excel/Properties utils — just called from a step definition instead of a `@DataProvider`.

---

## 2. Where `@DataProvider` actually appears

### a) The Cucumber runner (important)
```java
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)          // one row per scenario → TestNG test methods
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
```
> **This is how Cucumber-on-TestNG parallelises.** Cucumber alone does not parallelise — the runner's `scenarios()` DataProvider turns each scenario into a TestNG test method.

### b) Hybrid framework
UI via Cucumber/BDD, **API/REST Assured tests as plain TestNG** using `@DataProvider` (data-driven API tests don't need Gherkin).

---

## Memorize
1. **"BDD data = Scenario Outline `Examples` or `DataTable`. `@DataProvider` = the runner's `scenarios()` (parallelism) or plain TestNG/API tests."**
2. **"Each `Examples` row = a separate scenario in the report."**
3. **"Cucumber doesn't parallelise by itself — the TestNG runner's `@DataProvider(parallel=true)` does."**

## Likely follow-ups
- Can you use a DataProvider in a step definition? → Not natively; use Examples or read the file in the step.
- How do tags differ from groups? → Cucumber `@tags` filter scenarios; TestNG `groups` filter methods — in the runner the two meet.
- Where do hooks go? → `@Before`/`@After` (per scenario), `@BeforeStep`/`@AfterStep` (per step), `@BeforeAll`/`@AfterAll` (per feature).
