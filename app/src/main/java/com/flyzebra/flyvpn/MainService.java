package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.RatdSocketTask;

/**
 * ClassName: MainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class MainService extends Service {
    private RatdSocketTask mConnector;
    private HeartBeatTask heartBeatTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mConnector = new RatdSocketTask();
        heartBeatTask = new HeartBeatTask(mConnector);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        heartBeatTask.cancel();
        super.onDestroy();
    }
}