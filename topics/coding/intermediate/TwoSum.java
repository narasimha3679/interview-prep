import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Return the indices of the two numbers that add up to target.
 * Assumes exactly one solution and the same element cannot be used twice.
 */
public class TwoSum {

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));  // [0, 1]
        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));       // [1, 2]
        System.out.println(Arrays.toString(twoSum(new int[]{3, 3}, 6)));          // [0, 1]
        System.out.println(Arrays.toString(twoSum(new int[]{1, 2}, 10)));         // [-1, -1]
    }
}
