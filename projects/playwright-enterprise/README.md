# Playwright Enterprise Framework

A Playwright + TypeScript framework built to the conventions a bank or large product team actually
uses: validated configuration, session reuse, typed fixtures, a real API layer, guaranteed data
cleanup, sharded CI, and failure evidence you can replay.

The unit suite is small on purpose. **The structure is the deliverable**, not the number of tests.

> Companion project: [`../playwright/`](../playwright/) is the earlier, simpler version. Read that
> first if this one feels dense; the diff between the two is the lesson.

## Quick start

```powershell
npm ci
Copy-Item .env.example .env
npx playwright install chromium
npx playwright test
```

Expected: **13 passed**. The UI suite runs against `saucedemo.com`, the API suite against
`restful-booker.herokuapp.com`. Both are public demo services.

The framework **fails immediately** if `.env` is missing. That is deliberate — see
[Validated config](#validated-config).

## Commands

| Command                   | What it does                                            |
| ------------------------- | ------------------------------------------------------- |
| `npm test`                | Everything: auth setup, UI, API                         |
| `npm run test:ui`         | UI project only (auth setup runs first as a dependency) |
| `npm run test:api`        | API project only, no browser                            |
| `npm run test:smoke`      | `@smoke` tagged tests                                   |
| `npm run test:regression` | `@regression` tagged tests                              |
| `npm run test:shard`      | One shard — what CI runs per runner                     |
| `npm run test:ui-mode`    | Playwright UI mode, watch and time-travel               |
| `npm run test:debug`      | Inspector, step through actions                         |
| `npm run auth`            | Regenerate `auth/standard.json` only                    |
| `npm run report`          | Open the last HTML report                               |
| `npm run verify`          | Typecheck + lint + format check — the quality gate      |

## Structure

```text
playwright.config.ts          # projects, reporters, retries, timeouts — the policy file
eslint.config.mjs             # type-aware lint: catches un-awaited calls, bans `any`
src/
  config/env.ts               # zod-validated env → frozen typed config object
  fixtures/test.ts            # the only `test` a spec imports: pages, API clients, cleanup
  pages/                      # page objects: locators + user actions, no assertions
    base.page.ts              # navigation only
  api/
    api-client.ts             # transport: status handling, timing, schema validation
    auth.api.ts               # one job: credentials → token
    booking.api.ts            # domain methods, teardown-safe delete
    schemas.ts                # runtime contracts (zod), not just types
  data/factories.ts           # unique, parallel-safe test data
tests/
  setup/auth.setup.ts         # logs in once per role → storageState
  ui/*.spec.ts                # start already authenticated
  api/*.spec.ts               # contract tests, worker-scoped token
docs/
  ARCHITECTURE.md             # layers, lifecycles, decisions and trade-offs
  INTERVIEW.md                # question → file → spoken answer
```

## The six things that make it "enterprise"

**1. Validated config** — `src/config/env.ts` parses `process.env` through zod once and throws a
readable error listing every missing field. No `process.env.X!`, no silent `?? 'default'` that lets a
staging run point at production.

**2. Session reuse** — `tests/setup/auth.setup.ts` logs in once, writes `auth/standard.json`, and the
`ui` project starts from it via `dependencies: ['setup']`. The login page still has its own specs,
which explicitly clear the session. `auth/*.json` is gitignored: a committed session is a leaked
session.

**3. Typed fixtures with teardown** — `src/fixtures/test.ts` composes page objects and API clients.
`bookingCleanup` deletes every record a test created, and does it in `finally`-style teardown, so the
cleanup runs even when the assertion fails. The API token is `scope: 'worker'`: one login per worker,
not one per test.

**4. A real API layer** — `ApiClient` owns status handling and validates every response against a
zod schema. TypeScript types are erased at runtime; `as Booking` is a claim, not proof.

**5. Failure evidence** — trace on first retry, screenshot and video on failure, JUnit XML for CI
dashboards, and `test.step` so a trace reads like a user journey.

**6. Sharded CI with a quality gate** — typecheck, lint and format run before any browser starts;
three UI shards run in parallel; blob reports merge into one HTML report; artifacts upload on failure
too. `failOnFlakyTests` turns "passed on the second attempt" into a failure, because that is a bug
report, not a pass.

## Configuration

| Variable                                    | Required | Purpose                                                          |
| ------------------------------------------- | -------- | ---------------------------------------------------------------- |
| `APP_ENV`                                   | no       | `local` (default), `ci`, `staging` — recorded in report metadata |
| `WEB_BASE_URL`                              | yes      | UI under test                                                    |
| `API_BASE_URL`                              | yes      | API under test                                                   |
| `SAUCEDEMO_USERNAME` / `SAUCEDEMO_PASSWORD` | yes      | UI account                                                       |
| `API_USERNAME` / `API_PASSWORD`             | yes      | API account for token issuance                                   |
| `TEST_TIMEOUT_MS`                           | no       | Per-test timeout, default 30000                                  |
| `EXPECT_TIMEOUT_MS`                         | no       | Assertion timeout, default 5000                                  |
| `ACTION_TIMEOUT_MS`                         | no       | Per-action timeout, default 10000                                |
| `NAVIGATION_TIMEOUT_MS`                     | no       | `goto` timeout, default 15000                                    |

Never commit `.env`. In CI the same names come from repository secrets; `API_PASSWORD` and the
public demo credentials have safe defaults in the workflow.

## Adding things

**A page** — extend `BasePage`, declare `readonly` locators in the constructor, expose user-level
methods (`beginCheckout()`) and state locators (`cartBadge`). Never call `expect` inside it.

**A test** — import `test` and `expect` from `src/fixtures/test`. Ask for what you need:
`async ({ inventoryPage, cartPage })`. Tag it `@smoke` or `@regression`. Steps for anything longer
than three actions.

**A role** — add an entry to `roles` in `tests/setup/auth.setup.ts`, give it a project that points at
`config.auth.statePath('<role>')`. The session file is generated, never committed.

**An API resource** — add schemas in `schemas.ts`, a resource class in `src/api/`, and a fixture in
`src/fixtures/test.ts`. Specs then speak domain (`bookingApi.create`), not HTTP.

## Debugging

| Symptom                      | First move                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------- |
| Failed in CI, passed locally | Open the trace: `npx playwright show-trace <path>`                              |
| Element not found            | Check the trace DOM snapshot, not the error text — auto-waiting already retried |
| Flaky only when parallel     | Shared data or shared state; check factories and `storageState`                 |
| Passed on retry              | Read `failOnFlakyTests` output; treat it as a real bug                          |
| Need to see it happen        | `npx playwright test --ui` then step through the timeline                       |

## Honest constraints

- The UI app has no public API, so UI seeding cannot be demonstrated end to end. The two services are
  separate demo apps; in a real system `WEB_BASE_URL` and `API_BASE_URL` are the same backend and the
  seam is configuration only.
- `restful-booker` returns `405` (not `404`) for deleting a missing booking. Cleanup tolerates both;
  the API spec documents the quirk rather than hiding it.
- The suite depends on two external services. A bank run would target controlled environments; here
  network flakiness is possible and is not retried away locally.
- Deliberately out of scope: Docker, visual regression, accessibility scans, mobile emulation,
  database validation, and secret-manager integration. Each is a named next step, not an oversight.

## Read next

- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) — how a run flows, layer by layer, with the trade-offs
- [docs/INTERVIEW.md](docs/INTERVIEW.md) — the questions this structure answers, with spoken answers
