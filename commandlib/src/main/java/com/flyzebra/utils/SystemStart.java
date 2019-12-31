package com.flyzebra.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.os.UserHandle;

import java.lang.reflect.Field;

/**
 * ClassName: SystemStart
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-28 上午11:33
 */
public class SystemStart {

    private static UserHandle userHandle = null;

    static {
        try {
            Class clazz = Class.forName("android.os.UserHandle");
            Field field = clazz.getField("ALL");
            userHandle = (UserHandle) field.get(clazz);//treeToolbar类
        } catch (Exception e) {
            userHandle = null;
            e.printStackTrace();
        }

    }

    public static void sendBroadcast(Context context,Intent intent) {
        if (userHandle != null && Process.myUid() == Process.SYSTEM_UID) {
            context.sendBroadcastAsUser(intent, userHandle);
        } else {
            context.sendBroadcast(intent);
        }
    }

}
