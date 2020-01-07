package android.octopu.wifi.bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RequestPublicParam
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午12:48
 */
public class RequestPublicParam extends RequestBaseParam {
    public List<String> wifiDeviceIds;
    public String subsId = "620b0512";
    public Double lat = 22.543849;
    public Double lon = 113.95081;

    public RequestPublicParam(Context context) {
        super(context);
        wifiDeviceIds = new ArrayList<>();
    }
}
