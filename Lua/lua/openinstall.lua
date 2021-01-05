
local targetPlatform = cc.Application:getInstance():getTargetPlatform()

local openinstall = class("openinstall")

local activityClassName = "org/cocos2dx/lua/AppActivity"
local openinstallClassName = "com/fm/openinstall/OpenInstall"

function openinstall:config(adEnabled, oaid, gaid)
	print("call config start")
	if(oaid == nil) then
		oaid = "null"
	end
	if(gaid == nil) then
		gaid = "null"
	end
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {adEnabled,oaid,gaid}
		local signs = "(ZLjava/lang/String;Ljava/lang/String;)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "config", args, signs)
		if not ok then
			print("call config fail"..ret)
		end
	end
end

function openinstall:init(permission)
	print("call init start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {permission}
		local signs = "(Z)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "init", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	end
end

function openinstall:getInstall(s, callback)
	print("call getInstall start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {s, callback}
		local signs = "(II)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "getInstall", args, signs)
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


function openinstall:registerWakeupHandler(callback)
	print("call registerWakeupHandler start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {callback}
		local signs = "(I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "registerWakeupCallback", args, signs)
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

function openinstall:reportEffectPoint(pointId, pointValue)
	print("call reportEffectPoint start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {pointId, pointValue}
		local signs = "(Ljava/lang/String;I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "reportEffectPoint", args, signs)
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
