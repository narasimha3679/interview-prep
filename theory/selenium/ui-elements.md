# Selenium — Handling UI Elements (dropdown, alert, windows, frames, hover)

## Say it right
> "Dropdown → `Select` class (native `<select>` only). Alert → `switchTo().alert()`. Multiple windows → `getWindowHandles()`. Frames → `switchTo().frame()`. Hover/right-click/drag → `Actions` class, and I must call `.perform()`."

---

## Key lines

### 1. Dropdown — `Select` (native `<select>` only)
```java
Select select = new Select(driver.findElement(By.id("country")));
select.selectByVisibleText("India");   // or selectByIndex / selectByValue
select.getOptions();                    // all options
select.isMultiple();
```
⚠️ **Custom dropdowns** (Bootstrap / React `<div><ul><li>`) → `Select` **fails** with `UnexpectedTagNameException`. Click the control, then click the option instead.

### 2. JavaScript alert
```java
driver.switchTo().alert().accept();     // OK
driver.switchTo().alert().dismiss();    // Cancel
driver.switchTo().alert().getText();
driver.switchTo().alert().sendKeys("text");
```
- Not in the DOM → cannot locate it.
- Wait with `ExpectedConditions.alertIsPresent()`.
- Ignoring an alert → `UnhandledAlertException`.

### 3. Multiple windows / tabs
```java
String parent = driver.getWindowHandle();
Set<String> all = driver.getWindowHandles();   // unordered Set

for (String h : all)
    if (!h.equals(parent)) { driver.switchTo().window(h); break; }

driver.close();                          // close the child
driver.switchTo().window(parent);        // ALWAYS go back
```
- `getWindowHandle()` = current; `getWindowHandles()` = all (Set, **no order guarantee**).
- Use `close()` for child, not `quit()`. `quit()` = whole session.

### 4. Frames / iframes
```java
driver.switchTo().frame("frameName");   // or index, or a WebElement
driver.switchTo().defaultContent();      // back to main page
driver.switchTo().parentFrame();         // one level up (nested frames)
```

### 5. Hover / right-click / drag — `Actions`
```java
new Actions(driver).moveToElement(menu).perform();
new Actions(driver).contextClick(el).perform();
new Actions(driver).doubleClick(el).perform();
new Actions(driver).dragAndDrop(src, tgt).perform();
```

---

## Depth ladder
- **L2:** `Select` only works on `<select>`; alerts aren't DOM; window handles are a **Set** (unordered); you must exit a frame.
- **L3:** `Actions` = **W3C Actions API**; `Select` is a thin wrapper over an ordinary `WebElement`; alerts go through a driver endpoint, not the DOM.
- **L4:** Forgetting `.perform()` → **silently nothing happens**. Nested frames need one `switchTo().frame()` **per level**.

---

## Memorize
1. **"Dropdown → `Select` (native only). Alert → `switchTo().alert()`. Windows → `getWindowHandles()`. Frame → `switchTo().frame()`. Hover → `Actions` + `perform()`."**

## Likely follow-ups
- Why won't `Select` work on our dropdown? → it's a custom div/ul, not a `<select>`.
- How do you get back to the main page after an iframe? → `switchTo().defaultContent()`.
- How do you close only the popup window? → `driver.close()` then switch to the parent handle.
- Alert isn't appearing on time? → `ExpectedConditions.alertIsPresent()`.
