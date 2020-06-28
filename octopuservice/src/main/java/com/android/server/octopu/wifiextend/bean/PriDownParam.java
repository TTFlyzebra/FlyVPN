package com.android.server.octopu.wifiextend.bean;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.server.octopu.wifiextend.utils.SystemPropTools;

/**
 * ClassName: PubDownParam
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午12:48
 */
public class PriDownParam {
    public String deviceType = "1";
    public String deviceId;
    public String deviceInfo = android.os.Build.MODEL;
    public String remarks = "thisisaxinweiWIFI";
    public String subsId = "0";
//    private static final String jsonFromat = "{\"deviceType\":\"%s\",\"deviceId\":\"%s\",\"deviceInfo\":\"%s\",\"remarks\":\"%s\",\"subsId\":\"%s\"}";

    public PriDownParam(Context context) {
        if(TextUtils.isEmpty(deviceId)){
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        subsId = (SystemPropTools.get("persist.radio.mcwill.uid","0")).replace(".","").trim();
    }

    /**
     * 转换成Json
     * {"deviceType":"%s","deviceId":"%s","deviceInfo":"%s","remarks":"%s","subsId":"%s"}
     * @return
     */
    public String toJson() {
//        return String.format(jsonFromat,deviceType,deviceId,deviceInfo,remarks,subsId);
        return "{" +
                "\"deviceType\":\"" + deviceType +"\","+
                "\"deviceId\":\"" + deviceId +"\","+
                "\"deviceInfo\":\"" + deviceInfo +"\","+
                "\"remarks\":\"" + remarks +"\","+
                "\"subsId\":\"" + subsId +"\""+
                "}";
    }
}