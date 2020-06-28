package com.android.server.octopu;

import android.content.Context;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuListener;
import android.octopu.WifiDeviceBean;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings;

import com.android.server.octopu.wifiextend.bean.PriDelParam;
import com.android.server.octopu.wifiextend.bean.PriDownParam;
import com.android.server.octopu.wifiextend.bean.PriUpParam;
import com.android.server.octopu.wifiextend.bean.PubDelParam;
import com.android.server.octopu.wifiextend.bean.PubDownParam;
import com.android.server.octopu.wifiextend.bean.PubUpParam;
import com.android.server.octopu.wifiextend.bean.ResultPriData;
import com.android.server.octopu.wifiextend.bean.ResultPubData;
import com.android.server.octopu.wifiextend.http.HttpResult;
import com.android.server.octopu.wifiextend.http.HttpTools;
import com.android.server.octopu.wifiextend.store.WifiDeviceSQLite;
import com.android.server.octopu.wifiextend.utils.BASE64Util;
import com.android.server.octopu.wifiextend.utils.CheckUtils;

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
    static final int WIFIEXTEND_HTTP_DWONPUB = 1;
    static final int WIFIEXTEND_HTTP_DWONPRI = 2;
    static final int WIFIEXTEND_HTTP_DEL = 3;
    static final int WIFIEXTEND_HTTP_UP = 4;
    private String deviceId;
    private Context mContext;
    private HandlerThread mWifiextenTask;
    private Handler mWifiextenHandler;
    private final WifiDeviceSQLite dbHelper;
    private static RemoteCallbackList<OctopuListener> mOctopuListeners = new RemoteCallbackList<>();

    private List<WifiDeviceBean> mWifiDevices = new ArrayList<>();
    private final Object mWifiDevicesLock = new Object();

    private int retryTime = 2500;
    private static final int MAX_RETRY_TIME = 3600000;

    public OctopuService(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        mContext = context;
        dbHelper = new WifiDeviceSQLite(context);
        mWifiextenTask = new HandlerThread("wifiextend");
        mWifiextenTask.start();
        mWifiextenHandler = new WifiextendHandler(mWifiextenTask.getLooper());
        mWifiextenHandler.sendEmptyMessage(WIFIEXTEND_HTTP_DWONPRI);
    }

    @Override
    public void flyWifiDevices(List<String> wifiBssids) throws RemoteException {
        synchronized (mWifiDevicesLock) {
            mWifiDevices.clear();
        }
        Message.obtain(mWifiextenHandler, WIFIEXTEND_HTTP_DWONPUB, wifiBssids).sendToTarget();
    }

    @Override
    public void delWifiDevices(WifiDeviceBean wifiDeviceBean) throws RemoteException {
        Message.obtain(mWifiextenHandler, WIFIEXTEND_HTTP_DEL, wifiDeviceBean).sendToTarget();
    }

    @Override
    public void upWifiDevices(WifiDeviceBean wifiDeviceBean) throws RemoteException {
        Message.obtain(mWifiextenHandler, WIFIEXTEND_HTTP_UP, wifiDeviceBean).sendToTarget();
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
        FlyLog.d("merge list szie=" + wifiDevices.size());
        FlyLog.d("merge list=" + wifiDevices);
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
                //解密密码，每个客户端生成一个实例
                synchronized (mWifiDevicesLock) {
                    final List<WifiDeviceBean> tempList = new ArrayList<WifiDeviceBean>();
                    for (WifiDeviceBean wifiDeviceBean : mWifiDevices) {
                        WifiDeviceBean tempWifi = new WifiDeviceBean();
                        tempWifi.id = wifiDeviceBean.id;
                        tempWifi.wifiDeviceId = wifiDeviceBean.wifiDeviceId;
                        tempWifi.wifiPassword = wifiDeviceBean.wifiPassword;
                        tempWifi.wifiAuthType = wifiDeviceBean.wifiAuthType;
                        tempWifi.wifiName = wifiDeviceBean.wifiName;
                        tempWifi.wifiStatus = wifiDeviceBean.wifiStatus;
                        tempWifi.wifiCreateTime = wifiDeviceBean.wifiCreateTime;
                        tempWifi.wifiUpdateTime = wifiDeviceBean.wifiUpdateTime;
                        tempWifi.userId = wifiDeviceBean.userId;
                        tempWifi.longitude = wifiDeviceBean.longitude;
                        tempWifi.latitude = wifiDeviceBean.latitude;
                        tempWifi.remarks = wifiDeviceBean.remarks;
                        tempWifi.pswdStatu = wifiDeviceBean.pswdStatu;
                        if ("1".equals(tempWifi.wifiAuthType)) {
                            //加密类型
                            String desPassword = CheckUtils.MD5_16bit(tempWifi.userId + "XINWEI!@#$");
                            if (desPassword != null) {
                                byte[] decodeBase64Pwd = BASE64Util.decode(tempWifi.wifiPassword);
                                byte[] detBytes;
                                try {
                                    detBytes = CheckUtils.decrypt(decodeBase64Pwd, desPassword);
                                    tempWifi.wifiPassword = new String(detBytes);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        tempList.add(tempWifi);
                    }
                    mOctopuListeners.getBroadcastItem(i).notifyWifiDevices(tempList);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private class WifiextendHandler extends Handler {
        static final String PubDownURL = "http://wifi.cootel.com/wifi/public/wifiInfoManage/downloadWifiInfo";
        static final String PriDownURL = "http://wifi.cootel.com/wifi/private/wifiInfoManage/downloadWifiInfo";
        static final String PubDelURL = "http://wifi.cootel.com/wifi/public/wifiInfoManage/removeWifiInfo";
        static final String PriDelURL = "http://wifi.cootel.com/wifi/private/wifiInfoManage/removeWifiInfo";
        static final String PubUpURL = "http://wifi.cootel.com/wifi/public/wifiInfoManage/shareWifiInfo";
        static final String PriUpURL = "http://wifi.cootel.com/wifi/private/wifiInfoManage/shareWifiInfo";
        static final String en_key = "XINWEI!@#$";

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
                case WIFIEXTEND_HTTP_DEL:
                    handleDel((WifiDeviceBean) msg.obj);
                    break;
            }
        }

        private void handleHttpPubDown(final List<String> wifiBssids) {
            //同步删除服务器公有
            List<WifiDeviceBean> delList = dbHelper.getDelPubWifiDevices();
            for (WifiDeviceBean wifiDeviceBean : delList) {
                PubDelParam pubDelParam = new PubDelParam(mContext);
                //解密密码后生成签名
                if ("1".equals(wifiDeviceBean.wifiAuthType)) {
                    //加密类型
                    String desPassword = CheckUtils.MD5_16bit(wifiDeviceBean.userId + "XINWEI!@#$");
                    if (desPassword != null) {
                        byte[] decodeBase64Pwd = BASE64Util.decode(wifiDeviceBean.wifiPassword);
                        byte[] detBytes;
                        try {
                            detBytes = CheckUtils.decrypt(decodeBase64Pwd, desPassword);
                            wifiDeviceBean.wifiPassword = new String(detBytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String signature = wifiDeviceBean.userId + wifiDeviceBean.wifiDeviceId + wifiDeviceBean.wifiPassword + en_key;
                String md5Signature = CheckUtils.MD5_16bit(signature);
                if (md5Signature != null) {
                    byte[] md5SignatureRaw = md5Signature.getBytes();
                    pubDelParam.signature = BASE64Util.encode(md5SignatureRaw);
                } else {
                    pubDelParam.signature = "";
                }
                pubDelParam.deviceType = "1";
                pubDelParam.deviceInfo = "C10";
                pubDelParam.wifiDeviceId = wifiDeviceBean.wifiDeviceId;
                pubDelParam.remarks = wifiDeviceBean.remarks;
                HttpResult ret = HttpTools.doPostJson(PubDelURL, pubDelParam.toJson());
                if (200 == ret.code) {
                    if (dbHelper.delPubWifiDevice(wifiDeviceBean) > 0) {
                        FlyLog.e("del pub wifi http request success:" + wifiDeviceBean);
                    }
                } else {
                    FlyLog.e("del pub wifi http request error:" + wifiDeviceBean);
                }
            }

            final PubDownParam param1 = new PubDownParam(mContext);
            if (wifiBssids != null) {
                for (String bssid : wifiBssids) {
                    param1.addWifiDeviceIds(bssid);
                }
            }
            //网络获取公有密钥
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
                if (pubList != null && !pubList.isEmpty() && wifiBssids != null) {
                    for (int i = pubList.size() - 1; i >= 0; i--) {
                        boolean isFind = false;
                        for (String wifiBssid : wifiBssids) {
                            if (pubList.get(i).wifiDeviceId.equals(wifiBssid)) {
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind) {
                            pubList.remove(i);
                        }
                    }
                    mergeWifiDevices(pubList);
                }
            }

            //从本地私有库获取密钥
            List<WifiDeviceBean> priList = dbHelper.getPriWifiDevices();
            if (priList != null && !priList.isEmpty() && wifiBssids != null) {
                for (int i = priList.size() - 1; i >= 0; i--) {
                    boolean isFind = false;
                    for (String wifiBssid : wifiBssids) {
                        if (priList.get(i).wifiDeviceId.equals(wifiBssid)) {
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        priList.remove(i);
                    }
                }
                mergeWifiDevices(priList);
            }

            notifyWifiDevices();

        }

        private void handleHttpPriDown() {
            //同步删除服务器私有
            List<WifiDeviceBean> delList = dbHelper.getDelPriWifiDevices();
            for (WifiDeviceBean wifiDeviceBean : delList) {
                PriDelParam priDelParam = new PriDelParam(mContext);
                priDelParam.wifiPwd = wifiDeviceBean.wifiPassword;
                //解密密码后生成签名
                if ("1".equals(wifiDeviceBean.wifiAuthType)) {
                    //加密类型
                    String desPassword = CheckUtils.MD5_16bit(wifiDeviceBean.userId + "XINWEI!@#$");
                    if (desPassword != null) {
                        byte[] decodeBase64Pwd = BASE64Util.decode(wifiDeviceBean.wifiPassword);
                        byte[] detBytes;
                        try {
                            detBytes = CheckUtils.decrypt(decodeBase64Pwd, desPassword);
                            wifiDeviceBean.wifiPassword = new String(detBytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String signature = wifiDeviceBean.userId + wifiDeviceBean.wifiDeviceId + wifiDeviceBean.wifiPassword + en_key;
                String md5Signature = CheckUtils.MD5_16bit(signature);
                if (md5Signature != null) {
                    byte[] md5SignatureRaw = md5Signature.getBytes();
                    priDelParam.signature = BASE64Util.encode(md5SignatureRaw);
                } else {
                    priDelParam.signature = "";
                }
                priDelParam.deviceType = "1";
                priDelParam.deviceInfo = "C10";
                priDelParam.wifiDeviceId = wifiDeviceBean.wifiDeviceId;
                priDelParam.remarks = wifiDeviceBean.remarks;
                HttpResult ret = HttpTools.doPostJson(PriDelURL, priDelParam.toJson());
                if (200 == ret.code) {
                    if (dbHelper.delPubWifiDevice(wifiDeviceBean) > 0) {
                        FlyLog.e("del pub wifi http request success:" + wifiDeviceBean);
                    }
                } else {
                    FlyLog.e("del pri wifi http request error:" + wifiDeviceBean);
                }
            }

            PriDownParam param1 = new PriDownParam(mContext);
            HttpResult ret = HttpTools.doPostJson(PriDownURL, param1.toJson());
            if (200 == ret.code) {
                ResultPriData resultPriData = ResultPriData.createByJson(ret.data);
                FlyLog.d("resultPriData = %s", resultPriData);
                if (resultPriData != null && resultPriData.retInfo != null && resultPriData.retInfo.wifiList != null) {
                    dbHelper.updataPriWifiDevices(resultPriData.retInfo.wifiList);
                }
                //TODO:
//                sendEmptyMessageDelayed(WIFIEXTEND_HTTP_DWONPRI, 7200000);
            } else {
                //私有密钥库发送网络请求失败,重试,首次5s,最长间隔时间1小时
                retryTime = Math.min(retryTime * 2, MAX_RETRY_TIME);
                sendEmptyMessageDelayed(WIFIEXTEND_HTTP_DWONPRI, retryTime);
            }
        }

        private void handleDel(WifiDeviceBean wifiDeviceBean) {
            try {
                WifiDeviceBean pri = dbHelper.getPriWifiDevice(wifiDeviceBean);
                //TODO: password
                if (pri != null && pri.pswdStatu != -1) {
                    pri.pswdStatu = -1;
                    dbHelper.updataPriWifiDevice(pri);
                }
                WifiDeviceBean pub = dbHelper.getPubWifiDevice(wifiDeviceBean);
                //TODO: password
                if (pub != null && pub.pswdStatu != -1) {
                    pub.pswdStatu = -1;
                    dbHelper.updataPubWifiDevice(pub);
                }
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
            notifyWifiDevices();
        }

        private void handleUpPri(WifiDeviceBean wifiDeviceBean) {
            PriUpParam param = new PriUpParam();
            param.subsId = wifiDeviceBean.userId;
            param.version = "";
            param.lat = wifiDeviceBean.latitude;
            param.lon = wifiDeviceBean.longitude;
            param.wifiName = wifiDeviceBean.wifiName;
            param.wifiDeviceId = wifiDeviceBean.wifiDeviceId;
            param.wifiStatus = String.valueOf(wifiDeviceBean.wifiStatus);
            //wifi 密码加密的密钥
            final String desPassword = CheckUtils.MD5_16bit(wifiDeviceBean.userId + en_key);
            byte[] encryptPassword = CheckUtils.encrypt(wifiDeviceBean.wifiPassword.getBytes(), desPassword);
            if (encryptPassword != null) {
                param.wifiPwd = BASE64Util.encode(encryptPassword);
            }
            //签名
            String signature = wifiDeviceBean.userId + wifiDeviceBean.wifiDeviceId + en_key;
            String md5Signature = CheckUtils.MD5_16bit(signature);
            if (md5Signature != null) {
                byte[] md5SignatureRaw = md5Signature.getBytes();
                param.signature = BASE64Util.encode(md5SignatureRaw);
            }
        }

        private void handleUpPub(WifiDeviceBean wifiDeviceBean) {
            PubUpParam param = new PubUpParam();
            param.subsId = wifiDeviceBean.userId;
            param.lat = wifiDeviceBean.latitude;
            param.lon = wifiDeviceBean.longitude;
            param.wifiName = wifiDeviceBean.wifiName;
            param.wifiDeviceId = wifiDeviceBean.wifiDeviceId;
            param.wifiStatus = String.valueOf(wifiDeviceBean.wifiStatus);
            //wifi 密码加密的密钥
            final String desPassword = CheckUtils.MD5_16bit(wifiDeviceBean.userId + en_key);
            byte[] encryptPassword = CheckUtils.encrypt(wifiDeviceBean.wifiPassword.getBytes(), desPassword);
            if (encryptPassword != null) {
                param.wifiPwd = BASE64Util.encode(encryptPassword);
            }
            //签名
            String signature = wifiDeviceBean.userId + wifiDeviceBean.wifiDeviceId + en_key;
            String md5Signature = CheckUtils.MD5_16bit(signature);
            if (md5Signature != null) {
                byte[] md5SignatureRaw = md5Signature.getBytes();
                param.signature = BASE64Util.encode(md5SignatureRaw);
            }
        }
    }

}
