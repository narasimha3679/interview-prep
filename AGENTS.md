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

## Rules

- Sessions are append-only. Record the answer as given, then the correction.
- Preserve user work; investigate before deleting or rewriting.
- No secrets, credentials, or real personal data.
- Runnable examples or clearly marked pseudocode — never present untested code as working.
- Relative Markdown links must resolve: `python .github/scripts/check_links.py`.
