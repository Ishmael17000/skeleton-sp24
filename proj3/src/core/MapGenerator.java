package core;


import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.HashMap;
import java.util.Random;

/**
 * A class used for generating roguelike rooms and corridors in a 2D-tile based world.
 * A room is basically a rectangular, and the corridor is a 1-tile height path.
 * Use BSP algorithm (see proj3/BSP Algorithm.txt
 * or <a href="https://roguebasin.com/index.php/Basic_BSP_Dungeon_generation">...</a>   for explanation).
 * The basic idea is to recursively divide the board into sub-regions (forming a binary tree structure) at random
 * and create room of random size and position in each region.
 * After creating these non-intersecting rooms, recursively connect rooms from sister regions by corridors.
 */
public class MapGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private final long SEED;
    private final int MINROOMSIZE;
    private final int MAXROOMSIZE;

    // The pseudo-random generator.
    private final Random RANDOM;


    private TETile[][] tiles; // The board of our world.
    private final Node root; // The tree structure.



    public MapGenerator(long seed, int minSize, int maxSize) {
        this.SEED = seed;
        this.MAXROOMSIZE = maxSize;
        this.MINROOMSIZE = minSize;
        this.RANDOM =  new Random(seed);

        this.root = new Node(new int[][]{{0, 0}, {WIDTH - 1, HEIGHT - 1}}); // The whole board.
    }


    /**
     * The main result.
     * Generate and fill in tiles with rooms and corridors at random.
     */
    public void generateMap() {
        divideRegion();
        generateRooms();
        connectRegions();
    }


    /**
     * Recursively divide the board into distinct regions.
     * Each region has size limit (MINROOMSIZE+2) times (MAXROOMSIZE+2).
     * Update the tree structure accordingly.
     */
    private void divideRegion() {
        divideRegionHelper(root);
    }



    // Divide one region into two sub-regions.
    // Update the tree structure accordingly.
    private void divideRegionHelper(Node node) {
        // Randomly choose a direction.
        int dir = RandomUtils.uniform(RANDOM, 2);


        /*
         Randomly choose a splitting point.
         Make sure both parts has enough space for generating rooms.
         Suppose node is {{a,b},{c,d}}. Split vertically.
         Take the interval [a,c]. Let [a,x] and (x,c] be two parts.
         x should satisfy x-a+1 >= min and c-x >= min.
         min+a-1 <= x <= c-min.
         */
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];


        // Split horizontally.
        if (dir == 0) {
            int x = switchUniform(MINROOMSIZE+b-1, d-MINROOMSIZE+1);
            horizontalSplit(x, node);
        }
        // Split vertically.
        else if (dir == 1) {
            int x = switchUniform(MINROOMSIZE+a-1, c-MINROOMSIZE+1);
            verticalSplit(x, node);
        }




        if (canSplit(node.left)) { divideRegionHelper(node.left); }
        if (canSplit(node.right)) { divideRegionHelper(node.right); }
    }

    // Split the region horizontally at point x (x belongs to below).
    private void horizontalSplit(int x, Node node) {
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];
        node.left = new Node(new int[][]{{a, b}, {c, x}});
        node.right = new Node(new int[][]{{a, x+1}, {c, d}});
    }

    // Split the region vertically at point x (x belongs to left).
    private void verticalSplit(int x, Node node) {
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];
        node.left = new Node(new int[][]{{a, b}, {x, d}});
        node.right = new Node(new int[][]{{x+1, b}, {c, d}});
    }


    // Return whether the region has enough size to split.
    private boolean canSplit(Node node) {
        int[][] region = node.region;
        int width = region[1][0] - region[0][0] + 1;
        int height = region[1][1] - region[0][1] + 1;
        return (width >= 2 * (MINROOMSIZE + 2)) && (height >= 2 * (MINROOMSIZE + 2));
    }





    /**
     * Generate rooms of random size and position in each divided region.
     * Each room has walls surrounding (not include corner tile).
     * Update the tiles and nodes accordingly.
     */
    private void generateRooms() {
        generateRoomsHelper(root);
    }

    // Generate a room at random in the node's region.
    // Update the tiles and 'room' variable in the node accordingly.
    private void generateRoomsHelper(Node node) {
        // Choose a size at random.
        int width = switchUniform(MINROOMSIZE, MAXROOMSIZE + 1);
        int height = switchUniform(MINROOMSIZE, MAXROOMSIZE + 1);

        // Choose the bottom-left corner.
        // Suppose node is {{a,b},{c,d}}. The corner is {x,y}.
        // Then x+width-1 <= c, and y+height-1 <= d.
        // x <= c-width+1, y <= d-height+1.
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];
        int x = switchUniform(a, c-width+2), y=switchUniform(b, d-height+2);
        node.room = new int[][]{{x, y}, {x+width, y+height}};

        // Update the tiles.
        putRoom(node);
        putWall(node);

        if (node.left != null) { generateRoomsHelper(node.left); }
        if (node.right != null) { generateRoomsHelper(node.right); }
    }


    /**
     * Recursively connect the sister regions by corridors of one-tile width.
     * Might use tree post traversal.
     */
    private void connectRegions() {

    }


    private void connectRegionsHelper(Node node1, Node node2) {
        //
    }


    /**
     * Return a random integer uniformly distributed in [m,n).
     * @param m Start (Inclusive).
     * @param n End (not inclusive).
     * @return A uniform sample.
     */
    private int switchUniform(int m, int n) {
        return RandomUtils.uniform(RANDOM, n-m) + m;
    }


    /**
     * Fill the tiles of the room with floor.
     */
    private void putRoom(Node node) {
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];
        for (int x = a; x <= c; x++) {
            for (int y = b; y <= d; y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
    }


    /**
     * Fill the tiles around the room with wall.
     */
    private void putWall(Node node) {
        int a = node.region[0][0], b = node.region[0][1], c = node.region[1][0], d = node.region[1][1];
        for (int y = b; y <= d; y++) {
            tiles[a-1][y] = Tileset.WALL;
            tiles[c+1][y] = Tileset.WALL;
        }
        for (int x = a; x <= c; x++) {
            tiles[x][b-1] = Tileset.WALL;
            tiles[x][d+1] = Tileset.WALL;
        }
    }






    /**
     * The helper class used for representing a tree node of region when dividing the board.
     * Support some tree traversal methods.
     */
    private class Node {
        private final int[][] region;
        private int[][] room;
        private Node left;
        private Node right;


        /**
         * Create a node with region.
         * @param corners Store the bottom-left and top-right corners of the node.
         */
        private Node(int[][] corners) {
            region = corners;
        }



    }
}
