package com.androidsensei.soladroid.setup.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.tasks.ui.TaskStatusActivity;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

/**
 * The main activity of the SolaDroid application. It manages the fragments with the screens that will be presented to
 * the users.
 * <p/>
 * TODO polish the UI interface
 *
 * @author mihai
 */
public class TrelloSetupActivity extends ActionBarActivity implements SolaDroidFragmentContract {

    private static final String AUTH_FRAGMENT_TAG = "auth_fragment";
    private static final String SETUP_FRAGMENT_TAG = "setup_fragment";
    private static final String ACCESS_DENIED_FRAGMENT_TAG = "access_denied_fragment";

    private boolean configurationChanged;

    /**
     * This is the current fragment.
     */
    private Fragment currentFragment;

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
            currentFragment = getFragmentManager().findFragmentByTag(AUTH_FRAGMENT_TAG);
        } else {
            currentFragment = new TrelloAuthFragment();
        }

        replaceCurrentFragment(AUTH_FRAGMENT_TAG);
    }

    @Override
    public void showSetupFragment() {
        if (configurationChanged) {
            currentFragment = getFragmentManager().findFragmentByTag(SETUP_FRAGMENT_TAG);
        } else {
            currentFragment = new TrelloSetupFragment();
        }

        replaceCurrentFragment(SETUP_FRAGMENT_TAG);
    }

    @Override
    public void showTaskStatusActivity() {
        startActivity(new Intent(this, TaskStatusActivity.class));
        finish();
    }

    @Override
    public void showAccessDeniedFragment() {
        if (configurationChanged) {
            currentFragment = getFragmentManager().findFragmentByTag(ACCESS_DENIED_FRAGMENT_TAG);
        } else {
            currentFragment = new TrelloAccessDeniedFragment();
        }

        replaceCurrentFragment(ACCESS_DENIED_FRAGMENT_TAG);
    }

    /**
     * Re-usable replace fragment method.
     *
     * @param fragmentTag the tag by which this fragment is added.
     */
    private void replaceCurrentFragment(String fragmentTag) {
        if (findViewById(R.id.fragment_container) != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, currentFragment, fragmentTag).commit();
        }
    }

    @Override
    public void onBackPressed() {
        boolean canGoBack = false;
        if (currentFragment instanceof TrelloAuthFragment) {
            canGoBack = ((TrelloAuthFragment) currentFragment).goBack();
        }
        if (!canGoBack) {
            super.onBackPressed();
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
