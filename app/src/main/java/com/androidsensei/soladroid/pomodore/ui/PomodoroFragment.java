package com.androidsensei.soladroid.pomodore.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * Created by mihai on 5/29/15.
 */
public class PomodoroFragment extends SolaDroidBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_auth, container, false);
    }
}
