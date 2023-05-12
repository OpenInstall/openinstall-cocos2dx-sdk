//
// Created by Wenki on 2018/6/5.
//

#include "AndroidOpenInstall.h"

#include <jni.h>
#include <platform/android/jni/JniHelper.h>
#include "OpenInstallProxy.h"
#include "AndroidConfig.h"
#include <android/log.h>
#include <map>

using namespace cocos2d;
using namespace std;

extern "C" {

#define  LOG_TAG    "AndroidOpenInstall"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

const char *class_helper = "io/openinstall/sdk/OpenInstallHelper";
const char *method_config_name = "config";
const char *method_config_sig = "(Ljava/util/Map;)V";
const char *method_init_name = "init";
const char *method_init_sig = "(Landroid/content/Context;)V";
const char *method_install_name = "getInstall";
const char *method_install_sig = "(I)V";
const char *method_install_retryable_name = "getInstallCanRetry";
const char *method_install_retryable_sig = "(I)V";
const char *method_wakeup_name = "registerWakeup";
const char *method_wakeup_sig = "(Z)V";
const char *method_register_name = "reportRegister";
const char *method_register_sig = "()V";
const char *method_event_name = "reportEffectPoint";
const char *method_event_sig = "(Ljava/lang/String;JLjava/util/Map;)V";
const char *method_share_name = "reportShare";
const char *method_share_sig = "(Ljava/lang/String;Ljava/lang/String;)V";

jobject covert_map(StringMap stringMap){
    JniMethodInfo methodInfo_newMap;
    if (!JniHelper::getMethodInfo(methodInfo_newMap,
                                        "java/util/HashMap",
                                        "<init>",
                                        "()V")) {
        LOGD("get HashMap.<init> JniMethodInfo failed");
        return NULL;
    }
    JniMethodInfo methodInfo_putMap;
    if (!JniHelper::getMethodInfo(methodInfo_putMap,
                                        "java/util/HashMap",
                                        "put",
                                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
        LOGD("get HashMap.put JniMethodInfo failed");
        return NULL;
    }

    jobject jMap = methodInfo_newMap.env -> NewObject(methodInfo_newMap.classID, methodInfo_newMap.methodID);

    StringMap::iterator it;
    StringMap::iterator itEnd;
    it = stringMap.begin();
    itEnd = stringMap.end();
    while (it != itEnd) {
        printf("OpenInstall key=%s, value=%s", it->first.c_str(), it->second.c_str());
        methodInfo_putMap.env-> CallObjectMethod(jMap, methodInfo_putMap.methodID,
                                       methodInfo_putMap.env->NewStringUTF(
                                               it->first.c_str()),
                                       methodInfo_putMap.env->NewStringUTF(
                                               it->second.c_str()));
        it++;
    }
    return jMap;
}

void AndroidOpenInstall::config(AndroidConfig adConfig) {
    JniMethodInfo methodInfo_config;
    if (!JniHelper::getStaticMethodInfo(methodInfo_config,
                                        class_helper,
                                        method_config_name,
                                        method_config_sig)) {
        LOGE("get OpenInstallHelper.config JniMethodInfo failed");
        return;
    }

    jobject jMap = covert_map(adConfig.getProperties());

    methodInfo_config.env->CallStaticVoidMethod(methodInfo_config.classID,
                                                methodInfo_config.methodID,
                                                jMap);

    LOGD("call config success");
}

void AndroidOpenInstall::init() {
    JniMethodInfo methodInfo_init;
    if (!JniHelper::getStaticMethodInfo(methodInfo_init,
                                        class_helper,
                                        method_init_name,
                                        method_init_sig)) {
        LOGE("get OpenInstallHelper.init JniMethodInfo failed");
        return;
    }

    JniMethodInfo methodInfo_getContext;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getContext,
                                        "org/cocos2dx/lib/Cocos2dxActivity",
                                        "getContext",
                                        "()Landroid/content/Context;")) {
        LOGE("get Cocos2dxActivity.getContext JniMethodInfo failed");
        return;
    }

    jobject jContext = methodInfo_getContext.env->CallStaticObjectMethod(
            methodInfo_getContext.classID,
            methodInfo_getContext.methodID);

    methodInfo_init.env->CallStaticVoidMethod(methodInfo_init.classID,
                                              methodInfo_init.methodID,
                                              jContext);

    methodInfo_getContext.env->DeleteLocalRef(jContext);

    LOGD("call init success");

}


