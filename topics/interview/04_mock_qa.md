# 04 — Mock Q&A

Cover the answer, say your version out loud, then compare.

---

## Opening

**Q1. Tell me about yourself.**
> "I'm an SDET who builds automation frameworks rather than one-off tests. I've built API frameworks
> in Java with RestAssured and Karate, UI frameworks in Selenium and Playwright, and a small Spring
> Boot service so I understand the provider side too. The common thread is engineering: externalized
> config, isolated parallel-safe tests, clear layering, and CI with evidence. I like making testing
> fast and trustworthy for other engineers."

**Q2. Why this role?**
> "Because the part I enjoy most is building the thing other people use — the framework, the
> tooling, the fast feedback loop. I also want to work closer to backend services and cloud delivery,
> where the interesting failure modes are."

**Q3. What's your biggest weakness?**
> "I haven't run a large production microservices estate myself. I've built a small Spring Boot
> service and test harnesses, and I understand the architecture from the consumer side. I'm closing
> the gap by building service-level examples and learning the operational side — deployment,
> observability, and failure handling."

---

## Technical

**Q4. How do you design a test framework?**
> "Start with the users and the golden path. Layers: config, transport/clients, page objects/API
> clients, tests, data, reporting, CI. Opinionated defaults with escape hatches. Externalized config,
> isolated parallel-safe state, actionable failure messages, and reports that link to evidence. Ship
> it as a versioned artifact with docs and examples."

**Q5. How do you make tests parallel-safe?**
> "No shared mutable state. ThreadLocal drivers or per-test browser contexts, unique data per test,
> isolated DB/schema/namespace, no order dependence, and cleanup. In Selenium, remove the ThreadLocal
> on quit; in Playwright, each test gets a fresh context."

**Q6. How do you handle flaky tests?**
> "Detect per test, quarantine to a non-blocking job, assign an owner and deadline, diagnose the
> non-determinism, fix it, and return after a stability window. Retries are a detector, not a fix."

**Q7. How do you decide what to automate?**
> "High-value, repetitive, deterministic checks. Keep exploratory, usability, and one-off judgment
> manual. Automate at the cheapest level that can catch the risk."

**Q8. How do you test an API?**
> "Contract first: status, schema, auth, validation, error model, pagination, idempotency. Then
> behavior: happy path, boundaries, negative, partial failure. Then non-functional: latency, rate
> limits, resilience. Use API tests as the workhorse; UI only for UI-specific risk."

**Q9. How do you test a microservice?**
> "Unit for logic, component with Testcontainers for real infra, contract for interfaces, API for
> behavior, and a few E2E journeys. Plus async, resilience, and observability."

**Q10. How do you test async flows?**
> "Trigger, poll for eventual state with a timeout, assert payload/schema, test duplicates for
> idempotency, and verify the DLQ. Never assert immediately."

**Q11. How do you test a database?**
> "Repository/component tests with the real engine via Testcontainers, migrations, constraints,
> transactions, and query plans. H2 is not production parity."

**Q12. How do you test security?**
> "Auth matrix: anonymous, owner, non-owner, admin, tenant. Input validation, mass assignment,
> rate limits, secrets, dependency scan. Automate the repeatable checks; use manual/pen-test for
> business logic."

**Q13. How do you test performance?**
> "Start from the SLO, model realistic traffic, run in an isolated production-like environment,
> collect client and server metrics, assert p95/p99 and error rate, and find the first bottleneck."

**Q14. How do you test a UI?**
> "Stable role/test-id locators, auto-waiting, isolated context, API setup for state, and UI only for
> UI-specific risk. Traces/screenshots on failure."

**Q15. How do you test mobile?**
> "API/component tests for logic, Appium for native/hybrid critical journeys, Playwright for mobile
> web, real devices for release confidence. Focus on lifecycle, permissions, network, deep links,
> push, and device matrix."

**Q16. How do you test GraphQL?**
> "Schema diff, operation tests with variables/fragments/aliases, `data` and `errors` separately,
> nullability, auth, pagination, N+1/cost, subscriptions. HTTP 200 is not success."

**Q17. How do you test a release?**
> "Build once, deploy the same artifact to staging/canary, validate health/auth/migrations/critical
> APIs/async/UI smoke, check metrics, then promote or roll back. Rollback is tested, not assumed."

**Q18. How do you test in the cloud?**
> "Provision isolated environments from IaC, use managed identity/secrets, test network boundaries,
> quotas, managed-service behavior, and observability. Emulators for the fast loop; real cloud for
> release validation."

**Q19. How do you debug a CI-only failure?**
> "Artifacts first: report, logs, trace, screenshot/video, correlation ID. Check environment/version,
> data, order, and network. Reproduce, classify product/test/data/env/dependency, then fix at the
> right level."

**Q20. How do you keep a suite fast?**
> "Mostly unit/component/contract, few E2E, parallel independent jobs, sharding, API setup instead of
> UI setup, dependency caching, and slow tests nightly."

---

## Behavioral

**Q21. Tell me about a time you improved quality.**
Use the reliability story from `01_behavioral_star.md`.

**Q22. Tell me about a disagreement.**
Use the contract-vs-E2E story.

**Q23. Tell me about a mistake.**
Use the shared-default/compatibility story.

**Q24. Tell me about ambiguity.**
Use the thin-slice/feedback story.

**Q25. Why should we hire you?**
> "Because I combine framework engineering with testing judgment. I can build the automation, but I
> also know what not to automate, where the risk is, and how to make the result trustworthy in CI."

---

## Curveballs

**Q26. You don't have experience with X. Why are you here?**
> "I haven't done X directly. The underlying skills transfer — isolation, contracts, CI, debugging.
> Here's how I'd reason about X, and here's what I'd learn first."

**Q27. We don't have a framework team. Is that a problem?**
> "No. The same skills apply: make the right thing easy, reduce flakiness, and give teams fast
> feedback. I'd focus on the highest-pain area first."

**Q28. What would you do in your first 30 days?**
> "Understand the users and the pain, audit the current suite and pipeline, fix the highest-pain
> reliability issue, and ship one visible improvement with docs."

**Q29. What's your testing philosophy?**
> "Risk-based, cheapest effective level, fast feedback, evidence over assertions, and flakiness is a
> product defect."

**Q30. Any questions for us?**
Use `05_questions_to_ask.md`.
