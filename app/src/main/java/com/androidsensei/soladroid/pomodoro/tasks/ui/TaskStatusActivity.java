package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;

/**
 * This activity allows for sliding between the to do, doing and done Trello task lists in order for the users to
 * check out what is the status of their tasks.
 *
 * TODO set the correct label in the action bar
 *
 * Created by mihai on 5/29/15.
 */
public class TaskStatusActivity extends ActionBarActivity {

    /**
     * The number of pages corresponds to the number of Trello Task Lists that we want to switch between.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next steps.
     */
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_status);

        pager = (ViewPager) findViewById(R.id.tasks_status_view_pager);
        PagerAdapter pagerAdapter = new TaskListsPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
              invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_task_status, menu);

        menu.findItem(R.id.task_status_action_previous).setEnabled(pager.getCurrentItem() > 0);
        menu.findItem(R.id.task_status_action_next).setEnabled(pager.getCurrentItem() < 2);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //todo figure out where to navigate
                return true;

            case R.id.task_status_action_previous:
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                return true;

            case R.id.task_status_action_next:
                pager.setCurrentItem(pager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  A simple pager adapter that will load the fragment containing the Trello cards corresponding to the to do, doing
     * and done lists.
     *  Based on the position in the adapter we decide which of the lists to load.
     *  <ul>
     *      <li>0 will be the to do task list</li>
     *      <li>1 will be the doing task list</li>
     *      <li>2 will be the done task list</li>
     *  <ul/>
     */
    private class TaskListsPagerAdapter extends FragmentStatePagerAdapter {
        public TaskListsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TaskCardsFragment.create(SharedPrefsUtil.loadPreferenceString(AppConstants.TODO_LIST_KEY, TaskStatusActivity.this));

                case 1:
                    return TaskCardsFragment.create(SharedPrefsUtil.loadPreferenceString(AppConstants.DOING_LIST_KEY, TaskStatusActivity.this));

                case 2:
                    return TaskCardsFragment.create(SharedPrefsUtil.loadPreferenceString(AppConstants.DONE_LIST_KEY, TaskStatusActivity.this));

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
