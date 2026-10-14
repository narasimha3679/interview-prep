import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * List behavior + the ArrayList-vs-LinkedList performance reality check.
 *
 * Run:  javac ListDemos.java && java ListDemos
 */
public class ListDemos {

    public static void main(String[] args) {
        // ---- basic ops ----
        List<String> list = new ArrayList<>();
        list.add("c");
        list.add("a");
        list.add("b");
        System.out.println("as added      : " + list);
        list.sort(null);                              // natural order
        System.out.println("after sort    : " + list);
        System.out.println("contains a    : " + list.contains("a"));   // O(n) on a List
        System.out.println("indexOf b     : " + list.indexOf("b"));

        // ---- removeIf: the safe way to remove while filtering ----
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        nums.removeIf(n -> n % 2 == 0);
        System.out.println("removeIf even : " + nums);                 // [1, 3, 5]

        // ---- perf: indexed get ----
        final int N = 100_000;
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        System.out.println("\nSum via get(i) over " + N + " elements:");
        System.out.printf("  ArrayList  : %d ms%n", timeGet(arrayList));
        System.out.printf("  LinkedList : %d ms  <- O(n) walk per get%n", timeGet(linkedList));

        System.out.println("\nPrepend " + N + " elements:");
        System.out.printf("  ArrayList  : %d ms  <- shifts everything, O(n)%n", timePrepend(new ArrayList<>()));
        System.out.printf("  LinkedList : %d ms  <- O(1) at the head%n", timePrepend(new LinkedList<>()));
    }

    // Long loop, so we must actually consume the value.
    static long timeGet(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        if (sum == -1) System.out.print("");   // keep `sum` used
        return ms;
    }

    static long timePrepend(List<Integer> list) {
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            list.add(0, i);
        }
        return (System.nanoTime() - start) / 1_000_000;
    }
}
