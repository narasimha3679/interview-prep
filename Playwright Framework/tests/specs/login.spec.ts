import { test, expect } from '../fixtures/test';

const invalidCredentials = [
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

test('standard user can sign in @smoke @regression', async ({ loginPage, inventoryPage }) => {
  const username = process.env.SAUCEDEMO_USERNAME ?? 'standard_user';
  const password = process.env.SAUCEDEMO_PASSWORD ?? 'secret_sauce';

  await test.step('open the sign-in page', async () => {
    await loginPage.open();
  });

  await test.step('sign in with a valid account', async () => {
    await loginPage.login(username, password);
  });

  await expect(inventoryPage.heading).toBeVisible();
});

for (const { username, password, message } of invalidCredentials) {
  test(`shows a helpful error for ${username} @regression`, async ({ loginPage }) => {
    await loginPage.open();
    await loginPage.login(username, password);

    await expect(loginPage.errorMessage).toContainText(message);
  });
}
