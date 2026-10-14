/**
 * Reverse the WORDS in a sentence: "Java is fun" -> "fun is Java".
 * Note: this is not the same as reversing the whole string.
 * Built-in helpers: String.split() and String.join().
 */
public class ReverseWords {

    public static void main(String[] args) {
        System.out.println(reverseWords("Java is fun")); // fun is Java
    }

    public static String reverseWords(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(" ");
        }
        return sb.toString();
    }
}
