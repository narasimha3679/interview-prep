# AI Testing Interview Prep — LLM · RAG · Chatbots

Prep for a **software testing with AI** interview at **TD Bank**.

This pack is deliberately about **testing AI systems** — LLMs, RAG pipelines, chatbots and
agents — not about using AI to write test scripts. You already know that side. If they pivot to
"how do you use AI in testing?", there is a short bridge in `05_mock_qa.md` (A4–A5).

**The position to take into the room:**
> "I'm a QE who can test AI systems end to end. I won't build the model — I'll build the evidence
> that it is accurate, grounded, safe and reliable enough to ship, and the gates that stop it
> regressing. I test the whole system: data, retrieval, prompt, model, conversation, safety and
> operations."

That is the job. Banks are not short of people who can call an API; they are short of people who
can prove an AI feature is safe under regulation.

---

## 🔥 60-second cram sheet

1. **LLM** = next-token predictor trained on text. Fluent, not factual. It has no database of truth.
2. **RAG** = open-book exam: retrieve relevant chunks → put them in the prompt → generate an answer with citations.
3. **Two failure surfaces:** retrieval and generation. Test them separately before testing end to end.
4. **Non-determinism:** one green run is not evidence. Run N times, assert properties/rubrics, track distributions.
5. **Oracle:** exact match for constrained facts; expected source for retrieval; rubric or LLM-judge for open text; human for high-risk.
6. **Golden dataset:** versioned real questions + expected answer + expected source + unanswerable + adversarial + EN/FR.
7. **Retrieval metrics:** context recall, context precision, context relevancy, hit rate@k, MRR/nDCG.
8. **Generation metrics:** faithfulness/groundedness, answer relevancy, answer correctness, citation accuracy, refusal correctness.
9. **LLM-as-judge** scales evaluation, but calibrate it against human labels, pin its version, and never let it be the only gate for P0 safety.
10. **Safety:** prompt injection (direct + indirect), PII leakage, system-prompt leakage, bias/fairness, harmful financial advice, insecure output handling, vector/embedding weaknesses.
11. **Regression triggers:** model, prompt, chunking, embedding model, top-k, index, source docs, guardrails, locale, retrieval filters.
12. **CI:** fast smoke + retrieval checks per PR; full eval nightly; red team + human review before release; block on thresholds and zero P0 safety failures.
13. **Production:** sample traffic, thumbs/feedback, drift, tracing, canary, rollback/kill switch.
14. **Banking lens:** citations + source version + audit log + human-in-the-loop + EN/FR parity + no personalised advice without disclaimer.
15. **TD public context:** Layer 6 (AI R&D), the Trustworthy AI team, retrieval-grounded assistants with human oversight, client-facing TDI chatbot, agentic AI with underwriter review.

---

## 🧠 The 4-question method — use this for every AI testing question

When they ask "how would you test X?", answer in this order. It makes you sound senior and stops
you rambling.

| # | Question | What to say |
|---|---|---|
| 1 | **What is the risk?** | Wrong money/eligibility/coverage? PII leak? Bad advice? Bad UX? Rank P0/P1/P2. |
| 2 | **Which layer?** | Data (L0), retrieval (L1), prompt and generation (L2–L3), conversation (L4), safety (L5), non-functional (L6), production (L7). |
| 3 | **What is the oracle?** | Exact, expected source, semantic similarity, rubric/LLM-judge, human review. |
| 4 | **What is the gate?** | Threshold, number of runs, CI stage, production monitoring, rollback. |

Example:
> "For a fee question, risk is P0 because a wrong number is customer harm. I'd test the retrieval
> layer first — does the current fee-schedule chunk come back in the top 5? Then generation — is
> the number grounded and is the citation the right page? The oracle is exact for the amount and
> source-based for the citation, run 3 times because generation varies, and the gate is 100% on the
> high-risk fee set plus zero P0 safety failures."

---

## 📁 What is in this pack

