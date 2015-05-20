package com.androidsensei.soladroid.setup.ui;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
