package com.flyzebra.wifimanager.network;


import android.octopu.wifi.bean.ResultWifiDevice;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HttpApi {

    @GET("wifi/public/wifiInfoManage/downloadAll")
    Observable<ResultWifiDevice> downloadAllPublic();

    @GET("wifi/private/wifiInfoManage/downloadAll")
    Observable<ResultWifiDevice> downloadAllPrivate();

    @POST("/wifi/private/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiDevice> requestPrivateDb(@Body ResultWifiDevice body);

    @POST("/wifi/private/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultWifiDevice> uploadPrivateInfo(@Body ResultWifiDevice body);

    @POST("/wifi/private/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultWifiDevice> deletePrivateInfo(@Body ResultWifiDevice body);

    @POST("/wifi/public/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiDevice> requestPublicDb(@Body ResultWifiDevice body);

    @POST("/wifi/public/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiDevice> uploadPublicWifiInfo(@Body ResultWifiDevice body);

    @POST("/wifi/public/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiDevice> deletePublicWifiInfo(@Body ResultWifiDevice body);

}