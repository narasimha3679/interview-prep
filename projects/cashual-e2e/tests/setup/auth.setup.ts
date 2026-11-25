import { test as setup, expect } from '@playwright/test';
import path from 'path';

const email = process.env.E2E_EMAIL;
const password = process.env.E2E_PASSWORD;
if (!email) throw new Error('E2E_EMAIL is not set');
if (!password) throw new Error('E2E_PASSWORD is not set');
setup('authenticate', async ({ page }) => {
    await page.goto('/login');

    await page.getByLabel('Email address', { exact: true }).fill(email);
    await page.getByLabel('Password', { exact: true }).fill(password);

    await page.getByRole('button', { name: 'Sign in' }).click()

    await expect(page).toHaveURL('/dashboard');

    await page.context().storageState({ path: path.resolve(__dirname, '../../auth/standard.json') });
})