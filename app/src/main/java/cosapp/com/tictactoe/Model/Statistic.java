package cosapp.com.tictactoe.Model;

import java.io.Serializable;

/**
 * Created by kkoza on 17.03.2017.
 */

public class Statistic implements Serializable {
    private int wins;
    private int loses;
    private int draws;

    public Statistic() {
        this.wins = 0;
        this.loses = 0;
        this.draws = 0;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementDraws() {
        draws++;
    }

    public void incrementLoses() {
        loses++;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDraws() {
        return draws;
    }
}
