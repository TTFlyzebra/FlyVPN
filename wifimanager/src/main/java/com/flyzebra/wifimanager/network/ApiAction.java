package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.RequestPrivateParam;
import android.octopu.wifi.bean.RequestPublicParam;
import android.octopu.wifi.bean.ResultPrivateData;
import android.octopu.wifi.bean.ResultPublicData;

import io.reactivex.Observer;


public interface ApiAction {


    void downloadAllPrivate(Observer<ResultPublicData> observer);

    void downloadAllPublic(Observer<ResultPublicData> observer);

    void requestPublicWifiInfo(RequestPublicParam param,Observer<ResultPublicData> observer);

    void requestPrivateWifiInfo(RequestPrivateParam param, Observer<ResultPrivateData> observer);

}