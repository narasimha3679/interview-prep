import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Comparable (natural order, in the class) vs Comparator (external, many orderings),
 * and modern Comparator.comparing(...).thenComparing(...).
 *
 * Run:  javac ComparatorDemo.java && java ComparatorDemo
 */
public class ComparatorDemo {

    // Natural ordering lives in the class.
    record Person(String name, int age) implements Comparable<Person> {
        @Override
        public int compareTo(Person other) {
            return Integer.compare(this.age, other.age);   // not this.age - other.age
        }
    }

    public static void main(String[] args) {
        List<Person> people = new ArrayList<>(List.of(
                new Person("ana", 30),
                new Person("bob", 25),
                new Person("cy", 30),
                new Person("dan", 20)));

        // natural order = by age (Comparable)
        people.sort(null);
        System.out.println("natural (age)        : " + people);

        // external Comparator = by name
        people.sort(Comparator.comparing(Person::name));
        System.out.println("by name              : " + people);

        // multi-key: age, then name as tie-breaker
        people.sort(Comparator.comparingInt(Person::age).thenComparing(Person::name));
        System.out.println("by age then name     : " + people);

        // reversed + nulls
        people.sort(Comparator.comparing(Person::name).reversed());
        System.out.println("name reversed        : " + people);

        // ---- sort a Map by value using a LinkedHashMap to preserve order ----
        Map<String, Integer> ages = Map.of("ana", 30, "bob", 25, "cy", 30, "dan", 20);
        Map<String, Integer> sortedByValue = ages.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        java.util.LinkedHashMap::new));
        System.out.println("\nsorted by value      : " + sortedByValue);

        // ---- TreeMap with a custom Comparator ----
        TreeMap<String, Integer> byKeyReverse =
                new TreeMap<>(Comparator.reverseOrder());
        byKeyReverse.putAll(ages);
        System.out.println("TreeMap reversed keys: " + byKeyReverse.descendingMap());

        // ---- natural order in a TreeSet ----
        System.out.println("\nTreeSet natural      : "
                + new java.util.TreeSet<>(List.of("banana", "apple", "cherry")));
    }
}
