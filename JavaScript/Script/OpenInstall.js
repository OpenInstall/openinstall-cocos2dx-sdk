
var openinstall = {

    wakeupCallback: function(appData){

    },
    installCallback: function(appData){

    },

	config: function(adEnabled, oaid, gaid){
		if (cc.sys.OS_ANDROID == cc.sys.os) {
			cc.log("adEnabled = " + adEnabled + ", oaid = " + oaid +", gaid = " + gaid);
			if(null == oaid){
				oaid = "null";
			}
			if(null == gaid){
				gaid = "null";
			}
			jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "config", "(ZLjava/lang/String;Ljava/lang/String;)V", adEnabled, oaid, gaid);   
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
	},

	init: function(permission){
		if (cc.sys.OS_ANDROID == cc.sys.os) {
			cc.log("permission = " + permission);
			jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "init", "(Z)V", permission);   
        } else if(cc.sys.OS_IOS == cc.sys.os){
            //cc.log("此方法仅适用于Android平台");
        }
	},

    getInstall: function (s, callback) {
        this.installCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "getInstall", "(I)V", s);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","getInstall:",s);
        }
    },
    registerWakeUpHandler: function (callback) {
        this.wakeupCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "registerWakeup", "()V");
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","registerWakeUpHandler");
        }
    },

    reportRegister: function () {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("com/fm/openinstall/OpenInstall",
                "reportRegister", "()V");
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","reportRegister");
        }
    },

    reportEffectPoint: function (pointId, pointValue) {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("com/fm/openinstall/OpenInstall",
                "reportEffectPoint", "(Ljava/lang/String;J)V", pointId, pointValue);
        } else if(cc.sys.OS_IOS == cc.sys.os){
            jsb.reflection.callStaticMethod("IOSOpenInstallBridge","reportEffectPoint:Value:",pointId,pointValue);
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

};

module.exports = openinstall;

