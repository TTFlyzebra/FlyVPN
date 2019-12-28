package com.flyzebra.linkmanager.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.linkmanager.LinkManager;

import java.lang.reflect.Field;

import xinwei.com.mpapp.R;

/**
 * //=============Dragon Be Here!============
 * //       ___     ___
 * //      ┏┛ ┻━━━━━┛ ┻┓
 * //      ┃　　　　　　 ┃
 * //      ┃　　　━　　　┃
 * //      ┃　┳┛　  ┗┳　┃
 * //      ┃　　　　　　 ┃
 * //      ┃　　　┻　　　┃
 * //      ┃　　　　　　 ┃
 * //      ┗━┓　　　┏━━━┛
 * //        ┃　　　┃   神兽保佑
 * //        ┃　　　┃   代码无BUG！
 * //        ┃　　　┗━━━━━━━━━┓
 * //        ┃　　　　　　　    ┣┓
 * //        ┃　　　　         ┏┛
 * //        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
 * //          ┃ ┫ ┫   ┃ ┫ ┫
 * //          ┗━┻━┛   ┗━┻━┛
 * //=============神兽出没========By Jelansty
 *
 * @Author: __ Weiyi.Lee  liweiyi@cootf.com
 * @Package: _ com.octopus.linkmanager.float_window
 * @DESC: ____
 * @Time: ____ created at-2018-09-19 16:22
 */
public class FloatWindow extends LinearLayout {
    private static String TAG = "FloatWindow";
    public static int viewWidth;  //悬浮窗的宽度
    public static int viewHeight; //悬浮窗的高度
    private static int statusBarHeight; //系统状态栏的高度
    private WindowManager windowManager; //用于更新悬浮窗的位置
    private WindowManager.LayoutParams mParams; //悬浮窗的参数
    private float xInScreen; //当前手指位置在屏幕上的横坐标值
    private float yInScreen; //当前手指位置在屏幕上的纵坐标值
    private float xDownInScreen; //手指按下时在屏幕上的横坐标的值
    private float yDownInScreen; //手指按下时在屏幕上的纵坐标的值
    private float xInView; //手指按下时在悬浮窗的View上的横坐标的值
    private float yInView;//手指按下时在悬浮窗的View上的纵坐标的值
    private TextView mTvLinkOne, mTvLinkTwo, mTvLinkThree;

    private ImageView closeView;  //关闭按钮

    public FloatWindow(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_float_window, this);
        View view = findViewById(R.id.fl_float_window);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        closeView = (ImageView) findViewById(R.id.iv_close_notify);
        closeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LinkManager.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//        closeView.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                FloatWindowManager.removeFloatWindow(context);
//                Intent intent = new Intent(getContext(), FloatWindowService.class);
//                context.stopService(intent);
//                return false;
//            }
//        });

        mTvLinkOne = (TextView) findViewById(R.id.tv_link_one);
        mTvLinkOne.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvLinkOne.setHorizontallyScrolling(true);
        mTvLinkOne.setFocusable(true);

        mTvLinkTwo = (TextView) findViewById(R.id.tv_link_two);
        mTvLinkTwo.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvLinkTwo.setHorizontallyScrolling(true);
        mTvLinkTwo.setFocusable(true);

        mTvLinkThree = (TextView) findViewById(R.id.tv_link_three);
        mTvLinkThree.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvLinkThree.setHorizontallyScrolling(true);
        mTvLinkThree.setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将悬浮窗的参数传入，用于更新悬浮窗的位置。
     *
     * @param params 悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新悬浮窗在屏幕中的位置（手指拖动）
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 获取tv控件
     *
     * @param which
     * @return
     */
    public TextView getLinkTextView(int which) {
        if (which == 1) {
            return mTvLinkOne;
        } else if (which == 2) {
            return mTvLinkTwo;
        } else {
            return mTvLinkThree;
        }
    }
}