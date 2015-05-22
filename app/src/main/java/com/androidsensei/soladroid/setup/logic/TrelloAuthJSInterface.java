package com.androidsensei.soladroid.setup.logic;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.androidsensei.soladroid.SolaDroidFragmentContract;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mihai on 5/20/15.
 */
public class TrelloAuthJSInterface {
    private Context context;
    private Handler jsHandler;

    public TrelloAuthJSInterface(Context context) {
        this.context = context;
        jsHandler = new Handler();
    }

    @JavascriptInterface
    public void processHtml(final String html) {
        jsHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("r1k0", "print html: " + html);
                Pattern pattern = Pattern.compile("<pre>(.|\\n)*?<\\/pre>");
                Matcher matcher = pattern.matcher(html);
                matcher.find();
                String rawToken = matcher.group();
                String token = rawToken.replaceAll("<[^>]*>", "").trim();
                SharedPrefsUtil.savePreferenceString(TrelloConstants.TRELLO_AUTH_TOKEN_KEY, token, context);
                ((SolaDroidFragmentContract) context).showSetupFragment();
                Log.d("r1k0", "the token: " + token);
            }
        });
    }
}
