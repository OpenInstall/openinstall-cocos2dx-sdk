//
// Created by wade on 2021/9/6.
//

#include "AndroidConfig.h"

using namespace openInstall2dx;

bool AndroidConfig::isAdEnabled() {
	return _adEnabled;
}

void AndroidConfig::setAdEnabled(bool adEnabled){
	_adEnabled = adEnabled;
}

char* AndroidConfig::getOaid() {
	return _oaid;
}

void AndroidConfig::setOaid(char* oaid){
	_oaid = oaid;
}

char* AndroidConfig::getGaid() {
	return _gaid;
}

void AndroidConfig::setGaid(char* gaid){
	_gaid = gaid;
}

bool AndroidConfig::isImeiDisabled() {
	return _imeiDisabled;
}

void AndroidConfig::setImeiDisabled(bool imeiDisabled){
	_imeiDisabled = imeiDisabled;
}

bool AndroidConfig::isMacDisabled() {
	return _macDisabled;
}

void AndroidConfig::setMacDisabled(bool macDisabled){
	_macDisabled = macDisabled;
}


