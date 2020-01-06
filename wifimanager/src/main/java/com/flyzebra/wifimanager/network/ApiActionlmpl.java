package com.flyzebra.wifimanager.network;




import android.octopu.wifi.bean.ResultWifiDevice;

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
    public void downloadAllPublic(Observer<ResultWifiDevice> observer) {
        mNetService.downloadAllPublic()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void downloadAllPrivate(Observer<ResultWifiDevice> observer) {
        mNetService.downloadAllPrivate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
