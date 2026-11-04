# Spring Boot for SDETs — Study Pack

How to build and test a small Java backend service: Spring Boot, REST design, persistence, and the
testing layers that prove it works.

**Last verified:** November 2026.

---

## 60-second cram

1. **Spring Boot is opinionated Spring.** Auto-configuration, starter dependencies, embedded server,
   and externalized config. You get a runnable app with one `main`.
2. **Dependency injection is the core.** `@RestController`, `@Service`, `@Repository`, and
   `@Configuration` are beans managed by the container. Constructor injection is the default choice.
3. **Layers:** controller (HTTP) → service (business rules) → repository (persistence). DTOs at the
   boundary; entities inside.
4. **Validation belongs at the boundary.** `@Valid` + Bean Validation, then a global exception
   handler that returns a stable error model.
5. **REST semantics matter.** Correct method, status, idempotency, pagination, filtering, and
   versioning are part of the contract.
6. **Test at the right level.** Unit for logic, `@WebMvcTest` for the web slice, `@DataJpaTest` for
   persistence, `@SpringBootTest` for wiring, Testcontainers for real infrastructure.
7. **Do not test the framework.** Test your behavior: status, body, persistence, transactions, and
   error paths.
8. **A service is more than a controller.** Auth, idempotency, pagination, migrations, observability,
   and graceful failure are what production actually needs.

**The one line that shows depth:**

> "I keep the controller thin, put the business rules in the service, keep entities out of the API,
> and test each layer at the cheapest level that can still catch the defect."

---

## What the mini-service demonstrates

```text
examples/mini-service/
  ListingController   -> HTTP, validation, status codes, pagination
  ListingService      -> business rules, not-found handling
  ListingRepository   -> Spring Data JPA
  Listing             -> entity
  DTOs                -> request/response records
  GlobalExceptionHandler -> stable error model
  H2                  -> in-memory database for the demo
  tests               -> MockMvc integration tests
```

Endpoints:

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/listings` | Create a listing |
| `GET` | `/api/listings/{id}` | Get one |
| `GET` | `/api/listings?page=0&size=10&status=ACTIVE` | List with pagination/filter |
| `PUT` | `/api/listings/{id}` | Update |
| `DELETE` | `/api/listings/{id}` | Delete |
| `GET` | `/actuator/health` | Health check |

Run:

```bash
cd examples/mini-service
mvn test
mvn spring-boot:run
```

Then:

```bash
curl -X POST http://localhost:8080/api/listings \
  -H "Content-Type: application/json" \
  -d '{"title":"Vintage camera","description":"Working","price":249.99,"currency":"USD","sellerId":"s1"}'
```

---

## File map

| File | What it gives you |
|---|---|
| `02_rest_api_design.md` | Resources, status codes, idempotency, pagination, errors |
| `03_persistence.md` | JPA, transactions, DTOs, N+1, migrations, test data |
| `04_testing.md` | Unit, slice, integration, Testcontainers, WireMock |
| `examples/mini-service/` | Runnable service + tests |

Moved to [`../../archive/topics/spring-boot/`](../../archive/topics/spring-boot/): `01_spring_core.md`
and `05_qa.md`. They are dev-side material, not needed for an SDET role.

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Bean** | Object managed by Spring | Inject it; don't instantiate manually in tests |
| **IoC container** | Creates and wires beans | Misconfiguration shows up at startup |
| **Auto-configuration** | Boot config based on classpath | Test the actual wiring with `@SpringBootTest` |
| **Profile** | Named config set | Test with the profile used in the target env |
| **DTO** | Boundary data shape | Validate and assert it, not the entity |
| **Entity** | Persistent domain object | Keep out of the API; test persistence separately |
| **Repository** | Data access abstraction | `@DataJpaTest` or Testcontainers |
| **Transaction** | Unit of work | Test rollback and isolation |
| **Actuator** | Operational endpoints | Health/readiness are release-validation targets |
| **Slice test** | Loads part of the context | Fast, focused, but not full wiring |
| **Testcontainers** | Real dependency in Docker | Best for DB/broker integration |

---

## Traps

1. **Putting business logic in the controller.** It becomes untestable and duplicated.
2. **Returning entities directly.** Couples the API to the schema and leaks fields.
3. **Catching exceptions and returning 200.** Clients cannot distinguish success from failure.
4. **Using `@SpringBootTest` for everything.** Slow feedback; use slices where possible.
5. **Testing with H2 and assuming production SQL behavior.** Dialect differences matter.
6. **Ignoring transaction boundaries.** Lazy loading and rollback behavior change outside a
   transaction.
7. **No idempotency for retried writes.** Duplicate charges/orders.
8. **No pagination limits.** A client can request the whole table.
9. **Hardcoded config/secrets.** Use profiles/env/secret stores.
10. **No migration strategy.** `ddl-auto=update` is not a production migration tool.

---

## How to study this pack

- **10 minutes:** README + cram sheet.
- **30 minutes:** `02`, draw the request flow.
- **30 minutes:** `03`, explain entity vs DTO and transaction boundaries.
- **30 minutes:** `04`, run the mini-service tests and add one negative case.
