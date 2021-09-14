
local targetPlatform = cc.Application:getInstance():getTargetPlatform()

local openinstall = class("openinstall")

local openinstallClassName = "io/openinstall/cocos2dx/OpenInstallHelper"

--[[
 移除 config(adEnabled, oaid, gaid) 接口
 使用新接口进行Android平台的广告接入配置
]]--
function openinstall:configAndroid(options)
	print("call configAndroid start")
	if(options["adEnabled"] == nil) then
		options["adEnabled"] = false
	end
	if(options["oaid"] == nil) then
		options["oaid"] = "nil"
	end
	if(options["gaid"] == nil) then
		options["gaid"] = "nil"
	end
	if(options["macDisabled"] == nil) then
		options["macDisabled"] = false
	end
	if(options["imeiDisabled"] == nil) then
		options["imeiDisabled"] = false
	end
	
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {options["adEnabled"], options["oaid"], options["gaid"], options["macDisabled"], options["imeiDisabled"]}
		local signs = "(ZLjava/lang/String;Ljava/lang/String;ZZ)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "config", args, signs)
		if not ok then
			print("call config fail"..ret)
		end
	end
end

-- 初始化
function openinstall:init()
	print("call init start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {}
		local signs = "()V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "init", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	end
end

-- 获取安装参数
function openinstall:getInstall(s, callback)
	print("call getInstall start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {s, callback}
		local signs = "(II)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "getInstall", args, signs)
		if not ok then
			print("call getInstall fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
        local luaoc = require "cocos.cocos2d.luaoc"
        local args = {functionId = callback,time = s}
        local ok, ret = luaoc.callStaticMethod("LuaOpenInstallBridge","getInstall",args)
        if not ok then
            print("luaoc getInstall error:"..ret)
        end
    end
end

-- 注册跳转回调，获取跳转参数
function openinstall:registerWakeupHandler(callback)
	print("call registerWakeupHandler start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {callback}
		local signs = "(I)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "registerWakeupCallback", args, signs)
		if not ok then
			print("call registerWakeupHandler fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
        local luaoc = require "cocos.cocos2d.luaoc"
        local args = {functionId = callback}
        local ok, ret = luaoc.callStaticMethod("LuaOpenInstallBridge","registerWakeUpHandler",args)
        if not ok then
            print("luaoc registerWakeUpHandler error:"..ret)
        end
    end
end

-- 上报注册量
function openinstall:reportRegister()
	print("call reportRegister start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {}
		local signs = "()V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "reportRegister", args, signs)
		if not ok then
            print("call reportRegister fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
        local luaoc = require "cocos.cocos2d.luaoc"
        local ok, ret = luaoc.callStaticMethod("LuaOpenInstallBridge","reportRegister")
        if not ok then
            print("luaoc reportRegister error:"..ret)
        end
    end
end

-- 上报效果点
function openinstall:reportEffectPoint(pointId, pointValue)
	print("call reportEffectPoint start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {pointId, pointValue}
		local signs = "(Ljava/lang/String;I)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "reportEffectPoint", args, signs)
		if not ok then
            print("call reportEffectPoint fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
        local luaoc = require "cocos.cocos2d.luaoc"
        local args = {pointId = pointId, pointValue = pointValue}
        local ok, ret = luaoc.callStaticMethod("LuaOpenInstallBridge","reportEffectPoint",args)
        if not ok then
            print("luaoc reportEffectPoint error:"..ret)
        end
    end
end

return openinstall
