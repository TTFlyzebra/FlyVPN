package com.flyzebra.wifimanager.network;


import com.android.server.octopu.wifiextend.bean.PriDelParam;
import com.android.server.octopu.wifiextend.bean.PriDownParam;
import com.android.server.octopu.wifiextend.bean.PriUpParam;
import com.android.server.octopu.wifiextend.bean.PubDelParam;
import com.android.server.octopu.wifiextend.bean.PubDownParam;
import com.android.server.octopu.wifiextend.bean.PubUpParam;
import com.android.server.octopu.wifiextend.bean.ResultPriCode;
import com.android.server.octopu.wifiextend.bean.ResultPriData;
import com.android.server.octopu.wifiextend.bean.ResultPubCode;
import com.android.server.octopu.wifiextend.bean.ResultPubData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HttpApi {

    //TEST
    @GET("extendWifi/public/wifiInfoManage/downloadAll")
    Observable<ResultPubData> downloadAllPublic();

    //TEST
    @GET("extendWifi/private/wifiInfoManage/downloadAll")
    Observable<ResultPubData> downloadAllPrivate();

    @POST("/extendWifi/private/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPriData> requestPriWifiInfoList(@Body PriDownParam body);

    @POST("/extendWifi/private/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPriCode> uploadPriWifioInfo(@Body PriUpParam body);

    @POST("/extendWifi/private/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json","charset:utf-8"})
    Observable<ResultPriCode> deletePriWifiInfo(@Body PriDelParam body);

    @POST("/extendWifi/public/wifiInfoManage/downloadWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubData> requestPubWifiInfoList(@Body PubDownParam body);

    @POST("/extendWifi/public/wifiInfoManage/shareWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubCode> uploadPubWifiInfo(@Body PubUpParam body);

    @POST("/extendWifi/public/wifiInfoManage/removeWifiInfo")
    @Headers({"Content-Type:application/json"})
    Observable<ResultPubCode> deletePubWifiInfo(@Body PubDelParam body);

}