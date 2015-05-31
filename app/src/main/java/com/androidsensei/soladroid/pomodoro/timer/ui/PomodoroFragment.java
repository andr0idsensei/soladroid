package com.androidsensei.soladroid.pomodoro.timer.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
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

    private String timeSpent;

    private CountDownTimer pomodoroTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trelloCard = (Card) getArguments().getSerializable(AppConstants.ARG_START_TASK_CARD);

    }

    /**
     * 
     */
    private void startPomodoroCounter() {
        pomodoroTimer = new CountDownTimer(AppConstants.POMODORO_LENGTH, AppConstants.POMODORO_COUNTER_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

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
