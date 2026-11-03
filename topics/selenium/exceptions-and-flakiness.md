# Selenium — Exceptions & Flakiness

## Say it right
> "NoSuchElement = not found. Stale = found then re-rendered → re-locate. NotInteractable = present but can't interact. ClickIntercepted = something covering it. Flakiness comes from timing and shared state, not from Selenium itself."

---

## The exception wallet

| Exception | Means | Fix |
|---|---|---|
| **`NoSuchElementException`** | Element not in DOM (wrong locator / not loaded yet) | Fix locator + **explicit wait** |
| **`StaleElementReferenceException`** | Element *was* there, DOM **re-rendered** — reference detached | **Re-locate** the element |
| **`ElementNotInteractableException`** | Present but **can't be interacted with** (hidden, disabled, off-screen) | Wait visibility/clickable, scroll into view |
| **`ElementClickInterceptedException`** | **Something is covering it** (overlay, spinner, modal) | Wait for overlay to go away |
| **`TimeoutException`** | Explicit wait condition not met in time | Correct/longer condition |
| **`NoSuchWindowException`** | Window handle is gone | Re-fetch `getWindowHandles()` |
| **`NoSuchFrameException`** | Frame not found / not ready | Wait, then switch |
| **`InvalidSelectorException`** | Malformed locator | Fix the selector |
| **`UnhandledAlertException`** | Alert blocking the driver | Handle the alert first |
| **`NoAlertPresentException`** | `accept()` when no alert exists | Guard with `alertIsPresent()` |
| **`InvalidElementStateException`** | e.g. sendKeys into a read-only field | Check field state |
| **`SessionNotCreatedException`** | Browser ↔ driver version mismatch | Fix driver/browser versions |

---

## StaleElementReferenceException — the ladder

**L1 — what it is**
> "The element was located successfully, but by the time I used it the DOM had **re-rendered**, so my reference is no longer attached to the page."

**L2 — why**
Page refresh/navigation, **AJAX partial update**, React/Angular re-render, modal close/reopen. **PageFactory cached `WebElement`s** are a classic cause.

**L3 — the fix**
> "I **re-locate** the element — never hold a `WebElement` across actions. In a framework I wrap it in a retry or use `ExpectedConditions.refreshed(...)`."

```java
WebElement el = wait.until(
    ExpectedConditions.refreshed(
        ExpectedConditions.elementToBeClickable(By.id("submit"))));
el.click();
```

Or a retry loop catching `StaleElementReferenceException` and re-finding.

**L4 — framework level**
- **`RetryAnalyzer`** (TestNG) for transient failures.
- Prefer **plain `By` + explicit wait** over PageFactory proxies → fewer stale issues.
- Never mix implicit + explicit waits (it *creates* flakiness).

---

## Flaky test checklist (great to say out loud)
1. Explicit waits, **no `Thread.sleep`**
2. Never mix implicit + explicit waits
3. No test-order dependency / shared state
4. Fresh test data per run
5. Screenshot on failure for debugging
6. Retry only transient failures — **never hide real bugs**

---

## Memorize
1. **"NoSuchElement = not found. Stale = found then re-rendered → re-locate. NotInteractable = can't interact. ClickIntercepted = something is covering it."**
2. **"Fix stale by re-locating, or `ExpectedConditions.refreshed(...)`. Never hold a WebElement across actions."**
3. **"Flakiness fixes: explicit waits, no `Thread.sleep`, no implicit+explicit mixing, no test-order dependency, clean test data."**

## Likely follow-ups
- Difference between NotInteractable and ClickIntercepted? → can't interact vs something else is on top of it.
- How do you debug a flaky test? → run repeatedly, screenshots/logs on failure, look for races and shared data.
- Would you use JS click? → only as a last resort; it bypasses real user behavior.
