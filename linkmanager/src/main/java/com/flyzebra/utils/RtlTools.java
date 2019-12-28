package com.flyzebra.utils;

import android.text.TextUtils;
import android.view.View;

import java.util.Locale;

/**
 * ClassName: RtlTools
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-28 下午1:57
 */
public class RtlTools {

    public static boolean isRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isLayoutRtl(View view) {
        return View.LAYOUT_DIRECTION_RTL == view.getLayoutDirection();
    }
}
