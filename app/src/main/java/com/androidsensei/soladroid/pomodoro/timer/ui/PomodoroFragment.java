package com.androidsensei.soladroid.pomodoro.timer.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroTimer;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * This fragment displays the Pomodoro timer and the current task we're working on.
 * TODO treat configuration changes when the timer is paused or stopped
 * TODO save the time state for the task in Trello - maybe it would be a good idea to treat the timer as a singleton in
 * order to avoid keeping the same state in the fragment.
 * TODO handle the breaks
 *
 * Created by mihai on 5/29/15.
 */
public class PomodoroFragment extends SolaDroidBaseFragment {
    /**
     * Bundle key to refer the Pomodoro count.
     */
    private static final String POMODORO_COUNT_KEY = "pomodoro_count";

    /**
     * Bundle key to refer the remaining time.
     */
    private static final String REMAINING_TIME_KEY = "remaining_time";

    /**
     * Bundle key to refer the total time.
     */
    private static final String TOTAL_TIME_KEY = "total_time";

    /**
     * The Trello card object which contains the current task to work on.
     */
    private Card trelloCard;

    /**
     * The number of Pomodoros spent on the current task.
     */
    private int pomodoroCount;

    /**
     * The total time spent on the current task.
     */
    private long totalTime;

    /**
     * The remaining time for the current task.
     */
    private Long remainingTime;

    /**
     * The Pomodoro count down timer used to determine the current state of the Pomodoro for the task at hand.
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
     * The button section for running the Pomodoro.
     */
    private View runningSection;

    /**
     * The button section for when a Pomodoro is finished.
     */
    private View finishedSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("r1k0", "onActivityCreated...");
        restoreInstanceState(savedInstanceState);
        trelloCard = (Card) getArguments().getSerializable(AppConstants.ARG_START_TASK_CARD);
        initCountdownTimer(PomodoroTimer.TimerType.POMODORO);
        initTextViews();
        setupPomodoroPauseButton();
        setupPomodoroStopButton();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POMODORO_COUNT_KEY, pomodoroCount);
        outState.putLong(REMAINING_TIME_KEY, remainingTime);
        outState.putLong(TOTAL_TIME_KEY, totalTime);
        outState.putSerializable(AppConstants.ARG_START_TASK_CARD, trelloCard);
    }

    /**
     * Reads the saved data from the savedInstanceState Bundle to restore the views at their previous state.
     *
     * @param savedInstanceState the bundle object containing state that needs to be preserved
     */
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            pomodoroCount = savedInstanceState.getInt(POMODORO_COUNT_KEY);
            remainingTime = savedInstanceState.getLong(REMAINING_TIME_KEY);
            totalTime = savedInstanceState.getLong(TOTAL_TIME_KEY);
            trelloCard = (Card) savedInstanceState.getSerializable(AppConstants.ARG_START_TASK_CARD);
        }
    }

    /**
     * Initializes the views for the fragment.
     */
    private void initTextViews() {
        pomodoroCounter = (TextView) getView().findViewById(R.id.timer_pomodoro_counter);
        pomodoroTotalTime = (TextView) getView().findViewById(R.id.timer_pomodoro_total);

        pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + pomodoroCount));
        pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(totalTime)));
        setTimerView();
    }

    /**
     * Initializes the timer view.
     */
    private void setTimerView() {
        pomodoroTimerView = (TextView) getView().findViewById(R.id.timer_pomodoro_timer);
        pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(pomodoroTimer.getTimerLength()));
    }

    /**
     * Toggles the finished and running sections of buttons based on their current state. They will become visible if
     * they are invisible and viceversa.
     */
    private void toggleActionSections() {
        runningSection = getView().findViewById(R.id.timer_pomodoro_running_section);
        finishedSection = getView().findViewById(R.id.timer_pomodoro_finished_section);

        boolean isRunningVisbile = runningSection.getVisibility() == View.VISIBLE;
        boolean isFinishedVisible = finishedSection.getVisibility() == View.VISIBLE;

        runningSection.setVisibility(isRunningVisbile ? View.INVISIBLE : View.VISIBLE);
        finishedSection.setVisibility(isFinishedVisible ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Setup the pause/resume button.
     */
    private void setupPomodoroPauseButton() {
        final Button pause = (Button) getView().findViewById(R.id.timer_pomodoro_pause);
        final Button stop = (Button) getView().findViewById(R.id.timer_pomodoro_stop);
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
     * Setup the start/stop button.
     */
    private void setupPomodoroStopButton() {
        final Button stop = (Button) getView().findViewById(R.id.timer_pomodoro_stop);
        final Button pause = (Button) getView().findViewById(R.id.timer_pomodoro_pause);

        if (pomodoroTimer.isInitialized()) {
            stop.setText(getResources().getText(R.string.timer_pomodoro_start));
            pause.setEnabled(false);
        }
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pomodoroTimer.isStopped() || pomodoroTimer.isInitialized()) {
                    pomodoroTimer.start();
                    stop.setText(getResources().getText(R.string.timer_pomodoro_stop));
                    pause.setEnabled(true);
                } else {
                    pomodoroTimer.stop();
                    stop.setText(getResources().getText(R.string.timer_pomodoro_start));
                    pause.setEnabled(false);
                }
            }
        });
    }

    /**
     * Creates and initializes the Pomodoro countdown timer.
     */
    private void initCountdownTimer(PomodoroTimer.TimerType timerType) {
        Log.d("r1k0", "remainingTime: " + remainingTime);
        pomodoroTimer = new PomodoroTimer(remainingTime, timerType, new PomodoroTimer.PomodoroCounterCallback() {
            @Override
            public void onTick(long secondsToNone) {
                remainingTime = pomodoroTimer.getRemainingTime();
                if (isAdded()) {
                    pomodoroTimerView.setText(DateUtils.formatElapsedTime(secondsToNone));
                }
            }

            @Override
            public void onFinish() {
                totalTime = totalTime + pomodoroTimer.getElapsedTime();
                remainingTime = null;
                pomodoroCount++;
                if (isAdded()) {
                    pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + pomodoroCount));
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(totalTime)));
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(0));
                    toggleActionSections();
                }
            }

            @Override
            public void onStop(long elapsedTime) {
                totalTime = totalTime + pomodoroTimer.getElapsedTime();
                remainingTime = null;
                if (isAdded()) {
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(totalTime)));
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(pomodoroTimer.getTimerLength()));
                }
            }
        });
    }

    /**
     * Factory method for creating pomodoro fragment instances.
     *
     * @param card the Trello card we're currently working on - passed in the Fragment arguments
     * @return the PomodoroFragment instance created with arguments set
     */
    public static PomodoroFragment createFragment(Card card) {
        PomodoroFragment fragment = new PomodoroFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_START_TASK_CARD, card);
        fragment.setArguments(args);
        return fragment;
    }

    public PomodoroFragment() {
    }
}
