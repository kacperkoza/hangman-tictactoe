package cosapp.com.tictactoe.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import cosapp.com.tictactoe.Model.Definition;
import cosapp.com.tictactoe.R;

/**
 * Created by kkoza on 22.03.2017.
 */

public class WordDefinitionDialog extends DialogFragment {


    private static final String DEFINITION_KEY = "definition";

    public static WordDefinitionDialog newInstance(Definition definition) {
        Bundle args = new Bundle();
        args.putSerializable(DEFINITION_KEY, definition);
        WordDefinitionDialog fragment = new WordDefinitionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Definition definition = (Definition) getArguments().getSerializable(DEFINITION_KEY);

        builder .setTitle(definition.getWord())
                .setMessage(definition.getOriginOfDefinition() + "\n\n" + definition.getText())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.show();
    }
}
