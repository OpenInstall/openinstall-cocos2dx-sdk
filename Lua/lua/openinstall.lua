
local targetPlatform = cc.Application:getInstance():getTargetPlatform()

local openinstall = class("openinstall")

local openinstallClassName = "io/openinstall/cocos2dx/OpenInstallHelper"

-- 用于将 table 转化成结构为 k1=v1&k2=v2 的字符串 
local function convert_params(mytable)
	local params = ""
	if type(mytable) ~= "table" then 
		print("convert is not table")
		return params
	end
	local notFirst = false
	for k,v in pairs(mytable) do

		local kType = type(k)
		if kType ~= "string" then
			print("config option key is not string type")
			goto continue
		end

		local vStr = ""
		local vType = type(v)
		if vType == "boolean" or vType == "number" or vType == "string" then
			vStr = v
		else
			print("config option value is not string or boolean")
			goto continue
		end
		if notFirst then
			params = params.."&"
		end
		params = params..k.."="..vStr
		notFirst = true
		::continue::
	end
	return params
end

-- 预初始化
function openinstall:preInit()
	print("call preInit start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {}
		local signs = "()V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "preInit", args, signs)
		if not ok then
			print("call preInit fail"..ret)
		end
	end
end

--[[
 移除 config(adEnabled, oaid, gaid) 接口
 使用新接口进行Android平台的广告接入配置
]]--
function openinstall:configAndroid(optionTable)
	print("call configAndroid start")
	
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local optionParam = convert_params(optionTable)
		local args = {optionParam}
		local signs = "(Ljava/lang/String;)V"
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

-- 获取安装参数
function openinstall:getInstallCanRetry(s, callback)
	print("call getInstallCanRetry start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {s, callback}
		local signs = "(II)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "getInstallCanRetry", args, signs)
		if not ok then
			print("call getInstallCanRetry fail"..ret)
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
	openinstall:reportEffectPoint(pointId, pointValue, nil)
end

function openinstall:reportEffectPoint(pointId, pointValue, extraTable)
	print("call reportEffectPoint start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local extraParam = convert_params(extraTable)
		local args = {pointId, pointValue, extraParam}
		local signs = "(Ljava/lang/String;ILjava/lang/String;)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "reportEffectPoint", args, signs)
		if not ok then
            print("call reportEffectPoint fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
		-- 暂时还没有实现效果点明细上报
        local luaoc = require "cocos.cocos2d.luaoc"
        local args = {pointId = pointId, pointValue = pointValue}
        local ok, ret = luaoc.callStaticMethod("LuaOpenInstallBridge","reportEffectPoint",args)
        if not ok then
            print("luaoc reportEffectPoint error:"..ret)
        end
    end
end

function openinstall:reportShare(shareCode, sharePlatform, callback)
	print("call reportShare start")
	if (cc.PLATFORM_OS_ANDROID == targetPlatform) then
        local luaj = require "cocos.cocos2d.luaj"
		local args = {shareCode, sharePlatform, callback}
		local signs = "(Ljava/lang/String;Ljava/lang/String;I)V"
		local ok,ret = luaj.callStaticMethod(openinstallClassName, "reportShare", args, signs)
		if not ok then
            print("call reportShare fail"..ret)
		end
	end
    if (cc.PLATFORM_OS_IPHONE == targetPlatform) or (cc.PLATFORM_OS_IPAD == targetPlatform) then
        print("not impl")
    end
end

return openinstall
