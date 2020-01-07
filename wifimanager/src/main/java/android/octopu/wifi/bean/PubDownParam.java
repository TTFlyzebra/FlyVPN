package android.octopu.wifi.bean;

import android.content.Context;
import android.provider.Settings;

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
    public List<String> wifiDeviceIds;
    public String subsId = "620b0512";
    public Double lat = 22.543849;
    public Double lon = 113.95081;

    public PubDownParam(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
