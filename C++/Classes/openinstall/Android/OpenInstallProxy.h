//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALLPROXY_H
#define OPENINSTALLPROXY_H

#include <jni.h>
#include "../AppData.h"

using namespace openInstall2dx;

extern "C" {


static void (*appWakeUpCallbackMethod)(AppData appData);
static void (*appInstallCallbackMethod)(AppData appData);
static void (*appInstallRetryCallbackMethod)(AppData appData, bool retry);

JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_wakeup
        (JNIEnv *, jobject, jobject);
JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_install
        (JNIEnv *, jobject, jobject);
JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_installRetry
        (JNIEnv *, jobject, jobject, jboolean);

void setAppWakeUpCallbackMethod(void (*callbackMethod)(AppData appData));
void setAppInstallCallbackMethod(void (*callbackMethod)(AppData appData));
void setAppInstallRetryCallbackMethod(void (*callbackMethod)(AppData appData, bool retry));

}
#endif //PROJ_ANDROID_OPENINSTALLPROXY_H
