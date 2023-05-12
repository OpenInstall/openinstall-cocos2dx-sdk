//
// Created by Wenki on 2018/6/5.
//

#ifndef ANDROID_ANDROIDOPENINSTALL_H
#define ANDROID_ANDROIDOPENINSTALL_H

#include <map>
#include <string>
#include "../AppData.h"
#include "AndroidConfig.h"

using namespace openInstall2dx;
using namespace std;

typedef map<string, string> StringMap;

class AndroidOpenInstall {

public:

    static void config(AndroidConfig);

    static void init();

    static void getInstall(float s, void (*installCallback)(AppData, bool));

    static void getInstallCanRetry(float s, void (*installRetryCallback)(AppData, bool));

    static void registerWakeUpHandler(void (*wakeupCallback)(AppData));

    static void reportRegister();

    static void reportEffectPoint(const char *, long);

    static void reportEffectPoint(const char *, long, StringMap);

    static void reportShare(const char *, const char *, void (*callbackMethod)(bool, string));
};

#endif //ANDROID_ANDROIDOPENINSTALL_H
