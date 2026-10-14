# Playwright UI Automation Framework

An interview-prep browser automation framework built from scratch with **TypeScript**, **Playwright Test**, and **Sauce Demo**. It demonstrates a maintainable test structure, reusable page objects and fixtures, data-driven checks, cross-browser execution, and failure diagnostics without needing a local application.

## Prerequisites

- Node.js 20+
- npm

## Get started

```powershell
npm ci
npx playwright install
Copy-Item .env.example .env
npm run test:smoke
```

`npm test` runs the complete suite in Chromium, Firefox, and WebKit. Install browser binaries with `npx playwright install` before the first run. The smoke script runs the tagged critical paths in Chromium only.

To run one browser or open the interactive test runner:

```powershell
npm run test:chromium
npm run test:ui
```

## Configuration

Copy `.env.example` to `.env` to override the demo application's URL or credentials. Playwright loads `.env` when it reads `playwright.config.ts`. These defaults are public demo-site credentials, not credentials for a real account.

| Variable | Default | Purpose |
|----------|---------|---------|
| `BASE_URL` | `https://www.saucedemo.com` | UI application under test |
| `SAUCEDEMO_USERNAME` | `standard_user` | Demo account |
| `SAUCEDEMO_PASSWORD` | `secret_sauce` | Demo account password |

Keep real credentials in local environment variables or CI secrets; `.env` is ignored by Git.

## Project structure

```text
playwright.config.ts             # Browser matrix, timeouts, retries, diagnostics
tests/
  fixtures/test.ts                # Typed, reusable page-object fixtures
  pages/                          # Page objects: locators and user actions
  specs/
    login.spec.ts                 # Successful and data-driven negative login cases
    checkout.spec.ts              # Add-to-cart through order confirmation
.github/workflows/playwright.yml  # Cross-browser CI and report artifacts
```

## Framework decisions

- **Playwright Test runner:** built-in assertions, fixtures, isolation, parallel workers, and browser projects in one tool.
- **Page Object Model:** page classes own selectors and actions; specs describe user outcomes.
- **Custom fixtures:** page objects are created per test and share that test's isolated `Page`.
- **Accessible selectors first:** role and placeholder locators are preferred; the app's `data-test` hooks are configured as Playwright test IDs.
- **Cross-browser projects:** the same specs run on Chromium, Firefox, and WebKit.
- **Failure diagnostics:** HTML report, screenshot, video, and trace are retained for failures; the trace can be opened with `npx playwright show-trace <trace.zip>`.
- **CI-aware execution:** retries and worker limits apply in CI, and the workflow uploads reports even if tests fail.

## Reports and debugging

After a run, open the HTML report:

```powershell
npm run report
```

Test artifacts are written under `test-results/`; the HTML report is under `playwright-report/`. For a focused debug run, use:

```powershell
npx playwright test --debug
npx playwright test --grep "purchase an item" --project=chromium
```

## Interview walkthrough

Start with: *“I built a TypeScript Playwright Test framework with isolated fixtures, page objects, data-driven negative login coverage, a complete checkout flow, cross-browser projects, and trace-based failure debugging. CI runs the same suite and publishes the report.”*

Then explain the test lifecycle: Playwright creates an isolated browser context and page, the fixture constructs page objects around that page, a spec drives the customer workflow, and web-first assertions wait for the expected UI state. On failure, Playwright saves trace, screenshot, and video for diagnosis.

See [docs/INTERVIEW_GUIDE.md](docs/INTERVIEW_GUIDE.md) for the architecture walkthrough and practice questions.
