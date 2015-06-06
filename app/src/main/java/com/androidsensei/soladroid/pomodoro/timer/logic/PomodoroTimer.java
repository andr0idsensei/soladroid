package com.androidsensei.soladroid.pomodoro.timer.logic;

import android.os.CountDownTimer;

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
     * The tick of the Pomodoro timer in milliseconds.
     */
    private static final long POMODORO_COUNTER_TICK = 1000;

    /**
     * The current timeState of this Pomodoro timer - can be initialized, paused, stopped or running.
     */
    private TimerState timerState;

    /**
     * The number of milliseconds until the current Pomodoro or break ends.
     */
    private long remainingTime;

    /**
     * The number of milliseconds since the current Pomodoro or break started.
     */
    private long elapsedTime;

    /**
     * THe number of milliseconds with which this timer is started - is based on the TimerType value.
     */
    private long initialTime;

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
     * @param initialTime the initial time, in seconds, of the Pomodoro or break we are currently keeping - if this
     *                      is null, it means the timer gets started now.
     * @param callback the callback to notify our callers of when ticks happen.
     */
    public PomodoroTimer(int initialTime, PomodoroCounterCallback callback) {
        this.initialTime = initialTime * MILLI_TO_SECOND_RATE;
        this.remainingTime = this.initialTime;
        this.callback = callback;
        this.countDownTimer = createCountDownTimer();
        timerState = TimerState.INITIALIZED;
    }

    /**
     * @return an instance of CountDownTime to use as an internal time count down implementation.
     */
    private CountDownTimer createCountDownTimer() {
        return new CountDownTimer(remainingTime, POMODORO_COUNTER_TICK) {
            //TODO seems like Android's countdown timer skips the last tick - I should address that issue.
            @Override
            public void onTick(long millisUntilFinished) {
                callback.onTick(millisUntilFinished / MILLI_TO_SECOND_RATE);
                remainingTime = millisUntilFinished;
                elapsedTime = (initialTime - remainingTime) + POMODORO_COUNTER_TICK;
            }

            @Override
            public void onFinish() {
                remainingTime = 0;
                elapsedTime = initialTime;
                callback.onFinish(getElapsedTime());
            }
        };
    }

    /**
     * Starts the Pomodoro timer and if it was paused before, it continues from where it left off.
     */
    public void start() {
        if (isInitialized()) {
            countDownTimer.start();
            timerState = TimerState.RUNNING;
        } else if (isPaused() || isStopped()) {
            countDownTimer = createCountDownTimer().start();
            timerState = TimerState.RUNNING;
        }
    }

    /**
     * Pauses the current Pomodoro, allowing for continuation.
     */
    public void pause() {
        if (isRunning()) {
            timerState = TimerState.PAUSED;
            countDownTimer.cancel();
        }
    }

    /**
     * Stops the current Pomodoro and resets the counter.
     */
    public void stop() {
        if (isRunning()) {
            remainingTime = initialTime;
            countDownTimer.cancel();
            callback.onStop(getElapsedTime());
            timerState = TimerState.STOPPED;
        }
    }

    /**
     * Resets the current timer's callback - this is useful for runtime configuration changes.
     *
     * @param callback the new callback to use.
     */
    public void resetCallback(PomodoroCounterCallback callback) {
        this.callback = callback;
    }

    /**
     * @return the elapsed time since the current Pomodoro started, in seconds.
     */
    private long getElapsedTime() {
        return elapsedTime / MILLI_TO_SECOND_RATE;
    }

    /**
     * @return true if the current Pomodoro timer is paused, false otherwise.
     */
    public boolean isPaused() {
        return timerState == TimerState.PAUSED;
    }

    /**
     * @return true if the current Pomodoro timer is stopped, false otherwise.
     */
    public boolean isStopped() {
        return  timerState == TimerState.STOPPED;
    }

    /**
     * @return true if the current Pomodoro timer is running, false otherwise.
     */
    public boolean isRunning() {
        return timerState == TimerState.RUNNING;
    }

    /**
     * @return true if the current timer timeState is initialized, false otherwise.
     */
    public  boolean isInitialized() {
        return timerState == TimerState.INITIALIZED;
    }

    /**
     * Enumeration of the current timer's states.
     */
    private enum TimerState {
        INITIALIZED(1),
        PAUSED(2),
        STOPPED(3),
        RUNNING(4);

        private int value;

        TimerState(int value) {
            this.value = value;
        }
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
         *
         * @param elapsedTime the number of seconds elapsed since the start of the current timer
         */
        void onFinish(long elapsedTime);

        /**
         * Call this method when the timer gets stoped.
         *
         * @param elapsedTime the number of seconds elapsed since the start of the current timer
         */
        void onStop(long elapsedTime);
    }
}
