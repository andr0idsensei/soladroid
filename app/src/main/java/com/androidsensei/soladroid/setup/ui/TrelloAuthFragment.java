package com.androidsensei.soladroid.setup.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.setup.logic.TrelloAuthJSInterface;
import com.androidsensei.soladroid.setup.logic.TrelloWebViewClient;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

/**
 * This fragment is used for setting up Trello authorization. It will use a web view to load the Trello auth URL and
 * once the user allows the application to access the Trello account it will notify it's parent activity to move to
 * the next stage in the Trello setup process. If access is not granted, it will notify the parent activity of that
 * so it can take appropriate steps.
 *
 * @author mihai
 */
public class TrelloAuthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_auth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupWebView((WebView) getView().findViewById(R.id.trello_auth_web_view));
    }

    /**
     *  Sets up the web view which will connect to Trello in order to allow authorization for the app.
     *
     * @param webView the web view to be used.
     */
    private void setupWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new TrelloAuthJSInterface(getActivity()), "html_viewer");
        webView.setWebViewClient(new TrelloWebViewClient());
        webView.loadUrl(TrelloConstants.TRELLO_AUTH_URL);
    }
}
