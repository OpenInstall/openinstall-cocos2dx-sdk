//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALL_H
#define OPENINSTALL_H

#include "AppData.h"
#include "openinstall/Android/AndroidConfig.h"
#include "openinstall/iOS/iOSConfig.h"
#include "cocos2d.h"

namespace openInstall2dx {
    
    class OpenInstall {
        
    public:

        static void configAndroid(AndroidConfig adConfig);

        static void configIOS(iOSConfig adConfig);

        static void init();

        static void getInstall(float timeout,void (*installCallback)(AppData appData));
        
        static void registerWakeUpHandler(void (*wakeupCallback)(AppData appData));
        
        static void reportRegister();
        
        static void reportEffectPoint(const char *poindId, long pointValue);
        
    };

}

#endif //OPENINSTALL_H
