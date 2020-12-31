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
import android.util.Log;

import com.fm.openinstall.Configuration;
import com.fm.openinstall.OpenInstall;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

public class OpenInstallActivity extends Cocos2dxActivity {

    private static OpenInstallActivity app = null;
    private static Configuration configuration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = this;
        OpenInstallHelper.getWakeup(getIntent(), app);
    }

    public static void config(boolean adEnabled, String oaid, String gaid) {
        Configuration.Builder builder = new Configuration.Builder();
        builder.adEnabled(adEnabled);
        oaid = setNull(oaid);
        builder.oaid(oaid);
        gaid = setNull(gaid);
        builder.gaid(gaid);
        Log.d("OpenInstall", String.format("adEnabled = %s, oaid = %s, gaid = %s", adEnabled, oaid==null?"NULL":oaid, gaid==null?"NULL":gaid));
        configuration = builder.build();
    }

    private static String setNull(String res){
        // 传入 null 或者 未定义，设置为 null
        if(res == null || res.equalsIgnoreCase("null")
                || res.equalsIgnoreCase("undefined")){
            return null;
        }
        return res;
    }

    public static void init(boolean permission) {
        OpenInstallHelper.init(permission, app, configuration);
    }

    @Override
    public Cocos2dxGLSurfaceView onCreateView() {
        Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);
        // TestCpp should create stencil buffer
        glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);

        return glSurfaceView;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OpenInstallHelper.getWakeup(intent, app);
    }

    public static void getInstall(int s) {
        OpenInstallHelper.getInstall(s, app);
    }

    public static void registerWakeup() {
        OpenInstallHelper.registerWakeupCallback(app);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将权限请求结果告知 openinstall
        OpenInstall.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 告知 OpenInstallHelper，openinstall 已经接收到了回调并调用了初始化
        OpenInstallHelper.initialized(this);
    }
}
