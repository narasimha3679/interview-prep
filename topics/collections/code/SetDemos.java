import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * HashSet (no order) vs LinkedHashSet (insertion order) vs TreeSet (sorted).
 *
 * Run:  javac SetDemos.java && java SetDemos
 */
public class SetDemos {

    public static void main(String[] args) {
        List<String> input = Arrays.asList("banana", "apple", "cherry", "apple", "date");

        Set<String> hash = new HashSet<>(input);
        Set<String> linked = new LinkedHashSet<>(input);
        Set<String> tree = new TreeSet<>(input);

        System.out.println("input (with dup)   : " + input);
        System.out.println("HashSet  (no order): " + hash);
        System.out.println("LinkedHashSet      : " + linked + "   <- insertion order");
        System.out.println("TreeSet  (sorted)  : " + tree);

        // ---- uniqueness uses equals(); add() tells you if it was new ----
        Set<String> seen = new HashSet<>();
        System.out.println("\nseen.add(apple) : " + seen.add("apple"));
        System.out.println("seen.add(apple) : " + seen.add("apple") + "  <- already there");

        // ---- de-dup a list while keeping order ----
        List<String> deduped = List.copyOf(new LinkedHashSet<>(input));
        System.out.println("deduped list    : " + deduped);

        // ---- TreeSet range / nearest queries ----
        TreeSet<Integer> scores = new TreeSet<>(List.of(10, 20, 30, 40, 50));
        System.out.println("\nscores          : " + scores);
        System.out.println("floor(35)       : " + scores.floor(35));    // greatest <= 35
        System.out.println("ceiling(35)     : " + scores.ceiling(35));  // least >= 35
        System.out.println("headSet(<30)    : " + scores.headSet(30));

        // ---- null handling differs ----
        Set<String> withNull = new HashSet<>();
        withNull.add(null);
        System.out.println("\nHashSet allows null : " + withNull);
        try {
            new TreeSet<String>().add(null);
        } catch (NullPointerException e) {
            System.out.println("TreeSet rejects null: NullPointerException");
        }
    }
}
