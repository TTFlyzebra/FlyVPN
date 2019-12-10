package com.flyzebra.flyvpn.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;

/**
 * ClassName: DetectLinkTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午11:14
 */
public class DetectLinkTask {
    private RatdSocketTask ratdSocketTask;

    private static final int HEARTBEAT_TIME = 5000;
    private static final HandlerThread mDetectLinkThread = new HandlerThread("DetectLinkTask");

    static {
        mDetectLinkThread.start();
    }

    private static final Handler mDetectLinkHandler = new Handler(mDetectLinkThread.getLooper());

    private Runnable detectLinkTask = new Runnable() {
        @Override
        public void run() {
            long curretTime = SystemClock.uptimeMillis() % HEARTBEAT_TIME;
            long delayedTime = curretTime == 0 ? HEARTBEAT_TIME : HEARTBEAT_TIME - curretTime;
            mDetectLinkHandler.postDelayed(this, delayedTime);
            //发送心跳
            if (ratdSocketTask != null) {
            }
        }
    };


    public DetectLinkTask(RatdSocketTask ratdSocketTask){
        this.ratdSocketTask = ratdSocketTask;
        mDetectLinkHandler.postDelayed(detectLinkTask, SystemClock.uptimeMillis() % 5000);
    }

    public void cancel(){
        mDetectLinkHandler.removeCallbacksAndMessages(null);
        ratdSocketTask = null;
    }
}
