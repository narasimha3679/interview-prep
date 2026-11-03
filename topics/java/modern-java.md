# Java — Modern Java (17–21)

## 1. `var` (Java 10)

Local type inference:

```java
var listings = new ArrayList<Listing>();
var client = new ApiClient(baseUrl);
```

Rules:

- Local variables only.
- Initializer required.
- Use when the type is obvious; avoid when it hides intent.

---

## 2. Records (Java 16)

Immutable data carriers:

```java
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount.signum() < 0) throw new IllegalArgumentException("negative");
    }
}
```

You get: constructor, accessors, `equals`, `hashCode`, `toString`.

Use for DTOs, value objects, test data. Not for mutable entities.

---

## 3. Text blocks (Java 15)

```java
String json = """
        {
          "title": "Camera",
          "price": 249.99
        }
        """;
```

Great for JSON payloads, SQL, GraphQL queries in tests.

---

## 4. Switch expressions (Java 14)

```java
String label = switch (status) {
    case DRAFT -> "Draft";
    case ACTIVE -> "Active";
    case SOLD, REMOVED -> "Closed";
};
```

No fall-through, exhaustive, expression value.

---

## 5. Pattern matching

### `instanceof`

```java
if (obj instanceof Listing listing) {
    System.out.println(listing.title());
}
```

### Switch patterns (Java 21)

```java
String describe(Object o) {
    return switch (o) {
        case Listing l -> "Listing " + l.id();
        case Order ord -> "Order " + ord.id();
        case null -> "null";
        default -> "unknown";
    };
}
```

### Record patterns (Java 21)

```java
if (obj instanceof Money(BigDecimal amount, String currency)) {
    System.out.println(amount + " " + currency);
}
```

---

## 6. Sealed classes (Java 17)

```java
public sealed interface PaymentResult
        permits Approved, Declined, Pending {}

record Approved(String id) implements PaymentResult {}
record Declined(String reason) implements PaymentResult {}
record Pending() implements PaymentResult {}
```

Benefits: exhaustive switches, explicit hierarchy, safer domain modeling.

---

## 7. `Optional`

```java
Optional<Listing> listing = repository.findById(id);
String title = listing.map(Listing::title).orElse("unknown");
```

Rules:

- Use for return values that may be absent.
- Do not use for fields/parameters.
- Do not call `get()` without checking.
- Prefer `map`/`flatMap`/`orElseThrow`.

---

## 8. Virtual threads (Java 21)

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    Future<Listing> future = executor.submit(() -> api.get(id));
    Listing listing = future.get(2, TimeUnit.SECONDS);
}
```

- Lightweight, high-concurrency blocking I/O.
- Not for CPU-bound work.
- Avoid pinning (`synchronized` around blocking calls).
- Still need timeouts and backpressure.

---

## 9. Useful APIs for tests

```java
// Collections
List.of("a", "b");
Map.of("k", "v");
Set.of(1, 2, 3);

// Streams
list.stream().filter(...).map(...).toList();

// Files
Files.readString(Path.of("payload.json"));

// HTTP client (Java 11+)
HttpClient client = HttpClient.newHttpClient();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
```

---

## 10. Testing implications

- Records make DTO/test-data construction concise.
- Sealed types + pattern matching make response handling exhaustive.
- Text blocks make payloads readable.
- Virtual threads change concurrency tests (don't assume platform threads).
- `Optional` is not a wire type; serialize the value.

---

## 11. Modern Java checklist

- [ ] Records for DTOs/value objects.
- [ ] Text blocks for payloads/queries.
- [ ] Switch expressions for mapping.
- [ ] Sealed types for finite results.
- [ ] Pattern matching instead of casts.
- [ ] `Optional` used correctly.
- [ ] Virtual threads only for I/O-bound work.
- [ ] Compile/test on the target JDK.
