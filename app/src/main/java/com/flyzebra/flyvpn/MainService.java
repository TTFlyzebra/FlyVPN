package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.RatdSocketTask;

/**
 * ClassName: MainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class MainService extends Service implements OnRecvMessage{
    private RatdSocketTask ratdSocketTask;
    private HeartBeatTask heartBeatTask;
    private MpcStatus mpcStatus;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mpcStatus = new MpcStatus();
        ratdSocketTask = new RatdSocketTask();
        heartBeatTask = new HeartBeatTask(ratdSocketTask);
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

    @Override
    public void recv(String message) {

    }
}