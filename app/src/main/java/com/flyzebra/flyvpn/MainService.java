package com.flyzebra.flyvpn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.flyzebra.flyvpn.data.MpcMessage;
import com.flyzebra.flyvpn.data.MpcStatus;
import com.flyzebra.flyvpn.task.DetectLinkTask;
import com.flyzebra.flyvpn.task.HeartBeatTask;
import com.flyzebra.flyvpn.task.OnRecvMessage;
import com.flyzebra.flyvpn.task.RatdSocketTask;
import com.flyzebra.flyvpn.utils.MyTools;
import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MainService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:21
 */
public class MainService extends Service implements OnRecvMessage {
    private RatdSocketTask ratdSocketTask;
    private HeartBeatTask heartBeatTask;
    private DetectLinkTask detectLinkTask;
    private MpcStatus mpcStatus;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MpcStatus.getInstance().init();
        ratdSocketTask = new RatdSocketTask();
        ratdSocketTask.register(this);
        ratdSocketTask.sendMessage(String.format(MpcMessage.initMpc, MyTools.createSessionId()));
        heartBeatTask = new HeartBeatTask(ratdSocketTask);
        detectLinkTask = new DetectLinkTask(getApplicationContext(),ratdSocketTask);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FlyLog.d("onDestroy");
        ratdSocketTask.unRegister(this);
        heartBeatTask.cancel();
        detectLinkTask.cancel();
        super.onDestroy();
    }

    @Override
    public void recv(MpcMessage message) {
        switch (message.messageType){
            case 0x2: //增加子链路响应       2
            case 0x4: //探测包响应          4
            case 0x6: //删除子链路响应       6
            case 0x8: //释放MP链路响应       8
            case 0x0a: //MP建立链路响应     10
            case 0x12: //使能双流响应       18
            case 0x14: //关闭双流响应       20
                break;
            case 0x16: //初始化配置响应     22
                if(message.result==0) {
                    heartBeatTask.start();
                    detectLinkTask.start();
                }
                break;
            case 0x18: //心跳响应          24
            case 0x1a: //异常状态上报       26
            case 0x1b: //流量信息上报       27
                break;
        }
    }
}