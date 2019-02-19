//
//  LuaOpenInstallBridge.m
//  openinstall-Lua-SDk
//
//  Created by cooper on 2019/1/15.
//

#import "LuaOpenInstallBridge.h"
#import "LuaOpenInstallDelegate.h"

@implementation LuaOpenInstallBridge

+(void)getInstall:(NSDictionary *)dict
{
    int timeInterval = 8;
    if ([dict objectForKey:@"time"]) {
        timeInterval = [[dict objectForKey:@"time"] intValue];
        if (timeInterval <= 0) {
            timeInterval = 8;
        }
    }
    [[OpenInstallSDK defaultManager] getInstallParmsWithTimeoutInterval:timeInterval completed:^(OpeninstallData * _Nullable appData) {
        
        NSDictionary *installDic = @{@"bindData":appData.data?:@"",@"channelCode":appData.channelCode?:@""};
        NSString *json = [LuaOpenInstallDelegate jsonStringWithObject:installDic];
        NSLog(@"openinstall:iOS原生层获取到返回的安装参数为%@",json);
                
        int functionId = [[dict objectForKey:@"functionId"] intValue];
        
        LuaObjcBridge::pushLuaFunctionById(functionId);
        
        //将需要传递给 Lua function 的参数放入 Lua stack
        LuaObjcBridge::getStack()->pushString([json UTF8String]);//返回json字串
        LuaObjcBridge::getStack()->executeFunction(1);//1个参数
        LuaObjcBridge::releaseLuaFunctionById(functionId);//释放
        
        //        cocos2d::LuaEngine::getInstance()->executeGlobalFunction("GlobalFunction");//GlobalFunction是Lua全局函数名
    }];
    
}

+(void)registerWakeUpHandler:(NSDictionary *)dict{
    int funcId = [[dict objectForKey:@"functionId"] intValue];
    [LuaOpenInstallDelegate defaultManager].funcId = funcId;//保存当前funcId
    
    LuaOpenInstallDelegate *callBack = [LuaOpenInstallDelegate defaultManager];
    callBack.isRegister = YES;
    if (callBack.wakeUpJson.length != 0) {
        [LuaOpenInstallDelegate sendWakeUpJsonBack:callBack.wakeUpJson];
        callBack.wakeUpJson = nil;
    }
    
}

+(void)reportRegister{
    
    [OpenInstallSDK reportRegister];
    NSLog(@"openinstall: iOS原生层已调用注册统计方法");
}

+(void)reportEffectPoint:(NSDictionary *)dict{
    NSString *pointId = [dict objectForKey:@"pointId"];
    long pointValue = [[dict objectForKey:@"pointValue"] longValue];
    [[OpenInstallSDK defaultManager] reportEffectPoint:pointId effectValue:pointValue];
    NSLog(@"openinstall: iOS原生层已调用效果点统计方法 pointId = %@,pointValue = %ld",pointId, pointValue);
}

@end
