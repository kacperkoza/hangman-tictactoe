package cosapp.com.tictactoe.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cosapp.com.tictactoe.Game.TicTacToe;
import cosapp.com.tictactoe.Level;
import cosapp.com.tictactoe.Model.Statistic;
import cosapp.com.tictactoe.R;

public class TicTacToeFragment extends Fragment {

    @BindViews({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine})
    Button[] boardButtons;

    @BindView(R.id.info_text_view) TextView infoTextView;

    @BindView(R.id.reset_button) Button resetButton;

    @BindView(R.id.human_points_text_view) TextView humanPointsTextView;

    @BindView(R.id.draw_text_view) TextView drawsTextView;

    @BindView(R.id.computer_points_text_view) TextView computerPointsTextView;

    private static final int[] images = { R.drawable.tictactoebg, R.drawable.tictactoebg2,
            R.drawable.tictactoebg3, R.drawable.tictactoebg4 };

    private boolean humanAlwaysFirst;
    private Statistic statistic;
    private TicTacToe game;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreateView", "TicTacToeFragment");
        View view = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        MenuFragment.OnFragmentInteraction onFragmentInteraction = (MenuFragment.OnFragmentInteraction) getActivity();
        onFragmentInteraction.onFragmentChanged(getActivity().getString(R.string.tictactoe));

        statistic = new Statistic();
        game = new TicTacToe();

        updateResults();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.d("onAttach", "TicTacToeFragment");
        super.onAttach(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onResume() {
        Log.d("onResume", "TicTacToeFragment");
        super.onResume();
        game.setLevel(Level.values()[readLevelFromPreferences()]);
        humanAlwaysFirst = sharedPreferences.getBoolean("always_start", false);
        startNewGame();
    }

    private int readLevelFromPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return Integer.valueOf(sharedPreferences.getString("computer_level", "1"));
    }

    @OnClick(R.id.reset_button)
    public void restartGame() {
        startNewGame();
        infoTextView.setText("");
    }

    private void startNewGame() {
        game.clearBoard();

        for (int i = 0; i < boardButtons.length; i++) {
            boardButtons[i].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
            boardButtons[i].setText("");
            boardButtons[i].setEnabled(true);
            boardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        if (!humanAlwaysFirst) {
            if (!game.isHumanFirst()) {
                int move = 0;
                setMove(TicTacToe.ANDROID_PLAYER, move);
            }
        }
    }

    private void setMove(char player, int move) {
        game.setMove(player, move);

        boardButtons[move].setEnabled(false);

        if (player == TicTacToe.HUMAN_PLAYER) {
            boardButtons[move].setText(String.valueOf(player));
            boardButtons[move].setTextColor(Color.WHITE);
            boardButtons[move].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayClicked));
        } else {
            int drawableToPut = images[new Random().nextInt(4)];
            boardButtons[move].setBackgroundResource(drawableToPut);
        }
    }

    private void updateResults() {
        humanPointsTextView.setText(getResources().getString(R.string.wins) + " " + statistic.getWins());
        drawsTextView.setText(getResources().getString(R.string.draws) + " " + statistic.getDraws());
        computerPointsTextView.setText(getResources().getString(R.string.loses) + " " + statistic.getLoses());
    }

    private class ButtonClickListener implements View.OnClickListener {

        private int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View view) {
            if (game.evaluate() == -1) {
                if (boardButtons[location].isEnabled()) {
                    setMove(TicTacToe.HUMAN_PLAYER, location);

                    int winner = game.evaluate();

                    if (winner == -1) {
                        int computerMove = game.getComputerMove();
                        setMove(TicTacToe.ANDROID_PLAYER, computerMove);
                        winner = game.evaluate();
                    }

                    if (hasGameEnded(winner)) {
                        updateResults();
                    }
                }
            }
        }

        private boolean hasGameEnded(int winner) {
            switch (winner) {
                case -1: //game still in progress
                    return false;

                case 0:
                    statistic.incrementDraws();
                    infoTextView.setTextColor(Color.WHITE);
                    infoTextView.setText(R.string.draw);
                    break;

                case 10:
                    statistic.incrementWins();
                    infoTextView.setTextColor(Color.GREEN);
                    infoTextView.setText(R.string.you_have_won);

                    break;

                case -10:
                    statistic.incrementLoses();
                    infoTextView.setTextColor(Color.RED);
                    infoTextView.setText(R.string.you_have_lost);
                    break;
            }
            return true;
        }
    }
}
