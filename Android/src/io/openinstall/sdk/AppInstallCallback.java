package io.openinstall.sdk;

import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;

/**
 * Created by Wenki on 2018/5/28.
 */
public class AppInstallCallback extends AppInstallAdapter {

    native void install(AppData appData);

    @Override
    public void onInstall(AppData appData) {
        install(appData);
    }
}
