package com.androidsensei.soladroid.utils.trello;

/**
 * Class to contain Trello specific constants such as app key, secret, API URLs if needed.
 *
 * Created by mihai on 5/16/15.
 */
public final class TrelloConstants {
    /**
     * The generated Trello application key.
     */
    public static final String TRELLO_APP_KEY = "3022828b12fa421bed09c6f3fa69cf8c";

    /**
     * The generated Trello application secret.
     */
    public static final String TRELLO_APP_SECRET = "f6e52e15ac29a716d9be8c758d5aac7d6ecdc92599d7851958ba8c34fcaeda41";

    /**
     *
     * The application name we want to be identified by with Trello.
     */
    public static final String TRELLO_APP_NAME = "SolaDroid";

    /**
     * Key by which we refer the Trello authorization token throughout the application code.
     */
    public static final String TRELLO_AUTH_TOKEN_KEY = "trello_auth_token";

    /**
     * The Trello authorization url.
     */
    public static final String TRELLO_AUTH_URL = "https://trello.com/1/authorize?key="+ TRELLO_APP_KEY + "&name=" +
            TRELLO_APP_NAME + "&expiration=1day&response_type=token&scope=read,write";

    /**
     * Private constructor because we don't want an instance of this class.
     */
    private TrelloConstants() {}
}
