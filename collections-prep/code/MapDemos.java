import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * HashMap (no order) vs LinkedHashMap (insertion order) vs TreeMap (sorted keys),
 * plus the idioms: getOrDefault, computeIfAbsent, merge.
 *
 * Run:  javac MapDemos.java && java MapDemos
 */
public class MapDemos {

    public static void main(String[] args) {
        // ---- getOrDefault ----
        Map<String, Integer> stock = new HashMap<>();
        stock.put("apple", 5);
        System.out.println("apples      : " + stock.getOrDefault("apple", 0));
        System.out.println("missing     : " + stock.getOrDefault("kiwi", 0));

        // ---- merge: the count-occurrences idiom ----
        String sentence = "the cat sat on the mat the cat";
        Map<String, Integer> freq = new HashMap<>();
        for (String word : sentence.split(" ")) {
            freq.merge(word, 1, Integer::sum);
        }
        System.out.println("freq        : " + freq);

        // ---- computeIfAbsent: build a Map<String, List<...>> cleanly ----
        Map<String, StringBuilder> byLength = new HashMap<>();
        for (String word : freq.keySet()) {
            byLength.computeIfAbsent("len" + word.length(), k -> new StringBuilder())
                    .append(word).append(" ");
        }
        System.out.println("byLength    : " + byLength);

        // ---- ordering differences ----
        Map<String, Integer> hash = new HashMap<>(freq);
        Map<String, Integer> linked = new LinkedHashMap<>(freq);
        Map<String, Integer> tree = new TreeMap<>(freq);
        System.out.println("\nHashMap     : " + hash + "   <- unordered");
        System.out.println("LinkedHash  : " + linked + "   <- insertion order");
        System.out.println("TreeMap     : " + tree + "   <- sorted by key");

        // ---- TreeMap nearest-key queries ----
        TreeMap<Integer, String> page = new TreeMap<>();
        page.put(1, "cover");
        page.put(5, "intro");
        page.put(12, "body");
        page.put(20, "index");
        System.out.println("\nfirstKey    : " + page.firstKey());
        System.out.println("floorKey(10): " + page.floorKey(10));
        System.out.println("ceilingKey(10): " + page.ceilingKey(10));

        // ---- iterate: entrySet is the efficient pass for key+value ----
        System.out.println("\nentrySet iteration:");
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }

        // ---- HashMap allows one null key; TreeMap does not ----
        Map<String, Integer> withNull = new HashMap<>();
        withNull.put(null, 1);
        System.out.println("\nHashMap null key : " + withNull);
        try {
            new TreeMap<String, Integer>().put(null, 1);
        } catch (NullPointerException e) {
            System.out.println("TreeMap null key : NullPointerException");
        }
    }
}
