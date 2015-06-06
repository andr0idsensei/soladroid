package com.androidsensei.soladroid.pomodoro.timer.logic;

import com.androidsensei.soladroid.trello.api.model.Card;

/**
 * Singleton class for managing the state of the Pomodoro fragment. It is better to use this approach since I may be able
 * to encapsulate some state specific logic in here and move it out of the fragment.
 *
 * Created by mihai on 6/6/15.
 */
public class PomodoroFragmentStateManager {

    /**
     * The unique instance of the state manager.
     */
    private static PomodoroFragmentStateManager instance;

    /**
     * The state in which the fragment currently is - it can be that it is during a Pomodoro, a Short or a Long Break.
     */
    private PomodoroTimeState state;

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
     * The Pomodoro count down timer used to determine the current state of the Pomodoro for the task at hand.
     */
    private PomodoroTimer pomodoroTimer;

    /**
     * Private constructor as we control the intantiating process from within.
     */
    private PomodoroFragmentStateManager() {
    }

    /**
     * The get instance method to return the unique instance of the state manager.
     *
     * @return the unique instance of this class
     */
    public static PomodoroFragmentStateManager getInstance () {
        if (instance == null) {
            instance = new PomodoroFragmentStateManager();
        }

        return instance;
    }

    /**
     * @return the current time state of the task - Pomodoro, Long or Short Break.
     */
    public PomodoroTimeState state() {
        return state;
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
     * Initializes the timer with the given PomodoroTimeState wich can be a Pomodoro, a Short Break or a Long Break.
     *
     * @param state the state which will tell the timer's initial time.
     * @param callback the callback from the timer when various actions happen.
     * @return the initialized PomodoroTimer
     */
    public PomodoroTimer initTimer(boolean configChanged, PomodoroTimeState state, PomodoroTimer.PomodoroCounterCallback callback) {
        this.state = state;
        if (configChanged) {
            pomodoroTimer.resetCallback(callback);
        } else {
            pomodoroTimer = new PomodoroTimer(state.value(), callback);
        }

        return pomodoroTimer;
    }

    /**
     * Enumeration of the possible types the Pomodoro timer should handle.
     */
    public enum PomodoroTimeState {
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

        PomodoroTimeState(int value) {
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
