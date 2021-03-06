package com.androidsensei.soladroid.setup.ui.logic;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.androidsensei.soladroid.setup.ui.SolaDroidFragmentContract;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This JSInterface allows us to hook into the Trello authorization process in order to read the authorization token
 * from the html response returned when the user grants access to SolaDroid.
 * <p/>
 * Created by mihai on 5/20/15.
 */
public class TrelloAuthJSInterface {
    /**
     * The Android context in which this implementation resides.
     */
    private Context context;

    public TrelloAuthJSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void processHtml(final String html) {
        Pattern pattern = Pattern.compile("<pre>(.|\\n)*?<\\/pre>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String rawToken = matcher.group();
            String token = rawToken.replaceAll("<[^>]*>", "").trim();
            Log.d("r1k0", "the token: " + token);
            SharedPrefsUtil.savePreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, token, context);
            ((SolaDroidFragmentContract) context).showSetupFragment();
        }
    }
}
