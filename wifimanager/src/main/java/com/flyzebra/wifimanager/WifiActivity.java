package com.flyzebra.wifimanager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.android.server.octopu.wifiextend.bean.WifiDeviceBean;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class WifiActivity extends AppCompatActivity implements OctopuManager.WifiDeviceListener {
    private IOctopuService mService;
    private OctopuManager octopuManager;

    private MyReciver myReciver;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IOctopuService.Stub.asInterface(service);
            octopuManager = new OctopuManager(WifiActivity.this, mService);
            octopuManager.addWifiDeviceListener(WifiActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            tryBindService();
        }
    };
    private void tryBindService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.flyzebra.wifimanager", "com.flyzebra.wifimanager.WifiService"));
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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        myReciver = new MyReciver();
        registerReceiver(myReciver, intentFilter);


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            Toast.makeText(this, "未打开GPS,无法扫描", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (octopuManager != null) {
            octopuManager.removeWifiDeviceListener(this);
        }
        unbindService(conn);
        unregisterReceiver(myReciver);
        super.onDestroy();
    }

    @Override
    public void notifyWifiDevices(List<WifiDeviceBean> wifiDevices) {
        FlyLog.d("wifiDevices size = %d." , wifiDevices.size());
        FlyLog.d("wifiDevices:" + wifiDevices);
    }

    class MyReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    List<ScanResult> listResults = wifiManager.getScanResults();
                    FlyLog.d("listResults:"+listResults);
                    List<String> list = new ArrayList<>();
                    for(ScanResult scanResult:listResults){
                        list.add(scanResult.BSSID);
                    }
                    if(octopuManager!=null){
                        octopuManager.flyWifiDevices(list);
                    }
                    break;
            }
        }
    }
}
