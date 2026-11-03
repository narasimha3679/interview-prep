# SDET Study Repository

A reusable, topic-indexed study library for software development engineers in test.

This is **not** built around one interview or one company. Each topic pack is a self-contained study
unit: concepts, vocabulary, runnable examples where useful, interview questions, and exercises.
Company-specific notes are optional lenses, never the organizing principle.

**Last verified:** November 2026.

---

## Repository layout

```text
/
├── README.md              # this page
├── AGENTS.md              # instructions for future changes
├── STUDY_PLAN.md          # time-boxed study sequences
├── PROGRESS.md            # checklist + confidence tracker
├── CHEATSHEET.md          # 180 high-signal facts
├── EXERCISES.md           # cross-topic practice bank
├── CAPSTONE.md            # end-to-end build spec
├── CATALOG.md             # every topic with level/time/prereqs
├── PROJECTS.md            # runnable projects and commands
├── GLOSSARY.md            # cross-topic vocabulary
├── topics/                # study packs, grouped by subject
└── projects/              # runnable automation frameworks
```

**Topics** are for learning and reference. **Projects** are for running, breaking, and explaining.
Keep them separate in your head: read the topic, then prove it in a project.

---

## Start here

- **New?** Read this page, then open [`CATALOG.md`](CATALOG.md) and pick the tracks the role needs.
- **Following a plan?** Use [`STUDY_PLAN.md`](STUDY_PLAN.md) for 30-minute, 1-hour, 1-day, 3-day,
  1-week, and 2-week plans.
- **Tracking progress?** Use [`PROGRESS.md`](PROGRESS.md).
- **Revising?** Start with [`CHEATSHEET.md`](CHEATSHEET.md), then answer Q&A out loud.
- **Practicing?** Use [`EXERCISES.md`](EXERCISES.md) for cross-topic tasks.
- **Looking up a term?** Use [`GLOSSARY.md`](GLOSSARY.md).
- **Building?** Use [`CAPSTONE.md`](CAPSTONE.md) to combine the service, API, UI, CI/CD, cloud, and
  observability material.
- **Want to run something?** See [`PROJECTS.md`](PROJECTS.md).
- **Adding material?** Read [`AGENTS.md`](AGENTS.md).

---

## Learning paths

These are **independent tracks**, not a fixed curriculum. A role usually needs two or three of them.
Do not study all of them for every interview; use [`STUDY_PLAN.md`](STUDY_PLAN.md) to scope the role
first.

### 1. UI automation

1. [`topics/selenium/`](topics/selenium/) — waits, locators, POM, exceptions, framework design
2. [`topics/testng/`](topics/testng/) — lifecycle, parallel runs, assertions, listeners
3. [`topics/cucumber/`](topics/cucumber/) — BDD, data handling, reusable steps
4. [`projects/selenium-bdd/`](projects/selenium-bdd/) — runnable Selenium + Cucumber + TestNG
5. [`projects/playwright/`](projects/playwright/) — runnable TypeScript + Playwright
6. [`topics/typescript/`](topics/typescript/) — TypeScript for Playwright
7. [`topics/mobile/`](topics/mobile/) — mobile web and Appium

### 2. API automation

1. [`topics/restassured/`](topics/restassured/) — given/when/then, JSONPath, auth, specs
2. [`projects/restassured-bdd/`](projects/restassured-bdd/) — runnable RestAssured + Cucumber
3. [`projects/karate/`](projects/karate/) — runnable Karate API framework
4. [`topics/graphql/`](topics/graphql/) — queries, mutations, errors, schema, auth
5. [`topics/grpc/`](topics/grpc/) — Protobuf and gRPC testing
6. [`topics/security/`](topics/security/) — API auth and OWASP basics

### 3. Backend-aware SDET

1. [`topics/java/`](topics/java/) — OOP, collections, exceptions, streams, JDBC
2. [`topics/collections/`](topics/collections/) — collections deep dive and demos
3. [`topics/coding/`](topics/coding/) — coding drills
4. [`topics/spring-boot/`](topics/spring-boot/) — Spring Boot, REST, persistence, testing
5. [`topics/microservices/`](topics/microservices/) — microservices testing theory
6. [`topics/observability/`](topics/observability/) — logs, metrics, traces, debugging

