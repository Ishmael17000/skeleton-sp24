package deque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private int size;
    private final Node sentinel;

    /** Define the Node class. */
    public class Node {

        public T value;
        public Node next;
        public Node pre;

        public Node(T v) {
            this(v, null, null);
        }

        public Node(T v, Node p, Node n) {
            value = v;
            pre = p;
            next = n;
            if (n != null) {
                n.pre = this;
            }
            if (p != null) {
                p.next = this;
            }
        }
    }
    /** Construct an empty LinkedListDeque61B */
    public LinkedListDeque61B() {
        size = 0;
        sentinel = new Node(null);
        sentinel.next = sentinel;
        sentinel.pre = sentinel;
    }

    @Override
    public void addFirst(T x) {
        sentinel.next = new Node(x, sentinel, sentinel.next);
        size += 1;
    }

    @Override
    public void addLast(T x) {
        sentinel.pre = new Node(x, sentinel.pre, sentinel);
        size += 1;
    }

    @Override
    // Only for test purpose.
    public List<T> toList() {
        List<T> returnList = new ArrayList<T>();
        Node start = sentinel;
        for (int i = 0; i < size; i ++) {
            start = start.next;
            returnList.add(start.value);
        }
        return returnList;
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    @Override
    public boolean isEmpty() {
        return (sentinel.next == sentinel);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else {
            T element = sentinel.next.value;
            sentinel.next = sentinel.next.next;
            sentinel.next.pre = sentinel;
            size -= 1;
            return element;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        else {
            T element = sentinel.pre.value;
            sentinel.pre = sentinel.pre.pre;
            sentinel.pre.next = sentinel;
            size -= 1;
            return element;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node current = sentinel;
        for (int i = index; i >= 0; i -= 1) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index, 0);
    }

    /** Get the element at index {@code target}, starting from Node {@code current} at index {@code start}.
     *  A helper function for getRecursive().
     */
    private T getRecursiveHelper(Node current, int target, int start) {
        if (target == start) {
            return current.value;
        }
        return getRecursiveHelper(current.next, target, start + 1);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LinkedListDeque61B<?> otherLinkedListDeque61B) {
            if (this.size != otherLinkedListDeque61B.size) {
                return false;
            }
            int i = 0;
            Node node1 = this.sentinel.next;
            Node node2 = (Node) otherLinkedListDeque61B.sentinel.next;
            while (i < size) {
                T ele1 = node1.value;
                T ele2 = node2.value;
                if (ele1 != ele2) {
                    return false;
                }
                i += 1;
                node1 = node1.next;
                node2 = node2.next;
            }
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    // Implements the iterator.
    private class LinkedListIterator implements Iterator<T> {
        private int position;
        private Node currentNode;

        public LinkedListIterator() {
            position = 0;
            currentNode = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return (position < size);
        }

        @Override
        // Pop the current item, and return its value;
        public T next() {
            T returnValue = currentNode.value;
            currentNode = currentNode.next;
            position += 1;
            return returnValue;
        }
    }

}
