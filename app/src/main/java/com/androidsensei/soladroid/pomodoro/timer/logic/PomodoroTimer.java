package com.androidsensei.soladroid.pomodoro.timer.logic;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Wrapper over Android's CountdownTimer which we use to manage the Pomodoro related operations.
 *
 * Created by mihai on 6/1/15.
 */
public class PomodoroTimer {
    /**
     * Conversion between from milliseconds to seconds.
     */
    private static final long MILLI_TO_SECOND_RATE = 1000;

    /**
     * The length of one Pomodoro (25 minutes) in milliseconds.
     * 1500000 ms = 25 minutes
     */
    private static final long POMODORO_LENGTH = 25000;

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
     * The number of milliseconds since the current Pomodoro started.
     */
    private long elapsedTime;

    /**
     * Is current Pomodoro paused?
     */
    private boolean isPaused;

    /**
     * Is current Pomodoro stopped?
     */
    private boolean isStopped;

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
    public PomodoroTimer(long currentTimer, PomodoroCounterCallback callback) {
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
                elapsedTime = POMODORO_LENGTH - remainingTime;
            }

            @Override
            public void onFinish() {
                Log.d("r1k0", "countdown finished.");
                callback.onFinish();
                remainingTime = 0;
                elapsedTime = POMODORO_LENGTH;
            }
        };
    }

    /**
     * Starts the Pomodoro timer and if it was paused before, it continues from where it left off.
     */
    public void start() {
        if (isPaused || isStopped) {
            countDownTimer = createCountDownTimer().start();
        } else {
            countDownTimer.start();
        }
        isPaused = false;
        isStopped = false;
    }

    /**
     * Pauses the current Pomodoro, allowing for continuation.
     */
    public void pause() {
        isPaused = true;
        isStopped = false;
        countDownTimer.cancel();
    }

    /**
     * Stops the current Pomodoro and resets the counter.
     */
    public void stop() {
        isPaused = false;
        isStopped = true;
        remainingTime = POMODORO_LENGTH;
        countDownTimer.cancel();
        callback.onStop(getElapsedTime());
    }

    /**
     * @return the remaining time to the end of the current Pomodoro, in seconds.
     */
    public long getRemainingTime() {
        return remainingTime / MILLI_TO_SECOND_RATE;
    }

    /**
     * @return the elapsed time since the current Pomodoro started, in seconds.
     */
    public long getElapsedTime() {
        return elapsedTime / MILLI_TO_SECOND_RATE;
    }

    /**
     * @return true if the current Pomodoro timer is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * @return true if the current Pomodoro timer is stopped, false otherwise.
     */
    public boolean isStopped() {
        return  isStopped;
    }

    public static long getPomodoroLengthInSeconds() {
        return POMODORO_LENGTH / MILLI_TO_SECOND_RATE;
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

        /**
         * Call this method when the internal count down timer finishes.
         */
        void onFinish();

        /**
         * Call this method when the timer gets stoped.
         *
         * @param elapsedTime the number of seconds elapsed since the start of the current Pomodoro
         */
        void onStop(long elapsedTime);
    }
}
