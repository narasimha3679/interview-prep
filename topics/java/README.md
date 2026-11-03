# Java for SDETs

The Java concepts interviewers and framework work actually use: language, collections, concurrency,
JVM behavior, modern features, and patterns.

## 60-second cram

1. `==` compares references; `.equals()` compares content when overridden.
2. `String` is immutable; use `StringBuilder` in loops.
3. `ArrayList` is the default list; `HashMap` is the default map.
4. `HashMap` = buckets + list/tree; override `equals` and `hashCode` together.
5. `ConcurrentHashMap` for concurrent access; no null keys/values.
6. Streams are lazy, single-use, and do not modify the source.
7. `ThreadLocal` gives per-thread state; always `remove()` in pooled threads.
8. Generics are erased at runtime; PECS for wildcards.
9. Records, sealed types, pattern matching, and virtual threads are modern Java.
10. Know the patterns you use: factory, builder, strategy, adapter, DI, page object.

## File map

| File | Topic |
|---|---|
| [`oop-pillars-abstract-vs-interface.md`](oop-pillars-abstract-vs-interface.md) | 4 pillars, abstract vs interface |
| [`equals-and-string-immutability.md`](equals-and-string-immutability.md) | `==` vs `equals`, String pool |
| [`collections.md`](collections.md) | List/Set/Map, HashMap internals |
| [`exceptions.md`](exceptions.md) | checked/unchecked, try-with-resources |
| [`lambdas-and-streams.md`](lambdas-and-streams.md) | lambdas, Stream API |
| [`reading-test-data.md`](reading-test-data.md) | properties, JSON, Excel |
| [`jdbc-db-validation.md`](jdbc-db-validation.md) | JDBC, PreparedStatement |
| [`concurrency.md`](concurrency.md) | threads, locks, executors, ThreadLocal |
| [`generics.md`](generics.md) | type parameters, wildcards, erasure |
| [`jvm-memory.md`](jvm-memory.md) | heap/stack, GC, leaks |
| [`modern-java.md`](modern-java.md) | records, sealed, pattern matching |
| [`design-patterns.md`](design-patterns.md) | patterns used in frameworks |

## Study order

1. OOP → equals/String → collections → exceptions.
2. Streams → generics → modern Java.
3. Concurrency → JVM memory.
4. Design patterns.
5. Practice with [`../coding/`](../coding/) and [`../collections/`](../collections/).

## Related

- [`../collections/`](../collections/) — deeper collections pack
- [`../coding/`](../coding/) — coding drills
- [`../spring-boot/`](../spring-boot/) — backend application
