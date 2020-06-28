package android.octopu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

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
    private static final int NOTIFY_WIFIDEVICES = 1;

    private List<WifiDeviceListener> mWifiDeviceListeners = new ArrayList<>();
    private final Object mListenerLock = new Object();
    private List<WifiDeviceBean> mwifiDevices = new ArrayList<>() ;
    private final Object mWifiListLock = new Object();
    private IOctopuService mService;
    private Handler mHandler = new MainHandler(Looper.getMainLooper());
    private OctopuListener mOctopuListener = new OctopuListener.Stub() {
        @Override
        public void notifyWifiDevices(final List<WifiDeviceBean> wifiDevices) throws RemoteException {
            synchronized (mWifiListLock) {
                mwifiDevices.clear();
                mwifiDevices.addAll(wifiDevices);
            }
            mHandler.sendEmptyMessage(NOTIFY_WIFIDEVICES);
        }
    };

    public OctopuManager(Context context, IOctopuService octopuService) {
        mService = octopuService;
        try {
            mService.registerListener(mOctopuListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void flyWifiDevices(List<String> wifiBssids) {
        try {
            mService.flyWifiDevices(wifiBssids);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void delWifiDevices(WifiDeviceBean delWifiDevices) {
        try {
            mService.delWifiDevices(delWifiDevices);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void upWifiDevices(WifiDeviceBean wifiDeviceBean) {
        try {
            mService.upWifiDevices(wifiDeviceBean);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public interface WifiDeviceListener {
        void notifyWifiDevices(List<WifiDeviceBean> wifiDevices);
    }

    public void addWifiDeviceListener(WifiDeviceListener wifiDeviceListener) {
        synchronized (mListenerLock) {
            mWifiDeviceListeners.add(wifiDeviceListener);
        }
    }

    public void removeWifiDeviceListener(WifiDeviceListener wifiDeviceListener) {
        synchronized (mListenerLock) {
            mWifiDeviceListeners.remove(wifiDeviceListener);
        }
    }


    private class MainHandler extends Handler {
        public MainHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage( Message msg) {
            switch (msg.what){
                case NOTIFY_WIFIDEVICES:
                    synchronized (mListenerLock) {
                        for (WifiDeviceListener wifiDeviceListener: mWifiDeviceListeners) {
                            synchronized (mWifiListLock){
                                wifiDeviceListener.notifyWifiDevices(new ArrayList<WifiDeviceBean>(mwifiDevices));
                            }
                        }
                    }
                    break;
            }
        }
    }
}
