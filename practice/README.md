# Practice Log

Session-by-session record of what I was asked, how I answered, and what to drill next.

Notes create recognition. Being asked creates recall. This folder is the recall record: it is the only
place that keeps the *wrong* answers, because those are the ones worth revisiting.

## How a session runs

1. **Warm-up** — 5–8 rapid questions pulled from the `shaky` and `unknown` rows of the skill matrix.
2. **Drill** — 1–2 small code tasks written from scratch, no copy-paste.
3. **Grade** — every answer gets a 0–4 score and one line of verdict.
4. **Record** — write the session file, then update the skill matrix in place.

## Grading scale

| Score | Label | Meaning |
|---|---|---|
| 0 | unknown | No idea, or a wrong mental model |
| 1 | heard of it | Recognises the term, cannot use it |
| 2 | shaky | Partially right, misses the trap or the trade-off |
| 3 | solid | Correct, names the trap |
| 4 | strong | Correct + trap + trade-off + example, could teach it |

A skill moves to **solid** only after a correct answer *and* a working code drill. Talking is not
enough for a coding interview.

## Files

```text
practice/
├── README.md            # this page
└── java/
    ├── SKILLS.md        # live matrix: level, evidence, next action
    ├── sessions/        # one file per session, append-only
    │   └── YYYY-MM-DD.md
    └── drills/          # code written during practice, reviewed line by line
```

## Rules

- Record the answer **as given**, then the correction. Do not rewrite history.
- Sessions are append-only. `SKILLS.md` is the only file edited in place.
- A skill is not "done" because the topic was read. Only answers and code move it.
- No secrets, credentials, or real personal data in drills.
