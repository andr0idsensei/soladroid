package com.androidsensei.soladroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.pomodoro.tasks.ui.TaskStatusActivity;
import com.androidsensei.soladroid.setup.ui.TrelloAccessDeniedFragment;
import com.androidsensei.soladroid.setup.ui.TrelloAuthFragment;
import com.androidsensei.soladroid.setup.ui.TrelloSetupFragment;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

/**
 * The main activity of the SolaDroid application. It manages the fragments with the screens that will be presented to
 * the users.
 *
 * TODO polish the UI interface
 * TODO add some error handling for Trello requests
 * TODO update navigation between activities if needed - clean interfaces
 *
 * @author mihai
 */
public class SolaDroidActivity extends ActionBarActivity implements SolaDroidFragmentContract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sola_droid);

        String appKey = SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, this);
        boolean isSetup = SharedPrefsUtil.loadPreferenceBoolean(AppConstants.IS_APP_SETUP_KEY, this);
        if (isSetup) {
            Card card = (Card) getIntent().getSerializableExtra(AppConstants.START_TASK_CARD_KEY);
            if (card != null) {
                showTimerFragment();
            } else {
                showTaskStatusActivity();
            }
        } else {
            if ("".equals(appKey)) {
                showAuthFragment();
            } else {
                showSetupFragment();
            }
        }
    }

    @Override
    public void showAuthFragment() {
        replaceCurrentFragment(new TrelloAuthFragment());
    }

    @Override
    public void showSetupFragment() {
        replaceCurrentFragment(new TrelloSetupFragment());
    }

    @Override
    public void showTimerFragment() {

    }

    @Override
    public void showTaskStatusActivity() {
        startActivity(new Intent(this, TaskStatusActivity.class));
        finish();
    }

    @Override
    public void showAccessDeniedFragment() {
        replaceCurrentFragment(new TrelloAccessDeniedFragment());
    }

    /**
     * Re-usable replace fragment method.
     *
     * @param fragment the fragment which will replace the current one.
     */
    private void replaceCurrentFragment(SolaDroidBaseFragment fragment) {
        if (findViewById(R.id.fragment_container) != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
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
