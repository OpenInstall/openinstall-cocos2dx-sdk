//
// Created by Wenki on 2018/6/5.
//

#include "AndroidOpenInstall.h"

#include <jni.h>
#include "../../../cocos2d/cocos/platform/android/jni/JniHelper.h"
#include "OpenInstallProxy.h"
#include <android/log.h>

#define  LOG_TAG    "OpenInstall"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

using namespace cocos2d;

void AndroidOpenInstall::config(bool adEnabled, char *oaid, char *gaid){
    JniMethodInfo methodInfo_config;
    if (!JniHelper::getStaticMethodInfo(methodInfo_config, "io/openinstall/sdk/OpenInstallHelper",
                                        "config", "(ZLjava/lang/String;Ljava/lang/String;)V")) {
        LOGD("get OpenInstallHelper.config JniMethodInfo failed");
        return;
    }
    jstring jOaid = methodInfo_config.env->NewStringUTF(oaid);
    jstring jGaid = methodInfo_config.env->NewStringUTF(gaid);
    methodInfo_config.env->CallStaticVoidMethod(methodInfo_config.classID, methodInfo_config.methodID,
                                                adEnabled, jOaid, jGaid);

    LOGD("call config success");
}

void AndroidOpenInstall::init(bool permission) {
    JniMethodInfo methodInfo_init;
    if (!JniHelper::getStaticMethodInfo(methodInfo_init, "io/openinstall/sdk/OpenInstallHelper",
                                        "init", "(Landroid/app/Activity;Z)V")) {
        LOGD("get OpenInstallHelper.init JniMethodInfo failed");
        return;
    }

    JniMethodInfo methodInfo_getContext;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getContext,
                                        "org/cocos2dx/lib/Cocos2dxActivity",
                                        "getContext", "()Landroid/content/Context;")) {
        LOGD("get Cocos2dxActivity.getContext JniMethodInfo failed");
        return;
    }

    jobject jContext = methodInfo_getContext.env->CallStaticObjectMethod(
            methodInfo_getContext.classID,
            methodInfo_getContext.methodID);

    methodInfo_init.env->CallStaticVoidMethod(methodInfo_init.classID, methodInfo_init.methodID,
                                              jContext, permission);

    methodInfo_getContext.env->DeleteLocalRef(jContext);

    LOGD("call init success");

}


void AndroidOpenInstall::getInstall(float s, void (*installCallback)(AppData appData)) {

    JniMethodInfo methodInfo_getInstall;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getInstall,
                                        "com/fm/openinstall/OpenInstall",
                                        "getInstall",
                                        "(Lcom/fm/openinstall/listener/AppInstallListener;I)V")) {
        LOGD("get OpenInstall.getInstall JniMethodInfo failed");
        return;
    }
    // 创建 AppInstallCallback 实例
    JniMethodInfo methodInfo_init;
    if (!JniHelper::getMethodInfo(methodInfo_init, "io/openinstall/sdk/AppInstallCallback",
                                  "<init>", "()V")) {
        LOGD("get AppInstallCallback<init> JniMethodInfo failed");
        return;
    }
    jclass clsAppInstallCallback = methodInfo_init.env->FindClass(
            "io/openinstall/sdk/AppInstallCallback");
    jmethodID methodID_init = methodInfo_init.env->GetMethodID(clsAppInstallCallback, "<init>",
                                                               "()V");
    jobject jAppInstallCallback = methodInfo_init.env->NewObject(clsAppInstallCallback,
                                                                 methodID_init);
    int timeout = static_cast<int>(s);
    // 设置回调
    setAppInstallCallbackMethod(installCallback);
    // 调用方法
    methodInfo_getInstall.env->CallStaticVoidMethod(methodInfo_getInstall.classID,
                                                    methodInfo_getInstall.methodID,
                                                    jAppInstallCallback, timeout);
    // 释放
    methodInfo_init.env->DeleteLocalRef(jAppInstallCallback);

    LOGD("call OpenInstall.getInstall success");
}

void AndroidOpenInstall::registerWakeUpHandler(void (*wakeupCallback)(AppData appData)) {
    setAppWakeUpCallbackMethod(wakeupCallback);

    LOGD("OpenInstall.registerWakeUpHandler success");
}

void AndroidOpenInstall::reportRegister() {
    JniMethodInfo methodInfo_reportRegister;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportRegister,
                                        "com/fm/openinstall/OpenInstall",
                                        "reportRegister", "()V")) {
        LOGD("get OpenInstall.reportRegister JniMethodInfo failed");
        return;
    }

    methodInfo_reportRegister.env->CallStaticVoidMethod(methodInfo_reportRegister.classID,
                                                        methodInfo_reportRegister.methodID);

    LOGD("call OpenInstall.reportRegister success");
}

void AndroidOpenInstall::reportEffectPoint(const char *pId, long pValue) {
    JniMethodInfo methodInfo_reportEffectPoint;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportEffectPoint,
                                        "com/fm/openinstall/OpenInstall",
                                        "reportEffectPoint", "(Ljava/lang/String;J)V")) {
        LOGD("get OpenInstall.reportEffectPoint JniMethodInfo failed");
        return;
    }

    jstring pointId = methodInfo_reportEffectPoint.env->NewStringUTF(pId);
    jlong pointValue = (jlong) pValue;
    methodInfo_reportEffectPoint.env->CallStaticVoidMethod(methodInfo_reportEffectPoint.classID,
                                                           methodInfo_reportEffectPoint.methodID,
                                                           pointId,
                                                           pointValue);

    LOGD("call OpenInstall.reportEffectPoint success");
}