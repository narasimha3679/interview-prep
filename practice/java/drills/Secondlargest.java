// Second largest distinct number in an int[]. If there isn't one, print -.

// {3, 7, 7, 5, 1} → 5     (7 is first, 5 is second; duplicate 7 doesn't count)
// {4, 4, 4}       → -
// {10, 20}        → 10

//import java.util.*;

class Secondlargest{
    public static void main(String args[]){
        int[] numbers = {3, 7, 7, 5, 1};

        int largest=0, second=0;

        for(int n:numbers){

            if(n > largest){
                second=largest;
                largest = n;
            }else if(n > second& n!= largest){
                second=n;
            }
        }

        if(second == 0){
        System.out.println('-');
        }else{
        System.out.println(second);
        }
        
    }
}