package io.openinstall.cocos2dx;

import android.content.Intent;
import android.util.Log;

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
    private static boolean isRegister = false;
    private static String wakeupDataHolder = null;
    private static int wakeUpLuaFunc = -1;

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
        }, s * 1000);
    }

    public static void getWakeup(Intent intent) {
        OpenInstall.getWakeUp(intent, new AppWakeUpAdapter() {
            @Override
            public void onWakeUp(AppData appData) {
                final String json = toJson(appData);
                Log.d(TAG, json);
                if (!isRegister) {
                    Log.d(TAG, "wakeupCallback not register , wakeupData = " + json);
                    wakeupDataHolder = json;
                    return;
                }
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(wakeUpLuaFunc, json);
            }
        });
    }

    public static void registerWakeupCallback(final int luaFunc, final Cocos2dxActivity cocos2dxActivity) {
        isRegister = true;
        wakeUpLuaFunc = luaFunc;
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
