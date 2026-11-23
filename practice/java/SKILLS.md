# Java Skills Matrix

Live view of where I actually am. Updated after every session.

**Target:** Java coding round for SDET **and** the Java used inside an enterprise framework (streams,
generics, exceptions, `equals`/`hashCode`) · no language internals
**Deadline:** Citi round 3 — Tue 2026-11-24

**Last updated:** 2026-11-23 · **Sessions:** 5 · **Next up:** Citi round 3 (Tue 2026-11-24); no new Java before it

## Legend

| Label | Meaning |
|---|---|
| `not assessed` | Never asked |
| `unknown` | Asked, wrong or blank |
| `shaky` | Partially right, missed the trap |
| `solid` | Correct answer and working code |
| `strong` | Can teach it, including trade-offs |

## Language core

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| OOP pillars, abstract vs interface | not assessed | — | 5 questions after collections |
| `==` vs `.equals()`, string pool | shaky | S1 Q1: said `a == b` is false; literals are pooled | Re-ask with runtime-built strings |
| String immutability, `StringBuilder` | solid | S1 Q2 + S3 palindrome + S5 reverse without `StringBuilder` | — |
| `split`, regex basics | shaky | S2: literal `split(" ")`; S3b: `split("@",1)`; S4: `split(" ")` on single-spaced input | Re-test on `"  a   b "` |
| `final`, `static`, inner classes | not assessed | — | — |
| Overloading vs overriding | not assessed | — | — |
| Two pointers / `charAt` scan | solid | S3: palindrome with `start`/`end`, compiles, correct on 7 inputs | — |
| `char[]` + `Arrays` helpers | solid | S3: anagram with `toCharArray`/`sort`/`equals`, compiles, output correct | — |
| `int[]` traversal, in-place swap | solid | S4: `while (start < end)` + temp swap, `[5,4,3,2,1]` correct | — |

## Collections

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| List basics: `add`, `get(i)`, loop | solid | S3–S4: own loops over lists in `EmailDomains`, `WordCount`, `TopWord` | — |
| List choice: `ArrayList` vs `LinkedList` | unknown | S1 Q3 blank | Benchmark drill |
| Map semantics: `put`, `get`, `getOrDefault`, `merge` | solid | S3b: `merge` used in `EmailDomains`; S3: `Map.equals` in anagram, outputs correct | — |
| `HashMap` internals, `equals`/`hashCode` | unknown | S1 Q4 blank; S5 patch: bucket behaviour shown (set size 2 with equals only) | Q1 + `Person` drill |
| Set implementations and iteration order | solid | S5: `LinkedHashSet` dedupe preserving first-seen order, `[3,1,2,4]` correct | — |
| Sorted output: `TreeMap`, `entrySet` | solid | S3–S5: `entrySet` → `ArrayList` → comparator, five drills | — |
| `LinkedHashMap`, iteration order | not assessed | — | — |
| Iteration + `ConcurrentModificationException` | not assessed | — | — |
| `Comparable` vs `Comparator` | solid | S3b: sort in `EmailDomains`; S4: `WordCount` sort with tie-break, both inputs correct | — |
| `println` / `toString` output | shaky | S2: saw `AbstractMap.toString`; S4: printed `HashMap` and assumed it was sorted | Add `toString` to `Person` |

## Exceptions and resources

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `throw` vs `throws` | solid | S1 Q6: correct on both | — |
| `finally` edge cases | shaky | S1 Q6 + follow-up: `return` in `finally` overrides | Show a cleanup block that does not swallow |
| `try-with-resources`, suppressed exceptions | not assessed | — | Custom `AutoCloseable` drill |
| Checked vs unchecked, custom exceptions | not assessed | — | — |

## Streams and lambdas

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `map` vs `flatMap` | unknown | S1 Q5 blank; S5 patch: `filter`/`map`/`sorted`/`toList` shown | `Squares` task + Q2 |
| Stream is lazy, single-use | unknown | S1 Q5 blank; S5 patch: laziness stated | Q2 |
| Collectors, `groupingBy`, `partitioningBy` | not assessed | — | — |
| Functional interfaces, method references | shaky | S5: `String::length` and merge-`Integer::sum` right; `Math.max` written as `Integer::max` | Re-test `Math::max` vs `Integer::max` |

## Enums

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Enum is a class; constants are single instances | shaky | S3 Q2: `==` compares references, `new String` makes a new object | Parked — not in a coding round |
| Fields, constructor, methods on enum | not assessed | — | Parked |
| `name()` vs `toString()` vs `valueOf` | unknown | S3 Q1 blank | Parked; only `valueOf` + `try/catch` matters |
| `ordinal()` is not a stable id | unknown | S3 Q3 blank | Parked |
| `switch` over enum, exhaustiveness | not assessed | — | Parked |
| `EnumSet` / `EnumMap` | not assessed | — | Parked |
| Enum as map key / persisted status | not assessed | — | Parked |

## Not yet assessed

Generics and wildcards · concurrency and `ThreadLocal` · JVM memory and GC · modern Java (records,
sealed, pattern matching, virtual threads) · design patterns · OOP theory (abstract vs interface,
overloading vs overriding).

**Done since this list was written:** arrays, strings, two pointers, maps, comparator (S3–S5).

## Current focus order

1. **Java used in the framework** — streams/lambdas, `equals`/`hashCode`, generics. These are what a
   framework round asks about, and all three are still `unknown`.
2. **Selenium stages** — CI (done), layers/config, waits, parallel, data seeding.
3. **Coding round** — already `solid` on the six patterns; keep warm with one drill a week.
4. **Theory** — abstract vs interface, overloading vs overriding, `ArrayList` vs `LinkedList`.
5. Streams → generics → concurrency → JVM → modern Java (only if the role asks).

## Recurring traps to re-test every session

- Literal vs runtime `String` identity (`==`).
- "Duplicate key throws" — it overwrites.
- Instantiating an interface (`new Set`, `new Map`).
- Primitive type arguments (`HashMap<String, int>`).
- `split(" ")` on messy input.
- Overriding `equals` without `hashCode`.
- Mutable field used in `hashCode`, then mutated after insertion.
- `LinkedList.get(i)` is O(n), not O(1).
- Reusing a consumed `Stream`.
- `valueOf("failed")` when `toString()` was overridden — it parses `name()`, case-sensitive.
- Persisting `ordinal()` instead of `name()`.
- Methods without `()`: `getKey`, `charAt[i]`, `entrySet`, `equalsTo`.
- `List<Map<...>>` when you meant `List<Map.Entry<...>>`.
- `0` used as "not found" sentinel when `0` is a valid value.
- Printing a `Map` and assuming it is sorted — hash order is arbitrary.
