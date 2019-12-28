package com.flyzebra.linkmanager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.flyzebra.linkmanager.MainActivity;
import com.flyzebra.linkmanager.R;
import com.flyzebra.utils.FlyLog;
import com.flyzebra.utils.SystemPropTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @Author: __ Weiyi.Lee  liweiyi@cootf.com
 * @Package: _ com.flyzebra.linkmanager.fragment
 * @DESC: ____
 * @Time: ____ created at-2018/09/26 10:57
 */
public class LinkWatcherFragment extends Fragment {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private CheckBox mCbIsShowFloatWindow;
    private CheckBox mCbRcMode;
    private CheckBox mCbMpSwitch;
    private LinkInfoAdapter mLinkInfoAdapter;
    private RecyclerView mRvLinkInfo;
    /**
     * 三条链路信息属性值的总长度
     */
    private static final int LINK_INFO_LENGTH = 30; //modify by wangyongya 20190417 for MP : add three item (MP状态)
    private TextView mTvCurrentLink;
    private TextView mVersionName;
    private String mCurrentLink = null;
    private final static int ROUTE_UNKNOWN = -1;
    private final static int ROUTE_M_NET = 0;
    private final static int ROUTE_G_NET = 1;
    private final static int ROUTE_WIFI = 2;
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    private boolean isSetCheck = false;
    private static final String net_support_multi = "persist.sys.net.support.multi";
    /**
     * 发送更新M网信号等级的广播，用于初始化M网信号等级
     */
    private static final String SEND_AT_EXTEND_REQ = "android.intent.action.SEND_AT_EXTEND_REQ";

