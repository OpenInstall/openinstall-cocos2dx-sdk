//
// Created by Wenki on 2018/5/25.
//


#include "OpenInstall.h"
#include "AdConfig.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

#include "Android/AndroidOpenInstall.h"

#elif (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

#include "CPOpenInstall.h"

#endif

using namespace openInstall2dx;

void OpenInstall::config(bool adEnabled, char *oaid, char *gaid) {
    // 兼容旧版本 2021.09.06
    AdConfig adConfig = AdConfig(adEnabled, oaid, gaid, false, false);
    config(adConfig);
}

void OpenInstall::config(AdConfig adConfig) {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::config(adConfig);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    // just support on android
#endif
}

void OpenInstall::init() {
    init(false);
}

void OpenInstall::init(bool permission) {

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

    AndroidOpenInstall::init(permission);

#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::initOpeninstall();

#endif

}

void OpenInstall::getInstall(float s, void (*installCallback)(AppData appData)) {

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::getInstall(s, installCallback);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::getInstallFrom(s,installCallback);

#endif

}

void OpenInstall::registerWakeUpHandler(void (*wakeupCallback)(AppData appData)) {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::registerWakeUpHandler(wakeupCallback);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::registerWakeUpHandlerToNative(wakeupCallback);

#endif
}

void OpenInstall::reportRegister() {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::reportRegister();
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::reportRegisterToService();
#endif
}

void OpenInstall::reportEffectPoint(const char *pId, long pValue) {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::reportEffectPoint(pId, pValue);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::reportEffectPointToService(pId, pValue);
#endif
}
