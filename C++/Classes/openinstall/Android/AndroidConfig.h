//
// Created by wade on 2021/9/6.
//

#ifndef OPENINSTALL_ADCONFIG_H
#define OPENINSTALL_ADCONFIG_H

namespace openInstall2dx {

    class AndroidConfig {

    private:
        bool _adEnabled = false;
        char *_oaid = nullptr;
        char *_gaid = nullptr;
        bool _imeiDisabled = false;
        bool _macDisabled = false;

    public:

        AndroidConfig() {};

        AndroidConfig(bool adEnabled, char *oaid, char *gaid, bool imeiDisabled, bool macDisabled) {
            _adEnabled = adEnabled;
            _oaid = oaid;
            _gaid = gaid;
            _imeiDisabled = imeiDisabled;
            _macDisabled = macDisabled;
        }

        bool isAdEnabled();

        void setAdEnabled(bool adEnabled);

        char *getOaid();

        void setOaid(char* oaid);

        char *getGaid();

        void setGaid(char* gaid);

        bool isImeiDisabled();

        void setImeiDisabled(bool imeiDisabled);

        bool isMacDisabled();

        void setMacDisabled(bool macDisabled);

    };

}

#endif //OPENINSTALL_ADCONFIG_H
