package com.flyzebra.flyvpn.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.text.TextUtils;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.model.IRatdRecvMessage;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.utils.FlyLog;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClassName: DetectLinkTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午11:14
 */
public class DetectLinkTask implements ITask,Runnable, IRatdRecvMessage {
    private final ConnectivityManager cm;
    private Context mContext;
    private RatdSocketTask ratdSocketTask;
    private static final int HEARTBEAT_TIME = 5000;
    private static final HandlerThread mDetectLinkThread = new HandlerThread("DetectLinkTask");
    private long lastRunTime = 0;
    private AtomicBoolean isRun = new AtomicBoolean(false);

    private Object lock = new Object();
    private static final int RUN_LIGHT = 5; //激活态亮屏 5秒探测
    private static final int RUN_NOLIGHT = 5; //激活态灭屏 5秒探测
    private static final int NORUN_LIGHT = 60; //空闲态亮屏 60秒探测
    private static final int NORUN_NOLIGHT = 300; //空闲态灭屏 300秒探测
    private int status = RUN_LIGHT;

    static {
        mDetectLinkThread.start();
    }

    private static final Handler mDetectLinkHandler = new Handler(mDetectLinkThread.getLooper());

    public DetectLinkTask(Context context, RatdSocketTask ratdSocketTask) {
        this.mContext = context;
        this.ratdSocketTask = ratdSocketTask;
        cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void run() {
        long curretTime = SystemClock.uptimeMillis() % HEARTBEAT_TIME;
        long delayedTime = curretTime == 0 ? HEARTBEAT_TIME : HEARTBEAT_TIME - curretTime;
        mDetectLinkHandler.postDelayed(this, delayedTime);
        lastRunTime = curretTime;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                LinkProperties linkProperties = cm.getLinkProperties(network);
                if (linkProperties != null && ratdSocketTask != null) {
                    String iface = linkProperties.getInterfaceName();
                    if (TextUtils.isEmpty(iface)) continue;
                    int netType = iface.startsWith("wlan") ? 4 : iface.startsWith("rmnet_data") ? 2 : iface.startsWith("mcwill") ? 1 : -1;
                    if (netType > 0) {
                        ratdSocketTask.sendMessage(String.format(MpcMessage.detectLink, netType, iface, MyTools.createSessionId()));
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void start() {
        if (isRun.get()) {
            FlyLog.e("DetectLinkTask is Running...");
            return;
        }
        isRun.set(true);
        mDetectLinkHandler.post(this);
    }

    @Override
    public void stop() {
        isRun.set(false);
        mDetectLinkHandler.removeCallbacksAndMessages(null);
    }


    public void onDestory(){
        ratdSocketTask.register(this);
        ratdSocketTask = null;
    }

    @Override
    public void recvRatdMessage(MpcMessage message) {
        if (message.messageType == 0x1A) {
            switch (message.exceptionCode) {
                case -5:
                    break;
                case -6:
                    break;
            }
        }
    }
}
