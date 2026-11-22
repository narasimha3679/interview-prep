# Java Skills Matrix

Live view of where I actually am. Updated after every session.

**Target:** Java coding round for SDET · depth capped at round level, no language internals
**Deadline:** Citi round 3 — Tue 2026-11-24

**Last updated:** 2026-11-21 · **Sessions:** 3 · **Next up:** `RunSummary` coding-round task (enums capped at round level)

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
| String immutability, `StringBuilder` | solid | S1 Q2: new object per concat; knew to append | Compare with `StringBuffer` cost |
| `split`, regex basics | shaky | S2: guessed literal `split(" ")`; fixed to `\\s+` | Re-test `split(".")` and trailing empties |
| `println` / `toString` output | shaky | S2: saw `AbstractMap.toString`; no own code yet | Add `toString` to `Person` |
| `final`, `static`, inner classes | not assessed | — | — |
| Overloading vs overriding | not assessed | — | — |

## Collections

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| List basics: `add`, `get(i)`, loop | shaky | S2: read `CollectionsBasics`, no own code | Write a List loop unassisted |
| List choice: `ArrayList` vs `LinkedList` | unknown | S1 Q3 blank | Benchmark drill |
| Map semantics: `put`, `get`, `getOrDefault`, `merge` | shaky | S2 Q1: thought duplicate key fails; Q3 loop correct | Explain `put` return value |
| `HashMap` internals, `equals`/`hashCode` | unknown | S1 Q4 blank | `Person` drill: break it, then fix it |
| Set implementations and iteration order | shaky | S2: did not know any of the three; saw the demo | Pick the right Set in 3 scenarios |
| Sorted output: `TreeMap`, `entrySet` | unknown | S2: taught, not attempted | Finish Q3 with `Integer` + `TreeMap` |
| `LinkedHashMap`, iteration order | not assessed | — | — |
| Iteration + `ConcurrentModificationException` | not assessed | — | — |
| `Comparable` vs `Comparator` | not assessed | — | — |

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
| `map` vs `flatMap` | unknown | S1 Q5 blank | Explain + drill |
| Stream is lazy, single-use | unknown | S1 Q5 blank | Reproduce `IllegalStateException` |
| Collectors, `groupingBy`, `partitioningBy` | not assessed | — | — |
| Functional interfaces, method references | not assessed | — | `Integer::sum` seen in `merge` |

## Enums

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| Enum is a class; constants are single instances | shaky | S3 Q2: `==` compares references, `new String` makes a new object | Not needed for the round — stop drilling |
| Fields, constructor, methods on enum | not assessed | — | Out of scope (round level) |
| `name()` vs `toString()` vs `valueOf` | unknown | S3 Q1 blank | Only `valueOf` + `try/catch` — in task R1 |
| `ordinal()` is not a stable id | unknown | S3 Q3 blank | Out of scope (round level) |
| `switch` over enum, exhaustiveness | not assessed | — | Covered in the round task |
| `EnumSet` / `EnumMap` | not assessed | — | Out of scope until a task needs it |
| Enum as map key / persisted status | not assessed | — | Out of scope (round level) |

## Not yet assessed

Generics and wildcards · concurrency and `ThreadLocal` · JVM memory and GC · modern Java (records,
sealed, pattern matching, virtual threads) · design patterns · coding-round drills (arrays, strings,
two pointers, maps).

## Current focus order

0. **Enums** — user-requested start of core Java; opened in S3, test pending.
1. **Map + Set semantics in code** — one working file each, not just explanations.
2. **Equality and hashing** — blocks maps, sets, assertions, and half the coding round.
3. **List choice and complexity** — blocks the "why not `LinkedList`" question.
4. Streams → generics → concurrency → JVM → modern Java.
5. Coding drills once 1–3 are `solid`.

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
