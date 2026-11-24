/**
 * Login behaviour — the one place that drives the sign-in form.
 *
 * `test.use` clears the project-level `storageState`, so these tests start as a stranger. That is
 * the whole point of separating auth setup from auth testing.
 */
import { config } from '../../src/config/env';
import { expect, test } from '../../src/fixtures/test';

test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Authentication', () => {
  test('standard user signs in @smoke', async ({ page, loginPage, inventoryPage }) => {
    await test.step('open the sign-in page', async () => {
      await loginPage.open();
      await expect(loginPage.loginButton).toBeVisible();
    });

    await test.step('submit valid credentials', async () => {
      await loginPage.login(config.credentials.web.username, config.credentials.web.password);
    });

    await test.step('land on the catalogue', async () => {
      await expect(page).toHaveURL(/inventory\.html$/);
      await expect(inventoryPage.title).toHaveText('Products');
      await expect(inventoryPage.cartLink).toBeVisible();
    });
  });

  const rejectedLogins = [
    {
      username: 'locked_out_user',
      password: 'secret_sauce',
      message: 'Sorry, this user has been locked out.',
    },
    {
      username: 'not_a_real_user',
      password: 'wrong_password',
      message: 'Username and password do not match any user in this service',
    },
  ];

  for (const { username, password, message } of rejectedLogins) {
    test(`rejects ${username} @regression`, async ({ loginPage }) => {
      await loginPage.open();
      await loginPage.login(username, password);

      await expect(loginPage.errorMessage).toContainText(message);
    });
  }

  test('an unauthenticated visit to a protected page is refused @regression', async ({
    page,
    loginPage,
  }) => {
    await page.goto('/inventory.html');

    await expect(loginPage.errorMessage).toContainText('when you are logged in');
  });
});
