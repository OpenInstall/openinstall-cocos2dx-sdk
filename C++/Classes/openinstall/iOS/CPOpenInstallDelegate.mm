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
    
    id arguments = (jsonObject == nil ? [NSNull null] : jsonObject);
    
    NSArray* argumentsWrappedInArr = [NSArray arrayWithObject:arguments];
    
    NSString* argumentsJSON = [self cp_JSONString:argumentsWrappedInArr];
    
    argumentsJSON = [argumentsJSON substringWithRange:NSMakeRange(1, [argumentsJSON length] - 2)];
    
    return argumentsJSON;
}
+ (NSString *)cp_JSONString:(NSArray *)array{
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:array
                                                       options:0
                                                         error:&error];
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData
                                                 encoding:NSUTF8StringEncoding];
    
    if ([jsonString length] > 0 && error == nil){
        return jsonString;
    }else{
        return @"";
    }
}

@end
