package com.flyzebra.flyvpn.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.text.TextUtils;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.data.NetworkLink;
import com.flyzebra.flyvpn.utils.MyTools;

import java.util.List;

/**
 * ClassName: DetectLinkTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午11:14
 */
public class DetectLinkTask implements Runnable {
    private Context mContext;
    private RatdSocketTask ratdSocketTask;
    private static final int HEARTBEAT_TIME = 5000;
    private static final HandlerThread mDetectLinkThread = new HandlerThread("DetectLinkTask");

    static {
        mDetectLinkThread.start();
    }

    private static final Handler mDetectLinkHandler = new Handler(mDetectLinkThread.getLooper());

    public DetectLinkTask(Context context, RatdSocketTask ratdSocketTask) {
        this.mContext = context;
        this.ratdSocketTask = ratdSocketTask;
    }

    public void cancel() {
        mDetectLinkHandler.removeCallbacksAndMessages(null);
        ratdSocketTask = null;
    }

    @Override
    public void run() {
        long curretTime = SystemClock.uptimeMillis() % HEARTBEAT_TIME;
        long delayedTime = curretTime == 0 ? HEARTBEAT_TIME : HEARTBEAT_TIME - curretTime;
        mDetectLinkHandler.postDelayed(this, delayedTime);
        if (mContext != null) {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Network networks[] = cm.getAllNetworks();
                for (Network network : networks) {
                    LinkProperties linkProperties = cm.getLinkProperties(network);
                    if (linkProperties != null && ratdSocketTask != null) {
                        String iface = linkProperties.getInterfaceName();
                        if (TextUtils.isEmpty(iface)) continue;
                        int netType = iface.startsWith("wlan") ? 4 : iface.startsWith("rmnet_data") ? 2 : iface.startsWith("mcwill") ? 1 : -1;
                        if (netType > 0) {
                            ratdSocketTask.sendMessage(String.format(MpcMessage.detectLink, netType, iface, MyTools.createSessionId()));
                            List<LinkAddress> linkAddress = linkProperties.getLinkAddresses();
                            if (linkAddress != null && !linkAddress.isEmpty()) {
                                String ip = linkAddress.get(0).toString();
                                ip = ip.substring(0, ip.indexOf("/"));
                                if(TextUtils.isEmpty(ip)){
                                    NetworkLink networkLink = netType==4? MpcStatus.getInstance().wifiLink
                                            :netType==2?MpcStatus.getInstance().mobileLink
                                            :MpcStatus.getInstance().mcwillLink;
                                    networkLink.ip = ip;
                                    networkLink.name = iface;
                                    networkLink.type = netType;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void start() {
        mDetectLinkHandler.post(this);
    }
}
