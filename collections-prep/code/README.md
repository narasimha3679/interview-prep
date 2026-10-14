# Collections demos

Each file is a standalone class with a `main()` that **prints** the behaviour it demonstrates.
Compile and run any of them, then change them and watch the output change — that's what makes
this stick.

```powershell
# If java is not on PATH:
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

cd "C:\Users\nara\Projects\interview prep\collections-prep\code"
javac *.java
java MapDemos
```

Or run them all at once:

```powershell
.\run-all.ps1
```

## The demos

| File | What it proves |
|---|---|
| `CollectionHierarchy.java` | `List` / `Set` / `Queue` / `Map` at a glance; `Map` is not a `Collection` |
| `ListDemos.java` | `ArrayList` vs `LinkedList` — indexed `get` vs head insert (real timings) |
| `SetDemos.java` | `HashSet` vs `LinkedHashSet` vs `TreeSet`, de-dup, `floor`/`ceiling`, null |
| `MapDemos.java` | `HashMap` vs `LinkedHashMap` vs `TreeMap`, `getOrDefault`, `computeIfAbsent`, `merge` |
| `EqualsHashCodeDemo.java` | The #1 gotcha: broken `equals`, the mutable-key trap, `record` as a key |
| `ComparatorDemo.java` | `Comparable` vs `Comparator`, `comparing().thenComparing()`, sort map by value |
| `ConcurrentModificationDemo.java` | `ConcurrentModificationException` and the 3 correct fixes |
| `GotchasDemo.java` | Integer cache, `remove(int)` vs `remove(Object)`, `Arrays.asList` vs `List.of` |

## Experiments to try (5 minutes each)

- In `EqualsHashCodeDemo`, delete `Key.hashCode()` and re-run — watch `get` return `null`.
- In `ListDemos`, shrink `N` to `10_000` and compare the timings again.
- In `GotchasDemo`, swap `nums.remove(1)` for `nums.remove(20)` and see it throw.
- In `ConcurrentModificationDemo`, run the forward-index removal alone and find the skipped value.
