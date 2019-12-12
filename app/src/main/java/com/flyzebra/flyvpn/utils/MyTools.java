package com.flyzebra.flyvpn.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.text.TextUtils;

import com.flyzebra.flyvpn.data.NetworkLink;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * ClassName: MyTools
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:56
 */
public class MyTools {
    /**
     * 根据当前时间生成SessionID
     * @return
     */
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

    public static void upLinkManager(Context context, boolean wifi, boolean mobile, boolean mcwill) {
        Intent intent = new Intent("intent.action.UPDATE_MP_STATUS_FOR_LINK_MANAGER");
        intent.putExtra("NETWORK_LINK_WIFI", wifi ? 1 : 0);
        intent.putExtra("NETWORK_LINK_4G", mobile ? 1 : 0);
        intent.putExtra("NETWORK_LINK_MCWILL", mcwill ? 1 : 0);
        context.sendBroadcast(intent);
    }


    public static void setNetWorkLinks(Context context, NetworkLink wifi,NetworkLink mobile,NetworkLink mcwill){
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
                        if (!TextUtils.isEmpty(ip)) {
                            if (iface.startsWith("wlan")) {
                                wifi.netTypeName = iface;
                                wifi.ip = ip;
                            } else if (iface.startsWith("rmnet_data")) {
                                mobile.netTypeName = iface;
                                mobile.ip = ip;
                            } else if (iface.startsWith("mcwill")) {
                                mcwill.netTypeName = iface;
                                mcwill.ip = ip;
                            }
                        }
                    }
                }
            }
        }
    }
}
