# GraphQL Testing — Study Pack

How GraphQL differs from REST, what breaks, and how to test queries, mutations, errors, schema, auth,
pagination, and performance.

**Last verified:** November 2026.

---

## 60-second cram

1. **One endpoint, many shapes.** Clients send a `query` document; the server returns exactly the
   fields requested. The test must assert the **shape and values**, not just the status code.
2. **HTTP 200 does not mean success.** GraphQL often returns `200 OK` with an `errors` array and
   partial `data`. Status-only assertions are a classic miss.
3. **Three operation types.** `query` reads, `mutation` writes, `subscription` streams. Test them
   differently.
4. **Variables are the safe input path.** Tests should send `variables`, not string-concatenate
   values into the query.
5. **Nullability is behavior.** A non-null field failure bubbles to the nearest nullable parent;
   the response can have `data: null` plus errors.
6. **N+1 is the performance trap.** A nested query can trigger one resolver call per parent. Test
   query count/latency, not just correctness.
7. **Schema is the contract.** Removing a field, changing a type, or making a nullable field
   non-null is a breaking change. Additive changes are usually safe.
8. **Auth can be field-level.** A user may see a query but not a sensitive field. Test both.

**The one line that shows depth:**

> "REST testing is endpoint-centric; GraphQL testing is query-shape-centric. I assert `data` and
> `errors` separately, validate against the schema, and treat N+1 and field-level authorization as
> first-class test targets."

---

## GraphQL vs REST in one table

| Concern | REST | GraphQL |
|---|---|---|
| Endpoint | Many resources | Usually one `/graphql` |
| Response shape | Server-defined | Client-selected |
| Over/under-fetching | Common | Reduced by selection |
| Status codes | Meaningful | Often `200` even for errors |
| Errors | HTTP status + body | `errors[]` + partial `data` |
| Versioning | URL/header versions | Schema evolution, deprecation |
| Caching | HTTP caching | Harder; persisted queries/field caching |
| Auth | Endpoint/method | Endpoint + field/resolver level |
| Performance risk | N+1 in backend | N+1 exposed by nested queries |
| Contract | OpenAPI | GraphQL schema (SDL) |

---

## File map

| File | What it gives you |
|---|---|
| `01_rest_vs_graphql.md` | Mental model and when to use each |
| `02_queries_mutations.md` | Syntax, variables, fragments, pagination, batching |
| `03_errors_schema_auth.md` | Errors, nullability, schema, auth, limits |
| `04_testing_examples.md` | RestAssured/Karate examples and assertions |
| `05_qa.md` | Questions with model answers |
| `06_exercises.md` | Practice tasks |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Query** | Read operation | Assert selected fields and shape |
| **Mutation** | Write operation | Assert side effect + returned payload |
| **Subscription** | Streaming operation | Test connection, events, reconnect |
| **Resolver** | Function that returns a field | Unit/integration test target |
| **Schema/SDL** | Type system definition | Contract and validation source |
| **Fragment** | Reusable field selection | Test composition and deduplication |
| **Variable** | Parameterized input | Test validation and injection safety |
| **Directive** | Conditional field behavior (`@include`, `@skip`) | Test both branches |
| **Introspection** | Schema self-description | Often disabled in production |
| **Persisted query** | Pre-registered query by hash | Test allowlist and cache behavior |
| **DataLoader** | Batching/caching layer | Prevents N+1; test batch behavior |

---

## Traps

1. **Asserting only HTTP 200.** GraphQL errors often ride on 200.
2. **Ignoring partial data.** A response can have some fields and an `errors` array.
3. **String-building queries.** Use variables; it avoids escaping bugs and injection.
4. **Not testing nullability.** A non-null violation can null the parent object.
5. **Testing only happy-path queries.** Test invalid fields, bad variables, unauthorized fields.
6. **Ignoring N+1.** A nested query can be correct but unusably slow.
7. **Assuming introspection is enabled.** Many production servers disable it.
8. **Forgetting mutations are not automatically idempotent.** Test duplicate submits.
9. **Not testing pagination boundaries.** Empty page, last page, invalid cursor.
10. **Treating schema changes as harmless.** Removing/renaming fields breaks consumers.

---

## How to study this pack

- **10 minutes:** README + cram sheet.
- **30 minutes:** `01` and `02`, write a query and a mutation from memory.
- **30 minutes:** `03`, list the breaking schema changes.
- **30 minutes:** `04`, run the RestAssured example against a public GraphQL API or your own service.
- **30 minutes:** `05`, answer out loud.
- **20 minutes:** `06`, complete the exercises.
