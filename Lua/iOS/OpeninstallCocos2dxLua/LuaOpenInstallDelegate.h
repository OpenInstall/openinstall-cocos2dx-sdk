//
//  LuaOpenInstallDelegate.h
//  openinstall-Lua-SDk
//
//  Created by cooper on 2019/1/15.
//

#import <Foundation/Foundation.h>
#import "OpenInstallSDK.h"
#import "cocos2d.h"
USING_NS_CC;
#include "scripting/lua-bindings/manual/CCLuaEngine.h"
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_MAC)
#include "scripting/lua-bindings/manual/platform/ios/CCLuaObjcBridge.h"
#endif

@interface LuaOpenInstallDelegate : NSObject<OpenInstallDelegate>

@property (nonatomic, copy)NSString *wakeUpJson;
@property (nonatomic, assign)BOOL isRegister;
@property (nonatomic, assign)int funcId;

+(LuaOpenInstallDelegate *)defaultManager;
+(void)sendWakeUpJsonBack:(NSString *)json;
+(NSString *)jsonStringWithObject:(id)jsonObject;

@end
