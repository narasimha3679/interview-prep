# 04 — Tools and Automation

## 1. OWASP ZAP

ZAP is a dynamic application security testing (DAST) tool.

### Baseline scan

```bash
docker run --rm -v "$(pwd):/zap/wrk" ghcr.io/zaproxy/zap-stable \
  zap-baseline.py -t https://app.example.test -r zap-report.html
```

Use for:

- Passive scan of headers, cookies, TLS, information disclosure.
- Low-hanging misconfigurations.
- CI baseline on a running environment.

### Full scan

```bash
zap-full-scan.py -t https://app.example.test -r zap-full-report.html
```

More intrusive; run against a dedicated environment, not production.

### API scan

```bash
zap-api-scan.py -t https://api.example.test/openapi.json -f openapi -r zap-api.html
```

Use the OpenAPI/GraphQL definition to drive requests.

**Limits:** DAST finds generic issues; it cannot understand business logic, authorization
relationships, or multi-step abuse. Combine with manual/pen-test review.

---

## 2. Dependency scanning

| Tool | Use |
|---|---|
| **OWASP Dependency-Check** | Known CVEs in Java/.NET/npm |
| **Snyk** | SCA + container/IaC |
| **Trivy** | Containers, filesystems, IaC, dependencies |
| **Dependabot** | Automated dependency updates |
| **npm audit / mvn dependency:tree** | Quick local check |

Policy:

- Fail on new critical/high with a known exploit.
- Track exceptions with an owner and expiry.
- Scan base images and lockfiles.
- Generate an SBOM.

---

## 3. Secrets scanning

- `gitleaks`, `trufflehog`, GitHub secret scanning.
- Scan commits, history, and CI logs.
- Rotate any leaked secret immediately.
- Pre-commit hooks + CI gate.

---

## 4. Static analysis

- SonarQube/SonarCloud.
- Semgrep.
- SpotBugs/PMD/Checkstyle (Java).
- ESLint security plugins (JS/TS).

Static analysis finds patterns; it does not replace runtime testing.

---

## 5. Container/IaC scanning

- Trivy/Clair for image CVEs.
- Checkov/tfsec for Terraform/IaC misconfig.
- Kubernetes benchmarks (kube-bench).
- Ensure non-root, read-only filesystem, no secrets in image.

---

## 6. CI integration

```text
PR
  -> secrets scan
  -> dependency scan
  -> static analysis
  -> unit/API security tests
Merge/nightly
  -> ZAP baseline against staging
  -> container/IaC scan
Pre-release
  -> ZAP API/full scan (dedicated env)
  -> manual/pen-test review for high-risk changes
```

Rules:

- Fail on new critical/high.
- Publish reports as artifacts.
- Do not scan production with intrusive tests.
- Keep an allowlist with owners/expiry for accepted risks.

---

## 7. What automation cannot do

- Business-logic abuse (e.g., refund twice, bypass workflow).
- Complex authorization relationships.
- Chained attacks.
- Social engineering.
- Design flaws.

Those need threat modeling, manual testing, and security review.

---

## 8. Tooling checklist

- [ ] Secrets scan in CI.
- [ ] Dependency scan in CI.
- [ ] Static analysis in CI.
- [ ] Container/IaC scan for deployments.
- [ ] ZAP baseline on staging/nightly.
- [ ] ZAP API scan against OpenAPI/GraphQL.
- [ ] Reports published.
- [ ] Critical/high findings block release.
- [ ] Exceptions have owner/expiry.
- [ ] Manual review for high-risk changes.
