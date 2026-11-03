# TestNG — Annotations & Execution Order

## Say it right
> "Suite → Test → Class → Method, each with a Before/After. `@BeforeMethod` runs before **every** test method; `@BeforeTest` runs once per `<test>` tag in the XML — that's a common confusion."

---

## Execution order
```
@BeforeSuite
  └─ @BeforeTest          ← per <test> tag in testng.xml, NOT per @Test method
       └─ @BeforeClass
            └─ @BeforeMethod
                 └─ @Test
            └─ @AfterMethod
       └─ @AfterClass
  └─ @AfterTest
@AfterSuite
```
`@BeforeGroups` / `@AfterGroups` run around the first/last method of a **group**.

---

## Annotations you actually use
```java
@Test(priority = 1,
      groups = {"smoke"},
      dependsOnMethods = {"loginTest"},
      enabled = true,
      invocationCount = 3,
      timeOut = 5000,
      expectedExceptions = ArithmeticException.class,
      description = "Login with valid creds")
public void loginTest() { }
```
- `priority` → order (lower first)
- `groups` → smoke/regression selection
- `dependsOnMethods` → fail-fast dependency
- `invocationCount` → repeat (flakiness check)
- `enabled = false` → skip
- `timeOut` → fail if it runs too long
- `expectedExceptions` → assert an exception is thrown

---

## `@Parameters` — static config from testng.xml
```xml
<parameter name="browser" value="chrome"/>
```
```java
@Parameters({"browser"})
@BeforeMethod
public void setUp(String browser) { driver = DriverFactory.getDriver(browser); }
```

## `@DataProvider` — dynamic data from code
```java
@DataProvider(name = "loginData", parallel = true)
public Object[][] loginData() {
    return new Object[][] { {"user1","pass1"}, {"user2","pass2"} };
}

@Test(dataProvider = "loginData")
public void login(String user, String pass) { /* ... */ }
```
Cross-class: `@Test(dataProvider = "loginData", dataProviderClass = DataProviders.class)`.

---

## Depth ladder
**L2 — distinctions**
| | `@Parameters` | `@DataProvider` |
|---|---|---|
| Source | testng.xml | Java code |
| Values | single set | multiple rows → test runs N times |
| Parallel | no | **yes** |
| Good for | env/browser config | test data |

- `@BeforeTest` (per `<test>` tag) vs `@BeforeMethod` (per method) ⚠️ *most-asked trick*
- **`Assert` vs `SoftAssert`** → hard `Assert` stops on first failure; `SoftAssert` records all failures then `softAssert.assertAll()`. Best for verifying many fields on one page.

**L3 — parallel**
```xml
<suite name="Suite" parallel="methods" threadCount="5">
```
`parallel="methods" | "classes" | "tests"` → driver must be **ThreadLocal**.

**L4 — traps**
- `dependsOnMethods` fails → dependents are **skipped**, not failed. Use `alwaysRun = true` to run regardless.
- `priority` + `dependsOnMethods` can conflict; `priority` isn't guaranteed across classes.
- `ITestListener` (`onTestFailure`, `onTestSuccess`) is where you hook **screenshots + reporting**.

---

## Memorize
1. **"Order: Suite → Test → Class → Method. `@BeforeTest` is per XML `<test>` tag, `@BeforeMethod` is per test method."**
2. **"`@Parameters` = config from XML. `@DataProvider` = data from code, supports parallel, runs the test N times."**
3. **"SoftAssert collects all failures, reports at `assertAll()`. Listeners (`ITestListener`) drive screenshots and reports."**

## Likely follow-ups
- TestNG vs JUnit? → suites/groups/dependsOn/parallel/data providers; XML-driven; richer config.
- How do you skip a test? → `enabled = false` or `@Ignore`-equivalent via XML exclude.
- How do you retry a failed test? → `IRetryAnalyzer` on the `@Test`.
- How do you run a single test from CLI? → `mvn test -Dtest=LoginTest#loginTest`.
