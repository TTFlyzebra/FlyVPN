package android.octopu.wifi.bean;

import android.content.Context;
import android.provider.Settings;

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
    public String subsId = "620b0512";
    public Double lat = 22.543849;
    public Double lon = 113.95081;

    public PubDownParam(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public void addWifiDeviceIds(String wifiDeviceId){
        wifiDeviceIds.add(wifiDeviceId);
    }

    /**
     * 转换成Json
     * {"deviceType":"1","deviceId":"bc58dc871080e39f","deviceInfo":"C8","remarks":"thisisaxinweiWIFI","wifiDeviceIds":[],"subsId":"620b0512","lat":22.543849,"lon":113.95081}
     *
     * @return
     */
    public String toJson() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        if(wifiDeviceIds!=null) {
            for (int i = 0; i < wifiDeviceIds.size(); i++) {
                if (i == wifiDeviceIds.size() - 1) {
                    str.append("\"").append(wifiDeviceIds.get(i)).append("\"");
                } else {
                    str.append("\"").append(wifiDeviceIds.get(i)).append("\",");
                }
            }
        }
        str.append("]");
        return "{" +
                "\"deviceType\":\"" + deviceType + "\"," +
                "\"deviceId\":\"" + deviceId + "\"," +
                "\"deviceInfo\":\"" + deviceInfo + "\"," +
                "\"remarks\":\"" + remarks + "\"," +
                "\"wifiDeviceIds\":" + str.toString() + "," +
                "\"subsId\":\"" + subsId + "\"," +
                "\"lat\":" + lat + "," +
                "\"lon\":" + lon  +
                "}";
    }
}
