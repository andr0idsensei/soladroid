package com.androidsensei.soladroid.pomodoro.timer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.tasks.ui.TaskStatusActivity;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroActivityStateManager;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroTimer;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.trello.api.service.TrelloCallsService;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.NetworkUtil;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.RetrofitErrorBroadcastReceiver;

/**
 * This fragment displays the Pomodoro timer and the current task we're working on.
 * TODO - nice to have - offline mode with progress sync - right now if Trello server errors occur, progress gets lost
 * <p/>
 * Created by mihai on 5/29/15.
 */
public class PomodoroActivity extends ActionBarActivity {
    /**
     * This fragment's countdownTime manager.
     */
    private PomodoroActivityStateManager stateManager;

    /**
     * The Pomodoro count down timer used to determine the current countdownTime of the Pomodoro for the task at hand.
     */
    private PomodoroTimer pomodoroTimer;

    /**
     * Text view to display the Pomodoro counter.
     */
    private TextView pomodoroCounter;

    /**
     * Text view to display the timer view.
     */
    private TextView pomodoroTimerView;

    /**
     * Text view to display the total time.
     */
    private TextView pomodoroTotalTime;

    /**
     * The pause/resume button. This should be active only when a Pomodoro runs.
     */
    private Button pause;

    /**
     * The stop/start button. This should be active only when a Pomodoro runs.
     */
    private Button stop;

    /**
     * The short break button. This is active only when a Pomodoro is stopped or finished and if a long break is not already running.
     */
    private Button shortBreak;

    /**
     * The long break button. This is active only when a Pomodoro is stopped or finished and if a short break is not already running.
     */
    private Button longBreak;

