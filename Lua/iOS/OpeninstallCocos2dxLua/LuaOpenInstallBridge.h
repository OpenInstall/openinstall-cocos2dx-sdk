//
//  LuaOpenInstallBridge.h
//  openinstall-Lua-SDk
//
//  Created by cooper on 2019/1/15.
//

#import <Foundation/Foundation.h>

@interface LuaOpenInstallBridge : NSObject

+(void)getInstall:(NSDictionary *)dict;

+(void)registerWakeUpHandler:(NSDictionary *)dict;

+(void)reportRegister;

+(void)reportEffectPoint:(NSDictionary *)dict;

@end
