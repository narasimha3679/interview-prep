import java.util.*;
import java.util.stream.*;

public class Squares {

    public static void main(String[] args) {

        List<Integer> nums = List.of(5, 12, 7, 8, 3, 20);

        // EXPECTED OUTPUT
        // [64, 144, 400]

        // TODO: keep even numbers, square them, sort ascending, collect to a List.
        //       One stream chain: stream().filter(...).map(...).sorted().toList()

        System.out.println(nums);
    }
}
