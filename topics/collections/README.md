# Java Collections — Interview Prep

Everything you need to answer **Collections Framework** questions confidently and correctly.
This is the topic interviewers use to find out whether you actually know Java or just copied
code from Stack Overflow. The traps are all the same every time: **`equals`/`hashCode`**,
**`HashMap` internals**, **fail-fast iteration**, and **which implementation to pick**.

Read in this order:

1. `README.md` (this file) — cram sheet, Big-O, the traps
2. `01_qa_bank.md` — questions with model answers, grouped by topic
3. `code/` — runnable demos; each file proves one behavior. Run them.
4. `04_...` (n/a here) — rehearse out loud, then run every demo from memory.

---

## The 60-second cram sheet

If you read nothing else, be able to say these fluently.

**The split:** `Collection` has three children — **`List`** (ordered, duplicates, index access),
**`Set`** (no duplicates), **`Queue`/`Deque`** (order of processing). **`Map` is NOT a Collection** —
it maps keys to values and lives in its own branch.

**One line per implementation:**

| Interface | Ordered? | Duplicates? | Null? | Best pick | Backed by |
|---|---|---|---|---|---|
| `ArrayList` | insertion order | yes | yes | default `List` | resizable array |
| `LinkedList` | insertion order | yes | yes | queue/deque; `addFirst`/`removeLast` | doubly-linked list |
| `HashSet` | **no** | no | one `null` | default `Set`, fast `contains` | `HashMap` |
| `LinkedHashSet` | insertion order | no | one `null` | Set with stable order | `LinkedHashMap` |
| `TreeSet` | **sorted** | no | **no** | sorted / range queries | red-black tree |
| `HashMap` | **no** | keys unique | 1 `null` key | default `Map` | array of buckets + tree |
| `LinkedHashMap` | insertion order | keys unique | 1 `null` key | predictable iteration order | `HashMap` + linked list |
| `TreeMap` | **sorted by key** | keys unique | **no** | sorted / `firstKey`, `floorKey` | red-black tree |
| `Hashtable` | no | keys unique | **no** | legacy; don't use | synchronized |
| `ConcurrentHashMap` | no | keys unique | **no** | **thread-safe** map | lock striping / CAS |

**The three killer lines:**

- *"`ArrayList` is the default list. `LinkedList` only wins when you're inserting/removing at the
  ends a lot — for indexed `get`, it's O(n), so it loses to `ArrayList`."*
- *"`HashMap` gives O(1) average `get`/`put`. It's an array of buckets; the key's `hashCode()`
  picks the bucket and `equals()` finds the entry inside it."*
- *"If you override `equals()` you **must** override `hashCode()`, or `HashMap`/`HashSet` will
  fail to find objects that are actually equal."*

**Complexity cheat (average / good to recite):**

| Operation | `ArrayList` | `LinkedList` | `HashSet`/`HashMap` | `TreeSet`/`TreeMap` |
|---|---|---|---|---|
| `get(i)` / `contains` | O(1) | O(n) | O(1) avg | O(log n) |
| `add` at end | O(1)* | O(1) | O(1) avg | O(log n) |
| `add`/`remove` at index | O(n) | O(n) | — | — |
| `add`/`remove` at front | O(n) | O(1) | — | — |
| iterate | O(n) | O(n) | O(n) | O(n) |

\* amortised — occasionally O(n) to resize.

---

## The concepts you must be able to explain

### 1. Hierarchy

```
Iterable
└── Collection
    ├── List    → ArrayList, LinkedList, Vector
    ├── Set     → HashSet, LinkedHashSet, TreeSet
    └── Queue   → ArrayDeque, PriorityQueue, LinkedList
Map                                             ← separate! not a Collection
 ├── HashMap, LinkedHashMap, TreeMap, Hashtable, ConcurrentHashMap
```

`Map` isn't a `Collection` because it deals in **key→value pairs**, not single elements.
You reach its contents via `keySet()`, `values()`, and `entrySet()`.

### 2. `HashMap` internals (the "how deep do you know Java" question)

Memorise this story — it's a guaranteed question:

1. A `HashMap` is an **array of buckets** (default capacity **16**, load factor **0.75**).
2. On `put`, Java calls `key.hashCode()`, spreads the bits (`h ^ (h >>> 16)`) to reduce collisions,
   and masks it to an index: `index = (n - 1) & hash`. Capacity is a **power of two** so this
   masking works like a fast modulo.
3. If the bucket is empty → store there. If occupied → compare with `equals()` and either replace
   the value (same key) or add to the bucket (**collision**).
4. Since Java 8, when a bucket holds **> 8 entries** it converts from a linked list to a
   **red-black tree**, so worst-case lookup goes from O(n) to **O(log n)**.
5. When size exceeds `capacity * loadFactor` (16 * 0.75 = 12), it **doubles** capacity and rehashes.
6. **`null` key is allowed** — it goes in bucket 0. (`TreeMap`/`ConcurrentHashMap` reject `null`.)

**Why is `String`/`Integer` a good key?** They're **immutable** and correctly implement
`equals`/`hashCode`, so the hash can't change underneath the map.

### 3. `equals()` / `hashCode()` contract (the #1 gotcha)