### 4. Cloud and delivery

1. [`topics/ci-cd/`](topics/ci-cd/) — pipeline design, GitHub Actions, release validation
2. [`topics/cloud/`](topics/cloud/) — cloud fundamentals and provider mapping
3. [`topics/cloud/azure/`](topics/cloud/azure/) — Azure core services and Azure for SDETs
4. [`topics/cloud/containers/`](topics/cloud/containers/) — Docker and Kubernetes basics
5. [`topics/performance/`](topics/performance/) — load testing and CI integration
6. [`topics/security/`](topics/security/) — security testing and OWASP

### 5. Full SDET

Work through paths 1–4, then add:

- [`topics/test-strategy/`](topics/test-strategy/) — test levels, design, data, flakiness, metrics
- [`topics/accessibility/`](topics/accessibility/) — WCAG and automated checks
- [`topics/ai-testing/`](topics/ai-testing/) — testing LLM/RAG/chatbot systems
- [`topics/ai-assisted-testing/`](topics/ai-assisted-testing/) — using LLMs to help test
- [`CAPSTONE.md`](CAPSTONE.md) — build and ship a testable service end to end

### 6. Interview revision

- [`CHEATSHEET.md`](CHEATSHEET.md) — last-minute facts
- [`topics/interview/`](topics/interview/) — behavioral, project stories, system design, mock Q&A
- Each topic pack's `*_qa.md` — answer out loud before reading the model answer
- [`STUDY_PLAN.md`](STUDY_PLAN.md) — time-boxed sequences

---

## Topic map

### Foundations

| Topic | Path |
|---|---|
| Java | [`topics/java/`](topics/java/) |
| Collections | [`topics/collections/`](topics/collections/) |
| Coding drills | [`topics/coding/`](topics/coding/) |
| SQL | [`topics/sql/`](topics/sql/) |
| TypeScript | [`topics/typescript/`](topics/typescript/) |

### Automation

| Topic | Path |
|---|---|
| Selenium | [`topics/selenium/`](topics/selenium/) |
| TestNG | [`topics/testng/`](topics/testng/) |
| Cucumber | [`topics/cucumber/`](topics/cucumber/) |
| RestAssured | [`topics/restassured/`](topics/restassured/) |
| GraphQL | [`topics/graphql/`](topics/graphql/) |
| gRPC | [`topics/grpc/`](topics/grpc/) |
| Mobile | [`topics/mobile/`](topics/mobile/) |
| Accessibility | [`topics/accessibility/`](topics/accessibility/) |

### Backend and architecture

| Topic | Path |
|---|---|
| Spring Boot | [`topics/spring-boot/`](topics/spring-boot/) |
| Microservices | [`topics/microservices/`](topics/microservices/) |
| Observability | [`topics/observability/`](topics/observability/) |

### Delivery and cloud

| Topic | Path |
|---|---|
| CI/CD | [`topics/ci-cd/`](topics/ci-cd/) |
| Cloud | [`topics/cloud/`](topics/cloud/) |
| Azure | [`topics/cloud/azure/`](topics/cloud/azure/) |
| Containers | [`topics/cloud/containers/`](topics/cloud/containers/) |
| Performance | [`topics/performance/`](topics/performance/) |

### Quality

| Topic | Path |
|---|---|
| Test strategy | [`topics/test-strategy/`](topics/test-strategy/) |
| Security | [`topics/security/`](topics/security/) |
| AI systems testing | [`topics/ai-testing/`](topics/ai-testing/) |
| AI-assisted testing | [`topics/ai-assisted-testing/`](topics/ai-assisted-testing/) |
| Interview | [`topics/interview/`](topics/interview/) |

Full level/time/prerequisite details are in [`CATALOG.md`](CATALOG.md). The detailed topic index is
[`topics/README.md`](topics/README.md).

---

## Runnable projects

