# iOS 集成指南

## 导出工程
- 在菜单 `项目` -> `构建发布` 弹框中，发布平台选择 `iOS`，填写其他配置，最后点击 `构建`
- 构建完成后，使用 `Xcode` 打开位于 `${projectDir}/build/jsb-link/frameworks/runtime-src/proj.ios_mac` 的 iOS 工程。

## 拷贝文件
- 将 `iOS` 目录下的 `OpeninstallCocosJS` 文件夹拷贝到项目的 `ios` 目录下

## 初始化 openinstall 

在项目中 `ios` 文件夹下的 `Info.plist` 文件中配置appKey键值对，如下：

``` plist
  	<key>com.openinstall.APP_KEY</key>
	<string>从openinstall官网后台获取应用的appKey</string>
```

在 `AppController.mm` 中，增加头文件的引用：

```obj
#import "Openinstall.h"
```

在 `AppController.mm` 中尽量早的调用openinstall初始化方法:

```obj
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
   
//尽量早的去调用
   [Openinstall init];

//其他代码

   return YES;
}

```

#### universal links配置（iOS9以后推荐使用）

对于iOS，为确保能正常跳转，AppID必须开启Associated Domains功能，请到 https://developer.apple.com，选择Certificate, Identifiers & Profiles，选择相应的AppID，开启Associated Domains。注意：当AppID重新编辑过之后，需要更新相应的mobileprovision证书。(详细配置步骤请看openinstall官网后台文档，universal link从后台获取，https://www.openinstall.io)，如果已经开启过Associated Domains功能，进行下面操作：

- 在左侧导航器中点击您的项目
- 选择'Capabilities'标签
- 打开'Associated Domains'开关
- 添加openinstall官网后台中应用对应的关联域名（iOS集成->iOS应用配置->关联域名(Associated Domains)）

#### 相关代码：

在 `AppController.mm` 中添加通用链接(Universal Link)回调方法：

```obj
- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray * _Nullable))restorationHandler{
    //判断是否通过OpenInstall Universal Link 唤起App
    if ([Openinstall setUserActivity:userActivity]) {
        return YES;
    }
    //其他第三方回调；
    return YES;
}
```

#### scheme配置

在 `Info.plist` 文件中，在`CFBundleURLTypes`数组中添加应用对应的`scheme`

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

#### 相关代码：

在 `AppController.mm` 中添加 `scheme` 回调的方法

```obj
//支持目前所有版本的iOS
-(BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    //判断是否通过OpenInstall URL Scheme 唤起App
    if  ([Openinstall setLinkURL:url]){
        return YES;
    }
    //其他第三方回调；
    return YES;
    
}

//注意：在iOS9.0以上的设备中，下面这个方法会覆盖上面的方法，请结合自身业务（比如微信登录等业务）来调用

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

