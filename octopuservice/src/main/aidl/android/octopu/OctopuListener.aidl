// OctopuListener.aidl
package android.octopu;

import com.android.server.octopu.wifi.bean.WifiDeviceBean;
// Declare any non-default types here with import statements

interface OctopuListener {

    void notifyWifiDevices(in List<WifiDeviceBean> wifiDeviceBeans);
}
