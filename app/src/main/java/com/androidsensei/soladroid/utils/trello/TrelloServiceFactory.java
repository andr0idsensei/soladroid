package com.androidsensei.soladroid.utils.trello;

import com.androidsensei.soladroid.trello.api.TrelloService;

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
     * Creates and returns a Retrofit TrelloService implementation in order for us to do Trello API calls.
     *
     * @return the Trello service implementation.
     */
    public static TrelloService createService() {
        //todo remove logging - maybe move this in a service - use some notification system
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TrelloService.BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        TrelloService service = restAdapter.create(TrelloService.class);

        return service;
    }
}
