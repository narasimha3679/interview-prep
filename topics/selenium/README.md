# Selenium for SDETs

WebDriver fundamentals: waits, locators, page objects, exceptions, and framework design.

## 60-second cram

1. Implicit wait is global/presence-only; explicit wait is targeted/condition-based.
2. Never mix implicit and explicit waits; timeouts compound.
3. Locator priority: role/test-id → id/name → CSS → XPath.
4. POM: one class per page; locators + actions inside; tests hold behavior.
5. Stale element = found then re-rendered; re-locate.
6. Click intercepted = something covers it; wait for actionability.
7. `ThreadLocal<WebDriver>` for parallel safety; remove on quit.
8. Use `WebDriverWait` + expected conditions, not `Thread.sleep`.
9. Capture screenshot/logs on failure.
10. Test UI-specific risk; seed state through APIs.

## File map

| File | Topic |
|---|---|
| [`waits.md`](waits.md) | implicit/explicit/fluent, mixing traps |
| [`locators.md`](locators.md) | XPath vs CSS, dynamic elements |
| [`page-object-model.md`](page-object-model.md) | POM, PageFactory, chaining |
| [`ui-elements.md`](ui-elements.md) | dropdown, alert, windows, frames, Actions |
| [`exceptions-and-flakiness.md`](exceptions-and-flakiness.md) | exception wallet, stale, flakiness |
| [`framework-design.md`](framework-design.md) | 7-layer framework anatomy |
| [`exception-handling-strategy.md`](exception-handling-strategy.md) | where to catch |
| [`shadowdom.md`](shadowdom.md) | shadow DOM locators |
| [`cookie-session-reuse.md`](cookie-session-reuse.md) | session reuse |

## Related

- [`../../projects/selenium-bdd/`](../../projects/selenium-bdd/) — runnable framework
- [`../testng/`](../testng/) — runner/lifecycle
- [`../cucumber/`](../cucumber/) — BDD
- [`../test-strategy/`](../test-strategy/) — test levels/flakiness
