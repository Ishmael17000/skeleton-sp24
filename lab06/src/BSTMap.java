import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    // These are instance variables.
    // private int height;
    private Node root;
    private int size;

    // Initialize an empty BSTMap.
    public BSTMap() {
        // height = 0;
        size = 0;
        root = null;
    }

    // A helper subclass, representing the nodes.
    private class Node {
        // The children of a node are set to two trees.
        private Node left;
        private Node right;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }

    }


    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        root = putToNode(key, value, root);
        // Size manipulation already contained in the helper method.
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        return getFromNode(key, root);
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        return containsFromNode(key, root);
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new TreeSet<>();
        for (K x : this) {
            set.add(x);
        }
        return set;
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        V val = get(key);
        root = getRemovedNode(key, root);
        return val;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    // Helper methods are below.


    // This method compares two keys. Return as default with compareTo().
    private int compareKeys(K key1, K key2) {
        return key1.compareTo(key2);
    }

    // Helper method for put. Put k,v to a specified tree.
    private Node putToNode(K key, V value, Node node) {
        // Base case. Either reach an end, or meet the key.
        if (node == null) {
            size ++;
            return new Node(key, value);
        } else if (compareKeys(key, node.key) == 0) {
            node.value = value;
        }
        // If not found, search recursively.
        else if (compareKeys(key, node.key) > 0) {
            node.right = putToNode(key, value, node.right);
        } else {
            node.left = putToNode(key, value, node.left);
        }
        return node;
    }

    // Helper method for get. Get key from a specified tree.
    private V getFromNode(K key, Node node) {
        if (node == null) {
            return null;
        } else if (compareKeys(key, node.key) == 0) {
            return node.value;
        } else {
            return returnTruth(getFromNode(key, node.left), getFromNode(key, node.right));
        }
    }

    // Helper method for contains. Check if contains key from node.
    private boolean containsFromNode(K key, Node node) {
        if (node == null) {
            return false;
        } else if (compareKeys(key, node.key) == 0) {
            return true;
        } else {
            return (containsFromNode(key, node.left) || containsFromNode(key, node.right));
        }
    }

    // Return the element between two that are not null.
    // If both are null, return null.
    private V returnTruth(V v1, V v2) {
        if (v1 != null) {
            return v1;
        } else {
            return v2;
        }
    }

    // Helper method for remove. Remove the current key if it meets. Return the modified node.
    private Node getRemovedNode(K key, Node node) {
        if (node == null) {
            return null;
        }

        int compare = compareKeys(key, node.key);
        if (compare > 0) {
            node.right = getRemovedNode(key, node.right);
        } else if (compare < 0) {
            node.left = getRemovedNode(key, node.left);
        } else {
            // Find the target key.
            size -= 1;
            // removed[0] = node;
            // No child. Just delete it.
            if (node.left == null && node.right == null) {
                node = null;
            }
            // One child. Move that child up.
            else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            }
            // To children. Find the largest of the left tree. Move it to the current node.
            else {
                Node successor = findLargest(node.left);
                node.key = successor.key;
                node.value = successor.value;
                node.left = removeLargest(node.left);
            }
        }
        return node;
    }

    // Return the largest node.
    private Node findLargest(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Remove the largest node.
    private Node removeLargest(Node node) {
        if (node.right == null) {
            return node.left;
        }
        node.right = removeLargest(node.right);
        return node;
    }

    private class BSTMapIter implements Iterator<K> {
        private final Stack<Node> stack;

        private BSTMapIter() {
            stack = new Stack<>();
            pushLeftAll(root);
        }

        @Override
        public boolean hasNext() {
            return (! stack.isEmpty());
        }

        @Override
        public K next() {
            if (! hasNext()) { throw new NoSuchElementException(); }
            Node top = stack.pop();
            if (top.right != null) {
                pushLeftAll(top.right);
            }

            return top.key;
        }

        // Starting from node, recursively push all the left nodes along the way.
        private void pushLeftAll(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
    }
}
