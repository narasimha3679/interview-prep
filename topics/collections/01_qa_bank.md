# Collections Q&A Bank

Answer each one **out loud** first, then read the model answer. The model answer is what a strong
candidate says — concise, specific, with the right keyword.

---

## A. Fundamentals

**A1. What is the Java Collections Framework?**
> "A set of interfaces and implementations for storing and manipulating groups of objects —
> `List`, `Set`, `Queue`, and `Map` — plus algorithms in `Collections` and the stream-friendly
> `Collectors`. It replaced hand-written arrays and `Vector`/`Hashtable` with a consistent API."

**A2. Difference between `Collection` and `Collections`?**
> "`Collection` is the root **interface** for `List`/`Set`/`Queue`. `Collections` is a **utility
> class** of static methods — `sort`, `reverse`, `unmodifiableList`, `synchronizedMap`."

**A3. Why is `Map` not a `Collection`?**
> "Because it stores **key-value pairs**, not individual elements. Its API is about keys and
> entries, so it can't sensibly implement `add(E)` or `contains(Object)`."

**A4. `Iterable` vs `Iterator`?**
> "`Iterable` means 'can give me an `Iterator`' — it enables for-each. `Iterator` is the cursor
> with `hasNext()`, `next()`, `remove()`. Every `Collection` is `Iterable`."

**A5. Array vs `ArrayList`?**
> "Array is fixed-size, can hold primitives, and is faster/lighter. `ArrayList` grows dynamically,
> holds objects (autoboxing for primitives), and gives you `add`/`remove`/`contains`."

---

## B. List

**B1. `ArrayList` vs `LinkedList`?**
> "`ArrayList` is a resizable array: O(1) indexed `get`, amortised O(1) append, O(n) insert/remove in
> the middle. `LinkedList` is a doubly-linked list: O(1) insert/remove at the ends if you already
> hold the node, but O(n) `get(i)` because it walks the chain. In practice I default to `ArrayList`;
> I reach for `ArrayDeque`/`LinkedList` when the workload is queue-like at the ends."

**B2. How does `ArrayList` grow?**
> "It starts at capacity 10 (or 0 for the empty constructor), and when full it grows by
> **1.5x** (`oldCapacity + (oldCapacity >> 1)`) via `Arrays.copyOf`. That copy is why append is
> 'amortised' O(1). If I know the size, I pass it to the constructor to avoid resizes."

**B3. `ArrayList` vs `Vector`?**
> "`Vector` is the legacy synchronized version. Its per-method locking is coarse and slow, so
> it's effectively deprecated in favour of `ArrayList` or `CopyOnWriteArrayList`."

**B4. How do you remove while iterating a list?**
> "With `Iterator.remove()`, or `list.removeIf(predicate)` which is cleaner. Calling
> `list.remove()` inside a for-each throws `ConcurrentModificationException`."

**B5. How do you sort a `List`?**
> "`list.sort(comparator)` or `Collections.sort(list)`. `Collections.sort` delegates to the same
> stable merge sort (TimSort). For natural order the elements need to be `Comparable`."

---

## C. Set

**C1. `HashSet` vs `LinkedHashSet` vs `TreeSet`?**
> "`HashSet` has no order and O(1) ops. `LinkedHashSet` keeps **insertion order** with a linked
> list over the same hash table. `TreeSet` keeps elements **sorted** (a red-black tree) and gives
> O(log n) ops plus range methods like `floor`, `ceiling`, `headSet`."

**C2. How does `HashSet` guarantee uniqueness?**
> "It's backed by a `HashMap`; the element is the key, and a constant is the value. Uniqueness is
> enforced by `hashCode` + `equals`."

**C3. Can a `Set` contain `null`?**
> "`HashSet` and `LinkedHashSet` allow one `null`. `TreeSet` does not — it needs to compare, and
> throws `NullPointerException`."

**C4. How do you de-duplicate a `List`?**
> "`new LinkedHashSet<>(list)` to preserve order, or `list.stream().distinct().collect(...)`
> (which also uses `equals`)."

---

## D. Map

**D1. `HashMap` vs `Hashtable` vs `ConcurrentHashMap`?**
> "`HashMap` is not thread-safe and allows one null key. `Hashtable` is legacy, fully synchronized,
> and rejects nulls — avoid it. `ConcurrentHashMap` is the modern thread-safe map: it doesn't lock
> the whole map (lock striping / CAS) so reads are largely concurrent, and it rejects nulls."

**D2. `HashMap` vs `LinkedHashMap` vs `TreeMap`?**
> "`HashMap` has no order. `LinkedHashMap` preserves **insertion order** (access-order too, if you
> enable it — that's how you build an LRU cache via `removeEldestEntry`). `TreeMap` sorts by key and
> supports range/nearest queries."

**D3. How does `HashMap` work internally?**
> "It's an array of buckets. `put` computes `hash(key)`, spreads the high bits, and masks to an
> index since capacity is a power of two. Collisions form a linked list, which becomes a red-black
> tree once a bucket exceeds 8 entries (Java 8+). It resizes when `size > capacity * 0.75`.
> `equals` decides whether two colliding keys are actually the same."

**D4. What if two keys have the same `hashCode`?**
> "They land in the same bucket and are chained; `equals` picks the right one. Same hash, different
> equals → two distinct entries. That's why equal objects must share a hash but equal hashes don't
> imply equal objects."

**D5. Why override both `equals` and `hashCode`?**
> "`HashMap`/`HashSet` use `hashCode` to find the bucket and `equals` to find the entry. Override
> only `equals` and two 'equal' keys can land in different buckets, so `get` returns null."

