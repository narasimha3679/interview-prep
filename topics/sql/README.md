# SQL for SDETs

Query skills for API/database validation and data-heavy test scenarios. The interview query answers
use **Microsoft SQL Server (T-SQL)** as their base dialect.

## 60-second cram

1. `WHERE` filters rows; `HAVING` filters groups.
2. `COUNT(*)` counts rows; `COUNT(column)` ignores NULL.
3. `NULL = NULL` is unknown; use `IS NULL`.
4. `NOT IN` with NULL can return no rows.
5. Window functions handle top-N-per-group, running totals, and deduplication.
6. Keyset pagination is stable under inserts; offset pagination can skip/duplicate.
7. Composite index leftmost prefix matters.
8. Inspect the actual execution plan for critical SQL Server queries.
9. Know isolation levels and lost-update/deadlock behavior.
10. Parameterize queries; never concatenate input.

## File map

| File | Topic |
|---|---|
| [`03_qa.md`](03_qa.md) | 20 common, easy SQL Server “find this” interview queries with answers |
| [`sql-mini-round.md`](sql-mini-round.md) | JOINs, HAVING, duplicates, nth highest, ACID |
| [`sql-advanced.md`](sql-advanced.md) | window functions, CTEs, indexes, query plans, isolation |

## Practice

Use [`../coding/sql/`](../coding/sql/) for query drills and a sample schema.

## Related

- [`../java/jdbc-db-validation.md`](../java/jdbc-db-validation.md) — JDBC validation
- [`../spring-boot/03_persistence.md`](../spring-boot/03_persistence.md) — JPA/transactions
- [`../test-strategy/03_test_data_environments.md`](../test-strategy/03_test_data_environments.md) — test data
