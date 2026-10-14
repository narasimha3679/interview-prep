/**
 * Factorial: 5! = 5*4*3*2*1 = 120.
 * Java has no built-in factorial, so this is a loop or recursion.
 */
public class Factorial {

    public static void main(String[] args) {
        System.out.println(factorialLoop(5));      // 120
        System.out.println(factorialRecursive(5)); // 120
    }

    public static long factorialLoop(int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result = result * i;

        }
        return result;
    }

    public static long factorialRecursive(int n) {
        if (n <= 1) return 1;          // base case
        return n * factorialRecursive(n - 1);
        
    }
}


