//package com.flyzebra.linkmanager.fragment;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cootf.commonlib.config.LinkConfig;
//import com.cootf.commonlib.entity.McwillNetworkInfo;
//import com.cootf.commonlib.entity.SimNetworkInfo;
//import com.cootf.commonlib.entity.WifiNetworkInfo;
//import com.cootf.commonlib.event.ControllerEvent;
//import com.cootf.commonlib.event.CurrentLinkEvent;
//import com.cootf.commonlib.event.MpSwitchEvent;
//import com.cootf.commonlib.event.QaModeEvent;
//import com.cootf.commonlib.event.QaModeFailedEvent;
//import com.cootf.commonlib.event.RefreshUiEvent;
//import com.cootf.commonlib.utils.AppConstants;
//import com.cootf.commonlib.utils.LogUtils;
//import com.cootf.commonlib.utils.SPUtils;
//import com.cootf.commonlib.utils.SysPropUtils;
//import com.octopus.linkmanager.R;
//import com.flyzebra.linkmanager.floatwindow.FloatWindowManager;
//import com.flyzebra.linkmanager.floatwindow.FloatWindowService;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.fragment.app.Fragment;
//import xiaofei.library.hermeseventbus.HermesEventBus;
//
//
///**
// * @Author: __ Weiyi.Lee  liweiyi@cootf.com
// * @Package: _ com.flyzebra.linkmanager.fragment
// * @DESC: ____
// * @Time: ____ created at-2018/09/26 10:57
// */
//public class LinkWatcherFragment extends Fragment {
//    private static String TAG = "LinkWatcherFragment";
//    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private CheckBox mCbIsShowFloatWindow;
//    private CheckBox mCbRcMode;
//    private CheckBox mCbMpSwitch;
//    private LinkInfoAdapter mLinkInfoAdapter;
//    private RecyclerView mRvLinkInfo;
//    /**
//     * 三条链路信息属性值的总长度
//     */
//    private static final int LINK_INFO_LENGTH = 30; //modify by wangyongya 20190417 for MP : add three item (MP状态)
//    private TextView mTvCurrentLink;
//    private TextView mVersionName;
//    private String mCurrentLink = null;
//    private final static int ROUTE_UNKNOWN = -1;
//    private final static int ROUTE_M_NET = 0;
//    private final static int ROUTE_G_NET = 1;
//    private final static int ROUTE_WIFI = 2;
//    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
//    private boolean isSetCheck = false;
//    private static final String net_support_multi = "persist.sys.net.support.multi";
//    /**
//     * 发送更新M网信号等级的广播，用于初始化M网信号等级
//     */
//    private static final String SEND_AT_EXTEND_REQ = "android.intent.action.SEND_AT_EXTEND_REQ";
//
//    private List<String> mLinkInfoList = new ArrayList();
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.fragment_link_watcher, container, false);
//        LogUtils.d(TAG, "onCreateView");
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView(getView());
//        initListener();
//        sendInitMcwillInfoBroadcast(getActivity());
//    }
//
//    private void initView(View view) {
//        mCbIsShowFloatWindow = (CheckBox) view.findViewById(R.id.cb_float_window);
//        mCbRcMode = (CheckBox) view.findViewById(R.id.cb_rc_ctrl);
//        mCbMpSwitch = (CheckBox) view.findViewById(R.id.cb_mp_switch);
//        mTvCurrentLink = (TextView) view.findViewById(R.id.tv_current_link);
//        mVersionName = (TextView) view.findViewById(R.id.tv_device_data_state);
//        mVersionName.setText("v" + AppConstants.getLocalVersionName(getActivity()));
//
//        mCbIsShowFloatWindow.setChecked(SPUtils.getInstance().getBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, true));
//        mCbRcMode.setChecked(SPUtils.getInstance().getBoolean(AppConstants.IS_RC_MODE_ON, false));
//        mCbMpSwitch.setChecked(LinkConfig.getInstance().isMpServiceOpen());
//
//        mLinkInfoList.add(0, "wifi");
//        for (int i = 1; i < LINK_INFO_LENGTH; i++) {
//            mLinkInfoList.add("");
//        }
//        mLinkInfoList.set(10, "M网");
//        mLinkInfoList.set(20, "G网");
//        mRvLinkInfo = (RecyclerView) view.findViewById(R.id.rv_link_one);
//        mLinkInfoAdapter = new LinkInfoAdapter(getActivity(), null);
//        mRvLinkInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRvLinkInfo.setAdapter(mLinkInfoAdapter);
//    }
//
//    private void initListener() {
//        mCbIsShowFloatWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    SPUtils.getInstance().putBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, true);
//                } else {
//                    SPUtils.getInstance().putBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, false);
//                }
//            }
//        });
//
//        mCbRcMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                HermesEventBus.getDefault().post(new QaModeEvent(isChecked));
//            }
//        });
//
//        mCbMpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(!isSetCheck) {
//                    showDialog(isChecked);
//                    LogUtils.d(TAG, "showDialog isChecked: " + buttonView.isChecked()+",isFocused:"+buttonView.isFocused());
//                } else{
//                    isSetCheck = false;
//                }
//
//            }
//        });
//    }
//
//    private void showDialog(final boolean status){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setIcon(null)
//                .setTitle("Warning ! ! !")
//                .setMessage(status ? "开启多流会重启终端，确定要打开吗？" : "关闭多流会重启终端，确定要关闭吗？")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        LinkConfig.getInstance().setMpServiceOpen(status ? true : false);
//                        LogUtils.d(TAG, "showDialog status: " + status);
//                        HermesEventBus.getDefault().post(new MpSwitchEvent(status));
//                        SysPropUtils.set(net_support_multi,status ? "true":"false");
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        isSetCheck = true;
//                        mCbMpSwitch.setChecked(!status);
//                    }
//                })
//                .setCancelable(false)
//                .show();
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 100, sticky = true)
//    public void onEvent(ControllerEvent event) {
//        LogUtils.d(TAG, "onEvent, ControllerEvent getLink() = " + event.getLink());
//        switch (event.getLink()) {
//            case LINK_WIFI:
////                WifiController wifiController = event.getWifiController();
//                WifiNetworkInfo wifiNetInfo = event.getWifiNetInfo();
//                //链路名称
//                mLinkInfoList.set(0, "Wifi" + (event.wifiConnected() ? (wifiNetInfo != null ? "：" + wifiNetInfo.getSsid() : "") : ""));
//                //wifi信号质量
//                mLinkInfoList.set(1, event.getRssiLevel() + "");
//                //丢包率
//                mLinkInfoList.set(2, !event.wifiConnected() ? "wifi未连接" : event.isWifiAutoConnect() ?
//                        (event.getPingPacketLoss() == 101 ? "未测速" : event.getPingPacketLoss() + "") :
//                        (event.getManualWifiPacketLoss() == 101 ? "未测速" : event.getManualWifiPacketLoss() + ""));
//                //ping延时
//                String pingDelay = event.isWifiAutoConnect() ? (event.getPingDelayTime() != -1 ? event.getPingDelayTime() + "ms" : "--") :
//                        (event.getManualWifiPingDelay() != -1 ? event.getManualWifiPingDelay() + "ms" : "--");
//                mLinkInfoList.set(3, SysPropUtils.isDetectingWifi() ? "--" : pingDelay);
//                //当前是否连接
//                mLinkInfoList.set(4, SysPropUtils.isDetectingWifi() ? "false" : event.wifiConnected() + "");
//                //权重
//                mLinkInfoList.set(5, LinkConfig.getInstance().getWifiCostWeight() + "");
//                //是否手动连接
//                mLinkInfoList.set(6, event.wifiConnected() ? event.isWifiAutoConnect() ? "自动连接" : "手动连接" : "wifi未连接");
//                //拼的次数
//                mLinkInfoList.set(7, event.getPingCount() + "");
//                //最近一次ping的时间
//                mLinkInfoList.set(8, event.getPingTimeMillis() != 0 ? mDateFormat.format(event.getPingTimeMillis()) : "--");
//                mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLinkInfoAdapter.notifyItemRangeChanged(0, 9);
//                    }
//                });
//                break;
//            case LINK_MNET:
//                McwillNetworkInfo mcwillNetworkInfo = event.getMcwillNetworkInfo();
//                //链路名称
//                mLinkInfoList.set(10, "M网");
//                //M网信号质量
//                mLinkInfoList.set(11, mcwillNetworkInfo.getSignalLevel() + "");
//                //丢包率
//                mLinkInfoList.set(12, mcwillNetworkInfo.getMcwillState() == NetworkInfo.State.DISCONNECTED ? "M网数据被关闭" :
//                        (mcwillNetworkInfo.getPacketLoss() == 101 ? "未测速" : mcwillNetworkInfo.getPacketLoss() + ""));
//                //ping延时
//                mLinkInfoList.set(13, mcwillNetworkInfo.getMcwillPingDelay() == -1 ? "--" : mcwillNetworkInfo.getMcwillPingDelay() + "ms");
//                //当前是否连接
//                mLinkInfoList.set(14, mcwillNetworkInfo.getMcwillState() + "");
//                //权重
//                mLinkInfoList.set(15, LinkConfig.getInstance().getMcwillCostWeight() + "");
//                //惩罚时间
//                mLinkInfoList.set(16, mcwillNetworkInfo.getPenaltyTimeStamp() == 0 ? "--" : mDateFormat.format(mcwillNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始惩罚，180秒后解除");
//                //拼的次数
//                mLinkInfoList.set(17, event.getPingCount() + "");
//                //最近一次ping的时间
//                mLinkInfoList.set(18, event.getLastPingTimeMillis() != 0L ?
//                        mDateFormat.format(event.getLastPingTimeMillis()) : "--");
//                mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLinkInfoAdapter.notifyItemRangeChanged(10, 9);
//                    }
//                });
//                break;
//            case LINK_GNET:
//                SimNetworkInfo simNetworkInfo = event.getSimNetworkInfo();
//                //链路名称
//                mLinkInfoList.set(20, "G网");
//                //G网信号质量
//                mLinkInfoList.set(21, simNetworkInfo.getSignalLevel() + "");
//                //丢包率
//                mLinkInfoList.set(22, simNetworkInfo.getSimNetState() == NetworkInfo.State.DISCONNECTED ? "G网数据被关闭" :
//                        (simNetworkInfo.getPacketLoss() == 101 ? "未测速" : simNetworkInfo.getPacketLoss() + ""));
//                //ping延时
//                mLinkInfoList.set(23, simNetworkInfo.getSimPingDelay() == -1 ? "--" : simNetworkInfo.getSimPingDelay() + "ms");
//                //当前是否连接
//                mLinkInfoList.set(24, simNetworkInfo.getSimNetState() + "");
//                //权重
//                mLinkInfoList.set(25, LinkConfig.getInstance().getSimCostWeight() + "");
//                //惩罚时间
//                mLinkInfoList.set(26, simNetworkInfo.getPenaltyTimeStamp() == 0 ? "--" : mDateFormat.format(simNetworkInfo.getPenaltyTimeStamp()).substring(11, 19) + "开始惩罚，180秒后解除");
//                //拼的次数
//                mLinkInfoList.set(27, event.getPingCount() + "");
//                //最近一次ping的时间
//                mLinkInfoList.set(28, event.getLastPingTimeMillis() != 0L ?
//                        mDateFormat.format(event.getLastPingTimeMillis()) : "--");
//                mLinkInfoAdapter.setPropertyValues(mLinkInfoList);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLinkInfoAdapter.notifyItemRangeChanged(20, 9);
//                    }
//                });
//                break;
//            //add start by wangyongya 20190417 for MP : add MP状态
//            case LINK_MP:
//                mLinkInfoList.set(9,event.getWifi_statu()==1 ? "连接" : "断开");
//                mLinkInfoList.set(19,event.getMcwill_statu()==1 ? "连接" : "断开");
//                mLinkInfoList.set(29,event.getMpG_statu()==1 ? "连接" : "断开");
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLinkInfoAdapter.notifyItemChanged(9);
//                        mLinkInfoAdapter.notifyItemChanged(19);
//                        mLinkInfoAdapter.notifyItemChanged(29);
//                    }
//                });
//                break;
//            //add end by wangyongya 20190417 for MP : add MP状态
//            default:
//                break;
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 100, sticky = true)
//    public void onEvent(CurrentLinkEvent currentLinkEvent) {
//        mCurrentLink = null;
//        LogUtils.d(TAG, "onEvent, CurrentLinkEvent getCurrentLink() = " + currentLinkEvent.getCurrentLink());
//        switch (currentLinkEvent.getCurrentLink()) {
//            case ROUTE_UNKNOWN:
//                mCurrentLink = "不可用";
//                break;
//            case ROUTE_M_NET:
//                mCurrentLink = "M网";
//                break;
//            case ROUTE_G_NET:
//                mCurrentLink = "G网";
//                break;
//            case ROUTE_WIFI:
//                mCurrentLink = "wifi";
//                break;
//            default:
//                break;
//        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mTvCurrentLink.setText("当前：" + mCurrentLink);
//            }
//        });
//    }
//
//    @Override
//    public void onPause() {
//        LogUtils.d(TAG, "onPause, mCbIsShowFloatWindow.isChecked() = " + mCbIsShowFloatWindow.isChecked());
//        //页面挂起，不接收消息
//        if (HermesEventBus.getDefault().isRegistered(this)) {
//            HermesEventBus.getDefault().unregister(this);
//        }
//        if (mCbIsShowFloatWindow.isChecked()) {
//            FloatWindowService.startSelf(getActivity());
//        } else {
//            FloatWindowManager.removeFloatWindow(getActivity());
//            FloatWindowService.stopFloatWindowService(getActivity());
//        }
//        HermesEventBus.getDefault().postSticky(new RefreshUiEvent(true));
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        HermesEventBus.getDefault().register(this);
//        HermesEventBus.getDefault().postSticky(new RefreshUiEvent(true));
//        LogUtils.d(TAG, "onResume, mCbIsShowFloatWindow.isChecked() = " + mCbIsShowFloatWindow.isChecked());
//        mCbIsShowFloatWindow.setChecked(SPUtils.getInstance().getBoolean(AppConstants.IS_OPEN_FLOAT_WINDOW, false));
//        mCbRcMode.setChecked(SPUtils.getInstance().getBoolean(AppConstants.IS_RC_MODE_ON, false));
//        mCbMpSwitch.setChecked(LinkConfig.getInstance().isMpServiceOpen());
//        LogUtils.d(TAG, "onResume, mCbIsShowFloatWindow.isChecked() = " + mCbIsShowFloatWindow.isChecked());
//        mTvCurrentLink.setText("当前：" + mCurrentLink);
//        sendInitMcwillInfoBroadcast(getActivity());
//        //检查悬浮窗开关
//        if (mCbIsShowFloatWindow.isChecked()) {
//            FloatWindowService.startSelf(getActivity());
//        } else {
//            FloatWindowService.stopFloatWindowService(getActivity());
//        }
//        updataMpStatus();
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LogUtils.d(TAG, "onDestroy...");
//        SPUtils.getInstance().putBoolean(AppConstants.IS_APP_QUIT, true);
////        if (HermesEventBus.getDefault().isRegistered(this)) {
////            HermesEventBus.getDefault().unregister(this);
////        }
//    }
//
//    /**
//     * 发送获取M网信息的广播
//     *
//     * @param context
//     */
//    private void sendInitMcwillInfoBroadcast(Context context) {
//        Intent intent = new Intent();
//        intent.setAction(SEND_AT_EXTEND_REQ);
//        intent.putExtra("data", "AT+CSQ");
//        context.sendBroadcast(intent);
//    }
//
//    /**
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
//    public void onEvent(QaModeFailedEvent event) {
//        LogUtils.d(TAG,"onEvent: set QA mode failed");
//        if (event.isQaModeFailed()) {
//            Toast.makeText(getActivity(), "设置QA环境失败，请重试", Toast.LENGTH_SHORT).show();
//            mCbRcMode.setChecked(false);
//        }
//    }
//    private void updataMpStatus() {
//        int preGstatus = SPUtils.getInstance().getInt(AppConstants.GSTATUS,0);
//        int preMstatus = SPUtils.getInstance().getInt(AppConstants.MSTATUS,0);
//        int preWstatus = SPUtils.getInstance().getInt(AppConstants.WSTATUS,0);
//        LogUtils.d(TAG, "updataMpStatus  preGstatus：" + preGstatus+",preMstatus:"+preMstatus+",preWstatus:"+preWstatus);
//        mLinkInfoList.set(9,preWstatus==1 ? "连接" : "断开");
//        mLinkInfoList.set(19,preMstatus==1 ? "连接" : "断开");
//        mLinkInfoList.set(29,preGstatus==1 ? "连接" : "断开");
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLinkInfoAdapter.notifyItemChanged(9);
//                mLinkInfoAdapter.notifyItemChanged(19);
//                mLinkInfoAdapter.notifyItemChanged(29);
//            }
//        });
//    }
//}
