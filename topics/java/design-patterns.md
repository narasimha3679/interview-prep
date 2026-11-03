# Java — Design Patterns for Test Frameworks

Patterns are vocabulary for design decisions. Learn the problem each solves and the trade-off.

---

## 1. Factory

**Problem:** create the right implementation without exposing construction logic.

```java
public final class DriverFactory {
    public static WebDriver create(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            default -> throw new IllegalArgumentException("Unsupported: " + browser);
        };
    }
}
```

**Test use:** browser/driver creation, API client per environment.

**Trade-off:** hides construction; keep the factory small and explicit.

---

## 2. Builder

**Problem:** construct complex objects with many optional fields.

```java
Listing listing = Listing.builder()
        .title("Camera")
        .price(new BigDecimal("249.99"))
        .currency("USD")
        .sellerId("s1")
        .build();
```

**Test use:** request specs, test data, page objects.

**Trade-off:** more code than a constructor; worth it for readability.

---

## 3. Strategy

**Problem:** swap algorithms/behaviors at runtime.

```java
interface AuthStrategy {
    void apply(RequestSpecification spec);
}

class BearerAuth implements AuthStrategy { ... }
class ApiKeyAuth implements AuthStrategy { ... }
```

**Test use:** auth methods, environment strategies, retry policies.

**Trade-off:** more types; avoid if a simple parameter suffices.

---

## 4. Template Method

**Problem:** define a workflow with customizable steps.

```java
abstract class ApiTestBase {
    @BeforeMethod
    void setUp() { configure(); authenticate(); }

    protected abstract void configure();
    protected void authenticate() { /* default */ }
}
```

**Test use:** base test classes, setup/teardown.

**Trade-off:** inheritance coupling; prefer composition/fixtures.

---

## 5. Adapter

**Problem:** wrap an incompatible interface.

```java
class LegacyUserClientAdapter implements UserClient {
    private final LegacyUserClient legacy;
    public User getUser(String id) {
        return map(legacy.fetchUser(id));
    }
}
```

**Test use:** wrap third-party SDKs, legacy services, cloud clients.

**Trade-off:** extra layer; keeps vendor details out of tests.

---

## 6. Decorator

**Problem:** add behavior without modifying the object.

```java
class LoggingApiClient implements ApiClient {
    private final ApiClient delegate;
    public Response get(String path) {
        log.info("GET {}", path);
        return delegate.get(path);
    }
}
```

**Test use:** logging, retries, metrics around clients.

**Trade-off:** many small wrappers; watch ordering.

---

## 7. Observer

**Problem:** notify multiple listeners of events.

**Test use:** TestNG listeners, reporting hooks, event bus.

**Trade-off:** hidden control flow; keep listeners small.

---

## 8. Dependency Injection

**Problem:** supply dependencies instead of constructing them.

```java
class CheckoutTest {
    private final CheckoutPage page;
    CheckoutTest(CheckoutPage page) { this.page = page; }
}
```

**Test use:** Playwright fixtures, constructor-injected page objects, Spring.

**Trade-off:** wiring complexity; use a container/fixtures when it pays off.

---

## 9. Page Object

**Problem:** encapsulate page structure and user actions.

**Test use:** UI automation.

**Trade-off:** can become a god object; keep assertions about outcomes in specs.

---

## 10. Repository

**Problem:** abstract persistence.

**Test use:** Spring Data repositories, test data access.

**Trade-off:** leaks persistence concerns if used as a domain model.

---

## 11. Singleton

**Problem:** one instance.

**Test use:** config manager, driver manager (careful).

**Trade-off:** global state, hidden dependencies, parallel-test problems. Prefer DI/ThreadLocal.

---

## 12. Proxy

**Problem:** control access to an object.

**Test use:** lazy loading, retries, mocks, Spring AOP/transactions.

**Trade-off:** indirection; debugging gets harder.

---

## 13. Choosing a pattern

| Need | Pattern |
|---|---|
| Create driver/client by type | Factory |
| Complex request/test data | Builder |
| Swap auth/retry/env behavior | Strategy |
| Shared test lifecycle | Template Method |
| Wrap vendor/legacy API | Adapter |
| Add logging/retry to a client | Decorator |
| Event hooks/reporting | Observer |
| Provide page objects/clients | DI/fixtures |
| Encapsulate a page | Page Object |
| Abstract persistence | Repository |
| One shared config | Singleton (careful) |
| Control access/lazy load | Proxy |

---

## 14. Anti-patterns

- **God object** — one base class does everything.
- **Singleton everywhere** — hidden global state.
- **Inheritance for reuse** — fragile base class.
- **Pattern for pattern's sake** — complexity without a problem.
- **Leaky abstraction** — the wrapper exposes vendor details anyway.

---

## 15. Interview line

> "I use patterns as vocabulary, not decoration. Factory for driver/client creation, builder for
> request specs and test data, strategy for auth/retry, adapter for vendor SDKs, and DI/fixtures for
> composition. If a simple function is clearer, I don't add a pattern."
