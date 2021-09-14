# openinstall-cocos2dx-javascript
cocos2d-x 集成 openinstall SDK  

此仓库是根据 Cocos Creator 集成而写，直接使用 `cocos2d-js` 的用户请参考 [cocos2d-js集成](README/cocos2d-js.md) 修改相应文件后再集成

**注意：请使用最新的稳定版本集成**  
从 [Releases](https://github.com/OpenInstall/openinstall-cocos2dx-sdk/releases) 中选择最新发布的版本，按图标识获取稳定版本代码和文档

![最新稳定版](https://res.cdn.openinstall.io/doc/latest-release.png)


## Android 集成

集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)

## 使用指南  

将 `Script` 文件夹中的 `OpenInstall.js` 文件拷贝到项目的脚本文件夹 `Script` 中。  
在组件中使用时，需要先引入openinstall脚本
``` js
var openinstall = require("OpenInstall");
```

### 1 初始化
App 启动时，请确保用户同意《隐私政策》之后，再调用初始化；如果用户不同意，则不进行openinstall SDK初始化。参考 [应用合规指南](https://www.openinstall.io/doc/rules.html)   

``` js
openinstall.init();
```
### 2 快速安装和一键跳转  

在应用启动时，注册拉起回调。当 App 被唤醒时，可以及时在回调中获取跳转携带的数据    
可在组件脚本的`OnLoad`方法中调用，请在初始化后调用。
``` js
    // 拉起回调方法
    _wakeupCallback : function(appData){
        cc.log("拉起参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    },
    // 可在 onLoad 中调用
    openinstall.registerWakeUpHandler(this._wakeupCallback);
```

### 3 携带参数安装（高级版功能）

在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长(一般为8～15秒)，单位秒
``` js
    //安装回调方法
    _installCallback : function(appData){
        cc.log("安装参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    },
    //在 App 业务需要时调用
    openinstall.getInstall(10, this._installCallback);
```
> **注意：**    
> 1. 安装参数尽量不要自己保存，在每次需要用到的时候调用该方法去获取，因为如果获取成功sdk会保存在本地  
> 2. 该方法可重复获取参数，如需只要在首次安装时获取，可设置标记，详细说明可参考openinstall官网的常见问题

### 4 渠道统计（高级版功能）
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员使用 api 上报

#### 4.1 注册上报
根据自身的业务规则，在确保用户完成 app 注册的情况下调用 api
``` js
openinstall.reportRegister();
```

#### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。  
请在 [openinstall 控制台](https://developer.openinstall.io/) 的 “效果点管理” 中添加对应的效果点  
![创建效果点](https://res.cdn.openinstall.io/doc/effect_point.png)  
调用接口进行效果点的上报，第一个参数对应控制台中的 **效果点ID**  
```js
openinstall.reportEffectPoint("effect_test", 1);
```

## 导出apk/ipa包并上传
代码集成完毕后，需要导出安装包上传openinstall后台，openinstall会自动完成所有的应用配置工作。  
![上传安装包](https://res.cdn.openinstall.io/doc/upload-ipa-jump.png)

上传完成后即可开始在线模拟测试，体验完整的App安装/拉起流程；待测试无误后，再完善下载配置信息。  
![在线测试](https://res.cdn.openinstall.io/doc/js-test.png)

## 如有疑问

若您在集成或使用中有任何疑问或者困难，请 [联系我们](https://www.openinstall.io/)。 

