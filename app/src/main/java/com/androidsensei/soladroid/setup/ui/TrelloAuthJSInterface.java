package com.androidsensei.soladroid.setup.ui;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by mihai on 5/20/15.
 */
public class TrelloAuthJSInterface {
    Context context;

    public TrelloAuthJSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void processHtml(String html) {
        Log.d("r1k0", "print html: " + html);
        //todo read the token and save it - take care of the deny part too and see if we need a handler
    }
}
