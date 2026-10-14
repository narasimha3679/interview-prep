import java.util.Arrays;

/**
 * Find the second largest number in an array.
 * BUILT-IN : sort, then take the element at length - 2
 * MANUAL   : one pass tracking first and second place
 */
public class SecondLargest {

    public static void main(String[] args) {
        int[] nums = {10, 5, 8, 20, 15};
        System.out.println(secondLargestManual(nums));  // 15
        System.out.println(secondLargestBuiltIn(nums));  // 15
    }

    public static int secondLargestManual(int[] nums) {
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        for (int n : nums) {
            if (n > first) {
                second = first;
                first = n;
            } else if (n > second && n != first) {
                second = n;
            }
        }
        return second;
    }

    public static int secondLargestBuiltIn(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length - 2];
    }
}
