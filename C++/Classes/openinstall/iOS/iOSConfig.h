//
// Created by cooper on 2021/9/13.
//

#ifndef OPENINSTALL_ADCONFIG_H
#define OPENINSTALL_ADCONFIG_H

namespace openInstall2dx {

    class iOSConfig {

    private:
        bool _ASAEnabled;
        char *_idfa;

    public:

        iOSConfig() {};

        iOSConfig(bool ASAEnabled, char *idfa) {
            _ASAEnabled = ASAEnabled;
            _idfa = idfa;
        }

        bool isASAEnabled();

        void setASAEnabled(bool ASAEnabled);

        char *getIdfa();

        void setIdfa(char* idfa);

    };

}

#endif //OPENINSTALL_ADCONFIG_H
