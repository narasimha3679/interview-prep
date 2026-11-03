# Java — JDBC / DB Validation

## Say it right
> "For DB validation I use plain **JDBC** — `DriverManager.getConnection()`, a **`PreparedStatement`**, `executeQuery()`, read the **`ResultSet`**, and close everything with try-with-resources. Then I compare the DB row against the API response."

---

## The 6 JDBC steps
1. Get a `Connection`
2. Create a `PreparedStatement` with `?` placeholders
3. Bind parameters
4. `executeQuery()`
5. Iterate with `while (rs.next())`
6. Close in reverse order (ResultSet → Statement → Connection)

---

## Code
```java
public String getAccountStatus(String id) {
    String sql = "SELECT status FROM accounts WHERE id = ?";

    try (Connection con = DriverManager.getConnection(
             ConfigManager.get("db.url"), dbUser, dbPass);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString("status");   // getInt / getTimestamp also
        }
    } catch (SQLException e) {
        throw new TestDataException("DB query failed", e);
    }
    return null;
}
```

In a test:
```java
given().spec(reqSpec).body(account).when().post("/accounts").then().statusCode(201);
assertEquals("ACTIVE", dbUtil.getAccountStatus(account.getId()));   // proves persistence
```

---

## Key points they probe
- **`PreparedStatement` over `Statement`** → parameterized ⇒ prevents **SQL injection**, precompiled/faster. Always say this.
- **try-with-resources** auto-closes `Connection`/`Statement`/`ResultSet` (all `AutoCloseable`).
- **Typed reads** → `rs.getString`, `rs.getInt`, `rs.getTimestamp("col").toLocalDateTime()`.
- **Driver** → usually auto-loaded; historically `Class.forName("oracle.jdbc.OracleDriver")`.
- **Real frameworks** → `DBUtil`/DAO class + **HikariCP** pool; credentials from config/env.

## Multiple rows
```java
List<Map<String, Object>> rows = new ArrayList<>();
while (rs.next()) {
    Map<String, Object> row = new HashMap<>();      // LinkedHashMap if order matters
    row.put("id", rs.getInt("id"));
    row.put("status", rs.getString("status"));
    rows.add(row);
}
```

---

## Depth ladder
1. **Why `PreparedStatement`?** → parameterized, SQL-injection safe, precompiled.
2. **Is the DB always immediately updated?** → No — async systems are **eventually consistent** → **poll the DB with a retry loop** until the record appears (like an explicit wait).
3. **Keep test data clean?** → seed in `@BeforeClass`, clean in `@AfterMethod`, or roll back a transaction.
4. **ORM?** → Hibernate/JPA exist; test frameworks usually stick to plain JDBC.
5. **What do you validate?** → values, counts, statuses, timestamps — compared with the API response, not hardcoded.

---

## Memorize
1. **"JDBC: `DriverManager.getConnection` → `PreparedStatement` → `ResultSet` → close with try-with-resources."**
2. **"`PreparedStatement` = parameterized + SQL-injection safe. Read via `rs.getString/getInt/getTimestamp`."**
3. **"Async systems are eventually consistent → poll the DB with retries before asserting."**

## Likely follow-ups
- Difference from an ORM? → JDBC is manual SQL; ORM maps objects (Hibernate).
- How do you test cleanup? → `@AfterMethod` delete/update, or transaction rollback.
- How do you verify an update? → re-query the row and compare to the API response.
