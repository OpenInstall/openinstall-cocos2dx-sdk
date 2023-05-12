//
// Created by wade on 2021/9/6.
//

#include "AndroidConfig.h"

using namespace openInstall2dx;
using namespace std;


map<string, string> AndroidConfig::getProperties(){
	return properties;
}

string convert_bool(bool b){
	return b?"true":"false";
}

void AndroidConfig::setAdEnabled(bool adEnabled){
	properties.insert(map<string, string>::value_type("adEnabled", convert_bool(adEnabled)));
}

void AndroidConfig::setOaid(string oaid){
	properties.insert(map<string, string>::value_type("oaid", oaid));
}

void AndroidConfig::setGaid(string gaid){
	properties.insert(map<string, string>::value_type("gaid", gaid));
}

void AndroidConfig::setImeiDisabled(bool imeiDisabled){
	properties.insert(map<string, string>::value_type("imeiDisabled", convert_bool(imeiDisabled)));
}

void AndroidConfig::setImei(string imei){
	properties.insert(map<string, string>::value_type("imei", imei));
}

void AndroidConfig::setMacDisabled(bool macDisabled){
	properties.insert(map<string, string>::value_type("macDisabled", convert_bool(macDisabled)));
}

void AndroidConfig::setMacAddress(string macAddress){
	properties.insert(map<string, string>::value_type("macAddress", macAddress));
}

void AndroidConfig::setAndroidId(string androidId){
	properties.insert(map<string, string>::value_type("androidId", androidId));
}

void AndroidConfig::setSerialNumber(string serialNumber){
	properties.insert(map<string, string>::value_type("serialNumber", serialNumber));
}

void AndroidConfig::setSimulatorDisabled(bool simulatorDisabled){
	properties.insert(map<string, string>::value_type("simulatorDisabled", convert_bool(simulatorDisabled)));
}

void AndroidConfig::setStorageDisabled(bool storageDisabled){
	properties.insert(map<string, string>::value_type("storageDisabled", convert_bool(storageDisabled)));
}


