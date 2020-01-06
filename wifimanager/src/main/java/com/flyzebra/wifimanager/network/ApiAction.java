package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.ResultWifiDevice;

import io.reactivex.Observer;


public interface ApiAction {


    void downloadAllPrivate(Observer<ResultWifiDevice> observer);

    void downloadAllPublic(Observer<ResultWifiDevice> observer);

}