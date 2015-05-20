package com.androidsensei.soladroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.androidsensei.soladroid.setup.ui.TrelloAuthJSInterface;
import com.androidsensei.soladroid.setup.ui.TrelloWebViewClient;


public class SolaDroidActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView trelloAuth = new WebView(this);
        //R.layout.activity_sola_droid
        setContentView(trelloAuth);
        WebSettings webSettings = trelloAuth.getSettings();
        webSettings.setJavaScriptEnabled(true);
        trelloAuth.addJavascriptInterface(new TrelloAuthJSInterface(this), "html_viewer");
        trelloAuth.setWebViewClient(new TrelloWebViewClient());
        trelloAuth.loadUrl("https://trello.com/1/authorize?key=3022828b12fa421bed09c6f3fa69cf8c&name=SolaDroid&expiration=1day&response_type=token&scope=read,write");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sola_droid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
