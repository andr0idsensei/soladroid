package com.androidsensei.soladroid.utils;

/**
 * Class to keep all application specific constants.
 *
 * Created by mihai on 5/24/15.
 */
public final class AppConstants {
    private AppConstants() {}

    /**
     * Key by which to refer the value for app setup. If this is set to true, it means that users went through the setup
     * process and we can just show the Pomodoro timer screen.
     */
    public static final String IS_APP_SETUP_KEY = "is_app_setup";

    /**
     * Key by which we refer the Trello id of the to do task list mapped in SolaDroid.
     */
    public static final String TODO_LIST_KEY = "todo_list";

    /**
     * Key by which we refer the Trello id of the doing task list mapped in SolaDroid.
     */
    public static final String DOING_LIST_KEY = "doing_list";

    /**
     * Key by which we refer the Trello id of the done task list mapped in SolaDroid.
     */
    public static final String DONE_LIST_KEY = "done_list";

    /**
     * Key by which we refer the Trello task card which we intent to start working on.
     */
    public static final String ARG_START_TASK_CARD = "start_task_card";

    /**
     * The length of one Pomodoro (25 minutes) in milliseconds.
     */
    public static final long POMODORO_LENGTH = 1500000;

    /**
     * The tick of the Pomodoro timer in milliseconds.
     */
    public static final long POMODORO_COUNTER_TICK = 1000;
}
