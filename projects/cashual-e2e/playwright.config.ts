import 'dotenv/config';
import path from 'node:path';
import { defineConfig, devices } from '@playwright/test';


const baseURL = process.env.BASE_URL;
if (!baseURL) throw new Error('BASE_URL is missing — copy .env.example to .env');

const appDir = path.resolve(__dirname, process.env.APP_FRONTEND_DIR ?? '../../accounting/frontend');
const storageState = path.resolve(__dirname, 'auth/standard.json');

export default defineConfig({
    testDir: './tests',
    // TODO 1 — CI guards: forbidOnly, retries
    forbidOnly: Boolean(process.env.CI),
    retries: process.env.CI ? 2 : 0,
    workers: 1,
    timeout: 60000,
    expect: { timeout: 10000 },
    // TODO 2 — shared `use`: baseURL, trace, screenshot, video
    use: {
        baseURL,
        trace: 'on-first-retry',
        screenshot: 'only-on-failure',
        video: 'retain-on-failure',
        serviceWorkers: 'block'
    },
    // TODO 3 — reporters: list + html (open: 'never')
    reporter: [
        ['list'],
        ['html', { open: 'never' }]
    ],
    // TODO 4 — projects: setup, and chromium that depends on it
    projects: [
        {
            name: 'setup',
            testDir: './tests/setup',
            testMatch: /auth\.setup\.ts/,
        },
        {
            name: 'chromium',
            testDir: './tests/ui',
            dependencies: ['setup'],
            use: {
                ...devices['Desktop Chrome'],
                storageState,
            },
        },
    ]
});