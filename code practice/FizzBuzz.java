/**
 * FizzBuzz — the most common warm-up question.
 * multiples of 3 -> "Fizz", of 5 -> "Buzz", of both -> "FizzBuzz".
 */
public class FizzBuzz {

    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            System.out.print(fizzBuzz(i) + " ");
        }
        System.out.println();
    }

    public static String fizzBuzz(int n) {
        if (n % 15 == 0) return "FizzBuzz";   // check 3 AND 5 first
        if (n % 3 == 0) return "Fizz";
        if (n % 5 == 0) return "Buzz";
        return String.valueOf(n);
    }
}
