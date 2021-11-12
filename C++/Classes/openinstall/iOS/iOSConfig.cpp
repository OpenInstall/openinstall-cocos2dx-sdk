//
// Created by wade on 2021/9/13.
//

#include "iOSConfig.h"

using namespace openInstall2dx;

bool iOSConfig::isASAEnabled() {
	return _ASAEnabled;
}

void iOSConfig::setASAEnabled(bool ASAEnabled){
    _ASAEnabled = ASAEnabled;
}

char* iOSConfig::getIdfa() {
	return _idfa;
}

void iOSConfig::setIdfa(char* idfa){
    _idfa = idfa;
}
