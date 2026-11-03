# Capstone — Build and Ship a Testable Service

A single, end-to-end project that forces you to combine the repo instead of reading it in pieces.
The goal is **evidence**: a service, tests, a pipeline, a release checklist, and a demo you can
explain.

This is deliberately **not** a microservices lab. It is one service plus its tests and delivery
path. Microservices remain theory-only in `topics/microservices/`.

---

## Project: `catalog-lab`

A small marketplace-style catalog service with listings and orders.

Start from [`topics/spring-boot/examples/mini-service/`](topics/spring-boot/examples/mini-service/).

### Domain

- **Listing:** id, title, description, price, currency, status, sellerId, createdAt.
- **Order:** id, listingId, buyerId, quantity, total, status, createdAt.
- **Dependency:** a stubbed `inventory` or `payment` service you do not own.

### Status rules

```text
Listing: DRAFT -> ACTIVE -> SOLD
              \-> REMOVED

Order:   CREATED -> PAID -> FULFILLED
              \-> CANCELLED
```

Invalid transitions must return a stable error code.

---

## Phase 1 — Service foundation

**Build:**

- REST endpoints for listings and orders.
- DTOs, validation, global error handler, stable error codes.
- Pagination with a server-side cap, filtering, sorting.
- `Idempotency-Key` support for order creation.
- Status transition rules.
- H2 for local; PostgreSQL profile for integration.
- Flyway migration for the schema.
- Actuator health/readiness.

**Tests:**

- Unit tests for status transitions and totals.
- `@WebMvcTest` for status/body/validation/errors.
- `@DataJpaTest` for queries and constraints.
- One `@SpringBootTest` integration test.

**Done when:**

- `mvn test` passes.
- A duplicate `POST /orders` with the same idempotency key creates one order.
- An invalid status transition returns `409` with a stable code.

---

## Phase 2 — API contract and automation

**Build:**

- OpenAPI spec for the REST API.
- GraphQL endpoint for at least:
  - `listing(id)`
  - `listings(first, after, status)`
  - `createListing(input)`
- Schema validation for both REST and GraphQL.

**Tests:**

- RestAssured suite: happy path, validation, 401/403, 404, 409, pagination boundaries.
- GraphQL suite: query, mutation, variables, fragments, `errors[]`, nullability, auth, pagination.
- Schema diff in CI (OpenAPI and GraphQL SDL).

**Done when:**

- A GraphQL error returns HTTP 200 with `errors[]` and the test asserts both `data` and `errors`.
- A breaking schema change fails CI.
- The API suite runs from a clean environment with unique data.

---

## Phase 3 — Dependencies and failure modes

**Build:**

- A stubbed `inventory` client using WireMock or a local stub server.
- Timeout, retry with backoff, and a circuit breaker/fallback.
- Correlation ID propagation to the dependency.
- Structured logs and metrics for success/failure/latency.

**Tests:**

- Dependency success.
- 4xx and 5xx.
- Timeout.
- Malformed response.
- Retry/backoff behavior.
- Fallback/degradation.
- Correlation ID in logs/trace.

**Done when:**

- A slow dependency does not hang the service.
- A dependency failure produces the documented error/degradation.
- The failure is diagnosable by correlation ID.

---

## Phase 4 — UI and E2E

**Build:**

- A minimal page served by the service (or a small separate static app) that:
  - lists listings;
  - creates a listing;
  - creates an order;
  - shows validation errors.
- Keep it simple: HTML + fetch is enough.

**Tests:**

- Playwright page objects + typed fixtures.
- UI smoke: create listing → see it in the list → create order → confirmation.
- API setup for data; UI only for UI-specific risk.
- Trace/screenshot/video on failure.

**Done when:**

- The critical journey passes in Chromium and one mobile viewport.
- A failure produces a trace that identifies the failing step.
- UI tests do not create data by clicking through setup screens.

---

## Phase 5 — CI/CD and release validation

**Build:**

- GitHub Actions workflow:
  - compile + unit;
  - component/integration;
  - API/GraphQL smoke;
  - UI smoke;
  - artifact upload.
- Concurrency group and least-privilege permissions.
- A release workflow with a staging/canary step (Azure or local Docker equivalent).

**Tests:**

- Pipeline fails on a failing test.
- Reports/artifacts upload on failure.
- Release validation runs against the deployed artifact, not localhost.
- Rollback path is documented and rehearsed.

**Done when:**

- A red test blocks the pipeline.
- The same artifact is promoted, not rebuilt.
- The release checklist is completed and stored in the repo.

---

## Phase 6 — Cloud, observability, performance

**Build (choose one):**

- **Azure path:** App Service slot or Container Apps revision, managed identity, Key Vault,
  Application Insights.
- **Local path:** Docker Compose, structured logs, Prometheus/Grafana or simple metrics endpoint.

**Tests:**

- Health/readiness after deploy.
- Managed identity/secret access (Azure) or env-secret injection (local).
- Correlation ID in logs/traces.
- k6 smoke load against a critical endpoint with p95/error thresholds.
- Release validation checklist.

**Done when:**

- You can explain how to deploy, validate, monitor, and roll back.
- A performance smoke test fails on a deliberate regression.
- A failure produces a trace/log link, not just a stack trace.

---

## Deliverables

- [ ] Service code with REST + GraphQL.
- [ ] OpenAPI and GraphQL SDL checked in.
- [ ] Unit, web-slice, repository, integration, API, GraphQL, and UI tests.
- [ ] WireMock/stub for the external dependency.
- [ ] CI workflow with artifacts and concurrency.
- [ ] Release validation checklist.
- [ ] Rollback rehearsal notes.
- [ ] Observability: correlation ID, logs, metrics, health.
- [ ] k6 smoke test with thresholds.
- [ ] `README.md` with run/deploy/validate instructions.
- [ ] A 5-minute demo script.

---

## Demo script

1. **Problem:** a catalog service with listings, orders, and a dependency you don't own.
- **Architecture:** controller → service → repository; REST + GraphQL; one external dependency.
- **Risk:** duplicate order, invalid transition, dependency timeout, schema break.
- **Tests:** show one unit, one API, one GraphQL error, one dependency failure, one UI journey.
- **Pipeline:** show the stages and the artifact.
- **Release:** show staging/canary validation and rollback.
- **Observability:** show a correlation ID taking you to the failure.
- **Performance:** show the k6 threshold.
- **Trade-off:** what you did not test E2E and why.

---

## Rules

- Do not add a microservices lab, Pact, or Kafka. Theory stays in `topics/microservices/`.
- Do not add another UI framework. Use Playwright.
- Do not build a real payment integration. Stub it.
- Keep the service small enough to finish; depth beats breadth.
- Every claim in the demo must be backed by a test, artifact, or trace.

---

## Time estimate

| Phase | Time |
|---|---|
| 1 — Service foundation | 3–4 h |
| 2 — API/GraphQL automation | 3–4 h |
| 3 — Dependencies/failure modes | 2–3 h |
| 4 — UI/E2E | 2–3 h |
| 5 — CI/CD/release | 2–3 h |
| 6 — Cloud/observability/perf | 3–5 h |

If you have one day, do phases 1, 2, and 5. If you have two days, add 3 and 4. Phase 6 is the
stretch goal.
