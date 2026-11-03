# 03 — Playwright Patterns in TypeScript

## 1. Typed fixtures

```ts
import { test as base, expect, Page } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { InventoryPage } from '../pages/InventoryPage';

type Fixtures = {
  loginPage: LoginPage;
  inventoryPage: InventoryPage;
};

export const test = base.extend<Fixtures>({
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },
  inventoryPage: async ({ page }, use) => {
    await use(new InventoryPage(page));
  },
});

export { expect };
```

Benefits:

- Each test gets fresh page objects tied to its isolated `Page`.
- No shared mutable state.
- Type-safe autocomplete.
- Setup can be composed and scoped.

---

## 2. Page objects

```ts
import { Page, Locator, expect } from '@playwright/test';

export class LoginPage {
  readonly username: Locator;
  readonly password: Locator;
  readonly submit: Locator;

  constructor(private readonly page: Page) {
    this.username = page.getByTestId('username');
    this.password = page.getByTestId('password');
    this.submit = page.getByRole('button', { name: 'Sign in' });
  }

  async goto(): Promise<void> {
    await this.page.goto('/');
  }

  async login(user: string, pass: string): Promise<void> {
    await this.username.fill(user);
    await this.password.fill(pass);
    await this.submit.click();
  }

  async expectError(message: string): Promise<void> {
    await expect(this.page.getByRole('alert')).toContainText(message);
  }
}
```

Rules:

- Locators in the constructor (or lazy getters).
- Actions are user-level, not low-level clicks.
- Assertions about business outcomes live in the spec; page objects can expose state checks.
- Return the next page object for navigation if it improves readability.
- No `expect` on unrelated global state inside page objects.

---

## 3. API clients

```ts
import { APIRequestContext, expect } from '@playwright/test';

export class ListingApi {
  constructor(private readonly request: APIRequestContext) {}

  async create(payload: CreateListing): Promise<Listing> {
    const response = await this.request.post('/api/listings', { data: payload });
    expect(response.status()).toBe(201);
    return response.json() as Promise<Listing>;
  }

  async get(id: string): Promise<Listing> {
    const response = await this.request.get(`/api/listings/${id}`);
    expect(response.status()).toBe(200);
    return response.json() as Promise<Listing>;
  }
}
```

Use API setup to avoid clicking through the UI for state. Keep assertions about the API contract in
the API client/tests.

---

## 4. Authentication state

```ts
// auth.setup.ts
import { test as setup, expect } from '@playwright/test';

setup('authenticate', async ({ page }) => {
  await page.goto('/');
  await page.getByTestId('username').fill(process.env.USER!);
  await page.getByTestId('password').fill(process.env.PASS!);
  await page.getByRole('button', { name: 'Sign in' }).click();
  await expect(page.getByTestId('inventory')).toBeVisible();
  await page.context().storageState({ path: 'auth.json' });
});
```

```ts
// playwright.config.ts
projects: [
  { name: 'setup', testMatch: /auth\.setup\.ts/ },
  {
    name: 'chromium',
    use: { storageState: 'auth.json' },
    dependencies: ['setup'],
  },
]
```

Do not commit `auth.json`; it contains session state.

---

## 5. Test data factories

```ts
let counter = 0;

export function uniqueEmail(prefix = 'buyer'): string {
  counter += 1;
  return `${prefix}-${Date.now()}-${counter}@example.test`;
}

export function listingPayload(overrides: Partial<CreateListing> = {}): CreateListing {
  return {
    title: `Listing ${Date.now()}`,
    price: 10.0,
    currency: 'USD',
    sellerId: 'seller-1',
    ...overrides,
  };
}
```

Factories keep tests readable and parallel-safe.

---

## 6. Typed API responses

```ts
interface Money {
  amount: number;
  currency: string;
}

interface Listing {
  id: string;
  title: string;
  price: Money;
  status: 'DRAFT' | 'ACTIVE' | 'SOLD';
}
```

If the API is external/untyped, validate at runtime with Zod or a guard. A cast (`as Listing`) is not
validation.

---

## 7. Common Playwright/TS mistakes

1. `page.locator(...).click()` without `await`.
2. Using CSS/XPath when role/test-id is available.
3. Fixed `waitForTimeout`.
4. Sharing `Page` or mutable state across tests.
5. `any` for `Page`/fixtures.
6. Assertions in page objects that belong in specs.
7. Test data collisions in parallel runs.
8. Storing auth state in the repo.
9. Not capturing trace/video on failure.
10. No `tsc --noEmit` in CI.

---

## 8. Framework checklist

- [ ] Typed fixtures for page objects/API clients.
- [ ] Stable locators (role/test-id).
- [ ] Auto-waiting assertions, no fixed sleeps.
- [ ] Isolated context per test.
- [ ] API setup for state.
- [ ] Unique test data.
- [ ] Auth state generated, not committed.
- [ ] Trace/screenshot/video on failure.
- [ ] `tsc --noEmit` in CI.
- [ ] Config via env, fail fast if missing.
