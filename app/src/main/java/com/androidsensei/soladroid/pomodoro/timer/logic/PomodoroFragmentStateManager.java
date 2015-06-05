package com.androidsensei.soladroid.pomodoro.timer.logic;

/**
 * Created by mihai on 6/6/15.
 */
public class PomodoroFragmentStateManager {

    private static PomodoroFragmentStateManager instance;

    private PomodoroFragmentStateManager() {
    }

    public static PomodoroFragmentStateManager getInstance () {
        if (instance == null) {
            instance = new PomodoroFragmentStateManager();
        }
        
        return instance;
    }
}
