package com.androidsensei.soladroid.trello.api;

import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.HashMap;
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
    private Map<String, List<TrelloList>> trelloLists = new HashMap<>();

    /**
     * The map of Trello cards that belong to our to do, doing and done task lists, kept by Trello list id.
     */
    private Map<String, List<Card>> trelloCards = new HashMap<>();

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

    public void putTrelloBoards(List<Board> trelloBoards) {
        this.trelloBoards = trelloBoards;
    }

    public List<TrelloList> getTrelloList(String boardId) {
        return trelloLists.get(boardId);
    }


    public void putTrelloLists(String boardId, List<TrelloList> trelloList) {
        trelloLists.put(boardId, trelloList);
    }

    public void putCardList(String listId, List<Card> trelloCards) {
        this.trelloCards.put(listId, trelloCards);
    }

    public List<Card> getTrelloCards(String listId) {
        return trelloCards.get(listId);
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
    public boolean hasLists(String boardId) {
        List<TrelloList> taskList = trelloLists.get(boardId);

        return taskList != null && !taskList.isEmpty();
    }

    /**
     * Checks to see if we have some cards loaded.
     *
     * @param listId the Trello list id.
     * @return true if we have a loaded list of cards corresponding to the given list id.
     */
    public boolean hasCards(String listId) {
        List<Card> cardList = trelloCards.get(listId);

        return cardList != null && ! cardList.isEmpty();
    }

    /**
     * Moves a given Trello card from the origin Trello List to the destination Trello list.
     *
     * @param originListId      the origin Trello list id.
     * @param destinationListId the destination Trello list id.
     * @param card              the card to move
     */
    public void moveCardToList(String originListId, String destinationListId, Card card) {
        List<Card> origin = trelloCards.get(originListId);
        List<Card> destination = trelloCards.get(destinationListId);
        if (origin != null) { // make sure data is already loaded
            origin.remove(card);
        }
        if (destination != null) {
            destination.add(card);
        }
    }
}