Rules:
- Equal objects **must** have equal hash codes.
- Unequal objects **may** share a hash code (that's just a collision).
- Equal hash codes do **not** imply equality — `equals` is the final word.

Break it and you get one of the classic interview bugs:

```java
class Key { int id; }              // no equals/hashCode
map.put(new Key(1), "a");
map.get(new Key(1));               // null -> different hash, different bucket
```

Fix: override both. Use `Objects.hash(...)` / `Objects.equals(...)`, or generate in the IDE.
`record` types get correct `equals`/`hashCode` for free.

**Mutation trap:** never mutate a field that feeds `hashCode()` while the object is a map key —
the entry becomes unreachable. See `code/EqualsHashCodeDemo.java`.

### 4. `Comparable` vs `Comparator`

| | `Comparable` | `Comparator` |
|---|---|---|
| Package | `java.lang` | `java.util` |
| Method | `int compareTo(T o)` | `int compare(T a, T b)` |
| Location | inside the class (natural order) | external, many per class |
| Use | "this type has a default order" | "sort by these rules" |

- `Collections.sort(list)` / `list.sort(...)` / `TreeSet`/`TreeMap` use it.
- Modern style: `Comparator.comparing(Person::getName).thenComparing(Person::getAge)`, and
  `Comparator.nullsFirst(...)` for nulls.
- Return values: **negative** = a before b, **0** = equal, **positive** = a after b.
  Don't use `a - b` for ints (overflow) → use `Integer.compare(a, b)`.

### 5. Fail-fast vs fail-safe

- **Fail-fast** — `ArrayList`, `HashMap`, etc. use a `modCount`; if the collection is structurally
  modified while iterating (except via the iterator itself), the next `next()` throws
  **`ConcurrentModificationException`**.
- **Correct ways to remove while looping:**
  1. `Iterator.remove()`
  2. `list.removeIf(x -> ...)` ← preferred
  3. `map.entrySet().removeIf(...)`
- **Fail-safe** — `ConcurrentHashMap`, `CopyOnWriteArrayList` iterate over a snapshot and don't throw.
- **Trap:** `for (; i < list.size(); i++)` while removing shifts indexes and skips elements.
  Loop **backwards**, or use `removeIf`.

See `code/ConcurrentModificationDemo.java`.

### 6. Thread safety

- Default collections are **not** thread-safe.
- Legacy: `Collections.synchronizedList(new ArrayList<>())` — you still must `synchronize` manually
  when iterating.
- Modern: `ConcurrentHashMap` (high concurrency, no locking the whole map), `CopyOnWriteArrayList`
  (great for read-mostly listener lists), `ConcurrentLinkedQueue`.
- `HashMap` under concurrency can corrupt / loop forever — a favorite horror-story question.

### 7. Immutability and "fixed-size" traps

| Expression | Mutable? | Fixed size? | Nulls allowed? |
|---|---|---|---|
| `new ArrayList<>(...)` | yes | no | yes |
| `Arrays.asList(a, b)` | `set` yes, `add`/`remove` **no** (fixed-size view) | yes | yes |
| `List.of(a, b)` | **no** | yes | **no** |
| `Collections.unmodifiableList(list)` | no (view) | mirrors backing | mirrors |
| `List.copyOf(list)` | no | yes | no |

`Arrays.asList` is a **view over the array**: `set()` writes through to the array, and the array
writes show in the list. Great gotcha.

---

## The gotchas they *will* try on you

1. **`list.remove(1)` vs `list.remove(Integer.valueOf(1))`** — on a `List<Integer>`, `remove(int)`
   removes by **index**, `remove(Object)` removes by **value**. `remove(1)` → removes index 1;
   `remove(Integer.valueOf(1))` → removes the number 1. Recite this.
2. **`Arrays.asList` / `List.of` are not `ArrayList`** — `add` throws
   `UnsupportedOperationException`.
3. **`==` vs `.equals()`** — `Integer a = 127, b = 127; a == b` is `true` (Integer cache,
   −128..127), but `128 == 128` for boxed `Integer` is `false`. Use `.equals()`.
4. **Mutating a key after `put`** — entry is lost.
5. **`HashMap` iteration order is not stable** across runs/versions; use `LinkedHashMap` if order matters.
6. **`TreeMap`/`TreeSet` with a bad comparator** can drop elements as "duplicates."
7. **`hashCode()` returning a constant** → everything in one bucket → O(n), not O(1).
8. **`ConcurrentModificationException`** from `list.remove(x)` inside a for-each.
9. **`PriorityQueue` is not sorted when you iterate** — it only guarantees `poll()` order.
10. **`contains` on a `List` is O(n)**; convert to a `Set` when you do it in a loop.

---

## "Which should I use?" — say this

- Key→value lookups → **`HashMap`** (or `LinkedHashMap`/`TreeMap` if order matters).
- Unique elements / de-dup / membership → **`HashSet`** (or `LinkedHashSet`).
- Ordered/indexed sequence → **`ArrayList`**.
- Stack/queue/deque work → **`ArrayDeque`** (prefer it over legacy `Stack` and over `LinkedList`).
- Sorted / range / nearest-key queries → **`TreeMap` / `TreeSet`**.
- Concurrent access → **`ConcurrentHashMap`** / **`CopyOnWriteArrayList`**.
- Fixed, known, immutable constants → **`List.of` / `Map.of`** / **`Set.of`**.

---

## How to use this pack

- **10 min:** this README, top to bottom.
- **30 min:** `01_qa_bank.md` — answer out loud, then read the model answer.
- **30 min:** `cd code`, compile and run every demo, then edit them to break things.
- **Ongoing:** keep the two tables and the 10 gotchas in front of you the morning of.
