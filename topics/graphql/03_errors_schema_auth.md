# 03 — Errors, Schema, and Authorization

## 1. The GraphQL response envelope

A GraphQL response has up to three top-level keys:

```json
{
  "data": { "listing": null },
  "errors": [
    {
      "message": "Listing not found",
      "path": ["listing"],
      "locations": [{ "line": 2, "column": 3 }],
      "extensions": { "code": "NOT_FOUND", "traceId": "abc-123" }
    }
  ],
  "extensions": { "tracing": { "duration": 42 } }
}
```

- **`data`** — the result, possibly partial or `null`.
- **`errors`** — one or more errors; may coexist with `data`.
- **`extensions`** — optional metadata (tracing, cost, cache).

**Rule:** assert `data` and `errors` separately. Do not infer success from HTTP 200.

---

## 2. Error categories

| Category | Example | Typical HTTP | Test |
|---|---|---|---|
| **Syntax** | malformed query | 400/200 | `errors[].message`, no data |
| **Validation** | unknown field, wrong type | 400/200 | `errors[]`, no resolver executed |
| **Execution** | resolver throws | 200 | partial data + `errors[]` |
| **Authorization** | field forbidden | 200/403 | `errors[]` or null field |
| **Business** | insufficient funds | 200 | structured error code |
| **Rate limit** | complexity exceeded | 429/200 | error code + retry-after |

### Assertion shape

```java
given()
    .contentType(ContentType.JSON)
    .body(request)
.when()
    .post("/graphql")
.then()
    .statusCode(200)
    .body("errors", not(empty()))
    .body("errors[0].extensions.code", equalTo("NOT_FOUND"))
    .body("data.listing", nullValue());
```

Do not assert only `statusCode(200)`; that is the most common GraphQL testing mistake.

---

## 3. Nullability semantics

```graphql
type Listing {
  id: ID!
  title: String!
  seller: Seller
}
```

- `String!` means non-null. If the resolver returns null, the error propagates to the nearest
  nullable parent.
- `Seller` is nullable. A missing seller can be `null` without nulling the listing.
- If `seller.name` is non-null and fails, `seller` may become `null`; if `seller` is non-null, the
  error may bubble to `listing`.

Test cases:

1. Missing optional field → `null`, no error.
2. Missing required field → error, parent nulled.
3. Null in a list item → item nulled or list nulled depending on nullability.
4. Error path includes the field path.

**Interview line:** "GraphQL nullability is a contract. I test what happens when a non-null field
fails because the error bubbles to the nearest nullable parent, and the client may get partial data
with an errors array."

---

## 4. Schema as contract

### Breaking changes

- Removing a field.
- Renaming a field (remove + add).
- Changing a field type.
- Changing an argument type.
- Making an optional argument required.
- Removing an enum value.
- Changing a nullable field to non-null.
- Removing an interface/union member.
- Changing a mutation input in a non-additive way.

### Safe changes

- Adding a new field.
- Adding an optional argument.
- Adding an enum value (consumers must handle unknown values).
- Deprecating a field with `@deprecated`.
- Adding a new type.

### Schema validation in CI

1. Fetch the schema from the target environment (`introspection` or a checked-in SDL).
2. Diff against the previous version.
3. Fail on breaking changes.
4. Publish the schema diff as an artifact.
5. Version the schema with the release.

Tools: GraphQL Inspector, Apollo Studio schema checks, `graphql-codegen` for client types.

---

## 5. Authorization

GraphQL authorization can happen at multiple levels:

| Level | Example | Test |
|---|---|---|
| **Endpoint** | token required for `/graphql` | 401 without token |
| **Operation** | only admins can run `deleteListing` | 403/error |
| **Field** | seller email visible only to owner | field error/null |
| **Node** | user can read own order, not another's | 403/not found |
| **Tenant** | user cannot query another tenant's data | no data leak |
| **Mutation** | only seller can edit listing | 403/error |

Test matrix:

- Anonymous vs authenticated.
- Owner vs non-owner.
- Admin vs regular user.
- Expired/invalid token.
- Token with wrong scope.
- Field-level redaction.
- Batch query attempts to bypass field checks.

**Trap:** a query can be allowed but a nested field can leak data. Test the full query shape, not
just the top-level operation.

---

## 6. Query cost and abuse

GraphQL lets clients ask for expensive shapes. Defenses:

- **Depth limit** — reject queries nested beyond N levels.
- **Complexity/cost limit** — assign a cost per field and reject above a budget.
- **Pagination limits** — cap `first`/`last`.
- **Persisted queries** — only allow pre-approved query hashes.
- **Rate limiting** — per user/IP/operation.
- **Timeouts** — per resolver and per request.
- **Alias abuse** — many aliases in one query can multiply work.

Test:

- Deep query is rejected with a clear error.
- Expensive query is rejected before execution.
- `first: 100000` is clamped or rejected.
- Persisted query allowlist rejects unknown hashes.
- Rate limit returns a retry hint.

---

## 7. Introspection and production

Introspection is useful for tooling but can expose the schema. Many teams disable it in production.

Test:

- Introspection enabled in dev/test.
- Disabled or restricted in production (if that is the policy).
- Schema checks use a controlled endpoint or checked-in SDL, not production introspection.

---

## 8. Error contract checklist

- [ ] HTTP status is not the only assertion.
- [ ] `errors[]` is asserted when expected.
- [ ] Error `code` is stable and documented.
- [ ] Error `message` is useful but not leaking internals.
- [ ] `path` points to the failing field.
- [ ] `extensions.traceId` is present for debugging.
- [ ] Partial `data` is asserted, not ignored.
- [ ] Validation errors do not execute resolvers.
- [ ] Auth errors do not leak whether a resource exists.
- [ ] Rate-limit errors include retry guidance.
