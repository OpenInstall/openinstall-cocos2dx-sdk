//
//  IOSOpenInstallDelegate.m
//  hello_world-mobile
//
//  Created by cooper on 2018/6/26.
//

#import "IOSOpenInstallDelegate.h"

@implementation IOSOpenInstallDelegate

static IOSOpenInstallDelegate *obj = nil;
+(IOSOpenInstallDelegate *)defaultManager{
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (obj == nil)
        {
            obj = [[IOSOpenInstallDelegate alloc] init];
        }
    });
    
    return obj;
}

-(void)getWakeUpParams:(OpeninstallData *)appData{
    
    NSString *channelID = @"";
    NSString *datas = @"";
    if (appData.data) {
        datas = [IOSOpenInstallDelegate jsonStringWithObject:appData.data];
    }
    if (appData.channelCode) {
        channelID = appData.channelCode;
    }
    NSDictionary *installDic = @{@"bindData":datas,@"channelCode":channelID};
    NSString *json = [IOSOpenInstallDelegate jsonStringWithObject:installDic];
    
    if (self.isRegister) {
        [IOSOpenInstallDelegate sendWakeUpJsonBack:json];
        self.wakeUpJson = nil;
    }else{
        self.wakeUpJson = json;
    }
}

+(void)sendWakeUpJsonBack:(NSString *)json{
    
    std::string jsonStr = [json UTF8String];
    std::string funcName = [@"var openinstall = require(\"OpenInstall\");openinstall._wakeupCallback" UTF8String];
    std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());
    
#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
    se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
    ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif

}

+ (NSString *)jsonStringWithObject:(id)jsonObject{
    
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
