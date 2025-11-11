package tetris;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.TERenderer;
import tileengine.Tileset;

import java.util.*;

/**
 *  Provides the logic for Tetris.
 *
 *  @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
 */

public class Tetris {

    private static int WIDTH = 10;
    private static int HEIGHT = 20;

    // Tetrominoes spawn above the area we display, so we'll have our Tetris board have a
    // greater height than what is displayed.
    private static int GAME_HEIGHT = 25;

    // Contains the tiles for the board.
    private TETile[][] board;

    // Helps handle movement of pieces.
    private Movement movement;

    // Checks for if the game is over.
    private boolean isGameOver;

    // The current Tetromino that can be controlled by the player.
    private Tetromino currentTetromino;

    // The current game's score.
    private int score;

    /**
     * Checks for if the game is over based on the isGameOver parameter.
     * @return boolean representing whether the game is over or not
     */
    private boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Renders the game board and score to the screen.
     */
    private void renderBoard() {
        ter.drawTiles(board);
        renderScore();
        StdDraw.show();

        if (auxFilled) {
            auxToBoard();
        } else {
            fillBoard(Tileset.NOTHING);
        }
    }

    /**
     * Creates a new Tetromino and updates the instance variable
     * accordingly. Flags the game to end if the top of the board
     * is filled and the new piece cannot be spawned.
     */
    private void spawnPiece() {
        // The game ends if this tile is filled
        if (board[4][19] != Tileset.NOTHING) {
            isGameOver = true;
        }

        // Otherwise, spawn a new piece and set its position to the spawn point
        currentTetromino = Tetromino.values()[bagRandom.getValue()];
        currentTetromino.reset();
    }

    /**
     * Updates the board based on the user input. Makes the appropriate moves
     * depending on the user's input.
     * Interactivity logic:
     *      a: move the current piece towards the left by one tile
     *      s: move the current piece downwards by one tile
     *      d: move the current piece towards the right by one tile
     *      q: rotate the current piece to the left 90 degrees
     *      w: rotate the current piece to the right 90 degrees
     */
    private void updateBoard() {
        // Grabs the current piece.
        Tetromino t = currentTetromino;
        if (actionDeltaTime() > 500) {
            movement.dropDown();
            resetActionTimer();
            Tetromino.draw(t, board, t.pos.x, t.pos.y);
            return;
        }

        if (StdDraw.hasNextKeyTyped()) {
            char userInput = StdDraw.nextKeyTyped();
            switch (userInput) {
                case 'a': movement.tryMove(-1, 0);
                    break;
                case 's': movement.dropDown();
                    break;
                case 'd': movement.tryMove(1, 0);
                    break;
                case 'q': movement.rotateLeft();
                    break;
                case 'w': movement.rotateRight();
                default : break;
            }
            Tetromino.draw(t, board, t.pos.x, t.pos.y);
        }
    }

    /**
     * Increments the score based on the number of lines that are cleared. Rule:
     * 1: 100 points
     * 2: 300 points
     * 3: 500 points
     * 4: 800 points
     * @param linesCleared The number of lines cleared at this move.
     */
    private void incrementScore(int linesCleared) {
        int points = switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        score += points;
    }

    /**
     * Clears lines/rows on the provided tiles/board that are horizontally filled.
     * Repeats this process for cascading effects and updates score accordingly.
     * @param tiles
     */
    public void clearLines(TETile[][] tiles) {
        // Keeps track of the current number lines cleared
        int linesCleared = 0;

        // Check how many lines have been completed and clear it the rows if completed.
        // Start from the bottom line, move above until there's a line that is not clear.
        while (isLineFilled(tiles, 0)) {
            // When the y-th line is filled.
            linesCleared += 1;
            removeBottom(tiles);
            aboveMoveDown(tiles);
            removeLine(tiles, HEIGHT - 1); // Remove the top line.
        }

        // Increment the score based on the number of lines cleared.
        incrementScore(linesCleared);

        fillAux();
    }

    /**
     * Where the game logic takes place. The game should continue as long as the game isn't
     * over.
     */
    public void runGame() {
        resetActionTimer();

        while (!isGameOver()) {
            // This loop starts from creating a piece and ends with the piece being placed.
            spawnPiece();
            renderBoard();
            while (currentTetromino != null) {
                if (StdDraw.hasNextKeyTyped() || actionDeltaTime() >= 500) {
                    updateBoard();
                    renderBoard();
                }
            }
            // When current piece is null.
            clearLines(board);
            renderBoard();
        }
    }

