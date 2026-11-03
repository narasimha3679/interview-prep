import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Remove duplicates.
 * Built-in helper: Set (LinkedHashSet keeps insertion order; HashSet does not).
 */
public class RemoveDuplicates {

    public static void main(String[] args) {
        System.out.println(removeDuplicateChars("programming")); // progamin

        int[] nums = {1, 2, 2, 3, 3, 3, 4};
        System.out.println(Arrays.toString(removeDuplicateNumbers(nums))); // [1, 2, 3, 4]
    }

    // String version: keep a char only if we have not already added it.
    public static String removeDuplicateChars(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (sb.indexOf(String.valueOf(c)) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // Array version: a Set removes duplicates automatically.
    public static int[] removeDuplicateNumbers(int[] nums) {
        Set<Integer> set = new LinkedHashSet<>();
        for (int n : nums) {
            set.add(n);
        }
        return set.stream().mapToInt(Integer::intValue).toArray();
    }
}
