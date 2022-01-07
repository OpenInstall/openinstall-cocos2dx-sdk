//
// Created by Wenki on 2018/6/5.
//

#ifndef ANDROID_ANDROIDOPENINSTALL_H
#define ANDROID_ANDROIDOPENINSTALL_H

#include "../AppData.h"
#include "AndroidConfig.h"

using namespace openInstall2dx;

class AndroidOpenInstall {

public:

    static void config(AndroidConfig adConfig);

    static void init();

    static void getInstall(float s, void (*installCallback)(AppData appData));

    static void getInstallCanRetry(float timeout,void (*installRetryCallback)(AppData appData, bool retry));

    static void registerWakeUpHandler(void (*wakeupCallback)(AppData appData));

    static void registerWakeUpHandler(void (*wakeupCallback)(AppData appData), bool alwaysCallback);

    static void reportRegister();

    static void reportEffectPoint(const char *poindId, long pointValue);

};

#endif //ANDROID_ANDROIDOPENINSTALL_H
