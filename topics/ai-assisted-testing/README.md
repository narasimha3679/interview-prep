# AI-Assisted Testing — Study Pack

Using LLMs to help generate, review, triage, and maintain tests — without outsourcing judgment.

> This is different from [`../ai-testing/`](../ai-testing/), which is about **testing AI
> systems**. This pack is about **using AI as a testing tool**.

**Last verified:** November 2026.

---

## 60-second cram

1. **AI is an assistant, not an oracle.** It can draft tests, explain failures, and suggest locators;
   it cannot decide what risk matters.
2. **Generate, then verify.** Every generated test must be reviewed, run, and checked against the
   real behavior.
3. **Context is everything.** Give the model the API spec, page structure, existing conventions, and
   constraints; otherwise it invents.
4. **The model does not know your domain.** Business rules, edge cases, and compliance constraints
   come from you.
5. **Non-determinism is a feature and a bug.** Same prompt can produce different tests; pin prompts/
   models for reproducibility.
6. **Never send secrets/PII to a model.** Redact, use approved tools, and follow data policy.
7. **Prompt injection is real.** Treat untrusted content (web pages, logs, issues) as data, not
   instructions.
8. **Measure the assistant.** Track accepted suggestions, escaped defects, and review time — not
   "tests generated."

**The one line that shows depth:**

> "I use AI to remove the blank-page and boilerplate tax, then apply the same review and execution
> standards I would to a human-written test. The judgment about what to test stays with me."

---

## File map

| File | What it gives you |
|---|---|
| `01_llm_for_testing.md` | Where AI helps and where it doesn't |
| `02_prompt_patterns.md` | Prompts for tests, locators, data, triage |
| `03_validation_risks.md` | Hallucination, privacy, injection, over-reliance |
| `04_qa.md` | Questions with model answers |

---

## Where AI helps

| Task | Good use | Human check |
|---|---|---|
| Test ideas | Brainstorm boundaries/negative cases | Risk/domain relevance |
| Boilerplate | Page objects, API clients, fixtures | Conventions/typing |
| Locators | Suggest role/test-id selectors | Verify against DOM |
| Test data | Generate synthetic values | Validity/PII |
| Failure triage | Summarize logs/traces | Root cause |
| Docs | Draft README/runbook | Accuracy |
| Refactoring | Suggest extraction/naming | Behavior preservation |
| Review | Spot missing cases | Judgment |

## Where it struggles

- Business rules and domain edge cases.
- “What should we test?” prioritization.
- Flaky-test root cause without evidence.
- Complex stateful workflows.
- Security/authorization reasoning.
- Anything requiring real user empathy.
- Code that must compile against your exact versions.

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **LLM** | Large language model | Probabilistic text generation |
| **Prompt** | Instruction/context | Quality depends on context |
| **Hallucination** | Confident but wrong output | Always verify |
| **Context window** | Input size limit | Provide focused, relevant context |
| **RAG** | Retrieval-augmented generation | Ground the model in your docs |
| **Prompt injection** | Untrusted text hijacks instructions | Treat content as data |
| **Eval** | Systematic quality check | Measure, don't assume |
| **Human-in-the-loop** | Review gate | Required for high-risk output |

---

## Traps

1. **Pasting secrets/PII into a public model.**
2. **Accepting generated tests without running them.**
3. **Trusting generated locators without checking the DOM.**
4. **Letting the model invent business rules.**
5. **Using AI to "fix" flaky tests by adding waits.**
6. **No review standard for AI-generated code.**
7. **Prompt injection from logs/issues/web content.**
8. **Measuring volume instead of quality.**
9. **Not pinning model/prompt versions.**
10. **Assuming the model knows your framework conventions.**

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, pick one task and define the human check.
- **30 minutes:** `02`, adapt a prompt to your framework.
- **30 minutes:** `03`, list the risks for your data/tools.
- **20 minutes:** `04`, answer out loud.
