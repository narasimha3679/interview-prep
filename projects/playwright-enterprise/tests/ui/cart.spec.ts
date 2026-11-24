/**
 * Cart behaviour. No `open()` login here: the `ui` project already carries the stored session, and
 * that is the payoff of the setup project.
 */
import { expect, test } from '../../src/fixtures/test';

test.describe('Cart', () => {
  test('removing an item updates the badge and the list @regression', async ({
    inventoryPage,
    cartPage,
  }) => {
    await test.step('add two products', async () => {
      await inventoryPage.open();
      await inventoryPage.addToCart('sauce-labs-backpack');
      await inventoryPage.addToCart('sauce-labs-bike-light');
      await expect(inventoryPage.cartBadge).toHaveText('2');
    });

    await test.step('open the cart', async () => {
      await inventoryPage.openCart();
      await expect(cartPage.title).toHaveText('Your Cart');
      await expect(cartPage.item('Sauce Labs Backpack')).toBeVisible();
      await expect(cartPage.item('Sauce Labs Bike Light')).toBeVisible();
    });

    await test.step('remove one product', async () => {
      await cartPage.removeItem('sauce-labs-backpack');
      await expect(cartPage.item('Sauce Labs Backpack')).toHaveCount(0);
      await expect(cartPage.item('Sauce Labs Bike Light')).toBeVisible();
      await expect(cartPage.cartBadge).toHaveText('1');
    });
  });
});
