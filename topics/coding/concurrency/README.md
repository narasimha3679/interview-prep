# Concurrency Drills

| File | What it demonstrates |
|---|---|
| `ThreadLocalDemo.java` | Per-thread state in a thread pool and `remove()` |

## Drills

1. Increment a shared counter from 10 threads; then fix it with `AtomicInteger`.
2. Use `CountDownLatch` to start threads at the same time.
3. Use `ExecutorService` with a bounded queue and reject policy.
4. Use `CompletableFuture` to call two APIs in parallel and combine results.
5. Add a timeout to a `Future.get`.
6. Use `ConcurrentHashMap` to count words across threads.
7. Demonstrate a `ThreadLocal` leak when `remove()` is omitted.
8. Write a test that asserts an invariant under 100 concurrent iterations.

## Rules

- Prefer immutability and confinement.
- Keep critical sections small.
- Always release locks in `finally`.
- Always shut down executors.
- Always remove `ThreadLocal` in pooled threads.
- Use timeouts to avoid hangs.
- Do not use `Thread.sleep` as synchronization.

## Run

```bash
javac ThreadLocalDemo.java
java ThreadLocalDemo
```
