package com.flyzebra.wifimanager.network;




import android.octopu.wifi.bean.RequestPrivateParam;
import android.octopu.wifi.bean.RequestPublicParam;
import android.octopu.wifi.bean.ResultPrivateData;
import android.octopu.wifi.bean.ResultPublicData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiActionlmpl implements ApiAction {
    private HttpService mHttpService;
    private HttpApi mNetService;

    public ApiActionlmpl() {
        mHttpService = new HttpService();
        mNetService = mHttpService.getInspectionService();
    }



    @Override
    public void downloadAllPublic(Observer<ResultPublicData> observer) {
        mNetService.downloadAllPublic()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void requestPublicWifiInfo(RequestPublicParam param, Observer<ResultPublicData> observer) {
        mNetService.requestPublicWifiInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void downloadAllPrivate(Observer<ResultPublicData> observer) {
        mNetService.downloadAllPrivate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void requestPrivateWifiInfo(RequestPrivateParam param, Observer<ResultPrivateData> observer) {
        mNetService.requestPrivateWifiInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
