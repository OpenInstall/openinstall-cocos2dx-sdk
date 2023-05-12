//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALL_H
#define OPENINSTALL_H

#include "AppData.h"
#include "openinstall/Android/AndroidConfig.h"
#include "cocos2d.h"

namespace openInstall2dx {
    
    class OpenInstall {
        
    public:

        static void configAndroid(AndroidConfig androidConfig);

        static void init();

        #if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
        static void getInstall(float timeout,void (*installCallback)(AppData, bool));
        #elif  (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
        static void getInstall(float timeout,void (*installCallback)(AppData));
        #endif

        #if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
        static void getInstallCanRetry(float timeout,void (*installCallback)(AppData, bool));
        #endif
        
        static void registerWakeUpHandler(void (*wakeupCallback)(AppData));

        static void reportRegister();
        
        static void reportEffectPoint(const char *pointId, long pointValue);

        static void reportEffectPoint(const char *pointId, long pointValue, std::map<std::string, std::string> extraMap);

        static void reportShare(const char *shareCode, const char *sharePlatform, void (*callbackMethod)(bool, std::string));
        
    };

}

#endif //OPENINSTALL_H
