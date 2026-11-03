# Cucumber for SDETs

BDD structure, data handling, and reusable steps.

## 60-second cram

1. Feature = behavior; scenario = one rule; step = one action/assertion.
2. Keep features free of URLs, selectors, and JSONPath jargon.
3. Step definitions orchestrate and assert; API clients/pages do the work.
4. `Scenario Outline` + `Examples` for data-driven cases.
5. `DataTable` for structured input; transform it into a model.
6. Share state through a context object, not static fields.
7. Reuse login/setup through hooks or callable steps.
8. Tags select suites (`@smoke`, `@negative`).
9. BDD is for collaboration; if nobody reads the features, the overhead may not pay off.
10. One behavior per scenario; avoid long UI scripts.

## File map

| File | Topic |
|---|---|
| [`data-in-bdd.md`](data-in-bdd.md) | Examples vs DataTable vs DataProvider |
| [`reusable-login.md`](reusable-login.md) | reusable login/setup |

## Related

- [`../../projects/selenium-bdd/`](../../projects/selenium-bdd/) — UI BDD
- [`../../projects/restassured-bdd/`](../../projects/restassured-bdd/) — API BDD
- [`../test-strategy/`](../test-strategy/) — test design
