package com.flyzebra.wifimanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuManager;
import android.octopu.wifi.bean.WifiDeviceBean;
import android.os.Bundle;
import android.os.IBinder;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class WifiActivity extends AppCompatActivity implements OctopuManager.WifiDeviceListener {
    private IOctopuService mService;
    private OctopuManager octopuManager;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IOctopuService.Stub.asInterface(service);
            octopuManager = new OctopuManager(WifiActivity.this, mService);
            octopuManager.addWifiDeviceListener(WifiActivity.this);
            octopuManager.upWifiDeviceData(null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            tryBindService();
        }
    };

    private void tryBindService(){
        try {
            Intent intent = new Intent(this, WifiService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            FlyLog.e(e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tryBindService();
    }

    @Override
    protected void onDestroy() {
        if (octopuManager != null) {
            octopuManager.removeWifiDeviceListener(this);
        }
        unbindService(conn);
        super.onDestroy();
    }

    @Override
    public void notifyWifiDevices(List<WifiDeviceBean> wifiDeviceBeans) {
        FlyLog.d("wifiDeviceBeans:" + wifiDeviceBeans);
    }
}
