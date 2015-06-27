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
import com.androidsensei.soladroid.trello.api.service.TrelloCallsService;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * This fragment displays the Pomodoro timer and the current task we're working on.
 * TODO handle the done/back buttons
 * TODO move started tasks to the "in progress" list
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

        initTheTimer(savedInstanceState);
        initTextViews();
        initButtons();
        restoreViewState(savedInstanceState);
    }

    /**
     * Initializes the countdown timer when the fragment is started/restarted.
     *
     * @param savedInstanceState the saved instance state bundle.
     */
    private void initTheTimer(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.POMODORO, false);
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
        pomodoroCounter = (TextView) getView().findViewById(R.id.timer_pomodoro_counter);
        pomodoroTotalTime = (TextView) getView().findViewById(R.id.timer_pomodoro_total);

        pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
        pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
        setTimerView();
    }

    /**
     * Initializes the action buttons for countdown management of Pomodoros and breaks.
     */
    private void initButtons() {
        pause = (Button) getView().findViewById(R.id.timer_pomodoro_pause);
        stop = (Button) getView().findViewById(R.id.timer_pomodoro_stop);
        shortBreak = (Button) getView().findViewById(R.id.timer_pomodoro_five);
        longBreak = (Button) getView().findViewById(R.id.timer_pomodoro_fifteen);

        setupPomodoroPauseButton();
        setupPomodoroStopButton();
        setupShortBreakButton();
        setupLongBreakButton();
        setupDoneButton();
    }

    /**
     * Initializes the timer view.
     */
    private void setTimerView() {
        pomodoroTimerView = (TextView) getView().findViewById(R.id.timer_pomodoro_timer);
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
                    initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.POMODORO, false);
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
        if (!stateManager.isTaskInProgress()) {
            TrelloCallsService.moveCardToList(getActivity(), stateManager.trelloCard().getId(),
                    SharedPrefsUtil.loadPreferenceString(AppConstants.DOING_LIST_KEY, getActivity()));
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
        final Button shortBreak = (Button) getView().findViewById(R.id.timer_pomodoro_five);
        shortBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.SHORT_BREAK, false);
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
        final Button longBreak = (Button) getView().findViewById(R.id.timer_pomodoro_fifteen);
        longBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCountdownTimer(PomodoroFragmentStateManager.CountdownTime.LONG_BREAK, false);
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
        Button done = (Button) getView().findViewById(R.id.timer_pomodoro_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.stop();
                String timeComment = "Pomodoros: " + stateManager.pomodoroCount() + " - " + DateUtils.formatElapsedTime(
                        stateManager.totalTime());
                TrelloCallsService.saveTimeComment(getActivity(), timeComment, stateManager.trelloCard().getId());
                TrelloCallsService.moveCardToList(getActivity(), stateManager.trelloCard().getId(),
                        SharedPrefsUtil.loadPreferenceString(AppConstants.DONE_LIST_KEY, getActivity()));
                getActivity().onBackPressed(); // TODO use an intent here to tell the task status activity to reload with the updates
            }
        });
    }

    /**
     * Creates and initializes the Pomodoro countdown timer.
     */
    private void initCountdownTimer(final PomodoroFragmentStateManager.CountdownTime initialCountdown, boolean configChanged) {
        pomodoroTimer = stateManager.initTimer(configChanged, initialCountdown, new PomodoroTimer.PomodoroCounterCallback() {
            @Override
            public void onTick(long secondsToNone) {
                if (isAdded()) {
                    pomodoroTimerView.setText(DateUtils.formatElapsedTime(secondsToNone));
                }
            }

            @Override
            public void onFinish(long elapsedTime) {
                if (stateManager.pomodoroFinished()) {
                    stateManager.incrementTotalTime(elapsedTime);
                    stateManager.incrementPomodoroCount();
                    if (isAdded()) {
                        pomodoroCounter.setText(getString(R.string.timer_pomodoro_counter, "" + stateManager.pomodoroCount()));
                        pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                    }
                } else if (stateManager.breakFinished()) {
                    if (isAdded()) {
                        pause.setEnabled(true);
                    }
                }
                if (isAdded()) {
                    toggleBreakButtons(true);
                    setupStopButton();
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(0));
                }
            }

            @Override
            public void onStop(long elapsedTime) {
                stateManager.incrementTotalTime(elapsedTime);
                if (isAdded()) {
                    pomodoroTotalTime.setText(getString(R.string.timer_pomodoro_total, DateUtils.formatElapsedTime(stateManager.totalTime())));
                    pomodoroTimerView.setText("" + DateUtils.formatElapsedTime(initialCountdown.value()));
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
