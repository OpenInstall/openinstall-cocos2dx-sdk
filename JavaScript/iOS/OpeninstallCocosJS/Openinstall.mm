//
//  Openinstall.m
//  hello_world-mobile
//
//  Created by cooper on 2018/6/26.
//

#import "Openinstall.h"
#import "OpenInstallSDK.h"
#import "IOSOpenInstallDelegate.h"

@implementation Openinstall

+(void)init{
    
    [OpenInstallSDK initWithDelegate:[IOSOpenInstallDelegate defaultManager]];
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
