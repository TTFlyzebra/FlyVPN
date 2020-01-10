package android.octopu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.android.server.octopu.wifi.bean.WifiDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: OctopuManager
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:44
 */
public class OctopuManager {
    private IOctopuService mService;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private OctopuListener octopuListener = new OctopuListener.Stub() {
        @Override
        public void notifyWifiDevices(final List<WifiDeviceBean> wifiDeviceBeans) throws RemoteException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (mLock) {
                        for (int i = 0; i < wifiDeviceListeners.size(); i++) {
                            wifiDeviceListeners.get(i).notifyWifiDevices(wifiDeviceBeans);
                        }
                    }
                }
            });

        }
    };

    public OctopuManager(Context context, IOctopuService octopuService) {
        mService = octopuService;
        try {
            mService.addOctopuListener(octopuListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void upWifiDeviceData(List<WifiDeviceBean> wifiDeviceBeans) {
        try {
            mService.upWifiDeviceData(wifiDeviceBeans);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private List<WifiDeviceListener> wifiDeviceListeners = new ArrayList<>();
    private final Object mLock = new Object();

    public interface WifiDeviceListener {
        void notifyWifiDevices(List<WifiDeviceBean> wifiDeviceBeans);
    }

    public void addWifiDeviceListener(WifiDeviceListener wifiDeviceListener) {
        wifiDeviceListeners.add(wifiDeviceListener);
    }

    public void removeWifiDeviceListener(WifiDeviceListener wifiDeviceListener) {
        synchronized (mLock) {
            wifiDeviceListeners.remove(wifiDeviceListener);
        }
    }


}
