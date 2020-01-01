package com.flyzebra.linkmanager;

import android.app.Application;

import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MyApp
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-31 下午3:06
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlyLog.setTAG("LinkManager");
    }
}
