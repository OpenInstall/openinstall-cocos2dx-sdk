package io.openinstall.sdk;

import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

/**
 * Created by Wenki on 2018/5/28.
 */
public class AppWakeUpCallback extends AppWakeUpAdapter {

    native void wakeup(AppData appData);

    @Override
    public void onWakeUp(AppData appData) {
        wakeup(appData);
    }
}
