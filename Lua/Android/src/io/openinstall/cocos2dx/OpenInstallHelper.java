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
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wenki on 2018/2/13.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static boolean registerWakeup = false;
    private static boolean initialized = false;
    private static String wakeupDataHolder = null;
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
                Log.d(TAG, String.format("Configuration : adEnabled = %b, oaid = %s, gaid = %s, " +
                                "macDisabled = %s, imeiDisabled = %s",
                        configuration.isAdEnabled(), configuration.getOaid(), configuration.getGaid(),
                        configuration.isMacDisabled(), configuration.isImeiDisabled()));
            }
        });
    }

    private static String checkNull(String res) {
        // 传入 null 或者 未定义，设置为 null
        if (res == null || res.equalsIgnoreCase("null")
                || res.equalsIgnoreCase("nil")) {
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
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json);
                        }
                    });
                }
            });
        }
    }

    public static void getInstall(final int s, final int luaFunc) {
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
                                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, json);
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
                    Log.d(TAG, "wakeupDataHolder = " + wakeupDataHolder);
                    runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, wakeupDataHolder);
                            wakeupDataHolder = null;
                        }
                    });
                }
            }
        });
    }

    protected static void getWakeup(Intent intent) {
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
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json);
                        }
                    });

                }
            });
        } else {
            wakeupIntent = intent;
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

    public static void reportEffectPoint(final String pointId, final int pointValue) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                OpenInstall.reportEffectPoint(pointId, pointValue);
            }
        });
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
