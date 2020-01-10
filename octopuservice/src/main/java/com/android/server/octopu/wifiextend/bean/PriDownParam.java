package com.android.server.octopu.wifiextend.bean;

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
    public String subsId = "620b0512";
//    private static final String jsonFromat = "{\"deviceType\":\"%s\",\"deviceId\":\"%s\",\"deviceInfo\":\"%s\",\"remarks\":\"%s\",\"subsId\":\"%s\"}";

    public PriDownParam(String deviceId) {
        this.deviceId = deviceId;
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
