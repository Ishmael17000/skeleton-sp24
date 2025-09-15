import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {
    // TODO: Add any necessary instance variables.
    public final int N; // The size of the board.
    public boolean[][] openStatus; // Use a matrix of booleans to store the status.
    public int numOfOpenSites;
    private final WeightedQuickUnionUF trees; // The sorting model.
    private final WeightedQuickUnionUF treesWithoutBottom;

    // The top and bottom sites are virtual, and doesn't engage with neighbours interaction.
    private final int top;
    private final int bottom;

    public Percolation(int size) {
        if (size <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        N = size;
        openStatus = new boolean[N][N];
        numOfOpenSites = 0;
        trees = new WeightedQuickUnionUF(N * N + 2); // N^2 sites plus virtual top and bottom.
        treesWithoutBottom = new WeightedQuickUnionUF(N * N + 1);
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
        if (isOpen(row, col)) {
            return;
        }
        openStatus[row][col] = true;

        // Update the union tree structure.
        connectNeighbours(row, col, trees);
        connectNeighbours(row, col, treesWithoutBottom);
        // Special case when dealing virtual top and bottom.
        if (row == 0) {
            connectToTop(row, col, trees);
            connectToTop(row, col, treesWithoutBottom);
        }
        if (row == N-1) {
            connectToBottom(row, col, trees);
        }

        numOfOpenSites += 1;
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
        int thisOrdinal = gridToLine(row, col);
        return treesWithoutBottom.connected(thisOrdinal, top);

    }

    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    public boolean percolates() {
        return trees.connected(top, bottom);
    }



    /** Convert grid coordinates to line index. */
    private int gridToLine(int x, int y) {
        return x * N + y;
    }

    /** Return the neighbours (at most 4) of coordinate (row, column) in grid coordinate. */
    private int[][] findNeighbours(int row, int column) {
        int[][] returnList = new int[4][];
        int[] range = {-1, 1};
        int index = 0;
        // Loop through the four neighbours and add the available ones.
        int[][] neighbourCoordinates = {{row+1, column}, {row-1,column}, {row, column+1}, {row, column-1}};
        for (int[] coordinate: neighbourCoordinates) {
            if (isAvailableCoordinate(coordinate)) {
                    returnList[index] = coordinate;
                    index += 1;
                }
            }
        return returnList;
    }

    /** Check whether x is an available index between 0 and N-1 */
    private boolean isAvailable(int x) {
        return x < N && x >= 0;
    }

    /** Check whether (x, y) is an available coordinate */
    private boolean isAvailableCoordinate(int[] coordinate) {
        return isAvailable(coordinate[0]) && isAvailable(coordinate[1]);
    }

    /** Merge site (row, col) with its open neighbours. */
    private void connectNeighbours(int row, int col, WeightedQuickUnionUF someTrees) {
        int[] thisCoordinate = new int[] {row, col};
        int[][] neighbours = findNeighbours(row, col);

        // Check and merge open neighbours.
        for (int[] neighbour: neighbours) {
            if (neighbour != null) {
                if (isOpen(neighbour[0], neighbour[1])) {
                    connectSites(thisCoordinate, neighbour, someTrees);
                }
            }
        }
    }

    /** Connect two sites.
     *  Assume they are open.
     */
    private void connectSites(int[] site1, int[] site2, WeightedQuickUnionUF someTrees) {
        int index1 = gridToLine(site1[0], site1[1]);
        int index2 = gridToLine(site2[0], site2[1]);
        someTrees.union(index1, index2);
    }

    /** Connect site with top */
    private void connectToTop(int row,int column, WeightedQuickUnionUF someTrees) {
        int index = gridToLine(row, column);
        someTrees.union(index, top);
    }

    /** Connect site with bottom */
    private void connectToBottom(int row,int column, WeightedQuickUnionUF someTrees) {
        int index = gridToLine(row, column);
        someTrees.union(index, bottom);
    }



}
