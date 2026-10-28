# Cucumber / BDD — Reusable UI Login

## Say it right
> "If login is **setup**, I use a **tagged `@Before` hook** (or cookie injection for speed). If login is **under test**, I use an explicit **`Given` step** so it's visible in the report. The action itself always lives in a **page object**, and the driver/pages are shared via **PicoContainer**."

---

## Building blocks

### 1. Page object (reusable action)
```java
public class LoginPage {
    private final WebDriver driver;
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By loginBtn = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) { this.driver = driver; }

    public HomePage login(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();
        return new HomePage(driver);
    }
}
```

### 2. Share state with PicoContainer (DI)
Add `cucumber-picocontainer` to the pom.
```java
public class ScenarioContext {
    private WebDriver driver;
    public WebDriver getDriver() { return driver; }
    public void setDriver(WebDriver d) { this.driver = d; }
    public LoginPage loginPage() { return new LoginPage(driver); }
}
```
```java
public class Hooks {
    private final ScenarioContext ctx;
    public Hooks(ScenarioContext ctx) { this.ctx = ctx; }   // same instance injected

    @Before public void setUp() { ctx.setDriver(DriverFactory.create()); }
    @After  public void tearDown() { ctx.getDriver().quit(); }
}
```
Cucumber injects the **same instance** into every class that declares it in the constructor → that's the reuse mechanism.

---

## The 4 ways to reuse login

### Option 1 — Tagged `@Before` hook ← best for incidental login
```java
public class AuthHooks {
    private final ScenarioContext ctx;
    public AuthHooks(ScenarioContext ctx) { this.ctx = ctx; }

    @Before(value = "@requiresLogin", order = 1)     // after driver setup
    public void login() {
        ctx.loginPage().login(ConfigManager.get("user"), ConfigManager.get("password"));
    }
}
```
```gherkin
@requiresLogin
Scenario: View account balance
  Given I am on the accounts page
  Then I should see my balance
```
✅ Business-focused Gherkin, zero duplication.
❌ Login is **invisible** in the report.

### Option 2 — Reusable `Given` step ← best when login is part of the story
```java
@Given("I am logged in as {string}")
public void iAmLoggedInAs(String role) {
    Map<String, String> creds = TestData.credentials(role);
    ctx.loginPage().login(creds.get("user"), creds.get("pass"));
}
```
```gherkin
Given I am logged in as "admin"
```
✅ Visible in the report; reusable across **all** features (glue is global).

### Option 3 — `Background` (same feature file)
```gherkin
Feature: Accounts
  Background:
    Given I am logged in as "admin"

  Scenario: View balance
  Scenario: View statements
```

### Option 4 — Scenario Outline for multiple roles
```gherkin
Scenario Outline: Role access
  Given I am logged in as "<role>"
  Then I should see "<menu>"

  Examples:
    | role  | menu         |
    | admin | Manage Users |
    | user  | My Accounts  |
```

---

## 🪜 Advanced answer (stands out)
> "UI login per scenario is slow — a network round trip × hundreds of scenarios. So I **log in once, capture the session cookie / OAuth token from `localStorage`, and inject it per scenario** to skip the UI login. I keep **one** full end-to-end test that logs in through the real UI."

```java
Set<Cookie> cookies = ctx.getDriver().manage().getCookies();   // after one real login

driver.get(baseUrl);
cookies.forEach(c -> driver.manage().addCookie(c));            // refresh → already logged in
```

---

## Which to choose
> "Login as **setup** → tagged `@Before` hook (or cookie injection for speed). Login **under test** → explicit `Given` step so it shows in the report."

---

## Memorize
1. **"Reuse login: page object + PicoContainer-shared context; then tagged `@Before` hook, reusable `Given` step, or `Background`."**
2. **"Hook = login is setup (invisible in report); step = login is part of the test (visible)."**
3. **"For speed: log in once, reuse the cookie/token per scenario."**

## Likely follow-ups
- How do step definitions share the driver? → `cucumber-picocontainer` dependency injection.
- Why a hook and not a step? → login is setup, not behaviour under test.
- How do you speed up a big suite? → reuse the session cookie/token instead of UI login.
