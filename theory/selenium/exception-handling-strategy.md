# Selenium Framework — Exception Handling Strategy

## Say it right
> "I don't catch exceptions everywhere — I handle them **where there's something meaningful to do**, and I never swallow them. The goal: **fail fast, fail with context, and never let a green test hide a broken feature.**"

---

## Layer by layer

**1. Driver layer — fail fast**
Wrap driver creation; if the browser can't start, throw a custom exception with browser/env in the message. No point continuing.

**2. Wait / action wrapper — the layer that DOES catch**
```java
public void click(By locator) {
    try {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    } catch (StaleElementReferenceException e) {
        wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.elementToBeClickable(locator))).click();   // re-locate once
    } catch (TimeoutException e) {
        throw new ElementActionException("Click timed out: " + locator, e);   // wrap + context
    }
}
```

**3. Page layer — don't catch.** Pages call the wrapper; let exceptions propagate. No try/catch noise.

**4. Test layer — no try/catch.** Let the test fail; the **listener + `ITestResult`** capture failure + screenshot.

**5. Reporting — guard it.** Screenshot capture inside try/catch so reporting never breaks the run.

**6. Teardown — always guard `quit()`**
```java
@AfterMethod(alwaysRun = true)
public void tearDown() {
    try { DriverFactory.quitDriver(); } catch (Exception e) { log.warn("quit failed", e); }
}
```
A close failure must **never mask the real test failure**.

**7. Retry — only known-transient failures.** `IRetryAnalyzer`, capped (1 retry), never blind.

---

## Custom exception hierarchy
```java
public class FrameworkException extends RuntimeException { ... }              // base infra
public class DriverInitializationException extends FrameworkException { ... }
public class ElementActionException extends FrameworkException { ... }        // locator + action
public class TestDataException extends FrameworkException { ... }
```

> Say: **"This separates infrastructure failures from product bugs. In a regression report, an `ElementActionException` points to the framework/environment; a failed assertion points to the app."**

---

## Anti-patterns to name
1. **`catch (Exception e) { }`** — swallowing → green tests, broken app. Never.
2. **Catching `AssertionError`** — assertions must fail the test.

---

## Memorize
1. **"Catch where there's something to do: retry in the wait wrapper, context in the page layer, capture in the listener. Never swallow."**
2. **"Custom `FrameworkException` hierarchy separates infra failures from product bugs."**
3. **"Guard `driver.quit()` in teardown so cleanup never masks the real failure."**

## Likely follow-ups
- Where exactly do you catch Selenium exceptions? → the wait/action wrapper (retry + wrap with context).
- Do you catch in test methods? → No — let them fail and be reported.
- How do you know it's an env failure vs a product bug? → custom exception hierarchy + logs/screenshots.
