package com.androidsensei.soladroid.trello.api;

import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.List;
import java.util.Map;

/**
 * Singleton class for keeping the results we got from Trello calls during the application's lifetime.
 *
 * Created by mihai on 5/24/15.
 */
public final class TrelloResultsManager {

    /**
     * The unique instance of this manager class.
     */
    private static TrelloResultsManager instance;

    /**
     * The list of open Trello boards that we loaded for the current user.
     */
    private List<Board> trelloBoards;

    /**
     * The map of Trello task lists that we loaded for the current user kept by board id.
     */
    private Map<String, List<TrelloList>> trelloLists;

    private TrelloResultsManager() {
    }

    /**
     * Factory method for returning the unique instance of this manager class.
     *
     * @return the instance member
     */
    public static TrelloResultsManager getInstance() {
        if (instance == null) {
            instance = new TrelloResultsManager();
        }

        return instance;
    }

    public List<Board> getTrelloBoards() {
        return trelloBoards;
    }

    public void setTrelloBoards(List<Board> trelloBoards) {
        this.trelloBoards = trelloBoards;
    }

    public List<TrelloList> getTrelloList(String boardId) {
        return trelloLists.get(boardId);
    }

    public void putTrelloLists(String boardId, List<TrelloList> trelloLists) {
        //todo implement this
    }

    /**
     * Checks to see if we have some boards loaded.
     *
     * @return true if the boards list is not null or empty
     */
    public boolean hasBoards() {
        return trelloBoards != null && !trelloBoards.isEmpty();
    }

    /**
     * Checks to see if we have some task lists loaded.
     *
     * @return true if the task lists list is not null or empty
     */
    public boolean hasLists() {
        return trelloLists != null && !trelloLists.isEmpty();
    }
}
