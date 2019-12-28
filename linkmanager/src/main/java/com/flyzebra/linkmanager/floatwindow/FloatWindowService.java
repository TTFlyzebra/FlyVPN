//package com.flyzebra.linkmanager.floatwindow;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//
//import com.cootf.commonlib.config.LinkConfig;
//import com.cootf.commonlib.entity.McwillNetworkInfo;
//import com.cootf.commonlib.entity.SimNetworkInfo;
//import com.cootf.commonlib.event.ControllerEvent;
//import com.cootf.commonlib.event.CurrentLinkEvent;
//import com.cootf.commonlib.event.RefreshUiEvent;
//import com.cootf.commonlib.utils.AppConstants;
//import com.cootf.commonlib.utils.LogUtils;
//import com.cootf.commonlib.utils.SPUtils;
//import com.cootf.commonlib.utils.SysPropUtils;
//import com.cootf.commonlib.utils.ThreadPoolUtils;
//import com.flyzebra.utils.FlyLog;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.lang.ref.WeakReference;
//import java.text.SimpleDateFormat;
//import java.util.concurrent.TimeUnit;
//
//import xiaofei.library.hermeseventbus.HermesEventBus;
//
//import static com.cootf.commonlib.event.LinkInfoEvent.Link.LINK_GNET;
//import static com.cootf.commonlib.event.LinkInfoEvent.Link.LINK_MNET;
//import static com.cootf.commonlib.event.LinkInfoEvent.Link.LINK_WIFI;
//
///**
// * @Author: __ Weiyi.Lee  liweiyi@cootf.com
// * @Package: _ com.octopus.linkmanager.float_window
// * @DESC: ____ 悬浮窗服务类，用于拉起悬浮窗
// * @Time: ____ created at-2018-09-19 16:27
// */
//public class FloatWindowService extends Service implements Runnable {
//    private static String TAG = "FloatWindowService";
//    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private final static int ROUTE_WIFI = 2;
//    private final static int ROUTE_M_NET = 0;
//    private final static int ROUTE_G_NET = 1;
//    private String linkWifiInfo = "";
//    private String linkMNetInfo = "";
//    private String linkGNetInfo = "";
//    private UpdateRouteInfoHandler mUpdateRouteInfoHandler = null;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        FlyLog.d( "FloatWindowService onCreate...");
//        mUpdateRouteInfoHandler = new UpdateRouteInfoHandler(this);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        FlyLog.d( "FloatWindowService onStartCommand....");
//        ThreadPoolUtils.getInstance().getThreadPool().scheduleAtFixedRate(this, 0, 5000, TimeUnit.MILLISECONDS);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    /**
//     * 拉起链路管理窗口
//     *
//     * @param context
//     */
//    public static void startSelf(Context context) {
//        Intent startIntent = new Intent(context, FloatWindowService.class);
//        context.startService(startIntent);
//    }
//
//    /**
//     * 关闭悬浮窗
//     */
//    public static void stopFloatWindowService(Context context) {
//        Intent stopIntent = new Intent(context, FloatWindowService.class);
//        context.stopService(stopIntent);
//    }
//
//    @Override
//    public void onDestroy() {
//        unregisterEventBus();
//        FlyLog.d( "onDestroy service");
//        super.onDestroy();
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 100, sticky = true)
//    public void onEvent(ControllerEvent event) {
//        FlyLog.d( "FloatWindowService onEvent LinkInfo refresh");
//        switch (event.mLink) {
//            case LINK_WIFI:
//                linkWifiInfo = null;
////                WifiController wifiController = event.getWifiController();
//                if (!SysPropUtils.isDetectingWifi()) {
//                    linkWifiInfo = "Wifi" + " (信号质量:" + event.getRssiLevel() +
//                            ") (丢包率:" + (!event.wifiConnected() ? "wifi未连接" : (event.isWifiAutoConnect() ?
//                            (event.getPingPacketLoss() == 101 ? "未测速" : event.getPingPacketLoss()) :
//                            (event.getManualWifiPacketLoss() == 101 ? "未测速" : event.getManualWifiPacketLoss()))) +
//                            ") (连接状态:" + event.wifiConnected() +
//                            ") (权重:" + 0 +
//                            ") (wifi连接方式:" + (event.wifiConnected() ? event.isWifiAutoConnect() ? "自动连接" : "手动连接" : "wifi未连接") +
//                            ") (测速次数:" + event.getPingCount() +
//                            ") (最近测速时间:" + (event.getPingTimeMillis() != 0 ? mDateFormat.format(event.getPingTimeMillis()) : "--")  + ")";
//                    mUpdateRouteInfoHandler.sendEmptyMessage(ROUTE_WIFI);
//                }
//                break;
//            case LINK_MNET:
//                linkMNetInfo = null;
//                McwillNetworkInfo mcwillNetworkInfo = event.getMcwillNetworkInfo();
//                linkMNetInfo = "M网" + " (信号质量:" + mcwillNetworkInfo.getSignalLevel() +
//                        ") (丢包率:" + (mcwillNetworkInfo.getMcwillState() == NetworkInfo.State.DISCONNECTED ? "M网数据被关闭" : (mcwillNetworkInfo.getPacketLoss() == 101 ? "未测速" : mcwillNetworkInfo.getPacketLoss())) +
//                        ") (数据开关状态:" + mcwillNetworkInfo.getMcwillState() +
//                        ") (权重:" + LinkConfig.getInstance().getMcwillCostWeight() +
//                        ") (惩罚时间:" + (mcwillNetworkInfo.getPenaltyTimeStamp() == 0 ? "-- " : mDateFormat.format(mcwillNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始，180秒后解除") +
//                        ") (测速次数:" + event.getPingCount() +
//                        ") (最近测速时间:" + (event.getLastPingTimeMillis() != 0L ?
//                        mDateFormat.format(event.getLastPingTimeMillis()) : "--") + ")";
//
//                mUpdateRouteInfoHandler.sendEmptyMessage(ROUTE_M_NET);
//                break;
//            case LINK_GNET:
//                linkGNetInfo = null;
//                SimNetworkInfo simNetworkInfo = event.getSimNetworkInfo();
//                linkGNetInfo = "G网" + " (信号质量:" + simNetworkInfo.getSignalLevel() +
//                        ") (丢包率:" + (simNetworkInfo.getSimNetState() == NetworkInfo.State.DISCONNECTED ? "G网数据被关闭" : (simNetworkInfo.getPacketLoss() == 101 ? "未测速" : simNetworkInfo.getPacketLoss())) +
//                        ") (数据开关状态:" + simNetworkInfo.getSimNetState() +
//                        ") (权重:" + LinkConfig.getInstance().getSimCostWeight() +
//                        ") (惩罚时间:" + (simNetworkInfo.getPenaltyTimeStamp() == 0 ? "-- " : mDateFormat.format(simNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始，180秒后解除") +
//                        ") (测速次数:" + event.getPingCount() +
//                        ") (最近测速时间:" + (event.getLastPingTimeMillis() != 0L ?
//                        mDateFormat.format(event.getLastPingTimeMillis()) : "--") + ")";
//
//                mUpdateRouteInfoHandler.sendEmptyMessage(ROUTE_G_NET);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(CurrentLinkEvent currentLinkEvent) {
//        FlyLog.d( "FloatWindowService onEvent currentLink refresh，current link = " + currentLinkEvent.getCurrentLink());
//        FloatWindowManager.setTexViewColor(currentLinkEvent.getCurrentLink());
//    }
//
//    @Subscribe
//    private void registerEventBus() {
//        boolean registered = HermesEventBus.getDefault().isRegistered(this);
//        if (!registered) {
//            HermesEventBus.getDefault().register(this);
//        }
//    }
//
//    @Subscribe
//    public void unregisterEventBus() {
//        FlyLog.d( "unregisterEventBus...");
//        if (HermesEventBus.getDefault().isRegistered(this)) {
//            HermesEventBus.getDefault().unregister(this);
//        }
//    }
//
//    @Override
//    public void run() {
//        boolean isShowFloatWindow = SPUtils.getInstance().getBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, false);
//        if (isShowFloatWindow && !FloatWindowManager.isWindowShowing()) {
//            mUpdateRouteInfoHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    registerEventBus();
//                    FloatWindowManager.createFloatWindow(getApplicationContext());
//                    HermesEventBus.getDefault().postSticky(new RefreshUiEvent(true));
//                    FlyLog.d( "createFloatWindow");
//                }
//            });
//        } else if (!isShowFloatWindow && FloatWindowManager.isWindowShowing()) {
//            mUpdateRouteInfoHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    unregisterEventBus();
//                    FloatWindowManager.removeFloatWindow(getApplicationContext());
//                    FlyLog.d( "removeFloatWindow");
//                }
//            });
//        }
//    }
//
//    /**
//     * @Author: __ Weiyi.Lee  liweiyi@cootf.com
//     * @Package: _ com.octopus.linkmanager.float_window
//     * @DESC: ____ 处理链路更新
//     * @Time: ____ created at-2018/10/10 10:51
//     */
//    private static class UpdateRouteInfoHandler extends Handler {
//        WeakReference<FloatWindowService> mFloatWindowServiceWeakRef;
//
//        private UpdateRouteInfoHandler(FloatWindowService floatWindowService) {
//            mFloatWindowServiceWeakRef = new WeakReference<>(floatWindowService);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            final FloatWindowService floatWindowService = mFloatWindowServiceWeakRef.get();
//            if (floatWindowService == null) {
//                return;
//            }
//
//            switch (msg.what) {
//                case ROUTE_M_NET:
//                    FloatWindowManager.updateFloatShow(ROUTE_M_NET, floatWindowService.linkMNetInfo);
//                    break;
//                case ROUTE_G_NET:
//                    FloatWindowManager.updateFloatShow(ROUTE_G_NET, floatWindowService.linkGNetInfo);
//                    break;
//                case ROUTE_WIFI:
//                    FloatWindowManager.updateFloatShow(ROUTE_WIFI, floatWindowService.linkWifiInfo);
//                    break;
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }
//}