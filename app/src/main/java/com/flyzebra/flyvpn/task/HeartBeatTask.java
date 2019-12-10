package com.flyzebra.flyvpn.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.utils.MyTools;

/**
 * ClassName: HeartBeatTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午10:01
 */
public class HeartBeatTask {
    private RatdSocketTask ratdSocketTask;

    private static final int HEARTBEAT_TIME = 5000;
    private static final HandlerThread mHeartBeatThread = new HandlerThread("HeartBeatTask");

    static {
        mHeartBeatThread.start();
    }


    private static final Handler mHeartBeatHandler = new Handler(mHeartBeatThread.getLooper());
    private Runnable heartBeatTask = new Runnable() {
        @Override
        public void run() {
            long curretTime = SystemClock.uptimeMillis() % HEARTBEAT_TIME;
            long delayedTime = curretTime == 0 ? HEARTBEAT_TIME : HEARTBEAT_TIME - curretTime;
            mHeartBeatHandler.postDelayed(this, delayedTime);
            //发送心跳
            if (ratdSocketTask != null) {
                ratdSocketTask.sendMessage(String.format(MpcMessage.heartBeat, MyTools.createSessionId()));
            }
        }
    };


    public HeartBeatTask(RatdSocketTask ratdSocketTask){
        this.ratdSocketTask = ratdSocketTask;
        mHeartBeatHandler.postDelayed(heartBeatTask, SystemClock.uptimeMillis() % 5000);
    }

    public void cancel(){
        mHeartBeatHandler.removeCallbacksAndMessages(null);
        ratdSocketTask = null;
    }
}
