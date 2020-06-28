package android.octopu;

import android.os.Parcel;
import android.os.Parcelable;

public class WifiDeviceBean implements Parcelable {

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
    public String wifiCreateTime;
    public String wifiUpdateTime;
    public String userId;
    public double longitude;
    public double latitude;
    public String remarks;
    public int pswdStatu; //-1,passwore error;0,normal;


    public WifiDeviceBean() {
    }

    protected WifiDeviceBean(Parcel in) {
        id = in.readInt();
        wifiDeviceId = in.readString();
        wifiPassword = in.readString();
        wifiAuthType = in.readString();
        wifiName = in.readString();
        wifiStatus = in.readInt();
        wifiCreateTime = in.readString();
        wifiUpdateTime = in.readString();
        userId = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        remarks = in.readString();
        pswdStatu = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(wifiDeviceId);
        dest.writeString(wifiPassword);
        dest.writeString(wifiAuthType);
        dest.writeString(wifiName);
        dest.writeInt(wifiStatus);
        dest.writeString(wifiCreateTime);
        dest.writeString(wifiUpdateTime);
        dest.writeString(userId);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(remarks);
        dest.writeInt(pswdStatu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WifiDeviceBean> CREATOR = new Creator<WifiDeviceBean>() {
        @Override
        public WifiDeviceBean createFromParcel(Parcel in) {
            return new WifiDeviceBean(in);
        }

        @Override
        public WifiDeviceBean[] newArray(int size) {
            return new WifiDeviceBean[size];
        }
    };

    @Override
    public String toString() {
        return "WifiDeviceBean{" +
                ", wifiDeviceId='" + wifiDeviceId + '\'' +
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
                ", pswdStatu=" + pswdStatu +
                '}';
    }
}
