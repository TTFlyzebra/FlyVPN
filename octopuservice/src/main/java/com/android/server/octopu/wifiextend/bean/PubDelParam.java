package com.android.server.octopu.wifiextend.bean;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.server.octopu.wifiextend.utils.SystemPropTools;

/**
 * ClassName: PubDelParam
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午3:28
 */
public class PubDelParam {
    /**
     * signature : MDU3YzFmODJiY2RmNTIwNzdkMmNlZjB=
     * deviceType : 1
     * deviceId : 88903071
     * deviceInfo : M31
     * wifiDeviceId : cc:11:22:33:f8:d8
     * wifiPwd : bjxinwei
     * subsId : 1644559456
     * remarks : thisisaxinweiWIFI
     */

    public String signature;
    public String deviceType;
    public String deviceId;
    public String deviceInfo;
    public String wifiDeviceId;
    public String subsId;
    public String remarks = "thisisaxinweiWIFI";

    public PubDelParam(Context context){
        if (context != null && TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        subsId = (SystemPropTools.get("persist.radio.mcwill.uid", "0")).replace(".", "").trim();
    }

    public String toJson(){
        return "{" +
                "\"signature\":\"" + signature +"\","+
                "\"deviceType\":\"" + deviceType +"\","+
                "\"deviceId\":\"" + deviceId +"\","+
                "\"deviceInfo\":\"" + deviceInfo +"\","+
                "\"wifiDeviceId\":\"" + wifiDeviceId +"\","+
                "\"subsId\":\"" + subsId +"\","+
                "\"remarks\":\"" + remarks +"\""+
                "}";
    }
}
