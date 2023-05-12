//
// Created by Wenki on 2018/5/25.
//


#include "OpenInstall.h"


#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

#include "Android/AndroidConfig.h"
#include "Android/AndroidOpenInstall.h"

#elif (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

#include "CPOpenInstall.h"

#endif

using namespace openInstall2dx;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
void OpenInstall::configAndroid(AndroidConfig adConfig) {
    AndroidOpenInstall::config(adConfig);
}
#endif

void OpenInstall::init() {

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

    AndroidOpenInstall::init();

#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    CPOpenInstall::initOpeninstall();

#endif

}


#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
void OpenInstall::getInstall(float s, void (*installCallback)(AppData, bool)) {
    AndroidOpenInstall::getInstall(s, installCallback);
}
#endif


#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
void OpenInstall::getInstall(float s, void (*installCallback)(AppData)) {
    CPOpenInstall::getInstallFrom(s,installCallback);
}
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
void OpenInstall::getInstallCanRetry(float s, void (*installCallback)(AppData, bool)) {
    AndroidOpenInstall::getInstallCanRetry(s, installCallback);
}
#endif

void OpenInstall::registerWakeUpHandler(void (*wakeupCallback)(AppData)) {
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

void OpenInstall::reportEffectPoint(const char *pointId, long pointValue, std::map<std::string, std::string> extraMap){
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::reportEffectPoint(pointId, pointValue, extraMap);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#endif
}

void OpenInstall::reportShare(const char *shareCode, const char *sharePlatform, void (*callbackMethod)(bool, std::string)){
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    AndroidOpenInstall::reportShare(shareCode, sharePlatform, callbackMethod);
#elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#endif
}
