package io.openinstall.cocos2dx;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
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
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wenki on 2018/2/13.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static boolean initialized = false;
    private static boolean registerWakeup = false;

    private static AppData wakeupDataHolder = null;
    private static Intent wakeupIntent = null;
    private static int wakeUpLuaFunc = -1;

    private static Configuration configuration = null;

    private static final Handler uiHandler = new Handler(Looper.getMainLooper());

    // 防止初始化在 GLThread 调用，导致代码运行顺序不一致
    private static void runOnUIThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            uiHandler.post(action);
        }
    }

    private static void runOnGLThread(Runnable action) {
        Cocos2dxGLSurfaceView.getInstance().queueEvent(action);
    }


    public static Map<String, String> parse(String params) {
        String[] paramArr = params.split("&");
        Map<String, String> map = new HashMap<String, String>(paramArr.length);
        for (int i = 0; i < paramArr.length; i++) {
            if (TextUtils.isEmpty(paramArr[i])) continue;
            String kv[] = paramArr[i].split("=");
            if (kv.length != 2) continue;
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    private static boolean hasTrue(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return Boolean.parseBoolean(map.get(key));
        }
        return false;
    }

    private static String getString(Map<String, String> map, String key) {
        String value = map.get(key);
        if (value == null || value.equalsIgnoreCase("null")
                || value.equalsIgnoreCase("nil")) {
            return null;
        }
        return value;
    }

    public static void preInit() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.preInit(Cocos2dxActivity.getContext());
            }
        });
    }

    public static void config(final String params) {
        Log.d(TAG, "config " + params);
        if (TextUtils.isEmpty(params)) {
            return;
        }
        final Map<String, String> paramMap = parse(params);
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
                    builder.imei(getString(paramMap, "imei"));
                }
                if (hasTrue(paramMap, "macDisabled")) {
                    builder.macDisabled();
                }
                if (paramMap.containsKey("macAddress")) {
                    builder.macAddress(getString(paramMap, "macAddress"));
                }
                if (paramMap.containsKey("androidId")) {
                    builder.androidId(getString(paramMap, "androidId"));
                }
                if (paramMap.containsKey("serialNumber")) {
                    builder.serialNumber(getString(paramMap, "serialNumber"));
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

    public static void getInstall(final int s, final int luaFunc) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(new AppInstallListener() {
                    @Override
                    public void onInstallFinish(AppData appData, Error error) {
                        boolean shouldRetry = error != null && error.shouldRetry();
                        final JSONObject json = toJson(appData);
                        putRetry(json, error != null && error.shouldRetry());
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, json.toString());
                                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
                            }
                        });
                    }
                }, s);
            }
        });
    }

    public static void getInstallCanRetry(final int s, final int luaFunc) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstallCanRetry(new AppInstallRetryAdapter() {
                    @Override
                    public void onInstall(AppData appData, boolean shouldRetry) {
                        final JSONObject json = toJson(appData);
                        putRetry(json, shouldRetry);
                        // 废弃 retry
                        try {
                            json.put("retry", shouldRetry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, json.toString());
                                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
                            }
                        });
                    }
                }, s);
            }
        });
    }

    public static void registerWakeupCallback(final int luaFunc) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                wakeUpLuaFunc = luaFunc;
                registerWakeup = true;
                if (wakeupDataHolder != null) {
                    sendWakeup(wakeupDataHolder);
                    wakeupDataHolder = null;
                }
            }
        });
    }

    protected static void getWakeup(Intent intent) {
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
        final JSONObject json = toJson(appData);
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json.toString());
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

    public static void reportEffectPoint(final String pointId, final int pointValue, final String extraParam) {
        Log.d(TAG, "reportEffectPoint " + extraParam);
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> extraMap = parse(extraParam);
                OpenInstall.reportEffectPoint(pointId, pointValue, extraMap);
            }
        });
    }

    public static void reportShare(final String shareCode, final String sharePlatform, final int luaFunc) {
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
                                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, jsonObject.toString());
                                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
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

}
