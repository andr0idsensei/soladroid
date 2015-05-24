package com.androidsensei.soladroid.trello.api;

import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.List;

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
     * The list of Trello task lists that we loaded for the current user.
     */
    private List<TrelloList> trelloLists;

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

    public List<TrelloList> getTrelloLists() {
        return trelloLists;
    }

    public void setTrelloLists(List<TrelloList> trelloLists) {
        this.trelloLists = trelloLists;
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
