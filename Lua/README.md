# iOS 平台配置

- 将 iOS 目录下的 `OpeninstallCocos2dxLua` 文件夹拷贝到项目的 ios 目录下，然后在Xcode工程左边目录里找到ios 目录，右键通过 "Add Files to xxx" 添加 `OpeninstallCocos2dxLua` 文件夹，add的时候注意勾选“Copy items if needed”、“Create groups”

## 相关配置

### 初始化配置
根据openinstall官方文档，在Info.plist文件中配置appKey键值对，如下：

``` Info.plist
	<key>com.openinstall.APP_KEY</key>
	<string>“从openinstall官网后台获取应用的appkey”</string>
```

### 初始化sdk代码

在app `AppController.mm` 文件中引入头文件
```objc
#import "OpeninstallCocosLua/Openinstall.h"
```

在方法`application:didFinishLaunchingWithOptions:`中引入方法
```objc
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    //尽量早的去调用
    [Openinstall init];
  
    //其他代码

    return YES;
}
```

### 以下为 `一键拉起` 功能的相关配置和代码

### universal links配置（iOS9以后推荐使用）
对于iOS，为确保能正常跳转，AppID必须开启Associated Domains功能，请到[苹果开发者网站](https://developer.apple.com)，选择Certificate, Identifiers & Profiles，选择相应的AppID，开启Associated Domains。注意：当AppID重新编辑过之后，需要更新相应的mobileprovision证书。(详细配置步骤请看[openinstall官网](https://www.openinstall.io)后台文档，universal link从后台获取)，如果已经开启过Associated Domains功能，进行下面操作：  

- 在左侧导航器中点击您的项目
- 选择 `Capabilities` 标签
- 打开 `Associated Domains` 开关
- 添加 openinstall 官网后台中应用对应的关联域名（iOS集成->iOS应用配置->关联域名(Associated Domains)）

### universal links相关代码

在AppController.mm中引入头文件Openinstall.h，并添加通用链接(Universal Link)回调方法，委托Openinstall来处理
```objc

-(BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray * _Nullable))restorationHandler{
    ////判断是否通过OpenInstall Universal Link 唤起App
    if ([Openinstall setUserActivity:userActivity]){
        return YES;
    }
    
    //其他第三方回调；
    return YES;
}

```

### scheme配置
在 `Info.plist` 文件中，在 `CFBundleURLTypes` 数组中添加应用对应的 `scheme`，或者在工程“TARGETS-Info-URL Types”里快速添加

``` plist
	<key>CFBundleURLTypes</key>
	<array>
	    <dict>
		<key>CFBundleTypeRole</key>
		<string>Editor</string>
		<key>CFBundleURLName</key>
		<string>openinstall</string>
		<key>CFBundleURLSchemes</key>
		<array>
		    <string>"从openinstall官网后台获取应用的scheme"</string>
		</array>
	    </dict>
	</array>
```

### scheme相关代码：

在 `AppController.mm` 中添加 `scheme` 回调的方法

```objc
//支持目前所有版本的iOS
-(BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    //判断是否通过OpenInstall URL Scheme 唤起App
    if  ([Openinstall setLinkURL:url]){
        return YES;
    }
    //其他第三方回调；
    return YES;
    
}

//注意：在iOS9.0以上的设备中，下面这个系统方法会覆盖上面的系统方法（主要考虑到微信登录等业务），请结合自身业务来调用
//一般情况下，只要本地有下面的方法存在，则在下面方法中必须调用openinstall的相关api，没有下面方法的情况下可以只在上面的方法中调用openinstall的相关api

//支持iOS9以上
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(nonnull NSDictionary *)options{
    //判断是否通过OpenInstall URL Scheme 唤起App
    if  ([Openinstall setLinkURL:url]){
        return YES;
    }
    //其他第三方回调；
    return YES;
}
```

# Android 平台配置

## 拷贝文件
- 将 Android 目录下的 src 文件夹下的内容拷贝到项目的 app/src 目录下
- 将 Android 目录下的 libs/openinstall_v2.2.1.jar 拷贝到项目的 app/libs 目录下
## 添加应用权限

在 `AndroidManifest.xml` 中添加 `openinstall` 需要的权限

``` xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

## 配置 AppKey 和 scheme
从 [openinstall官网](https://www.openinstall.io/) 获取应用的 `AppKey` 和 `scheme`。将下面文档中的 `OPENINSTALL_APPKEY` 和 `OPENINSTALL_SCHEME` 替换
### AppKey 配置
在 `AndroidManifest.xml` 的 `application` 标签中添加

``` xml
    <meta-data
        android:name="com.openinstall.APP_KEY"
        android:value="OPENINSTALL_APPKEY"/>
```
## 拉起配置
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

            <data android:scheme="OPENINSTALL_SCHEME"/>
        </intent-filter>

    </activity>
```
_不采用继承 `OpenInstallActivity` 的方式时，可以将 `OpenInstallActivity` 中的相关代码拷贝到 `AppActivity` 中_

# 使用指南

## 1 快速下载
如果只需要快速下载功能，无需其它功能（携带参数安装、渠道统计、一键拉起），完成初始化相关工作即可

### 以下功能都需要导入 `openinstall.lua` 文件到工程里，并在调用方法的时候引用，例如：
``` lua
  local openinstall = require("app.models.openinstall")
  --require里面是openinstall.lua所在目录
```

## 2 一键拉起
完成文档前面iOS和Android介绍的一键拉起相关配置

### 获取拉起数据
一般在app启动后的第一个scene去获取
``` lua
  local function wakeUpCallBack(result)
     print("拉起参数回调:"..result)
  end

  openinstall:registerWakeUpHandler(wakeUpCallBack)  
```

## 3 携带参数安装 (高级版功能)

在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可重复获取，可设置超时时长，单位秒
``` lua
  local function getInstallCallBack(result)
     print("安装参数回调:"..result)
  end

  openinstall:getInstall(10, getInstallCallBack)
```

## 4 渠道统计 (高级版功能)
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员代码埋点上报

### 4.1 注册上报
根据自身的业务规则，在确保用户完成 app 注册的情况下调用相关api
``` lua
  openinstall:reportRegister()
```

### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。调用接口前，请先进入 openinstall 管理后台 “效果点管理” 中添加效果点，第一个参数对应管理后台 *效果点ID*
```lua
  openinstall:reportEffectPoint("effect_test", 1)
```


