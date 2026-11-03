import type { Locator, Page } from '@playwright/test';

export class CartPage {
  readonly heading: Locator;
  readonly checkoutButton: Locator;

  constructor(private readonly page: Page) {
    this.heading = page.getByText('Your Cart', { exact: true });
    this.checkoutButton = page.getByRole('button', { name: 'Checkout' });
  }

  item(itemName: string): Locator {
    return this.page.getByText(itemName, { exact: true });
  }

  async beginCheckout(): Promise<void> {
    await this.checkoutButton.click();
  }
}
