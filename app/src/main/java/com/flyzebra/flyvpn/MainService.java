package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.model.MpcController;
import com.flyzebra.flyvpn.model.OnRecvMessage;
import com.flyzebra.flyvpn.task.DetectLinkTask;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class MainService extends Service implements OnRecvMessage {
    private MpcController mpcController = MpcController.getInstance();
    private MpcStatus mpcStatus = MpcStatus.getInstance();
    private RatdSocketTask ratdSocketTask;
    private HeartBeatTask heartBeatTask;
    private DetectLinkTask detectLinkTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ratdSocketTask = new RatdSocketTask(getApplicationContext());
        ratdSocketTask.start();
        ratdSocketTask.register(this);

        heartBeatTask = new HeartBeatTask(getApplicationContext(),ratdSocketTask);

        detectLinkTask = new DetectLinkTask(getApplicationContext(), ratdSocketTask);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        upLinkManager();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FlyLog.d("onDestroy");
        ratdSocketTask.unRegister(this);
        ratdSocketTask.stop();
        heartBeatTask.stop();
        detectLinkTask.stop();
        super.onDestroy();
    }

    @Override
    public void recv(MpcMessage message) {
        switch (message.messageType) {
            case 0x2: //增加子链路响应       2
                mpcStatus.getNetLink(message.netType).isLink = message.isResultOk();
                upLinkManager();
                break;
            case 0x4: //探测包响应          4
                if (message.isResultOk()) {
                    mpcController.addNetworkLink(this,message.netType);
                }
                break;
            case 0x6: //删除子链路响应       6
                mpcStatus.getNetLink(message.netType).isLink = false;
                break;
            case 0x8: //释放MP链路响应       8
            case 0x0a: //MP建立链路响应     10
            case 0x12: //使能双流响应       18
                if (message.isResultOk()) {
                    detectLinkTask.start();
                }
                break;
            case 0x14: //关闭双流响应       20
                heartBeatTask.stop();
                detectLinkTask.stop();
                break;
            case 0x16: //初始化配置响应     22
                if (message.isResultOk()) {
                    heartBeatTask.start();
                    mpcController.enableMpcDefault(this);
                } else {
                    mpcController.initMpc();
                }
                break;
            case 0x18: //心跳响应          24
                break;
            case 0x1a: //异常状态上报       26
            case 0x1b: //流量信息上报       27
                break;
            case 0x63:
                mpcController.initMpc();
                break;
        }
    }

    private void upLinkManager() {
        Intent intent = new Intent("intent.action.UPDATE_MP_STATUS_FOR_LINK_MANAGER");
        intent.putExtra("NETWORK_LINK_WIFI", mpcStatus.wifiLink.isLink ? 1 : 0);
        intent.putExtra("NETWORK_LINK_4G", mpcStatus.mobileLink.isLink ? 1 : 0);
        intent.putExtra("NETWORK_LINK_MCWILL", mpcStatus.mcwillLink.isLink ? 1 : 0);
        sendBroadcast(intent);
    }
}