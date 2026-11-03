# 02 — REST API Design

Good API design makes testing easier because the contract is explicit, consistent, and observable.

---

## 1. Resources and methods

| Method | Purpose | Idempotent? | Safe? |
|---|---|---|---|
| `GET` | Read | Yes | Yes |
| `POST` | Create / non-idempotent action | No | No |
| `PUT` | Replace / full update | Yes | No |
| `PATCH` | Partial update | Not necessarily | No |
| `DELETE` | Remove | Yes | No |
| `HEAD` | Metadata | Yes | Yes |
| `OPTIONS` | Capabilities | Yes | Yes |

**Rule:** the method should match the semantics, not the convenience of the client.

---

## 2. Status codes

| Code | Meaning | When |
|---|---|---|
| `200` | OK | Successful read/update |
| `201` | Created | Successful create; include `Location` |
| `204` | No Content | Successful delete/update with no body |
| `400` | Bad Request | Malformed/invalid input |
| `401` | Unauthorized | Missing/invalid authentication |
| `403` | Forbidden | Authenticated but not allowed |
| `404` | Not Found | Resource does not exist |
| `409` | Conflict | Duplicate/state conflict |
| `422` | Unprocessable Entity | Semantically invalid (optional) |
| `429` | Too Many Requests | Rate limit |
| `500` | Internal Server Error | Unexpected failure |
| `502` | Bad Gateway | Upstream failure |
| `503` | Service Unavailable | Overload/maintenance |
| `504` | Gateway Timeout | Upstream timeout |

**Test:** each status has a corresponding body/error model, not just a code.

---

## 3. Resource naming

```text
GET    /api/listings
GET    /api/listings/{id}
POST   /api/listings
PUT    /api/listings/{id}
DELETE /api/listings/{id}
GET    /api/listings/{id}/offers
POST   /api/listings/{id}/offers
```

Rules:

- Nouns, plural, lowercase, hyphenated.
- No verbs in the path for CRUD.
- Actions that are not CRUD can be sub-resources or explicit action endpoints:
  `POST /api/listings/{id}/publish`.
- Keep nesting shallow; beyond two levels, use a top-level resource.

---

## 4. Idempotency

Retries are inevitable. A non-idempotent write can duplicate a charge, order, or listing.

### Idempotency key

```http
POST /api/payments
Idempotency-Key: 7f3c2a1e-...
```

Server behavior:

1. First request with the key: perform the operation, store the key + result.
2. Retry with the same key: return the stored result, do not repeat the effect.
3. Same key, different payload: return `409 Conflict`.
4. Key expiry: document the retention window.

Test:

- Same key twice → one effect, same response.
- Same key, different body → conflict.
- Different key, same body → two effects.
- Concurrent requests with the same key → one effect.
- Key missing → documented behavior (reject or allow).

---

## 5. Pagination

### Offset pagination

```http
GET /api/listings?page=0&size=20
```

Response:

```json
{
  "items": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0
}
```

Test: first page, last page, beyond end, size 0/negative/huge, stable ordering, total consistency.

### Cursor pagination

```http
GET /api/listings?cursor=abc&limit=20
```

Better for large/real-time data. Test cursor validity, no duplicates/gaps, ordering stability.

**Rule:** cap `size` server-side. A client should not be able to request the entire table.

---

## 6. Filtering and sorting

```http
GET /api/listings?status=ACTIVE&sellerId=s1&sort=createdAt,desc
```

Test:

- Each filter alone and combined.
- Unknown filter value.
- Invalid sort field.
- SQL injection attempts in filter values (should be parameterized).
- Empty result.

---

## 7. Versioning

| Strategy | Example | Trade-off |
|---|---|---|
| URI version | `/api/v1/listings` | Simple, visible, duplicates routes |
| Header version | `Accept: application/vnd.api.v1+json` | Clean URL, harder to discover |
| Query param | `?version=1` | Simple, easy to forget |
| Media type | custom `Content-Type` | Standards-based, complex |

**Rule:** prefer additive, backward-compatible changes; version only when you must break.

---

## 8. Error model

A stable error body:

```json
{
  "timestamp": "2026-11-03T10:15:30Z",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/listings",
  "fieldErrors": {
    "title": "must not be blank",
    "price": "must be greater than 0"
  },
  "traceId": "abc-123"
}
```

Rules:

- `code` is stable and machine-readable.
- `message` is human-readable but does not leak internals.
- `fieldErrors` maps field → message.
- `traceId` links to logs/traces.
- Never return a stack trace to a client.

---

## 9. Validation

```java
public record CreateListingRequest(
        @NotBlank String title,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotBlank String sellerId
) {}
```

Controller:

```java
@PostMapping
ResponseEntity<ListingResponse> create(@Valid @RequestBody CreateListingRequest request) {
    ListingResponse created = service.create(request);
    return ResponseEntity.created(URI.create("/api/listings/" + created.id())).body(created);
}
```

Test:

- Missing required field → 400 + field error.
- Wrong type → 400.
- Boundary values (min, max, zero, negative).
- Valid request → 201 + `Location`.

---

## 10. API design checklist

- [ ] Resource names are nouns and consistent.
- [ ] Methods match semantics.
- [ ] Status codes are correct and documented.
- [ ] Creation returns `201` + `Location`.
- [ ] Writes that can be retried support idempotency.
- [ ] Pagination is capped and tested at boundaries.
- [ ] Filtering/sorting are validated.
- [ ] Error model is stable and machine-readable.
- [ ] Validation errors identify the field.
- [ ] Auth is tested for 401 vs 403.
- [ ] Versioning/deprecation policy exists.
- [ ] Rate limits and timeouts are defined.
