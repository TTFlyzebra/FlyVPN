package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.task.DetectLinkTask;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.OnRecvMessage;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class MainService extends Service implements OnRecvMessage {
    private RatdSocketTask ratdSocketTask;
    private HeartBeatTask heartBeatTask;
    private DetectLinkTask detectLinkTask;
    private MpcStatus mpcStatus;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MpcStatus.getInstance().init();
        ratdSocketTask = new RatdSocketTask();
        ratdSocketTask.register(this);
        heartBeatTask = new HeartBeatTask(ratdSocketTask);
        detectLinkTask = new DetectLinkTask(getApplicationContext(),ratdSocketTask);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FlyLog.d("onDestroy");
        ratdSocketTask.unRegister(this);
        heartBeatTask.cancel();
        detectLinkTask.cancel();
        super.onDestroy();
    }

    @Override
    public void recv(MpcMessage message) {

    }
}