void AndroidOpenInstall::getInstall(float s, void (*installCallback)(AppData, bool)) {

    JniMethodInfo methodInfo_getInstall;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getInstall,
                                        class_helper,
                                        method_install_name,
                                        method_install_sig)) {
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

void AndroidOpenInstall::getInstallCanRetry(float s, void (*installCallback)(AppData, bool)) {

    JniMethodInfo methodInfo_getInstall;
    if (!JniHelper::getStaticMethodInfo(methodInfo_getInstall,
                                        class_helper,
                                        method_install_retryable_name,
                                        method_install_retryable_sig)) {
        LOGE("get OpenInstallHelper.getInstallCanRetry JniMethodInfo failed");
        return;
    }
    int timeout = static_cast<int>(s);
    // 设置回调
    setAppInstallCallbackMethod(installCallback);
    // 调用方法
    methodInfo_getInstall.env->CallStaticVoidMethod(methodInfo_getInstall.classID,
                                                    methodInfo_getInstall.methodID,
                                                    timeout);

    LOGD("call getInstallCanRetry success");
}

void AndroidOpenInstall::registerWakeUpHandler(void (*wakeupCallback)(AppData)) {
    JniMethodInfo methodInfo_registerWakeup;
    if (!JniHelper::getStaticMethodInfo(methodInfo_registerWakeup,
                                        class_helper,
                                        method_wakeup_name,
                                        method_wakeup_sig)) {
        LOGE("get OpenInstallHelper.registerWakeup JniMethodInfo failed");
        return;
    }
    setAppWakeUpCallbackMethod(wakeupCallback);
    // 调用方法
    methodInfo_registerWakeup.env->CallStaticVoidMethod(methodInfo_registerWakeup.classID,
                                                        methodInfo_registerWakeup.methodID,
                                                        JNI_FALSE);
    LOGD("call registerWakeUpHandler success");
}


void AndroidOpenInstall::reportRegister() {
    JniMethodInfo methodInfo_reportRegister;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportRegister,
                                        class_helper,
                                        method_register_name,
                                        method_register_sig)) {
        LOGD("get OpenInstallHelper.reportRegister JniMethodInfo failed");
        return;
    }

    methodInfo_reportRegister.env->CallStaticVoidMethod(methodInfo_reportRegister.classID,
                                                        methodInfo_reportRegister.methodID);

    LOGD("call reportRegister success");
}


void AndroidOpenInstall::reportEffectPoint(const char *pointId, long pointValue) {
    reportEffectPoint(pointId, pointValue, StringMap());
}


void AndroidOpenInstall::reportEffectPoint(const char *pointId, long pointValue,
                                           StringMap extraMap) {
    JniMethodInfo methodInfo_reportEffectPoint;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportEffectPoint,
                                        class_helper,
                                        method_event_name,
                                        method_event_sig)) {
        LOGD("get OpenInstallHelper.reportEffectPoint JniMethodInfo failed");
        return;
    }

    jstring jPointId = methodInfo_reportEffectPoint.env->NewStringUTF(pointId);
    jlong jPointValue = (jlong) pointValue;


    jobject jMap = covert_map(extraMap);

    methodInfo_reportEffectPoint.env->CallStaticVoidMethod(methodInfo_reportEffectPoint.classID,
                                                           methodInfo_reportEffectPoint.methodID,
                                                           jPointId,
                                                           jPointValue,
                                                           jMap);

    LOGD("call reportEffectPoint success");
}

void AndroidOpenInstall::reportShare(const char *shareCode,
                                     const char *sharePlatform,
                                     void (*callbackMethod)(bool, string)) {
    JniMethodInfo methodInfo_reportShare;
    if (!JniHelper::getStaticMethodInfo(methodInfo_reportShare,
                                        class_helper,
                                        method_share_name,
                                        method_share_sig)) {
        LOGD("get OpenInstallHelper.reportShare JniMethodInfo failed");
        return;
    }

    setShareResultCallbackMethod(callbackMethod);

    jstring jShareCode = methodInfo_reportShare.env->NewStringUTF(shareCode);
    jstring jSharePlatform = methodInfo_reportShare.env->NewStringUTF(sharePlatform);
    methodInfo_reportShare.env->CallStaticVoidMethod(methodInfo_reportShare.classID,
                                                     methodInfo_reportShare.methodID,
                                                     jShareCode, jSharePlatform);

    LOGD("call reportShare success");
}
}