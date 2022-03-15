package com.aron.webdemo;

import android.content.Context;
import android.content.RestrictionsManager;
import android.os.Bundle;


public class AppConfig {
    private static final String INTELLIGENCE_APP_ID = "INTELLIGENCE_APP_ID";
    private static final String INTELLIGENCE_APP_LOGGING_LEVEL = "INTELLIGENCE_APP_LOGGING_LEVEL";
    private static final String ENROLLMENT_USERNAME = "ENROLLMENT_USERNAME";
    private static final String URL = "URL";

    private String intelligenceAppId;
    private String intelligenceAppLoggingLevel;
    private String enrollmentUsername;
    private String url;


    public AppConfig(Context context) {
        RestrictionsManager restrictionsManager = (RestrictionsManager) context.getSystemService(Context.RESTRICTIONS_SERVICE);
        Bundle appRestrictions = restrictionsManager.getApplicationRestrictions();
        getApplicationRestrictions(appRestrictions);
    }

    private void getApplicationRestrictions(Bundle appRestrictions) {
        if (appRestrictions.containsKey(INTELLIGENCE_APP_ID)) {
            intelligenceAppId = appRestrictions.getString(INTELLIGENCE_APP_ID);
        }

        if (appRestrictions.containsKey(INTELLIGENCE_APP_LOGGING_LEVEL)) {
            intelligenceAppLoggingLevel = appRestrictions.getString(INTELLIGENCE_APP_LOGGING_LEVEL);
        }

        if (appRestrictions.containsKey(ENROLLMENT_USERNAME)) {
            enrollmentUsername = appRestrictions.getString(ENROLLMENT_USERNAME);
        }

        if (appRestrictions.containsKey(URL)) {
            url = appRestrictions.getString(URL);
        }
    }

    public String getIntelligenceAppId() {
        return intelligenceAppId;
    }

    public String getIntelligenceAppLoggingLevel() {
        return intelligenceAppLoggingLevel;
    }

    public String getEnrollmentUsername() {
        return enrollmentUsername;
    }

    public String getUrl() {
        return url;
    }
}
