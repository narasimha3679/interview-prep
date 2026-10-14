# Java — Lambdas & Stream API

## ⚠️ Two different "streams" in Java (classic trap)
| | `java.io` streams | `java.util.stream` (Java 8) |
|---|---|---|
| What | **I/O**: bytes/chars from files, network, DB | **Pipeline over collections/arrays** |
| Classes | `InputStream`, `OutputStream`, `Reader`, `Writer` | `Stream`, `IntStream`, `Collectors` |

> "Reading a file = `java.io` (`BufferedReader`). Processing a `List` = `java.util.stream`. They're unrelated."

---

## Say it right
> "A lambda is a concise implementation of a **functional interface** (one abstract method). A `java.util.stream` is a **pipeline**: source → intermediate ops (lazy) → one terminal op that triggers execution."

## 1. Lambdas
```java
(parameters) -> expression
(a, b) -> a + b

list.forEach(item -> System.out.println(item));
list.sort((a, b) -> a.getName().compareTo(b.getName()));   // lambda
list.sort(Comparator.comparing(Item::getName));            // method reference
```
Functional interfaces: `Runnable`, `Callable`, `Comparator`, `Consumer`, `Supplier`, `Function<T,R>`, `Predicate<T>` — mark them `@FunctionalInterface`.

## 2. Stream API
| Intermediate (lazy, returns Stream) | Terminal (runs, returns result) |
|---|---|
| `filter`, `map`, `sorted`, `distinct`, `limit`, `skip`, `flatMap`, `peek` | `collect`, `forEach`, `count`, `reduce`, `anyMatch`/`allMatch`/`noneMatch`, `findFirst`, `min`, `max`, `toArray` |

Facts: **lazy** (nothing until terminal op), **single-use**, **doesn't modify the source**.

## 3. QE example
```java
// Collect visible link texts
List<String> links = driver.findElements(By.tagName("a"))
        .stream()
        .filter(WebElement::isDisplayed)
        .map(WebElement::getText)
        .filter(t -> !t.isEmpty())
        .collect(Collectors.toList());

// Find a table row, fail clearly if absent
WebElement row = driver.findElements(By.cssSelector("tr"))
        .stream()
        .filter(r -> r.getText().contains("TX123"))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("Row not found"));

// Validate a batch
boolean allOk = responses.stream().allMatch(Response::isSuccess);
```

---

## Depth ladder
1. **`InputStream` vs `Stream`?** → I/O bytes/chars vs functional pipeline; unrelated.
2. **`map` vs `flatMap`?** → 1-to-1 transform vs 1-to-many + flatten.
3. **Why lazy?** → builds the pipeline; nothing runs until the terminal op → allows short-circuiting.
4. **Reuse a stream?** → No, single-use; consumed stream throws `IllegalStateException`.
5. **QE nugget:** never use `parallelStream()` with WebDriver — `WebDriver` is **not thread-safe**.

---

## Memorize
1. **"`java.io` streams = I/O; `java.util.stream` = pipeline over collections. Different things."**
2. **"Stream: source → intermediate (lazy: `filter`/`map`) → one terminal (`collect`/`forEach`/`findFirst`). Single-use."**
3. **"Lambdas implement functional interfaces (one abstract method) — `Comparator`, `Predicate`, `Function`, `Consumer`."**

## Likely follow-ups
- `map` vs `flatMap`, `filter` vs `findFirst`?
- Why not `parallelStream()` in tests? → WebDriver isn't thread-safe.
- What's a functional interface? → exactly one abstract method.
- `Comparator` vs `Comparable`? → external comparator vs natural ordering (`compareTo`).
