# TestNG for SDETs

Lifecycle, parallel execution, assertions, listeners, and retries.

## 60-second cram

1. Order: suite → test → class → groups → method.
2. `@BeforeMethod`/`@AfterMethod` for per-test setup/teardown.
3. `@DataProvider` for data-driven tests; `@Parameters` for suite values.
4. `parallel=classes` is the safest starting point.
5. `ThreadLocal` + unique data for parallel safety.
6. Hard assert stops; soft assert reports at `assertAll()`.
7. Listeners are event hooks: `ITestListener`, `ISuiteListener`.
8. Always `flush()` reports; forgetting it gives an empty report.
9. `IRetryAnalyzer` retries failures; use as a detector, not a fix.
10. `testng.xml` is the suite contract.

## File map

| File | Topic |
|---|---|
| [`annotations-and-order.md`](annotations-and-order.md) | execution order |
| [`assertions.md`](assertions.md) | hard vs soft assert |
| [`retry-and-listeners.md`](retry-and-listeners.md) | retry, ITestResult |
| [`listeners-and-reporting.md`](listeners-and-reporting.md) | listeners, Extent |
| [`testng-vs-junit.md`](testng-vs-junit.md) | comparison |
| [`testng-xml-and-parallel.md`](testng-xml-and-parallel.md) | parallel modes |
| [`unique-test-data-per-thread.md`](unique-test-data-per-thread.md) | parallel data |

## Related

- [`../../projects/selenium-bdd/`](../../projects/selenium-bdd/) — TestNG runner
- [`../../projects/restassured-bdd/`](../../projects/restassured-bdd/) — API runner
- [`../selenium/`](../selenium/) — UI theory
