/**
 * Fibonacci: 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
 * Loop is simple and fast; recursion is the "textbook" version.
 */
public class Fibonacci {

    public static void main(String[] args) {
        printSeries(10);                       // 0 1 1 2 3 5 8 13 21 34
        System.out.println(fibRecursive(10));  // 55
    }

    public static void printSeries(int n) {
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(a + " ");
            int next = a + b;
            a = b;
            b = next;
        }
        System.out.println();
    }

    public static int fibRecursive(int n) {
        if (n <= 1) return n;          // base case
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }
}
