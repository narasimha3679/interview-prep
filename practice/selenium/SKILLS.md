# Selenium Skills Matrix

Live view of where I actually am. Updated after every session.

**Target:** enterprise Java Selenium framework — the SDET round plus the first month on the job
**Last updated:** 2026-11-23 · **Sessions:** 1 · **Next up:** CI pipeline (stage 1)

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## Framework anatomy

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Layers: runner → hooks → driver → pages → tests | not assessed | Audit: project has all five | Explain the call order from `testng.xml` down |
| `ThreadLocal<WebDriver>` + quit/remove | not assessed | `DriverFactory` implements it | Explain why `remove()` matters |
| Config precedence: `-D` vs properties file | not assessed | `ConfigReader` + surefire `systemPropertyVariables` | Trace which value wins |
| Page objects: locators + actions, no asserts | not assessed | `BasePage`, `LoginPage`, `ProductsPage` | Rewrite one page cleanly |
| Cucumber glue, tags, filtering | not assessed | `@smoke` filter verified via `-Dcucumber.filter.tags` | Tag a new scenario |

## WebDriver behaviour

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Waits: implicit vs explicit, never mix | not assessed | `implicitWait=0`, `WaitUtils` present | Explain the mixing trap |
| Locators: id/name/CSS/XPath priority | not assessed | — | Rewrite a brittle locator |
| Elements: dropdown, alert, frames, windows, Actions | not assessed | — | One drill each |
| Stale element, click intercepted, timeouts | not assessed | — | Name the fix for each |

## CI and operations

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| GitHub Actions pipeline (JDK, cache, headless run) | not assessed | Workflow written, not yet run on Actions | Verify the exact CI command locally |
| Reports + screenshots as artifacts | not assessed | Extent + Cucumber + surefire paths uploaded | Check the artifact list |
| Parallel execution and thread safety | not assessed | `DataProvider(parallel = false)` | Turn on 2 threads and make it pass |
| Test data / API seeding | not assessed | — | Seed state before UI login |

## Current focus order

1. **CI pipeline** — a green pipeline is the definition of "enterprise".
2. **Layers + config precedence** — every interview question starts there.
3. **Waits and flakiness** — the #1 production problem.
4. **Parallel execution** — ThreadLocal is there; prove it.
5. **Data/API seeding** — the layer most candidates miss.

## Recurring traps to re-test every session

- Mixing implicit and explicit waits.
- `ThreadLocal` without `remove()` in pooled threads.
- Assertions inside page objects.
- Tags at feature level applying to every scenario in the file.
- CI that passes locally but has no browser/headless flag.
