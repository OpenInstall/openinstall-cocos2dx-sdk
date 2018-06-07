# iOS 集成指南

## 拷贝文件并导入

- 将 `Classes/openinstall` 文件夹拷贝到项目的 `Classes` 目录下
- 将 `iOS` 目录下的 `OpenInstallSDK2.2.0` 拷贝到源文件目录下
- 导入 `Classes/openinstall` 目录下的 C++ 源文件、iOS 相关文件以及 iOS 目录下的 `OpenInstallSDK2.2.0` 到工程中

## 配置项目

#### 初始化配置
根据 openinstall 官方文档，在 Info.plist 文件中配置 appKey 键值对，如下：

``` plist
	<key>com.openinstall.APP_KEY</key>
	<string>“从openinstall官网后台获取应用的appkey”</string>
```

#### universal links配置（iOS9以后推荐使用）

对于 iOS，为确保能正常跳转，AppID 必须开启 Associated Domains 功能，请到 [苹果开发者网站](https://developer.apple.com)，选择 Certificate, Identifiers & Profiles，选择相应的 AppID，开启 Associated Domains。  
__注意__：当 AppID 重新编辑过之后，需要更新相应的 mobileprovision 证书。(详细步骤请看 [openinstall官网](https://www.openinstall.io) 后台文档，universal link 从后台获取)

在 `AppController.mm` 中引入头文件 `OpenInstallSDK.h` ，并添加通用链接(Universal Link)回调方法，委托OpenInstallSDK来处理

``` objc
    #import "OpenInstallSDK.h"

    -(BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray * _Nullable))restorationHandler{
    
    ////判断是否通过OpenInstall Universal Link 唤起App
    if ([OpenInstallSDK continueUserActivity:userActivity]) {
        
        return YES;
    };
    
    //其他第三方回调；
    return YES;
    
}

```

#### scheme配置

在 `Info.plist` 文件中，在 `CFBundleURLTypes` 数组中添加应用对应的 scheme

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

在 `AppController.mm` 中引入头文件 `OpenInstallSDK.h` ，并添加 `scheme` 的回调方法，委托 OpenInstallSDK 来处理

``` objc
  #import "OpenInstallSDK.h"

//iOS9以下
-(BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    //判断是否通过OpenInstall URL Scheme 唤起App
    if  ([OpenInstallSDK handLinkURL:url]){
        return YES;
    }
    //其他第三方回调；
    return YES;

}

//iOS9以上
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(nonnull NSDictionary *)options{
    //判断是否通过OpenInstall URL Scheme 唤起App
    if  ([OpenInstallSDK handLinkURL:url]){
        return YES;
    }
    //其他第三方回调；
     return YES;

}

```
