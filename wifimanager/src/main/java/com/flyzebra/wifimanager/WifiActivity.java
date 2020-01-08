package com.flyzebra.wifimanager;

import android.octopu.wifi.bean.PriDownParam;
import android.octopu.wifi.bean.PubDownParam;
import android.octopu.wifi.bean.ResultPriData;
import android.octopu.wifi.bean.ResultPubData;
import android.octopu.wifi.db.WifiDeviceSQLite;
import android.octopu.wifi.utils.HttpTools;
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

        final PubDownParam param1 = new PubDownParam(this);
        final PriDownParam param2 = new PriDownParam(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ret = HttpTools.doPostJson(
                        "http://wifi.cootel.com/wifi/public/wifiInfoManage/downloadWifiInfo",
                        param1.toJson());
                FlyLog.d("ret = %s",ret);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ret = HttpTools.doPostJson(
                        "http://wifi.cootel.com/wifi/private/wifiInfoManage/downloadWifiInfo",
                        param2.toJson());
                FlyLog.d("ret = %s",ret);
            }
        }).start();



        httpApi.requestPubWifiInfoList(param1, new Observer<ResultPubData>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResultPubData resultWifiList) {
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

        httpApi.requestPriWifiInfoList(param2, new Observer<ResultPriData>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResultPriData resultWifiList) {
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
