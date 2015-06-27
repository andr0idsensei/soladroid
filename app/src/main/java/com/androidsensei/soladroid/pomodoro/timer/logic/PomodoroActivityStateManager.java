package com.androidsensei.soladroid.pomodoro.timer.logic;

import com.androidsensei.soladroid.trello.api.model.Card;

/**
 * Singleton class for managing the countdownTime of the Pomodoro activity. It is better to use this approach since I may be able
 * to encapsulate some countdownTime specific logic in here and move it out of the fragment.
 * <p/>
 * Created by mihai on 6/6/15.
 */
public class PomodoroActivityStateManager {

    /**
     * The unique instance of the countdownTime manager.
     */
    private static PomodoroActivityStateManager instance;

    /**
     * The countdownTime value for the current timer - it can be that of a Pomodoro, a Short or a Long Break.
     */
    private CountdownTime countdownTime;

    /**
     * The Trello card object which contains the current task to work on.
     */
    private Card trelloCard;

    /**
     * The number of Pomodoros spent on the current task.
     */
    private int pomodoroCount;

    /**
     * The total time in seconds spent on the current task.
     */
    private long totalTime;

    /**
     * The Pomodoro count down timer used to determine the current countdownTime of the Pomodoro for the task at hand.
     */
    private PomodoroTimer pomodoroTimer;

    /**
     * Private constructor as we control the intantiating process from within.
     */
    private PomodoroActivityStateManager() {
    }

    /**
     * The get instance method to return the unique instance of the countdownTime manager.
     *
     * @return the unique instance of this class
     */
    public static PomodoroActivityStateManager getInstance() {
        if (instance == null) {
            instance = new PomodoroActivityStateManager();
        }

        return instance;
    }

    /**
     * @return the current time countdownTime of the task - Pomodoro, Long or Short Break.
     */
    public CountdownTime countdownTime() {
        return countdownTime;
    }

    /**
     * @return the Trello Card (current task) we're working on.
     */
    public Card trelloCard() {
        return trelloCard;
    }

    public void setTrelloCard(Card trelloCard) {
        this.trelloCard = trelloCard;
    }

    /**
     * @return the task's current Pomodoro count.
     */
    public int pomodoroCount() {
        return pomodoroCount;
    }

    /**
     * Increments the number of Pomodoros on the current task (Trello Card).
     */
    public void incrementPomodoroCount() {
        pomodoroCount++;
    }

    /**
     * @return the current total time for the task.
     */
    public long totalTime() {
        return totalTime;
    }

    /**
     * Increments the total time spent on the current task (Trello Card) with the given elapsedTime.
     *
     * @param elapsedTime the elapsed time
     */
    public void incrementTotalTime(long elapsedTime) {
        totalTime += elapsedTime;
    }

    /**
     * @return true if the current Pomodoro countdown is finished, false otherwise.
     */
    public boolean pomodoroFinished() {
        return pomodoroTimer.isFinished() && countdownTime == CountdownTime.POMODORO;
    }

    /**
     * @return true if the current Long or Short break countdown is finished, false otherwise.
     */
    public boolean breakFinished() {
        return pomodoroTimer.isFinished() && countdownTime != CountdownTime.POMODORO;
    }

    /**
     * @return true if the current task is already in progres, false if it's not started yet.
     */
    public boolean isTaskInProgress() {
        return totalTime != 0;
    }

    /**
     * Initializes the timer with the given CountdownTime which can be a Pomodoro, a Short Break or a Long Break.
     *
     * @param state    the countdownTime which will tell the timer's initial time.
     * @param callback the callback from the timer when various actions happen.
     * @return the initialized PomodoroTimer
     */
    public PomodoroTimer initTimer(boolean configChanged, CountdownTime state, PomodoroTimer.PomodoroCounterCallback callback) {
        this.countdownTime = state;
        if (configChanged) {
            pomodoroTimer.resetCallback(callback);
        } else {
            pomodoroTimer = new PomodoroTimer(state.value(), callback);
        }

        return pomodoroTimer;
    }

    /**
     * Enumeration of the possible countdown times we could handle.
     */
    public enum CountdownTime {
        /**
         * The Pomodoro time (25 minutes) in seconds is represented by this value.
         * //TODO set back the number of seconds to 1500
         */
        POMODORO(25),

        /**
         * The Short Break time (5 minutes) in seconds is represented by this value.
         * //TODO set back the number of seconds to 300
         */
        SHORT_BREAK(5),

        /**
         * The Long Break time (15 minutes) in seconds is represented by this value.
         * //TODO set back the number of seconds to 900
         */
        LONG_BREAK(15);

        /**
         * The internal enum value.
         */
        private int value;

        CountdownTime(int value) {
            this.value = value;
        }

        /**
         * @return the internal value represented by this instance of the enum.
         */
        public int value() {
            return this.value;
        }
    }
}
