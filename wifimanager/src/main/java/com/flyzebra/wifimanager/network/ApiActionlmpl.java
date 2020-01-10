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
    public void downloadAllPublic(Observer<ResultPubData> observer) {
        mNetService.downloadAllPublic()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void downloadAllPrivate(Observer<ResultPubData> observer) {
        mNetService.downloadAllPrivate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void requestPubWifiInfoList(PubDownParam param, Observer<ResultPubData> observer) {
        mNetService.requestPubWifiInfoList(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void uploadPriWifioInfo(PriUpParam param, Observer<ResultPriCode> observer) {
        mNetService.uploadPriWifioInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void deletePriWifiInfo(PriDelParam param, Observer<ResultPriCode> observer) {
        mNetService.deletePriWifiInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void requestPriWifiInfoList(PriDownParam param, Observer<ResultPriData> observer) {
        mNetService.requestPriWifiInfoList(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void uploadPubWifiInfo(PubUpParam param, Observer<ResultPubCode> observer) {
        mNetService.uploadPubWifiInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void deletePubWifiInfo(PubDelParam param, Observer<ResultPubCode> observer) {
        mNetService.deletePubWifiInfo(param)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
