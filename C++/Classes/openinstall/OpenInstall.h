//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALL_H
#define OPENINSTALL_H

#include "AppData.h"
#include "cocos2d.h"

namespace openInstall2dx {
    
    class OpenInstall {
        
    public:

        static void config(bool adEnabled, char *oaid, char *gaid);

        static void init();

        static void init(bool permission);
        
        static void getInstall(float timeout,void (*installCallback)(AppData appData));
        
        static void registerWakeUpHandler(void (*wakeupCallback)(AppData appData));
        
        static void reportRegister();
        
        static void reportEffectPoint(const char *poindId, long pointValue);
        
    };

}

#endif //OPENINSTALL_H
