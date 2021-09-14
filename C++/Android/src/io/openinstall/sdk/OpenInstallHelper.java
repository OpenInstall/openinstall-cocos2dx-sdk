package io.openinstall.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;

/**
 * Created by Wenki on 2020/7/3.
 */
public class OpenInstallHelper {

    public static final String TAG = "OpenInstallHelper";
    private final static AppWakeUpCallback wakeUpCallback = new AppWakeUpCallback();
    private final static AppInstallCallback installCallback = new AppInstallCallback();
    private static volatile boolean initialized = false;
    private static Intent wakeUpIntent = null;

    private static Configuration configuration = null;

    private static final Handler UIHandler = new Handler(Looper.getMainLooper());

    // 防止初始化在 GLThread 调用，导致代码运行顺序不一致
    private static void runOnUIThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            UIHandler.post(action);
        }
    }

    public static void config(final boolean adEnabled, final String oaid, final String gaid,
                              final boolean imeiDisabled, final boolean macDisabled) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Configuration.Builder builder = new Configuration.Builder();
                builder.adEnabled(adEnabled);
                builder.oaid(oaid);
                builder.gaid(gaid);
                if (imeiDisabled) {
                    builder.imeiDisabled();
                }
                if (macDisabled) {
                    builder.macDisabled();
                }
                configuration = builder.build();

                Log.d(TAG, String.format("Configuration : adEnabled = %b, oaid = %s, gaid = %s, " +
                                "imeiDisabled = %s, macDisabled = %s",
                        configuration.isAdEnabled(),
                        configuration.getOaid(), configuration.getGaid(),
                        configuration.isImeiDisabled(), configuration.isMacDisabled()));
            }
        });
    }

    public static void init(final Context context) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.init(context, configuration);
                initialized = true;
                if (wakeUpIntent != null) {
                    Intent intent = wakeUpIntent;
                    wakeUpIntent = null;
                    OpenInstall.getWakeUp(intent, wakeUpCallback);
                }
            }
        });
    }

    public static void wakeup(final Intent intent) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (initialized) {
                    OpenInstall.getWakeUp(intent, wakeUpCallback);
                } else {
                    wakeUpIntent = intent;
                }
            }
        });
    }

    public static void getInstall(final int second) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(installCallback, second);
            }
        });
    }

    public static void reportRegister() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.reportRegister();
            }
        });
    }

    public static void reportEffectPoint(final String pointId, final long pointValue) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.reportEffectPoint(pointId, pointValue);
            }
        });
    }

}
