import java.util.*;
import java.util.function.Function;

public class OpenHashing <K,V> extends Hash<K,V> {

    protected TreeSet<Node<K, V>>[] LookUpList = new TreeSet[initialLookupSize];

    public OpenHashing(Function<? super K, Integer> mappingFn) {
        super(mappingFn);
    }

    @Override
    public void insert(K key, V value) {
        int index = hash(key);
        if (LookUpList[index] == null) {
            LookUpList[index] = new TreeSet<Node<K, V>>();
            LookUpList[index].add(new Node<K, V>(key, value, prehash.apply(key)));
        } else {
            LookUpList[index].add(new Node<K, V>(key, value, prehash.apply(key)));
        }
    }

    @Override
    public void delete(K key) {
        int index = hash(key);
        boolean found = false;
        if (LookUpList[index] != null) {
            TreeSet<Node<K, V>> toSearch = LookUpList[index];
            Iterator<Node<K,V>> it = toSearch.iterator();
            Node<K,V> node;
            while(it.hasNext()) {
                node = it.next();
                if (node.prehash > prehash.apply(key))
                    break;
                if (node.prehash == prehash.apply(key)) {
                    found = true;
                    it.remove();
                }
            }
            if (!found || LookUpList[index] == null) {
                throw new NoSuchElementException("Element to delete hasn't been found");
            }
            else if(LookUpList[index].size() == 0)
                LookUpList[index] = null;
        }
    }

    @Override
    public void dump() {
        for (int rec = 0; rec < LookUpList.length; rec++)
            if (LookUpList[rec] == null)
                System.out.println(String.format("slot %d is empty", rec));
            else{
                System.out.println(String.format("slot %d contains %s", rec, LookUpList[rec]));
            }

    }

}
