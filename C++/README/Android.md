# Android 集成指南

## 拷贝文件

- 将 `Classes/openinstall` 文件夹拷贝到项目的 `Classes` 目录下
- 将 `Android` 目录下的 `src` 文件夹下的内容拷贝到项目的 `app/src` 目录下
- 将 `Android` 目录下的 `libs`文件夹下的jar文件拷贝到项目的 `app/libs` 目录下 

## 配置项目

#### 添加 C++ 源文件定义

添加 openinstall 相关的 C++ 文件到 `Android.mk` 的 `LOCAL_SRC_FILES` 中

```
        ../../../Classes/openinstall/OpenInstall.cpp \
        ../../../Classes/openinstall/AppData.cpp \
        ../../../Classes/openinstall/Android/OpenInstallProxy.cpp \
        ../../../Classes/openinstall/Android/AndroidOpenInstall.cpp
```

#### 添加应用权限

在 `AndroidManifest.xml` 中添加 `openinstall` 需要的权限

``` xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

#### 配置 AppKey 和 scheme
从 [openinstall官网](https://www.openinstall.io/) 获取应用的 `AppKey` 和 `scheme`。将下面文档中的 `OPENINSTALL_APPKEY` 和 `OPENINSTALL_SCHEME` 替换。  
（scheme的值详细获取位置：openinstall应用控制台->Android集成->Android应用配置）

##### AppKey 配置
在 `AndroidManifest.xml` 的 `application` 标签中添加

``` xml
    <meta-data
        android:name="com.openinstall.APP_KEY"
        android:value="OPENINSTALL_APPKEY"/>
```
#### 拉起配置
- 将启动 `AppActivity` 替换成 openinstall 提供的 `OpenInstallActivity`
- 给启动 `Activity` 添加 `android:launchMode="singleTask"` 属性
- 给启动 `Activity` 添加 `scheme` 配置

最终配置大致如下
``` xml
    <activity
        android:name="io.openinstall.sdk.OpenInstallActivity"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:label="@string/app_name"
        android:launchMode="singleTask"
        android:screenOrientation="landscape"
        android:theme="@android:style/Theme.NoTitleBar.Fullscreen">

        <intent-filter>
            <action android:name="android.intent.action.MAIN"/>
            <category android:name="android.intent.category.LAUNCHER"/>
        </intent-filter>

        <intent-filter>
            <action android:name="android.intent.action.VIEW"/>

            <category android:name="android.intent.category.DEFAULT"/>
            <category android:name="android.intent.category.BROWSABLE"/>

            <data android:scheme="OPENINSTALL_SCHEME"/>
        </intent-filter>

    </activity>
```
_如果有其他的逻辑需要加入 `AppActivity` 中，可以采用继承 `OpenInstallActivity` 来实现_

#### 隐私政策规范
需要确保用户同意《隐私政策》之后，再初始化 openinstall。参考 [应用合规指南](https://www.openinstall.io/doc/rules.html) 
``` cpp
    openInstall2dx::OpenInstall::init();
```
初始化之后再调用其它接口，下面的`config` 接口除外

#### 广告平台
1、针对广告平台接入，新增配置接口，在调用 `init` 之前调用。参考 [广告平台对接Android集成指引](https://www.openinstall.io/doc/ad_android.html)
``` cpp
    /**
    * adEnabled 为 true 表示 openinstall 需要获取广告追踪相关参数，默认为 false
    * oaid 为 null 时，表示交由 openinstall 获取 oaid， 默认为 null
    * gaid 为 null 时，表示交由 openinstall 获取 gaid， 默认为 null
    */
    openInstall2dx::OpenInstall::config(true, "通过移动安全联盟获取到的 oaid", "通过 google api 获取到的 advertisingId");
```
例如： 开发者自己获取到了 oaid，但是需要 openinstall 获取 gaid，则调用代码为
``` cpp
    // f32a09dc-3312-d43e-6583-62fac13f33ae 是通过移动安全联盟获取到的 oaid
    openInstall2dx::OpenInstall::config(true, "f32a09dc-3312-d43e-6583-62fac13f33ae", nullptr);
```

2、为了精准地匹配到渠道，需要获取设备唯一标识码（IMEI），因此需要做额外的权限申请  
在 `AndroidManifest.xml` 中添加权限声明 `<uses-permission android:name="android.permission.READ_PHONE_STATE"/>` 

3、允许插件申请权限并初始化
``` cpp
    /**
    * 调用初始化，允许 openinstall 请求权限
    * permission 为 true，表示允许 openinstall 申请权限，以便获取 imei
    */
    openInstall2dx::OpenInstall::init(true);
```