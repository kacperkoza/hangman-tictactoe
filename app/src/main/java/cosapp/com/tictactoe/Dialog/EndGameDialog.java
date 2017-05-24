package cosapp.com.tictactoe.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import cosapp.com.tictactoe.R;

/**
 * Created by kkoza on 22.03.2017.
 */

public class EndGameDialog extends DialogFragment {

    private final static String CORRECT_ANSWER_KEY = "correct_answer";

    public interface StartNewGameListener {
        void onNewGameDialogConfirmation();
    }

    public static EndGameDialog newInstance(@Nullable String correctAnswer) {
        EndGameDialog endGameDialog = new EndGameDialog();

        Bundle args = new Bundle();
        args.putString(CORRECT_ANSWER_KEY, correctAnswer);
        endGameDialog.setArguments(args);

        return endGameDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        final Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder .setTitle(R.string.game_end)
                .setMessage(getMessage())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.show();
    }

    private String getMessage() {
        Resources r = getResources();

        return r.getString(R.string.you_have_lost) + "\n" +
                    r.getString(R.string.correct_answer_was) + " " + getCorrectAnswer() + ".";
    }

    private String getCorrectAnswer() {
        return getArguments().getString(CORRECT_ANSWER_KEY);
    }
}
