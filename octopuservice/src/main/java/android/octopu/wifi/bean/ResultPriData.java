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
                resultPriData.retInfo = new RetInfoBean();
                if(subJsonObject.has("version")) {
                    resultPriData.retInfo.version = subJsonObject.getString("version");
                }
                if(subJsonObject.has("wifiList")) {
                    JSONArray subJsonArray = subJsonObject.getJSONArray("wifiList");
                    resultPriData.retInfo.wifiList = new ArrayList<>();
                    for (int i = 0; i < subJsonArray.length(); i++) {
                        JSONObject partDaily = subJsonArray.getJSONObject(i);
                        WifiDeviceBean wifiDeviceBean = new WifiDeviceBean();
                        if (partDaily.has("wifiDeviceId")) {
                            wifiDeviceBean.wifiDeviceId = partDaily.getString("wifiDeviceId");
                        }
                        if (partDaily.has("wifiPassword")) {
                            wifiDeviceBean.wifiPassword = partDaily.getString("wifiPassword");
                        }
                        if (partDaily.has("wifiAuthType")) {
                            wifiDeviceBean.wifiAuthType = partDaily.getString("wifiAuthType");
                        }
                        if (partDaily.has("wifiName")) {
                            wifiDeviceBean.wifiName = partDaily.getString("wifiName");
                        }
                        if (partDaily.has("wifiStatus")) {
                            wifiDeviceBean.wifiStatus = partDaily.getInt("wifiStatus");
                        }
                        if (partDaily.has("wifiCreateTime")) {
                            wifiDeviceBean.wifiCreateTime = partDaily.getString("wifiCreateTime");
                        }
                        if (partDaily.has("wifiUpdateTime")) {
                            wifiDeviceBean.wifiUpdateTime = partDaily.getString("wifiUpdateTime");
                        }
                        if (partDaily.has("userId")) {
                            wifiDeviceBean.userId = partDaily.getString("userId");
                        }
                        if (partDaily.has("longitude")) {
                            wifiDeviceBean.longitude = partDaily.getDouble("longitude");
                        }
                        if (partDaily.has("latitude")) {
                            wifiDeviceBean.latitude = partDaily.getDouble("latitude");
                        }
                        if (partDaily.has("remarks")) {
                            wifiDeviceBean.remarks = partDaily.getString("remarks");
                        }
                        resultPriData.retInfo.wifiList.add(wifiDeviceBean);
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
