package android.octopu.wifi.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

/**
 * @author zhangqing
 * @date 2018/7/18
 */
public class RequestBaseParam {
    public String deviceType;
    public String deviceId;
    public String deviceInfo;
    public String remarks;

    @SuppressLint("HardwareIds")
    public RequestBaseParam(Context context) {
        deviceType = "1";
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceInfo = android.os.Build.MODEL;
        remarks = "thisisaxinweiWIFI";
    }
}
