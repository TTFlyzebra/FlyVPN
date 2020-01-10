// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;
import android.octopu.wifi.bean.WifiDeviceBean;

// Declare any non-default types here with import statements

interface IOctopuService {

    void upWifiDeviceData(in List<WifiDeviceBean> wifiDeviceBeans);

    void addOctopuListener(OctopuListener octopuListener);

    void removeOctopuListener(OctopuListener octopuListener);
}
