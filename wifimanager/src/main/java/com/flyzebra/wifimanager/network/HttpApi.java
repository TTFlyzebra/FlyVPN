package com.flyzebra.wifimanager.network;


import com.flyzebra.wifimanager.bean.ResultWifiList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HttpApi {

    @GET("wifi/public/wifiInfoManage/downloadAll")
    Observable<ResultWifiList> downloadAllPublic();

    @GET("wifi/private/wifiInfoManage/downloadAll")
    Observable<ResultWifiList> downloadAllPrivate();

    @POST("/wifi/private/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiList> requestPrivateDb(@Body ResultWifiList body);

    @POST("/wifi/private/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultWifiList> uploadPrivateInfo(@Body ResultWifiList body);

    @POST("/wifi/private/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultWifiList> deletePrivateInfo(@Body ResultWifiList body);

    @POST("/wifi/public/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiList> requestPublicDb(@Body ResultWifiList body);

    @POST("/wifi/public/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiList> uploadPublicWifiInfo(@Body ResultWifiList body);

    @POST("/wifi/public/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultWifiList> deletePublicWifiInfo(@Body ResultWifiList body);

}