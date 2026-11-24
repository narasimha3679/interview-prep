import type { Locator, Page } from '@playwright/test';
import { BasePage } from './base.page';

/**
 * Product catalogue.
 *
 * `addToCart(slug)` takes the product slug rather than a display name: slugs are stable
 * (`sauce-labs-backpack`), display names are copy that marketing can change on a Tuesday.
 */
export class InventoryPage extends BasePage {
  readonly title: Locator;
  readonly cartLink: Locator;
  readonly cartBadge: Locator;
  readonly sortDropdown: Locator;
  readonly itemNames: Locator;

  constructor(page: Page) {
    super(page, '/inventory.html');
    this.title = page.getByTestId('title');
    this.cartLink = page.getByTestId('shopping-cart-link');
    this.cartBadge = page.getByTestId('shopping-cart-badge');
    this.sortDropdown = page.getByTestId('product-sort-container');
    this.itemNames = page.getByTestId('inventory-item-name');
  }

  async addToCart(productSlug: string): Promise<void> {
    await this.page.getByTestId(`add-to-cart-${productSlug}`).click();
  }

  async removeFromCart(productSlug: string): Promise<void> {
    await this.page.getByTestId(`remove-${productSlug}`).click();
  }

  async openCart(): Promise<void> {
    await this.cartLink.click();
  }
}
