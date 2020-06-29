package com.android.server.octopu.wifiextend.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.octopu.R;
import android.octopu.WifiDeviceBean;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Method;



/**
 * Wifi 分享Dialog
 */
public class WifiShareDialog extends Dialog {
    private TextView tvNoteShareWifi;
    private WifiDeviceBean mWifiDeviceBean;

    public WifiShareDialog(Context context) {
        super(context,R.style.wifi_share_dialog);
        if(getWindow() != null){
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    public void setWifiInfo(WifiDeviceBean wifiDeviceBean){
        mWifiDeviceBean = wifiDeviceBean;
        setContent();
    }


    private static Point getRealScreenSize(Context context) {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            try {
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                size.set((Integer) mGetRawW.invoke(windowManager
                        .getDefaultDisplay()), (Integer) mGetRawH
                        .invoke(windowManager.getDefaultDisplay()));
            } catch (Exception e) {
                windowManager.getDefaultDisplay().getSize(size);
            }
        }
        return size;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取提示的ssid
        if (mWifiDeviceBean == null) {
            dismiss();
            return;
        }
        View contentView = View.inflate(getContext(), R.layout.wifi_share_dialog, null);
        tvNoteShareWifi = contentView.findViewById(R.id.tv_note_share_wifi);
        setContent();
        TextView tvShareNow = contentView.findViewById(R.id.tv_share_now);
        tvShareNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即分享
                dismiss();
            }
        });
        TextView tvShareLatter = contentView.findViewById(R.id.tv_share_latter);
        tvShareLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //稍后提醒
                dismiss();
            }
        });
        TextView tvShareNotRemind = contentView.findViewById(R.id.tv_not_remind);
        tvShareNotRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不再提醒
                dismiss();
            }
        });
        Point p = getRealScreenSize(getContext());
        int w = 30;
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(p.x - w,ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(contentView, vlp);
    }

    private void setContent(){
        if(tvNoteShareWifi != null){
            String currentContent = getContext().getString(R.string.note_to_share_wifi,mWifiDeviceBean.wifiName);
            tvNoteShareWifi.setText(currentContent);
        }
    }

}
