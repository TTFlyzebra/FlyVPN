package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.task.RatdDaemonConnector;
import com.flyzebra.utils.FlyLog;

public class MainService extends Service {
    private Thread mThread;
    private RatdDaemonConnector mConnector;
    private static final String RATD_TAG = "RatdConnector";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        FlyLog.d();
        super.onCreate();
        mConnector = new RatdDaemonConnector();
        mThread = new Thread(mConnector, RATD_TAG);
        mThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FlyLog.d();
        return super.onStartCommand(intent, flags, startId);
    }
}