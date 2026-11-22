# sql-validation — prove the write, not just the status code

Drill project for the *DB half* of an API test: call the endpoint, then read the row with plain
JDBC. No database install, no credentials, no shared environment.

## Why it exists

`then().statusCode(201)` proves the API *accepted* the request. It does not prove the row exists,
has the right status, or that anything was committed. This project makes that gap visible: the
embedded API answers `201` and only writes to H2 **300 ms later**, exactly like an async service.

## Run

```powershell
cd projects/sql-validation
mvn -o -Dtest=AccountPersistenceTest test
```

Green here is the acceptance check for the drill in
`practice/sql/sessions/2026-11-17.md`.

## What is given, what is yours

| File | Status |
|---|---|
| `src/main/java/com/sqlval/db/Db.java` | given — H2 URL, user, `open()` |
| `src/test/java/com/sqlval/app/AccountApi.java` | given — embedded HTTP API, async insert |
| `src/test/java/com/sqlval/support/Schema.java` | given — `accounts` + `orders`, seed rows, `reset()` |
| `src/test/java/com/sqlval/support/DbChecks.java` | **yours** — `findStatus`, `deleteById` (`waitForStatus` given) |
| `src/test/java/com/sqlval/tests/AccountPersistenceTest.java` | **yours** — two tests, bodies are TODO |

The two `@Test` methods currently fail with a `TODO:` message on purpose. They fail loudly so a
green run means something.

## Seed data

`accounts`: `(1, 'seed-user', 'ACTIVE')`

`orders` (`id, employee_id, amount, status`):

| id | employee_id | amount | status |
|---|---|---|---|
| 101 | 7 | 250.00 | COMPLETED |
| 102 | 7 | 120.50 | COMPLETED |
| 103 | 7 | 900.00 | CANCELLED |
| 104 | 8 | 400.00 | COMPLETED |
| 105 | 8 | 50.00 | PENDING |
| 106 | NULL | 75.00 | COMPLETED |

## Notes

H2 runs in `MODE=MSSQLServer`, so `TOP`, `BIT` and `IDENTITY` behave like the T-SQL pack in
[`../../topics/sql/`](../../topics/sql/) — but window functions and execution plans are H2's, not
SQL Server's. For plan and isolation depth, read the topic pack.
