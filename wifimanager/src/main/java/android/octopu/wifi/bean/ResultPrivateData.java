package android.octopu.wifi.bean;

import java.util.List;

public class ResultPrivateData {


    /**
     * retCode :
     * retMsg :
     * retInfo : {"version":"20180228152125","wifiList":[{"wifi_deviceId":"f0:9f:c2:dd:7a:58","wifi_password":"PKmMTUL8Mmhu7UvR9hpBxA==","wifi_authType":"1","wifi_status":0,"wifi_createDate":"2017-10-28 08:37:13","wifi_updateDate":"2017-10-28 08:37:13","user_id":"0","user_name":"","longitude":null,"latitude":null,"remarks":""},{"wifi_deviceId":"0:18:12:7:c7:15","wifi_password":"ESAGNHJKlpMkLyT02110A==","wifi_authType":"1","wifi_status":0,"wifi_createDate":"2017-10-28 08:37:13","wifi_updateDate":"2017-10-28 08:37:13","user_id":"0","user_name":"","longitude":null,"latitude":null,"remarks":""}]}
     */

    public String retCode;
    public String retMsg;
    public RetInfoBean retInfo;

    public static class RetInfoBean {
        public String version;
        public List<WifiDeviceBean> wifiList;

        @Override
        public String toString() {
            return "RetInfoBean{" +
                    "version='" + version + '\'' +
                    ", wifiList=" + wifiList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResultPrivateData{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", retInfo=" + retInfo +
                '}';
    }
}
