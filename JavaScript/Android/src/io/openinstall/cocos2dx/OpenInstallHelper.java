package io.openinstall.cocos2dx;

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

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wenki on 2018/6/26.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static volatile boolean initialized = false;
    private static boolean registerWakeup = false;
    private static boolean alwaysCallback = false;
    private static AppData wakeupDataHolder = null;
    private static Intent wakeupIntent = null;
    private static Configuration configuration = null;

    private static String REQUIRE_OPENINSTALL = "var openinstall = require(\"OpenInstall\");";
    //高版本Cocos Creator构建的项目请使用此语句
    //private static String REQUIRE_OPENINSTALL = "var openinstall = window.__require(\"OpenInstall\");";
    private final static String CALLBACK_PATTERN = "openinstall.%s(%s);";
    private final static String CALLBACK_INSTALL = "_installCallback";
    private final static String CALLBACK_WAKEUP = "_wakeupCallback";

    private static final Handler UIHandler = new Handler(Looper.getMainLooper());

    // 防止初始化在 GLThread 调用，导致代码运行顺序不一致
    private static void runOnUIThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            UIHandler.post(action);
        }
    }

    private static void runOnGLThread(Runnable action) {
        Cocos2dxGLSurfaceView.getInstance().queueEvent(action);
    }

    public static void config(final boolean adEnabled, final String oaid, final String gaid,
                              final boolean macDisabled, final boolean imeiDisabled) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Configuration.Builder builder = new Configuration.Builder();
                builder.adEnabled(adEnabled);
                builder.oaid(checkNull(oaid));
                builder.gaid(checkNull(gaid));
                if (macDisabled) {
                    builder.macDisabled();
                }
                if (imeiDisabled) {
                    builder.imeiDisabled();
                }
                configuration = builder.build();
                Log.d(TAG, String.format("adEnabled = %s, oaid = %s, gaid = %s, " +
                                "macDisabled = %s, imeiDisabled = %s",
                        configuration.isAdEnabled(), configuration.getOaid(), configuration.getGaid(),
                        configuration.isMacDisabled(), configuration.isImeiDisabled()));
            }
        });

    }

    private static String checkNull(String res) {
        // 传入 null 或者 未定义，设置为 null
        if (res == null || res.equalsIgnoreCase("null")
                || res.equalsIgnoreCase("undefined")) {
            return null;
        }
        return res;
    }

    public static void init() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.init(Cocos2dxActivity.getContext(), configuration);
                initialized();
            }
        });
    }

    private static void initialized() {
        initialized = true;
        if (wakeupIntent != null) {
            getWakeup(wakeupIntent);
            wakeupIntent = null;
        }
    }

    public static void getInstall(final int s) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(new AppInstallAdapter() {
                    @Override
                    public void onInstall(AppData appData) {
                        final JSONObject json = toJson(appData);
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_INSTALL, json.toString());
                            }
                        });
                    }
                }, s);
            }
        });
    }

    public static void getInstallCanRetry(final int s) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstallCanRetry(new AppInstallRetryAdapter() {
                    @Override
                    public void onInstall(AppData appData, boolean retry) {
                        final JSONObject json = toJson(appData);
                        try {
                            json.put("retry", retry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_INSTALL, json.toString());
                            }
                        });
                    }
                }, s);
            }
        });
    }

    public static void registerWakeup(final boolean always) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "registerWakeup alwaysCallback=" + always);
                registerWakeup = true;
                alwaysCallback = always;
                sendWakeup(wakeupDataHolder);
                wakeupDataHolder = null;
            }
        });
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
            wakeupIntent = intent;
        }
    }

    private static void sendWakeup(AppData appData) {
        if (appData != null || alwaysCallback) {
            if (appData == null) {
                appData = new AppData();
            }
            final JSONObject json = toJson(appData);
            runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    callback(CALLBACK_WAKEUP, json.toString());
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

    private static JSONObject toJson(AppData appData) {
        JSONObject jsonObject = new JSONObject();
        if (appData != null) {
            try {
                jsonObject.put("channelCode", appData.getChannel());
                jsonObject.put("bindData", appData.getData());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private static void callback(String method, String data) {
        String evalStr = REQUIRE_OPENINSTALL + String.format(CALLBACK_PATTERN, method, data);
//        String evalStr = String.format(CALLBACK_PATTERN, method, data);
        Log.d(TAG, evalStr);
        Cocos2dxJavascriptJavaBridge.evalString(evalStr);
    }

}
