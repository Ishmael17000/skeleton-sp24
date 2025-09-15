package deque;

import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T>{
    public Comparator<T> defaultComparator;
    public MaxArrayDeque61B(Comparator<T> c) {
            super();
            defaultComparator = c;
    }

    /** Returns the maximum element in the deque as governed by the previously given Comparator. */
    public T max() {
        return maxHelper(defaultComparator);
    }

    /** Returns the maximum element in the deque as governed by the parameter Comparator c. */
    public T max(Comparator<T> c) {
        return maxHelper(c);
    }

    // Actual comparing operation.
    private T maxHelper(Comparator<T> c) {
        if (size == 0) { return null; }
        T maxElement = get(0);
        for (int i = 0; i < size ; i ++) {
            T thisElement = this.get(i);
            if (c.compare(thisElement, maxElement) > 0) {
                maxElement = thisElement;
            }
        }
        return maxElement;
    }
}
