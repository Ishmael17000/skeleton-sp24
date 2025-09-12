import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {
    public int size;
    public Object[] items;
    private int underlyingSize;
    private static final int initialUnderlyingSize = 128;


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
        List<T> returnList = new ArrayList<>(size);
        for (int i = 0; i < size; i ++) {
            returnList.addLast((T) items[trueIndex(startIndex + 1 + i)]);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
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
        // To save memory.
        if (underlyingSize >= 16 && underlyingSize / size >= 4) {
            this.shrinkSize();
        }

        T returnElement = get(0);
        items[trueIndex(startIndex + 1)] = null;
        startIndex = trueIndex(startIndex + 1);
        size -= 1;
        return returnElement;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        // To save memory.
        if (underlyingSize >= 16 && underlyingSize / size >= 4) {
            this.shrinkSize();
        }

        T returnElement = get(size - 1);
        items[trueIndex(endIndex - 1)] = null;
        endIndex = trueIndex(endIndex - 1);
        size -= 1;
        return returnElement;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) items[trueIndex(startIndex + index + 1)];
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }

    // Expand the capacity of our underlying ArrayList.
    private void doubleSize() {
        Object[] newList = (T[]) new Object[underlyingSize * 2];
        for (int i = 0; i < size; i ++) {
            newList[i] = items[trueIndex(startIndex + 1 + i)];
        }
        underlyingSize *= 2;
        startIndex = underlyingSize - 1;
        endIndex = size;
        items = newList;
    }

    //Shrink the capacity to half.
    private void shrinkSize() {
        Object[] newList = (T[]) new Object[underlyingSize / 2];
        for (int i = 0; i < size; i ++) {
            newList[i] = items[trueIndex(startIndex + 1 + i)];
        }
        underlyingSize /= 2;
        startIndex = underlyingSize - 1;
        endIndex = size;
        items = newList;
    }

    // For integer n, get its place in ArrayList by modulo underlyingSize.
    private int trueIndex(int n) {
        return Math.floorMod(n, underlyingSize);
    }
}
