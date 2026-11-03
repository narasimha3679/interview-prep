# Runnable Projects

The repository’s hands-on projects. Use these after the matching theory pack; each one is small
enough to run locally and explain end to end.

---

## Project map

| Project | Path | Stack | What it proves |
|---|---|---|---|
| Selenium BDD | [`projects/selenium-bdd/`](projects/selenium-bdd/) | Java 17, Selenium 4, Cucumber, TestNG | UI framework, POM, waits, parallel-safe driver, Extent/Cucumber reports, CI |
| Playwright | [`projects/playwright/`](projects/playwright/) | TypeScript, Playwright Test | Fixtures, POM, cross-browser, traces, CI |
| RestAssured BDD | [`projects/restassured-bdd/`](projects/restassured-bdd/) | Java 17, RestAssured, Cucumber, TestNG | API layering, specs, POJOs, context, config |
| Karate | [`projects/karate/`](projects/karate/) | Java 17, Karate | API BDD without glue, callable features, schema match, runners |
| Spring Boot mini-service | [`topics/spring-boot/examples/mini-service/`](topics/spring-boot/examples/mini-service/) | Java 17, Spring Boot 3, JPA, H2 | Controller/service/repository, validation, errors, MockMvc tests |

---

## Run commands

```powershell
# Selenium BDD
cd "projects/selenium-bdd"; mvn test

# Playwright
cd "projects/playwright"; npm ci; npx playwright test

# RestAssured BDD
cd "projects/restassured-bdd"; mvn test

# Karate
cd "projects/karate"; mvn test

# Spring Boot mini-service
cd "topics/spring-boot/examples/mini-service"; mvn test
```

Some projects call public APIs or public demo sites. Network access is required for those.

---

## Recommended order

1. **Spring Boot mini-service** — see the provider side first.
2. **RestAssured BDD** — test an API with a familiar Java stack.
3. **Karate** — see the no-glue API style.
4. **Selenium BDD** — UI framework with explicit waits and reporting.
5. **Playwright** — modern UI framework with isolation and traces.

Then use [`CAPSTONE.md`](CAPSTONE.md) to combine the service, API tests, UI tests, CI, and cloud.

---

## What to be able to explain

For each project:

- **Problem:** what it replaces or demonstrates.
- **Shape:** layers and key components.
- **Decisions:** 2–3 trade-offs you made.
- **Evidence:** CI, reports, reliability, runtime.
- **Next:** what you would improve.

Use [`topics/interview/03_project_stories.md`](topics/interview/03_project_stories.md) for the
2-minute pitch.

---

## Project hygiene

- Do not commit `target/`, `node_modules/`, reports, or auth state.
- Keep credentials in environment variables or CI secrets.
- Pin dependency versions.
- Keep one clear command for “run the suite”.
- Publish reports and failure artifacts in CI.
- Add a README section for “what this proves”.
