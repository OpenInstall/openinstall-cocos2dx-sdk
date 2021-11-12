//
//  CPOpenInstall.h
//  cocos2d_tests
//
//  Created by cooper on 2018/6/5.
//

#ifndef CPOPENINSTALL_H
#define CPOPENINSTALL_H

#include "AppData.h"
#include "iOSConfig.h"
//#ifdef __OBJC__
//#import "OpeninstallData.h"
//#endif
//#include <string>

namespace openInstall2dx {
    
    class CPOpenInstall {
        
    public:
                
        static void config(iOSConfig adConfig);
                    
        static void initOpeninstall();
        
        static void getInstallFrom(float timeout,void (*installCallback)(AppData appData));
        
        static void registerWakeUpHandlerToNative(void (*wakeupCallback)(AppData appData));
        
        static void restoreWakeUpHandlerCallBack(AppData appdata);
        
        static void reportRegisterToService();
        
        static void reportEffectPointToService(const char *poindId, long pointValue);
        
    };
}

#endif //OPENINSTALL_H

