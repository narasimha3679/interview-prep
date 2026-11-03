# 01 — OWASP Top 10

## Web application risks

| Risk | What it means | Test idea |
|---|---|---|
| **A01 Broken access control** | Users act outside their permissions | Change IDs/roles/tenants; expect 403/404 |
| **A02 Cryptographic failures** | Weak encryption/exposure | Check TLS, hashing, secrets, sensitive data |
| **A03 Injection** | SQL/NoSQL/OS/LDAP injection | Malicious input; parameterized queries |
| **A04 Insecure design** | Missing threat modeling/business controls | Abuse cases, rate limits, workflow bypass |
| **A05 Security misconfiguration** | Defaults, open buckets, verbose errors | Headers, CORS, debug endpoints, default creds |
| **A06 Vulnerable components** | Known CVEs in dependencies | SCA/SBOM scan |
| **A07 Identification/auth failures** | Weak login/session | Credential stuffing, fixation, weak reset |
| **A08 Software/data integrity** | Unsigned updates/deserialization | Supply chain, integrity checks |
| **A09 Logging/monitoring failures** | No detection | Assert security events are logged/alerts |
| **A10 SSRF** | Server fetches attacker URL | Block internal/metadata addresses |

## API-specific risks (OWASP API Top 10)

| Risk | What it means | Test idea |
|---|---|---|
| **API1 Broken object-level auth** | Access another user's object by ID | Swap IDs; expect 403/404 |
| **API2 Broken authentication** | Weak tokens/login | Expired/wrong-scope/forged tokens |
| **API3 Excessive data exposure** | Response includes too much | Check fields, PII, internal IDs |
| **API4 Lack of resources/rate limiting** | Abuse/DoS | Burst requests; expect 429 |
| **API5 Broken function-level auth** | Access admin functions | Role/endpoint matrix |
| **API6 Mass assignment** | Client sets internal fields | Send `role`, `price`, `status` unexpectedly |
| **API7 Security misconfiguration** | CORS, headers, errors | Check headers, methods, errors |
| **API8 Injection** | SQL/NoSQL/command | Malicious input |
| **API9 Improper inventory** | Old/undocumented APIs | Discover versions/endpoints |
| **API10 Unsafe consumption** | Trusting third-party APIs | Stub malicious/slow responses |

---

## 1. Broken access control

Test three dimensions:

- **Horizontal:** user A accesses user B's object.
- **Vertical:** regular user accesses admin function.
- **Tenant:** tenant A accesses tenant B's data.

For every object endpoint:

```text
GET /orders/{id}
- owner -> 200
- other user -> 403 or 404
- admin -> 200 (if policy allows)
- anonymous -> 401
```

Do not accept `200` with an empty/redacted body unless that is the documented policy.

---

## 2. Injection

- SQL: `' OR '1'='1`, time-based, union.
- NoSQL: `{ "$ne": null }`, operator injection.
- OS command: `; id`, `| whoami`.
- LDAP/XPath/template injection.
- GraphQL: malicious arguments, depth/cost abuse.

Test through every input: path, query, header, body, file name, JSON nested fields.

**Fix is parameterization/validation, not blocklists.**

---

## 3. Security misconfiguration

Check:

- Security headers: `Content-Security-Policy`, `X-Content-Type-Options`, `Strict-Transport-Security`,
  `X-Frame-Options`/`frame-ancestors`.
- CORS: allowed origins, credentials, methods.
- Error responses: no stack traces, no internal paths.
- Debug/actuator endpoints: not public.
- Default credentials: removed.
- Cloud storage: not public.
- TLS: valid, strong, no mixed content.

---

## 4. Vulnerable components

- Generate an SBOM.
- Scan dependencies (OWASP Dependency-Check, Snyk, Trivy, Dependabot).
- Fail on new critical/high with a known exploit.
- Track transitive dependencies and base images.
- Patch cadence and owner.

---

## 5. Logging and monitoring

Test that security-relevant events are logged:

- Login success/failure.
- Authorization failure.
- Token refresh/revocation.
- Admin action.
- Rate limit.
- Input validation failure.

Assert no secrets/PII in logs. Assert alerts exist for repeated failures.

---

## 6. SSRF

If the app fetches a user-supplied URL:

- Block `localhost`, `127.0.0.1`, `169.254.169.254` (cloud metadata), private ranges.
- Allowlist domains/schemes.
- Disable redirects to internal addresses.
- Test DNS rebinding/resolved IP.

---

## 7. Security testing checklist

- [ ] Authn: login, MFA, token expiry/refresh/revocation.
- [ ] Authz: horizontal, vertical, tenant.
- [ ] Input: SQL/NoSQL/command/template injection.
- [ ] Mass assignment: privileged fields rejected.
- [ ] Rate limiting and resource limits.
- [ ] Security headers/CORS/TLS.
- [ ] Error responses do not leak internals.
- [ ] Secrets not in code/logs/reports.
- [ ] Dependencies scanned.
- [ ] Security events logged/alerts.
- [ ] SSRF protections if URLs are fetched.
- [ ] File upload validation.
