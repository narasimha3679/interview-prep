# AGENTS.md

Repository instructions for future sessions. Keep this file short; the detail lives in the packs.

## Layout

```text
README.md      # entry point
topics/        # study packs
projects/      # runnable automation frameworks
practice/      # learner state: session logs + skill matrices
.opencode/     # OpenCode plugin: practice auto-save
```

## Practice log protocol

The learner tracks progress in `practice/`. It is the deliverable, not a note.

1. **Read `practice/<topic>/SKILLS.md` first**, before teaching. It says what is weak.
2. **Write every graded answer to `practice/<topic>/sessions/YYYY-MM-DD.md` in the same reply.**
   Not at the end of the session, not when asked.
3. **Update `SKILLS.md` in place** whenever a level, piece of evidence, or next action changes.
4. **End every reply that taught or graded something with** `Logged: <path>`.
5. **Never finish a turn with graded but unwritten work.** Chat is not storage.
6. Progress is auto-committed by `.opencode/plugins/practice-autosave/index.ts` when a session goes
   idle. That is a backstop, not a substitute for 2–5.

A level moves only on evidence: `solid` requires a correct answer **and** working code.

## Starting a session

The user opens with something like "let's do Selenium" or "teach me collections". Do this, without
being told:

1. **`practice/<topic>/SKILLS.md` exists** → read it first. The `shaky` and `unknown` rows are the
   warm-up; "Current focus order" is the plan.
2. **It does not exist** → bootstrap. Copy `practice/_template/SKILLS.md` to
   `practice/<topic>/SKILLS.md`, then write the first session to
   `practice/<topic>/sessions/YYYY-MM-DD.md` using `practice/_template/session.md` as its **shape**,
   not as a file to copy verbatim. Announce it in one line.
3. **Seed the rows** from that pack's `README.md` file map and its questions — `03_qa.md` when the
   pack has one, otherwise the question headings inside each theory file. Everything starts
   `not assessed`.
4. **A new matrix has no weak rows**, so a warm-up is impossible. Calibrate with 4–5 questions on
   the first unit instead. Never invent prior weaknesses.
5. **No matching pack in `topics/`** → say so, then ask whether to build the pack or practice from
   the user's own material.

Naming: the folder is the topic family, not the language — `practice/selenium/`, not
`practice/selenium-java/`. Put the language and versions in the session header. Session numbering is
per topic folder. On day one most rows stay `not assessed` until code runs; that is correct, not
neglect.

Never ask "what have you covered so far?" if the matrix can answer it. Ask only for what changes the
drill: target role, deadline, or a topic the matrix does not cover.

## Teaching contract

- **Teach one unit, then test it in the same reply.** No lecture without a test.
- A unit is one idea the learner can hold in working memory: one collection idiom, one wait strategy,
  one failure mode. Not a chapter.
- **Test shape:** 3–6 questions plus 1–2 code tasks. Predict-the-output counts as a question.
- **Grade every answer:** score 0–4, what was wrong, the fix, and the trap in one line.
- **Answers are spoken-length.** If the learner writes an essay, the model answer is still 3 lines.
- Show runnable code only after running it. Prefer the learner's own file over a pasted snippet.
- Weak spots drive the next unit. Do not follow the pack's file order when the matrix says otherwise.

## Rules

- Sessions are append-only. Record the answer as given, then the correction.
- Preserve user work; investigate before deleting or rewriting.
- No secrets, credentials, or real personal data.
- Runnable examples or clearly marked pseudocode — never present untested code as working.
- Relative Markdown links must resolve: `python .github/scripts/check_links.py`.
