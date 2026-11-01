# 01 — LLM & RAG Primer (from the testing side)

Goal: enough vocabulary to follow the conversation and a strong opinion on **how each part fails**.
You are not expected to build a model. You are expected to know what the system is made of, so you
can decide what to test.

> **Golden rule for this interview:** every AI term you use, know it in one line, and know *how it
> changes testing*. That is the QE angle that earns the job.

---

## 30-second answer

> "An LLM is a next-token predictor trained on a large body of text. It generates fluent output
> from patterns, not from a database of verified facts. RAG turns it into an open-book exam: the
> app retrieves relevant passages from a controlled knowledge base, puts them in the prompt, and
> asks the model to answer from them and cite them. That gives us freshness, access control and an
> audit trail — but it adds a new failure surface, retrieval. So I test the data, retrieval,
> generation and conversation separately, then the whole thing end to end."

---

## 1. What an LLM actually is

An LLM is a very large neural network trained to predict the **next token** given everything
before it. Training adjusts the model's **parameters** (weights) over huge amounts of text. At
inference time the parameters are fixed: the model is not learning from your question. It is
producing a plausible continuation, one token at a time.

That one fact explains most of the behaviour you have to test:

| Behaviour | Why it happens | Test implication |
|---|---|---|
| Fluent, confident tone | The model is optimized for plausible text | Confidence is not evidence; check the facts and sources |
| Hallucination | It predicts likely text, not verified truth | Measure grounding/faithfulness, not just readability |
| Same question → different answer | Sampling and runtime non-determinism | Run multiple times; assert properties, not exact strings |
| Knowledge cutoff | It only knows what was in training | Fresh facts must come from RAG, tools or an updated model |
| Context window limit | It can only see a fixed number of tokens | Test long conversations and large retrieved contexts |
| Prompt sensitivity | Instructions are part of the input | Version prompts; regression-test prompt changes |
| No real "memory" | Each call is stateless unless the app adds history | Multi-turn context is an application feature to test |

### The terms in plain English

| Term | Plain English | Why a tester cares |
|---|---|---|
| **Token** | A piece of text, roughly 4 characters or ¾ of a word | Limits, cost and latency are measured in tokens |
| **Context window** | The maximum tokens the model can see at once (input + output) | Retrieved chunks and chat history compete for space |
| **Prompt** | The instructions and input sent to the model | A prompt change is a code change; it needs regression tests |
| **System prompt** | The app's standing rules: role, tone, boundaries, format | Test that it is applied — and that it doesn't leak secrets or internal rules |
| **Temperature** | Randomness of token selection | Low for consistency, but 0 is not a guarantee |
| **Top-p / nucleus** | Another way to control randomness | Same testing consequence as temperature |
| **Parameter** | A learned weight in the model | Changed by training/fine-tuning, not by prompts |
| **Fine-tuning** | Further training on examples to change behaviour | Needs its own eval set; can drift and forget |
| **Inference** | Running the trained model to get an output | What happens on every user request |
| **Knowledge cutoff** | The date after which the model has no built-in knowledge | Test that time-sensitive answers come from RAG, not memory |

**One-liner to memorize:**
> "An LLM is a very well-read intern who never says 'I don't know' unless you teach it to."

---

## 2. Embeddings and vector search

An **embedding** is a list of numbers (a vector) that represents the meaning of a piece of text.
Texts with similar meaning end up close together in that vector space. A **vector database** stores
those vectors and, given a query vector, returns the nearest chunks.

This is how RAG finds passages by **meaning**, not just keyword matching. A search for "card got
eaten by an ATM" can retrieve a document titled "Debit card retention" even though the words don't
match.

```
"lost my debit card"  ──embed──►  [0.12, -0.44, 0.91, ...]  ──nearest neighbours──►
                                                                  chunk A (lost/stolen policy)
                                                                  chunk B (replacement card fees)
                                                                  chunk C (travel notification)
```

**What to test at this layer**

- **Domain vocabulary:** acronyms, product names, internal jargon, typos.
- **Multilingual:** a French query should retrieve French (or correctly translated) sources.
- **Synonyms and paraphrase:** "fee", "charge", "cost"; "debit card", "bank card".
- **Negation and qualifiers:** "no fee" vs "fee"; "not available in Quebec".
- **Metadata filters:** locale, audience, effective date, classification, permissions.
- **Embedding model change:** re-index and re-run retrieval tests; vectors are not portable.

**Trap:** if you change the embedding model but don't re-embed the corpus, you are comparing
vectors from two different spaces. Retrieval quality collapses in a way that looks like a model
problem.

---

## 3. RAG step by step

RAG has two pipelines: **ingestion** (offline) and **query** (online).

```
INGESTION (offline)                                     QUERY (online)

 documents ──► parse ──► clean ──► chunk ──► embed ──► vector DB
   (PDF, HTML, policy, FAQ)                              ▲
                                                         │ similarity search
 user question ──► embed ──► retrieve top-k ──► rerank ──┘
                                                         │
                                                         ▼
                          prompt = system rules + question + retrieved chunks
                                                         │
                                                         ▼
                                                   LLM generates
                                                         │
                                                         ▼
                                      answer + citations + guardrails + log
```

