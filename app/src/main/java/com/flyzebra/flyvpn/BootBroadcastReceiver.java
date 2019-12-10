package com.flyzebra.flyvpn;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by JD on 2018/1/4.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "BootBroadcast";
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "开机自动>>>>>>>>>>>");
        //开机自启动
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mainintent = new Intent();
            mainintent.setClass(context, MainService.class);
            mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(mainintent);
        }
        //接收广播：安装更新后，自动启动自己。
        else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)
                || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            Intent mainintent = new Intent();
            mainintent.setClass(context, TestActivity.class);
            mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivity(mainintent);
        }
    }
}

