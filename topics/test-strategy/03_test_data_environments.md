# 03 — Test Data and Environments

## 1. Why test data is hard

- Shared data causes order dependence and parallel collisions.
- Production data has PII and cannot be copied freely.
- Microservices have no shared database; each service owns its data.
- Async systems need data to propagate.
- Tests that create data must clean up or isolate.

**Rule:** isolation beats cleanup. A fresh environment/database per run is better than a cleanup
script that misses a row.

---

## 2. Data strategies

| Strategy | Use | Trade-off |
|---|---|---|
| **Unique per test** | Parallel-safe IDs/emails | Must generate and track |
| **API-driven setup** | Realistic end-to-end data | Slower; depends on APIs |
| **Direct DB seed** | Specific state hard to reach via API | Bypasses business rules |
| **Factory/builders** | Readable fixtures | Can drift from production |
| **Snapshot/restore** | Large production-like datasets | Storage and refresh cost |
| **Synthetic generation** | Volume/performance/PII-free | May miss real edge cases |
| **Production sampling** | Realistic distribution | PII, privacy, compliance |
| **Ephemeral DB per run** | Full isolation | Startup cost |

Use a mix: unique per test for isolation, API setup for realism, direct seed only for specific
states.

---

## 3. Unique data

```java
String runId = UUID.randomUUID().toString().substring(0, 8);
String email = "buyer-" + runId + "@example.test";
String sellerId = "seller-" + runId;
String idempotencyKey = UUID.randomUUID().toString();
```

Rules:

- Prefix with the test run ID for traceability.
- Never reuse fixed emails/IDs across parallel tests.
- Keep the data identifiable so cleanup can target it.
- Use a test domain (`.test`) for emails.

---

## 4. Test data service / factories

A small helper that knows how to create domain objects:

```java
public final class ListingFactory {
    public static CreateListingRequest draft(String sellerId) {
        return new CreateListingRequest(
                "Test listing " + UUID.randomUUID(),
                "generated",
                new BigDecimal("10.00"),
                "USD",
                sellerId
        );
    }
}
```

Benefits: readable tests, one place to change payload shape, consistent valid defaults.

Avoid hidden magic: the test should still show the fields that matter.

---

## 5. Cleanup

- Prefer ephemeral resources (container, schema, namespace, resource group).
- If cleanup is needed, make it idempotent and targeted.
- Clean up in `finally`/`@AfterEach` even on failure.
- Do not delete shared data you did not create.
- Verify cleanup; leaked resources cost money and cause future collisions.

---

## 6. PII and compliance

- Do not copy production PII into test environments without approval.
- Use synthetic or anonymized data.
- Treat test environments as production for access control if they contain real data.
- Mask logs and reports.
- Define retention and deletion.

---

## 7. Environments

| Environment | Purpose | Data | Lifetime |
|---|---|---|---|
| Local | Fast inner loop | Synthetic | Developer |
| Ephemeral/preview | Per-PR integration | Seeded | Deleted after merge |
| Staging | Release validation | Production-like, anonymized | Persistent |
| Canary | Small production slice | Real | Short-lived |
| Production | Real users | Real | Permanent |

**Rule:** the higher the environment, the more it should resemble production in config, network,
identity, and data shape — not necessarily data content.

---

## 8. Environment parity checklist

- [ ] Same artifact as production.
- [ ] Same config keys and secret injection mechanism.
- [ ] Same database engine/version (not H2 for Postgres).
- [ ] Same messaging/queue semantics.
- [ ] Same auth/identity provider or a faithful sandbox.
- [ ] Same network boundaries/private endpoints.
- [ ] Same observability stack.
- [ ] Same time zone/locale where relevant.
- [ ] Documented differences and why they exist.

---

## 9. Test data for async systems

```text
1. Create data via API.
2. Poll for the expected state/event with a timeout.
3. Assert the payload/schema.
4. Assert idempotency with a duplicate event.
5. Assert dead-letter queue is empty.
```

Do not assert immediately after the write. Eventual consistency means "not yet" is normal.

---

## 10. Test data for UI tests

- Seed state through APIs, not by clicking through the UI.
- Use a unique user per test/run.
- Reset cookies/storage between tests (Playwright context, Selenium new profile).
- Avoid depending on a shared "test user" that other tests mutate.
- Capture the data ID in the report for debugging.

---

## 11. Secrets and config

- Config: URLs, timeouts, feature flags — non-secret, environment-specific.
- Secrets: tokens, passwords, keys — from a secret store or CI secret.
- Never commit secrets; scan for accidental commits.
- Fail fast if required config is missing.
- Do not log secrets or full tokens.

---

## 12. Data/environment checklist

- [ ] Data is unique per test or isolated per run.
- [ ] Setup uses the API where realism matters.
- [ ] Cleanup is automatic and verified.
- [ ] No production PII without approval.
- [ ] Environment uses the production engine/version where it matters.
- [ ] Config is externalized; secrets are injected.
- [ ] Async tests poll to eventual state.
- [ ] UI tests seed state through APIs.
- [ ] Environment differences are documented.
- [ ] Resource leaks are monitored.
