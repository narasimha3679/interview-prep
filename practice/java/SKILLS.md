# Java Skills Matrix

Live view of where I actually am. Updated after every session.

**Last updated:** 2026-11-16 · **Sessions:** 1 · **Next up:** equality + `HashMap` contract

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
| `==` vs `.equals()`, string pool | shaky | Q1: said `a == b` is false; literals are pooled | Re-ask with runtime-built strings |
| String immutability, `StringBuilder` | solid | Q2 correct: new object per concat; knew to append | Code drill: 10k concat vs `StringBuilder` timing |
| `final`, `static`, inner classes | not assessed | — | — |
| Overloading vs overriding | not assessed | — | — |

## Collections

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| List choice: `ArrayList` vs `LinkedList` | unknown | Q3: blank | Explain + benchmark drill D2 |
| `HashMap` internals, `equals`/`hashCode` | unknown | Q4: blank | Explain + drill D1 (break it, then fix it) |
| Set semantics, `HashSet` vs `TreeSet` | not assessed | — | After D1 |
| Iteration + `ConcurrentModificationException` | not assessed | — | — |
| `Comparable` vs `Comparator` | not assessed | — | — |

## Exceptions and resources

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `throw` vs `throws` | solid | Q6: correct on both | — |
| `finally` edge cases | shaky | Q6: did not know when it is skipped | Re-ask: `System.exit`, JVM kill, infinite loop |
| `try-with-resources`, suppressed exceptions | not assessed | — | Code drill with a custom `AutoCloseable` |
| Checked vs unchecked, custom exceptions | not assessed | — | — |

## Streams and lambdas

| Skill | Level | Evidence | Next action |
|---|---|---|---|
| `map` vs `flatMap` | unknown | Q5: blank | Explain + drill |
| Stream is lazy, single-use | unknown | Q5: blank | Reproduce `IllegalStateException` |
| Collectors, `groupingBy`, `partitioningBy` | not assessed | — | — |
| Functional interfaces, method references | not assessed | — | — |

## Not yet assessed

Generics and wildcards · concurrency and `ThreadLocal` · JVM memory and GC · modern Java (records,
sealed, pattern matching, virtual threads) · design patterns · coding-round drills (arrays, strings,
two pointers, maps).

## Current focus order

1. **Equality and hashing** — blocks maps, sets, test assertions, and half the coding round.
2. **List and Map internals** — blocks complexity questions and code review.
3. **Streams** — blocks the "write it in one line" expectation.
4. Generics → concurrency → JVM → modern Java.
5. Coding drills once 1–3 are `solid`.

## Recurring traps to re-test every session

- Literal vs runtime `String` identity (`==`).
- Overriding `equals` without `hashCode`.
- Mutable field used in `hashCode`, then mutated after insertion.
- `LinkedList.get(i)` is O(n), not O(1).
- Reusing a consumed `Stream`.
