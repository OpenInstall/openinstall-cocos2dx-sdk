//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALLPROXY_H
#define OPENINSTALLPROXY_H

#include <jni.h>
#include "../AppData.h"

using namespace openInstall2dx;

extern "C" {


static void (*appWakeUpCallbackMethod)(AppData);
static void (*appInstallCallbackMethod)(AppData, bool);
static void (*shareResultCallbackMethod)(bool, std::string);

JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_wakeup
        (JNIEnv *, jobject, jobject);
JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_install
        (JNIEnv *, jobject, jobject, jboolean);
JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_onResult
        (JNIEnv *, jobject, jstring, jobject, jboolean, jstring);

void setAppWakeUpCallbackMethod(void (*callbackMethod)(AppData));
void setAppInstallCallbackMethod(void (*callbackMethod)(AppData, bool));
void setShareResultCallbackMethod(void (*callbackMethod)(bool, std::string));

}
#endif //PROJ_ANDROID_OPENINSTALLPROXY_H
