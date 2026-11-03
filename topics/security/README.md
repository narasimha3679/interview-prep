# Security Testing — Study Pack

Security testing for SDETs: the OWASP risks, auth models, API-specific attacks, and where automation
fits.

**Last verified:** November 2026.

---

## 60-second cram

1. **Security is a risk area, not a phase.** Auth, input handling, secrets, and dependencies are
   test targets.
2. **OWASP Top 10 is the vocabulary.** Broken access control, injection, auth failures, misconfig,
   vulnerable components, SSRF, etc.
3. **Authn vs authz:** authentication proves who you are; authorization decides what you can do.
   Test both, including 401 vs 403.
4. **Broken access control is #1.** Test horizontal (other user), vertical (other role), and tenant
   boundaries.
5. **APIs have their own top 10:** object-level auth, broken auth, excessive data exposure, rate
   limits, mass assignment, inventory management.
6. **Secrets never belong in code, logs, or test reports.** Scan for them.
7. **Automate the repeatable checks:** auth matrix, input validation, headers, dependency scan, ZAP
   baseline. Keep deep manual/pen-test work for experts.
8. **Security testing is not a substitute for a security review.** It is evidence, not a guarantee.

**The one line that shows depth:**

> "I test security at the boundaries where trust changes: authentication, object-level authorization,
> tenant isolation, input handling, and secrets. Most real API breaches are broken access control, not
> exotic crypto."

---

## File map

| File | What it gives you |
|---|---|
| `01_owasp_top10.md` | OWASP Top 10 and API Top 10 |
| `02_authn_authz.md` | Auth models and the access-control test matrix |
| `03_api_security.md` | API-specific risks and tests |
| `04_tools_zap.md` | ZAP, dependency scanning, secrets scanning |
| `05_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Authn** | Who you are | Login, token, MFA |
| **Authz** | What you can do | Roles, scopes, object ownership |
| **IDOR** | Insecure direct object reference | Change an ID, expect 403/404 |
| **Tenant isolation** | Data separation between customers | Cross-tenant access must fail |
| **Mass assignment** | Binding client fields to internal objects | Reject privileged fields |
| **SSRF** | Server-side request forgery | Block internal/metadata URLs |
| **Injection** | Untrusted input executed | Parameterize, validate |
| **Secrets** | Credentials/keys | Never commit/log |
| **SBOM** | Software bill of materials | Dependency visibility |
| **CSP** | Content security policy | XSS mitigation |

---

## Traps

1. **Testing only 401, not 403.** Authenticated-but-forbidden is the common bug.
2. **Testing only your own user.** Try another user's ID.
3. **Assuming the UI hides it.** Call the API directly.
4. **Ignoring tenant boundaries.** Cross-tenant leaks are severe.
5. **Logging tokens/PII.** A test report can leak secrets.
6. **Trusting client-side validation.** Bypass it.
7. **Scanning only once.** Dependencies change; scan continuously.
8. **Treating ZAP as complete.** It finds low-hanging fruit, not business-logic flaws.
9. **No rate-limit tests.** Abuse and cost risk.
10. **No negative tests for mass assignment.** A client can set `role: admin`.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, map the OWASP risks to an API you know.
- **30 minutes:** `02`, build the auth matrix.
- **30 minutes:** `03`, write API security tests.
- **20 minutes:** `04`, run a ZAP baseline in a lab.
- **20 minutes:** `05`, answer out loud.
