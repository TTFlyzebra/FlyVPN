package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.model.IRatdRecvMessage;
import com.flyzebra.flyvpn.model.MpcController;
import com.flyzebra.flyvpn.task.DetectLinkTask;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.flyvpn.utils.SystemPropTools;
import com.flyzebra.utils.FlyLog;

import java.util.concurrent.atomic.AtomicBoolean;
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
    protected DetectLinkTask detectLinkTask;
    protected AtomicBoolean isReStart = new AtomicBoolean(false);

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
        heartBeatTask = new HeartBeatTask(getApplicationContext(), ratdSocketTask);
        detectLinkTask = new DetectLinkTask(getApplicationContext(), ratdSocketTask);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FlyLog.d("onDestroy");
        heartBeatTask.onDestory();
        detectLinkTask.onDestory();
        ratdSocketTask.unRegister(this);
        ratdSocketTask.onDestory();
        super.onDestroy();
    }

    @Override
    public void recvRatdMessage(MpcMessage message) {
        switch (message.messageType) {
            case 0x2: //增加子链路响应       2
                mpcStatus.getNetLink(message.netType).isLink = message.isResultOk();
                MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                break;
            case 0x4: //探测包响应          4
                if (message.isResultOk()) {
                    mpcController.addNetworkLink(this, message.netType);
                }
                break;
            case 0x6: //删除子链路响应       6
                mpcStatus.getNetLink(message.netType).isLink = false;
                MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                break;
            case 0x8: //释放MP链路响应       8
                //TODO:
                break;
            case 0x0a: //MP建立链路响应     10
                //TODO:
                break;
            case 0x12: //使能双流响应       18
                if (message.isResultOk()) {
                    detectLinkTask.start();
                }
                break;
            case 0x14: //关闭双流响应       20
                detectLinkTask.stop();
                if (isReStart.get()) {
                    mpcController.initMpc();
                } else {
                    heartBeatTask.start();
                }
                isReStart.set(false);
                break;
            case 0x16: //初始化配置响应     22
                if (message.isResultOk()) {
                    mpcStatus.init(this);
                    MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
                    heartBeatTask.start();
                    mpcController.enableMpcDefault(this);
                } else {
                    mpcController.initMpc();
                }
                break;
            case 0x18: //心跳响应          24
                //TODO:重启RATD
                break;
            case 0x1a: //异常状态上报       26
                if (message.exceptionCode == -2) {
                    FlyLog.e("exceptionCode=2, restart mpc");
                    isReStart.set(true);
                    detectLinkTask.stop();
                    mpcController.stopMpc();
                } else if (message.exceptionCode == -3) {
                    isReStart.set(false);
                    detectLinkTask.stop();
                    mpcController.initMpc();
                }
                break;
            case 0x1b: //流量信息上报       27
                break;
            case 0x64:
                switchMPC();
                break;
        }
    }

    public void switchMPC() {
        String switch_status = SystemPropTools.get("persist.sys.net.support.multi", "true");
        mpcStatus.init(this);
        heartBeatTask.stop();
        detectLinkTask.stop();
        if ("true".equals(switch_status)) {
            mpcController.initMpc();
        } else {
            mpcController.stopMpc();
        }
        MyTools.upLinkManager(this, mpcStatus.wifiLink.isLink, mpcStatus.mobileLink.isLink, mpcStatus.mcwillLink.isLink);
    }


}