**D6. How do you iterate a map?**
> "`map.entrySet()` for key+value in one pass — best for performance. `keySet()` or `values()` if I
> only need one side. `forEach((k, v) -> ...)` in Java 8+. `TreeMap` can't be iterated in sorted
> order any other way, so `entrySet` is the answer."

**D7. `computeIfAbsent` / `merge` vs `getOrDefault`?**
> "`getOrDefault(k, def)` is for reading. `computeIfAbsent(k, fn)` only builds the value if the key
> is missing — that's how you group into `Map<String, List<X>>`. `merge(k, v, fn)` is the concise
> 'count occurrences' idiom: `map.merge(word, 1, Integer::sum)`."

**D8. Can a `HashMap` be an LRU cache?**
> "Yes — `new LinkedHashMap<>(cap, 0.75f, true)` with access order, and override
> `removeEldestEntry` to evict when size > max. For a real cache I'd use Caffeine."

---

## E. equals / hashCode

**E1. State the contract.**
> "Reflexive, symmetric, transitive, consistent, and `x.equals(null)` is false. And crucially:
> equal objects **must** return equal hash codes; unequal objects may collide."

**E2. What happens if a key is mutated after `put`?**
> "The entry becomes unreachable — the stored bucket was computed from the old hash, so `get`
> with the new hash looks in the wrong place. Map keys should be immutable."

**E3. `a == b` vs `a.equals(b)`?**
> "`==` compares references (or primitive values). `equals` compares logical equality. For boxed
> `Integer`, `==` only 'works' in the −128..127 cache, so always use `.equals` for objects."

---

## F. Sorting & comparison

**F1. `Comparable` vs `Comparator`?**
> "`Comparable` is implemented by the class and defines natural order (`compareTo`). `Comparator`
> is external and lets you define many orderings without touching the class (`compare`). I use
> `Comparator.comparing(...).thenComparing(...)` for multi-key sorts."

**F2. How do you sort a `Map` by value?**
> "Stream the entry set and collect to a `LinkedHashMap`:
> `map.entrySet().stream().sorted(Map.Entry.comparingByValue())
> .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a,b)->a, LinkedHashMap::new))`."

**F3. Is sorting stable?**
> "`Collections.sort`/`List.sort` use TimSort and are **stable** — equal elements keep their
> relative order. That's why `thenComparing` chains work."

**F4. Why not `(a, b) -> a - b` in a comparator?**
> "Integer overflow can flip the sign. Use `Integer.compare(a, b)` or `Comparator.comparingInt`."

---

## G. Concurrency

**G1. Is `ArrayList`/`HashMap` thread-safe?**
> "No. Concurrent writes can corrupt the structure — historically `HashMap` could even loop forever
> during resize. I'd use `ConcurrentHashMap` or synchronize externally."

**G2. `synchronizedList` vs `CopyOnWriteArrayList`?**
> "`synchronizedList` locks every operation, and you must still synchronize manually while
> iterating. `CopyOnWriteArrayList` copies the array on write, so reads/iterations are lock-free and
> never throw `ConcurrentModificationException` — ideal when reads dominate writes."

**G3. How does `ConcurrentHashMap` achieve concurrency?**
> "It avoids a single global lock: reads are lock-free using volatile reads, and writes lock only
> the affected bin (JDK 7 used segments; JDK 8+ locks individual bins + CAS for empty bins). It also
> gives atomic `computeIfAbsent`, `merge`, and `putIfAbsent`. It disallows null keys/values."

---

## H. Streams & Collectors (they'll connect it)

**H1. `Collectors.toMap` collision?**
> "It throws `IllegalStateException` on duplicate keys unless you pass a merge function — usually
> `(a, b) -> b` (keep the newer) or a combining function."

**H2. `groupingBy` vs `partitioningBy`?**
> "`groupingBy` buckets by a function into `Map<K, List<V>>`. `partitioningBy` is the special case
> of a boolean predicate → at most two buckets under `Boolean` keys."

**H3. When would you *not* use streams?**
> "Tight loops where allocation matters, when it hurts readability, or when I need index-based
> mutation. Streams are great for transforms and aggregations, not for mutating in place."

---

## I. The rapid-fire round

| Question | One-line answer |
|---|---|
| Default `ArrayList` capacity | 10 |
| `HashMap` default capacity / load factor | 16 / 0.75 |
| `HashMap` treeify threshold | bucket length > 8 → red-black tree |
| List that allows duplicates + index access | `ArrayList` |
| Set with insertion order | `LinkedHashSet` |
| Sorted map | `TreeMap` |
| Map that allows null key | `HashMap`, `LinkedHashMap` |
| Map that is thread-safe | `ConcurrentHashMap` |
| Remove while iterating | `Iterator.remove()` / `removeIf` |
| De-dup a list keeping order | `new LinkedHashSet<>(list)` |
| Count word frequency | `map.merge(w, 1, Integer::sum)` |
| O(1) membership test | `HashSet.contains` |
| Immutable list literal | `List.of(...)` |
| Best stack/queue | `ArrayDeque` |
| `LinkedList` worst case `get(i)` | O(n) |
| `TreeMap.get` | O(log n) |
| Legacy synchronized map | `Hashtable` |
| Stable sort algorithm | TimSort |
| `Arrays.asList` add() | throws `UnsupportedOperationException` |
| `List<Integer>.remove(1)` | removes **index** 1, not value 1 |

---

## J. Questions to ask *them* about collections

- "Do you have a convention for which collection types the team uses, or is it per-service?"
- "Do you lean on streams/collectors, or a loops-first style?"
- "Any rules around thread-safe collections in your shared libraries?"
- "How do you handle large result sets — pagination, streaming, or bounded collections?"
