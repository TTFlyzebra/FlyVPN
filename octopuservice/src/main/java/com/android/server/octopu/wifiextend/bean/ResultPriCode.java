package com.android.server.octopu.wifiextend.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName: ResultPriCode
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午3:34
 */
public class ResultPriCode {

    /**
     * retCode : 0
     * retMsg :
     * retInfo : {"version":"20180228152520"}
     */

    public int retCode;
    public String retMsg;
    public RetInfoBean retInfo;

    public static class RetInfoBean {
        /**
         * version : 20180228152520
         */

        public String version;
    }

    public static ResultPriCode createByJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        ResultPriCode resultPriCode = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            resultPriCode = new ResultPriCode();
            if(jsonObject.has("retCode")){
                resultPriCode.retCode =jsonObject.getInt("retCode");
            }
            if(jsonObject.has("retMsg")){
                resultPriCode.retMsg = jsonObject.getString("retMsg");
            }
            if(jsonObject.has("retInfo")) {
                JSONObject subJsonObject = jsonObject.getJSONObject("retInfo");
                resultPriCode.retInfo = new ResultPriCode.RetInfoBean();
                if(subJsonObject.has("version")) {
                    resultPriCode.retInfo.version = subJsonObject.getString("version");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultPriCode;
    }
}
