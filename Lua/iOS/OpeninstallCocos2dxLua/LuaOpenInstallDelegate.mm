//
//  LuaOpenInstallDelegate.m
//  openinstall-Lua-SDk
//
//  Created by cooper on 2019/1/15.
//

#import "LuaOpenInstallDelegate.h"

@implementation LuaOpenInstallDelegate

static LuaOpenInstallDelegate *obj = nil;
+(LuaOpenInstallDelegate *)defaultManager{
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (obj == nil)
        {
            obj = [[LuaOpenInstallDelegate alloc] init];
        }
    });
    
    return obj;
}

-(void)getWakeUpParams:(OpeninstallData *)appData{
    
    NSDictionary *wakeupDic = @{@"bindData":appData.data?:@"",@"channelCode":appData.channelCode?:@""};
    NSString *json = [LuaOpenInstallDelegate jsonStringWithObject:wakeupDic];
    
    if (self.isRegister) {
        [LuaOpenInstallDelegate sendWakeUpJsonBack:json];
        self.wakeUpJson = nil;
    }else{
        self.wakeUpJson = json;
    }
}

+(void)sendWakeUpJsonBack:(NSString *)json{
    NSLog(@"openinstall:ios原生层返回的拉起参数json串为%@",json);
    LuaObjcBridge::pushLuaFunctionById([LuaOpenInstallDelegate defaultManager].funcId);
    LuaObjcBridge::getStack()->pushString([json UTF8String]);//返回json字串
    LuaObjcBridge::getStack()->executeFunction(1);//1个参数
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
