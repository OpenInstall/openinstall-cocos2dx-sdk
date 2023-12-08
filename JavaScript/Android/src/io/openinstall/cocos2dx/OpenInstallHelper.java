package io.openinstall.cocos2dx;

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

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Wenki on 2018/6/26.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static volatile boolean initialized = false;
    private static boolean registerWakeup = false;
    private static AppData wakeupDataHolder = null;
    private static Intent wakeupIntent = null;
    private static Configuration configuration = null;

    private static String REQUIRE_OPENINSTALL = "var openinstall = require(\"OpenInstall\");";
    //高版本Cocos Creator构建的项目请使用此语句
    //private static String REQUIRE_OPENINSTALL = "var openinstall = window.__require(\"OpenInstall\");";
    private final static String CALLBACK_PATTERN = "openinstall.%s(%s);";
    private final static String CALLBACK_INSTALL = "_installCallback";
    private final static String CALLBACK_WAKEUP = "_wakeupCallback";
    private final static String CALLBACK_SHARE = "_shareCallback";

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

    private static boolean hasTrue(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.optBoolean(key, false);
        }
        return false;
    }

    public static void preInit() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.preInit(Cocos2dxActivity.getContext());
            }
        });
    }

    public static void config(final String jsonStr) {
        Log.d(TAG, "config jsonStr=" + jsonStr);
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    Configuration.Builder builder = new Configuration.Builder();
                    if (hasTrue(jsonObject, "adEnabled")) {
                        builder.adEnabled(true);
                    }
                    if (jsonObject.has("oaid")) {
                        builder.oaid(jsonObject.optString("oaid"));
                    }
                    if (jsonObject.has("gaid")) {
                        builder.gaid(jsonObject.optString("gaid"));
                    }
                    if (hasTrue(jsonObject, "imeiDisabled")) {
                        builder.imeiDisabled();
                    }
                    if (jsonObject.has("imei")) {
                        builder.imei(jsonObject.optString("imei"));
                    }
                    if (hasTrue(jsonObject, "macDisabled")) {
                        builder.macDisabled();
                    }
                    if (jsonObject.has("macAddress")) {
                        builder.macAddress(jsonObject.optString("macAddress"));
                    }
                    if (jsonObject.has("androidId")) {
                        builder.androidId(jsonObject.optString("androidId"));
                    }
                    if (jsonObject.has("serialNumber")) {
                        builder.serialNumber(jsonObject.optString("serialNumber"));
                    }
                    if (hasTrue(jsonObject, "simulatorDisabled")) {
                        builder.simulatorDisabled();
                    }
                    if (hasTrue(jsonObject, "storageDisabled")) {
                        builder.storageDisabled();
                    }
                    configuration = builder.build();
                } catch (JSONException e) {
//                throw new RuntimeException(e);
                    Log.e(TAG, "config parse error");
                }
            }

        });

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
                OpenInstall.getInstall(new AppInstallListener() {
                    @Override
                    public void onInstallFinish(AppData appData, Error error) {
                        boolean shouldRetry = error != null && error.shouldRetry();
                        final JSONObject jsonObject = toJson(appData);
                        putRetry(jsonObject, error != null && error.shouldRetry());
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_INSTALL, jsonObject.toString());
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
                    public void onInstall(AppData appData, boolean shouldRetry) {
                        final JSONObject jsonObject = toJson(appData);
                        putRetry(jsonObject, shouldRetry);
                        // 废弃 retry
                        try {
                            jsonObject.put("retry", shouldRetry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_INSTALL, jsonObject.toString());
                            }
                        });
                    }
                }, s);
            }
        });
    }

    public static void registerWakeup() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "registerWakeup");
                registerWakeup = true;
                if (wakeupDataHolder != null) {
                    sendWakeup(wakeupDataHolder);
                    wakeupDataHolder = null;
                }
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
                        if (appData == null) {
                            wakeupDataHolder = new AppData();
                        } else {
                            wakeupDataHolder = appData;
                        }
                    }

                }
            });
        } else {
            wakeupIntent = intent;
        }
    }

    private static void sendWakeup(AppData appData) {
        if (appData == null) {
            appData = new AppData();
        }
        final JSONObject jsonObject = toJson(appData);
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                callback(CALLBACK_WAKEUP, jsonObject.toString());
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

    // js 那边函数签名不能设置为 J -> long ，所以修改为 I -> int
    public static void reportEffectPoint(final String pointId, final int pointValue, final String extraJson) {
        Log.d(TAG, "reportEffectPoint " + extraJson);
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> extraMap = new HashMap<>();
                // extraJson to Map
                try {
                    JSONObject jsonObject = new JSONObject(extraJson);
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = jsonObject.optString(key);
                        extraMap.put(key, value);
                    }
                } catch (JSONException e) {
                    // throw new RuntimeException(e);
                    Log.e(TAG, "reportEffectPoint parse error");
                }

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
                    public void onResult(Void ignore, Error error) {
                        final JSONObject jsonObject = new JSONObject();
                        putRetry(jsonObject, error != null && error.shouldRetry());
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_SHARE, jsonObject.toString());
                            }
                        });
                    }
                });
            }
        });
    }

    private static JSONObject toJson(AppData appData) {
        JSONObject jsonObject = new JSONObject();
        if (appData == null) return jsonObject;
        try {
            jsonObject.put("channelCode", appData.getChannel());
            jsonObject.put("bindData", appData.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static void putRetry(JSONObject jsonObject, boolean shouldRetry) {
        try {
            jsonObject.put("shouldRetry", shouldRetry);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void callback(String method, String data) {
        String evalStr = REQUIRE_OPENINSTALL + String.format(CALLBACK_PATTERN, method, data);
        //    String evalStr = String.format(CALLBACK_PATTERN, method, data);
        Log.d(TAG, evalStr);
        Cocos2dxJavascriptJavaBridge.evalString(evalStr);
    }

}
