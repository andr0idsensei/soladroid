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
     * The number of milliseconds until the current Pomodoro ends.
     */
    private long remainingTime;

    /**
     * Is current Pomodoro paused?
     */
    private boolean isPaused;

    /**
     * Internal count down timer to count time down.
     */
    private CountDownTimer countDownTimer;

    /**
     * The implementation of the pomodoro counter callback interface.
     */
    private PomodoroCounterCallback callback;

    /**
     * Constructor with a callback and the remaining time.
     *
     * @param currentTimer the remaining time of the Pomodoro we are currently keeping
     * @param callback the callback to notify our callers of when ticks happen
     */
    public PomodoroCounter(long currentTimer, PomodoroCounterCallback callback) {
        if (currentTimer == 0) {
            this.remainingTime = POMODORO_LENGTH;
        } else {
            this.remainingTime = currentTimer * MILLI_TO_SECOND_RATE;
        }
        this.callback = callback;
        this.countDownTimer = createCountDownTimer();
    }

    /**
     * @return an instance of CountDownTime to use as an internal time count down implementation.
     */
    private CountDownTimer createCountDownTimer() {
        return new CountDownTimer(remainingTime, POMODORO_COUNTER_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                callback.onTick(millisUntilFinished / MILLI_TO_SECOND_RATE);
                remainingTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Log.d("r1k0", "countdown finished.");
                callback.onFinish();
                remainingTime = 0;
            }
        };
    }

    /**
     * Starts the Pomodoro timer and if it was paused before, it continues from where it left off.
     */
    public void start() {
        if (isPaused) {
            countDownTimer = createCountDownTimer().start();
        } else {
            countDownTimer.start();
        }
        isPaused = false;
    }

    /**
     * Pauses the current Pomodoro, allowing for continuation.
     */
    public void pause() {
        isPaused = true;
        countDownTimer.cancel();
    }

    /**
     * Stops the current Pomodoro and resets the counter.
     */
    public void stop() {
        isPaused = false;
        countDownTimer.cancel();
    }

    /**
     * @return the remaining time to the end of the current Pomodoro in seconds.
     */
    public long getRemainingTime() {
        return remainingTime / MILLI_TO_SECOND_RATE;
    }

    public boolean isPaused() {
        return isPaused;
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
