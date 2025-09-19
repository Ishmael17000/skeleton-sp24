import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnionFind {
    private final int[] nodeList;
    private final int size;
    private int variety;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        nodeList = new int[N];
        size = N;
        variety = N;

        for (int i = 0; i < N; i ++) {
            nodeList[i] = -1;
        }
    }

    public int getVariety() {
        return variety;
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return - parent(find(v));
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return nodeList[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if (v1 == v2) {
            return true;
        }
        return (find(v1) == find(v2));

    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v >= size || v < 0) {
            throw new IllegalArgumentException("Input out of range.");
        }

        // Check whether itself is a root.
        if (parent(v) < 0) {
            return v;
        }

        // Iteratively search the root.
        int root = v;
        while (parent(root) >= 0) {
            setParent(root, parent(root));
            root = parent(root);
        }
        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie-break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 != v2) {
            int root1 = find(v1);
            int root2 = find(v2);
            if (root1 != root2) {
                if (sizeOf(root1) <= sizeOf(root2)) {
                    unionUpdate(root2, root1);
                }
                else {
                    unionUpdate(root1, root2);
                }
                variety -= 1;
            }
        }
    }

    // Do the union operation, and update the size. Assume both are roots.
    private void unionUpdate(int root, int child) {
        nodeList[root] += nodeList[child];
        setParent(child, root);
    }

    // Set the parent of a to b
    private void setParent(int a, int b) {
        nodeList[a] = b;
    }

}
