package com.androidsensei.soladroid.pomodoro.timer.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.timer.logic.PomodoroCounter;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * This fragment displays the Pomodoro timer and the current task we're working on.
 *
 * Created by mihai on 5/29/15.
 */
public class PomodoroFragment extends SolaDroidBaseFragment {
    private Card trelloCard;

    private int pomodoroCount;

    private PomodoroCounter pomodoroTimer;

    private TextView pomodoroCounter;

    private TextView pomodoroTimerView;

    private TextView pomodoroTotalTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trelloCard = (Card) getArguments().getSerializable(AppConstants.ARG_START_TASK_CARD);
        initTextViews();
        startCountdownTimer();
        initPomodoroRunningButtons();
    }

    /**
     * Initializes the views for the fragment.
     */
    private void initTextViews() {
        pomodoroCounter = (TextView) getView().findViewById(R.id.timer_pomodoro_counter);
        pomodoroTimerView = (TextView) getView().findViewById(R.id.timer_pomodoro_timer);
        pomodoroTotalTime = (TextView) getView().findViewById(R.id.timer_pomodoro_total);
    }

    //TODO change the pause button text
    //TODO set the stop button action or maybe do that in a separate method
    private void initPomodoroRunningButtons() {
        Button pause = (Button) getView().findViewById(R.id.timer_pomodoro_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pomodoroTimer.isPaused()) {
                    pomodoroTimer.start();
                } else {
                    pomodoroTimer.pause();
                }
            }
        });

    }

    /**
     * Creates and starts the Pomodoro countdown timer.
     */
    private void startCountdownTimer() {
        pomodoroTimer = new PomodoroCounter(0, new PomodoroCounter.PomodoroCounterCallback() {
            @Override
            public void onTick(long secondsToNone) {
                pomodoroTimerView.setText(DateUtils.formatElapsedTime(secondsToNone));
            }

            @Override
            public void onFinish() {

            }
        });

        pomodoroTimer.start();
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
