//
//  Openinstall.m
//  openinstall-Lua-SDk
//
//  Created by cooper on 2018/6/26.
//

#import "Openinstall.h"
#import "OpenInstallSDK.h"
#import "LuaOpenInstallDelegate.h"

#import <AdSupport/AdSupport.h>
#import <AppTrackingTransparency/AppTrackingTransparency.h>

#import <AdServices/AAAttribution.h>

@implementation Openinstall

+(void)init{
    //同时使用了广告平台渠道统计和ASA渠道统计的代码示例
    if (@available(iOS 14, *)) {
        //权限申请
        [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            [self opInit];
        }];
    }else{
        [self opInit];
    }
}

+(void)opInit{
    //ASA广告归因
    NSMutableDictionary *config = [[NSMutableDictionary alloc]init];
    if (@available(iOS 14.3, *)) {
        NSError *error;
        NSString *token = [AAAttribution attributionTokenWithError:&error];
        [config setValue:token forKey:OP_ASA_Token];
    }
    //第三方广告平台统计代码
    NSString *idfaStr = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    [config setValue:idfaStr forKey:OP_Idfa_Id];
    
    [OpenInstallSDK initWithDelegate:[LuaOpenInstallDelegate defaultManager] adsAttribution:config];
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
