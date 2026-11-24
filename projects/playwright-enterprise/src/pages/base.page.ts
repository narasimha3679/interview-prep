/**
 * Base page: navigation only.
 *
 * Deliberately tiny. Everything behavioural belongs to the concrete page, and every assertion
 * belongs to a spec. A base class that grows `isLoaded()` checks and generic click helpers becomes
 * the thing nobody can refactor.
 */
import type { Page } from '@playwright/test';

export abstract class BasePage {
  protected constructor(
    protected readonly page: Page,
    readonly path: string,
  ) {}

  async open(): Promise<void> {
    await this.page.goto(this.path);
  }
}
