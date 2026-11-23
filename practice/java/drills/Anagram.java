import java.util.*;
public class Anagram {

    public static void main(String[] args) {

        String[][] pairs = {
                {"listen", "silent"},
                {"java", "jvaa"},
                {"hello", "world"},
                {"abc", "ab"}
        };

        // EXPECTED OUTPUT
        // listen silent true
        // java jvaa true
        // hello world false
        // abc ab false

        for (String[] p : pairs) {
            System.out.println(p[0] + " " + p[1] + " " + isAnagram(p[0], p[1]));
        }
    }

    // TODO: two words are anagrams when sorting their characters gives the same result.
    //       String -> char[] (toCharArray), sort both, compare (Arrays.equals).
    // static boolean isAnagram(String a, String b) {
    //     char[] first = a.toCharArray();
    //     char[] second = b.toCharArray();

    //     Arrays.sort(first);
    //     Arrays.sort(second);

    //     return Arrays.equals(first,second);
    // }

    //without sort
        static boolean isAnagram(String a, String b) {
  
        if(a.length() != b.length()){
            return false;
        }

        Map<Character , Integer> aMap = new HashMap<>();
        Map<Character , Integer> bMap = new HashMap<>();

        for(char c : a.toCharArray()){
            aMap.merge(c,1,Integer::sum);
        }
        for(char c : b.toCharArray()){
            bMap.merge(c,1,Integer::sum);
        }

        return aMap.equals(bMap);
    }


}
