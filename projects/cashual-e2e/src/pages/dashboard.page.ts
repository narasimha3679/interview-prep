import type { Locator, Page } from "@playwright/test";

export class DashboardPage {

    readonly heading: Locator;

    constructor(private readonly page: Page) {
        this.heading = page.getByRole('heading', { level: 1, name: 'Cockpit' });
    }

    async open() {
        await this.page.goto('/dashboard');
    }
}
