package com.androidsensei.soladroid.pomodoro.timer.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroFragmentStateManager;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroTimer;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * This fragment displays the Pomodoro timer and the current task we're working on.
 * TODO handle the state changes properly
 * TODO handle the done/back buttons
 * TODO persist the Trello Card data
 * TODO think about how to include both action sections...
 * <p/>
 * Created by mihai on 5/29/15.
 */
public class PomodoroFragment extends SolaDroidBaseFragment {
    /**
     * This fragment's countdownTime manager.
     */
    private PomodoroFragmentStateManager stateManager;

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
        Card trelloCard = (Card) getArguments().getSerializable(AppConstants.ARG_START_TASK_CARD);

        stateManager = PomodoroFragmentStateManager.getInstance();
        stateManager.setTrelloCard(trelloCard);
        if (savedInstanceState == null) {
            initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.POMODORO, false);
        } else {
            initCountdownTimer(stateManager.countdownTime(), true);
            setTimerButtonsState();
            if (stateManager.pomodoroFinished() || stateManager.breakFinished()) {
                toggleActionSections();
            }
        }

        initTextViews();
        setupPomodoroPauseButton();
        setupPomodoroStopButton();
        setupShortBreakButton();
        setupLongBreakButton();
    }

    /**
     * Initializes the views for the fragment.
     */
    private void initTextViews() {
        pomodoroCounter = (TextView) getView().findViewById(R.id.timer_pomodoro_counter);
        pomodoroTotalTime = (TextView) getView().findViewById(R.id.timer_pomodoro_total);

        pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
        pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
        setTimerView();
    }

    /**
     * Initializes the timer view.
     */
    private void setTimerView() {
        pomodoroTimerView = (TextView) getView().findViewById(R.id.timer_pomodoro_timer);
        pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(pomodoroTimer.getRemainingTime()));
    }

    /**
     * Toggles the finished and running sections of buttons based on their current countdownTime. They will become visible if
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
     * Preserve the state of the timer action buttons after a configuration change.
     */
    private void setTimerButtonsState() {
        final Button pause = (Button) getView().findViewById(R.id.timer_pomodoro_pause);
        final Button stop = (Button) getView().findViewById(R.id.timer_pomodoro_stop);
        if (pomodoroTimer.isPaused()) {
            pause.setText(getResources().getText(R.string.timer_pomodoro_resume));
            stop.setEnabled(false);
        } else if (pomodoroTimer.isStopped()) {
            stop.setText(getResources().getText(R.string.timer_pomodoro_start));
            pause.setEnabled(false);
        }
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
     * Sets up the short break button.
     */
    private void setupShortBreakButton() {
        final Button shortBreak = (Button) getView().findViewById(R.id.timer_pomodoro_five);
        final Button longBreak = (Button) getView().findViewById(R.id.timer_pomodoro_fifteen);
        shortBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.SHORT_BREAK, false);
                setTimerView();
                longBreak.setEnabled(false);
                shortBreak.setEnabled(false);
                pomodoroTimer.start();
            }
        });
    }

    /**
     * Sets up the long break button.
     */
    private void setupLongBreakButton() {
        final Button longBreak = (Button) getView().findViewById(R.id.timer_pomodoro_fifteen);
        final Button shortBreak = (Button) getView().findViewById(R.id.timer_pomodoro_five);
        longBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.LONG_BREAK, false);
                setTimerView();
                longBreak.setEnabled(false);
                shortBreak.setEnabled(false);
                pomodoroTimer.start();
            }
        });
    }

    /**
     * Creates and initializes the Pomodoro countdown timer.
     */
    private void initCountdownTimer(final PomodoroFragmentStateManager.CountdownTime state, boolean configChanged) {
        pomodoroTimer = stateManager.initTimer(configChanged, state, new PomodoroTimer.PomodoroCounterCallback() {
            @Override
            public void onTick(long secondsToNone) {
                if (isAdded()) {
                    pomodoroTimerView.setText(DateUtils.formatElapsedTime(secondsToNone));
                }
            }

            @Override
            public void onFinish(long elapsedTime) {
                stateManager.incrementTotalTime(elapsedTime);
                stateManager.incrementPomodoroCount();
                if (isAdded()) {
                    pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(0));
                    toggleActionSections();
                    //TODO restore section state...
                }
            }

            @Override
            public void onStop(long elapsedTime) {
                stateManager.incrementTotalTime(elapsedTime);
                if (isAdded()) {
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(state.value()));
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
