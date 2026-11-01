# 06 — TD Banking Lens

Use this as **context, not insider knowledge**. Everything below is public. In the interview say
"I read that…" or "from what TD has published…", never "I know your architecture".

The goal is to show three things:
1. You understand why AI testing is a **control** in a bank, not a nice-to-have.
2. You can connect generic AI testing to **TD's public AI programme**.
3. You ask smart questions instead of overclaiming.

---

## 1. Why TD is a strong fit for this prep

TD is not experimenting with a toy chatbot. It has publicly described:

- **RAG-based virtual assistants** for contact-centre and branch colleagues, grounded in TD policy
  with **citations** so staff can verify answers.
- A **client-facing, unauthenticated generative AI chatbot** for TD Insurance, in **English and
  French**, with thumbs-up/down feedback.
- An **agentic AI** workflow for real-estate secured lending that prepares a summary memo for a
  human underwriter.
- A **Trustworthy AI** team, independent model validators, model-risk assessments and privacy
  impact assessments.

That mix — RAG, chat, agents, regulation, EN/FR, human oversight — is exactly the test surface this
pack covers. You don't need to know their code; you need to know how to reason about it.

### Your 60-second TD pitch

Adapt this to your real experience; do not claim work you haven't done.

> "I'm a QE/SDET with a strong automation background. What I've been building is the evidence layer
> for AI systems: a versioned golden set, retrieval checks, grounding and citation metrics, safety
> tests for prompt injection and PII, and CI gates. I read that TD's assistants use retrieval
> grounding and citations, with human oversight for colleague and underwriter workflows — that's
> exactly the kind of system I want to test. I won't build the model. I'll make sure we can prove
> it's accurate, grounded, fair, private and reliable enough to ship, and that we can detect it
> when it drifts."

---

## 2. What TD has said publicly

| Initiative | What it is (public) | Testing implication |
|---|---|---|
| **Responsible AI Principles** (Jul 2026) | Seven enterprise commitments across fairness, transparency/explainability, privacy/data use, quality/accountability, reliability, security and applicable law | Each principle needs a testable control and evidence, not a policy PDF |
| **Trustworthy AI team / Layer 6** | TD's AI research arm and the team that evaluates models; independent model validators; model-risk and privacy assessments | Expect evidence, documentation, traceability and independent review |
| **Contact-centre GenAI assistant** | RAG over TD policies and procedures; answers with citations; used by 1,000+ reps; reduced hold times | Retrieval quality, faithfulness, citation accuracy, human verification |
| **Branch Banking Virtual Assistant** | GenAI assistant for 19,000+ branch colleagues; high adoption; fewer outgoing support calls | Colleague-facing risk: correct policy, citations, escalation, EN/FR |
| **TD Insurance Virtual Assistant** (May 2026) | First client-facing, unauthenticated GenAI chatbot among the Big Five; home/auto/small business insurance; EN/FR; no login/PII; thumbs feedback | Privacy, disclaimers, advice boundary, accessibility, safe refusal, FR parity |
| **TD AI Prism** | A predictive foundation model using financial behaviour to predict future needs | Different test lens: fairness, explainability, drift, model risk — not just RAG |
| **Agentic AI for RESL** (May 2026) | Classifies documents, extracts income, validates policy, checks consent, finds discrepancies, writes a memo for underwriters; 15 hours → under 3 minutes | Tool use, calculations, permissions, consent, human approval, audit trail |
| **TD Securities / TD Wealth assistants** | Internal assistants for research and product/policy information | Role-based access, citations, confidentiality, high-risk advice boundary |

**The nuance that makes you sound credible:**
> "Not all AI is an LLM. TD AI Prism is predictive, so I'd test fairness, explainability and drift
> differently from a RAG assistant. The common thread is evidence and monitoring, but the failure
> modes are different."

### Predictive AI vs generative AI — don't test them the same way

