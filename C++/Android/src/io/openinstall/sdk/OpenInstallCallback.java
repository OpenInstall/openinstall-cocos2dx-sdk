package io.openinstall.sdk;

import com.fm.openinstall.model.AppData;

/**
 * Created by Wenki on 2021/01/05.
 */
public class OpenInstallCallback {

    // getInstall
    native void install(AppData appData);

    // getInstallCanRetry
    native void installRetry(AppData appData, boolean retry);

    // getWakeup
    native void wakeup(AppData appData);

}
