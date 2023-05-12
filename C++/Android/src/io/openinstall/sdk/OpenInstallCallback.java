package io.openinstall.sdk;

import com.fm.openinstall.model.AppData;

/**
 * Created by Wenki on 2021/01/05.
 */
public class OpenInstallCallback {

    native void install(AppData appData, boolean shouldRetry);

    native void wakeup(AppData appData);

    native void onResult(String method, Object obj, boolean shouldRetry, String message);

}
