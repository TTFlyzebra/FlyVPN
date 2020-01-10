package com.android.server.octopu;

import android.content.Context;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings;

import com.android.server.octopu.wifiextend.bean.PriDownParam;
import com.android.server.octopu.wifiextend.bean.PubDownParam;
import com.android.server.octopu.wifiextend.bean.ResultPriData;
import com.android.server.octopu.wifiextend.bean.ResultPubData;
import com.android.server.octopu.wifiextend.bean.WifiDeviceBean;
import com.android.server.octopu.wifiextend.http.HttpResult;
import com.android.server.octopu.wifiextend.http.HttpTools;
import com.android.server.octopu.wifiextend.store.WifiDeviceSQLite;

import java.util.ArrayList;
import java.util.List;


/**
 * @hide ClassName: OctopuService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:42
 */
public class OctopuService extends IOctopuService.Stub {
    private String deviceId;
    private Context mContext;
    private HandlerThread mWifiextenTask;
    private Handler mWifiextenHandler;
    private final WifiDeviceSQLite dbHelper;
    private static RemoteCallbackList<OctopuListener> mOctopuListeners = new RemoteCallbackList<>();

    private List<WifiDeviceBean> mWifiDevices = new ArrayList<>();
    private final Object mWifiDevicesLock = new Object();

    public OctopuService(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        mContext = context;
        dbHelper = new WifiDeviceSQLite(context);
        mWifiextenTask = new HandlerThread("wifiextend");
        mWifiextenTask.start();
        mWifiextenHandler = new WifiextendHandler(mWifiextenTask.getLooper());
        mWifiextenHandler.sendEmptyMessage(WifiextendHandler.WIFIEXTEND_HTTP_DWONPRI);
    }

    @Override
    public void flyWifiDevices(List<String> wifiBssids) throws RemoteException {
        synchronized (mWifiDevicesLock) {
            mWifiDevices.clear();
        }
        Message.obtain(mWifiextenHandler, WifiextendHandler.WIFIEXTEND_HTTP_DWONPUB, wifiBssids).sendToTarget();
    }

    @Override
    public void delWifiDevices(WifiDeviceBean wifiDeviceBean) throws RemoteException {
        Message.obtain(mWifiextenHandler, WifiextendHandler.WIFIEXTEND_HTTP_DEL, wifiDeviceBean).sendToTarget();
    }

    @Override
    public void upWifiDevices(WifiDeviceBean wifiDeviceBean) throws RemoteException {
        Message.obtain(mWifiextenHandler, WifiextendHandler.WIFIEXTEND_HTTP_UP, wifiDeviceBean).sendToTarget();
    }

    @Override
    public void registerListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.register(octopuListener);
    }

    @Override
    public void unregisterListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.unregister(octopuListener);
    }

    private void mergeWifiDevices(List<WifiDeviceBean> wifiDevices) {
        FlyLog.d("merge list szie="+wifiDevices.size());
        FlyLog.d("merge list="+wifiDevices);
        //TODO:算法可以根据顺序需要修改
        synchronized (mWifiDevicesLock) {
            final int size = mWifiDevices.size();
            for (WifiDeviceBean wifiDeviceBean1 : wifiDevices) {
                boolean isFind = false;
                for (int i = 0; i < size; i++) {
                    WifiDeviceBean wifiDeviceBean2 = mWifiDevices.get(i);
                    if (wifiDeviceBean1.wifiDeviceId.equals(wifiDeviceBean2.wifiDeviceId)) {
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    mWifiDevices.add(wifiDeviceBean1);
                }
            }
        }
    }

    private void notifyWifiDevices() {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mOctopuListeners.getBroadcastItem(i).notifyWifiDevices(mWifiDevices);
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private class WifiextendHandler extends Handler {
        static final int WIFIEXTEND_HTTP_DWONPUB = 1;
        static final int WIFIEXTEND_HTTP_DWONPRI = 2;
        static final int WIFIEXTEND_HTTP_DEL = 3;
        static final int WIFIEXTEND_HTTP_UP = 4;
        static final String PubDownURL = "http://wifi.cootel.com/wifi/public/wifiInfoManage/downloadWifiInfo";
        static final String PriDownURL = "http://wifi.cootel.com/wifi/private/wifiInfoManage/downloadWifiInfo";
        int retryTime = 2500;

        WifiextendHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WIFIEXTEND_HTTP_DWONPUB:
                    handleHttpPubDown((List<String>) msg.obj);
                    break;
                case WIFIEXTEND_HTTP_DWONPRI:
                    handleHttpPriDown();
                    break;
            }
        }

        private void handleHttpPubDown(final List<String> wifiBssids) {
            final PubDownParam param1 = new PubDownParam(deviceId);
            if (wifiBssids != null) {
                for (String bssid : wifiBssids) {
                    param1.addWifiDeviceIds(bssid);
                }
            }
            //网络获取密钥
            HttpResult ret = HttpTools.doPostJson(PubDownURL, param1.toJson());
            if (200 == ret.code) {
                ResultPubData resultPubData = ResultPubData.createByJson(ret.data);
                FlyLog.d("resultPubData = %s", resultPubData);
                if (resultPubData != null && resultPubData.retInfo != null) {
                    mergeWifiDevices(resultPubData.retInfo);
                    dbHelper.updataPubWifiDevices(resultPubData.retInfo);
                }
            }
            //网络获取失败从本地公有库获取密钥
            else {
                List<WifiDeviceBean> pubList = dbHelper.getPubWifiDevices();
                if(pubList!=null&&!pubList.isEmpty()) {
                    for (int i = pubList.size() - 1; i >= 0; i--) {
                        boolean isFind = false;
                        for(String wifiBssid:wifiBssids) {
                            if (pubList.get(i).wifiDeviceId.equals(wifiBssid)) {
                                isFind = true;
                                break;
                            }
                        }
                        if(!isFind){
                            pubList.remove(i);
                        }
                    }
                    mergeWifiDevices(pubList);
                }
            }

            //从本地私有库获取密钥
            List<WifiDeviceBean> priList = dbHelper.getPubWifiDevices();
            if(priList!=null&&!priList.isEmpty()) {
                for (int i = priList.size() - 1; i >= 0; i--) {
                    boolean isFind = false;
                    for(String wifiBssid:wifiBssids) {
                        if (priList.get(i).wifiDeviceId.equals(wifiBssid)) {
                            isFind = true;
                            break;
                        }
                    }
                    if(!isFind){
                        priList.remove(i);
                    }
                }
                mergeWifiDevices(priList);
            }

            notifyWifiDevices();

        }

        private void handleHttpPriDown() {
            final PriDownParam param1 = new PriDownParam(deviceId);
            HttpResult ret = HttpTools.doPostJson(PriDownURL, param1.toJson());
            if (200 == ret.code) {
                ResultPriData resultPriData = ResultPriData.createByJson(ret.data);
                FlyLog.d("resultPubData = %s", resultPriData);
                if (resultPriData != null && resultPriData.retInfo != null && resultPriData.retInfo.wifiList != null) {
                    dbHelper.updataPriWifiDevices(resultPriData.retInfo.wifiList);
                }
                //TODO:
//                sendEmptyMessageDelayed(WIFIEXTEND_HTTP_DWONPRI, 7200000);
            } else {
                //私有密钥库发送网络请求失败,重试,首次5s,最长间隔时间1小时
                retryTime = Math.min(retryTime * 2, 3600000);
                sendEmptyMessageDelayed(WIFIEXTEND_HTTP_DWONPRI, retryTime);
            }
        }
    }

}