### Ingestion side

1. **Collect** the source documents: policies, procedures, product pages, FAQs, rate sheets.
2. **Parse** them: PDF, HTML, tables, images, headers/footers. This is where a lot of silent
   quality loss happens.
3. **Clean**: remove duplicates, navigation text, expired versions.
4. **Chunk** into passages (common starting point: a few hundred tokens with 10–20% overlap).
5. **Enrich metadata**: source ID, title, section, effective date, locale, audience, classification.
6. **Embed** each chunk.
7. **Index** it in the vector database.

### Query side

1. Embed the user's question.
2. Retrieve the top-k nearest chunks, applying metadata filters (role, locale, date).
3. Optionally **rerank** the candidates with a more precise model.
4. Build the prompt: system rules + question + retrieved chunks + citation instructions.
5. Generate the answer.
6. Post-process: citations, PII redaction, disclaimers, links, formatting.
7. Log the trace: prompt version, model version, retrieved IDs and scores, answer, latency, cost.

### Why teams use RAG

| Benefit | What it means for testing |
|---|---|
| **Fresh knowledge** | Update the index, not the model. Test the refresh pipeline. |
| **Grounding** | The model is told to answer from sources. Test faithfulness. |
| **Citations** | Users and auditors can verify. Test citation accuracy, not just presence. |
| **Access control** | Metadata filters can restrict what a user retrieves. Test negative cases. |
| **Cost** | Cheaper than retraining for every knowledge change. Test retrieval cost/latency. |
| **Auditability** | You can see which sources produced the answer. Test the trace completeness. |

---

## 4. RAG vs fine-tuning vs prompt engineering

They solve different problems. Interviewers love this question because it shows whether you
understand the system, not just the buzzwords.

| | Prompt engineering | RAG | Fine-tuning |
|---|---|---|---|
| **What changes** | Instructions and examples in the prompt | External knowledge at query time | The model's weights |
| **Best for** | Tone, format, role, simple rules | Facts, freshness, citations, access control | Style, classification, narrow behaviour |
| **Knowledge update** | Immediate | Re-index (minutes/hours) | Retrain (days/weeks) |
| **Citations** | No | Yes | No |
| **Cost to change** | Low | Medium | High |
| **Main risk** | Fragile; prompt injection | Retrieval failure; stale index | Overfitting; regression; cost |
| **Testing focus** | Prompt regression, guardrails | Retrieval + grounding + citations | Eval set, drift, catastrophic forgetting |

**The line to use:**
> "For a bank assistant, RAG is usually the right default because policies change and you need an
> audit trail. Fine-tuning is for behaviour and style, not for facts that change every quarter.
> In practice you use all three: prompt for format, RAG for knowledge, fine-tuning only if the
> base model can't do the job."

---

## 5. Agents and tool calling

An **agent** is an LLM in a loop with tools. Instead of just answering, it can decide to call a
function, read the result, and decide what to do next.

```
user goal ──► LLM plans ──► tool call (args) ──► app executes ──► result ──► LLM
                  ▲                                                           │
                  └──────────────── loop until done / limit ──────────────────┘
```

**Tool calling** is the model emitting a structured request like
`get_account_balance(customer_id="123")`; the application validates and executes it.

**What to test**

- **Tool selection:** did it choose the right tool for the goal?
- **Arguments:** are they valid, complete and authorized?
- **Permissions:** can the user only do what they are entitled to do?
- **Side effects:** is the action idempotent if retried? Can it be undone?
- **Stop conditions:** max steps, timeout, loop detection.
- **Human approval:** irreversible or high-risk actions require a human.
- **Audit:** every tool call, argument and result is logged.
- **Injection through tools:** a tool result or document can contain malicious instructions.

**TD context:** TD has publicly described agentic AI for real-estate secured lending that
classifies documents, extracts income, checks policy and generates a summary memo for an
underwriter. That is a constrained workflow with human review — a good example of "agent as
assistant, not autonomous decision-maker".

---

## 6. Where RAG fails — the test map

This is the table to memorize. It lets you answer "how would you test RAG?" without hand-waving.

| Stage | Typical failure | How to test it |
|---|---|---|
| **Source** | Missing, stale, duplicated or conflicting documents | Freshness checks, ownership, effective-date review, duplicate detection |
| **Parse** | Tables, footnotes or scanned text lost | Fixture PDFs/HTML with known content; assert extracted text |
| **Chunk** | A policy split mid-section; table rows separated from headers | Questions that span chunks; table-specific fixtures |
| **Embed** | Domain terms or French queries miss relevant chunks | Retrieval hit rate by topic and locale; synonym tests |
| **Index** | Wrong metadata, permissions or locale filters | Role/locale isolation tests; negative retrieval tests |
| **Retrieve** | Top-k too small (misses) or too large (noise) | Context recall vs precision; tune top-k and rerank |
| **Prompt** | Model ignores context, overgeneralizes or is overconfident | Faithfulness, refusal correctness, prompt regression |
| **Generate** | Hallucinates, contradicts context, cites the wrong passage | Claim-level grounding, citation accuracy, answer correctness |
| **Post-process** | PII leak, missing disclaimer, broken link, wrong language | Redaction tests, compliance checks, link checks, EN/FR |
| **Operate** | Index stale, model changed, provider outage | Refresh SLA, version pinning, fallback and canary tests |