| File | What it gives you | Read when |
|---|---|---|
| `01_llm_rag_primer.md` | Plain-English LLM/RAG vocabulary and where each part fails | First — 30 min |
| `02_testing_ai_systems.md` | The test strategy, metrics, oracles, CI gates, tools | Core — 45 min |
| `03_chatbot_rag_test_cases.md` | Concrete test cases, Gherkin, release checklist | Core — 30 min |
| `04_automation_and_frameworks.md` | How to automate it (pytest, DeepEval/RAGAS, Playwright, CI) | Skim code — 30 min |
| `05_mock_qa.md` | 70+ interview questions with spoken answers | Rehearse — 45 min |
| `06_td_banking_lens.md` | TD public context + Trustworthy AI mapped to tests | Tailor — 20 min |

---

## 🎤 The 5 answers that win the room

1. **"I test retrieval and generation separately — otherwise a bad answer only tells me something
   broke, not which half to fix."**
2. **"I don't assert exact strings on generative output. I assert properties, expected sources and
   rubric scores, and I run it more than once."**
3. **"My oracle changes with the risk: exact for a fee, source-based for a citation, judge-based
   for tone, human for high-risk advice."**
4. **"An AI system is not just a model. It's data, retrieval, prompt, guardrails, UI, logging and
   operations — and that's where most defects live."**
5. **"In a bank, a good answer isn't enough. I need to show where it came from, which version of
   the policy, who reviewed it, and what stops it happening again."**

---

## 🗓️ Study plan for tonight

| Time | Do this |
|---|---|
| 20 min | This README. Say the cram sheet out loud once. |
| 30 min | `01_llm_rag_primer.md` — draw the RAG diagram from memory. |
| 45 min | `02_testing_ai_systems.md` — learn the layers, metrics, oracles, CI gates. |
| 30 min | `03_chatbot_rag_test_cases.md` — pick 10 cases you'd actually run. |
| 30 min | `04_automation_and_frameworks.md` — understand the test shape; don't memorize code. |
| 45 min | `05_mock_qa.md` — answer out loud before reading each model answer. |
| 20 min | `06_td_banking_lens.md` — pick 3 TD talking points and 5 questions to ask. |
| Morning | Cram sheet + the 4-question method + your 2 project stories. |

**If you only have one hour:** README + `02` sections 1–5 + `05` sections A, C1–C4, G6.

---

## 🧭 How to handle "I haven't done this exact thing"

Do not bluff. Use the honest bridge:

> "I haven't run a full LLM evaluation programme in production. What I have done is build
> automation frameworks, test APIs and data, and reason about risk and coverage. The AI-specific
> part I've learned is where the failure modes are and how to measure them — retrieval vs
> generation, faithfulness, citation accuracy, non-determinism, prompt injection. I'd be honest
> about the gap and close it fast, because the surrounding engineering is the same."

Then immediately show depth: name the layers, the metrics, the gate. Curiosity + structure beats a
false claim.

---

## 🎯 Delivery tips

- **Answer with structure:** risk → layer → oracle → gate. Never a one-liner.
- **Name the metric and the tool:** "I'd use context recall and faithfulness, with DeepEval or
  RAGAS in CI" beats "I'd check the answers".
- **Say the trap:** "…but if I only score the final answer, I can't tell whether retrieval or
  generation failed."
- **Give a TD example:** the retrieval-grounded policy assistant, the EN/FR requirement, the
  underwriter reviewing an agent-generated memo.
- **Be precise about what you don't know:** "I haven't built a vector index at scale; I understand
  the retrieval trade-offs and I'd test them."
- **Bring it back to risk:** in a bank, a confident wrong answer is worse than a refusal.

---

## 🌅 Morning of

- Say the **15-line cram sheet** out loud once.
- Say the **4-question method**: risk → layer → oracle → gate.
- Say the **8 layers (L0–L7)**: data, retrieval, prompt, generation, conversation, safety, non-functional, production.
- Pick **3 TD talking points**: grounding, human-in-the-loop, EN/FR.
- Pick **5 questions** from `06` to ask them.
- Have **one project story** and **one AI failure story** ready.
- If you don't know: *"I haven't worked with that directly. My instinct would be X because of Y —
  how does your team handle it?"*
- Breathe. You are not there to build the model; you are there to prove it is safe to ship.

---

## 🔗 The one-line version of this whole pack

> **"Test the data, test the retrieval, test the grounding, test the conversation, test the
> safety, test the operations — then gate the release on evidence and monitor it in production."**
