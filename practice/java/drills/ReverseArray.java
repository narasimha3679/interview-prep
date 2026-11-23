import java.util.*;

public class ReverseArray {

    public static void main(String[] args) {

        int[] nums = {1, 2, 3, 4, 5};

        // EXPECTED OUTPUT
        // [5, 4, 3, 2, 1]

        reverse(nums);
        System.out.println(Arrays.toString(nums));
    }

    // TODO: reverse in place — swap the two ends, then walk inward.
    //       No new array, no sorting.
    static void reverse(int[] a) {
       
        int start=0,end=a.length-1;

        while(start<end){
            int t = a[start];
            a[start] = a[end];
            a[end] = t;
            start++;
            end--;
        }
    }
}
