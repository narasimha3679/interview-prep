# Playwright Skills Matrix

Live view of where I actually am. Updated after every session.

**Target:** Playwright + TypeScript SDET round — working framework code, waits/locators, debugging
flakiness. Practice target: **Cashual** (`projects/cashual-e2e/`, local frontend + real Supabase).
Two-day sprint started 2026-11-24.
**Baseline:** new to Playwright. Strong Selenium/Java background (see `practice/selenium/SKILLS.md`).
**Last updated:** 2026-11-24 · **Sessions:** 3 · **Next up:** `cashual-e2e` step 2 — config + auth setup

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
| Locator is a lazy query, not an element | shaky | S2 verbal 3/4 — right mechanism, called it "lazy loading"; code pending | Re-ask after `drill-01` |
| Auto-waiting and actionability checks | shaky | S2 verbal 3/4 — missed stable / receives-events / strict mode | Name the four checks, then predict two outcomes |
| Web-first assertions vs `isVisible()` | shaky | S2 verbal 2/4 — saw redundancy, missed that `isVisible()` does not wait | Unit 2: snapshot vs poll |
| Isolation: browser → context → page | shaky | S2 verbal 3/4 — context + page per test, torn down; code pending | Re-ask after `drill-01` |
| Locator strategy: role > label > testId > CSS | shaky | S3 hit a strict-mode collision: `getByLabel('Password')` matched the field and the "Show password" toggle | Explain why `exact` fixes it, then write a locator for a table row |

## Test runner

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `test` / `describe` / hooks and their order | not assessed | — | Trace execution order for one spec |
| Built-in vs custom typed fixtures | shaky | Day 2 wrote `src/fixtures/test.ts` with `dashboard`; spec imports `{ test, expect }` from it | Add a second fixture and a teardown fixture unaided |
| Config: projects, `baseURL`, retries, workers, timeouts | shaky | S3 wrote it against a live target; review found a rogue `zod` import shadowing `process`, and no `tsconfig.json` | Fix both, then explain each block without notes |
| Tagging with `@smoke` and `--grep` selection | not assessed | — | Add and filter a tag |
| Parallelism and sharding | not assessed | — | Explain workers vs shards |

## Debugging and flakiness

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Trace viewer, screenshot, video on failure | shaky | D2 generated and decoded a real trace (actions, DOM snapshots, network, screencast); learner has not driven the viewer yet | Open `trace.zip`, name the failing action and the DOM state |
| `--ui`, `--debug`, `PWDEBUG` | not assessed | — | Drive one run through the UI mode |
| Diagnosing a flaky test from evidence | not assessed | — | Given a flaky run, name the root cause |
| Retries: what they hide and what they prove | not assessed | — | State the interview answer |

## Framework design

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Page objects: locators + actions, no business asserts | shaky | Day 2 wrote `DashboardPage`; first draft had a duplicate brace and no member declarations | Add a second page object unaided |
| `storageState` auth setup + project dependency | shaky | Day 2 proven: `chromium` reaches `/dashboard` without logging in; path needed two corrections | Explain role → state → project dependency out loud |
| API setup via `request` fixture | not assessed | Reference: worker-scoped token + per-test client in `src/api/`; demo UI has no API, so no UI seeding | Seed state without clicking |
| Network mocking with `route` / `fulfill` | not assessed | — | Stub one failing response |
| CI: install browsers, artifacts, sharding | not assessed | Reference: quality gate → 3 shards → merged blob report, traces on failure | Explain the workflow steps out loud |
| Environment isolation: local vs production data | shaky | S3 targets live; configured account is an employee, so company writes are not reachable yet | Decide QA company owner before any CRUD test |

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
- Calling a locator "lazy loaded" — it is a lazy *query*, and it caches nothing.
- Describing auto-wait as "waits for the page to load" instead of re-resolving + actionability.
- Treating a `Locator` like a `WebElement` (finding it once, reusing a stale node).
- Business assertions inside page objects.
- `page` shared across tests or `describe`-level mutable state.
- Calling `projects/playwright` "enterprise" — it is an interview reference implementation; name the
  gaps instead of overclaiming.
