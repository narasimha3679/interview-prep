# RestAssured for SDETs

API automation with RestAssured: request specs, JSONPath, POJOs, auth, uploads, and negative cases.

## 60-second cram

1. `given()` = request setup incl. body; `when()` = method/endpoint; `then()` = assertions.
2. Specs are the POM of API testing: base URI, headers, timeouts, logging.
3. No assertions in API clients; clients do HTTP, tests assert.
4. JSONPath extracts values; POJOs map bodies; schema validation catches drift.
5. Auth: basic, preemptive, Bearer, API key; keep tokens out of code.
6. `TestContext` shares state between Cucumber steps.
7. Config externalized; system properties override files.
8. Test status, headers, body, schema, and time.
9. Negative/boundary cases find more bugs than happy paths.
10. Validate DB state with JDBC when the API side effect matters.

## File map

| File | Topic |
|---|---|
| [`given-when-then.md`](given-when-then.md) | structure, key methods, specs |
| [`jsonpath-and-pojos.md`](jsonpath-and-pojos.md) | GPath, POJOs, schema |
| [`auth-and-specs.md`](auth-and-specs.md) | basic/Bearer, RequestSpecification |
| [`file-upload.md`](file-upload.md) | multipart, checksum, negatives |
| [`params-status-negative.md`](params-status-negative.md) | params, status, CRUD + JDBC |

## Related

- [`../../projects/restassured-bdd/`](../../projects/restassured-bdd/) — runnable framework
- [`../graphql/`](../graphql/) — GraphQL testing
- [`../spring-boot/`](../spring-boot/) — provider side
- [`../security/`](../security/) — API auth/security
