package com.aron.webdemo;

import static androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "WebDemo-MainActivity";
    private boolean usingIntelligenceSdk;
    private AppConfig appConfig;
    private String intelligenceAppId;
    private String intelligenceAppLoggingLevel;
    private String enrollmentUsername;
    private String startingUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appConfig = new AppConfig(getApplicationContext());

        configureIntelligenceSdk();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Main_Activity_Screen");
            Crittercism.beginUserFlow("Main_Activity_User_Flow");
            Crittercism.sendAppLoadData();
            Log.i(TAG, "App crashed on previous run:  " + Crittercism.didCrashOnLastLoad());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startCustomTab = (Button) findViewById(R.id.start_custom_tab);
        startCustomTab.setOnClickListener(this);

        Button startWebView = (Button) findViewById(R.id.start_web_view);
        startWebView.setOnClickListener(this);

        setUrlEditTextView();

        // Check if other browser apps support Chrome Custom Tabs
        getCustomTabsPackages(this);
    }

    private void configureIntelligenceSdk() {
        // Get app config values passed by WS1 UEM
        intelligenceAppId = appConfig.getIntelligenceAppId();
        if (intelligenceAppId != null && !intelligenceAppId.isEmpty()) {
            usingIntelligenceSdk = true;
            intelligenceAppLoggingLevel = appConfig.getIntelligenceAppLoggingLevel();
            enrollmentUsername = appConfig.getEnrollmentUsername();

            Log.i(TAG, "Intelligence App ID App Config:  " + intelligenceAppId);
            Log.i(TAG, "Intelligence App Logging Level App Config:  " + intelligenceAppLoggingLevel);
            Log.i(TAG, "WS1 UEM Enrolled User Username App Config:  " + enrollmentUsername);

            // Set Intelligence app ID for SDK
            Crittercism.initialize(getApplicationContext(), intelligenceAppId);

            // Set Intelligence SDK app logging level
            switch (intelligenceAppLoggingLevel) {
                case "DEBUG":
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Debug);
                    break;
                case "ERROR":
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Error);
                    break;
                case "WARN":
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Warning);
                    break;
                case "INFO":
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Info);
                    break;
                case "SILENT":
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Silent);
                    break;
                default:
                    Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Debug);
                    break;
            }

            // Set enrollment user username for Intelligence if given as app config
            if (enrollmentUsername != null && !enrollmentUsername.isEmpty()) {
                Crittercism.setUsername(enrollmentUsername);
            }
        }
        else {
            Log.w(TAG, "Intelligence App ID App Config is NULL or empty.  The Intelligence SDK will not be used.");
            usingIntelligenceSdk = false;
        }
    }

    private void setUrlEditTextView() {
        startingUrl = appConfig.getUrl();
        Log.i(TAG, "Starting URL App Config:  " + startingUrl);

        if (startingUrl != null && !startingUrl.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.url);
            editText.setText(startingUrl, TextView.BufferType.EDITABLE);
        }
    }

    @Override
    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.url);
        String url = editText.getText().toString();
        url = url.trim();

        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL and try again.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "URL is empty or invalid.  Please enter a URL and try again.");
            return;
        }

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        switch (view.getId()) {
            case R.id.start_custom_tab:
                onClickStartCustomTab(url);
                break;
            case R.id.start_web_view:
                onClickStartWebView(url);
                break;
            default:
                break;
        }
    }

    private void onClickStartCustomTab(String url) {
        Log.i(TAG, "Opening Chrome Custom Tab for URL " + url);

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Chrome_Custom_Tab");
            Crittercism.beginUserFlow("Chrome_Custom_Tab_User_Flow");
        }

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(this, Uri.parse(url));

        if (usingIntelligenceSdk) {
            Crittercism.endUserFlow("Chrome_Custom_Tab_User_Flow");
        }
    }

    private void onClickStartWebView(String url) {
        Log.i(TAG, "Opening Web View for URL " + url);

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, url);

        if (usingIntelligenceSdk) {
            intent.putExtra(WebViewActivity.EXTRA_USE_INTELLIGENCE_SDK, true);
        }

        startActivity(intent);
    }

    /**
     * Returns a list of packages that support Custom Tabs.
     */
    public static ArrayList<ResolveInfo> getCustomTabsPackages(Context context) {
        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.fromParts("http", "", null));

        // Get all apps that can handle VIEW intents.
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        ArrayList<ResolveInfo> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info);
                Log.i("WebDemo-MainActivity", "Found package that supports Chrome Custom Tabs:  " + info.activityInfo.packageName);
            }
        }
        return packagesSupportingCustomTabs;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Started");
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Restarted");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Resumed");
            Log.i(TAG, "App crashed on previous run:  " + Crittercism.didCrashOnLastLoad());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Paused");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Stopped");
            Crittercism.endUserFlow("Main_Activity_User_Flow");
            Crittercism.getNetworkInstrumentation();
            Crittercism.sendAppLoadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("App_Destroyed");
            Crittercism.sendAppLoadData();
        }
    }
}