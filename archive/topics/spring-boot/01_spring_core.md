# 01 — Spring Core

## 1. What Spring Boot gives you

- **IoC container** — creates and wires objects.
- **Dependency injection** — constructor, setter, or field injection.
- **Auto-configuration** — configures beans based on the classpath and properties.
- **Starter dependencies** — curated dependency sets (`spring-boot-starter-web`, `-data-jpa`, `-test`).
- **Embedded server** — Tomcat/Jetty/Undertow inside the app.
- **Externalized configuration** — properties, YAML, env vars, command-line args.
- **Actuator** — health, metrics, info, env.
- **Testing support** — slices, mock beans, test context.

---

## 2. IoC and dependency injection

**Inversion of Control:** the container, not your code, creates and wires objects.

**Dependency injection:** a class declares what it needs; the container provides it.

```java
@RestController
public class ListingController {

    private final ListingService service;

    public ListingController(ListingService service) {
        this.service = service;
    }
}
```

Constructor injection is preferred because:

- Dependencies are explicit and final.
- The object cannot be constructed in an invalid state.
- Tests can pass fakes without reflection.
- Circular dependencies are visible early.

---

## 3. Stereotypes

| Annotation | Role |
|---|---|
| `@Component` | Generic Spring bean |
| `@Service` | Business/service bean |
| `@Repository` | Data access; translates persistence exceptions |
| `@Controller` | Web controller returning views |
| `@RestController` | `@Controller` + `@ResponseBody` |
| `@Configuration` | Bean definitions |
| `@Bean` | Method-level bean definition |
| `@ComponentScan` | Package scanning |
| `@SpringBootApplication` | `@Configuration` + auto-config + component scan |

---

## 4. Auto-configuration

Spring Boot looks at the classpath and properties and configures sensible defaults.

Examples:

- `spring-boot-starter-web` → embedded Tomcat, Jackson, `DispatcherServlet`.
- `spring-boot-starter-data-jpa` → Hibernate, `EntityManager`, transaction manager.
- `spring-boot-starter-validation` → Bean Validation.
- `spring-boot-starter-actuator` → health/metrics endpoints.

You can override with your own `@Bean`, properties, or exclude an auto-configuration.

**Testing implication:** auto-configuration is real behavior. A slice test may not catch a missing
bean or a profile-specific wiring issue; `@SpringBootTest` does.

---

## 5. Configuration

### Sources and precedence (high → low, simplified)

1. Command-line arguments.
2. Environment variables.
3. `application-{profile}.yml`.
4. `application.yml`.
5. Defaults in code.

### Profiles

```yaml
# application.yml
spring:
  profiles:
    active: local

---
# application-local.yml
spring:
  datasource:
    url: jdbc:h2:mem:local

---
# application-postgres.yml
spring:
  datasource:
    url: ${DB_URL}
```

Run with:

```bash
java -jar app.jar --spring.profiles.active=postgres
```

**Testing rule:** tests should use the same profile semantics as the target environment. Do not
hardcode a test profile that hides a production config bug.

### Typed configuration

```java
@ConfigurationProperties(prefix = "listings")
public record ListingProperties(
        int maxPageSize,
        Duration requestTimeout
) {}
```

Typed config fails fast at startup if a required value is missing.

---

## 6. Actuator

| Endpoint | Use |
|---|---|
| `/actuator/health` | Liveness/readiness |
| `/actuator/info` | Build/version info |
| `/actuator/metrics` | Runtime metrics |
| `/actuator/env` | Config (protect it) |
| `/actuator/loggers` | Change log levels at runtime |

Expose only what you need:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      probes:
        enabled: true
```

**Release validation:** hit `/actuator/health` and the readiness probe before running smoke tests.

---

## 7. Error handling

Use `@RestControllerAdvice` for a stable error model:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ListingNotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(ListingNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(404)
                .body(ErrorResponse.of(404, "NOT_FOUND", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex,
                                             HttpServletRequest req) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a));
        return ResponseEntity.badRequest()
                .body(ErrorResponse.validation(fields, req.getRequestURI()));
    }
}
```

**Rule:** clients should be able to branch on a stable `code`, not parse a human message.

---

## 8. Logging and observability

- Use SLF4J, not `System.out`.
- Include a correlation/trace ID.
- Log at the boundary: request, key decision, failure.
- Do not log secrets or full PII.
- Use structured logs where the platform supports it.
- Expose metrics for request count, latency, errors, and dependency health.

```java
private static final Logger log = LoggerFactory.getLogger(ListingService.class);

log.info("Creating listing sellerId={} correlationId={}", sellerId, correlationId);
```

---

## 9. Spring traps

1. **Field injection** — hard to test, hides dependencies.
2. **`@Transactional` on a private/self-invoked method** — proxying does not apply.
3. **Lazy loading outside a transaction** — `LazyInitializationException`.
4. **Entity equals/hashCode with generated IDs** — unstable across states.
5. **Catching `Exception` and returning 200** — hides failures.
6. **`ddl-auto=update` in production** — unpredictable schema changes; use migrations.
7. **Exposing all actuator endpoints** — leaks config and environment.
8. **Circular dependencies** — usually a design smell; fix the boundary.
9. **Profiles that drift** — test the profile used in the target environment.
10. **No timeout on outbound calls** — one slow dependency hangs the service.
