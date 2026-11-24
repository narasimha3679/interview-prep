/**
 * The critical path: catalogue → cart → checkout → confirmation.
 *
 * `test.step` is not decoration. Each step becomes a named node in the trace and the HTML report, so
 * a failure reads "review the order" rather than "click timeout at line 41".
 */
import { customer } from '../../src/data/factories';
import { expect, test } from '../../src/fixtures/test';

test('a signed-in customer can complete a purchase @smoke', async ({
  inventoryPage,
  cartPage,
  checkoutPage,
}) => {
  await test.step('add two products to the cart', async () => {
    await inventoryPage.open();
    await inventoryPage.addToCart('sauce-labs-backpack');
    await inventoryPage.addToCart('sauce-labs-bike-light');
    await expect(inventoryPage.cartBadge).toHaveText('2');
  });

  await test.step('review the order', async () => {
    await inventoryPage.openCart();
    await expect(cartPage.item('Sauce Labs Backpack')).toBeVisible();
    await expect(cartPage.item('Sauce Labs Bike Light')).toBeVisible();
    await cartPage.beginCheckout();
  });

  await test.step('enter delivery details', async () => {
    await checkoutPage.enterCustomerDetails(customer);
    await expect(checkoutPage.summaryTotal).toContainText('Total');
  });

  await test.step('place the order', async () => {
    await checkoutPage.finishOrder();
    await expect(checkoutPage.completeHeader).toHaveText('Thank you for your order!');
  });
});