| Dimension | Predictive ML (e.g., TD AI Prism) | Generative AI / RAG |
|---|---|---|
| **Output** | Label, score, probability | Natural-language answer + citation |
| **Ground truth** | Labelled outcomes | Reference answer + expected sources; rubric |
| **Core metrics** | Precision/recall/F1/AUC, calibration, lift | Faithfulness, context recall/precision, answer relevancy, citation accuracy |
| **Fairness** | Disparate impact across protected groups; bias in features and labels | Bias probes across demographics/locales; equal quality EN/FR |
| **Explainability** | Feature importance/SHAP, reason codes | Citations, retrieved sources, trace |
| **Main risks** | Data leakage, overfitting, label bias, drift | Hallucination, retrieval failure, prompt injection, unsafe advice |
| **Monitoring** | Data drift, prediction drift, performance decay | Query/corpus drift, judge scores, refusal/escalation, model version |
| **Change trigger** | Retrain or new features | Prompt, model, index or guardrail change |

**The line:**
> "Predictive AI asks 'is the prediction accurate and fair?'. Generative AI also asks 'is the answer
> supported by the evidence and safe to act on?'. Same governance, different oracle."

---

## 3. What to emphasize in the interview

Given TD's public context, these themes will land:

1. **Grounding and citations.** A RAG answer without a verifiable source is not acceptable for
   policy guidance.
2. **Human-in-the-loop.** Colleague assistants are designed for verification; agentic workflows
   produce a memo for an underwriter. Test the review/override path, not just the output.
3. **EN/FR parity.** TD has publicly emphasized equally complete and accurate answers in English
   and French. That is a test dimension, not a translation afterthought.
4. **Risk tiering.** A client-facing unauthenticated insurance bot and an internal policy assistant
   do not get the same thresholds.
5. **Auditability.** Model version, prompt version, retrieved sources, answer and reviewer action
   must be reconstructable.
6. **Privacy.** Unauthenticated chat must not collect or reveal account data; logs must be redacted;
   vendor data flows must be approved.
7. **Advice boundary.** Information vs personalized financial advice, with disclaimers and
   escalation.
8. **Production monitoring.** Thumbs and sampled traces are signals; they are not a substitute for
   evaluation.
9. **Agentic controls.** Constrained tools, permissions, step limits, human approval and
   idempotency.

---

## 4. TD's Responsible AI principles mapped to tests

This table is the bridge between "responsible AI" (which can sound abstract) and your job.

| Principle | What it means in practice | Test evidence |
|---|---|---|
| **Fairness** | Equally complete and accurate answers across languages, demographics and customer groups; no discriminatory guidance | Bias probes with names/pronouns/locations; EN/FR quality comparison; refusal-rate comparison; fair-lending/insurance review |
| **Transparency & explainability** | Users and reviewers can see why an answer was given | Citations that support the claim; source version; model/prompt version in the trace; plain-language explanation |
| **Privacy & data use** | Only the data needed; no leakage; consent and retention respected | PII input/output/log tests; role-based retrieval; cross-user/tenant negatives; retention/deletion checks; vendor data-flow review |
| **Quality & accountability** | The system is accurate enough for its risk tier, with an owner | Golden-set thresholds; high-risk 100% gate; human sign-off; incident process; dataset ownership |
| **Reliability** | The service behaves under load, failure and change | p95 latency; fallback on provider timeout; canary; drift alerts; rollback |
| **Security** | The system resists injection, exfiltration and tool abuse | Prompt injection (direct/indirect); system-prompt canary; OWASP LLM Top 10; tool permissions; rate limits |
| **Compliance** | Applicable laws and internal policy are followed | Disclaimers; advice boundary; audit log; records retention; EN/FR; accessibility (WCAG/AODA) |

**The line to use:**
> "I'd turn each principle into a control with an owner, a test, a threshold and a piece of evidence.
> Otherwise 'responsible AI' is a statement of intent, not a release criterion."

---

## 5. How I'd test a TD virtual assistant

A practical 8-step plan. Use this when they ask "how would you approach it?"

