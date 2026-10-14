import java.util.HashMap;
import java.util.Map;

/**
 * Count how many times each character appears in a string.
 * The workhorse here is HashMap with getOrDefault().
 */
public class CharacterFrequency {

    public static void main(String[] args) {
        System.out.println(count("banana"));  // {a=3, b=1, n=2}
    }

    public static Map<Character, Integer> count(String str) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : str.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        return counts;
    }
}
