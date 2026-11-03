# RestAssured — Params, Status Codes, Negative Testing

## Say it right
> "For negative tests I don't just assert the status code — I validate the **error contract**: error code, message, traceId. A 400 with the wrong error code is still a bug. And for a full feature I verify the **CRUD lifecycle** end to end, including a **DB assertion via JDBC**."

---

## Params & headers
```java
// Path param
given().pathParam("id", 5).when().get("/accounts/{id}");

// Query params
given().queryParam("page", 1).queryParam("size", 10).when().get("/accounts");
given().queryParams("status", "A", "B");                  // multi-value

// Headers / cookies
given().header("x-correlation-id", "123").cookie("session", token);

// Form params
given().contentType("application/x-www-form-urlencoded")
       .formParam("user", "john").when().post("/login");
```

---

## Status codes
| Code | Meaning |
|---|---|
| 200 / 201 / 204 | OK / Created / No Content (delete) |
| 400 | Bad Request (validation) |
| 401 / 403 | Unauthorized / Forbidden |
| 404 | Not Found |
| 409 | Conflict (duplicate) |
| 415 / 422 | Unsupported media / Unprocessable |
| 429 | Rate limited |
| 500 / 502 / 503 | Server / bad gateway / unavailable |

---

## Negative testing
```java
given().spec(reqSpec).pathParam("id", "999999")
.when().get("/accounts/{id}")
.then()
    .statusCode(404)
    .body("errorCode", equalTo("ACCOUNT_NOT_FOUND"))       // validate the ERROR CONTRACT
    .body("message", containsString("not found"));
```

> "A 400 with the wrong error code is still a bug" → always check **code + message** (+ traceId if present).

---

## The strong answer — CRUD lifecycle
> "POST creates → GET retrieves the exact data → PUT updates → DELETE returns **204** → second GET returns **404**. Then I confirm persistence with a **JDBC** query against the DB."

API + DB verification is what a bank QE panel wants to hear.

### Follow-up: "How do you know it really persisted?"
→ JDBC query (`SELECT ... FROM ACCOUNTS WHERE ID = ?`) and compare with the response payload. Use `PreparedStatement` to avoid SQL injection, and `try-with-resources` to close the `Connection`/`ResultSet`.

---

## Memorize
1. **"`.pathParam()` = URL segment, `.queryParam()` = `?k=v`, `.formParam()` = form-urlencoded, `.header()`/`.cookie()` = the rest."**
2. **"Negative tests validate status code AND error contract (code, message), not just the code."**
3. **"CRUD lifecycle: POST → GET → PUT → DELETE(204) → GET(404), plus a DB check via JDBC."**

## Likely follow-ups
- How do you assert data in the DB? → JDBC `PreparedStatement` + compare with the API response.
- What does DELETE return? → 204 No Content (then GET → 404).
- How do you test validation errors? → send bad payloads, expect 400 + specific error codes.
