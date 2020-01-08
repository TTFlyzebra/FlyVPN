package com.flyzebra.wifimanager;

import android.app.Service;
import android.content.Intent;
import android.octopu.IOctopuService;
import android.os.IBinder;

import com.android.services.octopu.OctopuService;


/**
 * ClassName: WifiService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:50
 */
public class WifiService extends Service {
    private IOctopuService service;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = new OctopuService(this);
    }
}
