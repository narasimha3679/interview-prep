# 04 — Testing Spring Boot

## 1. Test levels

| Level | Annotation | What it loads | Use for |
|---|---|---|---|
| **Unit** | none | Nothing | Pure logic, validation, mapping |
| **Web slice** | `@WebMvcTest` | MVC + controller | Status, JSON, validation, error handling |
| **Data slice** | `@DataJpaTest` | JPA + embedded DB | Queries, mappings, constraints |
| **Full context** | `@SpringBootTest` | Whole app | Wiring, transactions, integration |
| **External infra** | `@Testcontainers` | Real DB/broker | Production-like integration |
| **Contract** | Pact/WireMock | Consumer/provider | Compatibility |

**Rule:** use the cheapest level that can catch the defect. Do not use `@SpringBootTest` for a
controller status-code check.

---

## 2. Unit tests

```java
class ListingServiceTest {

    private final ListingRepository repository = mock(ListingRepository.class);
    private final ListingService service = new ListingService(repository);

    @Test
    void throwsWhenListingMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(ListingNotFoundException.class);
    }
}
```

Unit tests are fast and precise. Use them for:

- Business rules.
- Mapping.
- Validation logic.
- Error branches.
- Calculations (money, tax, discounts).

Do not mock the class under test or over-mock until the test only proves the mocks.

---

## 3. Web slice: `@WebMvcTest`

```java
@WebMvcTest(ListingController.class)
class ListingControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean ListingService service;

    @Test
    void createsListing() throws Exception {
        ListingResponse response = new ListingResponse(
                1L, "Camera", new BigDecimal("249.99"), "USD", "DRAFT", "s1");

        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Camera",
                                "price", 249.99,
                                "currency", "USD",
                                "sellerId", "s1"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/listings/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Camera"));
    }
}
```

Test:

- Status codes.
- Response body/shape.
- Validation errors.
- Error handler mapping.
- Content type.
- `Location` header on create.

`@WebMvcTest` does not load `@Service`/`@Repository` beans; mock them.

---

## 4. Data slice: `@DataJpaTest`

```java
@DataJpaTest
class ListingRepositoryTest {

    @Autowired ListingRepository repository;

    @Test
    void findsByStatus() {
        Listing active = new Listing();
        active.setTitle("Active");
        active.setStatus(ListingStatus.ACTIVE);
        repository.save(active);

        Page<Listing> page = repository.findByStatus(
                ListingStatus.ACTIVE, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
    }
}
```

Test:

- Derived queries.
- Custom JPQL/SQL.
- Constraints and unique keys.
- Mapping of enums/dates/money.
- Pagination/sorting.

`@DataJpaTest` uses an embedded DB by default. For production parity, use Testcontainers.

---

## 5. Full context: `@SpringBootTest`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingIntegrationTest {

    @Autowired TestRestTemplate rest;

    @Test
    void createsAndReadsListing() {
        // POST, then GET by id
    }
}
```

Use for:

- Full wiring.
- Real HTTP (random port).
- Transactions across layers.
- Security filters.
- Serialization/deserialization.
- Profile-specific config.

Keep the number small; the context startup is expensive.

---

## 6. Testcontainers

```java
@SpringBootTest
@Testcontainers
class ListingPostgresTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void worksOnRealPostgres() {
        // ...
    }
}
```

Use for:

- SQL dialect differences.
- Migrations.
- Constraints/indexes.
- JSON/array types.
- Transaction/isolation behavior.

Not for: every test. Container startup is slower; use it where the database is the risk.

---

## 7. WireMock for dependencies

```java
@RegisterExtension
static WireMockExtension upstream = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

@Test
void handlesUpstreamTimeout() {
    upstream.stubFor(get(urlEqualTo("/seller/s1"))
            .willReturn(aResponse().withFixedDelay(3000)));

    // assert the service times out/degrade as designed
}
```

Test:

- Success response.
- 4xx/5xx.
- Timeout.
- Malformed JSON.
- Retry/backoff.
- Circuit breaker behavior.

---

## 8. What to assert

| Layer | Assert |
|---|---|
| Controller | Status, headers, body shape, validation, error code |
| Service | Business outcome, side effects, exceptions, transaction behavior |
| Repository | Persisted state, query results, constraints, pagination |
| Integration | End-to-end HTTP + DB + downstream stubs |
| Contract | Provider satisfies consumer expectations |

Do not assert implementation details (private method calls, SQL string equality) unless that is the
contract.

---

## 9. Test data and isolation

- Unique values per test (random email, seller ID, idempotency key).
- Clean state before each test or use a fresh schema/container.
- Do not depend on test order.
- Avoid shared mutable static state.
- Use builders/factories for readable fixtures.
- Keep secrets out of test code.

---

## 10. Common Spring testing mistakes

1. `@SpringBootTest` for everything → slow suite.
2. Mocking the class under test.
3. Testing getters/setters instead of behavior.
4. Asserting only status, not the body/error contract.
5. Using H2 and assuming Postgres behavior.
6. Not testing validation and error paths.
7. Shared test data causing order dependence.
8. `@Transactional` on tests hiding production transaction issues.
9. No timeout on integration tests.
10. Not cleaning up containers/ports.

---

## 11. Test checklist

- [ ] Unit tests cover business rules and edge cases.
- [ ] Web slice covers status/body/validation/errors.
- [ ] Repository tests cover queries and constraints.
- [ ] At least one full integration test covers wiring.
- [ ] Real DB tested with Testcontainers where dialect matters.
- [ ] Downstream failures are stubbed and asserted.
- [ ] Test data is unique and isolated.
- [ ] Tests run in parallel without shared state.
- [ ] Reports/artifacts are published in CI.
