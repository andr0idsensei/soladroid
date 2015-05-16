package com.androidsensei.soladroid.setup.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.R;

/**
 * The settings screen activity. This will behave like a wizard which will allow the user to do the following actions:
 * <ul>
 *     <li>Setup her account with Trello and authorize SolaDroid<li/>
 *     <li>See a list of her Trello boards and pick one for keeping the Pomodoro tasks<li/>
 *     <li>Choose which of the board lists to use for to do, doing and done Pomodoro tasks</li>
 * <ul/>
 *
 * @author mihai
 */
public class SetupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
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
