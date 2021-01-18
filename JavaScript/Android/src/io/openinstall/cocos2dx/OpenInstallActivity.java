/****************************************************************************
 Copyright (c) 2015 Chukong Technologies Inc.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package io.openinstall.cocos2dx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.fm.openinstall.OpenInstall;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

public class OpenInstallActivity extends Cocos2dxActivity {

    private static OpenInstallActivity app = null;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = this;
        OpenInstallHelper.getWakeup(getIntent(), app);
    }

    public static void config(final boolean adEnabled, final String oaid, final String gaid) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OpenInstallHelper.config(adEnabled, oaid, gaid);
            }
        });

    }

    public static void init(final boolean permission) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OpenInstallHelper.init(permission, app);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OpenInstallHelper.getWakeup(intent, app);
    }

    public static void getInstall(final int s) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OpenInstallHelper.getInstall(s, app);
            }
        });
    }

    public static void registerWakeup() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OpenInstallHelper.registerWakeupCallback(app);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将权限请求结果告知 openinstall
        OpenInstall.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
