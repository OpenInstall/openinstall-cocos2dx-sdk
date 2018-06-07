//
//  CPOpenInstallDelegate.m
//  cocos2d_tests
//
//  Created by cooper on 2018/6/5.
//

#import "CPOpenInstallDelegate.h"
#import "CPOpenInstall.h"

using namespace openInstall2dx;

@implementation CPOpenInstallDelegate

+(CPOpenInstallDelegate *)defaultManager{
    
    static CPOpenInstallDelegate *obj;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (obj == nil)
        {
            obj = [[CPOpenInstallDelegate alloc] init];
        }
    });
    
    return obj;
}

-(void)getWakeUpParams:(OpeninstallData *)appData{
    
    std::string bindData;
    std::string channelCode;
    if (appData.data) {
        bindData = std::string([[CPOpenInstallDelegate jsonStringWithObject:appData.data] UTF8String]);
    }
    if (appData.channelCode) {
        channelCode = std::string([appData.channelCode UTF8String]);
    }
    AppData newData = AppData(channelCode, bindData);
    
    CPOpenInstall::restoreWakeUpHandlerCallBack(newData);
}

+ (NSString *)jsonStringWithObject:(id)jsonObject{
    
    // 将字典或者数组转化为JSON串
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonObject
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData
                                                 encoding:NSUTF8StringEncoding];
    
    if ([jsonString length] > 0 && error == nil){
        
        jsonString = [jsonString stringByReplacingOccurrencesOfString:@"\n" withString:@""];
        
        return jsonString;
    }else{
        return @"";
    }
    
}

@end
