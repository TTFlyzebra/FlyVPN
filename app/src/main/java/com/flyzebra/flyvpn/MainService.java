package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;

import com.flyzebra.utils.FlyLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainService extends Service {
    private Thread mThread;
    private RatdDaemonConnector mConnector;
    private static final String RATD_TAG = "RatdConnector";

    private static final int HEARTBEAT_TIME = 5000;
    private static final HandlerThread mHeartBeatThread = new HandlerThread("HeartBeatTask");
    static {mHeartBeatThread.start();}
    private static final Handler mHeartBeatHandler = new Handler(mHeartBeatThread.getLooper());
    private Runnable heartBeatTask = new Runnable() {
        @Override
        public void run() {
            long curretTime = SystemClock.uptimeMillis() % HEARTBEAT_TIME;
            long delayedTime = curretTime == 0 ? HEARTBEAT_TIME : HEARTBEAT_TIME - curretTime;
            mHeartBeatHandler.postDelayed(this, delayedTime);
            if (mConnector != null) {
                mConnector.sendMessage(String.format(MpcMessage.heartBeat, createSessionId()));
            }
        }
    };

    public static synchronized int createSessionId() {
        StringBuffer sessionid = new StringBuffer();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
        sessionid.append(formatter.format(new Date()));
        if (sessionid.length() == 8) {
            Random rand = new Random();
            int ram = rand.nextInt(9);
            sessionid.append(ram);
        }
        return Integer.parseInt(sessionid.toString());
    }

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
        mHeartBeatHandler.postDelayed(heartBeatTask, SystemClock.uptimeMillis() % 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FlyLog.d();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mHeartBeatHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}