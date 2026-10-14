# RestAssured — Given/When/Then Structure

## Say it right
> "`given()` is **request setup** — base URI, headers, query/path params, auth, and the **request body**. `when()` is the **action** — the HTTP method and endpoint. `then()` is the **validation** — status code, headers, and body assertions."
>
> ⚠️ The **body goes in `given()`**, not `when()`.

---

## The structure
```java
given()
    .baseUri("https://api.example.com")
    .header("Content-Type", "application/json")
    .queryParam("page", 1)
    .auth().preemptive().basic("user", "pass")
    .body(requestPojo)                    // ← body here
    .log().all()
.when()
    .post("/accounts")                    // method + endpoint
.then()
    .statusCode(201)
    .body("id", notNullValue())
    .header("Content-Type", containsString("json"))
    .time(lessThan(3000L));
```

| Section | Holds |
|---|---|
| `given()` | baseUri/basePath, headers, cookies, query/path params, auth, contentType, **body** |
| `when()` | `get`/`post`/`put`/`patch`/`delete` + endpoint |
| `then()` | `statusCode`, `body`, `contentType`, `header`, `time`, `extract()` |

## Static imports (they notice if you miss these)
```java
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
```

## Extracting values, not just asserting
```java
Response resp = given().when().get("/accounts/1").then().extract().response();
String name = resp.jsonPath().getString("name");
int status  = resp.getStatusCode();
```

---

## Depth ladder
1. **Avoid repeated setup?** → `RequestSpecification` / `ResponseSpecification` via `given().spec(reqSpec)` — the **POM of API testing**.
2. **Send a Java object as JSON?** → `.body(pojo)`; RestAssured serializes with **Jackson/Gson** automatically.
3. **Validate without hardcoding?** → Hamcrest matchers: `equalTo`, `hasItem`, `notNullValue`, `containsString`, `greaterThan`.
4. **Debug a failing call?** → `.log().all()` (request), `.log().ifValidationFails()` / `.log().ifError()` (response).
5. **Global config?** → `RestAssured.baseURI` etc. — but these are **static/global**, so for parallel tests prefer per-request specs.

---

## Memorize
1. **"`given()` = request setup (headers, params, auth, **body**). `when()` = method + endpoint. `then()` = status, body, header, time."**
2. **"Static imports: `io.restassured.RestAssured.*` and `org.hamcrest.Matchers.*`."**
3. **"Reuse `RequestSpecification`/`ResponseSpecification` — the POM of API testing."**

## Likely follow-ups
- Where does the request body go? → `given().body(...)`.
- How do you assert a nested field? → `body("data.user.id", equalTo(5))`.
- Difference between `then()` assert and `extract()`? → immediate assertion vs pulling the value into a variable.
