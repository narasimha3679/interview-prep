# 02 — Prompt Patterns

## 1. The anatomy of a useful prompt

```text
Role:        You are an SDET working in <framework/language>.
Context:     Here is the API spec / DOM / conventions / existing example.
Task:        Generate <specific artifact>.
Constraints: Follow <patterns>, do not invent endpoints/fields, use <locator strategy>.
Output:      <format: table/code/tests>.
Verification: List assumptions and what I should check.
```

The **verification** line is the most valuable: it surfaces the model's assumptions.

---

## 2. Test-idea prompt

```text
You are an SDET. Given this API contract and business rules:
<contract>
<rules>

Produce a risk-ranked table of test cases:
- risk (P0-P3)
- test level (unit/component/contract/API/UI/E2E)
- scenario
- oracle
- data needed

Include boundary, negative, auth, idempotency, and partial-failure cases.
Do not write code. Call out assumptions.
```

---

## 3. API test prompt

```text
You are an SDET using RestAssured + TestNG.
Here is the OpenAPI operation and an existing test for style:
<spec>
<example>

Write tests for:
- 200 happy path
- 400 validation
- 401/403 auth
- 404 not found
- idempotency/duplicate
Use the existing BaseClient and ConfigManager. Do not invent fields.
List assumptions and any spec gaps.
```

---

## 4. Playwright page-object prompt

```text
You are an SDET using Playwright + TypeScript.
Here is the HTML snippet and our page-object conventions:
<html>
<conventions>

Create a typed page object with:
- role/test-id locators first
- user-level actions
- no fixed sleeps
- no assertions about business outcomes
Explain why each locator is stable.
```

---

## 5. Locator prompt

```text
Given this HTML, propose three locator strategies in priority order:
1. role/accessible name
2. test-id
3. CSS
For each, state uniqueness/stability risks. Do not use XPath unless nothing else works.
```

---

## 6. Test-data prompt

```text
Generate 10 synthetic test users for a marketplace checkout test.
Constraints:
- .test email domain
- unique emails
- valid US addresses
- no real people
- include one invalid postal code and one expired card token
Return JSON and note which fields are intentionally invalid.
```

---

## 7. Failure-triage prompt

```text
You are debugging a CI failure. Here is the evidence:
<assertion>
<request/response>
<logs>
<trace summary>
<recent changes>

List the top 3 hypotheses, evidence for/against each, and the next diagnostic step.
Do not propose a fix until the cause is supported by evidence.
```

---

## 8. Refactor prompt

```text
Refactor this test to:
- extract a page object
- use role/test-id locators
- remove fixed sleeps
- keep behavior identical
Show the diff and list the assumptions. Do not change assertions.
```

---

## 9. Review prompt

```text
Review this test for:
- missing negative/boundary cases
- flakiness risks
- shared state
- weak oracle
- wrong test level
Return a prioritized list. Do not rewrite the test yet.
```

---

## 10. Prompt anti-patterns

| Anti-pattern | Why it fails |
|---|---|
| "Write tests for this app" | No context, no risk, generic output |
| Pasting a whole repo | Context overload; model loses focus |
| No constraints | Invents endpoints/fields/conventions |
| "Make it pass" | Encourages hiding failures |
| Pasting secrets/logs with PII | Data leak |
| No verification step | Assumptions stay hidden |
| One-shot, no iteration | Quality plateaus |
| Trusting generated locators | Plausible but wrong |

---

## 11. Prompt checklist

- [ ] Role/framework stated.
- [ ] Relevant context included (not the whole repo).
- [ ] Task is specific.
- [ ] Constraints/patterns stated.
- [ ] Output format defined.
- [ ] Assumptions requested.
- [ ] No secrets/PII.
- [ ] Human review/execution planned.
