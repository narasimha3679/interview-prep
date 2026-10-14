/**
 * Reverse an integer: 12345 -> 54321.
 * BUILT-IN : turn into String, StringBuilder.reverse(), parse back to int
 * MANUAL   : peel off digits with % 10, build with * 10
 */
public class ReverseNumber {

    public static void main(String[] args) {
        System.out.println(reverseBuiltIn(12345)); // 54321
        System.out.println(reverseManual(12345));  // 54321
    }

    public static int reverseBuiltIn(int num) {
        String reversed = new StringBuilder(String.valueOf(num)).reverse().toString();
        return Integer.parseInt(reversed);
    }

    public static int reverseManual(int num) {
        int reversed = 0;
        while (num != 0) {
            int digit = num % 10;          // grab the last digit
            reversed = reversed * 10 + digit;
            num = num / 10;                // drop the last digit
        }
        return reversed;
    }
}
