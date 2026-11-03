# 06 — GraphQL Exercises

Work through these against a public GraphQL API or a local mock. Write the query, the variables,
and the assertions before running anything.

---

## 1. Query basics

**Task:** Write a query that returns a list of items with `id`, `name`, and a nested `owner { id name }`.
Use a variable for the page size.

**Check:**

- [ ] Query uses `variables`, not string concatenation.
- [ ] Response shape matches the selection.
- [ ] Empty list is handled.
- [ ] Invalid page size is rejected or clamped.

---

## 2. Aliases and fragments

**Task:** In one request, query two different filters of the same field using aliases. Extract the
shared fields into a fragment.

**Check:**

- [ ] Both aliases return independent data.
- [ ] Fragment fields resolve correctly.
- [ ] No duplicate field errors.

---

## 3. Mutation and read-back

**Task:** Write a mutation that creates an item. Then query it back by id.

**Check:**

- [ ] Mutation returns the created id.
- [ ] Read-back returns the same values.
- [ ] Validation errors are structured.
- [ ] Duplicate submission behavior is documented/tested.

---

## 4. Error assertion

**Task:** Query a non-existent id. Assert:

- HTTP status,
- `data` is null/absent,
- `errors[0].extensions.code`,
- `errors[0].path`.

**Check:**

- [ ] You did not assert only HTTP 200.
- [ ] The error code is stable.
- [ ] The message does not leak internals.

---

## 5. Nullability

**Task:** Find a non-null field whose resolver can fail. Force the failure and observe how the error
bubbles.

**Check:**

- [ ] You can explain which parent became null and why.
- [ ] `errors[].path` points to the failing field.
- [ ] Partial data is asserted.

---

## 6. Authorization matrix

**Task:** Pick one query with a sensitive field. Test:

- anonymous,
- owner,
- non-owner,
- admin,
- expired token,
- wrong scope.

**Check:**

- [ ] Unauthorized access is denied or redacted.
- [ ] Errors do not reveal whether the resource exists.
- [ ] Nested fields are checked, not just the top-level query.

---

## 7. Pagination boundaries

**Task:** Test a cursor-paginated connection.

**Check:**

- [ ] First page.
- [ ] Next page.
- [ ] Last page (`hasNextPage=false`).
- [ ] Invalid cursor.
- [ ] No duplicates/gaps across pages.
- [ ] Stable ordering.

---

## 8. N+1 investigation

**Task:** Query 1 parent and then 50 parents with a nested field. Measure latency and, if possible,
count downstream calls.

**Check:**

- [ ] You can explain whether N+1 exists.
- [ ] You know what DataLoader/batching would change.
- [ ] You can propose a CI-friendly assertion (query-count budget or relative threshold).

---

## 9. Schema diff

**Task:** Take two versions of an SDL (or fetch a schema before/after a change). Identify breaking
and non-breaking changes.

**Check:**

- [ ] Removed field = breaking.
- [ ] Added optional field = safe.
- [ ] Nullable → non-null = breaking.
- [ ] New enum value = potentially breaking for exhaustive clients.
- [ ] Deprecated field still works until removal.

---

## 10. CI design

**Task:** Design the GraphQL test stages for a service.

**Check:**

- [ ] Schema diff on every PR.
- [ ] Query/mutation smoke on PR or merge.
- [ ] Error/auth cases included.
- [ ] N+1/cost checks on critical nested queries.
- [ ] Release validation against the deployed environment.
- [ ] Reports and schema artifacts published.