**The sentence that shows you get it:**
> "RAG doesn't remove hallucination. It gives the model evidence and gives me a trace, but the
> model can still ignore the evidence, misread it or cite the wrong passage. That's why I test
> retrieval and generation separately."

---

## 7. What RAG does **not** fix

- **Bad source documents.** If the policy is wrong, RAG will confidently cite the wrong policy.
- **Stale content.** If the index isn't refreshed, the answer is old but looks authoritative.
- **Permissions.** Retrieval filters can leak restricted content if misconfigured.
- **Prompt injection.** A malicious document in the corpus can contain instructions.
- **Reasoning.** Retrieval gives facts, not arithmetic, eligibility logic or judgement.
- **Multilingual gaps.** A weak embedding model may retrieve poor French sources.
- **Tone and advice boundaries.** The model can still give personalized advice unless guardrails
  and prompts prevent it.
- **Overreliance.** A polished answer with a citation can still be wrong; users need to verify.

---

## 8. Analogies to use out loud

These make you sound clear, not academic.

| Concept | Analogy |
|---|---|
| **LLM** | A brilliant, fast-talking intern who has read a lot but never says "I don't know" |
| **Hallucination** | The intern making up a citation that looks real |
| **Embeddings** | Putting every sentence on a giant map where similar meanings sit close together |
| **Vector DB** | A library catalogue that finds books by meaning, not just title |
| **RAG** | An open-book exam: the intern gets the right pages and must cite them |
| **Chunking** | Cutting a policy manual into index cards |
| **Top-k** | How many index cards you hand the intern |
| **Reranking** | Putting the most useful cards on top of the pile |
| **Temperature** | How adventurous the intern is allowed to be |
| **Fine-tuning** | Sending the intern to a specialised training course |
| **Prompt injection** | A document in the library that says "ignore the librarian" |
| **LLM-as-judge** | A teaching assistant grading with a rubric |
| **Golden dataset** | The exam paper you re-use every release |
| **Guardrails** | Security at the door, before and after the model |

---

## 9. Likely follow-ups (short answers)

**What is a token?**
> A piece of text, roughly 4 characters or ¾ of a word. Limits, cost and latency are measured in
> tokens.

**What is temperature?**
> Controls randomness. 0 is greedy/most likely, higher is more varied. 0 reduces variance but
> doesn't guarantee identical output.

**Why not just fine-tune?**
> Fine-tuning changes behaviour and style, not a reliable knowledge base. It's expensive to
> refresh, can't cite sources and can regress. RAG is better for changing policy content.

**What is a vector database?**
> A store that indexes embeddings and returns the nearest chunks by semantic similarity. It also
> holds metadata for filtering by locale, audience and permissions.

**RAG vs semantic search?**
> Semantic search just returns the most similar passages. RAG takes those passages, puts them in a
> prompt and asks the model to generate a grounded answer with citations. RAG = retrieval +
> generation + prompting.

**What is hybrid search?**
> Combining keyword/BM25 search with vector search. It helps with exact terms like product codes,
> fee names and acronyms where pure embeddings can miss. It's a test dimension: exact-match queries
> vs paraphrase queries.

**What is top-k?**
> The number of chunks retrieved for the prompt. Too low misses information; too high adds noise
> and cost. It's a testable trade-off.

**What is a reranker?**
> A second, more precise model that reorders the retrieved candidates. It improves precision when
> the first-stage search is noisy.

**How does RAG cite sources?**
> The app passes chunk IDs/metadata into the prompt and asks the model to cite them, or it maps
> the answer back to chunks after generation. Either way, the citation must be verified — it can
> be wrong even when it looks plausible.

**What is prompt injection?**
> Text that tries to override the system's instructions. Direct = the user types it. Indirect =
> it arrives through a retrieved document, web page, email or tool output. Indirect is the bigger
> risk for RAG.

**What is an agent?**
> An LLM with tools and a loop. It plans, calls tools, observes results and repeats until done or
> stopped. Testing adds tool selection, permissions, side effects and stop conditions.

**What is the difference between the model and the AI system?**
> The model is one component. The system is model + prompt + retrieval + data + guardrails + UI +
> logging + operations. Most defects I can catch are in the system, not the model.

---

## 10. The 10-second version

> **LLM = predictor. Embeddings = meaning map. RAG = open-book exam. Agent = LLM with tools.
> The failures are retrieval, grounding, safety and operations — and that's what I test.**
