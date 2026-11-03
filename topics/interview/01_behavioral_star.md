# 01 — Behavioral Interviews (STAR)

## The structure

| Part | What to say | Time |
|---|---|---|
| **Situation** | Context: team, system, constraint | 15–20s |
| **Task** | What you owned / the problem | 10–15s |
| **Action** | What you did, in first person, with trade-offs | 40–60s |
| **Result** | Outcome, metric, what changed | 15–20s |

Keep the whole answer under 90 seconds. Let them ask follow-ups.

---

## What interviewers listen for

- **Ownership:** "I did" not "we did" for your part.
- **Judgment:** why you chose that approach, what you rejected.
- **Impact:** a metric or observable change.
- **Collaboration:** how you worked with dev/product/ops.
- **Reflection:** what you'd do differently.

---

## Story templates

### 1. Improved reliability

> "Our regression suite was flaky and slow, so teams ignored red builds. I owned the fix: split
> suites by risk/tag, replaced fixed sleeps with condition-based waits, isolated test data, and added
> failure artifacts. Result: flaky rate dropped and the suite became a real gate instead of a
> warning light."

### 2. Disagreed with a technical decision

> "A team wanted to solve cross-service breakage with more E2E tests. I proposed contract tests
> instead and prototyped one interface. It caught a breaking change before deploy, and we adopted it
> for the critical services. The key was bringing a working prototype, not just an opinion."

### 3. Learned something fast

> "I had to pick up a new language/tool for a project. I mapped it onto concepts I already knew, built
> a thin vertical slice, and shipped a working example before generalizing. Result: I was productive
> in days, not weeks."

### 4. Helped others

> "I turned repeated setup into shared clients, fixtures, and docs. New tests went from days to hours
> because people weren't re-solving HTTP setup and driver management."

### 5. Made a mistake

> "I changed a shared default without treating it as a public interface and broke other suites. I
> reverted, added a compatibility test, and adopted a rule: consumer-visible defaults need a
> changelog and a deprecation path."

### 6. Ambiguity

> "The requirements were unclear, so I made the smallest assumption explicit, shipped a thin slice,
> and got feedback early. That avoided building the wrong abstraction for weeks."

### 7. Influenced without authority

> "I couldn't mandate a tool, so I made adoption effortless: a template, a 10-minute quickstart, and
> one pilot team. Their win did more than any mandate."

### 8. Delivered under pressure

> "A release was blocked by a flaky integration. I triaged with logs/traces, isolated the failing
> dependency, added a stub and a regression test, and unblocked the release without hiding the
> problem."

---

## Delivery tips

- **Lead with the result if it's strong**, then explain. Or follow STAR; either works.
- **Quantify** only if true: runtime, flaky rate, adoption, defects caught, time saved.
- **Name the trade-off**: "I chose X over Y because…"
- **Keep it conversational.** Don't recite.
- **Have a "what I'd do differently"** for every story.

---

## Common behavioral questions

1. Tell me about yourself.
2. Why this role?
3. Tell me about a time you improved quality.
4. Tell me about a disagreement.
5. Tell me about a mistake.
6. Tell me about a time you learned fast.
7. Tell me about a time you influenced without authority.
8. Tell me about a time you handled ambiguity.
9. Tell me about a time you delivered under pressure.
10. What's your biggest weakness?
11. Why should we hire you?
12. Where do you see testing going?
13. How do you handle flaky tests?
14. How do you prioritize testing?
15. Tell me about a time you said no.

---

## Weakness answer structure

1. A real gap.
2. What you've done to close it.
3. Evidence of progress.
4. What you're still working on.

Avoid fake weaknesses ("I care too much") and fatal ones for the role.

---

## Behavioral checklist

- [ ] 6–8 stories prepared.
- [ ] Each under 90 seconds.
- [ ] First-person ownership.
- [ ] One metric/outcome per story.
- [ ] One trade-off per story.
- [ ] One "what I'd do differently."
- [ ] Practiced out loud.
- [ ] Ready for follow-ups.
