import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fail-fast iteration: ConcurrentModificationException and the three correct fixes.
 *
 * Run:  javac ConcurrentModificationDemo.java && java ConcurrentModificationDemo
 */
public class ConcurrentModificationDemo {

    public static void main(String[] args) {
        // ---- 1. THE BUG: remove during for-each throws ----
        try {
            List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            for (Integer n : list) {
                if (n % 2 == 0) list.remove(n);   // structural change while iterating
            }
        } catch (java.util.ConcurrentModificationException e) {
            System.out.println("for-each remove      : ConcurrentModificationException");
        }

        // ---- 2. FIX A: Iterator.remove() ----
        List<Integer> a = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Iterator<Integer> it = a.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) it.remove();
        }
        System.out.println("Iterator.remove      : " + a);

        // ---- 3. FIX B: removeIf (preferred) ----
        List<Integer> b = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        b.removeIf(n -> n % 2 == 0);
        System.out.println("removeIf             : " + b);

        // ---- 4. FIX C: remove from a map via entrySet().removeIf ----
        Map<String, Integer> ages = new HashMap<>(Map.of("ana", 30, "bob", 17, "cy", 15));
        ages.entrySet().removeIf(e -> e.getValue() < 18);
        System.out.println("map removeIf         : " + ages);

        // ---- 5. The index-shift trap: forward removal skips elements ----
        List<Integer> skip = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        for (int i = 0; i < skip.size(); i++) {
            if (skip.get(i) % 2 == 0) skip.remove(i);   // removing shifts the next element left
        }
        System.out.println("forward-index remove : " + skip + "   <- 4 was skipped!");

        // ---- 6. Fail-safe: ConcurrentHashMap iterates a snapshot, no exception ----
        Map<String, Integer> chm = new ConcurrentHashMap<>(Map.of("a", 1, "b", 2));
        for (String k : chm.keySet()) {
            chm.put("new-" + k, 9);         // allowed, no CME
        }
        System.out.println("ConcurrentHashMap    : " + chm.size() + " entries, no exception");
    }
}
