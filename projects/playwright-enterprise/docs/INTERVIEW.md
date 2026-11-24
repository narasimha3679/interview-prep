# Interview Sheet

Everything here maps to a real file in this repo. If you cannot point at the file, do not claim the
answer.

## 60-second pitch

> "It's a Playwright and TypeScript framework, not a test project. Config is validated at startup and
> fails fast, so a run can never silently point at the wrong environment. Auth is a setup project that
> writes `storageState`, so UI specs start logged in and the login page still gets its own tests.
> Page objects hold locators and user actions; assertions stay in specs. On the API side there is a
> typed client with runtime schema validation, a worker-scoped token, and fixtures that delete what a
> test created even when it fails. CI runs typecheck, lint and format first, then three UI shards with
> a merged HTML report, JUnit output and traces uploaded on failure. Retries exist only to classify
> flakiness — `failOnFlakyTests` makes a retried pass a failure."

## "How would you build it from scratch?"

Four buckets. Expand only what they ask.

| Bucket         | What you say                                                                                                         | Where it lives                                                               |
| -------------- | -------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| **Foundation** | TypeScript strict, one config file, `strict` + `noEmit` checked in CI, env-driven base URLs, projects per surface    | `tsconfig.json`, `playwright.config.ts`, `src/config/env.ts`                 |
| **Isolation**  | Fresh context per test; auth once per role into `storageState`; worker-scoped API token; unique data per test        | `tests/setup/auth.setup.ts`, `src/fixtures/test.ts`, `src/data/factories.ts` |
| **Design**     | Role/testId locators, page objects with user-level actions, specs own assertions, typed fixtures compose everything  | `src/pages/*`, `src/api/*`, `src/fixtures/test.ts`                           |
| **Operations** | Trace/video/screenshot on failure, JUnit + merged HTML, sharded CI, `failOnFlakyTests`, quality gate before browsers | `.github/workflows/playwright.yml`, `playwright.config.ts`                   |

## Questions

**1. How is test isolation achieved?**
A fresh browser context per test: own cookies, storage, permissions. Contexts are cheap because they
share one browser process. My auth state is seeded per context from `storageState`, so isolation and
reuse coexist.
→ `playwright.config.ts`, `tests/setup/auth.setup.ts`

**2. How do you avoid logging in on every test without weakening the login tests?**
A setup project logs in once per role and saves `storageState`. UI projects depend on it. The login
specs explicitly clear the state with `test.use(...)`, so the real form is still covered.
→ `tests/setup/auth.setup.ts`, `tests/ui/login.spec.ts`

**3. What is a fixture, and why not `beforeEach`?**
Dependency injection with scoped teardown. `beforeEach` shares mutable state through `describe`
variables and hides ordering; a fixture declares what a test needs, picks the right scope (test or
worker), and cleans up in the same block.
→ `src/fixtures/test.ts`

**4. Where do you draw the page object boundary?**
Locators and user-level actions inside; assertions about outcomes in the spec. A page object that
asserts cannot be reused by a test that expects the opposite outcome.
→ `src/pages/checkout.page.ts`

**5. How do you avoid flaky waits?**
Never sleep. Locators re-query at action time and wait for actionability; assertions poll. Stable
role/testId selectors, isolated data, no order dependence. If it is still flaky, the cause is data or
the app, and the trace will say which.
→ `src/pages/*`, `tests/ui/*.spec.ts`

**6. CI shows a pass on the second attempt. Ship it?**
No. That is a flake with a timestamp. Retries classify it, `failOnFlakyTests` refuses to let it pass,
and the trace from the first attempt is the bug report.
→ `playwright.config.ts`

**7. Workers versus shards?**
Workers are parallel processes on one machine; shards split the suite across machines. Both multiply,
neither fixes shared data. I scale workers first, shard when wall-clock time or browser count demands
it, and only after tests are order-independent.
→ `.github/workflows/playwright.yml`

**8. How do you test error states you cannot reproduce?**
Request interception. I abort image requests and assert checkout still completes — a real degradation
requirement. The same tool fakes a 500 or a slow third-party call.
→ `tests/ui/resilience.spec.ts`

**9. Why validate API responses if TypeScript already types them?**
Types are erased at runtime. `as Booking` is a claim, not proof. A schema fails at the boundary with
the field name, instead of surfacing as a confusing UI failure downstream.
→ `src/api/schemas.ts`, `src/api/api-client.ts`

**10. How is test data handled?**
Factories with a unique marker per call, cleanup registered in a fixture so it runs after failures
too. No shared rows, no ordering assumptions, no "test 4 depends on test 3".
→ `src/data/factories.ts`, `src/fixtures/test.ts`

**11. Why separate UI and API projects?**
Different failure budgets and different costs. Contract tests are seconds and pin the payload shape;
UI tests prove the journey. Same repo, same config, different projects and different CI jobs.
→ `playwright.config.ts`, `tests/api/booking.spec.ts`

**12. What would you change for a real bank?**
Controlled environments per stage, credentials from a secret manager, a seeding service instead of
clicking setup, DB and queue assertions for money movement, contract tests in the service pipeline,
flake tracking over time, and a quarantine lane. All named in the architecture doc.
→ `docs/ARCHITECTURE.md`

## Traps to avoid saying

- "Playwright is faster than Selenium because it is newer." Say: auto-waiting, one process for
  parallel contexts, and no WebDriver round trip.
- "We retry flaky tests." Say: retries classify flakes; the fix is locator/data discipline.
- "The page object asserts." Say: page objects expose state; specs assert.
- "We sleep to be safe." Always wrong, in every tool.
- "Types validate the API." Only schemas do.
