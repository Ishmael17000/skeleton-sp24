package core;


import tileengine.TETile;

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

    }




    private void divideRegionHelper(Node node) {

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
