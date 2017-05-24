package cosapp.com.tictactoe.Game;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

import cosapp.com.tictactoe.Level;

/**
 * Created by kkoza on 25.02.2017.
 */

public class TicTacToe implements Serializable {

    private static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';

    public static final char ANDROID_PLAYER = 'O';

    public static final char EMPTY_SPACE = ' ';

    private char[] mBoard;

    private boolean humanFirst;

    private Level computerLevel;

    private Random mRandom;

    public TicTacToe() {
        mBoard = new char[BOARD_SIZE];
        mRandom = new Random();
        clearBoard();
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = EMPTY_SPACE;
        }
    }

    public boolean isHumanFirst() {
        if (humanFirst) {
            humanFirst = false;
            return true;
        } else {
            humanFirst = true;
            return false;
        }
    }

    public void setMove(char player, int location) {
        mBoard[location] = player;
    }

    public int getComputerMove() {
        switch (computerLevel) {
            case EASY:
                return getRandomMove(); //totally random move

            case MEDIUM:
                return getMediumMove(); //one move forward prediction

            case UNBEATABLE:
                return findBestMove(); //full prediction - unbeatable
        }
        return 0;
    }

    private int getRandomMove() {
        int move;
        do {
            move = mRandom.nextInt(BOARD_SIZE);
        } while (mBoard[move] != EMPTY_SPACE);

        setMove(ANDROID_PLAYER, move);

        return move;
    }

    private int getMediumMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY_SPACE) {

                char curr = mBoard[i];

                mBoard[i] = ANDROID_PLAYER;

                if (evaluate() == -10) {
                    setMove(ANDROID_PLAYER, i);
                    return i;
                } else {
                    mBoard[i] = curr;
                }
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
                if (mBoard[i] == EMPTY_SPACE) {

                char curr = mBoard[i];

                mBoard[i] = HUMAN_PLAYER;

                if (evaluate() == 10) {
                    setMove(ANDROID_PLAYER, i);
                    return i;
                } else {
                    mBoard[i] = curr;
                }
            }
        }

        return getRandomMove();
    }

    private boolean isWinner(char player) {

        //check horizontal win
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard[i] == player &&
                    mBoard[i+1] == player &&
                    mBoard[i+2] == player) {
                return true;
            }
        }

        //check vertical win
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == player &&
                    mBoard[i+3] == player &&
                    mBoard[i+6] == player) {
                return true;
            }
        }

        //check cross win
        if ((mBoard[0] == player && mBoard[4] == player && mBoard[8] == player)
                                        ||
                (mBoard[2] == player && mBoard[4] == player && mBoard[6] == player)) {
            return true;
        }

        return false;
    }

    private int findBestMove() {
        int bestVal = 1000;
        int bestMove = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY_SPACE) {
                // Make the move
                mBoard[i] = ANDROID_PLAYER;

                int moveVal = minimax(0, true);

                Log.d("for position: " + i, "score: " + moveVal);

                mBoard[i] = EMPTY_SPACE;

                if (moveVal < bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        Log.d("Value of best Move: ", bestVal + " ");

        return bestMove;
    }

    private int minimax(int depth, boolean humanTurn) {
        int winner = evaluate();

        //Human win
        if (winner == 10)
            return winner - depth;

        //Android win
        if (winner == -10)
            return winner + depth;

        //draw
        if (winner == 0)
            return 0;

        if (humanTurn) { //maximizer
            int moveScore = -100;

            for (int i = 0; i < BOARD_SIZE; i++) {
                if (mBoard[i] == EMPTY_SPACE) {
                    mBoard[i] = HUMAN_PLAYER;

                    moveScore = Math.max(moveScore, minimax(depth + 1, false));

                    mBoard[i] = EMPTY_SPACE;
                }
            }
            return moveScore;
        } else { //minimizer
            int moveSCore = 100;

            for (int i = 0; i < BOARD_SIZE; i++) {
                if (mBoard[i] == EMPTY_SPACE) {
                    mBoard[i] = ANDROID_PLAYER;

                    moveSCore = Math.min(moveSCore, minimax(depth + 1, true));

                    mBoard[i] = EMPTY_SPACE;
                }
            }
            return moveSCore;
        }
    }

    public int evaluate() {
        if (isWinner(HUMAN_PLAYER)) {
            return 10;
        }

        if (isWinner(ANDROID_PLAYER)) {
            return -10;
        }

        if (!isAnyEmptySpace()) {
            return 0;
        }
        return -1; //game still in progress
    }

    private boolean isAnyEmptySpace() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY_SPACE) {
                return true;
            }
        }
        return false;
    }

    public void setLevel(Level level) {
        this.computerLevel = level;
    }
}


