/**
 * Auth setup — runs once per run, before every UI project that depends on it.
 *
 * Interview answer: "Logging in through the UI in every test costs seconds and tests the login page
 * forty times. A setup project logs in once per role, saves `storageState` (cookies + localStorage),
 * and the UI projects start already authenticated. The login page still gets its own specs."
 *
 * The state files are generated artifacts: `auth/*.json` is gitignored because a session token in git
 * is a leaked session token.
 */
import { expect, test as setup } from '@playwright/test';
import { config } from '../../src/config/env';
import { LoginPage } from '../../src/pages/login.page';

/**
 * Adding a second role (`locked_out_user`, an admin, a tenant) means adding one entry here plus one
 * project that points at `statePath(role)`.
 */
const roles = [{ name: 'standard', ...config.credentials.web }];

for (const role of roles) {
  setup(`authenticate as ${role.name}`, async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.open();
    await loginPage.login(role.username, role.password);

    await expect(page.getByTestId('title')).toHaveText('Products');
    await page.context().storageState({ path: config.auth.statePath(role.name) });
  });
}
