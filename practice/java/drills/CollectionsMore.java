import java.util.*;

/**
 * Collections, the next layer — runnable.
 * Run:  java CollectionsMore.java
 *
 * Six behaviours interviewers actually ask about:
 *   1. Set order: HashSet vs LinkedHashSet vs TreeSet
 *   2. Sorted output: TreeMap, and sorting entries by value
 *   3. Removing while looping: CME vs removeIf
 *   4. equals/hashCode: a Set of "equal" objects
 *   5. Immutability: List.of vs Arrays.asList
 *   6. Comparator: two fields, then a tie-break
 * 
1. HashSet       [a, b, c]
1. LinkedHashSet [b, a, c]
1. TreeSet       [a, b, c]
2. HashMap {a=1, b=2, c=3}
2. TreeMap {a=1, b=2, c=3}
2. by count [c=3, b=2, a=1]
3. remove first  -> ConcurrentModificationException
3. remove middle -> [a, c] (c was never visited, no exception)
3. removeIf      -> [a, c]
4. equals only      size=2
4. record (both)    size=1
5. List.of add      -> UnsupportedOperationException
5. asList set ok [z, b], add -> UnsupportedOperationException
6. age, then name: [Person[name=cy, age=22], Person[name=ana, age=30], Person[name=bo, age=30]]
 */
public class CollectionsMore {

    record Person(String name, int age) { }

    /** equals without hashCode — the classic half-fix. */
    static class OnlyEquals {
        final String name;
        OnlyEquals(String name) { this.name = name; }
        @Override public boolean equals(Object o) {
            return o instanceof OnlyEquals x && name.equals(x.name);
        }
    }

    public static void main(String[] args) {
        setOrder();
        sortedOutput();
        removeWhileLooping();
        equality();
        immutableLists();
        comparator();
    }

    // 1. Same elements, three different orders
    static void setOrder() {
        List<String> input = List.of("b", "a", "b", "c", "a");
        System.out.println("1. HashSet       " + new HashSet<>(input));
        System.out.println("1. LinkedHashSet " + new LinkedHashSet<>(input));
        System.out.println("1. TreeSet       " + new TreeSet<>(input));
    }

    // 2. A HashMap has no order; TreeMap sorts by key; sorting by value needs a List
    static void sortedOutput() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("b", 2);
        counts.put("a", 1);
        counts.put("c", 3);

        System.out.println("2. HashMap " + counts);
        System.out.println("2. TreeMap " + new TreeMap<>(counts));

        List<Map.Entry<String, Integer>> byCount = new ArrayList<>(counts.entrySet());
        byCount.sort((x, y) -> x.getValue().equals(y.getValue())
                ? x.getKey().compareTo(y.getKey())
                : y.getValue() - x.getValue());
        System.out.println("2. by count " + byCount);
    }

    // 3. Mutating during a for-each: throws for the first element, silently skips for a middle one
    static void removeWhileLooping() {
        List<String> throwsCase = new ArrayList<>(List.of("a", "b", "c"));
        try {
            for (String s : throwsCase) {
                if (s.equals("a")) {
                    throwsCase.remove(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("3. remove first  -> ConcurrentModificationException");
        }

        List<String> silent = new ArrayList<>(List.of("a", "b", "c"));
        for (String s : silent) {
            if (s.equals("b")) {
                silent.remove(s);
            }
        }
        System.out.println("3. remove middle -> " + silent + " (c was never visited, no exception)");

        silent.removeIf(s -> s.equals("b"));
        System.out.println("3. removeIf      -> " + silent);
    }

    // 4. equals without hashCode -> the Set never finds the duplicate
    static void equality() {
        Set<OnlyEquals> bad = new HashSet<>();
        bad.add(new OnlyEquals("ana"));
        bad.add(new OnlyEquals("ana"));
        System.out.println("4. equals only      size=" + bad.size());

        Set<Person> good = new HashSet<>();
        good.add(new Person("ana", 30));
        good.add(new Person("ana", 30));
        System.out.println("4. record (both)    size=" + good.size());
    }

    // 5. List.of is immutable; Arrays.asList is fixed-size but writable
    static void immutableLists() {
        List<String> of = List.of("a", "b");
        try {
            of.add("c");
        } catch (UnsupportedOperationException e) {
            System.out.println("5. List.of add      -> UnsupportedOperationException");
        }

        List<String> asList = Arrays.asList("a", "b");
        asList.set(0, "z");
        try {
            asList.add("c");
        } catch (UnsupportedOperationException e) {
            System.out.println("5. asList set ok " + asList + ", add -> UnsupportedOperationException");
        }
    }

    // 6. Sort by age, then by name when ages tie
    static void comparator() {
        List<Person> people = new ArrayList<>(List.of(
                new Person("bo", 30),
                new Person("ana", 30),
                new Person("cy", 22)));

        people.sort(Comparator.comparingInt(Person::age).thenComparing(Person::name));
        System.out.println("6. age, then name: " + people);
    }
}
