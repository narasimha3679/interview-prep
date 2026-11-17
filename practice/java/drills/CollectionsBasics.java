import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collections basics, runnable.
 * Run:  java CollectionsBasics.java
 *
 * Three shapes, one mental model:
 *   List -> a shelf with numbered slots
 *   Map  -> a dictionary: key -> value
 *   Set  -> a guest list: no duplicates, no order guarantee
 */
public class CollectionsBasics {

    public static void main(String[] args) {

        // ---------- 1. List: numbered shelf, grows by itself ----------
        List<String> browsers = new ArrayList<>();
        browsers.add("chrome");
        browsers.add("firefox");
        browsers.add("chrome");
        browsers.add(0, "safari");            // insert at index 0; the rest shift right
        System.out.println(browsers);          // [safari, chrome, firefox, chrome]

        System.out.println(browsers.get(1));   // chrome  -> argument is the INDEX
        System.out.println(browsers.contains("edge")); // false -> scans the whole list, O(n)
        System.out.println(browsers.size());   // 4
        System.out.println(browsers.indexOf("chrome")); // 1 -> first match only

        // ---------- 2. Map: dictionary, key -> value ----------
        Map<String, Integer> runs = new HashMap<>();
        runs.put("chrome", 3);
        runs.put("firefox", 1);

        System.out.println(runs.get("chrome"));            // 3
        System.out.println(runs.get("edge"));              // null  -> no such key
        System.out.println(runs.getOrDefault("edge", 0));  // 0     -> safe default
        System.out.println(runs.containsKey("firefox"));   // true

        // ---------- 3. The idiom you will use constantly: counting ----------
        List<String> logs = List.of("chrome", "firefox", "chrome", "edge", "chrome");

        // Step 1: the version that shows every moving part
        Map<String, Integer> counts = new HashMap<>();
        for (String browser : logs) {
            Integer current = counts.get(browser);
            if (current == null) {
                counts.put(browser, 1);
            } else {
                counts.put(browser, current + 1);
            }
        }
        System.out.println(counts);   // {chrome=3, firefox=1, edge=1}

        // Step 2: same thing, one line
        Map<String, Integer> counts2 = new HashMap<>();
        for (String browser : logs) {
            counts2.put(browser, counts2.getOrDefault(browser, 0) + 1);
        }
        System.out.println(counts2);  // {chrome=3, firefox=1, edge=1}

        // Step 3: same thing, shortest
        Map<String, Integer> counts3 = new HashMap<>();
        for (String browser : logs) {
            counts3.merge(browser, 1, Integer::sum);
        }
        System.out.println(counts3);  // {chrome=3, firefox=1, edge=1}

        // ---------- Trap: null unboxing ----------
        // int missing = runs.get("edge");   // NPE at runtime: null cannot become int
        // Fix: use getOrDefault, or keep the type Integer so null is legal.
    }
}
