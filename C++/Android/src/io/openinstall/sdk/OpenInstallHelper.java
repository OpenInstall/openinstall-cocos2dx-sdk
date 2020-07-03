package io.openinstall.sdk;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.fm.openinstall.OpenInstall;

/**
 * Created by Wenki on 2020/7/3.
 */
public class OpenInstallHelper {

    public static final String TAG = "OpenInstallHelper";
    private static AppWakeUpCallback wakeUpCallback = new AppWakeUpCallback();
    private static volatile boolean initialized = false;
    private static Intent wakeUpIntent = null;

    public static void init(Activity activity, boolean ad) {
        Log.d(TAG, "is Ad : " +ad);
        if (ad) {
            OpenInstall.initWithPermission(activity, new Runnable() {
                @Override
                public void run() {
                    initialized = true;
                    if (wakeUpIntent != null) {
                        Intent intent = wakeUpIntent;
                        wakeUpIntent = null;
                        OpenInstall.getWakeUp(intent, wakeUpCallback);
                    }
                }
            });
        } else {
            OpenInstall.init(activity);
            initialized = true;
        }

    }

    static void wakeup(Intent intent) {
        if (initialized) {
            OpenInstall.getWakeUp(intent, wakeUpCallback);
        } else {
            wakeUpIntent = intent;
        }
    }

    static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        OpenInstall.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