    private List<String> mLinkInfoList = new ArrayList();
    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_link_watcher, container, false);
        FlyLog.d("onCreateView");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCbIsShowFloatWindow = (CheckBox) getView().findViewById(R.id.cb_float_window);
        mCbRcMode = (CheckBox) getView().findViewById(R.id.cb_rc_ctrl);
        mCbMpSwitch = (CheckBox) getView().findViewById(R.id.cb_mp_switch);
        mTvCurrentLink = (TextView) getView().findViewById(R.id.tv_current_link);
        mVersionName = (TextView) getView().findViewById(R.id.tv_device_data_state);
        mVersionName.setText("v1.0");

        mCbIsShowFloatWindow.setEnabled(false);
        mCbRcMode.setEnabled(false);

        mLinkInfoList.add(0, "wifi");
        for (int i = 1; i < LINK_INFO_LENGTH; i++) {
            mLinkInfoList.add("");
        }
        mLinkInfoList.set(10, "M网");
        mLinkInfoList.set(20, "G网");
        mRvLinkInfo = (RecyclerView) getView().findViewById(R.id.rv_link_one);
        mLinkInfoAdapter = new LinkInfoAdapter(getActivity(), null);
        mRvLinkInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvLinkInfo.setAdapter(mLinkInfoAdapter);

        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCbMpSwitch.setChecked(SystemPropTools.getBoolen(net_support_multi,true));
                mHandler.postDelayed(this,3000);
            }
        });

        refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FlyLog.d("onDestroy...");
    }


    private void initListener() {
        mCbIsShowFloatWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    SPUtils.getInstance().putBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, true);
                } else {
//                    SPUtils.getInstance().putBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, false);
                }
            }
        });

        mCbRcMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                HermesEventBus.getDefault().post(new QaModeEvent(isChecked));

            }
        });

        mCbMpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((MainActivity)getActivity()).openMultipleStreams();
                }else{
                    ((MainActivity)getActivity()).closeMultipleStreams();
                }
            }
        });
    }

    public void refresh() {
        //链路名称
//        mLinkInfoList.set(0, "Wifi" + (event.wifiConnected() ? (wifiNetInfo != null ? "：" + wifiNetInfo.getSsid() : "") : ""));
//        //wifi信号质量
//        mLinkInfoList.set(1, event.getRssiLevel() + "");
//        //丢包率
//        mLinkInfoList.set(2, !event.wifiConnected() ? "wifi未连接" : event.isWifiAutoConnect() ?
//                (event.getPingPacketLoss() == 101 ? "未测速" : event.getPingPacketLoss() + "") :
//                (event.getManualWifiPacketLoss() == 101 ? "未测速" : event.getManualWifiPacketLoss() + ""));
//        //ping延时
//        String pingDelay = event.isWifiAutoConnect() ? (event.getPingDelayTime() != -1 ? event.getPingDelayTime() + "ms" : "--") :
//                (event.getManualWifiPingDelay() != -1 ? event.getManualWifiPingDelay() + "ms" : "--");
//        mLinkInfoList.set(3, SysPropUtils.isDetectingWifi() ? "--" : pingDelay);
//        //当前是否连接
//        mLinkInfoList.set(4, SysPropUtils.isDetectingWifi() ? "false" : event.wifiConnected() + "");
//        //权重
//        mLinkInfoList.set(5, LinkConfig.getInstance().getWifiCostWeight() + "");
//        //是否手动连接
//        mLinkInfoList.set(6, event.wifiConnected() ? event.isWifiAutoConnect() ? "自动连接" : "手动连接" : "wifi未连接");
//        //拼的次数
//        mLinkInfoList.set(7, event.getPingCount() + "");
//        //最近一次ping的时间
//        mLinkInfoList.set(8, event.getPingTimeMillis() != 0 ? mDateFormat.format(event.getPingTimeMillis()) : "--");
//        mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLinkInfoAdapter.notifyItemRangeChanged(0, 9);
//            }
//        });
//        //链路名称
//        mLinkInfoList.set(10, "M网");
//        //M网信号质量
//        mLinkInfoList.set(11, mcwillNetworkInfo.getSignalLevel() + "");
//        //丢包率
//        mLinkInfoList.set(12, mcwillNetworkInfo.getMcwillState() == NetworkInfo.State.DISCONNECTED ? "M网数据被关闭" :
//                (mcwillNetworkInfo.getPacketLoss() == 101 ? "未测速" : mcwillNetworkInfo.getPacketLoss() + ""));
//        //ping延时
//        mLinkInfoList.set(13, mcwillNetworkInfo.getMcwillPingDelay() == -1 ? "--" : mcwillNetworkInfo.getMcwillPingDelay() + "ms");
//        //当前是否连接
//        mLinkInfoList.set(14, mcwillNetworkInfo.getMcwillState() + "");
//        //权重
//        mLinkInfoList.set(15, LinkConfig.getInstance().getMcwillCostWeight() + "");
//        //惩罚时间
//        mLinkInfoList.set(16, mcwillNetworkInfo.getPenaltyTimeStamp() == 0 ? "--" : mDateFormat.format(mcwillNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始惩罚，180秒后解除");
//        //拼的次数
//        mLinkInfoList.set(17, event.getPingCount() + "");
//        //最近一次ping的时间
//        mLinkInfoList.set(18, event.getLastPingTimeMillis() != 0L ?
//                mDateFormat.format(event.getLastPingTimeMillis()) : "--");
//        mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLinkInfoAdapter.notifyItemRangeChanged(10, 9);
//            }
//        });
//        //链路名称
//        mLinkInfoList.set(20, "G网");
//        //G网信号质量
//        mLinkInfoList.set(21, simNetworkInfo.getSignalLevel() + "");
//        //丢包率
//        mLinkInfoList.set(22, simNetworkInfo.getSimNetState() == NetworkInfo.State.DISCONNECTED ? "G网数据被关闭" :
//                (simNetworkInfo.getPacketLoss() == 101 ? "未测速" : simNetworkInfo.getPacketLoss() + ""));
//        //ping延时
//        mLinkInfoList.set(23, simNetworkInfo.getSimPingDelay() == -1 ? "--" : simNetworkInfo.getSimPingDelay() + "ms");
//        //当前是否连接
//        mLinkInfoList.set(24, simNetworkInfo.getSimNetState() + "");
//        //权重
//        mLinkInfoList.set(25, LinkConfig.getInstance().getSimCostWeight() + "");
//        //惩罚时间
//        mLinkInfoList.set(26, simNetworkInfo.getPenaltyTimeStamp() == 0 ? "--" : mDateFormat.format(simNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始惩罚，180秒后解除");
//        //拼的次数
//        mLinkInfoList.set(27, event.getPingCount() + "");
//        //最近一次ping的时间
//        mLinkInfoList.set(28, event.getLastPingTimeMillis() != 0L ?
//                mDateFormat.format(event.getLastPingTimeMillis()) : "--");
//        mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLinkInfoAdapter.notifyItemRangeChanged(20, 9);
//            }
//        });
//        //add start by wangyongya 20190417 for MP : add MP状态
//        mLinkInfoList.set(9, event.getWifi_statu() == 1 ? "连接" : "断开");
//        mLinkInfoList.set(19, event.getMcwill_statu() == 1 ? "连接" : "断开");
//        mLinkInfoList.set(29, event.getMpG_statu() == 1 ? "连接" : "断开");
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLinkInfoAdapter.notifyItemChanged(9);
//                mLinkInfoAdapter.notifyItemChanged(19);
//                mLinkInfoAdapter.notifyItemChanged(29);
//            }
//        });
        mTvCurrentLink.setText("当前：" + mCurrentLink);
    }


}
