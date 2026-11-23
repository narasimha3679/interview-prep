import java.util.*;

public class GapDrill {

    public static void main(String[] args) {

        // A: reverse a string without StringBuilder
        System.out.println(reverse("interview"));            // weivretni

        // B: remove duplicates, keep first-seen order
        System.out.println(Arrays.toString(dedupe(new int[]{3, 1, 3, 2, 1, 4})));  // [3, 1, 2, 4]
    }

    // A: two pointers, same as palindrome, but build a char[] and swap.
    static String reverse(String s) {
        char[] c = s.toCharArray();

        int start=0, end=s.length()-1;

        while(start<end){
            char t = c[start];
            c[start] = c[end];
            c[end] = t;
            start++;
            end--;
        }
        s = new String(c);
        return s;
    }

    // B: a Set removes duplicates; LinkedHashSet keeps first-seen order.
static int[] dedupe(int[] a) {
    Set<Integer> seen = new LinkedHashSet<>();
    for (int n : a) {
        seen.add(n);          // autoboxing: int -> Integer
    }
    int[] out = new int[seen.size()];
    int i = 0;
    for (int n : seen) {
        out[i++] = n;
    }
    return out;
}
}
