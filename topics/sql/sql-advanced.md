# SQL — Advanced Topics for Testers

## 1. Window functions

```sql
SELECT
  seller_id,
  listing_id,
  price,
  ROW_NUMBER() OVER (PARTITION BY seller_id ORDER BY price DESC) AS rn,
  RANK()       OVER (PARTITION BY seller_id ORDER BY price DESC) AS rnk,
  SUM(price)   OVER (PARTITION BY seller_id) AS seller_total
FROM listings;
```

Use for:

- Top N per group (`rn = 1`).
- Running totals.
- Percentiles/median.
- Deduplication (keep the latest row per key).

**Test use:** verify "top 3 listings per seller" or "latest order per user" without self-joins.

---

## 2. CTEs (WITH)

```sql
WITH paid_orders AS (
  SELECT * FROM orders WHERE status = 'PAID'
),
totals AS (
  SELECT user_id, SUM(amount) AS total
  FROM paid_orders
  GROUP BY user_id
)
SELECT * FROM totals WHERE total > 1000;
```

Benefits: readable, testable chunks. Recursive CTEs handle hierarchies.

---

## 3. Indexes

| Index | Use |
|---|---|
| **B-tree** | Equality/range/order |
| **Hash** | Equality only |
| **Composite** | Multi-column filters; column order matters |
| **Covering** | Includes selected columns |
| **Partial** | Subset of rows |
| **Full-text** | Text search |

Rules:

- Index filters/joins/sorts, not every column.
- Composite index leftmost prefix matters.
- Too many indexes slow writes.
- Check the query plan, not assumptions.

**Test use:** compare query plans/latency before/after an index change; verify pagination queries use
the index.

---

## 4. Query plans

```sql
EXPLAIN ANALYZE
SELECT * FROM listings WHERE seller_id = 's1' ORDER BY created_at DESC LIMIT 20;
```

Look for:

- Sequential scan vs index scan.
- Estimated vs actual rows.
- Nested loop vs hash/merge join.
- Sort/hash spills.
- High cost nodes.

**Test use:** catch a missing index or N+1 before production.

---

## 5. Transactions and isolation

| Level | Dirty read | Non-repeatable read | Phantom read |
|---|---|---|---|
| Read uncommitted | Yes | Yes | Yes |
| Read committed | No | Yes | Yes |
| Repeatable read | No | No | Yes* |
| Serializable | No | No | No |

*Varies by database (Postgres uses MVCC).

Test:

- Concurrent update conflict.
- Lost update prevention.
- Deadlock behavior/retry.
- Isolation level matches the business requirement.

---

## 6. Locking

```sql
SELECT * FROM listings WHERE id = 1 FOR UPDATE;
```

- Row locks vs table locks.
- Optimistic locking (`version` column) vs pessimistic.
- Deadlocks: consistent ordering, short transactions, retry.

**Test use:** concurrent checkout/order updates; assert one winner and correct state.

---

## 7. Pagination

```sql
-- Offset
SELECT * FROM listings ORDER BY created_at DESC LIMIT 20 OFFSET 40;

-- Keyset (cursor)
SELECT * FROM listings
WHERE (created_at, id) < (:lastCreatedAt, :lastId)
ORDER BY created_at DESC, id DESC
LIMIT 20;
```

Keyset is stable under inserts; offset can skip/duplicate rows when data changes.

**Test:** first/last page, inserts between pages, stable ordering, no gaps/duplicates.

---

## 8. NULL semantics

- `NULL = NULL` is unknown, not true.
- Use `IS NULL` / `IS NOT NULL`.
- `COUNT(column)` ignores NULL; `COUNT(*)` counts rows.
- `NOT IN` with NULL can return no rows.
- `COALESCE`/`NULLIF` for defaults.

**Test:** missing optional fields, empty strings vs NULL, aggregation with NULLs.

---

## 9. Dates and time zones

- Store UTC; convert at the boundary.
- `TIMESTAMP` vs `TIMESTAMPTZ`.
- Daylight saving transitions.
- Date boundaries (midnight, month end, leap year).

**Test:** time zone conversion, DST, "today" queries, expiry.

---

## 10. Duplicates

```sql
-- Find duplicate emails
SELECT email, COUNT(*)
FROM users
GROUP BY email
HAVING COUNT(*) > 1;

-- Keep latest row per key
WITH ranked AS (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY email ORDER BY created_at DESC) AS rn
  FROM users
)
SELECT * FROM ranked WHERE rn = 1;
```

**Test:** uniqueness constraints, idempotency, dedup logic.

---

## 11. Aggregation

```sql
SELECT seller_id,
       COUNT(*) AS listings,
       AVG(price) AS avg_price,
       SUM(price) AS total
FROM listings
GROUP BY seller_id
HAVING COUNT(*) > 10;
```

- `WHERE` filters rows before grouping.
- `HAVING` filters groups after aggregation.
- `COUNT(*)` vs `COUNT(column)`.
- Integer division/rounding.

**Test:** totals, averages, currency rounding, filters.

---

## 12. SQL testing checklist

- [ ] Joins return expected cardinality.
- [ ] Aggregations match business totals.
- [ ] NULL semantics tested.
- [ ] Duplicates/uniqueness tested.
- [ ] Pagination stable at boundaries.
- [ ] Time zones/DST tested.
- [ ] Index/query plan checked for critical queries.
- [ ] Transactions/concurrency tested.
- [ ] Injection parameterized.
- [ ] Test data isolated.
