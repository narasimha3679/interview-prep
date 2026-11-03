import java.util.Arrays;

/**
 * Sort an array.
 * BUILT-IN : Arrays.sort(numbers)   and   Collections.sort(list)
 * MANUAL   : bubble sort — swap neighbours that are out of order
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] nums = {5, 1, 4, 2, 8};
        System.out.println(Arrays.toString(sortedBuiltIn(nums.clone()))); // [1, 2, 4, 5, 8]
        System.out.println(Arrays.toString(bubbleSort(nums.clone())));    // [1, 2, 4, 5, 8]
    }

    public static int[] sortedBuiltIn(int[] nums) {
        Arrays.sort(nums);
        return nums;
    }

    public static int[] bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        return nums;
    }
}
