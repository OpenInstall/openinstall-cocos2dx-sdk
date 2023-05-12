package io.openinstall.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallListener;
import com.fm.openinstall.listener.AppInstallRetryAdapter;
import com.fm.openinstall.listener.AppWakeUpListener;
import com.fm.openinstall.listener.ResultCallback;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import java.util.HashMap;
import java.util.Map;

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

    private static boolean hasTrue(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return Boolean.parseBoolean(map.get(key));
        }
        return false;
    }

    public static void config(final Map<String, String> paramMap) {
        Log.d(TAG, paramMap.toString());
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Configuration.Builder builder = new Configuration.Builder();
                if (hasTrue(paramMap, "adEnabled")) {
                    builder.adEnabled(true);
                }
                if (paramMap.containsKey("oaid")) {
                    builder.oaid(paramMap.get("oaid"));
                }
                if (paramMap.containsKey("gaid")) {
                    builder.gaid(paramMap.get("gaid"));
                }
                if (hasTrue(paramMap, "imeiDisabled")) {
                    builder.imeiDisabled();
                }
                if (paramMap.containsKey("imei")) {
                    builder.imei(paramMap.get("imei"));
                }
                if (hasTrue(paramMap, "macDisabled")) {
                    builder.macDisabled();
                }
                if (paramMap.containsKey("macAddress")) {
                    builder.macAddress(paramMap.get("macAddress"));
                }
                if (paramMap.containsKey("androidId")) {
                    builder.androidId(paramMap.get("androidId"));
                }
                if (paramMap.containsKey("serialNumber")) {
                    builder.serialNumber(paramMap.get("serialNumber"));
                }
                if (hasTrue(paramMap, "simulatorDisabled")) {
                    builder.simulatorDisabled();
                }
                if (hasTrue(paramMap, "storageDisabled")) {
                    builder.storageDisabled();
                }
                configuration = builder.build();
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
                OpenInstall.getInstall(new AppInstallListener() {
                    @Override
                    public void onInstallFinish(final AppData appData, final Error error) {
                        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                boolean shouldRetry = error != null && error.shouldRetry();
                                callback.install(appData, shouldRetry);
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
                    public void onInstall(final AppData appData, final boolean shouldRetry) {
                        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                callback.install(appData, shouldRetry);
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

    public static void reportEffectPoint(final String pointId, final long pointValue, final Map<String, String> extraMap) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.reportEffectPoint(pointId, pointValue, extraMap);
            }
        });
    }

    public static void reportShare(final String shareCode, final String sharePlatform) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.reportShare(shareCode, sharePlatform, new ResultCallback<Void>() {
                    @Override
                    public void onResult(Void v, final Error error) {
                        if(error != null){
                            Log.d(TAG, "reportShare failed : " + error.getErrorMsg());
                        }
                        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                boolean shouldRetry = error != null && error.shouldRetry();
                                String message = error == null ? "" : error.getErrorMsg();
                                callback.onResult("share", null, shouldRetry, message);
                            }
                        });
                    }
                });
            }
        });
    }

}