    /**
     * The Retrofit error receiver.
     */
    private BroadcastReceiver retrofitErrorReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pomodoro);
        Card trelloCard = (Card) getIntent().getExtras().getSerializable(AppConstants.ARG_START_TASK_CARD);

        stateManager = PomodoroActivityStateManager.getInstance();
        stateManager.setTrelloCard(trelloCard);

        getSupportActionBar().setTitle(stateManager.trelloCard().getName());

        initTheTimer(savedInstanceState);
        initTextViews();
        initButtons();
        restoreViewState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (retrofitErrorReceiver == null) {
            retrofitErrorReceiver = new RetrofitErrorBroadcastReceiver(getFragmentManager());
        }

        registerReceiver(retrofitErrorReceiver, new IntentFilter(TrelloCallsService.ACTION_RETROFIT_ERROR_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (retrofitErrorReceiver != null) {
            unregisterReceiver(retrofitErrorReceiver);
            retrofitErrorReceiver = null;
        }
    }

    /**
     * Initializes the countdown timer when the fragment is started/restarted.
     *
     * @param savedInstanceState the saved instance state bundle.
     */
    private void initTheTimer(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initCountdownTimer(PomodoroActivityStateManager.CountdownTime.POMODORO, false);
        } else {
            initCountdownTimer(stateManager.countdownTime(), true);
        }
    }

    /**
     * Restores the state of the views after a configuration change.
     *
     * @param savedInstanceState the saved instance state
     */
    private void restoreViewState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreTimerButtonsState();
            restoreBreakButtonsState();
        }
    }

    /**
     * Initializes the views for the fragment.
     */
    private void initTextViews() {
        pomodoroCounter = (TextView) findViewById(R.id.timer_pomodoro_counter);
        pomodoroTotalTime = (TextView) findViewById(R.id.timer_pomodoro_total);

        pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
        pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
        setTimerView();
    }

    /**
     * Initializes the action buttons for countdown management of Pomodoros and breaks.
     */
    private void initButtons() {
        pause = (Button) findViewById(R.id.timer_pomodoro_pause);
        stop = (Button) findViewById(R.id.timer_pomodoro_stop);
        shortBreak = (Button) findViewById(R.id.timer_pomodoro_five);
        longBreak = (Button) findViewById(R.id.timer_pomodoro_fifteen);

        setupPomodoroPauseButton();
        setupPomodoroStopButton();
        setupShortBreakButton();
        setupLongBreakButton();
        setupDoneButton();
        setupBackButton();
    }

    /**
     * Initializes the timer view.
     */
    private void setTimerView() {
        pomodoroTimerView = (TextView) findViewById(R.id.timer_pomodoro_timer);
        pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(pomodoroTimer.getRemainingTime()));
    }

    /**
     * Preserve the state of the timer action buttons after a configuration change.
     */
    private void restoreTimerButtonsState() {
        if (pomodoroTimer.isPaused()) {
            pause.setText(getText(R.string.timer_pomodoro_resume));
            stop.setText(getString(R.string.timer_pomodoro_stop));
            stop.setEnabled(false);
        } else if (pomodoroTimer.isStopped() || pomodoroTimer.isFinished()) {
            pause.setText(getText(R.string.timer_pomodoro_pause));
            stop.setText(getText(R.string.timer_pomodoro_start));
            pause.setEnabled(false);
        } else if (pomodoroTimer.isRunning()) {
            stop.setText(getText(R.string.timer_pomodoro_stop));
        }
    }

    /**
     * Restore the state of the break buttons after a configuration change.
     */
    private void restoreBreakButtonsState() {
        if (pomodoroTimer.isFinished() || pomodoroTimer.isInitialized() || pomodoroTimer.isStopped()) {
            toggleBreakButtons(true);
        } else {
            toggleBreakButtons(false);
        }
    }

    /**
     * Setup the pause/resume button.
     */
    private void setupPomodoroPauseButton() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pomodoroTimer.isPaused()) {
                    pomodoroTimer.start();
                    pause.setText(getResources().getText(R.string.timer_pomodoro_pause));
                    stop.setEnabled(true);
                } else {
                    pomodoroTimer.pause();
                    pause.setText(getResources().getText(R.string.timer_pomodoro_resume));
                    stop.setEnabled(false);
                }
            }
        });
    }

    /**
     * Sets up the stop/start button.
     */
    private void setupStopButton() {
        if (pomodoroTimer.isFinished()) {
            pause.setEnabled(false);
            stop.setEnabled(true);
            stop.setText(getText(R.string.timer_pomodoro_start));
        }
        if (pomodoroTimer.isInitialized()) {
            stop.setText(getResources().getText(R.string.timer_pomodoro_start));
            pause.setEnabled(false);
        }
    }

    /**
     * Setup the start/stop button.
     */
    private void setupPomodoroStopButton() {
        setupStopButton();

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pomodoroTimer.isFinished()) {
                    initCountdownTimer(PomodoroActivityStateManager.CountdownTime.POMODORO, false);
                    pomodoroTimer.start();
                    stop.setText(getResources().getText(R.string.timer_pomodoro_stop));
                    toggleBreakButtons(false);
                } else if (pomodoroTimer.isStopped() || pomodoroTimer.isInitialized()) {
                    pomodoroTimer.start();
                    stop.setText(getResources().getText(R.string.timer_pomodoro_stop));
                    pause.setEnabled(true);
                    toggleBreakButtons(false);
                    setCardInProgress();
                } else {
                    pomodoroTimer.stop();
                    stop.setText(getResources().getText(R.string.timer_pomodoro_start));
                    pause.setEnabled(false);
                    toggleBreakButtons(true);
                }
            }
        });
    }

    /**
     * Sets the current task in progress, once we start the pomodoro counter.
     */
    private void setCardInProgress() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            if (!stateManager.isTaskInProgress()) {
                String todoListId = SharedPrefsUtil.loadPreferenceString(AppConstants.TODO_LIST_KEY, this);
                String doingListId = SharedPrefsUtil.loadPreferenceString(AppConstants.DOING_LIST_KEY, this);
                TrelloCallsService.moveCardToList(this, stateManager.trelloCard().getId(), doingListId);
                TrelloResultsManager.getInstance().moveCardToList(todoListId, doingListId, stateManager.trelloCard());
            }
        } else {
            NetworkUtil.showNetworkExceptionDialog(getFragmentManager());
        }
    }

    /**
     * Enables or disables the break buttons based on the given argument.
     *
     * @param enabled true or false
     */
    private void toggleBreakButtons(boolean enabled) {
        shortBreak.setEnabled(enabled);
        longBreak.setEnabled(enabled);
    }

    /**
     * Sets up the short break button.
     */
    private void setupShortBreakButton() {
        final Button shortBreak = (Button) findViewById(R.id.timer_pomodoro_five);
        shortBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroActivityStateManager.CountdownTime.SHORT_BREAK, false);
                setTimerView();
                toggleBreakButtons(false);
                stop.setEnabled(false);
                pomodoroTimer.start();
            }
        });
    }

    /**
     * Sets up the long break button.
     */
    private void setupLongBreakButton() {
        final Button longBreak = (Button) findViewById(R.id.timer_pomodoro_fifteen);
        longBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroActivityStateManager.CountdownTime.LONG_BREAK, false);
                setTimerView();
                toggleBreakButtons(false);
                stop.setEnabled(false);
                pomodoroTimer.start();
            }
        });
    }

    /**
     * Sets up the done button.
     */
    private void setupDoneButton() {
        Button done = (Button) findViewById(R.id.timer_pomodoro_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.stop();
                String timeComment = "Pomodoros: " + stateManager.pomodoroCount() + " - " + DateUtils.formatElapsedTime(
                        stateManager.totalTime());
                if (NetworkUtil.isNetworkAvailable(PomodoroActivity.this)) {
                    String doneList = SharedPrefsUtil.loadPreferenceString(AppConstants.DONE_LIST_KEY, PomodoroActivity.this);
                    TrelloCallsService.saveTimeComment(PomodoroActivity.this, timeComment, stateManager.trelloCard().getId());
                    TrelloCallsService.moveCardToList(PomodoroActivity.this, stateManager.trelloCard().getId(), doneList);
                    startTaskStatusActivity();
                } else {
                    NetworkUtil.showNetworkExceptionDialog(getFragmentManager());
                }
            }
        });
    }

    /**
     * Sets up the back button.
     */
    private void setupBackButton() {
        Button back = (Button) findViewById(R.id.timer_pomodoro_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.stop();
                if (NetworkUtil.isNetworkAvailable(PomodoroActivity.this)) {
                    String todoList = SharedPrefsUtil.loadPreferenceString(AppConstants.TODO_LIST_KEY, PomodoroActivity.this);
                    TrelloCallsService.moveCardToList(PomodoroActivity.this, stateManager.trelloCard().getId(), todoList);
                    startTaskStatusActivity();
                } else {
                    NetworkUtil.showNetworkExceptionDialog(getFragmentManager());
                }
            }
        });
    }

    /**
     * Starts the task status activity, clearing the activity stack.
     */
    private void startTaskStatusActivity() {
        Intent taskActivity = TaskStatusActivity.getActivityIntent(PomodoroActivity.this);
        taskActivity.setAction(TaskStatusActivity.ACTION_REFRESH_DATA);
        taskActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(taskActivity);
    }
    /**
     * Creates and initializes the Pomodoro countdown timer.
     */
    private void initCountdownTimer(final PomodoroActivityStateManager.CountdownTime initialCountdown, boolean configChanged) {
        pomodoroTimer = stateManager.initTimer(configChanged, initialCountdown, new PomodoroTimer.PomodoroCounterCallback() {
            @Override
            public void onTick(long secondsToNone) {
                pomodoroTimerView.setText(DateUtils.formatElapsedTime(secondsToNone));
            }

            @Override
            public void onFinish(long elapsedTime) {
                if (stateManager.pomodoroFinished()) {
                    stateManager.incrementTotalTime(elapsedTime);
                    stateManager.incrementPomodoroCount();
                    pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                } else if (stateManager.breakFinished()) {
                    pause.setEnabled(true);
                }
                toggleBreakButtons(true);
                setupStopButton();
                pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(0));
            }

            @Override
            public void onStop(long elapsedTime) {
                stateManager.incrementTotalTime(elapsedTime);
                pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(initialCountdown.value()));
            }
        });
    }

    /**
     * Get the intent with which this activity can be started.
     *
     * @param context the context form which the activity is started
     * @param card    the Trello card id
     * @return the intent to start this activity
     */
    public static Intent getPomodoroActivityIntent(Context context, Card card) {
        Intent pomodoroActivityIntent = new Intent(context, PomodoroActivity.class);
        pomodoroActivityIntent.putExtra(AppConstants.ARG_START_TASK_CARD, card);

        return pomodoroActivityIntent;
    }

}
