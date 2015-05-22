package com.androidsensei.soladroid.setup.ui.logic;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidsensei.soladroid.SolaDroidFragmentContract;

/**
 * Web view client implementation which helps us hook into the Trello authorization process.
 *
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
        Log.d("r1k0", "page finished url: " + url);
        if (url.contains("token/approve")) {
            view.loadUrl("javascript:window.html_viewer.processHtml" +
                    "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!(url.contains("token/approve") || url.contains("1/authorize"))) {
            view.stopLoading();
            contract.showAccessDeniedFragment();
        }
    }
}
