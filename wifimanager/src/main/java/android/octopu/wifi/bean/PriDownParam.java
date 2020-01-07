package android.octopu.wifi.bean;

import android.content.Context;
import android.provider.Settings;

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

    public PriDownParam(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
