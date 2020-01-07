package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.PriDelParam;
import android.octopu.wifi.bean.PriDownParam;
import android.octopu.wifi.bean.PriUpParam;
import android.octopu.wifi.bean.PubDelParam;
import android.octopu.wifi.bean.PubDownParam;
import android.octopu.wifi.bean.PubUpParam;
import android.octopu.wifi.bean.ResultPriCode;
import android.octopu.wifi.bean.ResultPriData;
import android.octopu.wifi.bean.ResultPubCode;
import android.octopu.wifi.bean.ResultPubData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HttpApi {

    //TEST
    @GET("wifi/public/wifiInfoManage/downloadAll")
    Observable<ResultPubData> downloadAllPublic();

    //TEST
    @GET("wifi/private/wifiInfoManage/downloadAll")
    Observable<ResultPubData> downloadAllPrivate();

    @POST("/wifi/private/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPriData> requestPriWifiInfoList(@Body PriDownParam body);

    @POST("/wifi/private/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPriCode> uploadPriWifioInfo(@Body PriUpParam body);

    @POST("/wifi/private/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPriCode> deletePriWifiInfo(@Body PriDelParam body);

    @POST("/wifi/public/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubData> requestPubWifiInfoList(@Body PubDownParam body);

    @POST("/wifi/public/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubCode> uploadPubWifiInfo(@Body PubUpParam body);

    @POST("/wifi/public/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubCode> deletePubWifiInfo(@Body PubDelParam body);

}