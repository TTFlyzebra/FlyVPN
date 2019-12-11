package com.flyzebra.flyvpn.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.text.TextUtils;

import java.util.List;

/**
 * ClassName: MpcStatus
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:18
 */
public class MpcStatus {
    /**
     * 初始化
     */
    public Boolean mpcInit = false;
    /**
     * 开启双流
     */
    public Boolean mpcEnable = false;
    /**
     * mcwill链路
     */
    public NetworkLink mcwillLink = new NetworkLink(1);
    /**
     * 4G链路
     */
    public NetworkLink mobileLink = new NetworkLink(2);
    /**
     * Wifi链路
     */
    public NetworkLink wifiLink = new NetworkLink(4);


    public NetworkLink defaultLink = new NetworkLink(-1);


    private MpcStatus() {
    }

    public static MpcStatus getInstance() {
        return MpcStatusHolder.sInstance;
    }

    public NetworkLink getNetLink(int type) {
        switch (type) {
            case 1:
                return mcwillLink;
            case 2:
                return mobileLink;
            case 4:
                return wifiLink;
            default:
                return defaultLink;
        }
    }

    private static class MpcStatusHolder {
        public static final MpcStatus sInstance = new MpcStatus();
    }

    public void init(Context context) {
        mpcInit = false;
        mcwillLink.reset();
        mobileLink.reset();
        wifiLink.reset();

        mcwillLink.type = 1;
        mobileLink.type = 2;
        wifiLink.type = 4;

        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                LinkProperties linkProperties = cm.getLinkProperties(network);
                if (linkProperties != null) {
                    String iface = linkProperties.getInterfaceName();
                    if (TextUtils.isEmpty(iface)) continue;
                    List<LinkAddress> linkAddress = linkProperties.getLinkAddresses();
                    if (linkAddress != null && !linkAddress.isEmpty()) {
                        String ip = linkAddress.get(0).toString();
                        ip = ip.substring(0, ip.indexOf("/"));
                        if (TextUtils.isEmpty(ip)) {
                            if (iface.startsWith("wlan")) {
                                wifiLink.name = iface;
                                wifiLink.ip = ip;
                            } else if (iface.startsWith("rmnet_data")) {
                                mobileLink.name = iface;
                                mobileLink.ip = ip;
                            } else if (iface.startsWith("mcwill")) {
                                mcwillLink.name = iface;
                                mcwillLink.ip = ip;
                            }
                        }
                    }
                }
            }
        }
    }

}
