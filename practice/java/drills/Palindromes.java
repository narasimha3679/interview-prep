public class Palindromes {

    public static void main(String[] args) {

        String[] words = {"racecar", "java", "level", "noon", "test","","a"};

        // EXPECTED OUTPUT
        // racecar true
        // java false
        // level true
        // noon true
        // test false

        for (String w : words) {
            System.out.println(w + " " + isPalindrome(w));
        }
    }

    // TODO: compare the first character with the last, then move inward.
    //       Any mismatch -> false. Pointers meet -> true.
    // static boolean isPalindrome(String s) {
    //    StringBuilder sb = new StringBuilder(s);

    //    sb.reverse();

    //    return s.equals(sb.toString());
    //    }
    
    //without stringbuilder
    static boolean isPalindrome(String s) {
        int end = s.length()-1;

        for(int start=0; start < end; start++ ){
            if(s.charAt(start)==s.charAt(end)){
                end--;
            }else{
                return false;
            }
        }
        return true;
    }
}
