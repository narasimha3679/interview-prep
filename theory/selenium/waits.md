# Selenium — Waits

## 30-second answer
> "I prefer **explicit waits** for element-level conditions and avoid `Thread.sleep`. Implicit wait is set once, globally, and only covers element *presence*."

---

## Code you must be able to write cold

```java
// Implicit — set ONCE, applies to every findElement
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

// Explicit — targeted, per condition
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
login.click();
```

```java
// FluentWait — when you need custom polling + ignored exceptions
FluentWait<WebDriver> wait = new FluentWait<>(driver)
        .withTimeout(Duration.ofSeconds(15))
        .pollingEvery(Duration.ofMillis(300))
        .ignoring(NoSuchElementException.class);
wait.until(d -> d.findElement(By.id("login")).isDisplayed());
```

---

## The 4 levels of depth

### Level 1 — Which and why
Explicit waits, because they are targeted and cover any condition. `Thread.sleep` is banned (wastes time, flaky).

### Level 2 — Why explicit over implicit
Implicit **only covers presence**. It cannot wait for:
- element to become **visible** (`display:none`)
- element to be **clickable** (visible + enabled)
- text to **appear / change**
- element to **disappear**

Explicit wait handles all of these as conditions:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("spinner")));
wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "Success"));
```

### Level 3 — Internals

| | Implicit | Explicit |
|---|---|---|
| Where it runs | On the **driver** (remote/server side) | In **your client code** (polls the DOM) |
| Scope | Globally, every findElement | Only where you call it |
| Polling | Driver handles internally | **`FluentWait` polls every 500 ms** by default |
| On timeout | Throws `NoSuchElementException` | Throws **`TimeoutException`** |
| Custom exceptions | No | Yes (`.ignoring(...)`) |

`WebDriverWait` **extends `FluentWait`** → timeout + polling interval + ignored exceptions.

### Level 4 — Traps
- **Using both together = bad.** Timeouts **compound**; failure can take far longer and behaviour is unpredictable. Industry rule: **use explicit, set implicit to 0**.
- **Stale element** → element was found, page re-rendered → `StaleElementReferenceException`. Explicit wait **re-locates on each poll**, so wrap the action in a wait/retry.
- **`presenceOfElementLocated` vs `visibilityOfElementLocated`** → presence = in DOM (may be hidden); visibility = actually displayed (size > 0, not `display:none`/`visibility:hidden`).

---

## Memorize these 3 lines
1. **"Implicit = global, presence only, runs on the driver. Explicit = targeted, any condition, runs in my code via FluentWait polling every 500 ms."**
2. **"Never mix them — timeouts compound. I use explicit, implicit 0."**
3. **Explicit timeout → `TimeoutException`; implicit timeout → `NoSuchElementException`.**

---

## Likely follow-ups
- Default polling interval of explicit wait? → **500 ms**
- Which exception on explicit timeout? → `TimeoutException`
- Fastest fix for a greyed-out (present but disabled) button? → `ExpectedConditions.elementToBeClickable(...)`
- How to wait for a spinner to disappear? → `invisibilityOfElementLocated`
