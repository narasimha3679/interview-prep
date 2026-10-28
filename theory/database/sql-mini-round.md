# Database / SQL — Mini Round for QE Interviews

## Say it right
> "INNER = intersection; LEFT = all of the left plus matches. `WHERE` filters rows before grouping, `HAVING` after. And in test cleanup I only use `DELETE` with a `WHERE` — never `TRUNCATE`/`DROP` on a shared environment."

---

## JOINs
| JOIN | Returns |
|---|---|
| `INNER JOIN` | Only matching rows in **both** tables |
| `LEFT JOIN` | **All** left rows + matches (NULL where none) |
| `RIGHT JOIN` | **All** right rows + matches |
| `FULL JOIN` | Everything from both, NULLs where no match |

> "LEFT JOIN when I need to catch records with no related child row."

## WHERE vs HAVING
```sql
SELECT dept, COUNT(*) AS cnt
FROM employees
WHERE status = 'ACTIVE'          -- filter rows first
GROUP BY dept
HAVING COUNT(*) > 5;             -- filter groups after
```
`WHERE` = before grouping; `HAVING` = after (can use aggregates).

## Find duplicates
```sql
SELECT email, COUNT(*) AS cnt
FROM users
GROUP BY email
HAVING COUNT(*) > 1;
```

## Nth highest salary
```sql
-- MySQL / Postgres (2nd highest)
SELECT DISTINCT salary FROM emp ORDER BY salary DESC LIMIT 1 OFFSET 1;

-- Oracle (older)
SELECT MIN(salary) FROM (SELECT DISTINCT salary FROM emp ORDER BY salary DESC)
WHERE ROWNUM <= 2;
```
Dialects: `LIMIT/OFFSET` (MySQL/Postgres), `ROWNUM`/`FETCH FIRST` (Oracle), `TOP` (SQL Server).

## DELETE vs TRUNCATE vs DROP
| | DELETE | TRUNCATE | DROP |
|---|---|---|---|
| Removes | selected rows | all rows | the **table** |
| WHERE | ✅ | ❌ | ❌ |
| Rollback | ✅ | usually not | no |
| Speed | slow, logged | fast | n/a |

> "Test cleanup: `DELETE ... WHERE ...` — transactional and safe. Never `TRUNCATE`/`DROP` on a shared test env."

## Keys
- **Primary key** → unique + **NOT NULL**, one per table
- **Unique key** → unique, **allows NULL**, many per table
- **Foreign key** → enforces **referential integrity** to another table's PK

## ACID
**Atomicity, Consistency, Isolation, Durability** — a transaction fully completes or fully rolls back.

## What you validate in DB testing
Data type/size, **constraints** (PK, FK, unique, not null), **referential integrity**, defaults, stored-procedure output, **data migration** from legacy.

## Index (common follow-up)
- Speeds up **reads/lookups**, costs **writes** (index must be updated) and storage.
- Ask: "why not index everything?" → write overhead + space.

## Normalization (brief)
- **1NF** no repeating groups · **2NF** no partial dependency on a composite key · **3NF** no transitive dependency.

---

## Memorize
1. **"`WHERE` filters rows before grouping; `HAVING` after. `GROUP BY` + `HAVING COUNT(*) > 1` finds duplicates."**
2. **"INNER = matches only, LEFT = all left + matches. Nth highest: `LIMIT 1 OFFSET N-1`."**
3. **"DELETE = transactional with WHERE; TRUNCATE/DROP never on a shared test env. PK unique+not null; FK = referential integrity."**

## Likely follow-ups
- UNION vs UNION ALL? → deduplicates vs keeps duplicates (faster).
- How do you validate a stored procedure? → call it, then assert the output table/params.
- How do you check referential integrity? → query for orphan FKs (`LEFT JOIN ... WHERE child.fk IS NULL`).
