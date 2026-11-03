# Study Plan Builder

This is **not** a fixed curriculum and **not** a plan for one interview. It is a way to build a plan
for whatever SDET role you are preparing for.

The repository covers many tracks. You are not supposed to study all of them. Pick the tracks the
role needs, set the depth, and timebox the work.

---

## 1. Scope the interview

Before opening a topic pack, fill this in:

| Question | Your answer |
|---|---|
| Role focus | UI / API / backend / full-stack SDET / quality platform / other |
| Must-have skills | |
| Nice-to-have skills | |
| Explicitly not needed | |
| Coding round? | Yes / No / Unknown |
| System design or test strategy round? | Yes / No / Unknown |
| Cloud/CI/CD in scope? | Yes / No / Unknown |
| Time available | Hours/days |
| Weakest area | |

**Rule:** if a topic is not in scope, skip it. Do not study it “just in case” unless you have spare
time and genuine interest.

---

## 2. Choose tracks

| Track | Topic packs | Use when |
|---|---|---|
| **Foundations** | `topics/java/`, `topics/collections/`, `topics/coding/`, `topics/sql/` | Coding round, Java role, backend-adjacent |
| **UI** | `topics/selenium/`, `topics/testng/`, `topics/cucumber/`, `topics/typescript/`, `topics/mobile/`, `topics/accessibility/`, `projects/selenium-bdd/`, `projects/playwright/` | UI automation role |
| **API** | `topics/restassured/`, `topics/security/`, `projects/restassured-bdd/`, `projects/karate/`, plus GraphQL/gRPC **only if in scope** | API automation role |
| **Backend** | `topics/spring-boot/` or the role’s backend stack, `topics/observability/`, plus `topics/microservices/` **only if service-heavy** | Backend-aware SDET, service testing |
| **Delivery** | `topics/ci-cd/`, `topics/cloud/` + the provider in scope, plus containers/performance **only if in scope** | CI/CD, cloud, release validation |
| **Quality** | `topics/test-strategy/`, `topics/security/`, `topics/accessibility/` | Any senior SDET role |
| **AI** | `topics/ai-testing/`, `topics/ai-assisted-testing/` | AI product or AI-assisted testing role |
| **Interview** | `topics/interview/`, `CHEATSHEET.md` | Final rehearsal |

A role usually needs **2–3 tracks**, not all seven.

---

## 3. Choose depth

| Depth | What it means | Time |
|---|---|---|
| **Review** | Read the cram, answer Q&A, skim examples | 30–60 min |
| **Working** | Read theory, run examples, do one exercise | 2–4 h |
| **Knowledge** | Can explain trade-offs and design decisions | 4–8 h |
| **Build** | Use it in a project or capstone | 1–2 days |

Match depth to the interview. A UI role may need “Working” UI and only “Review” cloud. An API role
may need “Working” API and “Review” UI.

---

## 4. Build the timebox

Use the templates below as shapes, not prescriptions. Replace the sample tracks with the tracks you
actually need.

---

## 30-minute revision

1. Read one section of [`CHEATSHEET.md`](CHEATSHEET.md).
2. Answer five questions from one `03_qa.md`.
3. Say your 2-minute project pitch.

---

## 1-hour session

| Time | Do this |
|---|---|
| 10 min | Read the topic’s 60-second cram |
| 20 min | Read the theory or run one example |
| 20 min | Answer Q&A out loud |
| 10 min | Do one task from [`EXERCISES.md`](EXERCISES.md) |

One topic. One session. Do not context-switch.

---

## 1-day plan (single track)

Pick **one** track and go deep enough to explain it.

| Block | Activity |
|---|---|
| 1h | Scope the role + read [`CATALOG.md`](CATALOG.md) |
| 2h | Track theory: 2–3 topic packs |
| 2h | Run the matching project or exercise |
| 1h | Test strategy / system design for that track |
| 1h | Q&A + project pitch |
| 1h | [`CHEATSHEET.md`](CHEATSHEET.md) + weak spots |

### Sample: UI track

- `topics/selenium/`, `topics/testng/`, `topics/typescript/`
- `projects/selenium-bdd/` or `projects/playwright/`
- `topics/test-strategy/` for flakiness and test levels
- `topics/accessibility/` if the role touches it

### Sample: API track

- `topics/restassured/`
- `topics/graphql/` or `topics/grpc/` only if the role uses it
- `topics/security/` for auth and API risks
- `projects/restassured-bdd/` or `projects/karate/`

### Sample: Backend track

- `topics/java/`, `topics/sql/`
- `topics/spring-boot/`
- `topics/microservices/` if the role is service-heavy
- `topics/observability/`
- Run the mini-service

### Sample: Delivery/cloud track

- `topics/ci-cd/`
- `topics/cloud/` + the provider in scope
- `topics/cloud/containers/`
- `topics/performance/`
- Release-validation checklist

### Sample: Quality track

- `topics/test-strategy/`
- `topics/security/`
- `topics/accessibility/`
- `topics/interview/` for system-design answers

---

## 3-day plan

### Day 1 — Foundations and scope

- Scope the role using the worksheet.
- Review the language/tool base the role needs.
- Read `topics/test-strategy/01_test_levels.md`.
- Answer 10 Q&A.

### Day 2 — Primary track

- Go deep on the main track (UI, API, backend, or delivery).
- Run one project or a full exercise.
- Practice one system-design answer for that track.

### Day 3 — Secondary track + rehearsal

- Review the second track at “Review” depth.
- Do a mock interview or a capstone slice.
- Read `CHEATSHEET.md` and rehearse project stories.

---

## 1-week plan

| Day | Focus |
|---|---|
| 1 | Scope + foundations/coding |
| 2 | Primary track theory |
| 3 | Primary track project/exercise |
| 4 | Secondary track (review depth) |
| 5 | Test strategy + system design |
| 6 | CI/CD, cloud, or non-functional topic **only if in scope** |
| 7 | Mock interview + cheat sheet + project stories |

Skip any day that is not relevant. Replace it with more practice on the primary track.

---

## 2-week plan

### Week 1 — Primary track

- Days 1–2: foundations and language/tool base
- Days 3–4: primary track theory
- Days 5–6: primary track project/exercise
- Day 7: review + 20 Q&A

### Week 2 — Secondary track and system view

- Days 8–9: secondary track
- Days 10–11: test strategy, system design, release validation (if in scope)
- Day 12: security/performance/observability (only if in scope)
- Day 13: capstone slice
- Day 14: full mock + cheat sheet + project stories

---

## Revision cadence

| When | What |
|---|---|
| Same day | Write 3 bullets from memory |
| Next day | Answer 5 Q&A without notes |
| Day 3 | Re-run the example/exercise |
| Day 7 | Explain the topic or record yourself |
| Day 14 | Use it in a project or mock answer |

---

## Stop rules

Move on when you can:

- explain the concept in one minute;
- name the tool and the method;
- state one trap;
- give one project example;
- answer three Q&A without notes.

Do not move on if you only recognize the words.

---

## When a new topic appears

If an interview adds a topic that is not covered:

1. Check `CATALOG.md` first — it may already exist.
2. If missing, ask for a new topic pack.
3. Add it to `topics/`, `CATALOG.md`, `topics/README.md`, and `README.md`.
4. Do **not** rewrite the whole study plan around that interview.
5. Add the new topic as an optional track, not the default path.

The repository grows by adding topics. It should not become a different repository for every
interview.

---

## Track it

Use [`PROGRESS.md`](PROGRESS.md) to mark what you have read, run, answered, and practiced.
