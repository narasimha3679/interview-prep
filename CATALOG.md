# Topic Catalogue

Every study topic in one place: what it is, how deep it goes, how long it takes, and what to know
first.

Use this with [`STUDY_PLAN.md`](STUDY_PLAN.md) and [`PROGRESS.md`](PROGRESS.md).

---

## Foundations

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Java | [`topics/java/`](topics/java/) | Intermediate | 4–6 h | Basic programming |
| Collections | [`topics/collections/`](topics/collections/) | Intermediate | 2–3 h | Java basics |
| Coding drills | [`topics/coding/`](topics/coding/) | Beginner → Intermediate | Ongoing | Java basics |
| SQL | [`topics/sql/`](topics/sql/) | Beginner → Intermediate | 2–3 h | None |
| TypeScript | [`topics/typescript/`](topics/typescript/) | Beginner → Intermediate | 3–4 h | JavaScript basics |

---

## UI automation

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Selenium | [`topics/selenium/`](topics/selenium/) | Intermediate | 3–4 h | Java, HTML basics |
| TestNG | [`topics/testng/`](topics/testng/) | Intermediate | 2 h | Java |
| Cucumber | [`topics/cucumber/`](topics/cucumber/) | Intermediate | 2 h | Java, BDD basics |
| Mobile | [`topics/mobile/`](topics/mobile/) | Intermediate | 3 h | Testing basics |
| Accessibility | [`topics/accessibility/`](topics/accessibility/) | Beginner | 1–2 h | UI basics |

---

## API automation

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| RestAssured | [`topics/restassured/`](topics/restassured/) | Intermediate | 3 h | Java, HTTP |
| GraphQL | [`topics/graphql/`](topics/graphql/) | Intermediate | 2–3 h | HTTP, JSON |
| gRPC | [`topics/grpc/`](topics/grpc/) | Advanced | 2 h | HTTP, Protobuf basics |

---

## Backend and architecture

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Spring Boot | [`topics/spring-boot/`](topics/spring-boot/) | Intermediate | 6–8 h | Java, HTTP, SQL |
| Microservices theory | [`topics/microservices/`](topics/microservices/) | Intermediate | 3–4 h | Backend basics |
| Observability | [`topics/observability/`](topics/observability/) | Intermediate | 2–3 h | Backend basics |

---

## Delivery and cloud

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| CI/CD | [`topics/ci-cd/`](topics/ci-cd/) | Intermediate | 3–4 h | Git, CLI |
| Cloud | [`topics/cloud/`](topics/cloud/) | Beginner → Intermediate | 4–6 h | Networking basics |
| Azure | [`topics/cloud/azure/`](topics/cloud/azure/) | Beginner → Intermediate | 3–4 h | Cloud basics |
| Containers | [`topics/cloud/containers/`](topics/cloud/containers/) | Intermediate | 3 h | CLI, Linux basics |
| Performance | [`topics/performance/`](topics/performance/) | Intermediate | 3 h | HTTP, metrics |

---

## Quality engineering

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Test strategy | [`topics/test-strategy/`](topics/test-strategy/) | Beginner → Advanced | 3–4 h | Testing basics |
| Security | [`topics/security/`](topics/security/) | Intermediate | 3 h | HTTP, auth basics |
| AI systems testing | [`topics/ai-testing/`](topics/ai-testing/) | Intermediate | 3–4 h | Testing basics |
| AI-assisted testing | [`topics/ai-assisted-testing/`](topics/ai-assisted-testing/) | Beginner | 1–2 h | None |

---

## Career

| Topic | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Interview prep | [`topics/interview/`](topics/interview/) | Beginner | Ongoing | Repo familiarity |

---

## Runnable projects

| Project | Path | Level | Time | Prereqs |
|---|---|---|---|---|
| Selenium BDD | [`projects/selenium-bdd/`](projects/selenium-bdd/) | Intermediate | 2–3 h | Java, Maven |
| Playwright | [`projects/playwright/`](projects/playwright/) | Intermediate | 2–3 h | Node, TypeScript |
| RestAssured BDD | [`projects/restassured-bdd/`](projects/restassured-bdd/) | Intermediate | 2–3 h | Java, Maven, HTTP |
| Karate | [`projects/karate/`](projects/karate/) | Intermediate | 2 h | Java, Maven, HTTP |
| Spring Boot mini-service | [`topics/spring-boot/examples/mini-service/`](topics/spring-boot/examples/mini-service/) | Intermediate | 1–2 h | Java, Spring |

---

## Study system

| File | Purpose |
|---|---|
| [`STUDY_PLAN.md`](STUDY_PLAN.md) | Time-boxed plans and revision cadence |
| [`PROGRESS.md`](PROGRESS.md) | Checklist and confidence tracker |
| [`CHEATSHEET.md`](CHEATSHEET.md) | 180 high-signal facts |
| [`EXERCISES.md`](EXERCISES.md) | Cross-topic practice bank |
| [`CAPSTONE.md`](CAPSTONE.md) | End-to-end build spec |
| [`GLOSSARY.md`](GLOSSARY.md) | Cross-topic terms |
| [`AGENTS.md`](AGENTS.md) | Repository conventions |

---

## Suggested order

This is a **reference order**, not an interview plan. Skip tracks the role does not need. Use
[`STUDY_PLAN.md`](STUDY_PLAN.md) to scope the role first.

If you are starting from zero:

1. `topics/test-strategy/` — how to think about testing.
2. `topics/java/` + `topics/collections/` + `topics/coding/` — language base.
3. `topics/restassured/` + `topics/graphql/` — API automation.
4. `topics/selenium/` or `topics/typescript/` + `projects/playwright/` — UI automation.
5. `topics/spring-boot/` + `topics/microservices/` — backend view.
6. `topics/ci-cd/` + `topics/cloud/` — delivery.
7. `topics/observability/`, `topics/performance/`, `topics/security/` — non-functional.
8. `CAPSTONE.md` — combine everything.
9. `topics/interview/` + `CHEATSHEET.md` — rehearse.
