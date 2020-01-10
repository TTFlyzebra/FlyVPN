// OctopuListener.aidl
package android.octopu;

import android.octopu.WifiDeviceBean;
// Declare any non-default types here with import statements

interface OctopuListener {

    void notifyWifiDevices(in List<WifiDeviceBean> wifiDevices);
}
