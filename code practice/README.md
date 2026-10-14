# Code Practice — base Java

Simple, classic Java coding questions. Each file is a **standalone class with a `main()`** — you can
run any of them directly. Each shows a **built-in way** and an **easy manual way**.

## How to run

```powershell
# If java is not on PATH:
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

cd "C:\Users\nara\Projects\interview prep\code practice"
javac ReverseString.java     # compile one
java ReverseString           # run it

javac *.java                 # or compile them all
```

## The files

| File | Problem | Built-in trick to remember |
|------|---------|----------------------------|
| `ReverseString.java` | Reverse a string | `new StringBuilder(s).reverse().toString()` |
| `Palindrome.java` | Is it a palindrome? | reverse + `.equals()` |
| `ReverseNumber.java` | Reverse an integer | `String.valueOf` → `reverse()` → `Integer.parseInt` |
| `SwapNumbers.java` | Swap two numbers | temp variable, or `+`/`-` trick |
| `Factorial.java` | 5! = 120 | loop or recursion |
| `Fibonacci.java` | 0,1,1,2,3,5,8... | loop or recursion |
| `PrimeNumber.java` | Is it prime? | loop to `Math.sqrt(n)` |
| `SumOfDigits.java` | 1234 → 10 | `% 10` and `/ 10` |
| `CountVowels.java` | Count vowels/consonants | `"aeiou".indexOf(c) >= 0` |
| `CharacterFrequency.java` | Count each character | `map.getOrDefault(c, 0) + 1` |
| `RemoveDuplicates.java` | Remove dups | `Set` / `LinkedHashSet` |
| `Anagram.java` | listen / silent | `Arrays.sort` both + `Arrays.equals` |
| `MaxAndMin.java` | Biggest / smallest | `Arrays.stream(nums).max()` |
| `BubbleSort.java` | Sort an array | `Arrays.sort(nums)` |
| `SecondLargest.java` | 2nd biggest | sort, take `length - 2` |
| `ReverseWords.java` | "Java is fun" → "fun is Java" | `split(" ")` + loop backwards |
| `FizzBuzz.java` | 3→Fizz, 5→Buzz | check `% 15` first |

## Base-Java things they may ask (quick answers)

- **`String` vs `StringBuilder`** — `String` is **immutable** (every `+=` makes a new object);
  `StringBuilder` is mutable and fast. Use `StringBuilder` inside loops.
- **`==` vs `.equals()`** — `==` compares references (same object); `.equals()` compares values.
  **Always use `.equals()` for `String`.** Great gotcha to mention.
- **Array vs `ArrayList`** — array is fixed size; `ArrayList` grows. `list.add()`, `list.get(i)`, `list.size()`.
- **`HashMap`** — key/value pairs, `map.put(k,v)`, `map.getOrDefault(k, 0)`, `map.containsKey(k)`. No order.
- **`HashSet`** — stores unique values; `set.add()` returns false if it already existed.
- **Autoboxing / `Integer`** — `int` primitive vs `Integer` wrapper; `Integer.parseInt("123")`,
  `String.valueOf(123)`.
- **Conversions** — `Integer.parseInt(str)`, `String.valueOf(int)`, `str.toCharArray()`,
  `Arrays.toString(array)`, `new ArrayList<>(Arrays.asList(...))`.
- **Loops** — `for (int i = 0; i < n; i++)` and the enhanced `for (int n : nums)`.
- **Exceptions** — `try { } catch (Exception e) { }`; checked vs unchecked; `throws` in a signature.
- **`null`** — guard it: `if (str == null) return null;`. Empty check: `str.isEmpty()`.
- **Recursion** — needs a **base case** or it stack-overflows.
- **`final`, `static`, access modifiers** — `static` = belongs to the class, no object needed (that's
  why every method here is `static` and `main` can call it).

## The three habits that make you look good

1. **Return the value, don't print it.** `public static String reverse(String s)`, not `void ... println`.
   (Print inside `main`.) It's testable and composable.
2. **Guard the edge cases out loud**: `null`, empty string, single character, negative numbers.
3. **Say the built-in first, then the manual loop.** "I'd use `StringBuilder.reverse()`; if you want it
   by hand, I'd loop backwards" — that shows you know the language *and* can think.
