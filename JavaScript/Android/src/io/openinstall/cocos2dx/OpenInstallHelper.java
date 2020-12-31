package io.openinstall.cocos2dx;

import android.content.Intent;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wenki on 2018/6/26.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static boolean registerWakeup = false;
    private static volatile boolean callInit = false;
    private static volatile boolean initialized = false;
    private static String wakeupDataHolder = null;
    private static Intent wakeupIntent = null;

    private static String REQUIRE_OPENINSTALL = "var openinstall = require(\"OpenInstall\");";
    //高版本Cocos Creator构建的项目请使用此语句
    //private static String REQUIRE_OPENINSTALL = "var openinstall = window.__require(\"OpenInstall\");";
    private static String CALLBACK_PATTERN = "openinstall.%s(%s);";
    private static String CALLBACK_INSTALL = "_installCallback";
    private static String CALLBACK_WAKEUP = "_wakeupCallback";

    /**
     * 插件调用openinstall初始化
     *
     * @param permission
     * @param activity
     * @param configuration
     */
    public static void init(boolean permission, Cocos2dxActivity activity, Configuration configuration) {
        callInit = true;
        if (permission) {
            // openinstall 请求 READ_PHONE_STATE 权限，获取 IMEI
            OpenInstall.initWithPermission(activity, configuration);
        } else {
            OpenInstall.init(activity, configuration);
            initialized(activity);
        }
    }

    /**
     * OpenInstall 已经调用了初始化
     *
     * @param activity
     */
    public static void initialized(final Cocos2dxActivity activity) {
        initialized = true;
        if (wakeupIntent != null) {
            OpenInstall.getWakeUp(wakeupIntent, new AppWakeUpAdapter() {
                @Override
                public void onWakeUp(AppData appData) {
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
                            callback(CALLBACK_WAKEUP, json);
                        }
                    });
                    wakeupIntent = null;
                }
            });
        }
    }

    /**
     * 获取安装参数，不考虑未初始化的情况
     *
     * @param s
     * @param cocos2dxActivity
     */
    public static void getInstall(int s, final Cocos2dxActivity cocos2dxActivity) {
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                final String json = toJson(appData);
                Log.d(TAG, "installData = " + json);
                cocos2dxActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        callback(CALLBACK_INSTALL, json);
                    }
                });
            }
        }, s);
    }

    /**
     * 应用被拉起时，将调用此方法获取拉起参数，当 openinstall 为初始化时，将保存 intent，待初始化后再使用
     *
     * @param intent
     * @param cocos2dxActivity
     */
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
                            callback(CALLBACK_WAKEUP, json);
                        }
                    });
                }
            });
        } else {
            wakeupIntent = intent;
        }
    }

    /**
     * js 调用此方法，注册拉起回调
     *
     * @param cocos2dxActivity
     */
    public static void registerWakeupCallback(final Cocos2dxActivity cocos2dxActivity) {
        if (!callInit) {
            Log.d(TAG, "未调用 init，插件使用默认配置初始化");
            init(false, cocos2dxActivity, null);
        }
        registerWakeup = true;
        if (wakeupDataHolder != null) {
            Log.d(TAG, "wakeupDataHolder = " + wakeupDataHolder);
            cocos2dxActivity.runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    callback(CALLBACK_WAKEUP, wakeupDataHolder);
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

    private static void callback(String method, String data) {
//        String evalStr = REQUIRE_OPENINSTALL + String.format(CALLBACK_PATTERN, method, data);
        String evalStr = String.format(CALLBACK_PATTERN, method, data);
        Log.d(TAG, evalStr);
        Cocos2dxJavascriptJavaBridge.evalString(evalStr);
    }

}
