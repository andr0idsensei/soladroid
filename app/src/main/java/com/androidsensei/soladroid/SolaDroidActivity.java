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
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

/**
 * The main activity of the SolaDroid application. It manages the fragments with the screens that will be presented to
 * the users.
 * <p/>
 * TODO polish the UI interface
 * TODO add some error handling for Trello requests
 * TODO update navigation between activities if needed - clean interfaces
 *
 * @author mihai
 */
public class SolaDroidActivity extends ActionBarActivity implements SolaDroidFragmentContract {

    private static final String AUTH_FRAGMENT_TAG = "auth_fragment";
    private static final String SETUP_FRAGMENT_TAG = "setup_fragment";
    private static final String POMODORO_FRAGMENT_TAG = "pomodoro_fragment";
    private static final String ACCESS_DENIED_FRAGMENT_TAG = "access_denied_fragment";

    private boolean configurationChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            configurationChanged = true;
        }

        setContentView(R.layout.activity_sola_droid);

        String appKey = SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, this);
        boolean isSetup = SharedPrefsUtil.loadPreferenceBoolean(AppConstants.IS_APP_SETUP_KEY, this);
        if (isSetup) {
            showTaskStatusActivity();
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
        if (configurationChanged) {
            TrelloAuthFragment authFragment = (TrelloAuthFragment) getFragmentManager().findFragmentByTag(AUTH_FRAGMENT_TAG);
            replaceCurrentFragment(authFragment, AUTH_FRAGMENT_TAG);
        } else {
            replaceCurrentFragment(new TrelloAuthFragment(), AUTH_FRAGMENT_TAG);
        }
    }

    @Override
    public void showSetupFragment() {
        if (configurationChanged) {
            TrelloSetupFragment setupFragment = (TrelloSetupFragment) getFragmentManager().findFragmentByTag(SETUP_FRAGMENT_TAG);
            replaceCurrentFragment(setupFragment, SETUP_FRAGMENT_TAG);
        } else {
            replaceCurrentFragment(new TrelloSetupFragment(), SETUP_FRAGMENT_TAG);
        }
    }

    @Override
    public void showTaskStatusActivity() {
        startActivity(new Intent(this, TaskStatusActivity.class));
        finish();
    }

    @Override
    public void showAccessDeniedFragment() {
        if (configurationChanged) {
            TrelloAccessDeniedFragment accessDeniedFragment = (TrelloAccessDeniedFragment) getFragmentManager().findFragmentByTag(ACCESS_DENIED_FRAGMENT_TAG);
            replaceCurrentFragment(accessDeniedFragment, ACCESS_DENIED_FRAGMENT_TAG);
        } else {
            replaceCurrentFragment(new TrelloAccessDeniedFragment(), ACCESS_DENIED_FRAGMENT_TAG);
        }
    }

    /**
     * Re-usable replace fragment method.
     *
     * @param fragment the fragment which will replace the current one.
     */
    private void replaceCurrentFragment(SolaDroidBaseFragment fragment, String fragmentTag) {
        if (findViewById(R.id.fragment_container) != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, fragmentTag).commit();
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
