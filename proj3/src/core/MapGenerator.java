package core;


import tileengine.TETile;
import utils.RandomUtils;

import java.util.HashMap;
import java.util.Random;

/**
 * A class used for generating roguelike rooms and corridors in a world.
 * Use BSP algorithm(see proj3/BSP Algorithm.txt
 * and <a href="https://roguebasin.com/index.php/Basic_BSP_Dungeon_generation">...</a>).
 * The basic idea is to recursively divide the board into sub-regions (forming a tree structure) at random
 * and create room of random size and position in each region.
 * After creating these non-intersecting rooms, recursively connect rooms of sister regions by corridors.
 */
public class MapGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private final long SEED;
    private final int MINROOMSIZE;
    private final int MAXROOMSIZE;

    private final Random RANDOM;


    private TETile[][] tiles;


    private Node root;



    public MapGenerator(long seed, int minSize, int maxSize) {
        this.SEED = seed;
        this.MAXROOMSIZE = maxSize;
        this.MINROOMSIZE = minSize;
        this.RANDOM =  new Random(seed);

        this.root = new Node(new int[][]{{0, 0}, {WIDTH - 1, HEIGHT - 1}});
    }


    /**
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
        int dir = RandomUtils.uniform(RANDOM, 1);


        /*
         Randomly choose a splitting point.
         Make sure both parts has enough space for generating rooms.
         Suppose node is {{a,b},{c,d}}. Split vertically.
         Take the interval [a,c]. Let [a,x] and (x,c] be two parts.
         x should satisfy x-a+1 >= min and c-x >= min.
         min+a-1 <= x <= c-min.
         */
        int a = node.region[0][0];
        int b = node.region[0][1];
        int c = node.region[1][0];
        int d = node.region[1][1];


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


    private void horizontalSplit(int x, Node node) {
        int a = node.region[0][0];
        int b = node.region[0][1];
        int c = node.region[1][0];
        int d = node.region[1][1];
        node.left = new Node(new int[][]{{a, b}, {c, x}});
        node.right = new Node(new int[][]{{a, x+1}, {c, d}});
    }

    private void verticalSplit(int x, Node node) {
        int a = node.region[0][0];
        int b = node.region[0][1];
        int c = node.region[1][0];
        int d = node.region[1][1];
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
     * Revise the tiles accordingly.
     */
    private void generateRooms() {

    }


    /**
     * Recursively connect the sister regions by corridors of one-tile width.
     * Might use tree post traversal.
     */
    private void connectRegions() {

    }


    private void connectRegionsHelper(Node node1, Node node2) {

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
     * The helper class used for representing a tree node of region when dividing the board.
     * Support some tree traversal methods.
     */
    private class Node {
        private int[][] region;
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
