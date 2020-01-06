package com.flyzebra.wifimanager;

import android.octopu.wifi.bean.ResultWifiDevice;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.flyzebra.utils.FlyLog;
import com.flyzebra.wifimanager.network.ApiAction;
import com.flyzebra.wifimanager.network.ApiActionlmpl;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private ApiAction httpApi = new ApiActionlmpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpApi.downloadAllPublic(new Observer<ResultWifiDevice>() {
            @Override
            public void onSubscribe(Disposable d) {
                FlyLog.d("Disposable"+d);
            }

            @Override
            public void onNext(ResultWifiDevice resultWifiList) {
                FlyLog.d("result size:"+resultWifiList.retInfo.size());
            }

            @Override
            public void onError(Throwable e) {
                FlyLog.e("onError"+e);
            }

            @Override
            public void onComplete() {
                FlyLog.d("onComplete");
            }
        });

        httpApi.downloadAllPrivate(new Observer<ResultWifiDevice>() {
            @Override
            public void onSubscribe(Disposable d) {
                FlyLog.d("Disposable: "+d);
            }

            @Override
            public void onNext(ResultWifiDevice resultWifiList) {
                FlyLog.d("result size: "+resultWifiList.retInfo.size());
            }

            @Override
            public void onError(Throwable e) {
                FlyLog.e("onError: "+e);
            }

            @Override
            public void onComplete() {
                FlyLog.d("onComplete");
            }
        });

    }
}
