import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {
    // TODO: Add any necessary instance variables.
    public final int N; // The size of the board.
    public boolean[][] openStatus; // Use a matrix of booleans to store the status.
    private final WeightedQuickUnionUF trees; // The sorting model.

    // Virtual top and bottom.
    private final int top;
    private final int bottom;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        this.openStatus = new boolean[N][N];
        this.trees = new WeightedQuickUnionUF(N * N + 2); // N^2 sites plus virtual top and bottom.
        top = N * N;
        bottom = N * N + 1;
    }

    /** Open this site. Merge to its neighbours. */
    public void open(int row, int col) {
        // Check the range.
        if (! isAvailable(row) || ! isAvailable(col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        // Change the state in storage.
        openStatus[row][col] = true;

        // Update the union tree structure.
        int thisOrdinal = gridToLine(row, col);



    }

    /** Return whether a site is open (has been opened). */
    public boolean isOpen(int row, int col) {
        // Check the range.
        if (! isAvailable(row) || ! isAvailable(col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return openStatus[row][col];
    }

    /** Return whether a site is connected to another open site at the top. */
    public boolean isFull(int row, int col) {
        if (row == 0) {
            return true;
        }
        int thisOrdinal = gridToLine(row, col);
        // Naive approach by checking the bottom sitewise. This takes linear time.
//        int thisOrdinal = gridToLine(row, col);
//        for (int i = 0; i < N; i ++) {
//            int siteOrdinal = gridToLine(N-1, i);
//            if (trees.connected(siteOrdinal, thisOrdinal)) {
//                return true;
//            }
//        }
        return trees.connected(thisOrdinal, top);

    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return 0;
    }

    public boolean percolates() {
        return trees.connected(top, bottom);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

    // The following two methods convert between grid coordinates and ordinal numbers.
    private int gridToLine(int x, int y) {
        return x * N + y;
    }

    private int[] lineToGrid(int i) {
        int x = i / (N - 1) - 1;
        int y = i - N * x;
        return new int[] {x, y};
    }

    // Return the neighbours (at most 4) of coordinate (row, column) in line coordinate.
    private int[][] findNeighbours(int row, int column) {
        int[] returnList = new int[4];
        int[] range = {-1, 1};
        int index = 0;
        // Loop through the four neighbours and add the available ones.
        for (int i: range) {
            for (int j: range) {
                if (isAvailable(i+row) && isAvailable(j+column)) {
                    returnList[index] = gridToLine(i+row, j+column);
                    index += 1;
                }
            }
        }
        // Check the top and bottom cases.
        if (row == 0) {
            returnList[index] = top;
        }
        if (row == N - 1) {
            returnList[index] = bottom;
        }
        return returnList;
    }

    // Check whether x is an available index between 0 and N-1
    private boolean isAvailable(int x) {
        return x < N && x >= 0;
    }

    // Connect two open sites.
    private void connectSites(int x, int y) {
        int[][] neighbours = findNeighbours(x);
    }




}
