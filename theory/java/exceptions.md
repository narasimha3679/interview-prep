# Java — Exceptions, try-with-resources, finally

## Say it right
> "**Checked** exceptions are enforced at **compile time** — handle or declare `throws`; they extend `Exception` but not `RuntimeException` (e.g. `IOException`). **Unchecked** extend `RuntimeException`, surface at runtime (e.g. `NullPointerException`, and in Selenium `NoSuchElementException`). **Errors** are JVM-level and shouldn't be caught (`OutOfMemoryError`, `AssertionError`)."

---

## Hierarchy
```
Throwable
 ├── Exception
 │    ├── (checked)          IOException, SQLException, FileNotFoundException,
 │    │                       InterruptedException, ClassNotFoundException
 │    └── RuntimeException   NPE, ArithmeticException, IllegalArgumentException,
 │        (unchecked)        NumberFormatException, ClassCastException,
 │                           NoSuchElementException, StaleElementReferenceException
 └── Error                   OutOfMemoryError, StackOverflowError, AssertionError
```

**Testing links**
- **All Selenium exceptions are unchecked** → catch them for retry / stale handling.
- **File / Excel / JSON reading is checked** (`IOException`, POI) → must handle → good example of where checked is used.

---

## try-with-resources
```java
try (BufferedReader br = new BufferedReader(new FileReader("testdata.json"))) {
    return br.readLine();
} catch (IOException e) {
    log.error("Failed to read test data", e);
}
// br.close() runs automatically
```
- Works for anything implementing **`AutoCloseable`**.
- Multiple resources separated by `;`, closed in **reverse** order.

---

## `finally`
- Runs **always** — exception or not → used for cleanup.
- Runs **even after a `return`** in `try` (value computed → finally → return).
- Not run if `System.exit()` is called.
- A `return` inside `finally` **overrides** the try's return → never do it.

---

## Quick rules they probe
| Question | Answer |
|---|---|
| `throw` vs `throws` | `throw` raises; `throws` declares |
| Multiple `catch` order | Most **specific** (subclass) first, else compile error |
| Multi-catch | `catch (IOException \| SQLException e)` |
| try without catch? | Yes — `try-finally` or try-with-resources |
| Custom exception? | `extends Exception` (checked) or `extends RuntimeException` |
| Does `catch (Exception e)` catch `AssertionError`? | **No** — it's an `Error` |

**Testing example:** `class FrameworkException extends RuntimeException` for infra failures — keeps them distinct from real product bugs.

---

## Memorize
1. **"Checked = enforced at compile time (`IOException`). Unchecked = `RuntimeException` (`NPE`, Selenium exceptions). Error = `AssertionError`, `OutOfMemoryError`."**
2. **"try-with-resources auto-closes anything `AutoCloseable`; `finally` always runs, even after `return`."**
3. **"`catch (Exception e)` will NOT catch `AssertionError`."**

## Likely follow-ups
- Which is better for retry logic? → catch the unchecked Selenium exception, re-find, retry.
- Why prefer try-with-resources over `finally`? → shorter, and handles suppressed/close exceptions safely.
- Where would you use a custom exception? → reporting infra vs product failure distinctly.
