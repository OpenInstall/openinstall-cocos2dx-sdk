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
    NSDictionary *wakeupDic = @{@"bindData":datas,@"channelCode":channelID};
    NSString *json = [IOSOpenInstallDelegate jsonStringWithObject:wakeupDic];
    
    if (self.isRegister) {
        [IOSOpenInstallDelegate sendWakeUpJsonBack:json];
        self.wakeUpJson = nil;
    }else{
        self.wakeUpJson = json;
    }
}

+(void)sendWakeUpJsonBack:(NSString *)json{
    
    std::string jsonStr = [json UTF8String];
#ifndef HAVE_INSPECTOR
    std::string funcName = [@"var openinstall = require(\"OpenInstall\");openinstall._wakeupCallback" UTF8String];
#else
    std::string funcName = [@"var openinstall = window.__require(\"OpenInstall\");openinstall._wakeupCallback" UTF8String];
#endif
    std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());
    
#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
    BOOL success = se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
    BOOL success = ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif
    
    if (!success) {
        NSLog(@"---OpenInstallJS---:将通过直接引用的方式进行回调。the callback will be made by direct reference.---");
        std::string funcName = [@"_wakeupCallback" UTF8String];
        std::string jsCallStr = cocos2d::StringUtils::format("%s(%s);", funcName.c_str(),jsonStr.c_str());
        
#if CC_FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX
        BOOL s = se::ScriptEngine::getInstance()->evalString(jsCallStr.c_str());
#else
        BOOL s = ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
#endif
        if (!s) {
            NSLog(@"---OpenInstallJS---:回调失败，请在调用registerWakeUpHandler方法的地方，添加_installCallback回调方法，以获取回调数据。Callback failure,please add a method named '_wakeupCallback' to location where you call the method named 'registerWakeUpHandler'. e.g.---->_wakeupCallback: function (appData) {cc.log('channelCode=' + appData.channelCode + ', bindData=' + appData.bindData)}----"
                  );
        }
    }else{
        NSLog(@"---OpenInstallJS---:拉起参数回调成功。Installation parameters Callback success.----");
    }

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
