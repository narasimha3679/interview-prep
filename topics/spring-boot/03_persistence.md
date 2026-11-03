# 03 — Persistence and Transactions

## 1. The layering rule

```text
Controller  -> HTTP, validation, status codes
Service     -> business rules, transactions
Repository  -> queries, persistence
Entity      -> database mapping
DTO         -> API contract
```

Do not return entities from controllers. Use DTOs to:

- Keep the API stable when the schema changes.
- Avoid leaking lazy-loaded fields or internal columns.
- Control serialization.
- Validate input separately from persistence.

---

## 2. JPA basics

```java
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        if (status == null) status = ListingStatus.DRAFT;
    }
}
```

### Repository

```java
public interface ListingRepository extends JpaRepository<Listing, Long> {
    Page<Listing> findByStatus(ListingStatus status, Pageable pageable);
}
```

Spring Data derives the query from the method name. For complex queries, use `@Query` or a
Specification.

---

## 3. Entity vs DTO

| Concern | Entity | DTO |
|---|---|---|
| Purpose | Persistence | API contract |
| Changes | Schema-driven | Consumer-driven |
| Serialization | Lazy loading/proxies | Explicit fields |
| Validation | DB constraints | Request validation |
| Exposure | Internal | Public |

**Rule:** map explicitly. Do not use `BeanUtils.copyProperties` blindly; it hides field changes.

```java
public record ListingResponse(Long id, String title, BigDecimal price,
                              String currency, String status, String sellerId) {

    public static ListingResponse from(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getPrice(),
                listing.getCurrency(),
                listing.getStatus().name(),
                listing.getSellerId()
        );
    }
}
```

---

## 4. Transactions

```java
@Service
public class ListingService {

    @Transactional
    public ListingResponse create(CreateListingRequest request) {
        Listing listing = new Listing();
        // map fields
        return ListingResponse.from(repository.save(listing));
    }

    @Transactional(readOnly = true)
    public ListingResponse get(Long id) {
        return repository.findById(id)
                .map(ListingResponse::from)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }
}
```

Rules:

- `@Transactional` on public service methods, not private/self-invoked methods.
- Use `readOnly = true` for reads.
- Keep transactions short; do not call slow external services inside one.
- Understand rollback: unchecked exceptions roll back by default; checked exceptions do not unless
  configured.
- Lazy loading only works inside the transaction/session.

---

## 5. N+1 problem

```java
// Entity with a lazy association
@OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
private List<Offer> offers;

// This can issue 1 query for listings + 1 per listing for offers
List<Listing> listings = repository.findAll();
listings.forEach(l -> l.getOffers().size());
```

Fixes:

- `JOIN FETCH` / `@EntityGraph` for known access patterns.
- Batch fetching (`@BatchSize`).
- DTO projection for read models.
- Do not enable `EAGER` everywhere; it hides the problem and can explode the payload.

**Testing:** assert query count or latency as the parent count grows, not just correctness.

---

## 6. Migrations

`ddl-auto=update` is convenient for demos, not a production migration strategy.

Use Flyway or Liquibase:

```text
src/main/resources/db/migration/
  V1__create_listings.sql
  V2__add_currency.sql
```

Rules:

- Migrations are versioned and immutable once applied.
- Use **expand/contract** for zero-downtime changes:
  1. Add new column/table (nullable/default).
  2. Backfill.
  3. Switch reads/writes.
  4. Remove old column in a later release.
- Test migrations against production-like data.
- Measure lock time.
- Keep a rollback/forward-fix plan.

---

## 7. Test data

| Approach | Use |
|---|---|
| In-memory H2 | Fast demo/unit slice; not production SQL parity |
| Testcontainers Postgres/MySQL | Real dialect and behavior |
| Seeded SQL scripts | Reproducible fixtures |
| API-driven setup | End-to-end realism |
| Unique per-test data | Parallel safety |
| Ephemeral database per run | Isolation over cleanup |

**Rule:** create data through the API when testing the API; use direct DB setup when testing the
repository or a specific state that is hard to reach through the API.

---

## 8. Transactions and tests

- `@DataJpaTest` rolls back by default; useful for repository tests.
- `@SpringBootTest` with a real DB may not roll back if the service uses `REQUIRES_NEW`.
- Use `@Transactional` on the test only if you understand the production transaction boundaries.
- Test rollback explicitly for multi-step writes.
- Test optimistic locking (`@Version`) with concurrent updates if the domain needs it.

---

## 9. Persistence checklist

- [ ] Entity and DTO are separate.
- [ ] Business rules live in the service, not the controller.
- [ ] Transaction boundaries are explicit.
- [ ] Lazy loading is handled inside the transaction or via a fetch plan.
- [ ] N+1 is measured on critical queries.
- [ ] Migrations are versioned and tested.
- [ ] Indexes exist for common filters/sorts.
- [ ] Unique constraints back idempotency/business keys.
- [ ] Test data is isolated and parallel-safe.
- [ ] Production does not use `ddl-auto=update`.
