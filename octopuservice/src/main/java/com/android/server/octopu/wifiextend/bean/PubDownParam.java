package com.android.server.octopu.wifiextend.bean;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.server.octopu.wifiextend.utils.SystemPropTools;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PubDownParam
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午12:48
 */
public class PubDownParam {
    public String deviceType = "1";
    public String deviceId;
    public String deviceInfo = android.os.Build.MODEL;
    public String remarks = "thisisaxinweiWIFI";
    public List<String> wifiDeviceIds = new ArrayList<>();
    public String subsId = "0";
    public Double lat = 0.0;
    public Double lon = 0.0;
//    private static final String jsonFromat = "{\"deviceType\":\"%s\",\"deviceId\":\"%s\",\"deviceInfo\":\"%s\",\"remarks\":\"%s\",\"wifiDeviceIds\":%s,\"subsId\":\"%s\",\"lat\":%f,\"lon\":%f}";

    public PubDownParam(Context context) {
        if (context != null && TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        subsId = (SystemPropTools.get("persist.radio.mcwill.uid", "0")).replace(".", "").trim();
    }


    public void addWifiDeviceIds(String wifiDeviceId) {
        wifiDeviceIds.add(wifiDeviceId);
    }

    /**
     * 转换成Json
     * {"deviceType":"%s","deviceId":"%s","deviceInfo":"%s","remarks":"%s","wifiDeviceIds":%s,"subsId":"%s","lat":%f,"lon":%f}
     *
     * @return
     */
    public String toJson() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        if (wifiDeviceIds != null) {
            for (int i = 0; i < wifiDeviceIds.size(); i++) {
                if (i == wifiDeviceIds.size() - 1) {
                    str.append("\"").append(wifiDeviceIds.get(i)).append("\"");
                } else {
                    str.append("\"").append(wifiDeviceIds.get(i)).append("\",");
                }
            }
        }
        str.append("]");
//        return String.format(jsonFromat,deviceType,deviceId,deviceInfo,remarks,str,subsId,lat,lon);
        return "{" +
                "\"deviceType\":\"" + deviceType + "\"," +
                "\"deviceId\":\"" + deviceId + "\"," +
                "\"deviceInfo\":\"" + deviceInfo + "\"," +
                "\"remarks\":\"" + remarks + "\"," +
                "\"wifiDeviceIds\":" + str.toString() + "," +
                "\"subsId\":\"" + subsId + "\"," +
                "\"lat\":" + lat + "," +
                "\"lon\":" + lon +
                "}";
    }
}
