/**
 * Swap two numbers.
 * EASY : use a temp variable
 * TRICKY: no temp variable, using +/- arithmetic (or the array/lambda swaps)
 */
public class SwapNumbers {

    public static void main(String[] args) {
        int a = 5, b = 10;

        // With a temp variable (the normal way)
        int temp = a;
        a = b;
        b = temp;
        System.out.println("with temp: a=" + a + ", b=" + b); // a=10, b=5

        // Without a temp variable (arithmetic)
        int x = 5, y = 10;
        x = x + y;   // 15
        y = x - y;   // 5
        x = x - y;   // 10
        System.out.println("no temp:   x=" + x + ", y=" + y); // x=10, y=5
    }
}
