import { expect, test } from '../../src/fixtures/test';

test('company owner sees the cockpit', { tag: '@smoke' }, async ({ dashboardPage }) => {
  await dashboardPage.open();
  await expect(dashboardPage.heading).toBeVisible();
});
