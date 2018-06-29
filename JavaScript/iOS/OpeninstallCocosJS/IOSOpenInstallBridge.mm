//
//  IOSOpenInstallBridge.m
//  start_project-mobile
//
//  Created by cooper on 2018/6/22.
//

#import "IOSOpenInstallBridge.h"
#import "IOSOpenInstallDelegate.h"

using namespace cocos2d;
@implementation IOSOpenInstallBridge

+(void)getInstall:(NSNumber *)s
{
    int t = 8;
    if ([s intValue]>0) {
        t = [s intValue];
    }
    [[OpenInstallSDK defaultManager] getInstallParmsWithTimeoutInterval:t completed:^(OpeninstallData * _Nullable appData) {
       
        NSString *channelID = @"";
        NSString *datas = @"";
        if (appData.data) {
            datas = [IOSOpenInstallDelegate jsonStringWithObject:appData.data];
        }
        if (appData.channelCode) {
            channelID = appData.channelCode;
        }
        NSDictionary *installDic = @{@"bindData":datas,@"channelCode":channelID};
        NSString *json = [IOSOpenInstallDelegate jsonStringWithObject:installDic];
        std::string jsonStr = [json UTF8String];
        std::string funcName = [@"var openinstall = require(\"OpenInstall\");openinstall._installCallback" UTF8String];
        std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());
        
#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
        se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
        ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif
        
    }];
    
}

+(void)registerWakeUpHandler{
    
    IOSOpenInstallDelegate *callBack = [IOSOpenInstallDelegate defaultManager];
    callBack.isRegister = YES;
    if (callBack.wakeUpJson.length != 0) {
        [IOSOpenInstallDelegate sendWakeUpJsonBack:callBack.wakeUpJson];
        callBack.wakeUpJson = nil;
    }
    
}

+(void)reportRegister{
    
    [OpenInstallSDK reportRegister];
}

+(void)reportEffectPoint:(NSString *)pointId Value:(NSNumber *)pointValue{
    
    [[OpenInstallSDK defaultManager] reportEffectPoint:pointId effectValue:[pointValue longValue]];
}

@end
