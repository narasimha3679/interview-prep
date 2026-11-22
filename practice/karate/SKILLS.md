# Karate Skills Matrix

Live view of where I actually am. Updated after every session.

**Last updated:** 2026-11-19 · **Sessions:** 1 · **Next up:** `match` drill answers + non-empty-array code fix

Seeded from `projects/karate/` (Maven + Java 17 + Karate 1.5.2) and the interview topics for API
automation. There is no `topics/karate/` pack yet, so rows come from that project's features and its
`docs/INTERVIEW_GUIDE.md`.

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## Feature anatomy & DSL

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `Feature` / `Background` / `Scenario` layout; no step-definition classes | not assessed | — | Explain why the feature file *is* the test |
| `Given`/`When`/`Then` vs `*` step prefix; `path`/`request`/`method`/`status` steps | not assessed | — | Read `getUsers.feature` line by line |
| Variables: `def`, `set`, embedded `#(...)` inside JSON payloads | not assessed | — | Write a payload with two embedded expressions |
| `configure headers` / `configure logPrettyResponse` | not assessed | — | Attach a token header without repeating it per scenario |

## `match` semantics

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `==` is deep structural equality: extra key fails, key order irrelevant, array order matters | not assessed | — | Predict pass/fail on a 3-line payload pair |
| `contains` / `contains only` / `!contains` / `contains deep` | not assessed | — | Pick the right one for "provider adds fields" |
| Fuzzy markers `#string #number #boolean #null #present #notpresent #ignore #uuid #regex` | not assessed | — | Re-write a full-payload `==` as a schema |
| Array markers: `#[]` = any array **including empty**, `#[N]` = exact size, `#? _` guard | not assessed | — | Fix the non-empty claim in `getUsers.feature` |
| `match each` for per-element assertions | not assessed | — | Assert every element's `id`/`email` in one line |
| Reading Karate's diff: failing path + reason, not just a boolean | not assessed | — | Explain what a failed `match` prints |

## Config & reuse

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `karate-config.js`, `karate.env`, returned variables injected per scenario | not assessed | — | Trace one variable from config to feature |
| `call` vs `callonce` (cache scope: per scenario / per feature) | not assessed | — | Say which one for login |
| `read()` for JSON, schema, and feature files; `@ignore` on callable features | not assessed | — | Explain why helpers are tagged `@ignore` |
| Java interop via `Java.type` for data generation | not assessed | — | Say when DSL is not enough |
| `call` with arguments → returned variables (`createdUserId`) | not assessed | — | Describe the create-user helper contract |

## Data, runners, parallel

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `Scenario Outline` + `Examples`; inline `table` | not assessed | — | Convert a repeated scenario to an outline |
| `Runner.path(...).tags(...).parallel(n)`; failing the build on `getFailCount()` | not assessed | — | Explain why the assertion lives in the runner |
| Tag strategy (`@smoke`, `@regression`, `@ignore`) + Maven profiles | not assessed | — | Map tags to a CI pipeline |
| Parallel pitfalls: shared mutable state, `callonce` scope, rate-limited APIs | not assessed | — | Name three real failure modes |
| Reports: `target/karate-reports/`, `outputCucumberJson` | not assessed | — | Point to the artifact CI publishes |

## Interview framing

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Karate vs Cucumber + RestAssured trade-offs | not assessed | — | 3-line answer, no table |
| Designing a multi-service Karate framework (folders, config, common features) | not assessed | — | Whiteboard the folder tree |
| Karate 1.x vs 2.x (Java 21, virtual threads, no GraalJS) | not assessed | — | One-line answer |

## Not yet assessed

Mocking (`karate-netty` / test doubles) · UI automation · performance testing · GraphQL-over-Karate ·
gRPC · database assertions (`karate.callSingle` / JDBC helper) · contract testing (Pact) ·
auth beyond login-once bearer (OAuth2 refresh, per-role tokens) · CI wiring of this suite.

## Current focus order

1. **`match` semantics** — the most-asked Karate deep-dive, and the one wrong line already in the repo.
2. **`callonce` + config** — the answer to "how do you handle auth/DRY" in every panel.
3. **Runner + tags + parallel** — the CI story, and the questions that follow it.
4. **Interview framing** — Karate vs Cucumber vs RestAssured, said in three lines.

## Recurring traps to re-test every session

- `'#[]'` read as "non-empty array" — it matches `[]` too.
- Whole-payload `==` where a schema/`contains` belongs (breaks on every provider-side field addition).
- `call` where `callonce` belongs (re-login per scenario) or the reverse (stale token shared across tests).
- Forgetting `@ignore` on helper features → helpers run as standalone tests.
- Asserting only `status 200` and calling it a test.
- Saying "we have step definitions" about Karate.
- `parallel(n)` with scenarios that share mutable state or hit rate limits.
