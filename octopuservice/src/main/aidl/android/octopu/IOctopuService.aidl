// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;
import android.octopu.WifiDeviceBean;

// Declare any non-default types here with import statements

interface IOctopuService {

    void flyWifiDevices(in List<String> wifiBssids);

    void delWifiDevices(in WifiDeviceBean wifiDeviceBean);

    void upWifiDevices(in WifiDeviceBean wifiDeviceBean);

    void registerListener(OctopuListener octopuListener);

    void unregisterListener(OctopuListener octopuListener);
}
