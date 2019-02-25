import java.util.Objects;

public class FooBar<K,V> implements Bar<K,V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75F;

    private int threshold;
    private int size;
    private final float loadFactor = 0.75F;

    private Node<K,V>[] nodesArray;

    private int hash(K key) {
        if (key == null){
            return 0;
        }
        return key.hashCode() ^ (key.hashCode() >>> 16);
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private V putVal(int hash, K key, V value) {
        if (nodesArray == null || nodesArray.length == 0){
            nodesArray = resize();
        }

        int index = (nodesArray.length - 1) & hash;
        if (nodesArray[index] == null) {
            nodesArray[index] = newNode(hash, key, value, null);
        } else {
            Node<K,V> nextNode;

            if (nodesArray[index].hash == hash && key.equals(nodesArray[index].key)){
                nextNode = nodesArray[index];
            } else {
                nextNode = setNextNode(hash, key, value, index);
            }

            if (nextNode != null) {
                return nextNode.value;
            }
        }

        if (++size > threshold){
            resize();
        }
        return null;
    }

    private Node<K, V> setNextNode(int hash, K key, V value, int index) {
        Node<K, V> nextNode;
        while (true) {
            nextNode = nodesArray[index].next;

            if (nodesArray[index].next == null) {
                nodesArray[index].next = newNode(hash, key, value, null);
                break;
            }
            if (nextNode.hash == hash && key.equals(nextNode.key)){
                break;
            }
            nodesArray[index] = nextNode;
        }
        return nextNode;
    }

    private Node<K,V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] oldNodesArray = nodesArray;
        int newThreshold = 0;
        int newCapacity;
        int oldThreshold = threshold;
        int oldCapacity = (oldNodesArray == null) ? 0 : oldNodesArray.length;

        if (oldCapacity >= MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldNodesArray;
        }
        else if (oldCapacity >= INITIAL_CAPACITY && oldCapacity << 1 < MAX_CAPACITY){
            newCapacity = oldCapacity;
            newThreshold = oldThreshold << 1;
        }
        else if (oldCapacity <= 0 &&oldThreshold > 0) {
            newCapacity = oldThreshold;
        }
        else {
            newCapacity = INITIAL_CAPACITY;
            newThreshold = (int)(LOAD_FACTOR * INITIAL_CAPACITY);
        }

        newThreshold = setNewThreshold(newThreshold, newCapacity);

        Node<K,V>[] newNodeArray = new Node[newCapacity];
        nodesArray = newNodeArray;
        threshold = newThreshold;

        fillNewArrayWithNodes(oldNodesArray,newNodeArray, newCapacity, oldCapacity);

        return newNodeArray;
    }

    private int setNewThreshold(int newThreshold, int newCapacity) {
        if (newThreshold == 0) {
            if (newCapacity < MAX_CAPACITY && newCapacity * loadFactor < MAX_CAPACITY ){
                newThreshold = (int) (newCapacity * loadFactor);
            } else {
                newThreshold = Integer.MAX_VALUE;
            }
        }
        return newThreshold;
    }

    private void fillNewArrayWithNodes(Node<K, V>[] oldNodesArray, Node<K, V>[] newNodeArray, int newCapacity, int oldCapacity) {
        if (oldNodesArray != null) {
            for (int index = 0; index < oldCapacity; ++index) {

                Node<K, V> currentNode;
                if (oldNodesArray[index] != null) {
                    currentNode = oldNodesArray[index];
                    oldNodesArray[index] = null;

                    if (currentNode.next == null) {
                        newNodeArray[currentNode.hash & (newCapacity - 1)] = currentNode;
                    } else {
                        setUpNextNodes(newNodeArray, oldCapacity, index, currentNode);
                    }
                }
            }
        }
    }

    private void setUpNextNodes(Node<K, V>[] newNodeArray, int oldCapacity, int index, Node<K, V> currentNode) {
        Node<K, V> hiHead = null;
        Node<K, V> loHead = null;
        Node<K, V> hiTail = null;
        Node<K, V> loTail = null;
        Node<K, V> nextNode;

        do {
            nextNode = currentNode.next;
            if ((currentNode.hash & oldCapacity) == 0) {
                loHead = getKvNode(currentNode, loHead, loTail);
                loTail = currentNode;
            } else {
                hiHead = getKvNode(currentNode, hiHead, hiTail);
                hiTail = currentNode;
            }
        } while ((currentNode = nextNode) != null);

        if (loTail != null) {
            loTail.next = null;
            newNodeArray[index] = loHead;
        }

        if (hiTail != null) {
            hiTail.next = null;
            newNodeArray[index + oldCapacity -1] = hiHead;
        }
    }

    private Node<K, V> getKvNode(Node<K, V> currentNode, Node<K, V> loHead, Node<K, V> loTail) {
        if (loTail == null) {
            loHead = currentNode;
        } else {
            loTail.next = currentNode;
        }
        return loHead;
    }


    @Override
    public V get(K key) {
        if(getNode(hash(key), key) == null){
            return null;
        }
        return Objects.requireNonNull(getNode(hash(key), key)).value ;
    }

    private Node<K,V> getNode(int hash, K key) {
        Node<K,V>[] newNodesArray  = nodesArray;
        Node<K,V> firstNode = newNodesArray[newNodesArray.length - 1 & hash];
        Node<K,V> nextNode = getNextNode(hash, key, firstNode);

        if (newNodesArray.length > 0 && nextNode != null) {
            return nextNode;
        } else {
            return null;
        }
    }

    private Node<K, V> getNextNode(int hash, K key, Node<K, V> firstNode) {
        Node<K, V> nextNode;
        if (firstNode.hash == hash && key.equals(firstNode.key)){
            return firstNode;
        }

        nextNode = firstNode.next;
        while (nextNode != null) {
            if (nextNode.hash == hash &&  key.equals(nextNode.key)){
                return nextNode;
            }
            nextNode = nextNode.next;
        }
        return null;
    }


    @Override
    public void remove(K key) {
        removeNode(hash(key), key);
    }

    private Node<K,V> removeNode(int hash, K key) {
        Node<K,V>[] newArrayOfNodes = nodesArray;
        int index = newArrayOfNodes.length - 1 & hash;

        Node<K, V> nodeToBeRemoved = getRemovingNode(hash, key, newArrayOfNodes, index);

        if (nodeToBeRemoved != null){
            return nodeToBeRemoved;
        }
        return null;
    }

    private Node<K, V> getRemovingNode(int hash, K key, Node<K, V>[] newArrayOfNodes, int index) {
        Node<K, V> currentNode = newArrayOfNodes[index];
        Node<K, V> nextNode = currentNode.next;
        K currentNodeKey = currentNode.key;
        Node<K, V> removingNode = null;

        if (currentNode.hash == hash && key.equals(currentNodeKey)){
            removingNode = currentNode;
        }
        else if (nextNode != null) {
            do {
                if (nextNode.hash == hash &&
                    ((currentNodeKey = nextNode.key) == key ||
                     (key != null && key.equals(currentNodeKey)))) {
                    removingNode = nextNode;
                    break;
                }
                currentNode = nextNode;
            } while ((nextNode = nextNode.next) != null);
        }

        return removeNodeWithNoValue(newArrayOfNodes, currentNode, removingNode, index);
    }

    private Node<K, V> removeNodeWithNoValue(Node<K, V>[] newArrayOfNodes, Node<K, V> currentNode, Node<K, V> removingNode, int index) {
        if (removingNode != null && removingNode.value == null) {
            if (removingNode == currentNode) {
                newArrayOfNodes[index] = removingNode.next;
            } else {
                currentNode.next = removingNode.next;
            }
            --size;
            return removingNode;
        }
        return null;
    }


    /* ---------------- static ------------------*/
    static class Node<K,V>{
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Bar.Entry) {
                Entry<?,?> e = (Entry<?,?>)o;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }


}
