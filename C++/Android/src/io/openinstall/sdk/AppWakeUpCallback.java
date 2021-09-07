package io.openinstall.sdk;

import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

/**
 * Created by Wenki on 2018/5/28.
 */
public class AppWakeUpCallback extends AppWakeUpAdapter {

    native void wakeup(AppData appData);

    @Override
    public void onWakeUp(final AppData appData) {

        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
            @Override
            public void run() {
                wakeup(appData);
            }
        });

    }
}
