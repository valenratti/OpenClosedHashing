import java.util.TreeSet;
import java.util.function.Function;

public class Tester {
    public static void main(String args[]){
        /*Function<Integer, Integer> fn =  (Integer n) -> {return n;};
        OpenHashing<Integer,String> openHashing = new OpenHashing<>(fn);

        openHashing.insert(45,"Test");
        openHashing.insert(5, "Test2");
        openHashing.delete(5);
        openHashing.insert(23,"Test3");
        openHashing.delete(23);
        openHashing.dump();*/

        Function<Integer, Integer> fn =  (Integer n) -> {return n;};
        ClosedHashing<Integer,String> closedHashingHashing = new ClosedHashing<>(fn);

        for(int i=0; i<100; i++){
            closedHashingHashing.insert(i+13, "Test");
        }

        closedHashingHashing.dump();

    }
}
