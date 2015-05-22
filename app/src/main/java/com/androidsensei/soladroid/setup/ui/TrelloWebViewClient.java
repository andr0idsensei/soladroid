package com.androidsensei.soladroid.setup.ui;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by mihai on 5/16/15.
 */
public class TrelloWebViewClient extends WebViewClient {
    @Override
    public void onPageFinished(WebView view, final String url) {
        super.onPageFinished(view, url);
        Log.d("r1k0", "page finished url: " + url);
        if (url.contains("token/approve")) {
            view.loadUrl("javascript:window.html_viewer.processHtml" +
                    "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    }
}
