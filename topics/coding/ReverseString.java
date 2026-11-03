/**
 * Reverse a String.
 * BUILT-IN : StringBuilder.reverse()
 * MANUAL   : loop backwards and append each character
 */
public class ReverseString {

    public static void main(String[] args) {
        String s = "Hello, World!";
        System.out.println("built-in: " + reverseBuiltIn(s)); // !dlroW ,olleH
        System.out.println("manual:   " + reverseManual(s));  // !dlroW ,olleH
    }

    // BUILT-IN: StringBuilder already knows how to reverse itself.
    public static String reverseBuiltIn(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    // MANUAL: start at the last index, walk down to 0, append each character.
    public static String reverseManual(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }
}
