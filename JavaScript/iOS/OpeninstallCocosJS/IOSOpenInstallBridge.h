//
//  IOSOpenInstallBridge.h
//  start_project-mobile
//
//  Created by cooper on 2018/6/22.
//

#import <Foundation/Foundation.h>

@interface IOSOpenInstallBridge : NSObject

+(void)getInstall:(NSNumber *)s;

+(void)registerWakeUpHandler;

+(void)reportRegister;

+(void)reportEffectPoint:(NSString *)pointId Value:(NSNumber *)pointValue;

@end
