# SQL & DB Validation Skills Matrix

Live view of where I actually am. Updated after every session.

**Last updated:** 2026-11-17 · **Sessions:** 1 (drill outstanding) · **Next up:** finish `DbChecks.findStatus` → polling + cleanup assertions

Topic family: SQL for SDETs, applied to **DB validation inside API tests**. Seeded from
`topics/sql/` (queries) and `topics/java/jdbc-db-validation.md` (JDBC mechanics).

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## API → DB proof

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Status code is not persistence — add the DB assertion after 2xx | not assessed | — | Green run of `AccountPersistenceTest` |
| Compare the DB row to the API response, not to a hardcoded literal | not assessed | — | Extract the id, assert the same value |
| What to verify: values, counts, statuses, timestamps | not assessed | — | List them for one create endpoint |
| When *not* to hit the DB (pure contract/UI tests) | not assessed | — | One-line rule |

## JDBC mechanics

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| 6 steps: `getConnection` → `prepareStatement` → bind → `executeQuery` → `next()` → close | not assessed | — | Implement `DbChecks.findStatus` |
| try-with-resources closes `ResultSet` → `Statement` → `Connection` | not assessed | — | Explain what happens on an exception mid-iteration |
| `PreparedStatement` vs `Statement`: SQL injection + precompile | not assessed | — | Session 1 Q2 |
| Typed reads: `getString` / `getInt` / `getTimestamp().toLocalDateTime()` | not assessed | — | Read the `created_at` column |
| Multi-row → `List<Map<String,Object>>` or a POJO | not assessed | — | Map the three COMPLETED orders |
| Pool (HikariCP) + credentials from env, not source | not assessed | — | Describe the framework shape |

## Async writes and test data hygiene

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Eventual consistency → poll with a deadline, not `Thread.sleep` | not assessed | — | Session 1 Q4 |
| Timeout message shows the last observed state | not assessed | — | Make a failure readable |
| Cleanup: `DELETE ... WHERE id = ?`, assert the row count | not assessed | — | `AccountPersistenceTest` drill 2 |
| Never `TRUNCATE` / `DROP` / unscoped `DELETE` on a shared env | not assessed | — | Session 1 Q5 |
| Seed once per class vs reset per test — trade-off | not assessed | — | Explain both |

## SELECT correctness (the panel queries)

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `WHERE` vs `HAVING` | not assessed | — | Session 1 Q3 |
| `COUNT(*)` vs `COUNT(column)` under `LEFT JOIN` | not assessed | — | Count empty departments |
| `NULL`: `IS NULL`, `NOT IN` + `NULL` | not assessed | — | Two queries |
| `INNER` vs `LEFT`; anti-join with `NOT EXISTS` | not assessed | — | "employees with no orders" |
| Duplicates: `GROUP BY` + `HAVING COUNT(*) > 1`, then the full rows | not assessed | — | `topics/sql/03_qa.md` Q13–14 |
| Nth highest; `TOP ... WITH TIES`; `RANK` vs `ROW_NUMBER` | not assessed | — | `03_qa.md` Q7–8 |
| `ROW_NUMBER() OVER (PARTITION BY ...)` for latest-per-group | not assessed | — | `03_qa.md` Q18 |
| PK vs unique vs FK | not assessed | — | One line each |

## Transactions, indexes, plans

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Isolation levels; lost update; deadlock retry | not assessed | — | Default level and one failure story |
| Sargable predicates — a function on the column kills the seek | not assessed | — | Rewrite `YEAR(HireDate) = 2025` |
| Composite index leftmost prefix | not assessed | — | `(a,b)` vs `(b,a)` |
| Execution plan: table scan vs index seek | not assessed | — | Read one plan |
| Keyset vs `OFFSET` pagination | not assessed | — | Explain the skipped/duplicated row |

## Not yet assessed

JDBC in CI against a containerised DB · test-data factories and teardown strategies · SQL injection
testing beyond `PreparedStatement` · `@Sql`/Flyway seeding · NoSQL assertions · read-replica lag.

## Current focus order

1. **`DbChecks.findStatus` + `deleteById`** — nothing else runs until the JDBC loop is muscle memory.
2. **Polling + cleanup assertions** — the async and hygiene halves of the same test.
3. **`WHERE`/`HAVING`, `COUNT(column)`, `NOT EXISTS`** — the three that separate "wrote a query" from
   "understands a query".
4. **Nth-highest / latest-per-group windows** — the panel favourites.
5. **Indexes, plans, isolation** — depth for a senior panel, not day one.

## Recurring traps to re-test every session

None yet — first session, nothing graded. Add one line per wrong answer as it happens.
