import java.util.Arrays;

/**
 * Find the largest and smallest values in an array.
 * BUILT-IN : Arrays.stream(nums).max() / .min()
 * MANUAL   : one loop keeping two variables
 */
public class MaxAndMin {

    public static void main(String[] args) {
        int[] nums = {3, 7, 1, 9, 4};
        System.out.println("built-in: max=" + maxBuiltIn(nums) + ", min=" + minBuiltIn(nums));
        System.out.println("manual:   max=" + maxManual(nums) + ", min=" + minManual(nums));
    }

    public static int maxBuiltIn(int[] nums) {
        return Arrays.stream(nums).max().getAsInt();
    }

    public static int minBuiltIn(int[] nums) {
        return Arrays.stream(nums).min().getAsInt();
    }

    public static int maxManual(int[] nums) {
        int max = nums[0];
        for (int n : nums) {
            if (n > max) max = n;
        }
        return max;
    }

    public static int minManual(int[] nums) {
        int min = nums[0];
        for (int n : nums) {
            if (n < min) min = n;
        }
        return min;
    }
}
