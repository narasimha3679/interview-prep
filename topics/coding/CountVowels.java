/**
 * Count vowels and consonants in a string.
 * The built-in trick: String.indexOf() to check "is this char a vowel".
 */
public class CountVowels {

    public static void main(String[] args) {
        count("Hello World");  // Vowels: 3, Consonants: 7
    }

    public static void count(String str) {
        int vowels = 0, consonants = 0;
        String lower = str.toLowerCase();

        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c < 'a' || c > 'z') continue;      // skip space / punctuation / digits
            if ("aeiou".indexOf(c) >= 0) vowels++;
            else consonants++;
        }
        System.out.println("Vowels: " + vowels + ", Consonants: " + consonants);
    }
}
