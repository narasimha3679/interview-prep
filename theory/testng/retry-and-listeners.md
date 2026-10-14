# TestNG — Retry, Listeners, Screenshots

## Say it right
> "`IRetryAnalyzer` for transient failures, applied per-test or globally via `IAnnotationTransformer`. Cucumber retries with the `rerun` plugin, or through the TestNG runner. And I only retry known-transient failures — never to hide bugs."

---

## IRetryAnalyzer
```java
public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX = 1;          // 1 retry

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX) { count++; return true; }
        return false;
    }
}
```
Attach per test: `@Test(retryAnalyzer = RetryAnalyzer.class)`

### Apply globally — `IAnnotationTransformer`
```java
public class RetryTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation a, Class c, Constructor ct, Method m) {
        a.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
```
Register it in `testng.xml` (`<listeners>`) or `@Listeners`.

### Depth nuance (impresses interviewers)
The analyzer instance can be **reused across invocations**, so the retry count sometimes carries over. Robust fix: track attempts **per test method**:
```java
private static final Map<String,Integer> attempts = new HashMap<>();
```

---

## Cucumber retry — two real answers

### 1. Rerun plugin (standard)
```java
@CucumberOptions(
    plugin = {"rerun:target/rerun.txt", "html:target/report.html"}
)
```
First run writes failed scenarios to `rerun.txt`; re-run only those:
```
mvn test -Dcucumber.features="@target/rerun.txt"
```
Great for CI (fail fast, then re-run the flaky ones once).

### 2. Via the TestNG runner
`TestRunner extends AbstractTestNGCucumberTests` → each scenario executes as a **TestNG test method**, so TestNG listeners/retry mechanisms can apply there.

> Never retry blindly — retrying a real bug just wastes CI time and hides it.

---

## Screenshot on failure (with ITestResult)
```java
@AfterMethod
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        byte[] shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        // attach to Extent report / save to target/screenshots
    }
    driver.quit();
}
```
`ITestResult` gives: `getStatus()` (SUCCESS/FAILURE/SKIP), `getName()`, `getThrowable()`, `getMethod()`.

---

## Listeners (name these)
| Listener | Use |
|---|---|
| `ITestListener` | `onTestStart/onTestSuccess/onTestFailure/onTestSkipped` — reporting, screenshots |
| `ISuiteListener` | suite start/finish (build the Extent report) |
| `IInvokedMethodListener` | before/after every method — useful for logging |
| `IRetryAnalyzer` | retry logic |
| `IAnnotationTransformer` | change annotations at runtime |

TestNG itself has **no built-in reporting** → we pair it with **ExtentReports**.

---

## Wire it together — `ITestListener` for Extent
```java
public class TestListener implements ITestListener {
    @Override public void onTestStart(ITestResult r)   { test = extent.createTest(r.getName()); }
    @Override public void onTestSuccess(ITestResult r) { test.pass("Passed"); }
    @Override public void onTestFailure(ITestResult r) { test.fail(r.getThrowable()); }
    @Override public void onTestSkipped(ITestResult r) { test.skip("Skipped"); }
}
```

---

## Memorize
1. **"`IRetryAnalyzer` per test; `IAnnotationTransformer` to apply globally. Track attempts per method to avoid count carry-over."**
2. **"Cucumber retry = `rerun` plugin (rerun.txt) or TestNG runner. Retry only transient failures."**
3. **"Screenshot on failure in `@AfterMethod` using `ITestResult.getStatus()==FAILURE`; TestNG has no native report, so use ExtentReports via `ITestListener`."**

## Likely follow-ups
- How do you report pass/fail with a screenshot? → `ITestListener.onTestFailure` + ExtentReports.
- Difference between `ITestListener` and `IInvokedMethodListener`? → test-level events vs every method invocation.
- When would you NOT retry? → real functional failures, or when you've hit the retry cap.
