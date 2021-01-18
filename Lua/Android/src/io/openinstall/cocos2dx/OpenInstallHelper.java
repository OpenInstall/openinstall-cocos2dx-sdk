package io.openinstall.cocos2dx;

import android.content.Intent;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wenki on 2018/2/13.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static boolean registerWakeup = false;
    private static boolean callInit = false;
    private static boolean initialized = false;
    private static String wakeupDataHolder = null;
    private static Intent wakeupIntent = null;
    private static int wakeUpLuaFunc = -1;
    private static Configuration configuration = null;

    public static void config(boolean adEnabled, String oaid, String gaid) {
        Configuration.Builder builder = new Configuration.Builder();
        builder.adEnabled(adEnabled);
        oaid = setNull(oaid);
        builder.oaid(oaid);
        gaid = setNull(gaid);
        builder.gaid(gaid);
        Log.d(TAG, String.format("Configuration : adEnabled = %b, oaid = %s, gaid = %s",
                adEnabled, oaid == null ? "NULL" : oaid, gaid == null ? "NULL" : gaid));
        configuration = builder.build();
    }

    private static String setNull(String res) {
        // 传入 null 或者 未定义，设置为 null
        if (res == null || res.equalsIgnoreCase("null")
                || res.equalsIgnoreCase("nil")) {
            return null;
        }
        return res;
    }

    public static void init(final boolean permission, final Cocos2dxActivity activity) {
        Log.d(TAG, "permission = " + permission);
        callInit = true;
        if (permission) {
            OpenInstall.initWithPermission(activity, configuration, new Runnable() {
                @Override
                public void run() {
                    initialized(activity);
                }
            });
        } else {
            OpenInstall.init(activity);
            initialized(activity);
        }

    }

    private static void initialized(final Cocos2dxActivity activity) {
        initialized = true;
        if (wakeupIntent != null) {
            OpenInstall.getWakeUp(wakeupIntent, new AppWakeUpAdapter() {
                @Override
                public void onWakeUp(AppData appData) {
                    wakeupIntent = null;
                    final String json = toJson(appData);
                    Log.d(TAG, json);
                    if (!registerWakeup) {
                        Log.d(TAG, "wakeupCallback not register , wakeupData = " + json);
                        wakeupDataHolder = json;
                        return;
                    }
                    activity.runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json);
                        }
                    });
                }
            });
        }
    }

    public static void getInstall(int s, final int luaFunc, final Cocos2dxActivity cocos2dxActivity) {
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                final String json = toJson(appData);
                Log.d(TAG, "installData = " + json);
                cocos2dxActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, json);
                        Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
                    }
                });
            }
        }, s);
    }

    public static void getWakeup(Intent intent, final Cocos2dxActivity cocos2dxActivity) {
        if (initialized) {
            OpenInstall.getWakeUp(intent, new AppWakeUpAdapter() {
                @Override
                public void onWakeUp(AppData appData) {
                    final String json = toJson(appData);
                    Log.d(TAG, json);
                    if (!registerWakeup) {
                        Log.d(TAG, "wakeupCallback not register , wakeupData = " + json);
                        wakeupDataHolder = json;
                        return;
                    }
                    cocos2dxActivity.runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json);
                        }
                    });

                }
            });
        } else {
            wakeupIntent = intent;
        }
    }

    public static void registerWakeupCallback(final int luaFunc, final Cocos2dxActivity cocos2dxActivity) {
        wakeUpLuaFunc = luaFunc;
        if (!callInit) {
            Log.d(TAG, "未调用 init，插件使用默认配置初始化");
            init(false, cocos2dxActivity);
        }
        registerWakeup = true;
        if (wakeupDataHolder != null) {
            Log.d(TAG, "wakeupDataHolder = " + wakeupDataHolder);
            cocos2dxActivity.runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, wakeupDataHolder);
                    wakeupDataHolder = null;
                }
            });
        }
    }

    private static String toJson(AppData appData) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("channelCode", appData.getChannel());
            jsonObject.put("bindData", appData.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
