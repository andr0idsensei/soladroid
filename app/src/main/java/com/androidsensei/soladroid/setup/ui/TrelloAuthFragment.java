package com.androidsensei.soladroid.setup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.setup.ui.logic.TrelloAuthJSInterface;
import com.androidsensei.soladroid.setup.ui.logic.TrelloWebViewClient;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

/**
 * This fragment is used for setting up Trello authorization for our application. It will use a web view to load the
 * Trello auth URL and once the user allows the application to access the Trello account it will notify it's parent
 * activity to move to the next stage in the Trello setup process.
 * If access is not granted, it will notify the parent activity of that so it can take appropriate steps.
 *
 * @author mihai
 */
public class TrelloAuthFragment extends SolaDroidBaseFragment {
    /**
     * The web view in which we display the Trello auth page.
     */
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_auth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) getView().findViewById(R.id.trello_auth_web_view);
        setupWebView();
    }

    /**
     *  Sets up the web view which will connect to Trello in order to allow authorization for the app.
     */
    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new TrelloAuthJSInterface(getActivity()), "html_viewer");
        webView.setWebViewClient(new TrelloWebViewClient(contract));
        webView.loadUrl(TrelloConstants.TRELLO_AUTH_URL);
    }

    /**
     * Go back in the web view.
     */
    public boolean goBack() {
        boolean canGoBack = webView.canGoBack();
        if (canGoBack) {
            webView.goBack();
        }

        return canGoBack;
    }
}
