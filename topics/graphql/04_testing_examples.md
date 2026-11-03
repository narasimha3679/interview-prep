# 04 — Testing Examples

Examples are illustrative. Point them at your own GraphQL service or a public test API.

---

## 1. RestAssured: query with variables

```java
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GraphQlClient {

    private final String endpoint;

    public GraphQlClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public io.restassured.response.Response execute(String query, Map<String, Object> variables) {
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        return given()
                .contentType("application/json")
                .body(body)
            .when()
                .post(endpoint)
            .then()
                .extract().response();
    }
}
```

Test:

```java
@Test
void returnsListingById() {
    String query = """
            query Listing($id: ID!) {
              listing(id: $id) {
                id
                title
                price { amount currency }
              }
            }
            """;

    Response response = client.execute(query, Map.of("id", "123"));

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.jsonPath().getString("errors")).isNull();
    assertThat(response.jsonPath().getString("data.listing.id")).isEqualTo("123");
    assertThat(response.jsonPath().getString("data.listing.title")).isNotBlank();
    assertThat(response.jsonPath().getString("data.listing.price.currency")).isEqualTo("USD");
}
```

---

## 2. RestAssured: assert an error, not just 200

```java
@Test
void unknownListingReturnsNotFoundError() {
    String query = """
            query { listing(id: "does-not-exist") { id title } }
            """;

    Response response = client.execute(query, Map.of());

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.jsonPath().getList("errors")).isNotEmpty();
    assertThat(response.jsonPath().getString("errors[0].extensions.code"))
            .isEqualTo("NOT_FOUND");
    assertThat(response.jsonPath().get("data.listing")).isNull();
}
```

---

## 3. RestAssured: mutation + read-back

```java
@Test
void createsListingAndReadsItBack() {
    String mutation = """
            mutation Create($input: CreateListingInput!) {
              createListing(input: $input) {
                listing { id title status }
                errors { field message code }
              }
            }
            """;

    Map<String, Object> input = Map.of(
            "title", "Vintage camera",
            "price", Map.of("amount", 249.99, "currency", "USD"),
            "sellerId", "s1"
    );

    Response created = client.execute(mutation, Map.of("input", input));
    assertThat(created.jsonPath().getList("errors")).isEmpty();

    String id = created.jsonPath().getString("data.createListing.listing.id");
    assertThat(id).isNotBlank();

    Response read = client.execute("""
            query($id: ID!) { listing(id: $id) { id title status } }
            """, Map.of("id", id));

    assertThat(read.jsonPath().getString("data.listing.id")).isEqualTo(id);
    assertThat(read.jsonPath().getString("data.listing.title")).isEqualTo("Vintage camera");
}
```

---

## 4. Karate: GraphQL request

```gherkin
Feature: GraphQL listings

  Background:
    * url baseUrl
    * header Content-Type = 'application/json'

  Scenario: Query a listing by id
    Given request
      """
      {
        "query": "query($id: ID!) { listing(id: $id) { id title price { amount currency } } }",
        "variables": { "id": "123" }
      }
      """
    When method post
    Then status 200
    And match response.errors == '#notpresent'
    And match response.data.listing.id == '123'
    And match response.data.listing.price.currency == 'USD'

  Scenario: Unknown listing returns a structured error
    Given request
      """
      {
        "query": "query { listing(id: \"missing\") { id title } }"
      }
      """
    When method post
    Then status 200
    And match response.data.listing == null
    And match response.errors[0].extensions.code == 'NOT_FOUND'
```

Karate's `match` is useful for asserting partial shapes and fuzzy types.

---

## 5. Schema validation

### Checked-in SDL diff

```bash
# Example with GraphQL Inspector
npx @graphql-inspector/cli diff \
  schema/previous.graphql \
  schema/current.graphql \
  --rule suppressRemovalOfDeprecatedField
```

Fail the pipeline on breaking changes.

### Runtime shape assertion

If you cannot diff the schema, assert the fields your client depends on:

```java
assertThat(response.jsonPath().getMap("data.listing"))
        .containsKeys("id", "title", "price", "seller");
```

This is weaker than a schema contract but catches accidental removals.

---

## 6. Authorization test template

```java
@Test
void nonOwnerCannotReadSellerEmail() {
    String query = """
            query($id: ID!) {
              listing(id: $id) {
                id
                seller { id email }
              }
            }
            """;

    Response response = clientWithToken(nonOwnerToken)
            .execute(query, Map.of("id", listingId));

    // Depending on policy: field null + error, or top-level auth error
    assertThat(response.jsonPath().getString("data.listing.seller.email")).isNull();
    assertThat(response.jsonPath().getList("errors")).isNotEmpty();
}
```

Test both "not visible" and "not leaky": the error should not reveal whether the email exists.

---

## 7. N+1 / performance test

A simple correctness test will not catch N+1. Measure:

- Latency for 1 parent vs 50 parents.
- Downstream query count from logs/metrics.
- Query complexity/cost returned in extensions.
- Timeout behavior for expensive queries.

```java
@Test
void nestedSellerLookupDoesNotExplode() {
    long start = System.nanoTime();
    Response response = client.execute("""
            query { listings(first: 50) { edges { node { id seller { id } } } } }
            """, Map.of());
    long elapsedMs = (System.nanoTime() - start) / 1_000_000;

    assertThat(response.jsonPath().getList("errors")).isEmpty();
    assertThat(elapsedMs).isLessThan(2_000);
}
```

Latency thresholds are environment-specific; in CI, prefer asserting a **query-count budget** or a
relative increase over a baseline.

---

## 8. CI checklist

- [ ] Schema diff runs on every PR.
- [ ] Query/mutation smoke runs against a deployed environment.
- [ ] Error and auth cases are included.
- [ ] N+1/cost checks run on critical nested queries.
- [ ] Pagination boundaries are covered.
- [ ] Persisted-query allowlist is tested.
- [ ] Schema and test reports are published as artifacts.