| Project | Path | Stack |
|---|---|---|
| Selenium BDD | [`projects/selenium-bdd/`](projects/selenium-bdd/) | Java, Selenium, Cucumber, TestNG |
| Playwright | [`projects/playwright/`](projects/playwright/) | TypeScript, Playwright Test |
| RestAssured BDD | [`projects/restassured-bdd/`](projects/restassured-bdd/) | Java, RestAssured, Cucumber |
| Karate | [`projects/karate/`](projects/karate/) | Java, Karate |
| Spring Boot mini-service | [`topics/spring-boot/examples/mini-service/`](topics/spring-boot/examples/mini-service/) | Java, Spring Boot, JPA |

See [`PROJECTS.md`](PROJECTS.md) for run commands and what each project proves.

---

## Study system

| File | Purpose |
|---|---|
| [`STUDY_PLAN.md`](STUDY_PLAN.md) | 30-minute to 2-week plans, role paths, revision cadence |
| [`PROGRESS.md`](PROGRESS.md) | Checklist and confidence tracker |
| [`CHEATSHEET.md`](CHEATSHEET.md) | 180 high-signal facts |
| [`EXERCISES.md`](EXERCISES.md) | 70 cross-topic practice tasks with done checks |
| [`CAPSTONE.md`](CAPSTONE.md) | End-to-end build spec |
| [`CATALOG.md`](CATALOG.md) | Topic catalogue with level/time/prereqs |
| [`PROJECTS.md`](PROJECTS.md) | Runnable projects and commands |
| [`GLOSSARY.md`](GLOSSARY.md) | Cross-topic vocabulary |
| [`AGENTS.md`](AGENTS.md) | Repository conventions for future changes |

---

## Quick start

```powershell
# Spring Boot mini-service
cd "topics/spring-boot/examples/mini-service"; mvn test

# RestAssured BDD
cd "projects/restassured-bdd"; mvn test

# Karate
cd "projects/karate"; mvn test

# Selenium BDD
cd "projects/selenium-bdd"; mvn test

# Playwright
cd "projects/playwright"; npm ci; npx playwright test
```

Some projects call public APIs or public demo sites. Network access is required for those.

---

## Conventions

The operational version of these conventions — including what future agents must check before
adding or changing material — is [`AGENTS.md`](AGENTS.md). In short, each topic pack should follow
the same shape:

```text
topics/<topic>/
  README.md              # purpose, 60-second cram, file map, key terms, traps
  01_<topic>.md          # core concepts
  02_<topic>.md          # deeper mechanics or examples
  03_qa.md               # questions with spoken model answers
  04_exercises.md        # optional practice tasks
  examples/              # optional runnable code
```

Rules:

1. **Knowledge first, interview performance second.** Core notes stand alone; Q&A is separate.
2. **Name the tool and the method.** Prefer “`ExpectedConditions.elementToBeClickable`” over “wait”.
3. **State the trap.** Every concept includes the mistake people make.
4. **Keep examples runnable or clearly marked as pseudocode.**
5. **No secrets, credentials, or company-specific packs.**
6. **Link, don’t duplicate.** Extend an existing topic or cross-link it.

---

## Version baseline

| Tool | Version used in this repo |
|---|---|
| Java | 17 |
| Maven | 3.9.x |
| Spring Boot | 3.x |
| Selenium | 4.x |
| Playwright | Current Node LTS compatible |
| Node.js | 20+ |
| Cucumber | 7.x |
| TestNG | 7.x |
| RestAssured | 5.x |
| Karate | 1.5.x |
| Azure CLI | Reference commands; install locally when needed |

If a pack uses a different version, it says so in that pack’s README.

---

## Adding a new topic

1. Read [`AGENTS.md`](AGENTS.md) for the required structure and non-negotiables.
2. Create `topics/<topic>/` with a README and 60-second cram.
3. Add theory, examples, Q&A, and exercises.
4. Add the topic to [`CATALOG.md`](CATALOG.md) and the topic map above.
5. Cross-link from at least one related pack.
6. Run the verification steps in `AGENTS.md` if you touched code.
