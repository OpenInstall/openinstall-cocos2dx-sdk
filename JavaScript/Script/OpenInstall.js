
var openinstall = {

    wakeupCallback: function(appData){

    },
    installCallback: function(appData){

    },
    shareCallback: function(result){

    },

    preInit: function(){
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "preInit", "()V");   
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
    },

    configAndroid: function(options){
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            var jsonStr = JSON.stringify(options);
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "config", "(Ljava/lang/String;)V", jsonStr);   
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
    },

    init: function(){
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "init", "()V");   
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
    },

    getInstall: function (s, callback) {
        this.installCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "getInstall", "(I)V", s);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","getInstall:",s);
        }
    },
    
    getInstallCanRetry: function (s, callback) {
        this.installCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "getInstallCanRetry", "(I)V", s);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
    },
    
    registerWakeUpHandler: function (callback) {
        this.wakeupCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "registerWakeup", "()V");
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","registerWakeUpHandler");
        }
    },

    reportRegister: function () {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "reportRegister", "()V");
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","reportRegister");
        }
    },

    reportEffectPoint: function (pointId, pointValue, extraParam) {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            var jsonStr = "{}";
            if(extraParam){
                jsonStr = JSON.stringify(extraParam);
            }
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "reportEffectPoint", "(Ljava/lang/String;ILjava/lang/String;)V", pointId, pointValue, jsonStr);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            // 未实现效果点明细
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","reportEffectPoint:Value:",pointId,pointValue);
        }
    },

    reportShare: function (shareCode, sharePlatform, callback) {
        this.shareCallback = callback
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "reportShare", "(Ljava/lang/String;Ljava/lang/String;)V", shareCode, sharePlatform);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            cc.log("not impl")
        }
    },

    _installCallback: function (appData) {
        //cc.log("安装参数：channelCode=" + appData.channelCode + ", bindData=" + appData.bindData);
        this.installCallback(appData);
    },

    _wakeupCallback: function (appData) {
        //cc.log("拉起参数：channelCode=" + appData.channelCode + ", bindData=" + appData.bindData);
        this.wakeupCallback(appData);
    },

    _shareCallback: function (result) {
        // cc.log("分享统计回调：" + JSON.stringify(result))
        this.shareCallback(result);
    }

};

module.exports = openinstall;

