package com.flyzebra.utils;

/**
 * ClassName: NetworkTools
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-31 下午2:37
 */
public class NetworkTools {

    /**
     * 验证是不是IP地址
     * @param str
     * @return
     */
    public static boolean ipCheck(String str) {
        if (str != null && !str.isEmpty()) {
            String pattern = "^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
            if (str.matches(pattern))
                return true;
            return false;
        }
        return false;
    }
}
