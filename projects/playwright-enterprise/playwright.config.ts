/**
 * Framework configuration.
 *
 * Read this file as a list of policy decisions, not settings:
 * - `projects` separate *what* is tested (UI, API, auth setup) from *where* it runs.
 * - `setup` is a dependency, so session state exists before any UI spec starts.
 * - Retries exist in CI only, to classify flakiness; `failOnFlakyTests` makes a retried pass a
 *   failure, because a test that only passes on the second attempt is a bug report.
 * - Reporters are environment-dependent: rich local feedback, mergeable artifacts in CI.
 * - Traces on first retry, not always: full traces on every green run are how storage fills up.
 */
import { defineConfig, devices } from '@playwright/test';
import { config } from './src/config/env';

export default defineConfig({
  testDir: './tests',
  outputDir: 'test-results',

  fullyParallel: true,
  forbidOnly: config.isCI,
  retries: config.isCI ? 2 : 0,
  failOnFlakyTests: config.isCI,
  workers: config.isCI ? 3 : undefined,
  timeout: config.timeouts.test,

  expect: {
    timeout: config.timeouts.expect,
  },

  reporter: config.isCI
    ? [
        ['blob', { outputDir: 'blob-report' }],
        ['junit', { outputFile: 'reports/junit.xml' }],
        ['github'],
      ]
    : [['list'], ['html', { open: 'never' }], ['junit', { outputFile: 'reports/junit.xml' }]],

  use: {
    baseURL: config.web.baseUrl,
    testIdAttribute: 'data-test',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    actionTimeout: config.timeouts.action,
    navigationTimeout: config.timeouts.navigation,
  },

  metadata: {
    appEnv: config.appEnv,
    apiBaseUrl: config.api.baseUrl,
  },

  projects: [
    {
      name: 'setup',
      testDir: './tests/setup',
      testMatch: /.*\.setup\.ts/,
    },
    {
      name: 'ui',
      testDir: './tests/ui',
      dependencies: ['setup'],
      use: {
        ...devices['Desktop Chrome'],
        storageState: config.auth.statePath('standard'),
      },
    },
    {
      name: 'api',
      testDir: './tests/api',
      use: {
        baseURL: config.api.baseUrl,
      },
    },
  ],
});
