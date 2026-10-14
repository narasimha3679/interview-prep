# RestAssured — JSON Path, POJO (De)serialization, Schema

## Say it right
> "I extract values with `then().extract().jsonPath()` or deserialize straight into a POJO with `.as(Pojo.class)`. The same POJO is used for the request and the response, so I get type safety and no string-key typos. And I validate the response **contract** with a JSON schema."

---

## JSON path extraction

### A) Assert inline
```java
given().when().get("/users/1").then()
    .body("data.name", equalTo("John"))          // nested
    .body("data.roles[0]", equalTo("ADMIN"))     // index
    .body("data.roles", hasItem("ADMIN"))        // list contains
    .body("data.users.size()", greaterThan(0));  // count
```

### B) Extract into variables
```java
Response resp = given().when().get("/users/1").then().extract().response();

String name   = resp.jsonPath().getString("data.name");
int age       = resp.jsonPath().getInt("data.age");
List<String> roles = resp.jsonPath().getList("data.roles");
Map<String,Object> user = resp.jsonPath().getMap("data");
```

### GPath syntax
| Syntax | Meaning |
|---|---|
| `data.name` | dot path |
| `data.roles[0]` | index |
| `$.data.users` / `data.users` | root / path |
| `data.users.size()` | count |
| `data.users.find { it.id == 5 }` | predicate |
| `data.users.findAll { it.age > 30 }` | filter |
| `..name` | recursive descent |

---

## POJO — (de)serialization

```java
public class Account {
    private String id;          // needs NO-ARG constructor + getters/setters
    private String name;
    private double balance;
}
```

**Serialize (POJO → JSON)**
```java
given().contentType(ContentType.JSON)
       .body(account)                  // RestAssured serializes with Jackson
.when().post("/accounts");
```

**Deserialize (JSON → POJO)**
```java
Account acc = given().when().get("/accounts/1")
        .then().extract().as(Account.class);

List<Account> list = resp.jsonPath().getList("data", Account.class);
```

**Gotchas:** no-arg constructor required; `@JsonProperty` for name mismatches; **`@JsonIgnoreProperties(ignoreUnknown = true)`** because APIs add fields without telling you.

---

## JSON Schema validation
```java
given().when().get("/users/1").then()
    .assertThat().body(matchesJsonSchemaInClasspath("user-schema.json"));
```
Validates **structure** (types, required fields), not just values → response contract testing.

---

## Depth ladder
1. **`body("a.b", …)` vs `jsonPath().getString(…)`** → inline assertion vs extract into a variable for reuse.
2. **How does serialization happen?** → auto-detects **Jackson** (or Gson) on the classpath; configurable via `RestAssured.config`.
3. **POJO vs `Map`** → type safety + readability vs quick/dynamic payloads.
4. **Dynamic values** (ids, timestamps) → extract from the POST response, feed into the next GET (API chaining).
5. **Contract testing** → JSON Schema validation.

---

## Memorize
1. **"Extract: `extract().jsonPath().getString(\"a.b[0]\")` or `.as(Pojo.class)`. Assert: `body(\"a.b\", equalTo(x))`."**
2. **"POJO needs a no-arg constructor; Jackson handles (de)serialization. Use `@JsonIgnoreProperties(ignoreUnknown=true)`."**
3. **"JSON Schema validation checks the response contract, not just values."**

## Likely follow-ups
- How do you handle a field the API adds later? → `@JsonIgnoreProperties(ignoreUnknown = true)`.
- How do you validate the structure only? → `matchesJsonSchemaInClasspath`.
- How do you chain two API calls? → extract the id from the first response into the second request.
