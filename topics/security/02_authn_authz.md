# 02 — Authentication and Authorization

## 1. Authn vs authz

| | Authentication | Authorization |
|---|---|---|
| Question | Who are you? | What may you do? |
| Failure | 401 Unauthorized | 403 Forbidden |
| Examples | password, MFA, token | role, scope, ownership |
| Test | valid/invalid/expired credentials | access matrix |

**401 vs 403:** 401 means "authenticate"; 403 means "authenticated but not allowed." Some APIs
return 404 instead of 403 to avoid revealing existence. That must be a documented policy.

---

## 2. Session vs token

### Session cookie

- Server stores session; cookie carries an ID.
- Test: `HttpOnly`, `Secure`, `SameSite`, expiry, logout invalidation, fixation.

### JWT

- Token contains claims; server validates signature/expiry.
- Test: signature, `exp`, `nbf`, `aud`, `iss`, `scope`, revocation.
- Do not store sensitive data in the payload; it is readable.

### OAuth 2.0 / OIDC

- Authorization code + PKCE for user login.
- Client credentials for service-to-service.
- Test: state/nonce, redirect URI allowlist, PKCE, token audience, refresh rotation.

---

## 3. Access-control matrix

Build a matrix for every sensitive endpoint:

| Endpoint | Anonymous | User A | User B | Admin | Tenant B |
|---|---|---|---|---|---|
| `GET /orders/{id}` | 401 | 200 (own) | 403/404 | 200 | 403/404 |
| `POST /orders` | 401 | 201 | 201 | 201 | — |
| `DELETE /orders/{id}` | 401 | 204 (own) | 403/404 | 204 | 403/404 |
| `GET /admin/users` | 401 | 403 | 403 | 200 | — |

Test each cell. Automate the matrix as a data-driven test.

---

## 4. Object-level authorization (IDOR)

```http
GET /api/orders/1001
Authorization: Bearer <user-a-token>
```

If user A can read order 1001, try:

- Another user's order ID.
- Sequential IDs.
- UUIDs from another tenant.
- Nested resources (`/users/{id}/orders`).
- GraphQL node IDs.

Expected: 403/404, not 200 with another user's data.

---

## 5. Function-level authorization

- Admin endpoints.
- Internal endpoints.
- Bulk operations.
- Export/report endpoints.
- GraphQL mutations.
- HTTP method overrides.

Test with a low-privilege token. Do not rely on the UI hiding the button.

---

## 6. Tenant isolation

Multi-tenant systems must scope every query by tenant.

Test:

- Tenant A token + tenant B object ID → deny.
- Tenant A token + tenant B list filter → empty/deny.
- Cross-tenant search/index/cache.
- Background jobs/events do not cross tenants.
- Admin cross-tenant access is explicit and audited.

---

## 7. Token tests

| Test | Expected |
|---|---|
| No token | 401 |
| Malformed token | 401 |
| Expired token | 401 |
| Wrong signature | 401 |
| Wrong audience/issuer | 401 |
| Wrong scope | 403 |
| Revoked token | 401 |
| Token from another tenant | 403 |
| Token in query string | Reject/log policy |

---

## 8. Password and account flows

- Password policy and breach checks.
- Rate limit/lockout on repeated failures.
- Password reset token: single-use, expiry, no user enumeration.
- Email/phone verification.
- MFA enrollment/recovery.
- Session invalidation on password change/logout.
- Credential stuffing protection.

---

## 9. Auth checklist

- [ ] 401 vs 403 semantics are correct.
- [ ] Object-level access matrix tested.
- [ ] Function-level access matrix tested.
- [ ] Tenant isolation tested.
- [ ] Token expiry/signature/audience/scope tested.
- [ ] Revocation/logout tested.
- [ ] Password reset is single-use/expiring.
- [ ] Rate limit/lockout exists.
- [ ] No user enumeration.
- [ ] Auth events logged without secrets.
