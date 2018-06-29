# openinstall-cocos2dx-c++

cocos2d-x 集成 openinstall SDK

## Android 集成
集成 openinstall SDK 到 cocos2d-x Android 项目中，请参考 [Android 集成指南](README/Android.md)

## iOS 集成
集成 openinstall SDK 到 cocos2d-x iOS 项目中，请参考 [iOS 集成指南](README/iOS.md)

## 使用指南

#### 初始化
在 `AppDelegate` 的 `initGLContextAttrs` 的方法中进行初始化。
``` cpp
void AppDelegate::initGLContextAttrs()
{
    GLContextAttrs glContextAttrs = {8, 8, 8, 8, 24, 8, 0};

    GLView::setGLContextAttrs(glContextAttrs);

    // openinstall Initialize
    openInstall2dx::OpenInstall::init();

}
```
#### 获取拉起数据
同样，在 `AppDelegate` 的 `initGLContextAttrs` 方法中，`OpenInstall::init();` 之后注册拉起回调，这样当 App 被拉起时，会回调方法，并可在回调中获取拉起数据
``` cpp
    openInstall2dx::OpenInstall::registerWakeUpHandler([](openInstall2dx::AppData appData){
        std::string channelCode = appData.getChannelCode();
        std::string bindData = appData.getBindData();
    });
```

#### 获取安装参数
在应用需要安装参数时，调用以下 api 获取由 SDK 保存的安装参数，可设置超时时长，单位秒
``` cpp
    openInstall2dx::OpenInstall::getInstall(8,[](openInstall2dx::AppData appData){
        std::string channelCode = appData.getChannelCode();
        std::string bindData = appData.getBindData();
    });
```

#### 渠道统计
SDK 会自动完成访问量、点击量、安装量、活跃量、留存率等统计工作。其它业务相关统计由开发人员代码埋点上报
##### 注册上报
在用户注册成功后，调用接口上报注册量
``` cpp
    openInstall2dx::OpenInstall::reportRegister();
```

##### 效果点上报
统计终端用户对某些特殊业务的使用效果，如充值金额，分享次数等等。调用接口前，请先进入 openinstall 管理后台 “效果点管理” 中添加效果点，第一个参数对应管理后台 *效果点ID*
``` cpp
    openInstall2dx::OpenInstall::reportEffectPoint("effect_test", 1);
```