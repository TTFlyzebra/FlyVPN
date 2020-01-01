package com.flyzebra.linkmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.flyzebra.utils.FlyLog;

/**
 * ClassName: MainReceiver
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-31 下午2:23
 */
public class MainReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FlyLog.d("on Receive intent: "+ intent.toUri(0));
    }
}
