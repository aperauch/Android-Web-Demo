package com.aron.webdemo;

import static com.aron.webdemo.constants.Constants.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crittercism.app.Crittercism;


import java.util.Objects;

public class CrashActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean usingIntelligenceSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        Button fatalCrashButton = (Button) findViewById(R.id.fatal_crash_button);
        fatalCrashButton.setOnClickListener(this);
        Button nonFatalCrashButton = (Button) findViewById(R.id.non_fatal_button);
        nonFatalCrashButton.setOnClickListener(this);

        usingIntelligenceSdk = getIntent().getBooleanExtra(EXTRA_USE_INTELLIGENCE_SDK, false);
        if (usingIntelligenceSdk) {
            Crittercism.leaveBreadcrumb("Crash_Activity_Started");
            Crittercism.beginUserFlow("Crash_Activity_User_Flow");
        }

        Log.i(TAG, "Crash Acitivty started.");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fatal_crash_button:
                onClickStartFatalCrash();
                break;
            case R.id.non_fatal_button:
                onClickStartNonFatalHandledException();
            default:
                break;
        }
    }

    private void onClickStartFatalCrash() {
        if (usingIntelligenceSdk) {
            Toast.makeText(this, "Crashing app now!", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Causing fatal app crash due to ArrayIndexOutOfBoundsException exception.");
            Crittercism.leaveBreadcrumb("Fatal_Crash_Started");
            Crittercism.endUserFlow("Crash_Activity_User_Flow");
            Crittercism.sendAppLoadData();

            // Cause array index out of bounds exception
            boolean[] array = {true, false};
            if (array[99]) {
                Log.e(TAG, "Array index out of bounds exception should have occurred.");
            }
        }
        else {
            Toast.makeText(this, "ERROR:  The Intelligence SDK is not configured.  Skipping app crash.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Intelligence SDK is not configured.");
        }
    }

    private void onClickStartNonFatalHandledException() {
        try {
            // Cause and then handle a Null Pointer Exception
            Log.i(TAG, "Causing NullPointerException.");
            Object nullObject = null;
            Objects.requireNonNull(nullObject);
        }
        catch(java.lang.NullPointerException nullPointerException) {
            if (usingIntelligenceSdk) {
                Toast.makeText(this, "Handling NullPointerException now!", Toast.LENGTH_SHORT).show();
                Crittercism.leaveBreadcrumb("Non-Fatal_Crash_Occurred");
                Crittercism.endUserFlow("Crash_Activity_User_Flow");
                Crittercism.logHandledException(nullPointerException);
                Crittercism.sendAppLoadData();
                Log.i(TAG, "NullPointerException handled.");
            }
            else {
                Toast.makeText(this, "ERROR:  The Intelligence SDK is not configured.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Intelligence SDK is not configured.");
            }
        }
    }
}