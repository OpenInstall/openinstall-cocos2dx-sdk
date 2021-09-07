//
// Created by wade on 2021/9/6.
//

#include "AdConfig.h"

using namespace openInstall2dx;

bool AdConfig::isAdEnabled() {
	return _adEnabled;
}

void AdConfig::setAdEnabled(bool adEnabled){
	_adEnabled = adEnabled;
}

char* AdConfig::getOaid() {
	return _oaid;
}

void AdConfig::setOaid(char* oaid){
	_oaid = oaid;
}

char* AdConfig::getGaid() {
	return _gaid;
}

void AdConfig::setGaid(char* gaid){
	_gaid = gaid;
}

bool AdConfig::isImeiDisabled() {
	return _imeiDisabled;
}

void AdConfig::setImeiDisabled(bool imeiDisabled){
	_imeiDisabled = imeiDisabled;
}

bool AdConfig::isMacDisabled() {
	return _macDisabled;
}

void AdConfig::setMacDisabled(bool macDisabled){
	_macDisabled = macDisabled;
}


