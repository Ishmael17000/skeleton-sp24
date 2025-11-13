package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;

    private final long SEED;
    private final int MINROOMSIZE;
    private final int MAXROOMSIZE;

    private TETile[][] tiles;
    private final Random RANDOM;


    /**
     * Fill the board with void blocks.
     * @param tiles The board.
     */
    private void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }


    /**
     * Initialize an empty world with random seed.
     * @param seed Random seed.
     * @param minSize The minimum size for room(wall excluded).
     * @param maxSize The maximum size for room(wall excluded).
     */
    public World(long seed, int minSize, int maxSize) {
        this.SEED = seed;
        this.RANDOM = new Random(seed);
        this.MINROOMSIZE = minSize;
        this.MAXROOMSIZE = maxSize;
        this.tiles = new TETile[WIDTH][HEIGHT];

        fillWithNothing(tiles);
    }




}
