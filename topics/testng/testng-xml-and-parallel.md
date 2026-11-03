# TestNG — testng.xml & Parallel Execution

## Say it right
> "`testng.xml` is the **suite definition** — it declares which `<test>`s, `<classes>`, `<methods>`, or `<packages>` run, plus **groups**, **parameters**, **listeners**, and the **parallel mode with thread-count**. To run in parallel I move the driver to a **`ThreadLocal`** so each thread gets its own browser session, and I keep test classes stateless."

---

## testng.xml structure
```xml
<suite name="Regression" parallel="methods" thread-count="5">
  <listeners>
    <listener class-name="utils.TestListener"/>
  </listeners>

  <test name="Smoke">
    <parameter name="browser" value="chrome"/>
    <groups>
      <run><include name="smoke"/></run>
    </groups>
    <classes>
      <class name="tests.LoginTest"/>
    </classes>
  </test>
</suite>
```

- `<suite>` — top level, holds `parallel` + `thread-count`
- `<test>` — a logical group of classes (also holds `<parameter>`)
- `<classes>` / `<methods>` / `<packages>` — what actually runs
- `<groups>` — `<run><include name="smoke"/><exclude name="broken"/></run>`
- `<listeners>` — global listeners

### Maven hook
```xml
<plugin>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
    <suiteXmlFiles><suiteXmlFile>testng.xml</suiteXmlFile></suiteXmlFiles>
  </configuration>
</plugin>
```

---

## Parallel modes
`parallel="methods"` | `"classes"` | `"tests"` | `"instances"`

- `methods` — fastest, but the test-class instance is **shared across threads** → class must be stateless
- `classes` — safest default
- `tests` — parallelise whole `<test>` tags (e.g. per browser/env)
- DataProvider parallel: `-Ddataproviderthreadcount=N`

---

## The ThreadLocal driver
```java
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() { return driver.get(); }
    public static void setDriver(WebDriver d) { driver.set(d); }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();        // prevents leaks in thread pools
        }
    }
}
```

---

## Depth ladder — "what else makes it parallel-safe?" (the differentiator)
1. **No mutable state in the test class** — the class instance can be shared; use locals or ThreadLocal.
2. **`driver.remove()`** in teardown — thread pools reuse threads, otherwise leak/stale driver.
3. **Unique test data per thread** — no two threads on the same account.
4. **Thread-safe reporting** — `ExtentReports` is fine, but keep the `ExtentTest` in a `ThreadLocal` too.
5. **No static/global mutable state** — the #1 cause of parallel flakiness.

> Killer line: **"`parallel='classes'` is usually the safest default; `methods` is faster but needs a stateless test class."**

---

## Memorize
1. **"`testng.xml` declares suites/tests/classes/groups/parameters/listeners + `parallel` and `thread-count`."**
2. **"Parallel safety = `ThreadLocal<WebDriver>` + `driver.remove()` + no shared mutable state + unique test data."**
3. **"`parallel` = methods | classes | tests | instances. `classes` is the safest default."**

## Likely follow-ups
- Why ThreadLocal instead of static driver? → static = one browser shared by all threads → chaos.
- Why `remove()`? → thread pools reuse threads; without it the slot leaks.
- How do you run smoke vs regression? → groups/tags in `testng.xml` or the runner.
- Which parallel mode do you prefer? → `classes`, unless the class is stateless.
