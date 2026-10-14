import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The classic traps: autoboxing identity, Arrays.asList vs List.of,
 * remove(int) vs remove(Object), null handling.
 *
 * Run:  javac GotchasDemo.java && java GotchasDemo
 */
public class GotchasDemo {

    public static void main(String[] args) {
        // ---- 1. Integer cache: == "works" only for -128..127 ----
        Integer a = 127, b = 127;
        Integer c = 128, d = 128;
        System.out.println("127 == 127 (boxed) : " + (a == b) + "   <- cached");
        System.out.println("128 == 128 (boxed) : " + (c == d) + "   <- different objects");
        System.out.println("use .equals        : " + c.equals(d));

        // ---- 2. remove(int index) vs remove(Object value) ----
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30));
        nums.remove(1);                          // removes INDEX 1 -> value 20
        System.out.println("\nremove(1)          : " + nums + "   <- removed index 1");

        List<Integer> nums2 = new ArrayList<>(List.of(10, 20, 30));
        nums2.remove(Integer.valueOf(20));       // removes the VALUE 20
        System.out.println("remove(Integer 20) : " + nums2 + "   <- removed value 20");

        // ---- 3. Arrays.asList: fixed-size view (set works, add/remove throw) ----
        List<String> fixed = Arrays.asList("x", "y", "z");
        fixed.set(0, "X");
        System.out.println("\nArrays.asList set  : " + fixed);
        try {
            fixed.add("w");
        } catch (UnsupportedOperationException e) {
            System.out.println("Arrays.asList add  : UnsupportedOperationException");
        }

        // ---- 4. List.of: immutable AND rejects null ----
        List<String> immutable = List.of("a", "b");
        try {
            immutable.add("c");
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of add        : UnsupportedOperationException");
        }
        try {
            List.of("a", null);
        } catch (NullPointerException e) {
            System.out.println("List.of null       : NullPointerException");
        }

        // ---- 5. Wrap a fixed list to get a real ArrayList ----
        List<String> mutable = new ArrayList<>(Arrays.asList("x", "y", "z"));
        mutable.add("w");
        System.out.println("\nnew ArrayList<>(..) : " + mutable + "   <- now mutable");

        // ---- 6. HashMap: one null key, many null values ----
        Map<String, String> m = new HashMap<>();
        m.put(null, "null key");
        m.put("k", null);
        System.out.println("HashMap null key   : " + m.get(null));
        System.out.println("HashMap null val   : " + m.get("k") + "  (containsKey=" + m.containsKey("k") + ")");

        // ---- 7. contains on List O(n) vs Set O(1) ----
        Set<String> set = Set.copyOf(new ArrayList<>(List.of("a", "b", "c")));
        System.out.println("\nSet.contains       : " + set.contains("b"));
    }
}
