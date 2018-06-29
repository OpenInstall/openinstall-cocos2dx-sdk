//
//  CPOpenInstallDelegate.h
//  cocos2d_tests
//
//  Created by cooper on 2018/6/5.
//

#include "AppData.h"
#include "CPOpenInstall.h"
#import <Foundation/Foundation.h>
#import "OpenInstallSDK.h"

using namespace openInstall2dx;

/**
 *  回调代理
 */
@interface CPOpenInstallDelegate : NSObject<OpenInstallDelegate>


+(CPOpenInstallDelegate *)defaultManager;

+ (NSString *)jsonStringWithObject:(id)jsonObject;

@end
