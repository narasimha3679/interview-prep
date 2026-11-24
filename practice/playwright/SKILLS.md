# Playwright Skills Matrix

Live view of where I actually am. Updated after every session.

**Target:** Playwright + TypeScript SDET round — working framework code, waits/locators, debugging
flakiness. Two-day sprint started 2026-11-24.
**Baseline:** new to Playwright. Strong Selenium/Java background (see `practice/selenium/SKILLS.md`).
**Last updated:** 2026-11-24 · **Sessions:** 1 · **Next up:** auto-waiting and web-first assertions

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## Reference implementation

`projects/playwright-enterprise/` — enterprise-convention framework, built 2026-11-24 and verified
(13 tests green; typecheck, lint and format clean). It is the answer key for every framework-design
row below, not evidence of my own skill until I can rebuild a piece of it unaided.

| Read | Answers the row |
|---|---|
| `README.md` | whole-framework pitch |
| `docs/INTERVIEW.md` | Q1–Q12, each mapped to a file |
| `src/fixtures/test.ts` | fixtures, worker scope, cleanup |
| `tests/setup/auth.setup.ts` | `storageState` auth |
| `src/config/env.ts` | fail-fast typed config |
| `src/api/*` | API layer + runtime schema validation |
| `.github/workflows/playwright.yml` | sharded CI + quality gate |

## Core model

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Locator is a lazy query, not an element | not assessed | — | Explain when the DOM is first touched |
| Auto-waiting and actionability checks | not assessed | — | Predict a click on a late-rendering button |
| Web-first assertions vs `isVisible()` | not assessed | — | Name the race in the manual form |
| Isolation: browser → context → page | not assessed | — | Explain why parallel login tests don't collide |
| Locator strategy: role > label > testId > CSS | not assessed | — | Rewrite one brittle CSS selector |

## Test runner

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `test` / `describe` / hooks and their order | not assessed | — | Trace execution order for one spec |
| Built-in vs custom typed fixtures | not assessed | — | Write a fixture from scratch |
| Config: projects, `baseURL`, retries, workers, timeouts | not assessed | All keys present in `playwright.config.ts`; env values fall back silently | Explain what each key changes, and why silent fallback is a defect |
| Tagging with `@smoke` and `--grep` selection | not assessed | — | Add and filter a tag |
| Parallelism and sharding | not assessed | — | Explain workers vs shards |

## Debugging and flakiness

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Trace viewer, screenshot, video on failure | not assessed | — | Read one trace and name the failing step |
| `--ui`, `--debug`, `PWDEBUG` | not assessed | — | Drive one run through the UI mode |
| Diagnosing a flaky test from evidence | not assessed | — | Given a flaky run, name the root cause |
| Retries: what they hide and what they prove | not assessed | — | State the interview answer |

## Framework design

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Page objects: locators + actions, no business asserts | not assessed | — | Refactor one page cleanly |
| `storageState` auth setup + project dependency | not assessed | Audit: absent — every spec logs in through the UI | Add a `setup` project + `storageState` |
| API setup via `request` fixture | not assessed | Audit: absent; demo site is UI-only, so this needs a decision | Seed state without clicking |
| Network mocking with `route` / `fulfill` | not assessed | — | Stub one failing response |
| CI: install browsers, artifacts, sharding | not assessed | Workflow runs 3 browsers in one job, uploads report; no `tsc`, no sharding, no JUnit | Explain the workflow, then fix the three gaps |

## TypeScript for Playwright

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `async`/`await`; a missing `await` bug | not assessed | — | Spot the un-awaited call |
| Typing `Page`, `Locator`, fixtures without `any` | not assessed | — | Type one spec end to end |
| `tsc --noEmit` in CI | not assessed | — | Run it and fix one error |

## Current focus order

1. **Locators + auto-waiting** — every other answer is built on it. Read the reference README first.
2. **Web-first assertions** — the #1 Selenium habit that breaks here.
3. **Fixtures and page objects** — what the interview actually asks you to build.
4. **Trace-based debugging** — the differentiator in a senior round.
5. **Auth state, API setup, CI** — the "have you run this for real" layer.
6. **Framework hardening** — close the audited gaps so `projects/playwright` is quotable in the round.

## Recurring traps to re-test every session

- Writing `waitForTimeout` because Selenium needed a sleep.
- `expect(await locator.isVisible()).toBe(true)` instead of the web-first assertion.
- Treating a `Locator` like a `WebElement` (finding it once, reusing a stale node).
- Business assertions inside page objects.
- `page` shared across tests or `describe`-level mutable state.
- Calling `projects/playwright` "enterprise" — it is an interview reference implementation; name the
  gaps instead of overclaiming.
