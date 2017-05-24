package cosapp.com.tictactoe.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cosapp.com.tictactoe.Fragment.HangmanFragment;
import cosapp.com.tictactoe.Fragment.MenuFragment;
import cosapp.com.tictactoe.Fragment.PreferencesFragment;
import cosapp.com.tictactoe.Fragment.TicTacToeFragment;
import cosapp.com.tictactoe.R;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentSelect,
        MenuFragment.OnFragmentInteraction{

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        onFragmentSelected(MenuFragment.class);
        getSupportActionBar().setTitle(getString(R.string.choose_game));
    }

    @Override
    public void onFragmentSelected(Class c) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (c == MenuFragment.class) {
            fragmentTransaction
                    .replace(R.id.frame_layout, new MenuFragment())
                    .commit();
            return;
        } else if (c == HangmanFragment.class) {
            fragmentTransaction
                    .replace(R.id.frame_layout, new HangmanFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (c == TicTacToeFragment.class) {
            fragmentTransaction
                    .replace(R.id.frame_layout, new TicTacToeFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (c == MenuFragment.class) {
            fragmentTransaction
                    .replace(R.id.frame_layout, new MenuFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (c == PreferencesFragment.class) {
            fragmentTransaction
                    .replace(R.id.frame_layout, new PreferencesFragment())
                    .addToBackStack(null)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        int fragmentInStack = fragmentManager.getBackStackEntryCount();

        if (fragmentInStack > 0){
            fragmentManager.popBackStack();
        }
        if (fragmentInStack == 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onFragmentChanged(String title) {
        getSupportActionBar().setTitle(title);
    }
}
