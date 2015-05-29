package com.androidsensei.soladroid.trello.api;

import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.trello.api.model.MemberToken;
import com.androidsensei.soladroid.trello.api.model.TrelloList;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Interface which defines the Trello API service REST requests and which is used by Retrofit to handle the requests to
 * Trello.
 *
 * Created by mihai on 5/17/15.
 */
public interface TrelloService {
    String BASE_URL = "https://trello.com/1";

    /**
     * Returns the list of Trello open boards for the user, including the lists in the boards.
     *
     * @param appKey the application key
     * @param appToken the app auth token
     * @return the above mentioned list of boards.
     */
    @GET("/members/{memberId}/boards?filter=open")
    List<Board> loadOpenBoards(@Path("memberId") String memberId, @Query("key") String appKey, @Query("token") String appToken);

    /**
     * Returns the request auth token as well as the member id for a given application authorization token.
     *
     * @param authToken the app authorization token
     * @param appKey the application key
     * @return the member object containing the described data.
     */
    @GET("/tokens/{authToken}")
    MemberToken getMemberToken(@Path("authToken") String authToken, @Query("key") String appKey);

    /**
     * Returns the list of Trello task lists that belong to a given board.
     *
     * @param boardId the board id
     * @param appKey the application key
     * @param appToken the application authorization token
     * @return the above mentioned list of task lists.
     */
    @GET("/boards/{boardId}/lists?cards=open")
    List<TrelloList> loadTaskListsForBoard(@Path("boardId") String boardId, @Query("key") String appKey, @Query("token") String appToken);

    /**
     * Returns the list of Trello cards that belong to a given task list.
     * @param listId the task list id
     * @param appKey the application key
     * @param appToken the application token
     * @return the above mentioned list of cards.
     */
    @GET("/lists/{listId}/cards?filter=open")
    List<Card> loadTrelloCardsForList(@Path("listId") String listId, @Query("key") String appKey, @Query("token") String appToken);
}
