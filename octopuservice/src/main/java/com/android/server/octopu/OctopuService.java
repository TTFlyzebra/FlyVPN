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

    private class WifiextendHandler extends Handler {
        static final int WIFIEXTEND_HTTP_DWONPUB = 1;
        static final int WIFIEXTEND_HTTP_DWONPRI = 2;
        static final int WIFIEXTEND_HTTP_DEL = 3;
        static final int WIFIEXTEND_HTTP_UP = 4;
        static final String PubDownURL = "http://wifi.cootel.com/wifi/public/wifiInfoManage/downloadWifiInfo";
        static final String PriDownURL = "http://wifi.cootel.com/wifi/private/wifiInfoManage/downloadWifiInfo";
        int retryTime = 5000;

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

        private void handleHttpPubDown(List<String> wifiBssids) {
            final PubDownParam param1 = new PubDownParam(deviceId);
            if (wifiBssids != null) {
                for (String bssid : wifiBssids) {
                    param1.addWifiDeviceIds(bssid);
                }
            }
            HttpResult ret = HttpTools.doPostJson(PubDownURL, param1.toJson());
            ResultPubData resultPubData = ResultPubData.createByJson(ret.data);
            FlyLog.d("resultPubData = %s", resultPubData);
            if (resultPubData != null && resultPubData.retInfo != null) {
                final int N = mOctopuListeners.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    try {
                        mOctopuListeners.getBroadcastItem(i).notifyWifiDevices(resultPubData.retInfo);
                    } catch (RemoteException e) {
                        FlyLog.e(e.toString());
                    } catch (Exception e) {
                        FlyLog.e(e.toString());
                    }
                }
                mOctopuListeners.finishBroadcast();
                dbHelper.updataPubWifiDevices(resultPubData.retInfo);
            }
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
            } else {
                //TODO:私有密钥库发送网络请求失败,重试，最长间隔时间1小时
                retryTime = Math.min(retryTime * 2, 3600000);
                sendEmptyMessageDelayed(WIFIEXTEND_HTTP_DWONPRI, retryTime);
            }
        }
    }

}
