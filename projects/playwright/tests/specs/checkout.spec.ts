import { test, expect } from '../fixtures/test';

test('user can purchase an item from the catalog @smoke @regression', async ({
  loginPage,
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {
  const username = process.env.SAUCEDEMO_USERNAME ?? 'standard_user';
  const password = process.env.SAUCEDEMO_PASSWORD ?? 'secret_sauce';

  await loginPage.open();
  await loginPage.login(username, password);
  await expect(inventoryPage.heading).toBeVisible();

  await inventoryPage.addItemToCart('sauce-labs-backpack');
  await inventoryPage.openCart();
  await expect(cartPage.heading).toBeVisible();
  await expect(cartPage.item('Sauce Labs Backpack')).toBeVisible();

  await cartPage.beginCheckout();
  await checkoutPage.enterCustomerDetails('Interview', 'Candidate', '10001');
  await checkoutPage.finishOrder();

  await expect(checkoutPage.confirmation).toBeVisible();
});
