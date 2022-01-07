//
// Created by Wenki on 2018/6/5.
//

#include "AndroidOpenInstall.h"

#include <jni.h>
#include "../../../cocos2d/cocos/platform/android/jni/JniHelper.h"
#include "OpenInstallProxy.h"
#include <android/log.h>

#define  LOG_TAG    "AndroidOpenInstall"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

using namespace cocos2d;

void AndroidOpenInstall::config(AndroidConfig adConfig) {
    JniMethodInfo methodInfo_config;
    if (!JniHelper::getStaticMethodInfo(methodInfo_config, "io/openinstall/sdk/OpenInstallHelper",
                                        "config", "(ZLjava/lang/String;Ljava/lang/String;ZZ)V")) {
        LOGE("get OpenInstallHelper.config JniMethodInfo failed");
        return;
    }
    jstring jOaid = methodInfo_config.env->NewStringUTF(adConfig.getOaid());
    jstring jGaid = methodInfo_config.env->NewStringUTF(adConfig.getGaid());
    methodInfo_config.env->CallStaticVoidMethod(methodInfo_config.classID,
                                                methodInfo_config.methodID,
                                                adConfig.isAdEnabled(), jOaid, jGaid,
                                                adConfig.isImeiDisabled(),
                                                adConfig.isMacDisabled());

    LOGD("call config success");
}

void AndroidOpenInstall::init() {
    JniMethodInfo methodInfo_init;
    if (!JniHelper::getStaticMethodInfo(methodInfo_init, "io/openinstall/sdk/OpenInstallHelper",
                                        "init", "(Landroid/content/Context;)V")) {
        LOGE("get OpenInstallHelper.init JniMethodInfo failed");
        return;
    }

    JniMethodInfo methodInfo_getContext;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getContext,
                                        "org/cocos2dx/lib/Cocos2dxActivity",
                                        "getContext", "()Landroid/content/Context;")) {
        LOGE("get Cocos2dxActivity.getContext JniMethodInfo failed");
        return;
    }

    jobject jContext = methodInfo_getContext.env->CallStaticObjectMethod(
            methodInfo_getContext.classID,
            methodInfo_getContext.methodID);

    methodInfo_init.env->CallStaticVoidMethod(methodInfo_init.classID, methodInfo_init.methodID,
                                              jContext);

    methodInfo_getContext.env->DeleteLocalRef(jContext);

    LOGD("call init success");

}


void AndroidOpenInstall::getInstall(float s, void (*installCallback)(AppData appData)) {

    JniMethodInfo methodInfo_getInstall;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getInstall,
                                        "io/openinstall/sdk/OpenInstallHelper",
                                        "getInstall",
                                        "(I)V")) {
        LOGE("get OpenInstallHelper.getInstall JniMethodInfo failed");
        return;
    }
    int timeout = static_cast<int>(s);
    // 设置回调
    setAppInstallCallbackMethod(installCallback);
    // 调用方法
    methodInfo_getInstall.env->CallStaticVoidMethod(methodInfo_getInstall.classID,
                                                    methodInfo_getInstall.methodID,
                                                    timeout);

    LOGD("call getInstall success");
}

void AndroidOpenInstall::getInstallCanRetry(float s, void (*installRetryCallback)(AppData appData,
                                                                                  bool retry)) {

    JniMethodInfo methodInfo_getInstall;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getInstall,
                                        "io/openinstall/sdk/OpenInstallHelper",
                                        "getInstallCanRetry",
                                        "(I)V")) {
        LOGE("get OpenInstallHelper.getInstallCanRetry JniMethodInfo failed");
        LOGD("Java 方法调用失败，请获取最新代码并替换文件");
        return;
    }
    int timeout = static_cast<int>(s);
    // 设置回调
    setAppInstallRetryCallbackMethod(installRetryCallback);
    // 调用方法
    methodInfo_getInstall.env->CallStaticVoidMethod(methodInfo_getInstall.classID,
                                                    methodInfo_getInstall.methodID,
                                                    timeout);

    LOGD("call getInstallCanRetry success");
}

void AndroidOpenInstall::registerWakeUpHandler(void (*wakeupCallback)(AppData appData)) {
    JniMethodInfo methodInfo_registerWakeup;
    if (!JniHelper::getStaticMethodInfo(methodInfo_registerWakeup,
                                        "io/openinstall/sdk/OpenInstallHelper",
                                        "registerWakeup",
                                        "()V")) {
        LOGE("get OpenInstallHelper.registerWakeup JniMethodInfo failed");
        LOGD("Java 方法调用失败，请获取最新代码并替换文件");
        return;
    }
    setAppWakeUpCallbackMethod(wakeupCallback);
    // 调用方法
    methodInfo_registerWakeup.env->CallStaticVoidMethod(methodInfo_registerWakeup.classID,
                                                        methodInfo_registerWakeup.methodID);
    LOGD("registerWakeUpHandler success");
}

void AndroidOpenInstall::reportRegister() {
    JniMethodInfo methodInfo_reportRegister;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportRegister,
                                        "io/openinstall/sdk/OpenInstallHelper",
                                        "reportRegister", "()V")) {
        LOGD("get OpenInstallHelper.reportRegister JniMethodInfo failed");
        if (!JniHelper::getStaticMethodInfo(methodInfo_reportRegister,
                                            "com/fm/openinstall/OpenInstall",
                                            "reportRegister", "()V")) {
            LOGE("get OpenInstall.reportRegister JniMethodInfo failed");
            return;
        }
    }

    methodInfo_reportRegister.env->CallStaticVoidMethod(methodInfo_reportRegister.classID,
                                                        methodInfo_reportRegister.methodID);

    LOGD("call reportRegister success");
}

void AndroidOpenInstall::reportEffectPoint(const char *pId, long pValue) {
    JniMethodInfo methodInfo_reportEffectPoint;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportEffectPoint,
                                        "io/openinstall/sdk/OpenInstallHelper",
                                        "reportEffectPoint", "(Ljava/lang/String;J)V")) {
        LOGD("get OpenInstallHelper.reportEffectPoint JniMethodInfo failed");
        if (!JniHelper::getStaticMethodInfo(methodInfo_reportEffectPoint,
                                            "com/fm/openinstall/OpenInstall",
                                            "reportEffectPoint", "(Ljava/lang/String;J)V")) {
            LOGE("get OpenInstall.reportEffectPoint JniMethodInfo failed");
            return;
        }
    }

    jstring pointId = methodInfo_reportEffectPoint.env->NewStringUTF(pId);
    jlong pointValue = (jlong) pValue;
    methodInfo_reportEffectPoint.env->CallStaticVoidMethod(methodInfo_reportEffectPoint.classID,
                                                           methodInfo_reportEffectPoint.methodID,
                                                           pointId,
                                                           pointValue);

    LOGD("call reportEffectPoint success");
}