
# Android 平台配置

**版本升级时会增删一些文件，请对比最新的文件，进行添加和删除**  
**版本升级时会增删一些接口，请认真查看文档进行升级**  

## 拷贝文件
- 将 Android 目录下的 src 文件夹下的内容拷贝到项目的 app/src 目录下
- 将 Android 目录下的 libs 文件夹下的 jar 文件拷贝到项目的 app/libs 目录下

> 注意：请开发者在进行升级时，重新拷贝并覆盖旧的文件，删除低版本SDK  

## 配置项目

### 添加应用权限
在 `AndroidManifest.xml` 中添加 `openinstall` 需要的权限

``` xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

### 配置 AppKey 和 scheme
前往 [openinstall控制台](https://developer.openinstall.io/) ，进入应用，选择 “Android集成”，切换到“Android应用配置”，获取应用的 AppKey 和 scheme。  
![获取appkey和scheme](https://res.cdn.openinstall.io/doc/android-info.jpg)

#### AppKey 配置
在 `AndroidManifest.xml` 的 `application` 标签中添加

``` xml
    <meta-data
        android:name="com.openinstall.APP_KEY"
        android:value="openinstall为应用分配的appkey"/>
```

#### scheme 配置
- 将启动 `AppActivity` 继承 openinstall 提供的 `OpenInstallActivity`
- 给启动 `AppActivity` 添加 `android:launchMode="singleTask"` 属性
- 给启动 `AppActivity` 添加 `scheme` 配置

最终 `AppActivity` 的配置大致如下
``` xml
    <activity
        android:name="org.cocos2dx.lua.AppActivity"
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

            <data android:scheme="openinstall为应用分配的scheme"/>
        </intent-filter>

    </activity>
```
> 不采用继承 `OpenInstallActivity` 的方式时，可以将 `OpenInstallActivity` 中的相关代码拷贝到 `AppActivity` 中；开发者升级时，也需要对照 `OpenInstallActivity` 的修改对 `AppActivity` 做相应的修改


## 广告平台接入
1、针对广告平台接入，新增配置接口，在调用 `init` 之前调用。参考 [广告平台对接Android集成指引](https://www.openinstall.io/doc/ad_android.html)
``` lua
	local op_config = {adEnabled = true}
	openinstall:configAndroid(op_config)
```
传入参数说明：   
| 参数名| 参数类型 | 描述 |  
| --- | --- | --- |
| adEnabled| bool | 广告平台接入开关（必须） |
| macDisabled | bool | 是否禁止 SDK 获取 mac 地址 |
| imeiDisabled | bool | 是否禁止 SDK 获取 imei |
| gaid | string | 通过 google api 获取到的 advertisingId，SDK 将不再获取gaid |
| oaid | string | 通过移动安全联盟获取到的 oaid，SDK 将不再获取oaid |

> 注意：`openinstall:config(adEnabled, oaid, gaid)` 接口已移除，请使用新的配置接口  

2、为了精准地匹配到渠道，需要获取设备唯一标识码（IMEI），因此需要在 AndroidManifest.xml 中添加权限声明 
```
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```
3、在权限申请成功后，再进行 openinstall 初始化。**无论终端用户是否同意，都要调用初始化**
> 注意：`openinstall:init(permission)` 接口已移除，请自行处理权限请求
