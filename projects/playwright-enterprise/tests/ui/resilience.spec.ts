/**
 * Resilience against third-party failure, using request interception.
 *
 * Interview answer: "`page.route` lets me fail or delay a dependency the team does not control — an
 * image CDN, an analytics script, a payment iframe — and prove the checkout still completes. It is
 * also how I test error states without standing up a broken backend."
 *
 * This is a real assertion about product behaviour, not a stub that makes the test pass.
 */
import { customer } from '../../src/data/factories';
import { expect, test } from '../../src/fixtures/test';

test.describe('Resilience', () => {
  test('checkout still completes when product images fail @regression', async ({
    page,
    inventoryPage,
    cartPage,
    checkoutPage,
  }) => {
    await page.route('**/*.{png,jpg,jpeg,webp,svg}', (route) => route.abort());

    await inventoryPage.open();
    await inventoryPage.addToCart('sauce-labs-backpack');
    await inventoryPage.openCart();
    await expect(cartPage.item('Sauce Labs Backpack')).toBeVisible();

    await cartPage.beginCheckout();
    await checkoutPage.enterCustomerDetails(customer);
    await checkoutPage.finishOrder();

    await expect(checkoutPage.completeHeader).toHaveText('Thank you for your order!');
  });
});
