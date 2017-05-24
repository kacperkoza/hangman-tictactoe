package cosapp.com.tictactoe.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cosapp.com.tictactoe.R;


public class MenuFragment extends Fragment {

    OnFragmentSelect onFragmentSelect;

    public interface OnFragmentSelect {
        void onFragmentSelected(Class c);
    }

    public interface OnFragmentInteraction {
        void onFragmentChanged(String title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);
        setHasOptionsMenu(true);

        try {
            onFragmentSelect = (OnFragmentSelect) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        MenuFragment.OnFragmentInteraction onFragmentInteraction = (MenuFragment.OnFragmentInteraction) getActivity();
        onFragmentInteraction.onFragmentChanged(getActivity().getString(R.string.choose_game));

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                onFragmentSelect.onFragmentSelected(PreferencesFragment.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tictactoe, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.tic_tac_toe_button)
    public void startTicTacToe() {
        onFragmentSelect.onFragmentSelected(TicTacToeFragment.class);
    }

    @OnClick(R.id.hang_man_button)
    public void startHangman() {
        onFragmentSelect.onFragmentSelected(HangmanFragment.class);
    }

}
