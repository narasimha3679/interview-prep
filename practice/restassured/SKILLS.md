# RestAssured Skills Matrix

Live view of where I actually am. Updated after every session.

**Last updated:** 2026-11-17 · **Sessions:** 1 · **Next up:** request skeleton (body placement) + specs drill

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## Request structure (`given`/`when`/`then`)

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `given`/`when`/`then` split; body in `given()` | shaky | S1 Q1: body in `given()`, method in `when()` correct; `baseurl` typo, 1-arg `header("a: b")`, no `contentType`, empty `then()` | Re-write the chain with contentType + `statusCode(201)` |
| Static imports (`RestAssured.*`, `Matchers.*`) | unknown | S1: `StructureDrillTest` failed to compile — no imports, `RequestSpecification` unresolved | Add the import block from memory |
| `RequestSpecification` reused via `.spec()` | shaky | S1 Q3: described a request-builder helper + env file, never named `RequestSpecification`/`.spec()` | Re-answer using both class names |
| `ResponseSpecification` (`expectStatusCode`, `expectResponseTime`) | not assessed | — | Write one in drill |
| `.extract()` vs inline `body()` assertion | unknown | S1 Q4: "didn't understand" | Re-test after the teaching: assert vs reuse |
| Logging: `.log().all()` vs `.log().ifValidationFails()` | not assessed | — | Explain when each |
| `contentType(ContentType.JSON)`; raw `String` body without it → 415 | unknown | S1 Q1: omitted entirely | Send a `String` body and read the 415 |
| REST Assured vs plain `HttpClient`/`MockMvc` trade-off | not assessed | — | Not in pack; ask later |

## JSONPath, POJOs, schema

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Nested path, index, `size()`, `find`/`findAll` | not assessed | — | Assert `data.roles[0]`, `..name` |
| `jsonPath().getList(path, Class)` / `getMap` | not assessed | — | Extract a list of POJOs |
| POJO (de)serialization: no-arg ctor, getters | not assessed | — | Reuse one POJO both ways |
| `@JsonProperty`, `@JsonIgnoreProperties(ignoreUnknown=true)` | not assessed | — | Add a field, watch it not break |
| JSON Schema: `matchesJsonSchemaInClasspath` | not assessed | — | Contract test on `/users/1` |
| API chaining: extract id → next request | not assessed | — | POST then GET with returned id |

## Auth, specs, config

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `basic` vs `preemptive().basic` (extra 401 round trip) | unknown | S1 Q2: blank | Re-ask cold next session |
| Bearer via `.auth().oauth2(token)` | not assessed | — | Attach to a spec |
| Token caching + refresh on expiry/401 | not assessed | — | Describe the helper |
| Secrets from env/CI, never in code | not assessed | — | Config drill |
| Per-role tokens (user/admin/anonymous) | not assessed | — | 401/403 tests |
| Static `RestAssured.baseURI` vs per-request specs under parallel | not assessed | — | Explain the race |

## Params, status, negative, CRUD

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `pathParam` vs `queryParam` vs `formParam` vs `header`/`cookie` | not assessed | — | Map 4 calls to the right method |
| Status code expectations (201/204/400/401/403/404/409/415/422/429) | not assessed | — | Predict per scenario |
| Error contract: code + message + traceId, not just status | not assessed | — | Negative test drill |
| CRUD lifecycle POST → GET → PUT → DELETE(204) → GET(404) | not assessed | — | Full lifecycle drill |
| DB verification via JDBC `PreparedStatement` + try-with-resources | not assessed | — | Describe, then code |

## File upload and binary

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `multiPart` and why manual `Content-Type` breaks the boundary | shaky | S1 Q5: guessed a form/file mismatch; no boundary mechanism | Re-test: draw the multipart body |
| Round-trip checksum (`extract().asByteArray()` + MD5/SHA-256) | not assessed | — | Explain the killer answer |
| Negative uploads: 415 / 413 / 400 / 401 | not assessed | — | Data-driven file provider |
| Data-driven uploads with `@DataProvider` | not assessed | — | 4-file table |

## Not yet assessed

WireMock/stubbing · contract testing (Pact) · performance (k6/Gatling) · GraphQL over RestAssured ·
OAuth2 flows beyond bearer attachment · CI wiring of the API suite.

## Current focus order

1. **Request skeleton** — body in `given()`, correct `.spec()` reuse; nothing else compiles without it.
2. **Specs + auth attachment** — the "POM of API testing" answer every panel asks for.
3. **JSONPath extraction → POJO → schema** — the extraction half of the pack.
4. **Negative/error contract + CRUD + JDBC** — the bank-QE differentiator.
5. **Uploads** — one round-trip checksum test.

## Recurring traps to re-test every session

- Body placed in `when()` instead of `given()`.
- One-argument `header("Name: value")` (cURL habit; RestAssured needs two arguments).
- Chain ending at an empty `then()` with no assertion.
- Raw `String` body with no `contentType(ContentType.JSON)` → 415.
- "We build a helper class" said instead of `RequestSpecification` + `.spec()`.
- Setting `Content-Type: multipart/form-data` by hand (boundary is the point).
- A method without `@Test` → zero tests run, build stays green.
- `given()`/`when()`/`then()` cited as "arrange-act-assert" without the body placement detail.
- `basic()` described as "sends creds immediately" (it waits for the 401).
- Setting `Content-Type: multipart/form-data` by hand.
- Negative test that asserts only the status code.
- Global `RestAssured.baseURI` mutation in parallel runs.
