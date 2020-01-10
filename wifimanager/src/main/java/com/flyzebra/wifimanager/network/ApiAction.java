package com.flyzebra.wifimanager.network;


import com.android.server.octopu.wifi.bean.PriDelParam;
import com.android.server.octopu.wifi.bean.PriDownParam;
import com.android.server.octopu.wifi.bean.PriUpParam;
import com.android.server.octopu.wifi.bean.PubDelParam;
import com.android.server.octopu.wifi.bean.PubDownParam;
import com.android.server.octopu.wifi.bean.PubUpParam;
import com.android.server.octopu.wifi.bean.ResultPriCode;
import com.android.server.octopu.wifi.bean.ResultPriData;
import com.android.server.octopu.wifi.bean.ResultPubCode;
import com.android.server.octopu.wifi.bean.ResultPubData;

import io.reactivex.Observer;


public interface ApiAction {


    void downloadAllPrivate(Observer<ResultPubData> observer);

    void downloadAllPublic(Observer<ResultPubData> observer);

    void requestPubWifiInfoList(PubDownParam param, Observer<ResultPubData> observer);

    void uploadPriWifioInfo(PriUpParam body, Observer<ResultPriCode> observer);

    void deletePriWifiInfo(PriDelParam body, Observer<ResultPriCode> observer);

    void requestPriWifiInfoList(PriDownParam param, Observer<ResultPriData> observer);

    void uploadPubWifiInfo(PubUpParam body, Observer<ResultPubCode> observer);

    void deletePubWifiInfo(PubDelParam body, Observer<ResultPubCode> observer);

}