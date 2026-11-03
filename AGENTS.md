# AGENTS.md — Repository Instructions

This file is the source of truth for how to add or change study material in this repository.

## Purpose

This is a reusable, topic-indexed SDET study library. It is **not** built around one company, one
job description, or one interview. Company-specific material is allowed only as an optional,
clearly labeled case study.

## Repository layout

```text
/
├── README.md
├── AGENTS.md
├── STUDY_PLAN.md
├── PROGRESS.md
├── CHEATSHEET.md
├── CAPSTONE.md
├── CATALOG.md
├── PROJECTS.md
├── GLOSSARY.md
├── topics/       # study packs
└── projects/     # runnable automation frameworks
```

Keep the distinction:

- **`topics/`** — concepts, theory, Q&A, exercises.
- **`projects/`** — runnable frameworks and demos.

The Spring Boot mini-service lives under `topics/spring-boot/examples/mini-service/` because it is a
study example, not a standalone framework.

## Non-negotiables

1. **No company-specific folders or packs.** Do not create `acme-prep/`, `bank-interview/`, etc.
2. **Microservices stay theory-only.** Do not add a Testcontainers/Pact/Kafka lab unless the user
   explicitly asks for one.
3. **Cloud is Azure-first and provider-neutral.** Use Azure examples and keep the AWS/GCP mapping
   table current. Do not create a GCP-only pack.
4. **Do not duplicate existing coverage.** Search `topics/` first. If a topic exists, extend it or
   cross-link it.
5. **Preserve user work.** Inspect existing files before deleting or rewriting. Do not “clean up”
   unrelated content.
6. **No secrets, credentials, or real PII** in notes, examples, or test data.
7. **Runnable examples or clearly marked pseudocode.** Do not present untested code as working.
8. **Keep study plans role-agnostic.** Add topics when asked; do not rewrite the repository or the
   study plan around one interview.

## Topic pack structure

Use this shape for a new topic under `topics/`:

```text
topics/<topic>/
  README.md              # purpose, 60-second cram, file map, key terms, traps, study plan
  01_<topic>.md          # core concepts
  02_<topic>.md          # deeper mechanics or examples
  03_qa.md               # questions with spoken model answers
  04_exercises.md        # practice tasks with checklists (optional)
  examples/              # optional runnable code or configs
```

Small topics do not need every file. A `README.md`, one theory file, and `03_qa.md` are enough if
that is the honest scope. If a pack does not need its own exercise file, add the task to
`EXERCISES.md` instead. Do not create empty placeholder files.

### README requirements

Every pack README must include:

- one-line purpose and why an SDET should care;
- a **60-second cram** section with the highest-signal facts;
- a file map;
- key terms table with testing implications;
- traps or common mistakes;
- a short study plan;
- links to related packs.

### Q&A requirements

- 15–30 questions for a substantial pack.
- Answers should be spoken, not essay-length.
- Include the trap or trade-off, not just the definition.
- Prefer “what → why → trade-off → example”.

### Exercise requirements

- Tasks should produce something: a query, a test, a checklist, a diagram, a release plan.
- Include a short “check” list so the learner can self-verify.
- Prefer realistic scenarios over trivia.

## Naming and layout

- Topic folder: `topics/<topic>/`, lowercase and hyphenated.
- Project folder: `projects/<project>/`, lowercase and hyphenated.
- Numbered files: `01_`, `02_`, etc.
- Keep code under `examples/` when it is runnable.
- Keep project-specific code in the existing runnable project folders:
  - `projects/selenium-bdd/`
  - `projects/playwright/`
  - `projects/restassured-bdd/`
  - `projects/karate/`
  - `topics/spring-boot/examples/mini-service/`

## Writing style

- Lead with the **60-second cram**.
- Prefer tables, short diagrams, and checklists over long prose.
- Name the tool and the method: “`ExpectedConditions.elementToBeClickable`” beats “wait for it”.
- State the trap explicitly.
- Include a testing implication for every concept.
- Avoid marketing language and generic filler.
- Keep the tone direct and practical.

## Adding a new pack

1. Search `topics/` and `CATALOG.md` for existing coverage.
2. Decide whether this is a new pack or an extension of an existing one.
3. Create `topics/<topic>/` and the README.
4. Write the 60-second cram first.
5. Add theory, examples, Q&A, and exercises.
6. Add the topic to:
   - `CATALOG.md`;
   - the topic map in `README.md`;
   - `topics/README.md`;
   - at least one related pack.
7. Run verification.
8. Do not rewrite unrelated packs.

## Updating existing packs

- Preserve the existing voice and structure unless it is clearly broken.
- Generalize company-specific framing; keep real case studies only as optional examples.
- Update links when renaming or moving files.
- If you delete content, confirm it is empty, duplicated, or explicitly obsolete.
- Keep `README.md`, `CATALOG.md`, and `PROGRESS.md` accurate.

## Current intentional decisions

- Microservices: theory only; no executable lab.
- `CAPSTONE.md` is a single-service build, not a microservices lab.
- Cloud: Azure-first with AWS/GCP equivalents.
- AI testing and AI-assisted testing are separate packs:
  - `topics/ai-testing/` — testing AI systems.
  - `topics/ai-assisted-testing/` — using AI to help test.
- Existing UI/API frameworks are the runnable examples; do not add another one.
- Interview material lives in `topics/interview/` and each pack’s `03_qa.md`.
- The study system files are `STUDY_PLAN.md`, `PROGRESS.md`, `CHEATSHEET.md`, `EXERCISES.md`,
  `CAPSTONE.md`, `CATALOG.md`, `PROJECTS.md`, and `GLOSSARY.md`.

## Verification

Run these before finishing a code or structure change:

```powershell
# Mini service tests
cd "topics/spring-boot/examples/mini-service"; mvn -B -q test

# Compile the other Maven projects
cd "projects/karate"; mvn -B -q -DskipTests package
cd "projects/restassured-bdd"; mvn -B -q -DskipTests package
cd "projects/selenium-bdd"; mvn -B -q -DskipTests package

# Playwright typecheck
cd "projects/playwright"; npx tsc --noEmit

# Whitespace check
git diff --check
```

Also verify that every relative Markdown link resolves before finishing. The CI workflow runs a link
check as well.

## When in doubt

Ask the user before adding a large new topic area, deleting content, or changing the repository’s
organizing principle.
