# TypeScript for Automation — Study Pack

The TypeScript you need to read, write, and maintain a Playwright framework — without turning into a
full frontend course.

**Last verified:** November 2026.

---

## 60-second cram

1. **Types are erased at runtime.** TypeScript catches errors at compile time; it does not validate
   API responses. Runtime assertions still matter.
2. **`interface` vs `type`:** interfaces are extendable/mergeable; type aliases handle unions,
   intersections, and primitives.
3. **`unknown` is safe; `any` turns off the checker.** Prefer `unknown` + narrowing.
4. **Unions + narrowing** model API states: `Success | Failure`, `Loading | Loaded | Error`.
5. **Generics** make helpers reusable without losing type safety (`Page<T>`, `ApiResponse<T>`).
6. **`async/await` is syntax over Promises.** Always `await` or return the promise; unhandled promises
   become flaky tests.
7. **`tsconfig` strict mode is non-negotiable** for a maintainable framework.
8. **Playwright fixtures are dependency injection.** Typed fixtures give each test the page objects
   and data it needs.

**The one line that shows depth:**

> "TypeScript gives me compile-time safety in the framework, but I still assert runtime behavior. A
> typed response is a claim about shape, not proof the API returned it."

---

## File map

| File | What it gives you |
|---|---|
| `01_ts_fundamentals.md` | Types, interfaces, unions, generics, narrowing |
| `02_async_and_modules.md` | Promises, async/await, modules, config |
| `03_playwright_patterns.md` | Typed fixtures, page objects, API clients |
| `04_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Type annotation** | Declared type | Compile-time check only |
| **Interface** | Object shape contract | Extendable page/API models |
| **Type alias** | Name for any type | Unions/intersections |
| **Union** | One of several types | Model states/results |
| **Generic** | Type parameter | Reusable typed helpers |
| **Narrowing** | Refine a union at runtime | Safe handling of API variants |
| **`unknown`** | Safe top type | Must narrow before use |
| **`any`** | Disables checking | Avoid |
| **`strict`** | Strict compiler mode | Catch null/undefined bugs |
| **Promise** | Async result | Await it |
| **Module** | File with import/export | Organize framework |

---

## Traps

1. **Using `any` to silence errors.** It hides real defects.
2. **Assuming types validate runtime data.** They don't; validate API responses.
3. **Forgetting `await`.** The test continues before the action completes.
4. **`async` in `forEach`.** It doesn't wait; use `for...of` or `Promise.all`.
5. **Non-null assertion `!` everywhere.** It bypasses null checks; use narrowing.
6. **Over-typing test data.** Use factories/builders, not giant interfaces.
7. **Not enabling `strict`.** Null/undefined bugs slip through.
8. **Importing with the wrong module setting.** `module`/`moduleResolution` must match the runtime.
9. **Sharing mutable state across tests.** Playwright fixtures/contexts should isolate.
10. **Typing Playwright `Page` as `any`.** You lose auto-completion and safety.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, write a union type and narrow it.
- **20 minutes:** `02`, explain `async/await` and module config.
- **30 minutes:** `03`, refactor a page object/fixture with types.
- **20 minutes:** `04`, answer out loud.
