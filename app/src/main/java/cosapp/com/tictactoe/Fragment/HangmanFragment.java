package cosapp.com.tictactoe.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cosapp.com.tictactoe.Controller;
import cosapp.com.tictactoe.Model.Definition;
import cosapp.com.tictactoe.Dialog.EndGameDialog;
import cosapp.com.tictactoe.Dialog.WordDefinitionDialog;
import cosapp.com.tictactoe.Game.Hangman;
import cosapp.com.tictactoe.R;
import retrofit2.Call;
import retrofit2.Response;

public class HangmanFragment extends Fragment implements EndGameDialog.StartNewGameListener {
    private final static int[] hangmanPhotos = {
            R.drawable.wisielec1, R.drawable.wisielec2, R.drawable.wisielec3,
            R.drawable.wisielec4, R.drawable.wisielec5, R.drawable.wisielec6,
            R.drawable.wisielec7};

    @BindView(R.id.letters) GridView gridView;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.imageView) ImageView imageView;

    private ProgressDialog progressDialog;
    private Hangman hangman;
    private Definition definition;
    private Controller controller;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hang_man, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        MenuFragment.OnFragmentInteraction mCallback = (MenuFragment.OnFragmentInteraction) getActivity();
        mCallback.onFragmentChanged(getActivity().getString(R.string.title_activity_hangman));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Initialize components");
        progressDialog.setMessage("Downloading word");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int maxLength = sharedPreferences.getInt("max_word_length", 15);
        Log.d("MaxLength", maxLength + "");

        controller = new Controller(maxLength) {
            @Override
            public void onResponse(Call<List<Definition>> call, Response<List<Definition>> response) {
                if (response.isSuccessful()) {
                    definition = response.body().get(0);
                    startNewGame();
                } else {
                    progressDialog.hide();
                    Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_LONG)
                            .show();
                    Log.d("Response error", response.message() + ", " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Definition>> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG)
                        .show();
                t.printStackTrace();
            }
        };
        onNewGameDialogConfirmation();
        return view;
    }

    private void startNewGame() {
        hangman = new Hangman(definition.getWord());
        textView.setText(hangman.getCodedWord());
        imageView.setImageResource(R.drawable.wisielec1);
        LetterAdapter letterAdapter = new LetterAdapter(getActivity());
        gridView.setAdapter(letterAdapter);
        progressDialog.hide();
    }

    @Override
    public void onNewGameDialogConfirmation() {
        controller.getDefinition();
        progressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart_game:
                onNewGameDialogConfirmation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class LetterAdapter extends BaseAdapter {
        private final char[] LETTERS_ENG = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        private LayoutInflater letterInf;

        LetterAdapter(Context c) {
            this.letterInf = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
                return LETTERS_ENG.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button letterBtn;

            if (convertView == null) {
                letterBtn = (Button) letterInf.inflate(R.layout.letter, parent, false);
            } else {
                letterBtn = (Button) convertView;
            }

            letterBtn.setText(String.valueOf(LETTERS_ENG[position]));

            letterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;

                    //change button's background if present or not present. Update TextView
                    checkIfLetterIsPresent(button);

                    //disable used button
                    button.setEnabled(false);

                    //check if just won or lost
                    isWinOrLose();
                }

                private void checkIfLetterIsPresent(Button button){
                    char letter = button.getText().toString().charAt(0);

                    if (hangman.checkForLetter(letter)) {
                        button.setBackgroundResource(R.drawable.circle_clicked_pass);
                        textView.setText(hangman.getCodedWord());
                    } else {
                        button.setBackgroundResource(R.drawable.circle_clicked_fail);
                        imageView.setImageResource(hangmanPhotos[hangman.getFails()]);
                    }
                }

                private void isWinOrLose() {
                    FragmentManager fm = getActivity().getFragmentManager();

                    if (hangman.hasUserLost() || hangman.hasUserWon()) {
                        boolean wordDefinitionEnabled = sharedPreferences.getBoolean("enable_definitions", true);

                        if (wordDefinitionEnabled) {
                            WordDefinitionDialog definitionDialog = WordDefinitionDialog.newInstance(definition);
                            definitionDialog.show(fm, "definitionDialog");
                        } else {
                            EndGameDialog dialog = EndGameDialog.newInstance(hangman.getOriginalWord());
                            dialog.setTargetFragment(fm.findFragmentById(R.id.frame_layout), 0);
                            dialog.show(fm, "EndGameDialog");
                        }
                        onNewGameDialogConfirmation();
                    }

                }
            });
            return letterBtn;
        }
    }
}
