# 03 — Mobile Web with Playwright

Mobile web is browser-based, so Playwright/Selenium can test it. It is not the same as desktop web:
viewport, touch, user agent, browser engine, and network conditions matter.

---

## 1. Playwright device emulation

```ts
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  projects: [
    {
      name: 'mobile-chrome',
      use: { ...devices['Pixel 7'] },
    },
    {
      name: 'mobile-safari',
      use: { ...devices['iPhone 13'] },
    },
  ],
});
```

Built-in devices include viewport, user agent, device scale factor, and `isMobile`/`hasTouch`.

**Limit:** emulation is not a real device. It does not reproduce OS/browser quirks, performance, or
real network conditions. Use it for fast feedback and a device farm for release confidence.

---

## 2. Touch and viewport

```ts
test('adds item to cart on mobile', async ({ page }) => {
  await page.goto('/');
  await page.getByRole('button', { name: 'Add to cart' }).tap();
  await expect(page.getByTestId('cart-count')).toHaveText('1');
});
```

Playwright's `tap()` requires `hasTouch: true` (set by device profiles).

Test:

- Tap targets are large enough.
- Sticky headers/footers do not cover content.
- Modals fit the viewport.
- Horizontal scrolling does not appear unexpectedly.
- Orientation change (landscape/portrait).
- Keyboard opening does not hide the submit button.

---

## 3. Responsive layout

```ts
const viewports = [
  { name: 'small', width: 320, height: 568 },
  { name: 'common', width: 390, height: 844 },
  { name: 'large', width: 430, height: 932 },
  { name: 'tablet', width: 768, height: 1024 },
];

for (const viewport of viewports) {
  test(`checkout fits ${viewport.name}`, async ({ page }) => {
    await page.setViewportSize({ width: viewport.width, height: viewport.height });
    await page.goto('/checkout');
    await expect(page.getByRole('button', { name: 'Place order' })).toBeVisible();
  });
}
```

---

## 4. Network conditions

```ts
test('shows offline state', async ({ page, context }) => {
  await page.goto('/');
  await context.setOffline(true);
  await page.getByRole('button', { name: 'Refresh' }).tap();
  await expect(page.getByText('You are offline')).toBeVisible();
});
```

Playwright can also route/abort requests to simulate slow or failing APIs:

```ts
await page.route('**/api/listings', route =>
  route.fulfill({ status: 500, body: 'upstream error' }));
```

Test:

- Offline banner and retry.
- Slow API loading state.
- Failed request error state.
- Retry/backoff.
- Cached data behavior.

---

## 5. Mobile browser differences

| Area | Risk |
|---|---|
| **Viewport** | Content clipped, horizontal scroll |
| **Touch** | Hover-only interactions unusable |
| **Keyboard** | Inputs hidden, layout shift |
| **Safe areas** | Notch/home indicator overlap |
| **Font scaling** | Layout breaks |
| **Browser engine** | Safari/WebKit differences |
| **File upload** | Camera/gallery picker |
| **Permissions** | Location/camera prompts |
| **Deep links** | App/browser handoff |
| **Performance** | Slow device, low memory |

---

## 6. Mobile web checklist

- [ ] Device profiles for the supported matrix.
- [ ] Touch interactions use `tap`, not hover.
- [ ] Viewport tests for small/common/large/tablet.
- [ ] Orientation change tested.
- [ ] Keyboard does not hide critical actions.
- [ ] Safe areas respected.
- [ ] Offline/slow/failed network states tested.
- [ ] Deep links and app handoff tested.
- [ ] WebKit/Safari tested, not just Chromium.
- [ ] Real devices used for release confidence.
