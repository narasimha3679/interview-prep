import {test as base, expect} from '@playwright/test';
import {DashboardPage} from '../pages/dashboard.page';

type Fixtures = {
    dashboardPage: DashboardPage;
}

export const test = base.extend<Fixtures>({
    dashboardPage: async ({page}, use) => {
        await use(new DashboardPage(page));
    }
});

export {expect};