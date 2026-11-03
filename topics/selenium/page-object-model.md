# Selenium — Page Object Model (POM)

## 30-second answer
> "POM is a design pattern where each **page is a Java class**. Locators and page actions live **inside the class**; tests contain only test logic. It gives maintainability (one place to fix a locator), reusability, and readability."

---

## Code you must be able to write cold

### Plain POM (preferred by many teams today)
```java
public class LoginPage {

    private WebDriver driver;

    private By username  = By.id("username");
    private By password  = By.id("password");
    private By loginBtn  = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage login(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();
        return new HomePage(driver);          // page chaining
    }
}
```

### With PageFactory
```java
public class LoginPage {

    private WebDriver driver;

    @FindBy(id = "username")  private WebElement username;
    @FindBy(id = "password")  private WebElement password;
    @FindBy(css = "button[type='submit']") private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        // or lazy + timeout:
        // PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    public HomePage login(String user, String pass) {
        username.sendKeys(user);
        password.sendKeys(pass);
        loginBtn.click();
        return new HomePage(driver);
    }
}
```

### The test
```java
public class LoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://example.com/login");
    }

    @Test
    public void validLogin() {
        LoginPage loginPage = new LoginPage(driver);   // no locators in the test
        HomePage home = loginPage.login("user", "pass");
        Assert.assertTrue(home.isLoaded());
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
```

---

## The 4 levels of depth

### Level 1 — What & why
- Each page = one class. Locators + actions inside. Tests only call page methods.
- Benefits: **maintainability** (locator changes in one place), **reusability**, **readability**, no duplication.

### Level 2 — PageFactory vs plain POM
- `@FindBy` declares elements; `PageFactory.initElements(driver, this)` wires them up in the constructor.
- `@FindBy` fields are **lazy proxies** → the element is located **when first used**, not at init.
- `AjaxElementLocatorFactory(driver, timeout)` adds a built-in wait for those proxies.
- Alternate syntax: `@FindBy(how = How.ID, using = "username")`.

### Level 3 — Going deeper
- **Page chaining / fluent POM:** a method returns the **next page object** (`return new HomePage(driver);`) → readable flows.
- **BasePage:** shared `driver`, shared `wait`, common helpers — pages extend it.
- **Never expose locators to tests.** Tests should read like business steps.
- Folder structure: `pages/`, `tests/`, `base/`, `utils/`, `constants/`.
- `LoadableComponent<T>` is the built-in alternative for load-verification (`isLoaded()` / `load()`).

### Level 4 — Traps
- **Why do some teams avoid PageFactory?** → locators are hidden inside annotations (harder to reuse/parameterize), and `WebElement` fields can go **stale**; plain `By` + explicit wait is often more robust.
- **POM vs utility class?** → POM is per-page state + behavior; utils are stateless helpers.
- **Does POM help parallel execution?** → Yes, if `driver` is **not static** — each thread gets its own `driver` (ThreadLocal in the base class).

---

## Memorize these 3 lines
1. **"POM = one class per page; locators + actions inside, tests only hold test logic."**
2. **"`PageFactory.initElements(driver, this)` + `@FindBy` = lazy proxies; `AjaxElementLocatorFactory` adds a wait."**
3. **"Page chaining: methods return the next Page Object. `driver` must be non-static for parallel runs."**

---

## Likely follow-ups
- Difference between POM and PageFactory? → POM is the pattern; PageFactory is one implementation using annotations.
- Where do you keep locators? → In the page class, private. ✓
- Why prefer plain POM today? → reusable `By` locators, explicit waits, fewer stale-element issues.
- Base class purpose? → shared driver/wait/helpers; pages extend it.
