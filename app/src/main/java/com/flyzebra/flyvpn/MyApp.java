package com.flyzebra.flyvpn;

import android.app.Application;
import android.content.Intent;

import xinwei.com.mpapp.MainService;

/**
 * ClassName: MyApp
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-24 上午9:13
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //启动服务
        Intent mainintent = new Intent();
        mainintent.setClass(this, MainService.class);
        mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(mainintent);
    }
}
