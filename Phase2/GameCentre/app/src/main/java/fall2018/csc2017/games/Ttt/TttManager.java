package fall2018.csc2017.games.Ttt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import fall2018.csc2017.games.Game;

/**
 * Manages a Tic Tac Toe game.
 */
public class TttManager implements Game, Serializable {
    /**
     * The number of undos; default set to 1.
     */
    private int numUndos = 1;
    /**
     * boolean to keep track of p1's turn
     */
    boolean p1Turn = true;
    /**
     * Hash-map points system of p1 and p2.
     */
    Map<String, Integer> points = new HashMap<String, Integer>() {
        {
            put("p1", 0);
            put("p2", 0);
        }
    };
    /**
     * The game board.
     */
    int[][] board = new int[3][3];
    /**
     * Tracks the number of rounds
     */
    int roundCount = 0;
    /**
     * The game mode.
     */
    String mode;
    /**
     * The undo tracker.
     */
    ArrayList<int[]> undoTracker = new ArrayList<>();


    /**
     * Creates a new manager
     *
     * @param mode the mode for this manager
     */
    TttManager(String mode) {
        this.mode = mode;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.mode = difficulty;
    }

    /**
     * Gets the game difficulty level, higher number means higher difficulty,
     * starts at 0
     *
     * @return the game's difficulty
     */
    @Override
    public String getDifficulty() {
        return mode;
    }

    /**
     * Returns this game's unique game ID
     *
     * @return the game's ID
     */
    @Override
    public String getGameId() {
        return "Ttt";
    }

    /**
     * Returns whether a high score is a good score or bad in this game
     *
     * @return whether a high score is a good score or bad in this game
     */
    @Override
    public boolean highTopScore() {
        return false;
        //wtf is this
        //why is this even needed
    }

    /**
     * Sets how many undos are possible in this game
     *
     * @param undos the number of undos
     */
    @Override
    public void setNumUndos(int undos) {
        numUndos = undos;
    }

    public int getNumUndos() {
        return numUndos;
    }

    /**
     * Performs an redo
     *
     * @return whether redo was successful
     */
    @Override
    public boolean undo() {
        Boolean success = false;
        Iterator<int[]> it = undoTracker.iterator();
        int time = getUndoTime();
        for (int i = time; i < 3; i++) {
            if (roundCount > 0 & it.hasNext() & getNumUndos() > 0) {
                int[] coords = it.next();
                play(coords[0], coords[1], 0);
                success = true;
                p1Turn = !p1Turn;
                roundCount--;
            }
        }
        numUndos = numUndos - 1;
        return success;
    }


    /**
     * Computer makes a move on the board
     */
    int[] computerPlay() {

        ArrayList<int[]> possibilities = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    int[] candidate = new int[2];
                    candidate[0] = i;
                    candidate[1] = j;
                    possibilities.add(candidate);
                }
            }
        }
        int[] chosen = possibilities.get(rand.nextInt(possibilities.size() - 1));
        play(chosen[0], chosen[1], 2);
        return chosen;
    }


    void play(int row, int col, int item) {
        board[row][col] = item;
    }

    /**
     * Checks the board for wins
     *
     * @return whether there is a win
     */
    boolean checkForWin() {

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == (board[i][1])
                    && board[i][0] == (board[i][2])
                    && board[i][0] != 0) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == (board[1][i])
                    && board[0][i] == (board[2][i])
                    && board[0][i] != 0) {
                return true;
            }
        }

        if (board[0][0] == (board[1][1])
                && board[0][0] == (board[2][2])
                && board[0][0] != 0) {
            return true;

        } else return board[0][2] == (board[1][1])
                && board[0][2] == (board[2][0])
                && board[0][2] != 0;

    }

    /**
     * Reset the board and buttons
     */
    void resetBoard() {
        board = new int[3][3];
        roundCount = 0;
        p1Turn = true;
        numUndos = 3;
    }

    /**
     * Returns how many times undo should occur (twice for single-player and once for multi)
     */
    int getUndoTime() {
        int time = 2;
        if (mode.equals("single")) {
            time = 1;
        }
        return time;
    }


    public int getScore() {
        return points.get("p1") - points.get("p2");
    }
}
