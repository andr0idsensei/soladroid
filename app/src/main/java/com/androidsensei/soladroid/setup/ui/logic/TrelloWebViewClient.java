package com.androidsensei.soladroid.setup.ui.logic;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidsensei.soladroid.setup.ui.SolaDroidFragmentContract;

/**
 * Web view client implementation which helps us hook into the Trello authorization process.
 * <p/>
 * Created by mihai on 5/16/15.
 */
public class TrelloWebViewClient extends WebViewClient {
    /**
     * Reference to the fragment contract in order to be able to control the flow of the authorization action.
     */
    private SolaDroidFragmentContract contract;

    public TrelloWebViewClient(SolaDroidFragmentContract contract) {
        this.contract = contract;
    }

    @Override
    public void onPageFinished(WebView view, final String url) {
        super.onPageFinished(view, url);
        if (url.contains("token/approve")) {
            view.loadUrl("javascript:window.html_viewer.processHtml" +
                    "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("r1k0", "pageStarted - url: " + url);
        super.onPageStarted(view, url, favicon);
        if ("https://trello.com/".equals(url)) { // means that the user pressed the deny button and the default trello url is loaded.
            view.stopLoading();
            contract.showAccessDeniedFragment();
        }
    }
}
