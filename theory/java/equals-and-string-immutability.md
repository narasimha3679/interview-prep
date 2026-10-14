# Java — `==` vs `equals()` , String Immutability

## Say it right
> "`==` compares **references** (memory address) for objects and **values** for primitives. `.equals()` compares **content** — but only if the class **overrides** it. The default `Object.equals()` is just `==`."

```java
String a = new String("td");
String b = new String("td");
a == b          // false  (two different objects)
a.equals(b)     // true   (same content)
```
**Testing example:** always compare with `.equals` / `assertEquals`, never `==`.

---

## Why is String immutable? (4 reasons)

1. **String Pool / interning** — literals are shared, so mutation would corrupt other references.
```java
String s1 = "abc", s2 = "abc";
s1 == s2          // true — same pooled object
```
2. **Security** — Strings carry class names, file paths, DB URLs, hosts; if mutable, a value could change after a validation check.
3. **Thread safety** — immutable objects are safely shareable across threads with no synchronization.
4. **Cached hashCode + reliable HashMap key** — a mutable key would break the map.

---

## String vs StringBuilder vs StringBuffer
| | Mutable | Thread-safe | Speed |
|---|---|---|---|
| `String` | ❌ | ✅ (immutable) | slow for repeated concat |
| `StringBuilder` | ✅ | ❌ | **fast** ← default |
| `StringBuffer` | ✅ | ✅ (synchronized) | slower |

> "In a loop I use **`StringBuilder`** — `+` creates a new object every time."

---

## Depth ladder
1. **Does `Object.equals()` compare content?** → No — default is identity (`==`). You must override it (which is why POJOs override `equals`).
2. **`equals`/`hashCode` contract** → **equal objects MUST have equal hashCodes.** Override `equals` ⇒ override `hashCode`, or `HashMap`/`HashSet` misbehave. (Very common follow-up.)
3. **`String` vs `new String()`** → literal → pool; `new String(...)` → new heap object.
4. **Trivia:** `Integer a = 100, b = 100; a == b` → **true** (cache −128..127); `200 == 200` → **false**.
5. **Is Java pass-by-value?** → Always. Object references are passed **by value** (a copy of the reference).

---

## Memorize
1. **"`==` = reference, `.equals()` = content, and default `equals` is just `==` unless overridden."**
2. **"String is immutable for: pool sharing, security, thread safety, cached hashCode as a map key."**
3. **"Use `StringBuilder` in loops. Override `equals` ⇒ must override `hashCode`."**

## Likely follow-ups
- Where do you override `equals`? → POJOs/models used as map keys or in collections/assertions.
- Is String thread-safe? → yes, because immutable.
- `String s = null; s.equals("x")` → NPE; use `"x".equals(s)` or `Objects.equals`.
