package com.androidsensei.soladroid.utils.trello;

import com.androidsensei.soladroid.trello.api.TrelloApiService;

import retrofit.RestAdapter;

/**
 * Factory class for loading the Trello service implementation.
 *
 * Created by mihai on 5/24/15.
 */
public final class TrelloServiceFactory {

    /**
     * We don't need this instantiated.
     */
    private TrelloServiceFactory() {
    }

    /**
     * Creates and returns a Retrofit TrelloApiService implementation in order for us to do Trello API calls.
     *
     * @return the Trello service implementation.
     */
    public static TrelloApiService createService() {
        //todo remove logging - maybe move this in a service - use some notification system
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TrelloApiService.BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        TrelloApiService service = restAdapter.create(TrelloApiService.class);

        return service;
    }
}
