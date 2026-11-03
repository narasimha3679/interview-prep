# TestNG — Listeners & Reporting (ExtentReports)

## Say it right
> "A listener is an **event hook** — TestNG fires events during a run and the listener reacts. `ITestListener` handles pass/fail/skip for reporting and screenshots; `ISuiteListener` initialises the report at the start and calls **`flush()`** at the end. TestNG has no native report, so I pair it with **ExtentReports**."

---

## What a listener is
TestNG fires **events** (suite start → test start → pass/fail/skip → suite end). A listener implements TestNG's interfaces and reacts. That's how a report is built — nobody hand-writes it.

| Listener | Fires on | Used for |
|---|---|---|
| `ITestListener` | test start / success / failure / skip | reporting, screenshots |
| `ISuiteListener` | suite start / finish | init report, **`flush()`** |
| `IInvokedMethodListener` | every method (config + test) | logging, timing |
| `IAnnotationTransformer` | before execution | set retry, disable tests |
| `IReporter` | end of run | fully custom report |
| `IExecutionListener` | run start/finish | global setup |

---

## How to register (3 ways — know all 3)
1. `@Listeners(TestListener.class)` on the class.
2. **`testng.xml`** (global, preferred):
```xml
<listeners><listener class-name="utils.TestListener"/></listeners>
```
3. **ServiceLoader** — auto-applies to every run:
```
src/test/resources/META-INF/services/org.testng.ITestListener
  → contains: utils.TestListener
```

---

## The code

### Thread-safe test holder
```java
public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static ExtentTest getTest() { return test.get(); }
    public static void setTest(ExtentTest t) { test.set(t); }
}
```

### Report singleton
```java
public class ExtentManager {
    private static ExtentReports extent;
    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
            spark.config().setDocumentTitle("Regression Report");
            spark.config().setReportName("Automation");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
```

### The listener
```java
public class TestListener implements ITestListener, ISuiteListener {

    @Override public void onStart(ISuite suite) { ExtentManager.getInstance(); }   // init

    @Override public void onTestStart(ITestResult r) {
        ExtentTest t = ExtentManager.getInstance().createTest(r.getName());
        ExtentTestManager.setTest(t);
    }

    @Override public void onTestSuccess(ITestResult r) { ExtentTestManager.getTest().pass("Passed"); }

    @Override public void onTestFailure(ITestResult r) {
        ExtentTest t = ExtentTestManager.getTest();
        t.fail(r.getThrowable());
        WebDriver d = DriverFactory.getDriver();
        if (d != null)
            t.addScreenCaptureFromBase64String(
                ((TakesScreenshot) d).getScreenshotAs(OutputType.BASE64));
    }

    @Override public void onTestSkipped(ITestResult r) { ExtentTestManager.getTest().skip(r.getThrowable()); }

    @Override public void onFinish(ISuite suite) { ExtentManager.getInstance().flush(); }  // writes file
}
```

---

## "How is the report I see daily actually generated?" (answer exactly this)
1. CI runs `mvn test` with `testng.xml`.
2. TestNG executes and **fires events**.
3. The listener: `onStart` → init report · `onTestStart` → create node · `onSuccess/Failure/Skipped` → write status + attach screenshot on failure · `onFinish` → **`flush()`** → HTML on disk.
4. HTML published as a **CI artifact** (GitHub Actions `upload-artifact` / Jenkins HTML Publisher) or shared link.
5. Optionally push results to the **test management tool** (Zephyr/qTest) via API.

---

## Gotchas
- **Forgetting `flush()`** → empty report. (= the `assertAll()` of reporting)
- **`ExtentReports` thread-safe, `ExtentTest` NOT** → keep `ExtentTest` in `ThreadLocal`.
- Nice detail: `createTest(name).assignAuthor("...").assignCategory("Regression")`.
- **Cucumber projects:** report also comes from Cucumber plugins — `plugin = {"html:target/cucumber.html", "json:target/cucumber.json", "pretty"}` — with Extent layered on the TestNG listener (each scenario runs as a TestNG test method).

---

## Memorize
1. **"Listeners = TestNG event hooks. `ITestListener` for pass/fail/screenshots, `ISuiteListener` for init + `flush()`."**
2. **"Register via `@Listeners`, `testng.xml`, or ServiceLoader."**
3. **"`ExtentReports` is thread-safe, `ExtentTest` is not → ThreadLocal. Forgetting `flush()` = empty report."**

## Likely follow-ups
- How does a screenshot get into the report? → `addScreenCaptureFromBase64String` in `onTestFailure`.
- Where do you init the report? → `ISuiteListener.onStart`; write it in `onFinish` via `flush()`.
- Does the listener work with Cucumber? → yes, via the `AbstractTestNGCucumberTests` runner (scenarios are TestNG test methods).
- How does the report reach the team? → CI artifact / published HTML link.
