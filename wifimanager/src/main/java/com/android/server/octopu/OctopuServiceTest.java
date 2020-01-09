package com.android.server.octopu;

import android.content.Context;
import android.octopu.FlyLog;
import android.octopu.IOctopuServiceTest;
import android.octopu.OctopuListener;
import android.octopu.wifi.bean.PubDownParam;
import android.octopu.wifi.bean.ResultPubData;
import android.octopu.wifi.bean.WifiDeviceBean;
import android.octopu.wifi.db.WifiDeviceSQLite;
import android.octopu.wifi.http.HttpTools;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.provider.Settings;

import java.util.List;

/**
 * ClassName: OctopuServiceTest
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:42
 */
public class OctopuServiceTest extends IOctopuServiceTest.Stub {
    private static String deviceId;
    private static final HandlerThread mThreadTask = new HandlerThread("OctopuService");

    static {
        mThreadTask.start();
    }

    private static final Handler mThreadHandler = new Handler(mThreadTask.getLooper());
    private final WifiDeviceSQLite dbHelper;
    private Context mContext;
    private OctopuListener octopuListener;

    public OctopuServiceTest(Context context) {
        mContext = context;
        dbHelper = new WifiDeviceSQLite(context);
        deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    @Override
    public void upWifiDeviceData(List<WifiDeviceBean> wifiDeviceBeans) throws RemoteException {
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final PubDownParam param1 = new PubDownParam(deviceId);
                String ret = HttpTools.doPostJson(
                        "http://wifi.cootel.com/wifi/public/wifiInfoManage/downloadWifiInfo",
                        param1.toJson());
                FlyLog.d("pub ret = %s", ret);
                ResultPubData resultPubData = ResultPubData.createByJson(ret);
                FlyLog.d("resultPubData = %s", resultPubData);
                if (resultPubData != null && resultPubData.retInfo != null) {
                    try {
                        octopuListener.notifyWifiDevices(resultPubData.retInfo);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    dbHelper.updataPubWifiDevices(resultPubData.retInfo);
                }
            }
        });
    }

    @Override
    public void addOctopuListener(OctopuListener octopuListener) throws RemoteException {
        this.octopuListener=octopuListener;
    }

    @Override
    public void removeOctopuListener(OctopuListener octopuListener) throws RemoteException {
        this.octopuListener = null;
    }
}
