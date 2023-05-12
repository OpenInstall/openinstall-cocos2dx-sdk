//
// Created by Wenki on 2018/5/28.
//

#include <iostream>
#include "../../../cocos2d/cocos/platform/android/jni/JniHelper.h"
#include "OpenInstallProxy.h"

#include <android/log.h>

#define  LOG_TAG    "OpenInstallProxy"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

AppData jAppData2AppData(jobject jAppData) {
    JNIEnv *env = cocos2d::JniHelper::getEnv();
    // 获取数据
    jclass clsAppData = env->FindClass("com/fm/openinstall/model/AppData");
    jfieldID fID_channel = env->GetFieldID(clsAppData, "channel", "Ljava/lang/String;");
    jfieldID fID_data = env->GetFieldID(clsAppData, "data", "Ljava/lang/String;");
    jstring jChannelCode = (jstring) (env->GetObjectField(jAppData, fID_channel));
    jstring jBindData = (jstring) (env->GetObjectField(jAppData, fID_data));
    //转化
    std::string channelCode = cocos2d::JniHelper::jstring2string(jChannelCode);
    std::string bindData = cocos2d::JniHelper::jstring2string(jBindData);
    AppData appData = AppData(channelCode, bindData);
    return appData;
}


JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_wakeup
        (JNIEnv *env, jobject obj, jobject jAppData) {
    LOGD("AppWakeUpCallback wakeup");
    if (NULL == appWakeUpCallbackMethod) {
        return;
    }
    if (NULL == jAppData) {
        return;
    }
    AppData appData = jAppData2AppData(jAppData);
    appWakeUpCallbackMethod(appData);
}


JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_install
        (JNIEnv *env, jobject obj, jobject jAppData, jboolean jShouldRetry) {
    LOGD("AppInstallCallback install");
    if (NULL == appInstallCallbackMethod) {
        return;
    }
    if (NULL == jAppData) {
        return;
    }
    AppData appData = jAppData2AppData(jAppData);
    appInstallCallbackMethod(appData, jShouldRetry);
}

JNIEXPORT void JNICALL Java_io_openinstall_sdk_OpenInstallCallback_onResult
        (JNIEnv *env, jobject obj, jstring jMethod, jobject jResult, jboolean jShouldRetry, jstring jMessage) {
    LOGD("ResultCallback onResult");
    if (NULL == jMethod) {
        return;
    }

    std::string method_id = cocos2d::JniHelper::jstring2string(jMethod);
    if(method_id == "share"){
        std::string error_msg = cocos2d::JniHelper::jstring2string(jMessage);
        shareResultCallbackMethod(jShouldRetry, error_msg);
    } else {
        LOGD("ResultCallback onResult %s", method_id.c_str());
    }

}

void setAppWakeUpCallbackMethod(void (*callbackMethod)(AppData)) {
    appWakeUpCallbackMethod = callbackMethod;
}

void setAppInstallCallbackMethod(void (*callbackMethod)(AppData, bool)) {
    appInstallCallbackMethod = callbackMethod;
}

void setShareResultCallbackMethod(void (*callbackMethod)(bool, std::string)) {
    shareResultCallbackMethod = callbackMethod;
}