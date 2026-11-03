# 02 — Tools and Automation

## 1. axe-core

The most common automated accessibility engine.

### Playwright

```ts
import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test('login page has no critical accessibility violations', async ({ page }) => {
  await page.goto('/login');

  const results = await new AxeBuilder({ page })
    .withTags(['wcag2a', 'wcag2aa'])
    .analyze();

  expect(results.violations).toEqual([]);
});
```

You can exclude known issues, but track them with an owner and expiry.

### Selenium/Java

```java
import com.deque.html.axecore.selenium.AxeBuilder;

Results results = new AxeBuilder().analyze(driver);
assertTrue(results.getViolations().isEmpty());
```

### CLI

```bash
npx axe https://app.example.test --tags wcag2a,wcag2aa
```

---

## 2. Lighthouse

```bash
npx lighthouse https://app.example.test --only-categories=accessibility --output=json
```

Useful for a quick score and performance/SEO overlap. A score is not a conformance proof.

---

## 3. Manual keyboard pass

1. Start at the top of the page.
2. Press Tab through every interactive element.
3. Confirm focus is visible.
4. Confirm order matches visual/logical order.
5. Activate buttons/links with Enter/Space.
6. Open/close modals; confirm focus trap/return.
7. Confirm no keyboard trap.
8. Check skip links.

---

## 4. Screen reader pass

| Platform | Tool |
|---|---|
| Windows | NVDA (free), JAWS |
| macOS/iOS | VoiceOver |
| Android | TalkBack |
| Linux | Orca |

Test:

- Page title/headings.
- Landmarks/navigation.
- Form labels/errors.
- Button/link names.
- Dynamic announcements.
- Modal focus.
- Image alt text.

---

## 5. Browser/device checks

- Zoom to 200%.
- Increase font size.
- High contrast/dark mode.
- Reduced motion.
- Mobile viewport/touch targets.
- Orientation change.

---

## 6. CI integration

```text
PR
  -> axe scan on critical pages/components
  -> lint (eslint-plugin-jsx-a11y)
Merge/nightly
  -> Lighthouse accessibility
  -> keyboard/screen-reader checklist for changed journeys
Pre-release
  -> manual accessibility pass on critical journeys
```

Rules:

- Fail on new critical/serious violations.
- Track existing violations with owner/expiry.
- Do not rely on the score alone.
- Include accessibility in definition of done.

---

## 7. Common tooling mistakes

1. Scanning only the home page.
2. Ignoring serious/moderate findings.
3. Suppressing violations without a ticket.
4. Testing only in Chromium.
5. Not testing after opening modals/menus.
6. Not testing dynamic content.
7. Assuming a Lighthouse 100 means accessible.
8. No manual keyboard/screen-reader pass.

---

## 8. Tooling checklist

- [ ] axe on critical pages/components.
- [ ] CI fails on new critical/serious issues.
- [ ] Existing violations tracked.
- [ ] Keyboard pass in the release checklist.
- [ ] Screen-reader pass for critical journeys.
- [ ] Lighthouse as a signal, not proof.
- [ ] Mobile/touch targets checked.
- [ ] Reduced motion/zoom checked.
