package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsensei.soladroid.R;

/**
 *
 */
public class TasksFragment extends Fragment {

    /**
     * Argument bundle key for passing the Trello task list id to this fragment.
     */
    private static final String ARG_TRELLO_TASK_LIST_ID = "task_list_id";

    /**
     * The Trello task list id for which we show the tasks in this fragment.
     */
    private String taskListId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskListId = getArguments().getString(ARG_TRELLO_TASK_LIST_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView listName = (TextView) getView().findViewById(R.id.task_status_list_name);
        listName.setText(taskListId);
    }

    /**
     * Creates a TasksFragment instance and sets the given task list id as argument.
     *
     * @param taskListId the Trello task list id for which we'll load the tasks in this fragment.
     * @return an instance of TasksFragment
     */
    public static TasksFragment create(String taskListId) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRELLO_TASK_LIST_ID, taskListId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * No-arg constructor.
     */
    public TasksFragment() {
    }
}
