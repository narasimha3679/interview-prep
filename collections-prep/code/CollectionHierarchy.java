import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Collections hierarchy in one run.
 *
 *   Iterable -> Collection -> List / Set / Queue
 *   Map  <-- separate, NOT a Collection
 *
 * Run:  javac CollectionHierarchy.java && java CollectionHierarchy
 */
public class CollectionHierarchy {

    public static void main(String[] args) {
        // ---- List: ordered, allows duplicates, index access ----
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("a");                       // duplicate is fine
        System.out.println("List        : " + list + "  (get(1) = " + list.get(1) + ")");

        // ---- Set: no duplicates (add returns false if already present) ----
        Set<String> set = new HashSet<>();
        System.out.println("add b       : " + set.add("b"));
        System.out.println("add b again : " + set.add("b"));   // false -> rejected
        System.out.println("Set         : " + set);

        // ---- Queue / Deque: order of processing, not index access ----
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(0);
        System.out.println("Deque       : " + deque + "  (poll = " + deque.poll() + ")");

        // ---- Map: key -> value, NOT a Collection ----
        Map<String, Integer> ages = new HashMap<>();
        ages.put("ana", 30);
        ages.put("bob", 25);
        ages.put("ana", 31);                 // same key overwrites
        System.out.println("Map         : " + ages + "  (size = " + ages.size() + ")");

        // The three views of a Map
        System.out.println("keySet      : " + ages.keySet());
        System.out.println("values      : " + ages.values());
        System.out.println("entrySet    : " + ages.entrySet());   // best for iterating both

        // Map is not Iterable -> this would NOT compile:
        // for (String s : ages) { }
    }
}
