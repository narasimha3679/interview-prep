# RestAssured — Authentication & Request/Response Specs

## Say it right
> "Auth: I get a token from the token endpoint in a helper — parameters like client id/secret and URL come from **env-specific config**, never hardcoded — then attach it as a **Bearer** header. Base URL and common headers go into a **`RequestSpecification`** built once, so tests just call `given().spec(reqSpec)`."

---

## 1. Auth options
```java
.auth().basic("user", "pass")                  // sends creds only AFTER a 401 challenge
.auth().preemptive().basic("user", "pass")     // sends the header UP FRONT (no extra round trip)
.auth().oauth2(token)                          // Bearer token
.auth().digest("user", "pass")
.auth().form("user", "pass")
.header("x-api-key", key)                      // API key
```

**Classic follow-up — preemptive vs challenged basic auth:**
> "Challenged sends credentials only after the server returns 401 → an extra round trip. **Preemptive sends them immediately** — what most APIs/frameworks want."

**OAuth2 grant types:** `client_credentials` (most common for API testing), password, authorization_code, refresh_token.

---

## 2. Specs — the real "avoid repeating" answer
```java
RequestSpecification reqSpec = new RequestSpecBuilder()
        .setBaseUri(ConfigManager.get("api.baseUrl"))
        .setContentType(ContentType.JSON)
        .addHeader("Authorization", "Bearer " + token)
        .build();

ResponseSpecification resSpec = new ResponseSpecBuilder()
        .expectContentType(ContentType.JSON)
        .expectStatusCode(200)
        .expectResponseTime(lessThan(3000L))
        .build();
```

Then each test is one line:
```java
given().spec(reqSpec).when().get("/accounts/1")
       .then().spec(resSpec).body("name", notNullValue());
```

> **"Request/Response specs are the POM of API testing."**

---

## 3. Token handling
- **Cache** the token; don't regenerate for every test — refresh on **expiry** or a **401** (retry once).
- Generate in `@BeforeSuite`/`@BeforeClass` or a lazy singleton depending on how fresh it must be.
- **Never hardcode secrets** → env variables / vault / CI secrets.
- Multiple **roles** (user/admin/unauthorised) → keep a token **per role**, choose per test.

---

## Depth ladder
1. **preemptive vs challenged?** → extra 401 round trip vs header sent immediately.
2. **Where does the token live?** → cached helper, refreshed on expiry/401; never hardcoded.
3. **Parallel API tests?** → avoid global `RestAssured.baseURI` statics; use per-request specs.
4. **Secrets?** → env / CI secrets, not the repo.
5. **Who issues it?** → token endpoint, `client_credentials` grant for service-to-service.

---

## Memorize
1. **"`.auth().preemptive().basic()` sends creds upfront; `.auth().basic()` waits for the 401. Bearer via `.auth().oauth2(token)`."**
2. **"`RequestSpecification`/`ResponseSpecification` = POM of API testing; built once, reused with `.spec()`."**
3. **"Token from a parameterized helper → cached → refreshed on expiry/401 → never hardcoded."**

## Likely follow-ups
- Difference between basic and preemptive basic? → an extra 401 round trip.
- How do you avoid repeating base URL? → `RequestSpecification` (or `RestAssured.baseURI` globally, avoided for parallel).
- How do you test an unauthorised user? → send no/invalid token, expect 401.
