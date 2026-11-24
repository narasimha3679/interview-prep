# Architecture

## Layers

```text
tests/**.spec.ts            behaviour and assertions — the only layer allowed to assert
        │
src/fixtures/test.ts        dependency graph: page objects, API clients, cleanup, worker auth
        │
src/pages/*  src/api/*      domain vocabulary: beginCheckout(), create(), remove()
        │
BasePage    ApiClient       transport: navigation, status handling, schema validation, timing
        │
Page (browser)  APIRequestContext   Playwright primitives
```

Dependencies point down only. A page object never imports a fixture; a fixture never imports a spec;
the API client never knows what a booking is. That is what makes any layer replaceable in isolation.

## Lifecycle of a UI test

1. **Config** — `src/config/env.ts` parses `process.env` through zod once per process. A missing value
   aborts the run before a browser opens.
2. **Setup project** — `tests/setup/auth.setup.ts` launches Chromium, logs in through the real form,
   asserts the catalogue loaded, then writes cookies + localStorage to `auth/standard.json`.
3. **UI project** — a new browser context is created per test, seeded from that file. Cookies and
   storage are per-context, so two tests can run simultaneously without seeing each other.
4. **Fixture graph** — `page` is injected; page objects are constructed around it. Nothing is shared
   between tests. The API `request` context is only created if an API fixture is requested.
5. **Action** — a locator is re-queried at action time and retried until actionable. If the selector
   matches two elements, it fails: strictness catches ambiguous locators early.
6. **Assertion** — web-first assertions poll the DOM until the condition holds or the timeout expires.
   No `sleep`, no manual retry loop.
7. **Teardown** — `bookingCleanup` deletes created records; contexts close. On failure Playwright
   writes a trace, screenshot, video and error context to `test-results/`.

## Lifecycle of an API test

1. Worker starts → `apiToken` (worker scope) creates a short-lived request context, calls
   `POST /auth`, yields the token, disposes the context at worker teardown. **One login per worker,
   not per test.**
2. Per test, `bookingApi` wraps the per-test `request` context with that token.
3. `ApiClient` sends the request, rejects non-2xx with method, path, status and body, then parses the
   response through a zod schema. A shape change fails here, with the field name, instead of three
   assertions later.
4. `bookingCleanup` records ids and deletes them in teardown — including after a failed assertion.

## Decisions and trade-offs

| Decision                                      | Why                                                                                  | What it costs                                                      |
| --------------------------------------------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------ |
| `setup` **project** instead of `globalSetup`  | Appears in reports, is retryable, runs per role, fails with a normal test error      | Slightly slower than one opaque hook                               |
| `storageState` instead of API login for UI    | Exercises the real session mechanism the app depends on; one code path for all tests | Regenerate when auth changes; state file is a secret               |
| Worker-scoped token, per-test request context | Auth cost paid once; request context still isolated per test                         | A rotating token mid-run breaks the worker fixture                 |
| zod at the API boundary                       | Types are erased; schemas assert                                                     | Extra dependency; schemas must be maintained when contracts change |
| Page objects expose locators, specs assert    | Keeps assertions readable in one place; page objects stay reusable                   | Specs are slightly more verbose                                    |
| `testIdAttribute: 'data-test'`                | One convention for every test id, no `[data-test=...]` strings                       | Requires app cooperation; documented in the config                 |
| Unique factories, no shared rows              | Parallel-safe and order-independent                                                  | Demo data cannot be asserted for exact counts                      |
| `trace: 'on-first-retry'`                     | Full traces on every green run are expensive to store and slow                       | A first-attempt local failure has artifacts but no trace           |
| Retries in CI only + `failOnFlakyTests`       | Retries classify flakiness; the flag refuses to let it pass silently                 | CI can be red on an intermittent, which is the point               |
| Shards + merged blob report                   | Wall-clock time scales with runners                                                  | Report merging is a separate job and a separate failure mode       |

## Parallelism: workers, shards, contexts

- **Context** — isolation unit. New per test, own cookies/storage/permissions. Cheap.
- **Worker** — a Node process with one browser. `fullyParallel` lets tests from the same file run in
  different workers. Worker count is local CPU (or 3 in CI here).
- **Shard** — a slice of the suite sent to a different machine. `--shard=2/3`. Orthogonal to workers.

The failure mode to know: workers scale until tests fight over shared data. Fix the data, not the
worker count.

## Failure triage runbook

1. **Reproduce locally, same seed of failure**: `npx playwright test <file> --repeat-each=5`.
2. **Open the trace before reading the stack trace.** The DOM snapshot at the failing step usually
   explains more than the timeout message.
3. **Classify**:
   - _Product bug_ — assertion correctly failed; file it.
   - _Locator bug_ — element ambiguous or renamed; fix the page object, not the timeout.
   - _Data bug_ — collision or leftover state; fix the factory or cleanup.
   - _Environment_ — third-party slowness; document it, do not paper over it with sleeps.
   - _Framework bug_ — fixture ordering, missing await; `npm run lint` catches most un-awaited calls.
4. **Only then** consider a retry or a quarantine, and record why.

## What a real bank would add next

| Gap                                          | Why it matters at scale                                              |
| -------------------------------------------- | -------------------------------------------------------------------- |
| Environment matrix (dev/staging/prod-smoke)  | Same suite, different `WEB_BASE_URL`, different credential source    |
| Secret manager integration                   | `.env` does not exist in production pipelines                        |
| Seeding service instead of UI setup          | Order history, entitlements and balances are not created by clicking |
| DB / queue assertions                        | The UI is not the source of truth for a payment                      |
| Contract tests (Pact or schema registry)     | Catches breaking API changes before the UI suite runs                |
| Flake tracking over time                     | Retries hide trend data; a dashboard makes it visible                |
| Quarantine lane (`@quarantine` project)      | Keeps the main pipeline honest while a known flake is fixed          |
| Trace retention policy                       | Storage cost grows with suite size, not test count                   |
| Accessibility and visual regression projects | Separate signal, separate failure budget                             |
