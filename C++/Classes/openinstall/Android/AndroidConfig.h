//
// Created by wade on 2021/9/6.
//

#ifndef OPENINSTALL_ADCONFIG_H
#define OPENINSTALL_ADCONFIG_H

#include <map>
#include <string>

namespace openInstall2dx {


    class AndroidConfig {

    private:
        std::map<std::string, std::string> properties = std::map<std::string, std::string>();

    public:

        AndroidConfig() {};

        std::map<std::string, std::string> getProperties();

        void setAdEnabled(bool adEnabled);

        void setOaid(std::string  oaid);

        void setGaid(std::string  gaid);

        void setImeiDisabled(bool imeiDisabled);

        void setImei(std::string imei);

        void setMacDisabled(bool macDisabled);

        void setMacAddress(std::string macAddress);

        void setAndroidId(std::string androidId);

        void setSerialNumber(std::string serialNumber);

        void setSimulatorDisabled(bool simulatorDisabled);

        void setStorageDisabled(bool storageDisabled);
        

    };

}

#endif //OPENINSTALL_ADCONFIG_H
