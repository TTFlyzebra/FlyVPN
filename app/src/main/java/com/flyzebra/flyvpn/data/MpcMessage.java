package com.flyzebra.flyvpn.data;

/**
 * ClassName: MpcMessage
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:20
 */
public class MpcMessage {
    public static final String heartBeat = "[{\"messageType\":23,\"sessionid\":%s}]";
    public static final String initMpc = "[{\"messageType\":21,\"uid\":2172748161,\"dns\":\"172.16.251.77\",\"mag\":\"103.5.126.153\",\"sessionid\":%s}]";
    public static final String enableMpc = "[{\"messageType\":17,\"netType\":%d,\"netTypeName\":\"%s\",\"sessionid\":%s}]";
    public static final String testLink = "[{\"messageType\":3,\"netType\":%d,\"netTypeName\":\"%s\",\"sessionid\":%s}]";
    public static final String addLink = "[{\"messageType\":1,\"netType\":%d,\"netTypeName\":\"%s\",\"ip\":\"%s\",\"token\":0,\"band\":0,\"rtt\":0\",\"sessionid\":%s}]";

    public int messageType;
    public int sessionid;
    public long uid;
    public String dns;
    public String mag;
    public String netType;
    public String netTypeName;
    public String ip;
    public String token;
    public String band;
    public String rrt;

}

