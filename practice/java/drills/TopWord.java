// Count the words in a sentence. Ignore words shorter than 3 characters. Print the top 2 as word=count, count descending, ties alphabetically. If fewer than 2 exist, print whatever exists. If none, print nothing.

// "the cat the dog the a an"   →  the=3
//                                 cat=1

// "a an i"                     →  (nothing)

// "hello hello world"           →  hello=2
//                                 world=1

// "run run run"                →  run=3
import java.util.*;

class TopWord{
    public static void main(String args[]){
        String sentence1 = "the cat the dog the a an";
        String sentence2 = "a an i";
        String sentence3 = "hello hello world" ;
        String sentence4 = "run run run";

         re(sentence1);
         re(sentence2);
         re(sentence3);
         re(sentence4);
    }

    static void re(String sentence){

        String[] words = sentence.split(" ");

        Map<String,Integer> wordcount = new HashMap<>();

        for(String word: words){
            if(word.length()>2){
                wordcount.merge(word, 1, Integer::sum);
            }
        }

        List<Map.Entry<String,Integer>> sortedwordcount = new ArrayList<>(wordcount.entrySet());

        sortedwordcount.sort((a,b)->a.getValue().equals(b.getValue())
                                    ? a.getKey().compareTo(b.getKey())
                                    : b.getValue() - a.getValue());
        int loopcounter = 0;
        for(Map.Entry<String,Integer> word : sortedwordcount){
            if(loopcounter >1){break;}
            System.out.println(word.getKey()+"="+word.getValue());
            loopcounter++;
            
        }
        
    }
}