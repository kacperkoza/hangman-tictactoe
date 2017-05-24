package cosapp.com.tictactoe.Game;

/**
 * Created by kkoza on 14.03.2017.
 */

public class Hangman {
    private final int MAX_FAILS = 6;
    private final char UNDERLINE_SYMBOL = '_';
    private final char SPACE = ' ';

    private int fails;

    private String codedWord;
    private String originalWord;

    public Hangman(String originalWord) {
        this.originalWord = originalWord.toUpperCase();
        this.fails = 0;
        this.codedWord = "";
        codeWord();
    }

    private void codeWord() {
        for (int i = 0; i < originalWord.length(); i++) {
            if (originalWord.charAt(i) == SPACE) {
                codedWord += SPACE;
            } else {
                codedWord += UNDERLINE_SYMBOL;
            }
        }
    }

    public boolean checkForLetter(char letter) {
        if (!originalWord.contains(String.valueOf(letter))) {
            fails++;
            return false;
        }

        String temp = codedWord;
        codedWord = "";
        boolean letterPresent = false;

        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == UNDERLINE_SYMBOL) {
                if (originalWord.charAt(i) == letter) {
                    codedWord += letter;
                    letterPresent = true;
                } else {
                    codedWord += UNDERLINE_SYMBOL;
                }
            } else if (temp.charAt(i) == SPACE) {
                codedWord += SPACE;
            } else if (temp.charAt(i) != UNDERLINE_SYMBOL) {
                codedWord += originalWord.charAt((i));
            }
        }

        return letterPresent;
    }

    public boolean hasUserWon() {
        return !codedWord.contains(String.valueOf(UNDERLINE_SYMBOL));
    }

    public boolean hasUserLost() {
        return fails == MAX_FAILS;
    }

    public String getCodedWord() {
        return codedWord;
    }

    public int getFails() {
        return fails;
    }

    public String getOriginalWord() {
        return originalWord;
    }

}
