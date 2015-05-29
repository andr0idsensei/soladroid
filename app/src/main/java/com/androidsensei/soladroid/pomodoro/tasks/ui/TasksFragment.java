package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 *
 */
public class TasksFragment extends SolaDroidBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

}
