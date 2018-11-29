package fall2018.csc2017.games.Hangman;

import java.io.Serializable;
import java.util.Random;

import fall2018.csc2017.games.Game;

/**
 * Manages a Hangman object.
 */
class HangmanManager implements Game, Serializable {
    /**
     * The arrays holding the possible words
     */
    private final static String[] easyWords = {"CHARGER", "TABLET", "SYSTEM", "INTERNET", "STYLUS"};
    private final static String[] mediumWords = {"AGGRESSIVE", "FUTURISTIC", "RELIGION", "ANACONDA", "EINSTEIN"};
    private final static String[] hardWords = {"CROQUET", "BAGPIPES", "BANJO", "DWARVES", "GAZEBO"};
    /**
     * The current score.
     */
    private int score;
    /**
     * The Hangman being managed.
     */
    private Hangman hangman;
    /**
     * The number of undos; default set to 3.
     */
    private int numUndos = 3;
    /**
     * The difficulty of the word; default set to medium.
     */
    private String difficulty = "medium";

    /**
     * Creates a new manager for a specific word.
     */
    HangmanManager(int mode, String word) {
        hangman = new Hangman(word);
    }

    /**
     * Manage a new shuffled hangman game.
     *
     * @param difficulty the difficulty
     */
    HangmanManager(String difficulty) {
        hangman = new Hangman(getNewWord());
        this.difficulty = difficulty;
        setNumUndos(3);
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        hangman = new Hangman(getNewWord());
    }

    @Override
    public String getGameId() {
        return "hangman";
    }

    @Override
    public boolean highTopScore() {
        return false;
    }

    public int getNumUndos() {
        return numUndos;
    }

    public void setNumUndos(int numUndos) {
        this.numUndos = numUndos;
    }

    @Override
    public boolean undo() {
        return false;
    }

    /**
     * Returns the hangman for this manager
     *
     * @return the hangman for this game
     */
    Hangman getHangman() {
        return hangman;
    }

    /**
     * Generates a new word based on difficulty.
     *
     * @return a word for the user to guess
     */
    String getNewWord() {
        Random rand = new Random();
        String[] words;
        switch (difficulty) {
            case "easy":
                words = easyWords;

                break;
            case "hard":
                words = hardWords;
                break;
            default:
                words = mediumWords;
                break;
        }
        String newWord = words[rand.nextInt(words.length)];

        while (hangman != null && newWord.equals(hangman.currWord)) {
            newWord = words[rand.nextInt(words.length)];
        }

        return newWord;

    }

    public boolean redo() {
        return false;
    }

    /**
     * Returns if the puzzle was solved.
     *
     * @return if the game is solved
     */
    public boolean puzzleSolved() {
        return (new String(hangman.getRevealedWord())).equals(hangman.currWord);
    }

    /**
     * Returns if the game has been lost.
     *
     * @return if the game has been lost.
     */
    public boolean puzzleLost() {
        return hangman.getCurrentGuesses() == hangman.TOTAL_GUESSES;
    }

    /**
     * Returns if the guess was valid.
     *
     * @param guess, the users guess
     * @return if guess is a valid guess, single char from english alphabet
     */
    boolean isValid(String guess) {
        return guess.matches("[a-zA-Z]");
    }

    /**
     * Returns the current score for this hangman game
     *
     * @return the current score for this hangman game
     */
    int getScore() {
        int modifier;
        switch (getDifficulty()) {
            case "easy":
                modifier = 3;
                break;
            case "medium":
                modifier = 6;
                break;
            case "hard":
                modifier = 9;
                break;
            default:
                modifier = 3;
                break;
        }
        return modifier * (6 - hangman.getCurrentGuesses());
    }
}
