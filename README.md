# Android Web Demo

## Synposis

App shows differences in native single sign-on (SSO) user experience between WebViews and Chrome Custom Tabs.  Further nuances with SSO user experience can be demonstrated when the app is enabled for VMware's Mobile SSO solution using Workspace ONE and the VMware Tunnel app for Android.  Lastly, the app demonstrates integration with VMware Workspace ONE Intellgience SDK for Consumer Apps for remote crash, app usage, app logging, and network stats reporting.

## Description

Android Web Demo allows a user, IT administrator, or developer to open a default or managed config (app config) provided URL using a WebView or Chrome Custom Tab instance.  The app allows you to demonstrate first-hand the differences in how session and persitence cookies are managed between the two libraries and how this impacts the single sign-on (SSO) user experience within the app itself and other apps on the Android device.  The app further demonstratos how gaps in the SSO user experience can be bridged using VMware's Mobile SSO solution for Android and how this solution can benefit any third-party developed or custom-built Android app that uses WebViews or Custom Tabs without requiring any code changes.  Finally, developers and administrators can use this app to demonstrate how easily an Android app can be integrated with VMware Workspace ONE Intelligence for app/user usage dashboarding and reporting of remotely collected app crashes, handled exceptions, app logs, user breadcrumbs, user flow tracing, and network stat insights.

### Main Activity - Android Web Demo
![Main_Activity](/assets/images/main_activity_25.png)

---

## Getting Started

### Simple Test of WebView and Custom Tabs SSO User Experience

If you're simply looking to test the cookie sharing behavior with WebView and Custom Tabs enabled apps, then...

1. Download the two .apk files from the APKs folder directly to your Android test device and then install the downloaded .apk files.
2. Download the files to your computer and sideload install them to your test device using adb.
3. Download this code repo, run gradle sync, and then run the app to build and install the .apk on your test device or emulator.  Repeat this step after changing the app ID to create a second build of the app on the same device or emulator.

###  Compare SSO User Experience of WebView and Custom Tabs with VMware's Mobile SSO (MSSO)

If you have MSSO working with your Workspace ONE UEM and Workspace ONE Access tenants, then you can enable the two apps for MSSO to see how the user experience differs from the native SSO experience.

1. Upload the two .apk files to Workspace ONE UEM as Internal Apps.
2. Enable each app for Android Tunnel.
3. Install the apps on your test device using WS1 UEM.

### Test VMware Workspace ONE Intelligence SDK

If you have a VMware Workspace ONE Intelligence tenant with Dashboarding or a tenant with an active trial, then...

1. Log into the Intelligence admin portal and create an app ID for the two .apk file package IDs you downloaded (e.g., com.aron.demo1 and com.aron.demo2) or whatever you set your own package IDs to.
2. Create an Android profile in Workspace ONE UEM, add a CUSTOM SETTINGS payload, and then copy the following to the payload after adding your values to the XML:
```
<characteristic type="com.airwatch.android.androidwork.app:ENTER_APP_1_PACKAGE_ID_HERE" uuid="1abc235c-03b6-4ea7-a94c-079f5a8bc123">
    <parm name="INTELLIGENCE_APP_ID" value="ENTER_INTELLIENCE_APP_1_ID_HERE" />
    <parm name="INTELLIGENCE_APP_LOGGING_LEVEL" value="DEBUG" />
    <parm name="URL" value="ENTER_URL_HERE" />
    <parm name="ENROLLMENT_USERNAME" value="{EnrollmentUser}" />
</characteristic>
```
3. Add a second CUSTOM SETTINGS payload for the second app and then copy to the payload afer adding your values for the second app:
```
<characteristic type="com.airwatch.android.androidwork.app:ENTER_APP_2_PACKAGE_ID_HERE" uuid="1abc235c-03b6-4ea7-a94c-079f5a8bc456">
    <parm name="INTELLIGENCE_APP_ID" value="ENTER_INTELLIENCE_APP_2_ID_HERE" />
    <parm name="INTELLIGENCE_APP_LOGGING_LEVEL" value="DEBUG" />
    <parm name="URL" value="ENTER_URL_HERE" />
    <parm name="ENROLLMENT_USERNAME" value="{EnrollmentUser}" />
</characteristic>
```
4. Save and install the profile to your test device.


---

## Example App Logs
The app will generate the following logs which can be captured by remotely requesting device system logs through UEM or using `adb logcat`:

```
I WebDemo : App crashed on previous run:  false
I WebDemo : Opening Web View for URL https://www.vmware.com
I WebDemo : App crashed on previous run:  false
I WebDemo : App crashed on previous run:  false
I WebDemo : Opening Chrome Custom Tab for URL https://www.vmware.com
I WebDemo : App crashed on previous run:  false
I WebDemo : Opening Crash Activity
I WebDemo : Crash Acitivty started.
I WebDemo : Causing NullPointerException.
I WebDemo : NullPointerException handled.
W WebDemo : Causing fatal app crash due to ArrayIndexOutOfBoundsException exception.
I WebDemo : Intelligence App ID App Config:  YOUR_INTELLIENCE_APP_ID
I WebDemo : Intelligence App Logging Level App Config:  DEBUG
I WebDemo : WS1 UEM Enrolled User Username App Config:  aaperauch
I WebDemo : Intelligence SDK Opt-Out Status: false
I WebDemo : Intelligence SDK Logcat Reporting Enabled: true
I WebDemo : Intelligence SDK Service Monitoring Enabled: false
I WebDemo : App crashed on previous run:  true
I WebDemo : Starting URL App Config:  https://www.vmware.com
I WebDemo-MainActivity: Found package that supports Chrome Custom Tabs:  com.android.chrome
I WebDemo : App crashed on previous run:  true
```

---

## Additional Information

For additional information about working with web content for Android apps or building enterprise ready Android web apps, please see the following:

* Android Web Apps and the WebView Library:
    * https://developer.android.com/guide/webapps
* Android Chrome Custom Tabs:
    * https://developer.chrome.com/docs/android/custom-tabs/
* Android Managed Configurations (App Configs):
    * https://developer.android.com/work/managed-configurations
* Android Mobile SIngle Sign-On to VMware Workspace ONE:
    * https://docs.vmware.com/en/VMware-Workspace-ONE/services/WS1_android_sso_config/GUID-1E5128A5-1394-4A50-8098-947780E38166.html
* VMware Worskpace ONE Intelligence:
    * https://www.vmware.com/products/workspace-one/intelligence.html
* VMware Worksapce ONE Intelligence SDK for Consumer Apps: 
    * https://www.vmware.com/products/workspace-one/intelligence-consumer-apps.html
    * https://docs.vmware.com/en/VMware-Workspace-ONE/services/intelligence-documentation/GUID-48_consumerapps_intro.html
* VMware Worksapce ONE Intelligence SDK for Consumer Apps Developer Docs:
    * https://developer.vmware.com/web/workspace-one-intelligence/docs
---

## Notes

* Version:        1.2
* Creation Date:  03/28/2022
* Author:         Aron Aperauch - aaperauch@vmware.com
* Purpose/Change: Initial Release
