import java.util.Arrays;

/**
 * Anagram: same letters, different order ("listen" / "silent").
 */
public class Anagram {

    public static void main(String[] args) {
        System.out.println(isAnagram("listen", "silent")); // true
    }

    public static boolean isAnagram(String a, String b) {
        char[] x = a.toLowerCase().toCharArray();
        char[] y = b.toLowerCase().toCharArray();
        Arrays.sort(x);
        Arrays.sort(y);
        return Arrays.equals(x, y);
    }



}
