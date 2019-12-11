package xinwei.com.mpapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.flyzebra.flyvpn.BaseMainService;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.utils.FlyLog;

import xinwei.com.mpapp.aidl.IServiceAidl;


/**
 * Created by JD on 2018/1/4.
 */

public class MainService extends BaseMainService {
    private static final String TAG = "BaseMainService";
    //对外接口
    private MyServiceImpl myService = null;
    //是否初始化双流配置
    //是否开启多流

    //是否开启多流
    private MainReceiver mainReceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        FlyLog.d("onCreate");
        myService = new MyServiceImpl();
        mainReceiver = new MainReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constant.ACTION_MAIN_SERVICE_UPDATE_NETWORK);
        mIntentFilter.addAction(Constant.ACTION_MAIN_SERVICE_RESP_UI_OPT);
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mIntentFilter.addAction(Constant.ACTION_MCWILL_DATA_STATE_CHANGE);
        mIntentFilter.addAction(Constant.ACTION_NOTIFICATION_NET_STANDARD);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mainReceiver, mIntentFilter);
    }

    /**
     * 广播接收器
     */
    private class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Constant.ACTION_MAIN_SERVICE_UPDATE_NETWORK)) {
                int netStatus = intent.getIntExtra(Constant.KEY_NET_STATUS, 0);
                int netType = intent.getIntExtra(Constant.KEY_NET_TYPE, 0);

            } else if (action != null && action.equals(Constant.ACTION_MAIN_SERVICE_RESP_UI_OPT)) {
                int optcode = intent.getIntExtra(Constant.KEY_OPT_CODE, 0);
                int serviceType = intent.getIntExtra(Constant.KEY_SERVICE_TYPE, 0);
                switch (serviceType) {
                    case -1:
                        //重新初始化
                        switchMPC();
                        break;
                    case 1:
                    case 2:
                    case 4:
                        break;
                    case 6:
                        switchMPC();
                        break;
                    case 7:
                        //日志开关
                        mpcController.switchMpcLog(optcode);
                        break;
                }
            } else if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            } else if (action != null && action.equals(Constant.ACTION_MCWILL_DATA_STATE_CHANGE)) {
            } else if (action != null && action.equals(Constant.ACTION_NOTIFICATION_NET_STANDARD)) {
            } else if (action != null && action.equals(Intent.ACTION_SCREEN_ON)) {
            } else if (action != null && action.equals(Intent.ACTION_SCREEN_OFF)) {
            }
        }
    }


    private class MyServiceImpl extends IServiceAidl.Stub {
        /**
         * 打开多流开关
         *
         * @param magip
         * @param magport
         * @param dns
         * @param phone
         * @param pwd
         * @param token
         */
        @Override
        public boolean openMultipleStreams(java.lang.String magip, int magport, java.lang.String dns, int uid, java.lang.String phone, java.lang.String pwd, java.lang.String token) throws android.os.RemoteException {
            mpcStatus.init(MainService.this);
            heartBeatTask.stop();
            detectLinkTask.stop();
            mpcController.stopMpc();
            MyTools.upLinkManager(MainService.this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
            return true;
        }

        /**
         * 关闭多流开关
         */
        @Override
        public boolean closeMultipleStreams() throws android.os.RemoteException {
            mpcStatus.init(MainService.this);
            heartBeatTask.stop();
            detectLinkTask.stop();
            mpcController.stopMpc();
            MyTools.upLinkManager(MainService.this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
            return true;
        }

        /**
         * 设置网络状态
         *
         * @param network
         */
        @Override
        public void updateNetwork(java.util.List<xinwei.com.mpapp.aidl.Network> network) throws android.os.RemoteException {
        }
    }


    public IBinder onBind(Intent intent) {
        return myService;
    }

    @Override
    public void onDestroy() {
        FlyLog.d("onDestroy");
        super.onDestroy();
    }
}
