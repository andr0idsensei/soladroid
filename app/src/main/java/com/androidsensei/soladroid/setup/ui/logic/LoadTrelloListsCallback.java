package com.androidsensei.soladroid.setup.ui.logic;

import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.List;

/**
 * Callback interface to get a notification when Trello task lists get loaded for a board.
 *
 * Created by mihai on 5/31/15.
 */
public interface LoadTrelloListsCallback {
    /**
     * This gets called when Trello task lists for a board are loaded.
     * @param trelloLists the list of Trello task lists.
     */
    void onTaskListsLoaded(List<TrelloList> trelloLists);
}
