import java.util.*;

public class EmailDomains {

    public static void main(String[] args) {

        String[] emails = {
               "x@a.com", "y@a.com", "z@b.com", "nope"
        };

        // EXPECTED OUTPUT
        // gmail.com=3
        // invalid=1
        // yahoo.com=1

        // TODO 1: for each email, get the domain = part after '@'
        //         (no '@' at all -> "invalid")
        // TODO 2: count how many emails each domain has
        // TODO 3: sort by count descending, ties by domain name ascending
        // TODO 4: print "domain=count", one per line

        // Extra test when the above works (write the expected output yourself first):
        // String[] more = {"x@a.com", "y@a.com", "z@b.com", "nope"};

        Map<String,Integer> domainCount = new HashMap<>();

        for(String email: emails){
            String domain;
            if(email.contains("@")){
             domain = email.split("@")[1];
            }else{
                domain = "invalid";
            }
            
            domainCount.merge(domain,1, Integer::sum);
        }
        List<Map.Entry<String,Integer>> sortedDomainCount = new ArrayList<>(domainCount.entrySet());

        sortedDomainCount.sort((a,b)->a.getValue().equals(b.getValue())
                                       ? a.getKey().compareTo(b.getKey())
                                       : a.getValue().compareTo(b.getValue()) ); 

        for(Map.Entry<String,Integer> entry: sortedDomainCount){
            
            System.out.println(entry.getKey()+"="+entry.getValue());
        
        }
    }
}
