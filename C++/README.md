# openinstall-cocos2dx-c++

cocos2d-x 集成 openinstall SDK

## Android 集成
集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)

## 使用指南
在所有需要使用到 openinstall api 的文件中，需要导入 openinstall 头文件
``` cpp
#include "OpenInstall.h"
```
> 备注：请根据文件目录结构适当调整引用路径
### 1 初始化
App 启动时，请确保用户同意《隐私政策》之后，再调用初始化；如果用户不同意，则不进行openinstall SDK初始化。参考 [应用合规指南](https://www.openinstall.io/doc/rules.html)   
示例：在 `AppDelegate` 的 `initGLContextAttrs` 的方法中进行初始化。
``` cpp
void AppDelegate::initGLContextAttrs()
{
    GLContextAttrs glContextAttrs = {8, 8, 8, 8, 24, 8, 0};

    GLView::setGLContextAttrs(glContextAttrs);

    // 初始化openinstall
    openInstall2dx::OpenInstall::init();

}
```
> **注意：** 请在调用初始化后，再调用 openinstall 其它 api。
### 2 快速安装与一键拉起

在应用启动时，注册拉起回调。当 App 被唤醒时，可以及时在回调中获取跳转携带的数据  
可在 `AppDelegate` 的 `initGLContextAttrs` 的方法中，在初始化后调用。
``` cpp
    openInstall2dx::OpenInstall::registerWakeUpHandler([](openInstall2dx::AppData appData){
        std::string channelCode = appData.getChannelCode();
        std::string bindData = appData.getBindData();
    });
```

### 3 携带参数安装（高级版功能）

在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长，单位秒
``` cpp
    openInstall2dx::OpenInstall::getInstall(10,[](openInstall2dx::AppData appData){
        std::string channelCode = appData.getChannelCode();
        std::string bindData = appData.getBindData();
    });
```

### 4 渠道统计 （高级版功能）

SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员使用 api 上报

#### 4.1 注册上报
根据自身的业务规则，在确保用户完成 app 注册的情况下调用 api
``` cpp
    openInstall2dx::OpenInstall::reportRegister();
```

#### 4.2 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。  
请在 [openinstall 控制台](https://developer.openinstall.io/) 的 “效果点管理” 中添加对应的效果点  
![创建效果点](https://res.cdn.openinstall.io/doc/effect_point.png)  
调用接口进行效果点的上报，第一个参数对应控制台中的 **效果点ID**  
``` cpp
    openInstall2dx::OpenInstall::reportEffectPoint("effect_test", 1);
```

## 导出apk/ipa包并上传
代码集成完毕后，需要导出安装包上传openinstall后台，openinstall会自动完成所有的应用配置工作。  
![上传安装包](https://res.cdn.openinstall.io/doc/upload-ipa-jump.png)

上传完成后即可开始在线模拟测试，体验完整的App安装/拉起流程；待测试无误后，再完善下载配置信息。  
![在线测试](https://res.cdn.openinstall.io/doc/js-test.png)

## 如有疑问

若您在集成或使用中有任何疑问或者困难，请 [咨询openinstall客服](https://www.openinstall.io/)。 