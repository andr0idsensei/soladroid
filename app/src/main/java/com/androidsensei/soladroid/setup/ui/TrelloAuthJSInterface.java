package com.androidsensei.soladroid.setup.ui;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mihai on 5/20/15.
 */
public class TrelloAuthJSInterface {
    Context context;
    Handler jsHandler;

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
                Log.d("r1k0", "the token: " + token);
            }
        });
    }
}
