# Java — Collections (List / Set / Map)

## Say it right
> "`List`, `Set`, `Queue` come under `Collection`. **`Map` is separate — it's not a Collection.** For lists I default to **ArrayList**, use **LinkedList** for frequent insert/delete. For maps: **HashMap**, **LinkedHashMap** when order matters, **ConcurrentHashMap** for thread safety."
>
> ⚠️ **HashMap / ConcurrentHashMap / HashSet are NOT ordered.** Insertion order → `LinkedHashMap`; sorted → `TreeMap`.

---

## Hierarchy
```
Iterable → Collection → List   → ArrayList, LinkedList, Vector, Stack
                      → Set    → HashSet, LinkedHashSet, TreeSet
                      → Queue  → PriorityQueue, ArrayDeque

Map (NOT a Collection) → HashMap, LinkedHashMap, TreeMap, Hashtable, ConcurrentHashMap
```

| Wants | Use |
|---|---|
| No order needed | `HashSet` / `HashMap` |
| **Insertion order** | `LinkedHashSet` / `LinkedHashMap` |
| **Sorted order** | `TreeSet` / `TreeMap` |
| Thread-safe | `ConcurrentHashMap`, `CopyOnWriteArrayList`, `Collections.synchronizedMap()` |

---

## ArrayList vs LinkedList

| | ArrayList | LinkedList |
|---|---|---|
| Backing | resizable **array** | **doubly-linked** nodes |
| `get(index)` | **O(1)** fast | O(n) — walks |
| Insert/delete middle | O(n) — shifts | **O(1)** if node known |
| Memory | less | more (pointers) |
| Also implements | `List` | `List`, `Deque`, `Queue` |

> "ArrayList for fast random access (default); LinkedList for frequent insert/delete, and it doubles as a queue/deque."

---

## HashMap vs Hashtable vs ConcurrentHashMap

| | HashMap | Hashtable | ConcurrentHashMap |
|---|---|---|---|
| Thread-safe | ❌ | ✅ (whole-table lock) | ✅ (fine-grained) |
| Null key/value | 1 null key, null values | ❌ | ❌ |
| Speed | fastest single-thread | slowest | good under concurrency |
| Legacy | no | yes | no |

> "Hashtable locks the **entire** map → kills concurrency. `ConcurrentHashMap` locks only the **bucket/node** (Java 8+: CAS + synchronized on node) → modern choice."

---

## Depth ladder — HashMap internals
1. **`put` flow:** `hash(key)` → index `hash & (n-1)` → bucket; collisions → **chaining**.
2. **Java 8:** bucket with **≥ 8 entries** (capacity ≥ 64) converts to a **red-black tree** → O(log n) lookup.
3. **Defaults:** capacity **16**, load factor **0.75** → resize (double) past **12** entries.
4. **Why override `hashCode` for keys?** → equal objects must have equal hashCodes, else lookups fail.
5. **`ConcurrentModificationException`:** HashMap iterators are **fail-fast**; modify while iterating → throws. Fix: `Iterator.remove()` or use `ConcurrentHashMap`.
6. **Testing examples:** `findElements()` → `List<WebElement>`; `getWindowHandles()` → **unordered Set**; test data/API payloads → `Map<String,Object>` (`LinkedHashMap` if order matters).

---

## Memorize
1. **"List/Set/Queue are Collections; Map is NOT. HashMap = unordered, LinkedHashMap = insertion order, TreeMap = sorted."**
2. **"ArrayList = O(1) random access, default; LinkedList = O(1) insert/delete, also a Deque."**
3. **"HashMap not thread-safe; Hashtable locks everything; ConcurrentHashMap locks per-bucket — none are ordered."**

## Likely follow-ups
- Which map preserves order? → `LinkedHashMap` (insertion), `TreeMap` (sorted).
- Is `Map` a `Collection`? → No.
- Why `ConcurrentHashMap` over `synchronizedMap`? → finer-grained locking, much better concurrency.
- Can a HashMap key be null? → Yes, one null key allowed; Hashtable/ConcurrentHashMap disallow null.
