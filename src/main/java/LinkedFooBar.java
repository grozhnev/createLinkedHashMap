import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedFooBar<K,V> extends FooBar implements Bar {
    public LinkedList<K> linkedList;

    public LinkedFooBar() {
        super();
        this.linkedList = new LinkedList<>();
    }


    public void put(Object key, Object value) {

        if (!linkedList.contains(key)){
            linkedList.add((K) key);
        } else {
            super.put(key,value);
        }

    }

    public void remove(Object key) {
        if (linkedList.contains(key)){
            linkedList.remove(key);
        }
        super.remove(key);
    }


    public List<K> keySet(){
        LinkedList<K> innerLinkedList = new LinkedList<>(linkedList);
        ArrayList<K> array = new ArrayList<>();

        for (K key : linkedList) {
            if(!array.contains(key)){
                array.add(key);
            }
        }

        return innerLinkedList;
    }

}
