//
//  Openinstall.m
//  openinstall-Lua-SDk
//
//  Created by cooper on 2018/6/26.
//

#import "Openinstall.h"
#import "OpenInstallSDK.h"
#import "LuaOpenInstallDelegate.h"

@implementation Openinstall

+(void)init{
    
    [OpenInstallSDK initWithDelegate:[LuaOpenInstallDelegate defaultManager]];
}

+(BOOL)setUserActivity:(NSUserActivity*_Nullable)userActivity{
    
    if ([OpenInstallSDK continueUserActivity:userActivity]) {
        
        return YES;
    }
    
    return NO;
}

+(BOOL)setLinkURL:(NSURL *_Nullable)URL{
    
    if ([OpenInstallSDK handLinkURL:URL]) {
        
        return YES;
    }
    
    return NO;
}

@end
