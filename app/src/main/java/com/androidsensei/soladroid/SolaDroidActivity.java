package com.androidsensei.soladroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.setup.ui.TrelloAuthFragment;

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
        Log.d("r1k0", "showSetupFragment...");
    }

    @Override
    public void showTimerFragment() {

    }

    @Override
    public void showAccessDeniedFragment() {
        Log.d("r1k0", "showAccessDeniedFragment...");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
