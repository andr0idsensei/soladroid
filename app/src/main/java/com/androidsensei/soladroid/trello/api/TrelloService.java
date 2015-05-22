package com.androidsensei.soladroid.trello.api;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mihai on 5/17/15.
 */
public interface TrelloService {
    String BASE_URL = "https://trello.com/1";

    /**
     * Returns the list of Trello open boards for the user, including the lists in the boards.
     *
     * @param appKey the application key
     * @param authToken the auth token
     * @return the above mentioned list of boards.
     */
    @GET("/boards?lists=open&list_fields=name")
    List<Board> loadBoards(@Query("key") String appKey, @Query("token") String authToken);
}
