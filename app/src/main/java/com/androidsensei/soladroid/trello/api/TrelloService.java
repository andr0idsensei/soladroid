package com.androidsensei.soladroid.trello.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mihai on 5/17/15.
 */
public interface TrelloService {
    String BASE_URL = "https://trello.com/1";

    /**
     * https://trello.com/1/authorize?key=3022828b12fa421bed09c6f3fa69cf8c&name=SolaDroid&expiration=1day&response_type=token&scope=read,write
     * @return
     */
    @GET("/authorize?key={appKey}&name={appName}&expiration=1day&response_type=token&scope=read,write")
    Response requestAuthToken(@Path("appKey") String appKey, @Path("appName") String appName);
}
