/**
 * The only `test` and `expect` a spec should import.
 *
 * Three ideas are worth being able to defend here:
 *
 * 1. `base.extend<Fixtures, WorkerFixtures>` gives one typed dependency graph. A spec asks for
 *    `checkoutPage`; the framework injects a page object bound to *this test's* isolated Page.
 * 2. `apiToken` is `scope: 'worker'`. Authentication is expensive and read-only, so one login per
 *    worker is enough. Page objects stay per-test because page state is not shareable.
 * 3. Teardown lives next to setup. `bookingCleanup` guarantees API data created during a test is
 *    deleted even when the test fails — which is exactly when it matters.
 *
 * Interview line: "Fixtures are dependency injection with teardown. Setup and cleanup in the same
 * block is the reason leakage stops."
 */
import { test as base, expect, request as playwrightRequest } from '@playwright/test';
import { ApiClient } from '../api/api-client';
import { AuthApi } from '../api/auth.api';
import { BookingApi } from '../api/booking.api';
import { config } from '../config/env';
import { CartPage } from '../pages/cart.page';
import { CheckoutPage } from '../pages/checkout.page';
import { InventoryPage } from '../pages/inventory.page';
import { LoginPage } from '../pages/login.page';

type Fixtures = {
  loginPage: LoginPage;
  inventoryPage: InventoryPage;
  cartPage: CartPage;
  checkoutPage: CheckoutPage;
  authApi: AuthApi;
  bookingApi: BookingApi;
  /** Push created booking ids here; the fixture deletes them during teardown. */
  bookingCleanup: number[];
};

type WorkerFixtures = {
  apiToken: string;
};

export const test = base.extend<Fixtures, WorkerFixtures>({
  apiToken: [
    // Playwright requires the first parameter to be a destructuring pattern.
    // eslint-disable-next-line no-empty-pattern
    async ({}, use) => {
      const context = await playwrightRequest.newContext({
        baseURL: config.api.baseUrl,
        extraHTTPHeaders: { Accept: 'application/json' },
      });

      try {
        const token = await new AuthApi(new ApiClient(context, config.api.baseUrl)).createToken(
          config.credentials.api.username,
          config.credentials.api.password,
        );
        await use(token);
      } finally {
        await context.dispose();
      }
    },
    { scope: 'worker' },
  ],

  authApi: async ({ request }, use) => {
    await use(new AuthApi(new ApiClient(request, config.api.baseUrl)));
  },

  bookingApi: async ({ request, apiToken }, use) => {
    await use(new BookingApi(new ApiClient(request, config.api.baseUrl), apiToken));
  },

  bookingCleanup: async ({ bookingApi }, use) => {
    const createdIds: number[] = [];
    await use(createdIds);

    for (const id of createdIds) {
      await bookingApi.removeIfExists(id);
    }
  },

  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },

  inventoryPage: async ({ page }, use) => {
    await use(new InventoryPage(page));
  },

  cartPage: async ({ page }, use) => {
    await use(new CartPage(page));
  },

  checkoutPage: async ({ page }, use) => {
    await use(new CheckoutPage(page));
  },
});

export { expect };
