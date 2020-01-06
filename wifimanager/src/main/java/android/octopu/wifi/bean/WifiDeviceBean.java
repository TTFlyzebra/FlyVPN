package android.octopu.wifi.bean;

public class WifiDeviceBean {

    /**
     * wifiDeviceId : 00:0a:f5:02:d0:64
     * wifiPassword : R6m5qy6STQ84QE6yIMzRkA==
     * wifiAuthType : 1
     * wifiName : C10
     * wifiStatus : 0
     * wifiCreateTime : 2019-05-24 16:21:24
     * wifiUpdateTime : 2019-05-24 16:21:24
     * userId : 0
     * longitude : 113.95081
     * latitude : 22.543849
     * remarks : thisisaxinweiWIFI
     */

    public int id;
    public String wifiDeviceId;
    public String wifiPassword;
    public String wifiAuthType;
    public String wifiName;
    public int wifiStatus;
    public long wifiCreateTime;
    public long wifiUpdateTime;
    public String userId;
    public double longitude;
    public double latitude;
    public String remarks;

    @Override
    public String toString() {
        return "WifiDeviceBean{" +
                "wifiDeviceId='" + wifiDeviceId + '\'' +
                ", wifiPassword='" + wifiPassword + '\'' +
                ", wifiAuthType='" + wifiAuthType + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", wifiStatus=" + wifiStatus +
                ", wifiCreateTime='" + wifiCreateTime + '\'' +
                ", wifiUpdateTime='" + wifiUpdateTime + '\'' +
                ", userId='" + userId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
