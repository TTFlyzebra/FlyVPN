package android.octopu.wifi.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultPriData {


    /**
     * retCode :
     * retMsg :
     * retInfo : {"version":"20180228152125","wifiList":[{"wifi_deviceId":"f0:9f:c2:dd:7a:58","wifi_password":"PKmMTUL8Mmhu7UvR9hpBxA==","wifi_authType":"1","wifi_status":0,"wifi_createDate":"2017-10-28 08:37:13","wifi_updateDate":"2017-10-28 08:37:13","user_id":"0","user_name":"","longitude":null,"latitude":null,"remarks":""},{"wifi_deviceId":"0:18:12:7:c7:15","wifi_password":"ESAGNHJKlpMkLyT02110A==","wifi_authType":"1","wifi_status":0,"wifi_createDate":"2017-10-28 08:37:13","wifi_updateDate":"2017-10-28 08:37:13","user_id":"0","user_name":"","longitude":null,"latitude":null,"remarks":""}]}
     */

    public int retCode;
    public String retMsg;
    public RetInfoBean retInfo;

    public static ResultPriData createByJson(String json){
        ResultPriData resultPriData = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            resultPriData = new ResultPriData();
            if(jsonObject.has("retCode")){
                resultPriData.retCode =jsonObject.getInt("retCode");
            }
            if(jsonObject.has("retMsg")){
                resultPriData.retMsg = jsonObject.getString("retMsg");
            }
            if(jsonObject.has("retInfo")) {
                JSONObject subJsonObject = jsonObject.getJSONObject("retInfo");
                RetInfoBean retInfoBean = new RetInfoBean();
                if(jsonObject.has("version")) {
                    retInfoBean.version = subJsonObject.getString("version");
                }
                if(jsonObject.has("wifiList")) {
                    JSONArray subJsonArray = subJsonObject.getJSONArray("wifiList");
                    retInfoBean.wifiList = new ArrayList<>();
                    for (int i = 0; i < subJsonArray.length(); i++) {
                        JSONObject partDaily = subJsonArray.getJSONObject(i);
                        WifiDeviceBean wifiDeviceBean = new WifiDeviceBean();
                        if (jsonObject.has("wifiDeviceId")) {
                            wifiDeviceBean.wifiDeviceId = partDaily.getString("wifiDeviceId");
                        }
                        if (jsonObject.has("wifiPassword")) {
                            wifiDeviceBean.wifiPassword = partDaily.getString("wifiPassword");
                        }
                        if (jsonObject.has("wifiAuthType")) {
                            wifiDeviceBean.wifiAuthType = partDaily.getString("wifiAuthType");
                        }
                        if (jsonObject.has("wifiName")) {
                            wifiDeviceBean.wifiName = partDaily.getString("wifiName");
                        }
                        if (jsonObject.has("wifiStatus")) {
                            wifiDeviceBean.wifiStatus = partDaily.getInt("wifiStatus");
                        }
                        if (jsonObject.has("wifiCreateTime")) {
                            wifiDeviceBean.wifiCreateTime = partDaily.getString("wifiCreateTime");
                        }
                        if (jsonObject.has("wifiUpdateTime")) {
                            wifiDeviceBean.wifiUpdateTime = partDaily.getString("wifiUpdateTime");
                        }
                        if (jsonObject.has("userId")) {
                            wifiDeviceBean.userId = partDaily.getString("userId");
                        }
                        if (jsonObject.has("longitude")) {
                            wifiDeviceBean.longitude = partDaily.getDouble("longitude");
                        }
                        if (jsonObject.has("latitude")) {
                            wifiDeviceBean.latitude = partDaily.getDouble("latitude");
                        }
                        if (jsonObject.has("remarks")) {
                            wifiDeviceBean.remarks = partDaily.getString("remarks");
                        }
                        retInfoBean.wifiList.add(wifiDeviceBean);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultPriData;
    }

    @Override
    public String toString() {
        return "ResultPriData{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", retInfo=" + retInfo +
                '}';
    }

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
}
