package com.flyzebra.wifimanager;

import android.octopu.wifi.bean.RequestPrivateParam;
import android.octopu.wifi.bean.RequestPublicParam;
import android.octopu.wifi.bean.ResultPrivateData;
import android.octopu.wifi.bean.ResultPublicData;
import android.octopu.wifi.db.WifiDeviceSQLite;
import android.os.Bundle;

import com.flyzebra.utils.FlyLog;
import com.flyzebra.wifimanager.network.ApiAction;
import com.flyzebra.wifimanager.network.ApiActionlmpl;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class WifiActivity extends AppCompatActivity {
    private ApiAction httpApi = new ApiActionlmpl();
    private WifiDeviceSQLite dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new WifiDeviceSQLite(getApplicationContext());

        RequestPublicParam param1 = new RequestPublicParam(this);

        httpApi.requestPublicWifiInfo(param1, new Observer<ResultPublicData>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResultPublicData resultWifiList) {
                if (resultWifiList != null) {
                    if (resultWifiList.retInfo != null) {
                        dbHelper.updataPubWifiDevices(resultWifiList.retInfo);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                FlyLog.e("onError" + e);
            }

            @Override
            public void onComplete() {
            }
        });

        RequestPrivateParam param2 = new RequestPrivateParam(this);

        httpApi.requestPrivateWifiInfo(param2, new Observer<ResultPrivateData>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResultPrivateData resultWifiList) {
                if (resultWifiList != null) {
                    if (resultWifiList.retInfo != null&&resultWifiList.retInfo.wifiList!=null) {
                        dbHelper.updataPriWifiDevices(resultWifiList.retInfo.wifiList);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                FlyLog.e("onError" + e);
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
