import java.util.NoSuchElementException;
import java.util.function.Function;

public class ClosedHashing<K,V> extends Hash<K,V> {

    protected boolean[] states = new boolean[initialLookupSize]; //true si esta ocupado, false si es borrado logico

    public ClosedHashing(Function mappingFn) {
        super(mappingFn);
    }

    @Override
    public void dump(){
        for(int rec= 0; rec < LookUp.length; rec++)
            if (states[rec] == false) {
                System.out.print(String.format("slot %d is empty", rec));
                if(LookUp[rec] != null)
                    System.out.println(", there was a logic delete");
                else
                    System.out.println();
            }
            else
                System.out.println(String.format("slot %d contains %s", rec, LookUp[rec]));
    }

    @Override
    protected void duplicateSpaceAndRehash(){
        Node<K,V>[] auxLookUp = new Node[initialLookupSize];
        boolean[] auxStates = new boolean[initialLookupSize];
        System.arraycopy(LookUp,0,auxLookUp,0,initialLookupSize);
        System.arraycopy(states,0,auxStates,0,initialLookupSize);
        initialLookupSize *= 2;
        LookUp = new Node[initialLookupSize]; //reset
        states = new boolean[initialLookupSize];
        usedKeys = 0;
        int i = 0;
        for(Node<K,V> node : auxLookUp){
            if( node!= null && auxStates[i%initialLookupSize])
                insert(node.key,node.value);
            i++;
        }
    }

    @Override
    public void insert(K key, V value) {
        int index = hash(key);
        Node<K, V> node;
        int firstLogicDelete = -1;
        boolean found = false;

        while ((node = LookUp[index]) != null && !found) {
            //Si la key es la misma, updateo el nodo<k,v>
            if (states[index] == true) {
                if (node.key.equals(key)) {
                    LookUp[index] = new Node<K, V>(key, value);
                    found = true;
                }
            }
            //Si la key es distinta, me fijo si tengo que guardar la posicion
            else {
                if (firstLogicDelete == -1)
                    firstLogicDelete = index;
            }
            if (index == LookUp.length - 1)
                index = 0;
            else index++;
        }


        //Si el key que voy a insertar no lo encontre, tengo que ver si lo inserto en un index de borrado logico o en un espacio libre(null)
        if (!found) {
            int newIndex = firstLogicDelete == -1 ? index : firstLogicDelete;
            LookUp[newIndex] = new Node<K, V>(key, value);
            usedKeys++;
            states[newIndex] = true;
            loadFactor = ((double) usedKeys / initialLookupSize);
            if (loadFactor > THERSHOLD)
                duplicateSpaceAndRehash();
        }

    }

    @Override
    public void delete(K key) {
        int index = hash(key);
        int foundIndex = -1;
        Node<K, V> node;
        boolean found = false;
        while ( (node = LookUp[index] ) != null && !found) {
            //Si lo encuentro, tengo que ver si hago borrado logico o fisico
            if (states[index] == true && node.key.equals(key)) {
                found = true;
                foundIndex = index;
            }
            if (index == LookUp.length - 1)
                index = 0;
            else index++;
        }
        if (found) {
            if (LookUp[index] == null)
                LookUp[index] = null;
            usedKeys--;
            states[foundIndex] = false;
        }

        else throw new NoSuchElementException("No se ha econtrado el elemento que desea eliminar");
    }
}
