/**
 * Sum of the digits of a number: 1234 -> 1+2+3+4 = 10.
 * MANUAL   : peel digits with % 10
 * STRING   : loop over the characters (Character.getNumericValue)
 */
public class SumOfDigits {

    public static void main(String[] args) {
        System.out.println(sumDigits(1234));       // 10
        System.out.println(sumDigitsString(1234)); // 10
    }

    public static int sumDigits(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    public static int sumDigitsString(int num) {
        int sum = 0;
        for (char c : String.valueOf(num).toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return sum;
    }
}
