# 03 — API Security

## 1. The API attack surface

- Path/query/header/body parameters.
- Object IDs and nested resources.
- Auth tokens/scopes.
- HTTP methods and content types.
- GraphQL queries/mutations.
- File uploads.
- Webhooks/callbacks.
- Rate limits and quotas.
- Error responses.
- Old API versions.

---

## 2. Input validation

Test:

- Missing/empty/null.
- Wrong type.
- Boundary values.
- Oversized payload.
- Unicode/encoding.
- Injection strings.
- Unexpected fields (mass assignment).
- Array/object where scalar expected.
- Duplicate parameters.
- HTTP parameter pollution.

**Rule:** validate on the server; client validation is UX, not security.

---

## 3. Mass assignment

If the create/update endpoint binds the request body directly to an entity, a client may set fields
it should not.

```json
{
  "title": "Camera",
  "price": 10.00,
  "sellerId": "s1",
  "role": "admin",
  "status": "SOLD"
}
```

Test:

- Send privileged fields (`role`, `status`, `ownerId`, `price`).
- Expect them ignored or rejected, not applied.
- Use a DTO with an explicit allowlist.

---

## 4. Excessive data exposure

- Response includes internal IDs, emails, tokens, stack traces.
- GraphQL field-level leakage.
- Admin fields in public payloads.
- Debug headers.

Test: inspect the full response, not just the fields the UI uses. Assert the contract, not "extra
fields are fine."

---

## 5. Rate limiting and resource limits

Test:

- Burst above the limit → 429/error with retry guidance.
- Per user/tenant, not global only.
- Expensive endpoints (search, export, GraphQL) have stricter limits.
- Pagination caps.
- Request size limits.
- Timeouts.

**Why:** abuse, cost, and availability. A single client should not exhaust the service.

---

## 6. File upload

- Content type vs actual content.
- Size limit.
- Filename path traversal (`../../`).
- Double extension.
- Malware scanning.
- Image decompression bombs.
- Access control on download.
- Signed URLs with expiry.

---

## 7. Webhooks/callbacks

- Signature verification.
- Replay protection (timestamp/nonce).
- Idempotency.
- Allowlist of destinations.
- Timeout/retry behavior.

---

## 8. GraphQL-specific

- Field-level authorization.
- Depth/complexity limits.
- Introspection policy.
- Persisted queries/allowlist.
- Alias/batch abuse.
- N+1/resource exhaustion.
- Mutation idempotency.

See [`../graphql/`](../graphql/).

---

## 9. Error handling

- No stack traces/internal paths.
- No SQL/query details.
- No user enumeration (forgot-password, login).
- Consistent error codes.
- Correlation ID for support, not secrets.

---

## 10. Security headers

| Header | Purpose |
|---|---|
| `Strict-Transport-Security` | Force HTTPS |
| `Content-Security-Policy` | Restrict scripts/resources |
| `X-Content-Type-Options: nosniff` | Prevent MIME sniffing |
| `X-Frame-Options` / `frame-ancestors` | Clickjacking |
| `Referrer-Policy` | Limit referrer leakage |
| `Permissions-Policy` | Restrict browser features |

---

## 11. API security checklist

- [ ] Object/function/tenant authorization.
- [ ] Input validation and mass-assignment protection.
- [ ] No excessive data exposure.
- [ ] Rate limits and pagination caps.
- [ ] File upload validation.
- [ ] Webhook signature/replay protection.
- [ ] No secrets/PII in responses/logs.
- [ ] Security headers.
- [ ] Dependency scan.
- [ ] Old API versions inventoried/retired.
- [ ] Error responses do not leak internals.
