import type { Locator, Page } from '@playwright/test';
import { BasePage } from './base.page';

export type CustomerDetails = {
  firstName: string;
  lastName: string;
  postalCode: string;
};

/**
 * Checkout spans three screens. The page object spans them too, because a spec should describe one
 * journey — not the number of URLs the vendor happened to build.
 */
export class CheckoutPage extends BasePage {
  readonly firstNameInput: Locator;
  readonly lastNameInput: Locator;
  readonly postalCodeInput: Locator;
  readonly continueButton: Locator;
  readonly finishButton: Locator;
  readonly completeHeader: Locator;
  readonly errorMessage: Locator;
  readonly summaryTotal: Locator;

  constructor(page: Page) {
    super(page, '/checkout-step-one.html');
    this.firstNameInput = page.getByTestId('firstName');
    this.lastNameInput = page.getByTestId('lastName');
    this.postalCodeInput = page.getByTestId('postalCode');
    this.continueButton = page.getByTestId('continue');
    this.finishButton = page.getByTestId('finish');
    this.completeHeader = page.getByTestId('complete-header');
    this.errorMessage = page.getByTestId('error');
    this.summaryTotal = page.getByTestId('total-label');
  }

  async enterCustomerDetails(details: CustomerDetails): Promise<void> {
    await this.firstNameInput.fill(details.firstName);
    await this.lastNameInput.fill(details.lastName);
    await this.postalCodeInput.fill(details.postalCode);
    await this.continueButton.click();
  }

  async finishOrder(): Promise<void> {
    await this.finishButton.click();
  }
}
