package com.flyzebra.flyvpn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.data.NetworkLink;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.flyvpn.utils.MyTools;

import java.util.List;

/**
 * ClassName: MpcController
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午11:05
 */
public class MpcController {

    public static final HandlerThread mSendMpcThread = new HandlerThread("SendToMpcTask");

    static {
        mSendMpcThread.start();
    }

    private static final Handler mSendMpcHandler = new Handler(mSendMpcThread.getLooper());
    private RatdSocketTask socketClient;

    private MpcController() {
    }

    public static MpcController getInstance() {
        return MpcController.MpcControllerHolder.sInstance;
    }

    private static class MpcControllerHolder {
        public static final MpcController sInstance = new MpcController();
    }

    public void init(RatdSocketTask ratdSocketTask) {
        this.socketClient = ratdSocketTask;
    }

    public void initMpc(String ip, String uid) {
        mSendMpcHandler.post(new Runnable() {
            @Override
            public void run() {
                if (socketClient == null || !socketClient.sendMessage(String.format(MpcMessage.initMpc, MyTools.createSessionId()))) {
                    mSendMpcHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void enableMpcDefault(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mSendMpcHandler.post(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    LinkProperties linkProperties = cm.getLinkProperties(cm.getActiveNetwork());
                    if (linkProperties != null) {
                        String iface = linkProperties.getInterfaceName();
                        if (!TextUtils.isEmpty(iface)) {
                            int netType = iface.startsWith("wlan") ? 4 : iface.startsWith("rmnet_data") ? 2 : iface.startsWith("mcwill") ? 1 : -1;
                            if (netType > 0) {
                                if (socketClient != null) {
                                    flag = socketClient.sendMessage(String.format(MpcMessage.enableMpc, netType, iface, MyTools.createSessionId()));
                                }
                            }
                        }
                    }
                }
                if (!flag) {
                    mSendMpcHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void enableMpc(final NetworkLink networkLink) {

    }

    public void disableMpc(NetworkLink networkLink) {

    }

    public void addNetworkLink(Context context, final int type) {
        NetworkLink networkLink = MpcStatus.getInstance().getNetLink(type);
        if (socketClient == null || networkLink == null || networkLink.isLink) return;
        final ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mSendMpcHandler.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Network networks[] = cm.getAllNetworks();
                    for (Network network : networks) {
                        LinkProperties linkProperties = cm.getLinkProperties(network);
                        if (linkProperties != null) {
                            String iface = linkProperties.getInterfaceName();
                            if (TextUtils.isEmpty(iface)) continue;
                            int netType = iface.startsWith("wlan") ? 4 : iface.startsWith("rmnet_data") ? 2 : iface.startsWith("mcwill") ? 1 : -1;
                            if (netType == type) {
                                List<LinkAddress> linkAddress = linkProperties.getLinkAddresses();
                                if (linkAddress != null && !linkAddress.isEmpty()) {
                                    String ip = linkAddress.get(0).toString();
                                    ip = ip.substring(0, ip.indexOf("/"));
                                    if (TextUtils.isEmpty(ip)) {
                                        socketClient.sendMessage(String.format(MpcMessage.addLink, netType, iface, ip, MyTools.createSessionId()));
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public void delNetworkLink(NetworkLink networkLink) {

    }
}
