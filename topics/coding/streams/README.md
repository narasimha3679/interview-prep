# Stream Drills

Practice the Stream API patterns interviewers ask about.

## Drills

1. Filter a list of strings to those longer than 3 characters, uppercase them, and sort.
2. Group strings by length.
3. Count occurrences of each word in a sentence.
4. Flatten a `List<List<Integer>>` and sum the distinct values.
5. Partition numbers into even and odd.
6. Find the second-highest number.
7. Join names with a comma.
8. Map a list of objects to a `Map<id, name>`.
9. Find the most frequent word.
10. Compute total price of a list of orders with a discount.

## Rules

- Prefer `map`/`filter`/`collect` over loops when it improves readability.
- Know when a loop is clearer; streams are not a goal.
- Watch for `NullPointerException` in lambdas.
- Remember: streams are lazy until a terminal operation.
- `Collectors.toMap` throws on duplicate keys unless you provide a merge function.

## Example

```java
List<String> words = List.of("java", "api", "test", "automation");
List<String> result = words.stream()
        .filter(w -> w.length() > 3)
        .map(String::toUpperCase)
        .sorted()
        .toList();
```
