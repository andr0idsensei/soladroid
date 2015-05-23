package com.androidsensei.soladroid.setup.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying the Trello task lists in the spinners.
 *
 * Created by mihai on 5/23/15.
 */
public class TaskListSpinnerAdapter extends BaseAdapter {
    /**
     * The list of Trello task lists for a board.
     */
    private List<TrelloList> trelloTaskLists;

    /**
     * The layout inflater we'll use to load up this adapter's views.
     */
    private LayoutInflater inflater;

    /**
     * Constructor that takes a layout inflater as an argument.
     *
     * @param inflater the layout inflater
     */
    public TaskListSpinnerAdapter(LayoutInflater inflater) {
        trelloTaskLists = new ArrayList<>();
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return trelloTaskLists.size();
    }

    @Override
    public Object getItem(int position) {
        return trelloTaskLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.setup_screen_spinner_item, parent, false);
        }
        TextView taskListName = (TextView) convertView.findViewById(R.id.trello_list_name);
        taskListName.setText(trelloTaskLists.get(position).getName());

        return convertView;
    }

    /**
     * Adds a list of Trello task lists to the current adapter's data.
     *
     * @param trelloLists the list of Trello task lists
     */
    public void addItems(List<TrelloList> trelloLists) {
        trelloTaskLists.addAll(trelloLists);
        notifyDataSetChanged();
    }
}
