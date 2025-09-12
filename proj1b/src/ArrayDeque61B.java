import java.util.ArrayList;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {
    public int size;
    public Object[] items;
    private int underlyingSize;
    private final int initialUnderlyingSize = 100;


    /* The deque goes clockwise along a looped ArrayList.
     * Use the edge indexes to mark the deque in the ArrayList.
     * The underlying ArrayList has capacity 100 at the beginning, it will be extended when necessary.
     */
    private int startIndex;
    private int endIndex;

    // Create the constructor.
    public ArrayDeque61B() {
        size = 0;
        items = (T[]) new Object[initialUnderlyingSize];
        underlyingSize = initialUnderlyingSize;
        startIndex = 0; // Both positions are empty.
        endIndex = 1;
    }

    @Override
    public void addFirst(T x) {
        // Here is something requires thinking carefully.
        if (size == underlyingSize) {
            this.doubleSize();
        }
        items[startIndex] = x;
        startIndex = trueIndex(startIndex - 1);
        size += 1;

    }

    @Override
    public void addLast(T x) {
        if (size == underlyingSize) {
            this.doubleSize();
        }
        items[endIndex] = x;
        endIndex = trueIndex(endIndex + 1);
        size += 1;
    }

    @Override
    public List<T> toList() {
        return List.of();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }

    // Expand the capacity of our underlying ArrayList.
    private void doubleSize() {
        Object[] newList = (T[]) new Object[underlyingSize * 2];
        // ArrayList seems to have no slice copy method, so I'm just going to use for loop here.
        // Since we don't know where to start from and end with, so we have to travel through the whole list.
        // In this case, the startIndex and endIndex don't need to change.
        for (int i = 0; i < underlyingSize; i ++) {
            newList[i] = items[i]; // !!! This is not true. Think it twice.
        }
        underlyingSize *= 2;
        items = newList;
    }

    // For integer n, get its place in ArrayList by modulo underlyingSize.
    private int trueIndex(int n) {
        return Math.floorMod(n, underlyingSize);
    }
}
