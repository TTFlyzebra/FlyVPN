package com.flyzebra.linkmanager.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.flyzebra.linkmanager.R;
import com.flyzebra.utils.FlyLog;


/**
 * @Author: __ Weiyi.Lee  liweiyi@cootf.com
 * @Package: _ com.octopus.linkmanager.float_window
 * @DESC: ____ 悬浮窗管理类，添加/删除
 * @Time: ____ created at-2018-09-19 16:27
 */
public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";
    private static Context mContext;
    /**
     * 悬浮窗View的实例
     */
    private static FloatWindow mFloatWindow;
    /**
     * 悬浮窗View的参数
     */
    private static WindowManager.LayoutParams mWindowLayoutParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    /**
     * 变换文字的目标link
     */
    private static TextView mTvTargetLink;
    private static TextView mTvLinkWifi;
    private static TextView mTvLinkMNet;
    private static TextView mTvLinkGNet;
    private final static int ROUTE_M_NET = 0;
    private final static int ROUTE_G_NET = 1;
    private final static int ROUTE_WIFI = 2;

    /**
     * 创建一个悬浮窗
     * 初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatWindow(Context context) {
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        if (mFloatWindow == null) {
            mFloatWindow = new FloatWindow(context);
            if (mWindowLayoutParams == null) {
                mWindowLayoutParams = new WindowManager.LayoutParams();
                mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mWindowLayoutParams.format = PixelFormat.RGBA_8888;
                mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mWindowLayoutParams.width = FloatWindow.viewWidth;
                mWindowLayoutParams.height = FloatWindow.viewHeight;
                mWindowLayoutParams.x = screenWidth;
                mWindowLayoutParams.y = screenHeight / 2;
            }
            mFloatWindow.setParams(mWindowLayoutParams);
            //最为关键的一步操作，该操作之后就可以在桌面上看到视图
            windowManager.addView(mFloatWindow, mWindowLayoutParams);

            mTvLinkWifi = (TextView) mFloatWindow.findViewById(R.id.tv_link_one);
            mTvLinkMNet = (TextView) mFloatWindow.findViewById(R.id.tv_link_two);
            mTvLinkGNet = (TextView) mFloatWindow.findViewById(R.id.tv_link_three);
        }
    }

    /**
     * 将悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeFloatWindow(Context context) {
        if (mFloatWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mFloatWindow);
            mFloatWindow = null;
        }
    }

    /**
     * 更新悬浮窗文字显示
     *
     * @param whichLink
     * @param content
     */
    public static void updateFloatShow(int whichLink, String content) {
        if (mFloatWindow != null) {
            mTvTargetLink = null;
            if (whichLink == ROUTE_WIFI) {
                mTvTargetLink = mTvLinkWifi;
            } else if (whichLink == ROUTE_M_NET) {
                mTvTargetLink = mTvLinkMNet;
            } else if (whichLink == ROUTE_G_NET) {
                mTvTargetLink = mTvLinkGNet;
            }
            mTvTargetLink.setText(content);
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(1000);
            //动画集
            AnimationSet as = new AnimationSet(false);
            as.addAnimation(animation);
            mTvTargetLink.startAnimation(as);
        }
    }

    /**
     * @param whichLink
     */
    public static void setTexViewColor(int whichLink) {
        FlyLog.d( "whichLink = " + whichLink);
        if (mContext != null) {
            mTvLinkWifi.setTextColor(whichLink == ROUTE_WIFI ? ContextCompat.getColor(mContext, R.color.colorWhite) : ContextCompat.getColor(mContext, R.color.colorBlack));
            mTvLinkMNet.setTextColor(whichLink == ROUTE_M_NET ? ContextCompat.getColor(mContext, R.color.colorWhite) : ContextCompat.getColor(mContext, R.color.colorBlack));
            mTvLinkGNet.setTextColor(whichLink == ROUTE_G_NET ? ContextCompat.getColor(mContext, R.color.colorWhite) : ContextCompat.getColor(mContext, R.color.colorBlack));
        }
    }

    public static boolean isWindowShowing() {
        return mFloatWindow != null;
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}