# 01 — Using LLMs in Testing

## 1. The workflow

```text
1. Define the task and the risk.
2. Provide context: spec, conventions, examples, constraints.
3. Generate a draft.
4. Review for correctness/domain fit.
5. Run it.
6. Fix/iterate.
7. Record what was accepted/rejected.
```

Never skip step 5.

---

## 2. Test ideas

**Good prompt:**

> "Here is the API contract and the business rules. List boundary, negative, auth, and partial-failure
> cases. For each, state the risk and the cheapest test level. Do not write code yet."

**Why:** it separates ideation from implementation and asks for risk, not volume.

**Human check:** remove irrelevant cases, add domain rules the model doesn't know.

---

## 3. Boilerplate code

**Good use:**

- Page object skeleton from a DOM snippet.
- API client from an OpenAPI operation.
- Data factory from a schema.
- TestNG/JUnit parameterized test skeleton.

**Human check:**

- Compiles against your versions.
- Follows your naming/layering.
- No assertions in the wrong layer.
- No invented endpoints/fields.

---

## 4. Locators

**Good prompt:**

> "Given this HTML snippet, propose Playwright locators using role/test-id first. Explain why each is
> stable. Do not use XPath unless necessary."

**Human check:** run against the real page; verify uniqueness and actionability.

**Trap:** the model may invent attributes that look plausible. Always inspect the DOM.

---

## 5. Test data

**Good use:**

- Synthetic names/emails/addresses.
- Boundary values.
- Localized formats.
- Large datasets for load tests.

**Human check:** validity, uniqueness, PII policy, domain constraints.

**Never** send real customer data to a model.

---

## 6. Failure triage

**Good prompt:**

> "Here is the failing assertion, the request/response, the relevant logs, and the trace summary.
> List the top three hypotheses, the evidence for each, and the next diagnostic step. Do not guess
> without evidence."

**Human check:** verify hypotheses against logs/traces; the model may pattern-match incorrectly.

---

## 7. Refactoring and review

**Good use:**

- Extract a page object/API client.
- Rename for clarity.
- Suggest missing negative cases.
- Summarize a large diff.

**Human check:** behavior preservation, tests still pass, no hidden breaking changes.

---

## 8. Documentation

**Good use:**

- Draft README/quickstart.
- Explain a complex fixture.
- Summarize a test report.

**Human check:** accuracy, runnable commands, version correctness.

---

## 9. What not to delegate

- Risk ranking and test strategy.
- Business rules and compliance.
- Security/authorization decisions.
- Root cause without evidence.
- Final approval of high-risk changes.
- Anything requiring real user empathy.

---

## 10. Measuring usefulness

| Metric | What it tells you |
|---|---|
| Accepted suggestions | Usefulness |
| Rejected suggestions | Noise/context gap |
| Review time | Hidden cost |
| Escaped defects | Quality |
| Test runtime/flakiness | Maintainability |
| Developer satisfaction | Adoption |

Do not measure "tests generated."

---

## 11. Checklist

- [ ] Task and risk defined.
- [ ] Context provided (spec/conventions/examples).
- [ ] Output reviewed for domain correctness.
- [ ] Code compiled/run.
- [ ] No secrets/PII sent.
- [ ] Prompt/model version recorded.
- [ ] Accepted/rejected tracked.
- [ ] Human owns the final decision.
