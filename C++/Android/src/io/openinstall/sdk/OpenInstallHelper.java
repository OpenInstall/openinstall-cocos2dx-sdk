package io.openinstall.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wenki on 2020/7/3.
 */
public class OpenInstallHelper {

    public static final String TAG = "OpenInstallHelper";
    private final static AppWakeUpCallback wakeUpCallback = new AppWakeUpCallback();
    private final static AppInstallCallback installCallback = new AppInstallCallback();
    private static volatile boolean initialized = false;
    private static volatile boolean callInit = false;
    private static Intent wakeUpIntent = null;

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

    private static final List<Runnable> delayTaskList = new ArrayList<>();

    public static void config(final boolean adEnabled, final String oaid, final String gaid) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Configuration.Builder builder = new Configuration.Builder();
                builder.adEnabled(adEnabled);
                builder.oaid(oaid);
                builder.gaid(gaid);
                Log.d(TAG, String.format("Configuration : adEnabled = %b, oaid = %s, gaid = %s", adEnabled,
                        oaid == null ? "NULL" : oaid, gaid == null ? "NULL" : gaid));
                configuration = builder.build();
            }
        });
    }

    public static void init(final Activity activity, final boolean permission) {
        Log.d(TAG, "permission = " + permission);
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                callInit = true;
                if (permission) {
                    OpenInstall.initWithPermission(activity, configuration, new Runnable() {
                        @Override
                        public void run() {
                            initialized = true;
                            if (wakeUpIntent != null) {
                                Intent intent = wakeUpIntent;
                                wakeUpIntent = null;
                                OpenInstall.getWakeUp(intent, wakeUpCallback);
                            }
                            for (Runnable task : delayTaskList) {
                                task.run();
                            }
                        }
                    });
                } else {
                    OpenInstall.init(activity, configuration);
                    initialized = true;
                }
            }
        });
    }

    static void wakeup(final Intent intent) {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                if (initialized) {
                    OpenInstall.getWakeUp(intent, wakeUpCallback);
                } else {
                    wakeUpIntent = intent;
                }
            }
        };

        delayTask(action);
    }

    static void getInstall(final int second) {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                OpenInstall.getInstall(installCallback, second);
            }
        };

        delayTask(action);
    }

    private static void delayTask(final Runnable task) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "callInit = " + callInit + ", initialized = " + initialized);
                // 如果调用了初始化，但是还没有初始化完成，先将任务存下延后调用
                if (callInit && !initialized) {
                    delayTaskList.add(task);
                } else {
                    task.run();
                }
            }
        });
    }

    static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        OpenInstall.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