### Step 1 — Clarify the use case and risk tier
- Who uses it: customer, contact-centre rep, branch colleague, underwriter?
- What decisions does it influence: information only, or money/eligibility/coverage?
- What is explicitly out of scope?
- What does "good" mean to the product owner, risk and compliance?

### Step 2 — Inventory the knowledge
- Which documents are authoritative, who owns them, when do they expire?
- What is the effective-date logic?
- Which content is restricted by role, locale or product?
- What happens when a document is updated or retracted?

### Step 3 — Build the golden set
- Real questions from reps/advisors/customers, not invented happy paths.
- Expected answer + expected source + locale + role + risk tier.
- Answerable, unanswerable, ambiguous, multi-turn, adversarial, high-risk.
- EN/FR from day one.
- Review labels with a subject-matter expert.

### Step 4 — Test retrieval
- Expected source in top-k.
- Current version retrieved; superseded version excluded.
- Permissions and locale filters.
- Tables, footnotes and PDF parsing.
- Target: retrieval is deterministic and fast; most defects should be caught here.

### Step 5 — Test generation and grounding
- Faithfulness: every claim supported by retrieved context.
- Citation accuracy: the cited passage supports the claim.
- Correctness against the reference answer.
- Refusal correctness for unanswerable questions.
- Advice boundary and disclaimer.

### Step 6 — Test the conversation and UI
- Multi-turn context, correction, topic switch, repetition.
- Handoff to a human with context.
- Streaming, error/retry, session timeout.
- Accessibility: keyboard, screen reader, live region, contrast.
- Thumbs feedback and transcript.

### Step 7 — Red team
- Direct and indirect prompt injection.
- System-prompt extraction (canary).
- PII extraction and cross-user leakage.
- Bias probes.
- Harmful financial advice.
- Fraud/social engineering.
- Tool abuse for agentic workflows.

### Step 8 — Non-functional + operations + release gate
- Latency, throughput, cost per conversation.
- Provider timeout, fallback, rate limits.
- Trace completeness and PII redaction.
- Canary, rollback, kill switch.
- Gate: zero P0 safety failures, 100% high-risk, no regression, human sign-off.

---

## 6. High-risk banking scenarios

These are the ones to name when they ask "what keeps you up at night?"

| Scenario | Why it matters | Test focus |
|---|---|---|
| **Wrong fee, rate, eligibility or coverage** | Direct customer harm and regulatory exposure | Exact match to current source; effective date; 100% high-risk gate |
| **Stale policy cited** | Looks authoritative but is out of date | Freshness checks; superseded-version exclusion; index alerts |
| **Personalized financial advice** | Unlicensed/unsuitable advice | Advice boundary; disclaimer; advisor route; human review |
| **Account/card data in unauthenticated chat** | Privacy breach | Auth boundary; PII tests; cross-user negatives |
| **Fraud/social engineering** | Customer loss | Refusal; escalation to fraud; no bypass instructions |
| **Bias in lending/insurance guidance** | Fair-lending/insurance risk | Bias probes; human/legal review; consistency across groups |
| **Missing or poor French** | Legal/linguistic obligation; unequal service | EN/FR parity; French sources; terminology review |
| **Unsupported claim with a citation** | False confidence | Citation accuracy; claim-level grounding |
| **Agentic action without approval** | Unauthorized side effect | Tool permissions; human approval; idempotency; audit |
| **PII in logs/feedback** | Privacy incident | Redaction; retention; access control |
| **Hallucinated phone number/link** | Customer sent to the wrong place | Exact entity tests; link checks; source version |
| **Vendor model changes silently** | Behaviour drift | Pin version; canary; regression; provider monitoring |

---

## 7. Smart questions to ask TD

Pick 5–8. These are tailored to their public context.

1. **Which assistant would I own first — colleague-facing (contact centre/branch), client-facing
   (TDI), or an agentic workflow?**
