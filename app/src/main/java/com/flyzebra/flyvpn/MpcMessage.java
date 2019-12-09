package com.flyzebra.flyvpn;

public class MpcMessage {
    public static final String heartBeat = "[{\"messageType\":23,\"sessionid\":%s}]";
    public static final String initMpc = "[{\"messageType\":21,\"uid\":2172748161,\"dns\":\"172.16.251.77\",\"mag\":\"103.5.126.153\",\"sessionid\":%s}]";
    public static final String enableMpc = "[{\"messageType\":17,\"netType\":%d,\"netTypeName\":\"%s\",\"sessionid\":%s}]";
    public static final String testLink = "[{\"messageType\":3,\"netType\":%d,\"netTypeName\":\"%s\",\"sessionid\":%s}]";
}
