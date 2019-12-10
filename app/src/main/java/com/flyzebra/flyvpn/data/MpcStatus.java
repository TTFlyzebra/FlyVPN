package com.flyzebra.flyvpn.data;

/**
 * ClassName: MpcStatus
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:18
 */
public class MpcStatus {
    /**
     * 初始化
     */
    public Boolean mpcInit = false;
    /**
     * 开启双流
     */
    public Boolean mpcEnable = false;
    /**
     * mcwill链路
     */
    public NetworkLink mcwillLink = new NetworkLink();
    /**
     * 4G链路
     */
    public NetworkLink mobileLink = new NetworkLink();
    /**
     * Wifi链路
     */
    public NetworkLink wifiLink = new NetworkLink();


    private MpcStatus() {
    }

    public static MpcStatus getInstance() {
        return MpcStatusHolder.sInstance;
    }

    private static class MpcStatusHolder {
        public static final MpcStatus sInstance = new MpcStatus();
    }

    public void reset(){
        mpcInit = false;
        mpcEnable = false;
        mcwillLink.reset();
        mobileLink.reset();
        wifiLink.reset();
    }

}
