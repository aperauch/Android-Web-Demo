package com.aron.webdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crittercism.app.Crittercism;

public class WebViewActivity extends AppCompatActivity {

    private final String TAG = "WebDemo-WebViewActivity";
    private boolean usingIntelligenceSdk;
    public static final String EXTRA_URL = "extra.url";
    public static final String EXTRA_USE_INTELLIGENCE_SDK = "extra.useIntelligence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String url = getIntent().getStringExtra(EXTRA_URL);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        setTitle(url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usingIntelligenceSdk = getIntent().getBooleanExtra(EXTRA_USE_INTELLIGENCE_SDK, false);
        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Created");
            Crittercism.beginUserFlow("Web_View_User_Flow");
            Crittercism.instrumentWebView(webView);
            Crittercism.getNetworkInstrumentation();
        }

        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Started");
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Restarted");
        }
    }

    @Override
    public void onResume() {
        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Resumed");
            Log.i(TAG, "App crashed on previous run:  " + Crittercism.didCrashOnLastLoad());
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Paused");
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Stopped");
            Crittercism.endUserFlow("Web_View_User_Flow");
            Crittercism.sendAppLoadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Web_View_Destroyed");
            Crittercism.sendAppLoadData();
        }
    }
}