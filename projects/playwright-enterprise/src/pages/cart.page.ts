import type { Locator, Page } from '@playwright/test';
import { BasePage } from './base.page';

export class CartPage extends BasePage {
  readonly title: Locator;
  readonly checkoutButton: Locator;
  readonly continueShoppingButton: Locator;
  readonly cartBadge: Locator;

  constructor(page: Page) {
    super(page, '/cart.html');
    this.title = page.getByTestId('title');
    this.checkoutButton = page.getByTestId('checkout');
    this.continueShoppingButton = page.getByTestId('continue-shopping');
    this.cartBadge = page.getByTestId('shopping-cart-badge');
  }

  /** Scoping by row first, then text, is what keeps `getByText` from matching the whole page. */
  item(productName: string): Locator {
    return this.page.getByTestId('inventory-item').filter({ hasText: productName });
  }

  async removeItem(productSlug: string): Promise<void> {
    await this.page.getByTestId(`remove-${productSlug}`).click();
  }

  async beginCheckout(): Promise<void> {
    await this.checkoutButton.click();
  }
}
