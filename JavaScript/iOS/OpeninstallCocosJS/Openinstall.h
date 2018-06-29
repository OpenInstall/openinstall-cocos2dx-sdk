//
//  Openinstall.h
//  hello_world-mobile
//
//  Created by cooper on 2018/6/26.
//

#import <Foundation/Foundation.h>

@interface Openinstall : NSObject

+(void)init;

+(BOOL)setUserActivity:(NSUserActivity*_Nullable)userActivity;

+(BOOL)setLinkURL:(NSURL *_Nullable)URL;

@end
