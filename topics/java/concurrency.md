# Java — Concurrency

## 1. Why it matters for SDETs

- Parallel test execution.
- `ThreadLocal` drivers/contexts.
- Async API/service tests.
- Load/concurrency bugs.
- Framework code that must be thread-safe.

---

## 2. Thread lifecycle

```text
NEW -> RUNNABLE -> (BLOCKED / WAITING / TIMED_WAITING) -> TERMINATED
```

- `start()` creates a new thread; `run()` does not.
- `join()` waits for completion.
- `interrupt()` requests cancellation; the thread must cooperate.
- `sleep()` is not a synchronization primitive.

---

## 3. Race conditions

A race condition occurs when the result depends on timing.

```java
class Counter {
    private int count = 0;
    void increment() { count++; } // read-modify-write, not atomic
}
```

Fixes:

- `synchronized` method/block.
- `AtomicInteger`/`LongAdder`.
- `ReentrantLock`.
- Immutability.
- Confine state to one thread.

**Test:** run many threads, assert the final count; use a barrier/latch to maximize interleaving.

---

## 4. `synchronized` and locks

```java
synchronized void increment() {
    count++;
}

// Explicit lock
private final ReentrantLock lock = new ReentrantLock();

void increment() {
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}
```

Rules:

- Always unlock in `finally`.
- Keep critical sections short.
- Avoid nested locks (deadlock).
- Prefer higher-level concurrency utilities.

---

## 5. `volatile`

`volatile` guarantees visibility, not atomicity.

```java
private volatile boolean running = true;

void stop() { running = false; }
```

Use for a flag one thread writes and others read. Do not use it for `count++`.

---

## 6. Executors

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
try {
    Future<Integer> future = pool.submit(() -> compute());
    Integer result = future.get(2, TimeUnit.SECONDS);
} finally {
    pool.shutdown();
}
```

Rules:

- Always shut down executors.
- Use bounded queues; unbounded queues hide overload.
- Set timeouts on `get`.
- Handle `InterruptedException` (restore the flag).
- Use virtual threads for high-concurrency I/O (Java 21+).

---

## 7. `CompletableFuture`

```java
CompletableFuture<Listing> listing =
        CompletableFuture.supplyAsync(() -> api.get(id));

CompletableFuture<Money> price =
        listing.thenApply(Listing::price)
               .exceptionally(ex -> Money.zero("USD"));

Money result = price.get(2, TimeUnit.SECONDS);
```

Patterns:

- `thenApply` / `thenCompose` / `thenCombine`.
- `allOf` / `anyOf`.
- `exceptionally` / `handle`.
- Always bound with a timeout.

**Trap:** `CompletableFuture` uses the common pool by default; blocking work can starve it. Pass an
explicit executor.

---

## 8. Concurrent collections

| Collection | Use |
|---|---|
| `ConcurrentHashMap` | Concurrent key/value access |
| `CopyOnWriteArrayList` | Read-mostly listener lists |
| `BlockingQueue` | Producer/consumer |
| `ConcurrentLinkedQueue` | Non-blocking queue |
| `AtomicInteger`/`LongAdder` | Counters |

Default `ArrayList`/`HashMap` are not thread-safe.

---

## 9. `ThreadLocal`

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

public static WebDriver getDriver() {
    return DRIVER.get();
}

public static void setDriver(WebDriver driver) {
    DRIVER.set(driver);
}

public static void quit() {
    try {
        WebDriver driver = DRIVER.get();
        if (driver != null) driver.quit();
    } finally {
        DRIVER.remove(); // prevent leaks
    }
}
```

Use for per-thread state (driver, context, tenant). Always `remove()` in a thread pool.

---

## 10. Virtual threads (Java 21+)

- Lightweight threads managed by the JVM.
- Good for high-concurrency blocking I/O.
- Not a fix for CPU-bound work or shared mutable state.
- Avoid pinning (e.g., long `synchronized` blocks around blocking calls).

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> api.call());
}
```

---

## 11. Deadlocks

Four conditions: mutual exclusion, hold-and-wait, no preemption, circular wait.

Avoid:

- Consistent lock ordering.
- Lock timeouts (`tryLock`).
- Avoid nested locks.
- Prefer single-writer/actor or concurrent collections.

**Detect:** thread dump (`jstack`), look for "Found one Java-level deadlock".

---

## 12. Testing concurrency

- Use `CountDownLatch`/`CyclicBarrier` to align threads.
- Run many iterations to expose races.
- Assert invariants, not timing.
- Use timeouts to avoid hangs.
- Test cancellation/interruption.
- Test with a thread pool larger than 1.
- Avoid `Thread.sleep` as synchronization.

---

## 13. Concurrency checklist

- [ ] Shared mutable state minimized.
- [ ] `ThreadLocal` removed after use.
- [ ] Executors shut down.
- [ ] Locks released in `finally`.
- [ ] Timeouts on blocking calls.
- [ ] Concurrent collections where needed.
- [ ] Interruption handled.
- [ ] Concurrency tests assert invariants.
- [ ] No fixed sleeps as synchronization.
- [ ] Thread dumps available on hang.
