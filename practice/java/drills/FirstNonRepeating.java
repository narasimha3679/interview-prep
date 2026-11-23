// Return the first character that appears exactly once. If none, return '-'.

// "swiss" → w        (s appears 3 times, w once)
// "aab"   → b
// "aabb"  → -
import java.util.*;
class FirstNonRepeating{
    public static void main(String args[]){
        String word = "swiss";
        
        Map<Character,Integer> count = new HashMap<>();
        for(char c: word.toCharArray()){

            count.merge(c,1, Integer::sum);
         
        }
       for (char c : word.toCharArray()) {
         if (count.get(c) == 1) {
            System.out.println(c);
         return;
            }
        }
        System.out.println('-');
    }
}