/**
 * Prime check: divisible only by 1 and itself.
 * No built-in in Java, so test divisibility up to the square root.
 */
public class PrimeNumber {

    public static void main(String[] args) {
        System.out.println(isPrime(7));  // true
        System.out.println(isPrime(10)); // false
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
