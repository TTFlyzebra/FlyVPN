package com.android.server.octopu.wifiextend.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName: ResultPubCode
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午3:35
 */
public class ResultPubCode {

    /**
     * retCode : 0
     * retMsg :
     * retInfo : {}
     */

    public int retCode;
    public String retMsg;
    public RetInfoBean retInfo;

    public static class RetInfoBean {
    }

    public static ResultPubCode createByJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        ResultPubCode resultPubCode = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            resultPubCode = new ResultPubCode();
            if(jsonObject.has("retCode")){
                resultPubCode.retCode =jsonObject.getInt("retCode");
            }
            if(jsonObject.has("retMsg")){
                resultPubCode.retMsg = jsonObject.getString("retMsg");
            }
            if(jsonObject.has("retInfo")) {
                JSONObject subJsonObject = jsonObject.getJSONObject("retInfo");
                resultPubCode.retInfo = new ResultPubCode.RetInfoBean();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultPubCode;
    }
}
