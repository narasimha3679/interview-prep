# Selenium — Locators (and XPath vs CSS)

## 30-second answer
> "Priority: **id → name → CSS → XPath**. CSS is faster; XPath is for text matching, ancestors, and functions. For dynamic ids I match the stable part or use `data-testid`."

---

## Code you must be able to write cold

```java
By.id("username");
By.name("password");
By.cssSelector("button.submit");
By.xpath("//button[text()='Login']");

// Dynamic ids — match the stable part
By.xpath("//input[contains(@id,'user_')]");
By.xpath("//input[starts-with(@id,'user_')]");
By.cssSelector("input[id^='user_']");   // starts-with
By.cssSelector("input[id$='_name']");   // ends-with   (CSS CAN do this)
By.cssSelector("input[id*='user_']");   // contains

// Best fix — ask devs for a test attribute
By.cssSelector("[data-testid='login-btn']");

// Last resort — anchor to stable ancestor / label
By.xpath("//label[text()='Username']/following::input[1]");
```

---

## XPath can do 3 things CSS CANNOT
1. **Go UP the DOM** — parent/ancestor/siblings. CSS only goes forward/down (no parent selector).
2. **Match by text** — `//button[text()='Login']`, `//button[contains(text(),'Log')]`. CSS has no text matching.
3. **Use functions & logic** — `contains()`, `starts-with()`, `normalize-space()`, `and`/`or`, `[last()]`, `[position()]`.

**CSS wins on:** speed + simpler, more readable syntax.
→ Say: *"CSS when possible, XPath when I need text, an ancestor, or functions."*

---

## The 4 levels of depth

### Level 1 — Priority order
`id` → `name` → `cssSelector` → `xpath`. Never build locators off volatile text unless necessary.

### Level 2 — Dynamic elements
- Dynamic ids usually only change **part** → use `contains` / `starts-with` (XPath) or `^=` / `$=` / `*=` (CSS).
- Best long-term fix: **`data-testid`** attribute agreed with devs.
- Only if nothing stable exists at all → anchor to a **stable ancestor/label** with a relative XPath.

### Level 3 — Absolute vs relative, axes
- **Absolute:** `/html/body/div[1]/form/input` → brittle. ❌
- **Relative:** `//input[@id='user']` → preferred. ✅
- **Axes (XPath only):** `parent::`, `ancestor::`, `following-sibling::`, `preceding-sibling::`, `following::`.
- **`ends-with()` does NOT exist in XPath 1.0** (browsers) → use `contains(@id,'_99213')` or CSS `[id$='_99213']`.

### Level 4 — Traps
- **Verify locators in DevTools:** `$x("//xpath")` and `$$("css")`. (Say this out loud — interviewers love it.)
- **Multiple matches with `findElement`** → returns the **first**, no error. `findElements` → `List`, empty if none, **never throws**.
- **Faster?** → CSS, generally. Pick based on scenario, not blindly.

---

## Memorize these 3 lines
1. **"Priority: id → name → CSS → XPath. CSS is faster, XPath is for text, ancestors, and functions."**
2. **"Dynamic id → match the stable part with `contains`/`starts-with`, or use `data-testid`."**
3. **"Always relative XPath (`//`), never absolute (`/html/...`). I verify locators in DevTools with `$x` and `$$`."**

---

## Likely follow-ups
- XPath can do what CSS can't? → text match, traverse up (ancestor/parent), functions & logic.
- id suffix changing? → XPath 1.0 has no `ends-with`; use `contains` or CSS `[id$='...']`.
- `findElement` vs `findElements`? → first match vs full list; latter returns empty list, never throws.
- `//a[1]` vs `(//a)[1]`? → per-parent first child vs absolute first match in the whole document.
