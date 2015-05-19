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
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Log.d("r1k0", "intercept url: " + request.getUrl());
        final String url = request.getUrl().toString();
        if (url.contains("token/approve")) {
            Observable.create(new Observable.OnSubscribe<Response>() {
                @Override
                public void call(Subscriber<? super Response> subscriber) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        subscriber.onNext(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {
                    try {
                        Log.d("r1k0", "response body from page started: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            
            return null;
        }

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final String url) {
        Log.d("r1k0", "override url: " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, final String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d("r1k0", "page started url: " + url);
    }
}
