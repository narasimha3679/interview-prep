// Count words in a sentence, print word=count, sorted by count descending, ties alphabetically.

// "the cat the dog the"

// the=3
// cat=1
// dog=1
import java.util.*;

class WordCount{
    public static void main(String args[]){
        String sentence = "the cat the dog the";

        Map<String,Integer> count = new HashMap<>();

        String[] words = sentence.split(" ");

        for(String word: words){
            count.merge(word, 1 , Integer::sum);
        }

        List<Map.Entry<String,Integer>> sortedList = new ArrayList<>(count.entrySet());

        sortedList.sort((a,b)->a.getValue().equals(b.getValue())
                                ? a.getKey().compareTo(b.getKey())
                                : b.getValue() - a.getValue());

        for(Map.Entry<String,Integer> e:sortedList ){
            System.out.println(e);
        }
    }
}