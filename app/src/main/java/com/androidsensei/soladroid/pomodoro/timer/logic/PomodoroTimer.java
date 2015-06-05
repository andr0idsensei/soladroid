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
    private static final long SHORT_BREAK = 300000;

    /**
     * The length of the long break (15 minutes) in milliseconds.
     */
    private static final long LONG_BREAK = 900000;

    /**
     * The tick of the Pomodoro timer in milliseconds.
     */
    private static final long POMODORO_COUNTER_TICK = 1000;

    /**
     * The type of this Pomodoro timer - can be either pomodoro, short or long break.
     */
    private TimerType timerType;

    /**
     * The current state of this Pomodoro timer - can be initialized, paused, stopped or running.
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
     * @param remainingTime the remaining time, in seconds, of the Pomodoro or break we are currently keeping - if this
     *                      is null, it means the timer gets started now.
     * @param callback the callback to notify our callers of when ticks happen.
     */
    public PomodoroTimer(Long remainingTime, TimerType timerType, PomodoroCounterCallback callback) {
        this.timerType = timerType;
        if (remainingTime == null) {
            switch (timerType) {
                case POMODORO:
                    this.initialTime = POMODORO_LENGTH;
                    break;

                case SHORT_BREAK:
                    this.initialTime = SHORT_BREAK;
                    break;

                case LONG_BREAK:
                    this.initialTime = LONG_BREAK;
                    break;

                default:
                    break;
            }

        } else {
            this.initialTime = remainingTime * MILLI_TO_SECOND_RATE;
        }

        this.remainingTime = initialTime;
        this.callback = callback;
        this.countDownTimer = createCountDownTimer();
        timerState = TimerState.INITIALIZED;
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
     * @return the remaining time to the end of the current Pomodoro, in seconds.
     */
    public long getRemainingTime() {
        return remainingTime / MILLI_TO_SECOND_RATE;
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
     * @return true if the current timer state is initialized, false otherwise.
     */
    public  boolean isInitialized() {
        return timerState == TimerState.INITIALIZED;
    }

    /**
     * @return the number of initial seconds this timer has set.
     */
    public long getTimerLength() {
        return initialTime / MILLI_TO_SECOND_RATE;
    }

    /**
     * Enumeration of the possible types the Pomodoro timer should handle.
     */
    public enum TimerType {
        POMODORO(1),
        SHORT_BREAK(2),
        LONG_BREAK(3);

        private int value;

        TimerType(int value) {
            this.value = value;
        }

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
