package com.flyzebra.flyvpn.task;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.model.IRatdRecvMessage;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.utils.FlyLog;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClassName: HeartBeatTask
 * Description: 使能双流线程，因为使能双流【消息为0x11】发送过去RATD可能不会有不成功的返回，
 * 并且要求在存在多个网络的情况下，如果没能收到成功消息，需要依据wifi,4g,mcwill
 * 的顺序向RATD不停发送消息，只要不成功，直到返回成功为止。
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午10:01
 */
public class EnableMpcTask implements ITask, Runnable, IRatdRecvMessage {
    private RatdSocketTask ratdSocketTask;
    private Context mContext;
    private static final int ENABLEMPC_TIME = 2000;
    private ArrayBlockingQueue<Integer> networkList = new ArrayBlockingQueue<Integer>(4);
    private static final HandlerThread mEnableMpcThread = new HandlerThread("EnableMpc_Task");

    static {
        mEnableMpcThread.start();
    }

    private static final Handler mEnableMpcHandler = new Handler(mEnableMpcThread.getLooper());
    private AtomicBoolean isRun = new AtomicBoolean(false);

    public EnableMpcTask(Context context, RatdSocketTask ratdSocketTask) {
        this.ratdSocketTask = ratdSocketTask;
        this.mContext = context;
        onCreate();
    }

    @Override
    public void onCreate() {
        this.ratdSocketTask.register(this);
    }

    @Override
    public void start() {
        if (isRun.get()) {
            FlyLog.e("EnableMpcTask is Running...");
            return;
        } else {
            FlyLog.e("EnableMpcTask start...");
        }
        isRun.set(true);
        mEnableMpcHandler.post(this);
    }

    @Override
    public void stop() {
        if (isRun.get()) {
            isRun.set(false);
            FlyLog.e("EnableMpcTask stop...");
        }
        mEnableMpcHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestory() {
        mEnableMpcHandler.removeCallbacksAndMessages(null);
        ratdSocketTask = null;
    }


    @Override
    public void run() {
        long curretTime = SystemClock.uptimeMillis() % ENABLEMPC_TIME;
        long delayedTime = curretTime == 0 ? ENABLEMPC_TIME : ENABLEMPC_TIME - curretTime;
        mEnableMpcHandler.postDelayed(this, Math.max(delayedTime,ENABLEMPC_TIME-500));
        //发送使能双流
        if (ratdSocketTask != null) {
            try {
                int netType = networkList.poll();
                if (isRun.get()) {
                    if (netType == 4 || netType == 2 || netType == 1) {
                        String iface = netType == 4 ? "wlan0" : netType == 2 ? "rmnet_data0" : "mcwill";
                        ratdSocketTask.sendMessage(String.format(MpcMessage.enableMpc, netType, iface, MyTools.createSessionId()));
                    } else {
                        FlyLog.d("EnableMpcTask error! unknown netType=%d", netType);
                    }
                }
            } catch (Exception e) {
//                FlyLog.e("EnableMpcTask throw Exception:" + e);
            }
        }
    }

    @Override
    public void recvRatdMessage(MpcMessage message) {
        //使能双流返回成功结束线程
        if (message.messageType == 0x12 && message.result == 0) {
            networkList.clear();
            stop();
        }
    }

    public void addNetworkTask(int netType) {
        while (networkList.size() > 3) {
            networkList.poll();
        }
        networkList.offer(netType);
    }
}
