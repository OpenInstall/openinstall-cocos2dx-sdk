# openinstall-cocos2dx-javascript
cocos2d-x 集成 openinstall SDK  

此仓库是根据 Cocos Creator 集成而写，直接使用 `cocos2d-js` 的用户请参考 [cocos2d-js集成](README/cocos2d-js.md) 修改相应文件后再集成

## Android 集成

集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)

## 使用指南  

### 除了`快速下载`功能，其他功能都需要先引入 openinstall 脚本
将 `Script` 文件夹中的 `OpenInstall.js` 拖入项目的脚本文件夹 `Script` 中，在组件中使用时，请先引入脚本
``` js
var openinstall = require("OpenInstall");
```

### 1 快速下载
如果只需要快速下载功能，无需其它功能（携带参数安装、渠道统计、一键拉起），完成初始化即可（包括iOS.md和Android.md中的初始化工作）


### 2 一键拉起  
##### 一键拉起的配置见iOS.md和Android.md相关文档

#### 获取拉起数据
在组件脚本的 `onLoad` 方法中，注册拉起回调，这样当 App 被拉起时，会回调方法，并可在回调中获取拉起数据

``` js
    // 拉起回调方法
    _wakeupCallback : function(appData){
        cc.log("拉起参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    },
    // 在 onLoad 中调用
    openinstall.registerWakeUpHandler(this._wakeupCallback);
```

### 3 携带参数安装（高级版功能）
#### 获取安装数据
在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长(一般为8～15秒)，单位秒，
``` js
    //安装回调方法
    _installCallback : function(appData){
        cc.log("安装参数：channelCode=" + appData.channelCode 
            + ", bindData=" + appData.bindData);
    },
    //在 App 业务需要时调用
    openinstall.getInstall(10, this._installCallback);
```
_备注：  
- 注意这个安装参数尽量不要自己保存，在每次需要用到的时候调用该方法去获取，因为如果获取成功sdk会保存在本地  
- 该方法可重复获取参数，如需只要在首次安装时获取，可设置标记，详细说明可参考openinstall官网的常见问题

### 4 渠道统计（高级版功能）
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员代码埋点上报

##### 4.1 注册上报
在用户注册成功后，调用接口上报注册量
``` js
openinstall.reportRegister();
```

##### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。调用接口前，请先进入 openinstall 管理后台 “效果点管理” 中添加效果点，第一个参数对应管理后台 效果点ID
```js
openinstall.reportEffectPoint("effect_test", 1);
```
