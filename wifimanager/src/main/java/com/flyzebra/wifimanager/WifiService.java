package com.flyzebra.wifimanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.server.octopu.OctopuService;


/**
 * ClassName: WifiService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:50
 */
public class WifiService extends Service {
    private OctopuService service;
    @Override
    public IBinder onBind(Intent intent) {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = new OctopuService(this);
    }
}
