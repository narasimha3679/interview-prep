# Mini Service

A small Spring Boot 3 service used by `topics/spring-boot/`.

## What it demonstrates

- Layered design: controller → service → repository
- DTOs at the API boundary, entity kept internal
- Bean Validation and a stable error model
- Pagination, filtering, and server-side page-size limits
- `201 Created` with a `Location` header
- `204 No Content` on delete
- Actuator health endpoint
- MockMvc integration tests

## Run

```bash
mvn test
mvn spring-boot:run
```

The app uses an in-memory H2 database. Data is lost on restart.

## Endpoints

| Method | Path | Notes |
|---|---|---|
| `POST` | `/api/listings` | Create a listing |
| `GET` | `/api/listings/{id}` | Get one |
| `GET` | `/api/listings?page=0&size=20&status=ACTIVE` | List with pagination/filter |
| `PUT` | `/api/listings/{id}` | Full update |
| `DELETE` | `/api/listings/{id}` | Delete |
| `GET` | `/actuator/health` | Health |

## Create example

```bash
curl -X POST http://localhost:8080/api/listings \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Vintage camera",
    "description": "Working condition",
    "price": 249.99,
    "currency": "USD",
    "sellerId": "seller-1"
  }'
```

## Exercises

1. Add an `Idempotency-Key` header to `POST` and test duplicate submission.
2. Add a `PATCH` endpoint for partial updates.
3. Add optimistic locking with `@Version` and test concurrent updates.
4. Replace H2 with PostgreSQL via Testcontainers.
5. Add Flyway migrations and test an expand/contract change.
6. Add OpenAPI documentation and validate the spec in CI.
7. Add a `GET /api/listings/{id}/offers` endpoint with its own pagination.
