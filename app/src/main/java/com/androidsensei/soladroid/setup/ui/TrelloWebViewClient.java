package com.androidsensei.soladroid.setup.ui;

import android.graphics.Bitmap;
import android.util.Log;
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
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("r1k0", "page finished url: " + url);
        Log.d("r1k0", "page finished content: " + view.getContentDescription());
    }

    @Override
    public void onPageStarted(WebView view, final String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d("r1k0", "page started url: " + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

            Observable.create(new Observable.OnSubscribe<Response>() {
                @Override
                public void call(Subscriber<? super Response> subscriber) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
