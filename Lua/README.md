# openinstall-cocos2dx-lua
cocos2d-x 集成 openinstall SDK  

**注意：请使用最新的稳定版本集成**  
从 [Releases](https://github.com/OpenInstall/openinstall-cocos2dx-sdk/releases) 中选择最新发布的版本，按图标识获取稳定版本代码和文档

![最新稳定版](https://res.cdn.openinstall.io/doc/latest-release.png)

## Android 集成

集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)


## 使用指南

导入 `openinstall.lua` 文件到工程里，并在调用方法的时候引用。  
例如将文件放入工程 `src/app/models/` 目录下引用：
``` lua
  --require 里的参数是 openinstall.lua 文件所在路径
  local openinstall = require("app.models.openinstall")
  
```
### 1 初始化
App 启动时，请确保用户同意《隐私政策》之后，再调用初始化；如果用户不同意，则不进行openinstall SDK初始化。参考 [应用合规指南](https://www.openinstall.io/doc/rules.html)   

``` lua
  openinstall:init()
```

### 2 快速安装和一键跳转

在应用启动时，注册拉起回调。当 App 被唤醒时，可以及时在回调中获取跳转携带的数据，请在初始化后调用。  
``` lua
  local function wakeUpCallBack(result)
     print("拉起参数回调:"..result)
  end

  openinstall:registerWakeupHandler(wakeUpCallBack)
```

### 3 携带参数安装 (高级版功能)

在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长(一般为8～15秒)，单位秒  
``` lua
  local function getInstallCallBack(result)
     print("安装参数回调:"..result)
  end

  openinstall:getInstall(10, getInstallCallBack)
```

### 4 渠道统计 (高级版功能)
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员使用 api 上报

#### 4.1 注册上报
根据自身的业务规则，在确保用户完成 app 注册的情况下调用 api
``` lua
  openinstall:reportRegister()
```

#### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。  
请在 [openinstall 控制台](https://developer.openinstall.io/) 的 “效果点管理” 中添加对应的效果点  
![创建效果点](https://res.cdn.openinstall.io/doc/effect_point.png)  
调用接口进行效果点的上报，第一个参数对应控制台中的 **效果点ID**  
```lua
  openinstall:reportEffectPoint("effect_test", 1)
```
## 导出apk/ipa包并上传
代码集成完毕后，需要导出安装包上传openinstall后台，openinstall会自动完成所有的应用配置工作。  
![上传安装包](https://res.cdn.openinstall.io/doc/upload-ipa-jump.png)

上传完成后即可开始在线模拟测试，体验完整的App安装/拉起流程；待测试无误后，再完善下载配置信息。  
![在线测试](https://res.cdn.openinstall.io/doc/js-test.png)

## 如有疑问

若您在集成或使用中有任何疑问或者困难，请 [联系我们](https://www.openinstall.io/)。 
