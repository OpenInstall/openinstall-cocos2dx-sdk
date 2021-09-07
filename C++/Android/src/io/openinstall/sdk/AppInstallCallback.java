package io.openinstall.sdk;

import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

/**
 * Created by Wenki on 2018/5/28.
 */
public class AppInstallCallback extends AppInstallAdapter {

    native void install(AppData appData);

    @Override
    public void onInstall(final AppData appData) {
        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
            @Override
            public void run() {
                install(appData);
            }
        });
    }
}
