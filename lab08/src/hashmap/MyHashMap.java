package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private int capacity;
    private final double loadFactor;
    private double size;

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = initialCapacity;
        this.size = 0.0;

        // Create the empty buckets.
        buckets = createEmptyBuckets(capacity);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // TODO: Fill in this method.
        return new ArrayList<>();
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void put(K key, V value) {
        putNodeTo(key, value, buckets);

        if (size/capacity >= loadFactor) {
            redistribute();
        }
    }

    @Override
    public V get(K key) {
        int index = hashIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = hashIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return (int)size;
    }

    @Override
    public void clear() {
        size = 0;
        buckets = createEmptyBuckets(capacity);
    }

    @Override
    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                returnSet.add(node.key);
            }
        }
        return returnSet;
    }

    @Override
    public V remove(K key) {
        int index = hashIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                size -= 1;
                return node.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    // Return the index of bucket where key is going.
    private int hashIndex(K key) {
        int signature = key.hashCode() % capacity;
        while (signature < 0) {
            signature += capacity;
        }
        return signature;
    }

    // Resize the buckets.
    private void redistribute() {
        capacity *= 2;
        Collection<Node>[] newBuckets = createEmptyBuckets(capacity);
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                putNodeToWithoutChecking(node.key, node.value, newBuckets);
            }
        }

        buckets = newBuckets;
    }

    // Put a Node into a list of buckets.
    private void putNodeTo(K key, V v, Collection<Node>[] buckets) {
        int index = hashIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = v;
                return;
            }
        }
        bucket.add(new Node(key,v));
        size += 1;
    }

    // Put a Node into a new list of buckets.
    private void putNodeToWithoutChecking(K key, V v, Collection<Node>[] buckets) {
        int index = hashIndex(key);
        Collection<Node> bucket = buckets[index];
        bucket.add(new Node(key,v));
    }

    private Collection<Node>[] createEmptyBuckets(int capacity) {
        Collection<Node>[] newBuckets = new Collection[capacity];
        for (int i = 0; i < capacity; i ++) {
            newBuckets[i] = createBucket();
        }
        return newBuckets;
    }


    // Subclass for Iterator.
    private class MyHashMapIterator implements Iterator<K> {
        private int currentBucket;
        private Iterator<Node> currentIterator;
        private K currentKey;


        private MyHashMapIterator() {
            currentBucket = 0;
            currentIterator = buckets[0].iterator();
        }

        @Override
        public boolean hasNext() {
            if (currentBucket == capacity-1) {
                return currentIterator.hasNext();
            }
            return (currentBucket < capacity);
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            movePointer();
            return currentKey;
        }

        // Move the pointer one valid node ahead.
        // Start from currentBucket.
        private void movePointer() {
            if (currentIterator.hasNext()) {
                currentKey = currentIterator.next().key;
                return;
            }
            // When current is empty, move until find a nonempty bucket or reach the end.
            while (!currentIterator.hasNext() && currentBucket < capacity) {
                nextBucket();
            }
            if (currentBucket == capacity) {
                return;
            }
            // Find a nonempty bucket.
            currentKey = currentIterator.next().key;
        }

        // Move to the next bucket. Change currentBucket and currentIterator.
        // Assume there is a next bucket.
        private void nextBucket() {
            currentBucket += 1;
            if (currentBucket < capacity) {
                currentIterator = buckets[currentBucket].iterator();
            }
        }
    }
}
