package com.androidsensei.soladroid.pomodoro.timer.logic;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Wrapper over Android's CountdownTimer which we use to manage the Pomodoro related operations.
 *
 * Created by mihai on 6/1/15.
 */
public class PomodoroCounter {
    /**
     * Conversion between from milliseconds to seconds.
     */
    private static final long MILLI_TO_SECOND_RATE = 1000;

    /**
     * The length of one Pomodoro (25 minutes) in milliseconds.
     */
    public static final long POMODORO_LENGTH = 1500000;

    /**
     * The length of the short break (5 minutes) in milliseconds.
     */
    public static final long SHORT_BREAK = 300000;

    /**
     * The length of the long break (15 minutes) in milliseconds.
     */
    public static final long LONG_BREAK = 900000;

    /**
     * The tick of the Pomodoro timer in milliseconds.
     */
    private static final long POMODORO_COUNTER_TICK = 1000;

    /**
     * The number of seconds until the current Pomodoro ends.
     */
    private long currentTimer;

    private boolean isPaused;

    private CountDownTimer countDownTimer;

    private PomodoroCounterCallback callback;

    public PomodoroCounter(long currentTimer, PomodoroCounterCallback callback) {
        if (currentTimer == 0) {
            this.currentTimer = POMODORO_LENGTH;
        } else {
            this.currentTimer = currentTimer;
        }
        this.callback = callback;
        this.countDownTimer = createCountDownTimer();
    }

    private CountDownTimer createCountDownTimer() {
        return new CountDownTimer(currentTimer * MILLI_TO_SECOND_RATE, POMODORO_COUNTER_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                callback.onTick(millisUntilFinished / MILLI_TO_SECOND_RATE);
                currentTimer = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Log.d("r1k0", "countdown finished.");
                callback.onFinish();
                currentTimer = 0;
            }
        };
    }

    public void start() {
        if (isPaused) {
            countDownTimer = createCountDownTimer().start();
        } else {
            countDownTimer.start();
        }
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
        countDownTimer.cancel();
    }

    public void stop() {
        isPaused = false;
        countDownTimer.cancel();
    }

    /**
     * Callback interface for hooking up on the countdown timer's events.
     */
    public interface PomodoroCounterCallback {
        /**
         * Callback on each tick of the internal count down timer.
         *
         * @param secondsToNone the number of seconds to finishing the whole countdown.
         */
        void onTick(long secondsToNone);

        void onFinish();
    }
}
