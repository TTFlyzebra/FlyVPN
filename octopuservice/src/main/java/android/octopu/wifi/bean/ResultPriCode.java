package android.octopu.wifi.bean;

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
}
