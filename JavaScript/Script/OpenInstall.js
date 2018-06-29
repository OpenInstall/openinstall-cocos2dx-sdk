
var openinstall = {

    wakeupCallback: function(appData){

    },
    installCallback: function(appData){

    },

    getInstall: function (s, callback) {
        this.installCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "getInstall", "(I)V", s);
        } else {
            console.log("not support " + cc.sys.os);
        }
    },
    registerWakeUpHandler: function (callback) {
        this.wakeupCallback = callback;
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity",
                "registerWakeup", "()V");
        } else {
            console.log("not support " + cc.sys.os);
        }
    },

    reportRegister: function () {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("com/fm/openinstall/OpenInstall",
                "reportRegister", "()V");
        } else {
            console.log("not support " + cc.sys.os);
        }
    },

    reportEffectPoint: function (pointId, pointValue) {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("com/fm/openinstall/OpenInstall",
                "reportEffectPoint", "(Ljava/lang/String;J)V", pointId, pointValue);
        } else {
            console.log("not support " + cc.sys.os);
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

