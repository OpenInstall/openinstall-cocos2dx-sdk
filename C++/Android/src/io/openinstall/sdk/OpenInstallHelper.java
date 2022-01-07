package io.openinstall.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppInstallRetryAdapter;
import com.fm.openinstall.listener.AppWakeUpListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

/**
 * Created by Wenki on 2020/7/3.
 */
public class OpenInstallHelper {

    public static final String TAG = "OpenInstallHelper";
    private final static OpenInstallCallback callback = new OpenInstallCallback();
    private static boolean initialized = false;
    private static boolean registerWakeup = false;
    private static boolean alwaysCallback = false;
    private static Intent wakeUpIntent = null;
    private static AppData wakeupDataHolder = null;

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
                    getWakeup(wakeUpIntent);
                    wakeUpIntent = null;
                }
            }
        });
    }

    /**
     * 移除，保留一段时间
     */
    @Deprecated
    public static void wakeup(Intent intent) {
        getWakeup(intent);
    }

    public static void getWakeup(Intent intent) {
        if (initialized) {
            OpenInstall.getWakeUpAlwaysCallback(intent, new AppWakeUpListener() {
                @Override
                public void onWakeUpFinish(AppData appData, Error error) {
                    if (error != null) {
                        Log.d(TAG, "getWakeUpAlwaysCallback " + error.toString());
                    }
                    if (registerWakeup) {
                        sendWakeup(appData);
                    } else {
                        wakeupDataHolder = appData;
                    }
                }
            });
        } else {
            wakeUpIntent = intent;
        }
    }

    public static void getInstall(final int second) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(new AppInstallAdapter() {
                    @Override
                    public void onInstall(final AppData appData) {
                        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                callback.install(appData);
                            }
                        });
                    }
                }, second);
            }
        });
    }

    public static void getInstallCanRetry(final int second) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstallCanRetry(new AppInstallRetryAdapter() {
                    @Override
                    public void onInstall(final AppData appData, final boolean retry) {
                        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                callback.installRetry(appData, retry);
                            }
                        });
                    }
                }, second);
            }
        });
    }

    public static void registerWakeup(final boolean always) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                alwaysCallback = always;
                registerWakeup = true;
                if (wakeupDataHolder != null) {
                    final AppData appData = wakeupDataHolder;
                    wakeupDataHolder = null;
                    sendWakeup(appData);
                }
            }
        });
    }

    private static void sendWakeup(AppData appData) {
        if (appData != null || alwaysCallback) {
            if (appData == null) {
                appData = new AppData();
            }
            final AppData finalAppData = appData;
            Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                @Override
                public void run() {
                    callback.wakeup(finalAppData);
                }
            });
        }
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
