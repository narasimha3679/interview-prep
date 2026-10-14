import type { Locator, Page } from '@playwright/test';

export class InventoryPage {
  readonly heading: Locator;
  readonly cartLink: Locator;

  constructor(private readonly page: Page) {
    this.heading = page.getByText('Products', { exact: true });
    this.cartLink = page.getByTestId('shopping-cart-link');
  }

  async addItemToCart(itemId: string): Promise<void> {
    await this.page.getByTestId(`add-to-cart-${itemId}`).click();
  }

  async openCart(): Promise<void> {
    await this.cartLink.click();
  }
}
