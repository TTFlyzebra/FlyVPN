package com.android.server.octopu.wifiextend.bean;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.server.octopu.wifiextend.utils.SystemPropTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName: PriDelParam
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午3:20
 */
public class PriDelParam {

    /**
     * signature : MDU3YzFmODJiY2RmNTIwNzdkMmNlZjB=
     * version : 0
     * deviceType : 1
     * deviceId : 88903071
     * deviceInfo : M31
     * wifiDeviceId : cc:11:22:33:f8:d8
     * wifiPwd : bjxinwei
     * subsId : 1644559456
     * remarks : thisisaxinweiWIFI
     */

    public String signature;
    public String version;
    public String deviceType;
    public String deviceId;
    public String deviceInfo;
    public String wifiDeviceId;
    public String wifiPwd;
    public String subsId;
    public String remarks;

    public PriDelParam(Context context){
        if (context != null && TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        subsId = (SystemPropTools.get("persist.radio.mcwill.uid", "0")).replace(".", "").trim();
    }

    public String toJson(){
        return "{" +
                "\"signature\":\"" + signature +"\","+
                "\"version\":\"" + version +"\","+
                "\"deviceType\":\"" + deviceType +"\","+
                "\"deviceId\":\"" + deviceId +"\","+
                "\"deviceInfo\":\"" + deviceInfo +"\","+
                "\"wifiDeviceId\":\"" + wifiDeviceId +"\","+
                "\"wifiPwd\":\"" + wifiPwd +"\","+
                "\"subsId\":\"" + subsId +"\","+
                "\"remarks\":\"" + remarks +"\""+
                "}";
    }

    public static ResultPriCode createByJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        ResultPriCode resultPriCode = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            resultPriCode = new ResultPriCode();
            if(jsonObject.has("retCode")){
                resultPriCode.retCode =jsonObject.getInt("retCode");
            }
            if(jsonObject.has("retMsg")){
                resultPriCode.retMsg = jsonObject.getString("retMsg");
            }
            if(jsonObject.has("retInfo")) {
                JSONObject subJsonObject = jsonObject.getJSONObject("retInfo");
                resultPriCode.retInfo = new ResultPriCode.RetInfoBean();
                if(subJsonObject.has("version")) {
                    resultPriCode.retInfo.version = subJsonObject.getString("version");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultPriCode;
    }

}
