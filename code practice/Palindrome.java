/**
 * Palindrome: reads the same forwards and backwards ("madam", "racecar").
 * BUILT-IN : reverse it and compare with equals()
 * MANUAL   : two pointers, one from each end
 */
public class Palindrome {

    public static void main(String[] args) {
        System.out.println(isPalindromeBuiltIn("racecar")); // true
        System.out.println(isPalindromeManual("racecar"));  // true
        System.out.println(isPalindromeManual("hello"));    // false
    }

    public static boolean isPalindromeBuiltIn(String str) {
        String cleaned = str.toLowerCase();
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    public static boolean isPalindromeManual(String str) {
        str = str.toLowerCase();
        int left = 0;
        int right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
