# Interview Guide: Playwright Framework

Use this guide to explain the framework you built and the trade-offs behind it. Be ready to walk through a real test and the files it uses.

## 1. One-minute project pitch

> “I built a browser automation framework from scratch using TypeScript and Playwright Test. It uses isolated test fixtures and page objects, covers login and checkout on a public demo shop, supports Chromium, Firefox, and WebKit, and captures traces, screenshots, and video when tests fail. The same suite runs in GitHub Actions and publishes an HTML report.”

## 2. Execution flow

```text
npm test
  -> playwright.config.ts selects specs and browser projects
  -> Playwright creates a fresh browser context and Page per test
  -> tests/fixtures/test.ts constructs page objects around that Page
  -> a spec performs user actions through page objects
  -> web-first expect assertions wait for the UI state
  -> report and failure artifacts are written under playwright-report/ and test-results/
```

## 3. What belongs where

### `playwright.config.ts`

- Defines the spec directory and Chromium / Firefox / WebKit projects.
- Reads local `.env` values and applies the application base URL.
- Configures retries and worker limits for CI without slowing local feedback.
- Sets screenshot, video, and trace retention to make failures diagnosable.

### `tests/fixtures/test.ts`

Extends Playwright's `test` with typed page-object fixtures. Each test gets page objects tied to its own isolated `Page`; specs do not construct browser objects or share mutable state.

### `tests/pages/`

Page objects keep selectors and user-level actions close together. For example, `LoginPage.login()` encapsulates entering credentials, while a spec describes whether sign-in should succeed. This keeps locator changes localized without hiding the scenario's intent.

### `tests/specs/`

Specs describe observable behavior. The login suite includes a data-driven set of invalid credentials; the checkout test exercises a full purchase journey. `@smoke` and `@regression` tags let scripts select different test scopes.

### `.github/workflows/playwright.yml`

CI installs Node dependencies and browser binaries, runs all browser projects, and uploads reports and test artifacts even when test execution fails.

## 4. Why Playwright?

- Auto-waiting and retrying assertions reduce timing-related flakes without fixed sleeps.
- Browser contexts provide test isolation without launching a separate browser for every test.
- Browser projects reuse the same test contract across three browser engines.
- Fixtures make setup composable and scoped to each test.
- Traces combine actions, DOM snapshots, and network activity into a replayable failure artifact.

## 5. Questions to practice

**How is test isolation achieved?**  
Playwright Test creates a fresh browser context and page for each test by default. The custom fixtures build page objects on that page, so cookies and page state are not shared across tests.

**Why use page objects?**  
They centralize selectors and meaningful UI actions. If a selector changes, I can update the page object rather than every spec. I keep assertions about business outcomes in the specs so the scenario remains readable.

**How does Playwright avoid arbitrary sleeps?**  
Locators and web-first assertions automatically wait for actionability or the expected condition up to a timeout. Fixed sleeps make tests slower and still do not prove the UI is ready.

**How would you reduce flaky tests?**  
Use stable role or test-id selectors, wait for a specific state, isolate test data, avoid order-dependent tests, and use traces to identify the actual timing or application issue. Retries help expose intermittent failures in CI but are not a substitute for fixing them.

**Why keep screenshot, video, and trace?**  
They provide complementary evidence: a screenshot is a point-in-time view, video shows the interaction sequence, and a trace provides replayable actions plus DOM and network context. Retaining them only on failure avoids unnecessary artifacts.

**How would you add a new environment?**  
Set `BASE_URL` in the environment or CI secret/variable for that environment. Keep environment selection outside the specs so the same scenarios can run against different deployments.

**How would you scale this framework?**  
Add domain-aligned page objects and specs, define stable test-data setup and cleanup, introduce API fixtures where they reduce UI setup time, and use CI sharding when suite duration warrants it. Keep tests independent before increasing parallel workers.

**What is the difference between a browser and a browser context?**  
A browser is the engine process. A context is an isolated session with its own cookies, storage, and permissions. Multiple contexts can share a browser process while remaining isolated.

## 6. Run commands to remember

```powershell
npm run test:smoke
npm run test:regression
npm run test:chromium
npm run test:ui
npm run report
```
