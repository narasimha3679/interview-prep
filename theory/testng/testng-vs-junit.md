# TestNG — TestNG vs JUnit & Other @Test Attributes

## Say it right
> "TestNG gives me features I rely on daily that JUnit historically didn't: **native parallel execution**, **`@DataProvider`**, **groups**, **`dependsOnMethods`**, **retry via `IRetryAnalyzer`**, and a **flexible `testng.xml` suite** plus listeners for reporting. JUnit 5 has closed some of that gap, but for a Selenium/Cucumber framework TestNG's suite model and listeners make it the practical choice."

---

## Comparison table
| Feature | TestNG | JUnit 5 |
|---|---|---|
| Parallel execution | ✅ native (`parallel`, `thread-count`) | ⚠️ limited / opt-in |
| Parameterized data | `@DataProvider` | `@ParameterizedTest` + `@MethodSource` / `@CsvSource` |
| Grouping | `groups` | `@Tag` |
| Dependencies between tests | ✅ `dependsOnMethods` | ❌ not supported |
| Retry | ✅ `IRetryAnalyzer` | ❌ needs a custom extension |
| Suite config | `testng.xml` | JUnit Platform / Surefire config |
| Listeners | `ITestListener` | `Extension` model |
| Ordering control | `priority` | `@Order` |
| Reports | pairs with ExtentReports | pairs with Allure/Extent |

Always give JUnit its due: JUnit 5 is more modern (`@Nested`, constructor injection, better IDE support). Choose by project.

---

## Other `@Test` attributes
```java
@Test(expectedExceptions = ArithmeticException.class)  // assert an exception is thrown
@Test(timeOut = 2000)                                  // fail if slower than 2s
@Test(invocationCount = 5)                             // run 5 times
@Test(priority = 1)                                    // ordering
```

---

## Memorize
1. **"TestNG: parallel, `@DataProvider`, groups, `dependsOnMethods`, `IRetryAnalyzer`, `testng.xml` + listeners. JUnit 5 is more modern but lighter on suite/retry features."**

## Likely follow-ups
- Does JUnit support retry? → not built-in, needs an extension.
- How do you assert an exception? → `@Test(expectedExceptions = ...)` or try/catch with fail().
- Why is TestNG common in Selenium projects? → suite XML, parallel, listeners, parameterization.