2. **How is the ground truth maintained across policy owners, and who signs off on the golden set?**
3. **How do you measure citation accuracy today — presence, or claim-level support?**
4. **What does the model-risk / independent validation evidence look like for a RAG assistant?**
5. **How do you test the agentic memo workflow's calculations, consent checks and discrepancy
   handling?**
6. **What is the release gate for a prompt, model or index change?**
7. **How do you monitor quality after release — thumbs, sampled traces, drift alerts?**
8. **How do you handle EN/FR parity in the evaluation set and the source corpus?**
9. **Where is the boundary between information and advice in the bot's design, and how is it
   tested?**
10. **How do QE, Trustworthy AI, model risk, privacy and the product team divide responsibilities?**
11. **What's the biggest quality incident you've had with an AI feature, and what changed?**
12. **What would success look like for this role in six months?**

**Closing question:**
> "Where is the evidence weakest today — retrieval, grounding, safety or production monitoring?
> That's where I'd start."

---

## 8. What not to say

| Don't say | Say instead |
|---|---|
| "RAG eliminates hallucinations." | "RAG reduces hallucination and gives me a trace, but the model can still ignore or misread the context." |
| "I'd compare the answer string." | "I'd use exact matching only for constrained facts; otherwise expected sources plus rubric/judge." |
| "AI is a black box, so we can't test it." | "I test the contract and behaviour at each layer, and I monitor the parts I can't see." |
| "I'd just use the vendor's benchmark." | "I'd use their evidence as an input, but my release gate is my own golden set and safety suite." |
| "I'd test with production data." | "Synthetic or masked data; production only through approved, redacted sampling." |
| "I've built LLMs." (if you haven't) | "I understand how they work and how to test them; I haven't trained one." |
| "I know your architecture." | "From what TD has published, I understand the pattern; I'd want to confirm the details." |
| "The average score is 90%, so we can ship." | "The high-risk slice must pass 100%; averages hide P0 failures." |
| "It's stochastic, so flaky is expected." | "Variance is expected; I fix the oracle and gate on the distribution." |
| "Manual review is enough." | "Human review is the last line; I automate the repeatable evidence and reserve humans for judgement." |

---

## 9. Sources (public)

Use these to verify before quoting. Dates are publication dates.

- **TD strengthens Its Responsible AI Principles** — TD Media Room, 30 Jul 2026:
  <https://td.mediaroom.com/2026-07-30-TD-strengthens-Its-Responsible-AI-Principles-to-support-trust-in-AI-innovation>
- **TD Insurance launches client-facing GenAI Virtual Assistant** — TD Media Room, 1 May 2026:
  <https://td.mediaroom.com/2026-05-01-Lets-Chat-Coverage-TD-Insurance-Rolls-Out-New-Virtual-Assistant>
- **TD launches agentic AI for real estate secured lending** — TD Media Room, 21 May 2026:
  <https://td.mediaroom.com/2026-05-21-TD-Launches-Agentic-AI-to-Transform-Real-Estate-Secured-Lending-from-End-to-End>
- **How an AI chatbot supports TD branch banking** — TD Stories, 30 Jul 2026:
  <https://stories.td.com/ca/en/article/branch-banking-virtual-assistant>
- **TD customer service in the age of AI** — TD Stories, 15 Aug 2025:
  <https://stories.td.com/ca/en/article/td-customer-support-agent-ai-tool>
- **Meet the Trustworthy AI team at TD** — TD Stories, 5 Jun 2025:
  <https://stories.td.com/ca/en/article/layer-6-trustworthy-ai-td>
- **Inside TD's Responsible AI Strategy** — Fintech.ca, 31 Mar 2026:
  <https://www.fintech.ca/2026/03/31/inside-td-responsible-ai-strategy/>

---

## 10. The 10-second version

> **TD is deploying RAG assistants with citations, a client-facing EN/FR chatbot and agentic
> workflows with human review. My job is to prove retrieval, grounding, citations, privacy,
> fairness, safety and operations — and to make that evidence a release gate.**
