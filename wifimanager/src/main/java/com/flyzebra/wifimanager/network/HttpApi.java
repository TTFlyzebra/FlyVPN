package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.RequestPrivateParam;
import android.octopu.wifi.bean.RequestPublicParam;
import android.octopu.wifi.bean.ResultPrivateData;
import android.octopu.wifi.bean.ResultPublicData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HttpApi {

    @GET("wifi/public/wifiInfoManage/downloadAll")
    Observable<ResultPublicData> downloadAllPublic();

    @GET("wifi/private/wifiInfoManage/downloadAll")
    Observable<ResultPublicData> downloadAllPrivate();

    @POST("/wifi/private/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPrivateData> requestPrivateWifiInfo(@Body RequestPrivateParam body);

    @POST("/wifi/private/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPrivateData> uploadPrivateWifioInfo(@Body ResultPublicData body);

    @POST("/wifi/private/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPrivateData> deletePrivateInfo(@Body ResultPublicData body);

    @POST("/wifi/public/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPublicData> requestPublicWifiInfo(@Body RequestPublicParam body);

    @POST("/wifi/public/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPublicData> uploadPublicWifiInfo(@Body ResultPublicData body);

    @POST("/wifi/public/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPublicData> deletePublicWifiInfo(@Body ResultPublicData body);

}