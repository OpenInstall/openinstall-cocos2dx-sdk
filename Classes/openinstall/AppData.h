//
// Created by Wenki on 2018/5/30.
//

#ifndef OPENINSTALL_APPDATA_H
#define OPENINSTALL_APPDATA_H


#include <string>
//#include "cocos2d.h"

namespace openInstall2dx {
    
    class AppData {
        
    private:
        std::string channelCode;
        std::string bindData;
        
    public:
        
        AppData() {};
        
        AppData(std::string channelCode, std::string bindData) {
            this->channelCode = channelCode;
            this->bindData = bindData;
        }
        
        std::string getChannelCode();
        
        std::string getBindData();
        
    };

}

#endif //OPENINSTALL_APPDATA_H
