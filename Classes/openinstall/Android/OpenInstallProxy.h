//
// Created by Wenki on 2018/5/28.
//

#ifndef OPENINSTALLPROXY_H
#define OPENINSTALLPROXY_H

#include <jni.h>
#include "openinstall/AppData.h"

using namespace openInstall2dx;

extern "C" {


static void (*appWakeUpCallbackMethod)(AppData appData);
static void (*appInstallCallbackMethod)(AppData appData);

JNIEXPORT void JNICALL Java_io_openinstall_sdk_AppWakeUpCallback_wakeup
        (JNIEnv *, jobject, jobject);
JNIEXPORT void JNICALL Java_io_openinstall_sdk_AppInstallCallback_install
        (JNIEnv *, jobject, jobject);

void setAppWakeUpCallbackMethod(void (*callbackMethod)(AppData appData));
void setAppInstallCallbackMethod(void (*callbackMethod)(AppData appData));

}
#endif //PROJ_ANDROID_OPENINSTALLPROXY_H
