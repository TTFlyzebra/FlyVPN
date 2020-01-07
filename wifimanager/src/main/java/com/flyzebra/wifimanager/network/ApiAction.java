package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.PriDelParam;
import android.octopu.wifi.bean.PriDownParam;
import android.octopu.wifi.bean.PriUpParam;
import android.octopu.wifi.bean.PubDelParam;
import android.octopu.wifi.bean.PubDownParam;
import android.octopu.wifi.bean.PubUpParam;
import android.octopu.wifi.bean.ResultPri;
import android.octopu.wifi.bean.ResultPriData;
import android.octopu.wifi.bean.ResultPub;
import android.octopu.wifi.bean.ResultPubData;

import io.reactivex.Observer;


public interface ApiAction {


    void downloadAllPrivate(Observer<ResultPubData> observer);

    void downloadAllPublic(Observer<ResultPubData> observer);

    void requestPubWifiInfoList(PubDownParam param, Observer<ResultPubData> observer);

    void uploadPriWifioInfo(PriUpParam body, Observer<ResultPri> observer);

    void deletePriWifiInfo(PriDelParam body, Observer<ResultPri> observer);

    void requestPriWifiInfoList(PriDownParam param, Observer<ResultPriData> observer);

    void uploadPubWifiInfo(PubUpParam body, Observer<ResultPub> observer);

    void deletePubWifiInfo(PubDelParam body, Observer<ResultPub> observer);

}