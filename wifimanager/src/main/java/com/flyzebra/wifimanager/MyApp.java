package com.flyzebra.wifimanager;

import android.app.Application;

import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MyApp
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-7 下午12:05
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlyLog.setTAG("WIFILOG");
    }
}
