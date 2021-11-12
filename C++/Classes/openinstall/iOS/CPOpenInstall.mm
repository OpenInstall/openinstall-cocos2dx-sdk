//
//  CPOpenInstall.m
//  cocos2d_tests
//
//  Created by cooper on 2018/6/5.
//

#import <Foundation/Foundation.h>
#import "CPOpenInstallDelegate.h"
#include "CPOpenInstall.h"
#include "OpenInstallSDK.h"

using namespace openInstall2dx;

//static openInstall2dx::CPOpenInstall::restoreCallBack restoreHandler;
static void(*restoreCallBack)(AppData appData);

void CPOpenInstall::config(iOSConfig adConfig)
{
    if (adConfig.isASAEnabled) {
        
    }
}

void CPOpenInstall::initOpeninstall()
{
    [OpenInstallSDK initWithDelegate:[CPOpenInstallDelegate defaultManager]];
}

void CPOpenInstall::getInstallFrom(float timeout,void (*installCallback)(AppData))
{
    float time = 10.0;
    if (timeout>0) {
        time = timeout;
    }
    [[OpenInstallSDK defaultManager] getInstallParmsWithTimeoutInterval:time completed:^(OpeninstallData * _Nullable appData) {
       
        std::string bindData;
        std::string channelCode;
        if (appData.data) {
            bindData = std::string([[CPOpenInstallDelegate jsonStringWithObject:appData.data] UTF8String]);
        }
        if (appData.channelCode) {
            channelCode = std::string([appData.channelCode UTF8String]);
        }
        AppData newData = AppData(channelCode, bindData);
        
        installCallback(newData);
    }];
}

void CPOpenInstall::registerWakeUpHandlerToNative(void (*wakeupCallback)(AppData))
{
    restoreCallBack = wakeupCallback;
}

void CPOpenInstall::restoreWakeUpHandlerCallBack(AppData appdata)
{
    if (restoreCallBack) {
        restoreCallBack(appdata);
    }
}

void CPOpenInstall::reportRegisterToService(){
    
    [OpenInstallSDK reportRegister];
}

void CPOpenInstall::reportEffectPointToService(const char *poindId, long pointValue)
{
    NSString *pId = [[NSString alloc]initWithCString:poindId encoding:NSASCIIStringEncoding];
    [[OpenInstallSDK defaultManager] reportEffectPoint:pId effectValue:pointValue];
}
