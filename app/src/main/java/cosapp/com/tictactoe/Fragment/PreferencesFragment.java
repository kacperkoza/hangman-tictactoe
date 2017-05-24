package cosapp.com.tictactoe.Fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import cosapp.com.tictactoe.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        MenuFragment.OnFragmentInteraction onFragmentInteraction = (MenuFragment.OnFragmentInteraction) getActivity();
        onFragmentInteraction.onFragmentChanged(getActivity().getString(R.string.settings));

        setHasOptionsMenu(true);
    }
}
