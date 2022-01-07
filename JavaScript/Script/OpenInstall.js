
var openinstall = {

    wakeupCallback: function(appData){

    },
    installCallback: function(appData){

    },

	configAndroid: function(options){
		if (cc.sys.OS_ANDROID == cc.sys.os) {
			// adEnabled, oaid, gaid, macDisabled, imeiDisabled
			cc.log("adEnabled = " + options.adEnabled + ", oaid = " + options.oaid +", gaid = " + options.gaid 
				+ ", macDisabled = " + options.macDisabled + ", imeiDisabled = " + options.imeiDisabled);
			if(!options.oaid){
				options.oaid = "undefined";
			}
			if(!options.gaid){
				options.gaid = "undefined";
			}
			jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "config", "(ZLjava/lang/String;Ljava/lang/String;ZZ)V", 
				options.adEnabled, options.oaid, options.gaid, options.macDisabled, options.imeiDisabled);   
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
	
    registerWakeUpHandler: function (callback, alwaysCallback) {
        this.wakeupCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
                "registerWakeup", "(Z)V", alwaysCallback||false);
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

    reportEffectPoint: function (pointId, pointValue) {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("io/openinstall/cocos2dx/OpenInstallHelper",
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

