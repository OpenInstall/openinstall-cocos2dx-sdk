package io.openinstall.cocos2dx;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

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
    private static boolean registerWakeup = false;
    private static volatile boolean initialized = false;
    private static String wakeupDataHolder = null;
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
                    runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            callback(CALLBACK_WAKEUP, json);
                        }
                    });

                }
            });
        }
    }

    public static void getInstall(final int s) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(new AppInstallAdapter() {
                    @Override
                    public void onInstall(AppData appData) {
                        final String json = toJson(appData);
                        Log.d(TAG, "installData = " + json);
                        runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                callback(CALLBACK_INSTALL, json);
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
                registerWakeup = true;
                if (wakeupDataHolder != null) {
                    Log.d(TAG, "wakeupDataHolder = " + wakeupDataHolder);
                    runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            callback(CALLBACK_WAKEUP, wakeupDataHolder);
                            wakeupDataHolder = null;
                        }
                    });

                }
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

    /**
     * 应用被拉起时，将调用此方法获取拉起参数
     * 当 openinstall 未初始化时，将保存 intent，待初始化后再使用
     *
     * @param intent
     */
    public static void getWakeup(Intent intent) {
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
                    runOnGLThread(new Runnable() {
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
        String evalStr = REQUIRE_OPENINSTALL + String.format(CALLBACK_PATTERN, method, data);
//        String evalStr = String.format(CALLBACK_PATTERN, method, data);
        Log.d(TAG, evalStr);
        Cocos2dxJavascriptJavaBridge.evalString(evalStr);
    }

}
