# TestNG — Annotations, Lifecycle, Data Providers

## Say it right
> "Suite → Test → Class → Groups → Method. Driver setup in `@BeforeMethod`, `quit()` in `@AfterMethod` — that keeps tests independent and parallel-safe. `@DataProvider` is data in code; `@Parameters` comes from `testng.xml`."

---

## Order of execution

| Annotation | Runs |
|---|---|
| `@BeforeSuite` | Once, before the whole suite |
| `@BeforeTest` | Before each `<test>` tag in `testng.xml` |
| `@BeforeClass` | Once per class |
| `@BeforeGroups` | Before the first method of a group |
| `@BeforeMethod` | **Before every `@Test`** ← set up the driver |
| `@Test` | The test |
| `@AfterMethod` | After every test |
| `@AfterGroups` | After the last method of a group |
| `@AfterClass` | Once per class |
| `@AfterTest` | After the `<test>` tag |
| `@AfterSuite` | Once, at the end |

Mnemonic: **Suite → Test → Class → Groups → Method** (`@After`s unwind in reverse).

---

## `@Test` attributes worth naming
```java
@Test(priority = 1, groups = {"smoke"},
      dependsOnMethods = {"login"},
      enabled = true,
      retryAnalyzer = RetryAnalyzer.class,
      dataProvider = "loginData")
```
- `priority` — lower runs first (default 0); use sparingly
- `dependsOnMethods` / `dependsOnGroups` — **skip** instead of fail if the dependency fails
- `enabled = false` — skip
- Heavy `dependsOn` creates order coupling → flakiness

---

## `@DataProvider` vs `@Parameters`

### `@DataProvider` — data in code, N runs of one test
```java
@DataProvider(name = "loginData")
public Object[][] data() {
    return new Object[][] { {"user1","pass1"}, {"user2","pass2"} };
}

@Test(dataProvider = "loginData")
public void login(String user, String pass) { ... }
```

### `@Parameters` — values from `testng.xml`
```java
@Parameters({"browser"})
@BeforeMethod
public void setUp(String browser) { ... }
```
```xml
<test name="smoke">
  <parameter name="browser" value="chrome"/>
</test>
```

One-liner: **"`@DataProvider` = data-driven within code; `@Parameters` = config from `testng.xml`."**

---

## Other annotations
- `@Factory` — build test-class instances at runtime (e.g. one per browser).
- `@Listeners` — attach listeners to a class. (Prefer `testng.xml` for globals.)

---

## Memorize
1. **"Suite → Test → Class → Groups → Method. Driver setup in `@BeforeMethod`, `quit()` in `@AfterMethod`."**
2. **"`@DataProvider` = data in code; `@Parameters` = values from testng.xml; `priority`/`dependsOnMethods`/`groups` control execution."**
3. **"`@BeforeSuite`/`@AfterSuite` = whole suite; `@BeforeClass`/`@AfterClass` = per class; `enabled=false` skips a test."**

## Likely follow-ups
- Run the same test with many datasets? → `@DataProvider`.
- Pass browser/env without changing code? → `@Parameters` + `testng.xml`.
- Why is setup in `@BeforeMethod` and not `@BeforeClass`? → clean state per test, parallel-safe.
- `@BeforeTest` vs `@BeforeMethod`? → per `<test>` tag vs per test method.
