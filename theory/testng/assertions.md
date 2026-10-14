# TestNG — Hard vs Soft Assertions

## Say it right
> "A **hard assert** throws immediately and stops the test. A **soft assert** collects all failures and reports at the end via **`assertAll()`**, so one run validates every field. I use hard asserts for preconditions/flow and soft asserts when validating many fields of one response."

---

## Code
```java
// Hard assert — stops the test here
Assert.assertEquals(actual.getTitle(), "TD Bank", "Title mismatch");
Assert.assertTrue(condition);
Assert.fail("explicit failure");

// Soft assert — continues, reports at the end
SoftAssert soft = new SoftAssert();
soft.assertEquals(resp.getName(), "John");
soft.assertEquals(resp.getStatus(), "ACTIVE");
soft.assertTrue(resp.getId() > 0);
soft.assertAll();               // MUST call, or failures are silently ignored
```

Common matchers: `assertEquals`, `assertNotEquals`, `assertTrue/False`, `assertNull/NotNull`, `fail`. Always pass a **message** as the last arg.

---

## Gotchas (this is what they test)
1. **Forgetting `assertAll()`** → the test reports **Passed** even though assertions failed. ← the main answer
2. **Create `SoftAssert` per test method**, not static/shared → otherwise parallel runs mix failures.
3. Call `assertAll()` at the **end of the test method** (some teams do it in `@AfterMethod`).
4. **Depth nugget:** a hard assert throws **`AssertionError`**, which extends **`Error`, not `Exception`** — so `catch (Exception e)` won't catch it.

---

## When to use which
- **Hard** → if it fails the rest is meaningless (login failed → don't continue).
- **Soft** → validating many independent fields/rows in one payload to see *all* mismatches.

---

## Memorize
1. **"Hard assert = stops the test; soft assert = collects failures, reports at `assertAll()`."**
2. **"Never forget `assertAll()` — otherwise the test passes despite failures."**
3. **"Hard assert throws `AssertionError` (an `Error`); soft asserts must be per-method for parallel safety."**

## Likely follow-ups
- What happens if you forget `assertAll()`? → test passes despite failed assertions.
- SoftAssert and parallel execution? → declare per test/method, never static.
- Is `AssertionError` an exception? → No, it's an `Error`.
