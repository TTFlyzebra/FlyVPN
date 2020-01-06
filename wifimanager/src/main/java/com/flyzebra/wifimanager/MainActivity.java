package com.flyzebra.wifimanager;

import android.octopu.wifi.bean.ResultWifiDevice;
import android.octopu.wifi.bean.WifiDeviceBean;
import android.octopu.wifi.db.WifiDeviceSQLite;
import android.os.Bundle;

import com.flyzebra.utils.FlyLog;
import com.flyzebra.wifimanager.network.ApiAction;
import com.flyzebra.wifimanager.network.ApiActionlmpl;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private ApiAction httpApi = new ApiActionlmpl();
    private WifiDeviceSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new WifiDeviceSQLite(getApplicationContext());

        httpApi.downloadAllPublic(new Observer<ResultWifiDevice>() {
            @Override
            public void onSubscribe(Disposable d) {
                FlyLog.d("Disposable"+d);
            }

            @Override
            public void onNext(ResultWifiDevice resultWifiList) {
                FlyLog.d("result size:"+resultWifiList.retInfo.size());
                for(WifiDeviceBean wifiDeviceBean:resultWifiList.retInfo){
                    db.insertWifiDevice(wifiDeviceBean);
                }
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
