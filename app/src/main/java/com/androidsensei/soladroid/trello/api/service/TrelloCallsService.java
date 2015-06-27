package com.androidsensei.soladroid.trello.api.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.androidsensei.soladroid.trello.api.TrelloApiService;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;
import com.androidsensei.soladroid.utils.trello.TrelloServiceFactory;

/**
 * Intent service for handling Trello API calls in the background.
 */
public class TrelloCallsService extends IntentService {
    /**
     * Intent action name for saving the time comment.
     */
    private static final String ACTION_SAVE_TIME_COMMENT = "com.androidsensei.soladroid.trello.api.service.action.ACTION_SAVE_TIME_COMMENT";

    /**
     * Intent action name for moving the card from one board to another.
     */
    private static final String ACTION_MOVE_CARD = "com.androidsensei.soladroid.trello.api.service.action.ACTION_MOVE_CARD";

    /**
     * Intent extra param name for the time comment text value.
     */
    private static final String EXTRA_COMMENT_TEXT = "com.androidsensei.soladroid.trello.api.service.extra.COMMENT_TEXT";

    /**
     * Intent extra param name for the card id value.
     */
    private static final String EXTRA_CARD_ID = "com.androidsensei.soladroid.trello.api.service.extra.CARD_ID";

    /**
     * Intent extra param name for the "to board" id value.
     */
    private static final String EXTRA_TO_BOARD_ID = "com.androidsensei.soladroid.trello.api.service.extra.TO_BOARD_ID";

    /**
     * The Trello REST API Service wrapper created with Retrofit.
     */
    private TrelloApiService trelloApiService;

    /**
     * The Trello App Token.
     */
    private String trelloAppToken;

    /**
     * Starts this service to perform the save time comment action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void saveTimeComment(Context context, String timeCommentText, String cardId) {
        Intent intent = new Intent(context, TrelloCallsService.class);
        intent.setAction(ACTION_SAVE_TIME_COMMENT);
        intent.putExtra(EXTRA_COMMENT_TEXT, timeCommentText);
        intent.putExtra(EXTRA_CARD_ID, cardId);
        context.startService(intent);
    }

    /**
     * Service constructor.
     */
    public TrelloCallsService() {
        super("TrelloCallsService");
        trelloApiService = TrelloServiceFactory.createService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        trelloAppToken = SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_TIME_COMMENT.equals(action)) {
                final String timeCommentText = intent.getStringExtra(EXTRA_COMMENT_TEXT);
                final String cardId = intent.getStringExtra(EXTRA_CARD_ID);
                handleActionSaveTimeComment(timeCommentText, cardId);
            }
        }
    }

    /**
     * Handle save time comment action in the provided background thread with the provided
     * parameters.
     *
     * @param timeCommentText the time comment text
     * @param cardId          the Trello card id for which the comment is saved
     */
    private void handleActionSaveTimeComment(String timeCommentText, String cardId) {
        trelloApiService.addTimeComment(cardId, timeCommentText, TrelloConstants.TRELLO_APP_KEY, trelloAppToken);
    }

}