    /**
     * Renders the score using the StdDraw library.
     */
    private void renderScore() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(7, 19, Integer.toString(score));
    }

    /**
     * Use this method to run Tetris.
     * @param args
     */
    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
        Tetris tetris = new Tetris(seed);
        tetris.runGame();
    }

    /**
     * Everything below here you don't need to touch.
     */

    // This is our tile rendering engine.
    private final TERenderer ter = new TERenderer();

    // Used for randomizing which pieces are spawned.
    private Random random;
    private BagRandomizer bagRandom;

    private long prevActionTimestamp;
    private long prevFrameTimestamp;

    // The auxiliary board. At each time step, as the piece moves down, the board
    // is cleared and redrawn, so we keep an auxiliary board to track what has been
    // placed so far to help render the current game board as it updates.
    private TETile[][] auxiliary;
    private boolean auxFilled;

    public Tetris() {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(new Random().nextLong());
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    public Tetris(long seed) {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);

        ter.initialize(WIDTH, HEIGHT);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    // Setter and getter methods.

    /**
     * Returns the current game board.
     * @return
     */
    public TETile[][] getBoard() {
        return board;
    }

    /**
     * Returns the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current auxiliary board.
     * @return
     */
    public TETile[][] getAuxiliary() {
        return auxiliary;
    }


    /**
     * Returns the current Tetromino/piece.
     * @return
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * Sets the current Tetromino to null.
     * @return
     */
    public void setCurrentTetromino() {
        currentTetromino = null;
    }

    /**
     * Sets the boolean auxFilled to true;
     */
    public void setAuxTrue() {
        auxFilled = true;
    }

    /**
     * Fills the entire board with the specific tile that is passed in.
     * @param tile
     */
    private void fillBoard(TETile tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = tile;
            }
        }
    }

    /**
     * Copies the contents of the src array into the dest array using
     * System.arraycopy.
     * @param src
     * @param dest
     */
    private static void copyArray(TETile[][] src, TETile[][] dest) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
        }
    }

    /**
     * Copies over the tiles from the game board to the auxiliary board.
     */
    public void fillAux() {
        copyArray(board, auxiliary);
    }

    /**
     * Copies over the tiles from the auxiliary board to the game board.
     */
    private void auxToBoard() {
        copyArray(auxiliary, board);
    }

    /**
     * Calculates the delta time with the previous action.
     * @return the amount of time between the previous Tetromino movement with the present
     */
    private long actionDeltaTime() {
        return System.currentTimeMillis() - prevActionTimestamp;
    }

    /**
     * Resets the action timestamp to the current time in milliseconds.
     */
    private void resetActionTimer() {
        prevActionTimestamp = System.currentTimeMillis();
    }



    /**
     * Return whether the y-th line of tiles is filled.
     */
    private boolean isLineFilled(TETile[][] tiles, int y) {
        // Iterate through the y-th line.
        for (int x = 0; x < WIDTH; x++) {
            TETile currentTile = tiles[x][y];
            // If there's any vacancy, return false.
            if (currentTile == Tileset.NOTHING) {
                return false;
            }
        }
        return true;
    }


    /**
     * Remove the y-th line of the board.
     */
    private void removeLine(TETile[][] tiles, int y) {
        for (int x = 0; x < WIDTH; x++) {
            tiles[x][y] = Tileset.NOTHING;
        }
    }

    /**
     * Remove the bottom line of the board.
     */
    private void removeBottom(TETile[][] tiles) {
        removeLine(tiles, 0);
    }


    /**
     * After the bottom is removed, all the tiles above are dropped down.
     */
    private void aboveMoveDown(TETile[][] tiles) {
        for (int y = 1; y < HEIGHT; y++) {
            moveDown(tiles, y);
        }
    }

    /**
     * Helper method for aboveMoveDown. Move the y-th line down by one tile.
     * Assume y >= 1.
     */
    private void moveDown(TETile[][] tiles, int y) {
        for (int x = 0; x < WIDTH; x++) {
            tiles[x][y-1] = tiles[x][y];
        }
    }

}
