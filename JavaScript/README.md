# openinstall-cocos2dx-javascript
cocos2d-x 集成 openinstall SDK


## Android 集成

集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)

## 使用指南
基于开发工具 Cocos Creator 的集成使用手册
#### 引入 openinstall 脚本
将 `Script` 文件夹中的 `OpenInstall.js` 拖入项目的脚本文件夹 `Script` 中，在组件中使用时，请先引入脚本
``` js
var openinstall = require("OpenInstall");
```

#### 获取拉起数据
在组件脚本的 `onLoad` 方法中，注册拉起回调，这样当 App 被拉起时，会回调方法，并可在回调中获取拉起数据

``` js
    openinstall.registerWakeUpHandler(function(appData){
        cc.log("拉起参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    });
```

#### 获取安装数据
在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长，单位秒
``` js
    openinstall.getInstall(8, function(appData){
        cc.log("安装参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    });
```

#### 渠道统计
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员代码埋点上报

##### 注册上报
在用户注册成功后，调用接口上报注册量
``` js
openinstall.reportRegister();
```

##### 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。调用接口前，请先进入 openinstall 管理后台 “效果点管理” 中添加效果点，第一个参数对应管理后台 效果点ID
```js
openinstall.reportEffectPoint("effect_test", 1);
```
