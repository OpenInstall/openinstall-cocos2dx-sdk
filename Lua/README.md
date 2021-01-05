# openinstall-cocos2dx-lua
cocos2d-x 集成 openinstall SDK  

## Android 集成

集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)


## 使用指南

导入 `openinstall.lua` 文件到工程里，并在调用方法的时候引用，例如：
``` lua
  local openinstall = require("app.models.openinstall")
  --require里面是openinstall.lua所在目录
```
### 1 快速下载
如果只需要快速下载功能，无需其它功能（携带参数安装、渠道统计、一键拉起），完成初始化相关工作即可

### 2 一键拉起
完成文档前面iOS和Android介绍的一键拉起相关配置

#### 获取拉起数据
一般在app启动后的第一个scene去获取
``` lua
  local function wakeUpCallBack(result)
     print("拉起参数回调:"..result)
  end

  openinstall:registerWakeupHandler(wakeUpCallBack)
```

### 3 携带参数安装 (高级版功能)

在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可重复获取，可设置超时时长，单位秒
``` lua
  local function getInstallCallBack(result)
     print("安装参数回调:"..result)
  end

  openinstall:getInstall(10, getInstallCallBack)
```

### 4 渠道统计 (高级版功能)
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员代码埋点上报

#### 4.1 注册上报
根据自身的业务规则，在确保用户完成 app 注册的情况下调用相关api
``` lua
  openinstall:reportRegister()
```

#### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。调用接口前，请先进入 openinstall 管理后台 “效果点管理” 中添加效果点，第一个参数对应管理后台 *效果点ID*
```lua
  openinstall:reportEffectPoint("effect_test", 1)
```

## 导出apk/api包并上传
- 代码集成完毕后，需要导出安装包上传openinstall后台，openinstall会自动完成所有的应用配置工作。  
- 上传完成后即可开始在线模拟测试，体验完整的App安装/拉起流程；待测试无误后，再完善下载配置信息。  

下面是apk包的上传界面（后台截图）：  

![上传安装包](res/guide2.jpg)

