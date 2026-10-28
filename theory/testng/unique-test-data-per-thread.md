# TestNG / Framework — Unique Test Data per Thread

## Say it right
> "Within a JVM I use a `ThreadLocal` to hold the data and a UUID / AtomicInteger / DB sequence to generate it — created in `@BeforeMethod`, deleted in `@AfterMethod`. Across parallel CI jobs I prefix with the run id. And I always tear the data down so the next run starts clean."

Two problems to solve:
1. **Threads in the same JVM** must not share a record.
2. **Parallel CI machines/jobs** must not collide.

---

## 1. Generate unique values (thread-safe)
```java
public class TestDataFactory {
    private static final AtomicInteger seq = new AtomicInteger(0);

    public static String uniqueUser() {
        return "user_" + System.currentTimeMillis() + "_" + seq.incrementAndGet();
    }
    // or simply: "user_" + UUID.randomUUID();
}
```
- `AtomicInteger` → thread-safe counter, no duplicates
- `UUID.randomUUID()` → simplest unique value
- Best: let the **DB** generate it (sequence/identity column)

## 2. Hold it per thread — `ThreadLocal`
```java
public class TestDataContext {
    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    public static void set(String id) { userId.set(id); }
    public static String get() { return userId.get(); }
    public static void clear() { userId.remove(); }
}
```
```java
@BeforeMethod
public void setUp() {
    String id = TestDataFactory.uniqueUser();
    TestDataContext.set(id);
    apiClient.createUser(id);          // create via API, not UI — fast
}

@AfterMethod
public void tearDown() {
    apiClient.deleteUser(TestDataContext.get());   // cleanup → no collision on re-run
    TestDataContext.clear();
}
```

## 3. Pool of pre-provisioned data
```java
private static final ConcurrentLinkedQueue<String> pool =
        new ConcurrentLinkedQueue<>(List.of("ACC1", "ACC2", "ACC3", "ACC4"));

@BeforeMethod public void take() { TestDataContext.set(pool.poll()); }
@AfterMethod  public void give() { pool.offer(TestDataContext.get()); }   // return it
```

## 4. Parallel CI jobs — namespace by run
```java
String runId = System.getenv("BUILD_NUMBER");       // or GITHUB_RUN_ID
String user  = "u_" + runId + "_" + UUID.randomUUID();
```

---

## Memorize
1. **"Unique per thread: `ThreadLocal` to hold it + `UUID`/`AtomicInteger`/DB sequence to generate it; create in `@BeforeMethod`, delete in `@AfterMethod`."**
2. **"Pre-existing data → `ConcurrentLinkedQueue` pool, poll/offer per thread."**
3. **"Across parallel CI jobs → prefix with the run id (build number)."**

## Likely follow-ups
- Why not a static counter variable? → race condition; use `AtomicInteger`.
- Why create data via API instead of UI? → much faster and more reliable setup.
- What if cleanup fails? → `alwaysRun = true` and log; the run-id prefix still prevents collision.
