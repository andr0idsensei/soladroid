package com.androidsensei.soladroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.setup.ui.TrelloAccessDeniedFragment;
import com.androidsensei.soladroid.setup.ui.TrelloAuthFragment;
import com.androidsensei.soladroid.setup.ui.TrelloSetupFragment;

public class SolaDroidActivity extends Activity implements SolaDroidFragmentContract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sola_droid);
        showAuthFragment();
    }

    @Override
    public void showAuthFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            TrelloAuthFragment authFragment = new TrelloAuthFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, authFragment).commit();
        }
    }

    @Override
    public void showSetupFragment() {
        //TODO create a generic method for replacing fragments
        if (findViewById(R.id.fragment_container) != null) {
            TrelloSetupFragment setupFragment = new TrelloSetupFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, setupFragment).commit();
        }
    }

    @Override
    public void showTimerFragment() {

    }

    @Override
    public void showAccessDeniedFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            TrelloAccessDeniedFragment accessDeniedFragment = new TrelloAccessDeniedFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, accessDeniedFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sola_droid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
