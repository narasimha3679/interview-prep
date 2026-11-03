# Java — Generics

## 1. Why generics

Type safety at compile time, no casts, reusable algorithms.

```java
List<String> names = new ArrayList<>();
names.add("Ana");
String first = names.get(0); // no cast
```

Without generics, `List` holds `Object` and errors appear at runtime.

---

## 2. Type parameters

```java
class Box<T> {
    private T value;
    void set(T value) { this.value = value; }
    T get() { return value; }
}

Box<String> box = new Box<>();
```

Conventions: `T` type, `E` element, `K`/`V` key/value, `R` result.

---

## 3. Bounded type parameters

```java
static <T extends Comparable<T>> T max(List<T> items) {
    T best = items.get(0);
    for (T item : items) {
        if (item.compareTo(best) > 0) best = item;
    }
    return best;
}
```

`extends` means "is a subtype of" (for classes) or "implements" (for interfaces).

---

## 4. Wildcards

| Wildcard | Meaning | Use |
|---|---|---|
| `?` | Unknown type | Read-only |
| `? extends T` | T or subtype | Producer (read) |
| `? super T` | T or supertype | Consumer (write) |

**PECS:** Producer Extends, Consumer Super.

```java
void printAll(List<? extends Number> numbers) {
    for (Number n : numbers) System.out.println(n);
}

void addInts(List<? super Integer> target) {
    target.add(1);
}
```

---

## 5. Type erasure

Generics are a compile-time feature; the runtime sees raw types.

```java
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
// a.getClass() == b.getClass() -> true
```

Consequences:

- Cannot `new T()`.
- Cannot `instanceof List<String>`.
- Cannot create generic arrays safely.
- Overloads with the same erasure clash.

Workarounds: `Class<T>` token, factory, reflection.

---

## 6. Generic methods

```java
static <T> List<T> listOf(T... items) {
    return List.of(items);
}
```

---

## 7. Common pitfalls

1. **Raw types** (`List list`) — lose type safety.
2. **`List<Object>` vs `List<?>`** — `List<Object>` accepts any list, `List<?>` is read-only.
3. **Arrays vs generics** — arrays are covariant and reified; generics are invariant and erased.
4. **Heap pollution** — unchecked casts mixing raw/generic types.
5. **Wildcard overuse** — start concrete; add wildcards at API boundaries.
6. **`@SuppressWarnings("unchecked")`** — document why, keep scope minimal.

---

## 8. Testing implications

- Generic helpers still need runtime validation of external data.
- A `Page<T>`/`ApiResponse<T>` type is a compile-time claim, not proof.
- Test with multiple types where the logic is type-sensitive.
- Watch for `ClassCastException` at boundaries (JSON, reflection).

---

## 9. Generics checklist

- [ ] No raw types.
- [ ] Bounds express the contract.
- [ ] PECS applied at API boundaries.
- [ ] No unnecessary casts.
- [ ] Runtime validation at external boundaries.
- [ ] Unchecked warnings documented.
