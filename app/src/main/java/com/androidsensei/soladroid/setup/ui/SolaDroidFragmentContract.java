package com.androidsensei.soladroid.setup.ui;

/**
 * This interface provides the contract for navigating between fragments in the TrelloSetupActivity.
 * <p/>
 * Created by mihai on 5/22/15.
 */
public interface SolaDroidFragmentContract {
    /**
     * Show the fragment for Trello authorization.
     */
    void showAuthFragment();

    /**
     * Show the fragment for setting up the Trello to do, doing and done lists.
     */
    void showSetupFragment();

    /**
     * Show the fragment for choosing the Trello tasks to work on. - todo this is going to be an activity
     */
    void showTaskStatusActivity();

    /**
     * In case the user decides to deny Trello access for SolaDroid, we should notify her.
     */
    void showAccessDeniedFragment();
}
