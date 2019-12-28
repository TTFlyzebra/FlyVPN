package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.model.IRatdRecvMessage;
import com.flyzebra.flyvpn.model.MpcController;
import com.flyzebra.flyvpn.task.DetectLinkTask;
import com.flyzebra.flyvpn.task.EnableMpcTask;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.utils.FlyLog;
import com.flyzebra.utils.SystemPropTools;

import xinwei.com.mpapp.Constant;

/**
 * ClassName: BaseMainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class BaseMainService extends Service implements IRatdRecvMessage {
    protected MpcController mpcController = MpcController.getInstance();
    protected MpcStatus mpcStatus = MpcStatus.getInstance();
    protected RatdSocketTask ratdSocketTask;
    protected HeartBeatTask heartBeatTask;
    protected EnableMpcTask enableMpcTask;
    protected DetectLinkTask detectLinkTask;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected long DELAY_TIME = 5000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FlyLog.e("+++++++++++++++++++++++++++++++++++");
        FlyLog.e("+++++version 1.02---2019.12.26+++++");
        FlyLog.e("+++++++++++++++++++++++++++++++++++");
        FlyLog.d("+++++onCreate, mpapp is start!+++++");
        ratdSocketTask = new RatdSocketTask(getApplicationContext());
        ratdSocketTask.start();
        ratdSocketTask.register(this);
        heartBeatTask = new HeartBeatTask(getApplicationContext(), ratdSocketTask);
        enableMpcTask = new EnableMpcTask(getApplicationContext(), ratdSocketTask);
        detectLinkTask = new DetectLinkTask(getApplicationContext(), ratdSocketTask);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FlyLog.d("+++++onDestroy, mpApp is Stop!+++++");
        heartBeatTask.onDestory();
        enableMpcTask.onDestory();
        detectLinkTask.onDestory();
        ratdSocketTask.unRegister(this);
        ratdSocketTask.onDestory();
        super.onDestroy();
    }

    @Override
    public void recvRatdMessage(MpcMessage message) {
        switch (message.messageType) {
            case 0x2: //增加子链路响应       2
                switch (message.result) {
                    case 0:
                        mpcStatus.getNetLink(message.netType).isLink = message.isResultOk();
                        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                        break;
                    case Constant.ADD_LINK_RESULT_CODE_USER_NOT_EXIST:
                        if (mpcStatus.mpcEnable) {
                            delayTryOpenOrCloseMpc(DELAY_TIME);
                        } else {
                            enableMpcTask.addNetworkTask(message.netType);
                        }
                        break;
                    case Constant.ADD_LINK_RESULT_CODE_MP_NOT_START:
                    case Constant.ADD_LINK_RESULT_CODE_DHCP_FAIL:
                    case Constant.ADD_LINK_RESULT_CODE_NETTY_ERROR:
                        delayTryOpenOrCloseMpc(DELAY_TIME);
                        break;
                    case Constant.ADD_LINK_RESULT_CODE_NORMAL_ERROR:
                    case Constant.ADD_LINK_RESULT_CODE_PARAM_ERROR:
                        break;
                }
                break;
            case 0x4: //探测包响应          4
                switch (message.result) {
                    case 0:
                        if (mpcStatus.mpcEnable) {
                            mpcController.addNetworkLink(this, message.netType);
                        } else {
                            enableMpcTask.addNetworkTask(message.netType);
                        }
                        break;
                    case Constant.DETECT_LINK_RESULT_CODE_USER_NOT_EXIST:
                    case Constant.DETECT_LINK_RESULT_CODE_MP_NOT_START:
                    case Constant.DETECT_LINK_RESULT_CODE_DSN_EXCEPTION:
                        if (mpcStatus.mpcEnable) {
                            delayTryOpenOrCloseMpc(DELAY_TIME);
                        } else {
                            enableMpcTask.addNetworkTask(message.netType);
                        }
                        break;
                    case Constant.DETECT_LINK_RESULT_CODE_DHCP_FAIL:
                    case Constant.DETECT_LINK_RESULT_CODE_NETTY_ERROR:
                        if (!mpcStatus.mpcEnable) {
                            enableMpcTask.addNetworkTask(message.netType);
                        }
                        break;
                    case Constant.DETECT_LINK_RESULT_CODE_NORMAL_ERROR:
                    case Constant.DETECT_LINK_RESULT_CODE_PARAM_ERROR:
                    case Constant.DETECT_LINK_RESULT_CODE_TIME_OUT:
                        if (mpcStatus.mpcEnable) {
                            mpcController.delNetworkLink(message.netType, Constant.DELETE_LINK_CAUSE_DETECT_TIMEOUT);
                        } else {
                            enableMpcTask.addNetworkTask(message.netType);
                        }
                        break;
                }
                break;
            case 0x6: //删除子链路响应       6
                switch (message.result) {
                    case 0:
                    default:
                        mpcStatus.getNetLink(message.netType).isLink = false;
                        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                        break;
                }
                break;
            case 0x8: //释放MP链路响应       8
                //TODO:需要确认需求和测试
                break;
            case 0x0a: //MP建立链路响应     10
                //TODO:需要确认需求和测试
                break;
            case 0x12: //使能双流响应       18
                switch (message.result) {
                    case 0:
                        mpcStatus.mpcEnable = true;
                        break;
                }
                break;
            case 0x14: //关闭双流响应       20
                switch (message.result) {
                    case 0:
                        enableMpcTask.stop();
                        heartBeatTask.stop();
                        detectLinkTask.stop();
                        mpcStatus.disbleAllLink();
                        mpcStatus.mpcEnable = false;
                        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                        break;
                }
                break;
            case 0x16: //初始化配置响应     22
                switch (message.result) {
                    case 0:
                        break;
                    default:
                        delayTryOpenOrCloseMpc(1000);
                        break;
                }
                break;
            case 0x18: //心跳响应          24
                //TODO:重启RATD
                break;
            case 0x1a: //异常状态上报       26
                //TODO:-2删除链路还是复位
                switch (message.exceptionCode) {
                    case Constant.EXCEPTION_CODE_1:
                    case Constant.EXCEPTION_CODE_2:
                        FlyLog.e("exceptionCode=%d, delete link netType=%d", message.exceptionCode, message.netType);
                        mpcController.delNetworkLink(message.netType, Constant.DELETE_LINK_CAUSE_DEVICE_EXCEPTION);
                        mpcStatus.getNetLink(message.netType).isLink = false;
                        break;
                    case Constant.EXCEPTION_CODE_3:
                        String switch_status = SystemPropTools.get("persist.sys.net.support.multi", "true");
                        if ("true".equals(switch_status)) {
                            tryOpenOrCloseMpc();
                        }
                        break;
                    case Constant.EXCEPTION_CODE_4:
                        enableMpcTask.stop();
                        heartBeatTask.stop();
                        detectLinkTask.stop();
                        mpcStatus.disbleAllLink();
                        mpcStatus.mpcEnable = false;
                        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                        break;
                }
                break;
            case 0x1b: //流量信息上报       27
                break;
            case 0x63:
                //跟RATD失去联系,RatdSocketTask自动发起重新连接操作，初始化所有状态，关闭探测
                FlyLog.d("ratd socket disconnet, reset all netwrok!");
                mpcStatus.disbleAllLink();
                MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                break;
            case 0x64:
                //跟RATD建立通信成功
                tryOpenOrCloseMpc();
                break;
        }
    }

    public void tryOpenOrCloseMpc() {
        String switch_status = SystemPropTools.get("persist.sys.net.support.multi", "true");
        enableMpcTask.stop();
        heartBeatTask.stop();
        detectLinkTask.stop();
        mpcStatus.disbleAllLink();
        mpcStatus.mpcEnable = false;
        if ("true".equals(switch_status)) {
            FlyLog.e("mpc switch is open,mpapp start run...");
            heartBeatTask.start();
            detectLinkTask.start();
            enableMpcTask.start();
            mpcController.initMpc();
        } else {
            FlyLog.e("mpc switch is close,mpapp not running...");
            mpcController.stopMpc();
        }
        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
    }

    private Runnable delayOpenTask = new Runnable() {
        @Override
        public void run() {
            tryOpenOrCloseMpc();
        }
    };

    public void delayTryOpenOrCloseMpc(long millis) {
        enableMpcTask.stop();
        heartBeatTask.stop();
        detectLinkTask.stop();
        mpcStatus.disbleAllLink();
        mpcStatus.mpcEnable = false;
        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
        mHandler.removeCallbacks(delayOpenTask);
        mHandler.postDelayed(delayOpenTask, millis);
    }


}