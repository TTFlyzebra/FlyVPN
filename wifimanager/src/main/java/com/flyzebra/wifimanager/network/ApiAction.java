package com.flyzebra.wifimanager.network;


import com.flyzebra.wifimanager.bean.ResultWifiList;

import io.reactivex.Observer;


public interface ApiAction {


    void downloadAllPrivate(Observer<ResultWifiList> observer);

    void downloadAllPublic(Observer<ResultWifiList> observer);

}