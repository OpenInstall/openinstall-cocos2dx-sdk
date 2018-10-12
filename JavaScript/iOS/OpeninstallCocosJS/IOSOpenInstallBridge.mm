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
#ifndef HAVE_INSPECTOR
        std::string funcName = [@"var openinstall = require(\"OpenInstall\");openinstall._installCallback" UTF8String];
#else
        std::string funcName = [@"var openinstall = window.__require(\"OpenInstall\");openinstall._installCallback" UTF8String];
#endif
        std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());

#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
        BOOL success = se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
        BOOL success = ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif
        
        if (!success) {
            NSLog(@"---OpenInstallJS---:将通过直接引用的方式进行回调。the callback will be made by direct reference.---");
            std::string funcName = [@"_installCallback" UTF8String];
            std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());
            
#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
            BOOL s = se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
            BOOL s = ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif
            if (!s) {
                NSLog(@"---OpenInstallJS---:回调失败，请在调用getInstall方法的地方，添加_installCallback回调方法，以获取回调数据。Callback failure,please add a method named '_installCallback' to location where you call the method named 'getInstall'. e.g.---->_installCallback: function (appData) {cc.log('channelCode=' + appData.channelCode + ', bindData=' + appData.bindData)}----"
                    );
            }
        }else{
            NSLog(@"---OpenInstallJS---:安装参数回调成功。Installation parameters Callback success.----");
        }
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
