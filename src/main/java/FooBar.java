import java.util.Objects;

public class FooBar<K,V> implements Bar<K,V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;

    final float loadFactor = 0.75F;

    int threshold;
    int size;

    Node<K,V>[] nodesArray;


    final int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    final V putVal(int hash, K key, V value) {
        Node<K,V>[] localNodeArray;
        Node<K,V> insertionNode;
        int lengthOfNodeArray;
        int index;

        if ((localNodeArray = nodesArray) == null || (lengthOfNodeArray = localNodeArray.length) == 0)
            lengthOfNodeArray = (localNodeArray = resize()).length;
        if ((insertionNode = localNodeArray[index = (lengthOfNodeArray - 1) & hash]) == null)
            localNodeArray[index] = newNode(hash, key, value, null);
        else {
            Node<K,V> otherValuenNode;
            K otherValueNodeKey;

            if (insertionNode.hash == hash &&
                ((otherValueNodeKey = insertionNode.key) == key || (key != null && key.equals(otherValueNodeKey))))
                otherValuenNode = insertionNode;
            else {
                while (true) {
                    if ((otherValuenNode = insertionNode.next) == null) {
                        insertionNode.next = newNode(hash, key, value, null);
                        break;
                    }
                    if (otherValuenNode.hash == hash &&
                        ((otherValueNodeKey = otherValuenNode.key) == key || (key != null && key.equals(otherValueNodeKey))))
                        break;
                    insertionNode = otherValuenNode;
                }
            }
            if (otherValuenNode != null) {
                V oldValue = otherValuenNode.value;
                return oldValue;
            }
        }
        if (++size > threshold)
            resize();
        return null;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    final Node<K,V>[] resize() {
        Node<K,V>[] oldNodesArray = nodesArray;
        int oldCapacity = (oldNodesArray == null) ? 0 : oldNodesArray.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold = 0;
        if (oldCapacity > 0) {
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldNodesArray;
            } else if ((newCapacity = oldCapacity << 1) < MAXIMUM_CAPACITY && oldCapacity >= DEFAULT_INITIAL_CAPACITY){
                newThreshold = oldThreshold << 1;
            }
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (newThreshold == 0) {
            float newMaxThresholdBound = (float)newCapacity * loadFactor;
            newThreshold = (newCapacity < MAXIMUM_CAPACITY && newMaxThresholdBound < (float)MAXIMUM_CAPACITY ?
                      (int)newMaxThresholdBound : Integer.MAX_VALUE);
        }
        threshold = newThreshold;
//        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newNodeArray = (Node<K,V>[])new Node[newCapacity];
        nodesArray = newNodeArray;
        if (oldNodesArray != null) {
            for (int j = 0; j < oldCapacity; ++j) {
                Node<K,V> currentNode;
                if ((currentNode = oldNodesArray[j]) != null) {
                    oldNodesArray[j] = null;
                    if (currentNode.next == null){
                        newNodeArray[currentNode.hash & (newCapacity - 1)] = currentNode;
                    } else {
                        Node<K,V> hiHead = null;
                        Node<K,V> loHead = null;
                        Node<K,V> hiTail = null;
                        Node<K,V> loTail = null;
                        Node<K,V> nextNode;

                        do {
                            nextNode = currentNode.next;
                            if ((currentNode.hash & oldCapacity) == 0) {
                                if (loTail == null){
                                    loHead = currentNode;
                                } else {
                                    loTail.next = currentNode;
                                }
                                loTail = currentNode;

                            } else {
                                if (hiTail == null){
                                    hiHead = currentNode;
                                } else {
                                    hiTail.next = currentNode;
                                }
                                hiTail = currentNode;
                            }
                        } while ((currentNode = nextNode) != null);

                        if (loTail != null) {
                            loTail.next = null;
                            newNodeArray[j] = loHead;
                        }

                        if (hiTail != null) {
                            hiTail.next = null;
                            newNodeArray[j + oldCapacity] = hiHead;
                        }
                    }
                }
            }
        }
        return newNodeArray;
    }


    @Override
    public V get(K key) {
        Node<K,V> selectedNode = getNode(hash(key), key);
        return selectedNode == null ? null : selectedNode.value ;
    }

    final Node<K,V> getNode(int hash, K key) {
        Node<K,V>[] newNodesArray;
        Node<K,V> firstNode;
        Node<K,V> nextNode;
        int nodesArrayLenght;
        K nodeKey;

        if ((newNodesArray = nodesArray) != null && (nodesArrayLenght = newNodesArray.length) > 0 && (firstNode = newNodesArray[(nodesArrayLenght - 1) & hash]) != null) {
            if (firstNode.hash == hash &&
                ((nodeKey = firstNode.key) == key || (key != null && key.equals(nodeKey))))
                return firstNode;
            if ((nextNode = firstNode.next) != null) {
                do {
                    if (nextNode.hash == hash &&
                        ((nodeKey = nextNode.key) == key || (key != null && key.equals(nodeKey))))
                        return nextNode;
                } while ((nextNode = nextNode.next) != null);
            }
        }
        return null;
    }


    @Override
    public V remove(K key) {
        Node<K,V> removingNode;
        return (removingNode = removeNode(hash(key), key, null)) == null ? null : removingNode.value;
    }

    final Node<K,V> removeNode(int hash, K key, V value) {
        Node<K,V>[] newArrayOfNodes;
        Node<K,V> currentNode;
        int lenghtOfNewArrayOfNodes;
        int index;

        if ((newArrayOfNodes = nodesArray) != null && (lenghtOfNewArrayOfNodes = newArrayOfNodes.length) > 0 && (currentNode = newArrayOfNodes[index = (lenghtOfNewArrayOfNodes - 1) & hash]) != null) {
            Node<K,V> node = null;
            Node<K,V> nextNode;
            K currentNodeKey;
            V currentNodeValue;

            if (currentNode.hash == hash &&
                ((currentNodeKey = currentNode.key) == key || (key != null && key.equals(currentNodeKey)))){
                node = currentNode;
            } else if ((nextNode = currentNode.next) != null) {
                do {
                    if (nextNode.hash == hash &&
                        ((currentNodeKey = nextNode.key) == key ||
                         (key != null && key.equals(currentNodeKey)))) {
                        node = nextNode;
                        break;
                    }
                    currentNode = nextNode;
                } while ((nextNode = nextNode.next) != null);
            }

            if (node != null && ((currentNodeValue = node.value) == value ||
                                 (value != null && value.equals(currentNodeValue)))) {
                if (node == currentNode) {
                    newArrayOfNodes[index] = node.next;
                } else {
                    currentNode.next = node.next;
                }
                --size;
                return node;
            }
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

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Bar.Entry) {
                Entry<?,?> e = (Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }


}
