# 03 — Validation and Risks

## 1. Hallucination

The model produces plausible but wrong content:

- Invented endpoints/fields.
- Wrong status codes.
- Non-existent libraries/APIs.
- Locators that don't exist.
- Business rules that sound right but aren't.

**Mitigation:** provide the spec, compile/run, verify against the real system, ask for assumptions.

---

## 2. Non-determinism

Same prompt can produce different outputs.

**Mitigation:**

- Pin model/version.
- Keep prompts in version control.
- Use deterministic settings where available.
- Treat generated tests as drafts, not source of truth.

---

## 3. Privacy and secrets

**Never send:**

- Customer PII.
- Credentials/tokens.
- Internal hostnames/keys.
- Unredacted production logs.

**Mitigation:**

- Use approved enterprise tools.
- Redact/synthesize data.
- Follow data residency/retention policy.
- Review the provider's training/retention terms.

---

## 4. Prompt injection

Untrusted content can contain instructions:

- Web pages ("ignore previous instructions").
- Logs/issues/PR comments.
- API responses.
- Documents.

**Mitigation:**

- Treat content as data, not instructions.
- Delimit/quote untrusted content.
- Use tools with permission boundaries.
- Never let a model execute actions without human approval.

---

## 5. Over-reliance

Risks:

- Tests look comprehensive but cover the same happy path.
- Assertions are weak ("no exception").
- Domain edge cases missing.
- Review time grows; quality doesn't.

**Mitigation:**

- Risk-based review.
- Mutation testing/coverage of critical logic.
- Escaped-defect feedback.
- Human ownership of strategy.

---

## 6. Bias and fairness

Generated data/tests can encode stereotypes or miss locales.

**Mitigation:**

- Specify locale/diversity requirements.
- Review names/addresses/currency.
- Include accessibility/localization cases.

---

## 7. Security of generated code

Generated code may:

- Log secrets.
- Use insecure defaults.
- Add dependencies with vulnerabilities.
- Disable TLS/verification.
- Use `eval`/unsafe deserialization.

**Mitigation:**

- Review like any PR.
- Dependency/secret scanning.
- No blind copy-paste.

---

## 8. Legal/IP

- Check the tool's terms for code ownership/training.
- Do not paste proprietary code into unapproved tools.
- Keep provenance for generated code if required.

---

## 9. Validation checklist

- [ ] Output compiles/runs.
- [ ] Fields/endpoints verified against the spec.
- [ ] Locators verified against the DOM.
- [ ] Assertions are meaningful.
- [ ] Negative/boundary cases included.
- [ ] No secrets/PII.
- [ ] No prompt injection from untrusted content.
- [ ] Dependencies scanned.
- [ ] Human reviewed/approved.
- [ ] Model/prompt version recorded.

---

## 10. The human gate

| Risk | Human review |
|---|---|
| Low (docs, naming) | Light |
| Medium (test code, data) | Required |
| High (auth, payments, security) | Required + domain expert |
| Critical (release gate) | Human decision, never automated by an LLM alone |

**Rule:** the model can draft; the human owns the decision.
