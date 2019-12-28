//package com.flyzebra.linkmanager.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//
//
///**
// * @Author: zhoujinyan
// * @Time: 2019-08-12
// */
//public class FaultDiagnosisFragment extends Fragment implements View.OnClickListener {
//
//    private static final String TAG = FaultDiagnosisFragment.class.getSimpleName();
//    private View rootView;
//    private TextView tv_initConfig_result;
//    private TextView tv_m_enabledouble_result;
//    private TextView tv_wlan_enabledouble_result;
//    private TextView tv_g_enabledouble_result;
//    private TextView tv_m_detect_result;
//    private TextView tv_wlan_detect_result;
//    private TextView tv_g_detect_result;
//    private TextView tv_m_detect_failurecount;
//    private TextView tv_wlan_detect_failurecount;
//    private TextView tv_g_detect_failurecount;
//    private TextView tv_m_delet_result;
//    private TextView tv_wlan_delet_result;
//    private TextView tv_g_delet_result;
//    private TextView tv_add_m_result;
//    private TextView tv_add_wifi_result;
//    private TextView tv_add_g_result;
//    private EditText et_ping_network;
//    private TextView tv_m_mpexception_result;
//    private TextView tv_wlan_mpexception_result;
//    private TextView tv_g_mpexception_result;
//    private Button btn_ping;
//    private Spinner spinner;
//    private int spinnerPos = 0;
//    private TextView tv_ping_result;
//    private boolean isPinging = false;
//    private int tunPostion = -1;
//
//    private boolean isPortTest = false;
//    private Button mSnedPortCmd;
//    private EditText mFullInIp,mFullInPort;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fault_diagnosis_layout, container, false);
//        LogUtils.d(TAG, "onCreateView");
//        return rootView;
//}
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//        initListener();
//    }
//
//    private void initView() {
//        tv_initConfig_result = (TextView)rootView.findViewById(R.id.tv_initConfig_result);
//        tv_m_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_m_enabledouble_result);
//        tv_wlan_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_wlan_enabledouble_result);
//        tv_g_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_g_enabledouble_result);
//        tv_m_detect_result = (TextView)rootView.findViewById(R.id.tv_m_detect_result);
//        tv_wlan_detect_result = (TextView)rootView.findViewById(R.id.tv_wlan_detect_result);
//        tv_g_detect_result = (TextView)rootView.findViewById(R.id.tv_g_detect_result);
//        tv_m_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_m_detect_failurecount);
//        tv_wlan_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_wlan_detect_failurecount);
//        tv_g_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_g_detect_failurecount);
//        tv_m_delet_result = (TextView)rootView.findViewById(R.id.tv_m_delet_result);
//        tv_wlan_delet_result = (TextView)rootView.findViewById(R.id.tv_wlan_delet_result);
//        tv_g_delet_result = (TextView)rootView.findViewById(R.id.tv_g_delet_result);
//        tv_add_m_result = (TextView)rootView.findViewById(R.id.tv_add_m_result);
//        tv_add_wifi_result = (TextView)rootView.findViewById(R.id.tv_add_wifi_result);
//        tv_add_g_result = (TextView)rootView.findViewById(R.id.tv_add_g_result);
//        tv_m_mpexception_result = (TextView)rootView.findViewById(R.id.tv_m_mpexception_result);
//        tv_wlan_mpexception_result = (TextView)rootView.findViewById(R.id.tv_wlan_mpexception_result);
//        tv_g_mpexception_result = (TextView)rootView.findViewById(R.id.tv_g_mpexception_result);
//        et_ping_network = (EditText) rootView.findViewById(R.id.et_ping_network);
//        btn_ping = (Button) rootView.findViewById(R.id.btn_ping);
//        spinner = (Spinner)rootView.findViewById(R.id.spinner);
//        tv_ping_result = (TextView)rootView.findViewById(R.id.tv_ping_result);
//        mSnedPortCmd = (Button) rootView.findViewById(R.id.btn_send_port_test);
//        mFullInIp = (EditText) rootView.findViewById(R.id.et_fill_in_ip_address);
//        mFullInPort = (EditText) rootView.findViewById(R.id.et_fill_in_port);
//
//    }
//
//    private void initListener() {
//        btn_ping.setOnClickListener(this);
//        mSnedPortCmd.setOnClickListener(this);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LogUtils.d(TAG, "onItemSelected position:" + position+",id:"+id);
//                spinnerPos = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                LogUtils.d(TAG, "onNothingSelected parent:" +parent);
//            }
//        });
//    }
//    private void initData() {
//        LogUtils.d(TAG, "initData");
//        boolean initConfig = SPUtils.getInstance().getBoolean(AppConstants.FaultDiagnosisData.INITCONFIG, false);
//        int m_enabledouble = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_ENABLEDOUBLE, DEFAULT_VALUE);
//        int wlan_enabledouble = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_ENABLEDOUBLE, DEFAULT_VALUE);
//        int g_enabledouble = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_ENABLEDOUBLE, DEFAULT_VALUE);
//        int m_detect_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_DETECT_NETWORK, DEFAULT_VALUE);
//        int wlan_detect_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_DETECT_NETWORK, DEFAULT_VALUE);
//        int g_detect_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_DETECT_NETWORK, DEFAULT_VALUE);
//        int m_detect_netwrok_fail_count = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_DETECT_NETWORK_FAIL_COUNT, DEFAULT_VALUE);
//        int wlan_detect_netwrok_fail_count = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_DETECT_NETWORK_FAIL_COUNT, DEFAULT_VALUE);
//        int g_detect_netwrok_fail_count = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_DETECT_NETWORK_FAIL_COUNT, DEFAULT_VALUE);
//        int m_delete_netwrok =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_DELETE_NETWORK, DEFAULT_VALUE);
//        int wlan_delete_netwrok =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_DELETE_NETWORK, DEFAULT_VALUE);
//        int g_delete_netwrok =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_DELETE_NETWORK, DEFAULT_VALUE);
//        int m_add_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_ADD_NETWORK, DEFAULT_VALUE);
//        int wlan_add_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_ADD_NETWORK, DEFAULT_VALUE);
//        int g_add_netwrok = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_ADD_NETWORK, DEFAULT_VALUE);
//        int m_mp_exception = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.M_MP_EXCEPTION, DEFAULT_VALUE);
//        int wlan_mp_exception = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.WLAN_MP_EXCEPTION, DEFAULT_VALUE);
//        int g_mp_exception = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.G_MP_EXCEPTION, DEFAULT_VALUE);
//        int tun_exception = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_EXCEPTION, DEFAULT_VALUE);
//        setInitConfig(initConfig);
//        if(!initConfig) {
//            m_enabledouble = DEFAULT_VALUE;
//        }
//        if(m_enabledouble != DEFAULT_VALUE) {
//            setEnableDoubleStatus(NetworkLink_MCWILL,m_enabledouble);
//        }
//        if(wlan_enabledouble != DEFAULT_VALUE) {
//            setEnableDoubleStatus(NetworkLink_WIFI,wlan_enabledouble);
//        }
//        if(g_enabledouble != DEFAULT_VALUE) {
//            setEnableDoubleStatus(NetworkLink_4G,wlan_enabledouble);
//        }
//        if(m_detect_netwrok != DEFAULT_VALUE) {
//            setDetectNetWork(NetworkLink_MCWILL,m_detect_netwrok);
//        }
//        if(wlan_detect_netwrok != DEFAULT_VALUE) {
//            setDetectNetWork(NetworkLink_WIFI,wlan_detect_netwrok);
//        }
//        if(g_detect_netwrok != DEFAULT_VALUE) {
//            setDetectNetWork(NetworkLink_4G,g_detect_netwrok);
//        }
//        if(m_detect_netwrok_fail_count != DEFAULT_VALUE) {
//            setDetectNetWorkFailCount(NetworkLink_MCWILL,m_detect_netwrok);
//        }
//        if(wlan_detect_netwrok_fail_count != DEFAULT_VALUE) {
//            setDetectNetWorkFailCount(NetworkLink_WIFI,wlan_detect_netwrok_fail_count);
//        }
//        if(g_detect_netwrok_fail_count != DEFAULT_VALUE) {
//            setDetectNetWorkFailCount(NetworkLink_4G,g_detect_netwrok_fail_count);
//        }
//        if(m_mp_exception != DEFAULT_VALUE) {
//            setMpException(NetworkLink_MCWILL,m_mp_exception);
//        }
//        if(wlan_mp_exception != DEFAULT_VALUE) {
//            setMpException(NetworkLink_WIFI,wlan_mp_exception);
//        }
//        if(g_mp_exception != DEFAULT_VALUE) {
//            setMpException(NetworkLink_4G,g_mp_exception);
//        }
//        if(m_add_netwrok != DEFAULT_VALUE) {
//            setAddLink(NetworkLink_MCWILL,m_add_netwrok);
//        }
//        if(wlan_add_netwrok != DEFAULT_VALUE) {
//            setAddLink(NetworkLink_WIFI,wlan_add_netwrok);
//        }
//        if(g_add_netwrok != DEFAULT_VALUE) {
//            setAddLink(NetworkLink_4G,g_add_netwrok);
//        }
//        if(m_delete_netwrok != DEFAULT_VALUE) {
//            setDeleteLink(NetworkLink_MCWILL,m_delete_netwrok);
//        }
//        if(wlan_delete_netwrok != DEFAULT_VALUE) {
//            setDeleteLink(NetworkLink_WIFI,wlan_delete_netwrok);
//        }
//        if(g_delete_netwrok != DEFAULT_VALUE) {
//            setDeleteLink(NetworkLink_4G,g_delete_netwrok);
//        }
//        if(tun_exception != DEFAULT_VALUE) {
//            setMpException(TUN_EXCEPTION_NETTYPE, tun_exception);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        HermesEventBus.getDefault().register(this);
//        isPinging = false;
//        isPortTest = false;
//        initData();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (HermesEventBus.getDefault().isRegistered(this)) {
//            HermesEventBus.getDefault().unregister(this);
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        if (HermesEventBus.getDefault().isRegistered(this)) {
////            HermesEventBus.getDefault().unregister(this);
////        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_ping:
//                LogUtils.d(TAG,"btn_ping");
//                if(!isPinging) {
//                    isPinging = true;
//                    tv_ping_result.setText("");
//                    String ip = et_ping_network.getText().toString().trim();
//                    sendShellCmd(getAppointedNetworkAdapter(spinnerPos), ip);
//                }
//                break;
//            case R.id.btn_send_port_test:
//                LogUtils.d(TAG,"btn_port_test");
//                if(!isPortTest) {
//                    String ip = mFullInIp.getText().toString();
//                    String port = mFullInPort.getText().toString();
//                    LogUtils.d(TAG,"ip = " + ip + "  port = " + port);
//                    tv_ping_result.setText("");
//                    if(!ip.isEmpty() && port.isEmpty()){
//                        sendShellCmd2(ip, port);
//                    }else{
//                        Toast mToast = Toast.makeText(getContext(), "请检查ip地址和端口是否输入正确(例:ip地址 14.215.177.39 端口 80)", Toast.LENGTH_LONG);
//                        mToast.show();
//                    }
//                }
//
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void sendShellCmd2(String ip, String port) {
//        String command = "curl "+ip+":" + port;
//        LogUtils.d(TAG, "sendShellCmd command:" + command);
//        HermesEventBus.getDefault().post(new ShellCmdEvent(command));
//        isPortTest = true;
//    }
//
//    private void sendShellCmd(String pointNet, String ip) {
//        String command = "ping "+pointNet+" -c 4 -w 10 " + ip;
//        LogUtils.d(TAG, "sendShellCmd command:" + command);
//        HermesEventBus.getDefault().post(new ShellCmdEvent(command));
//    }
//
//    private String getAppointedNetworkAdapter(int pos) {
//        String pointStr = "";
//        switch (pos){
//            case 0:
//                pointStr ="";
//                break;
//            case 1:
//                pointStr = "-I mcwill";
//                break;
//            case 2:
//                pointStr ="-I wlan0";
//                break;
//            case 3:
//                pointStr ="-I rmnet_data0";
//                break;
//        }
//        return pointStr;
//    }
//    /**
//     * 执行Shell命令
//     * @param shellCmdResponseEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(ShellCmdResponseEvent shellCmdResponseEvent) {
//        LogUtils.d(TAG,"onEvent: ShellCmdResponseEvent ping结果 :" + shellCmdResponseEvent.getResult());
//        isPinging = false;
//        isPortTest = false;
//        if(shellCmdResponseEvent != null) {
//            tv_ping_result.setText(shellCmdResponseEvent.getResult());
//        }
//    }
//    /**
//     * 获取initConfig结果
//     * @param initConfigEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(InitConfigEvent initConfigEvent) {
//        LogUtils.d(TAG,"onEvent:  initConfig 结果 :" + initConfigEvent.getStatus());
//        isPinging = false;
//        if(initConfigEvent != null) {
//            setInitConfig(initConfigEvent.getStatus());
//        }
//    }
//    /**
//     * 获取使能双流结果
//     * @param enableDoubleStatusEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(EnableDoubleStatusEvent enableDoubleStatusEvent) {
//        LogUtils.d(TAG,"onEvent: enableDoubleStatusEvent结果 :" + enableDoubleStatusEvent.getRsult()+",netType:"+enableDoubleStatusEvent.getNetType());
//        setEnableDoubleStatus(enableDoubleStatusEvent.getNetType(), enableDoubleStatusEvent.getRsult());
//    }
//
//    /**
//     * 获取关闭双流结果
//     * @param disableDoubleStatusEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(DisableDoubleStatusEvent disableDoubleStatusEvent) {
//        LogUtils.d(TAG,"onEvent: enableDoubleStatusEvent结果 :" + disableDoubleStatusEvent.getRsult());
//        setDisableDoubleStatus(disableDoubleStatusEvent.getRsult());
//    }
//    /**
//     * 获取探测结果
//     * @param detectNetWorkEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(DetectNetWorkEvent detectNetWorkEvent) {
//        LogUtils.d(TAG,"onEvent: detectNetWorkEvent结果 :" + detectNetWorkEvent.getResult()+",netType:"+detectNetWorkEvent.getNetType());
//        setDetectNetWork(detectNetWorkEvent.getNetType(), detectNetWorkEvent.getResult());
//    }
//    /**
//     * 获取探测结果
//     * @param detectNetWorkFailCount
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(DetectNetWorkFailCount detectNetWorkFailCount) {
//        LogUtils.d(TAG,"onEvent: detectNetWorkFailCount结果 :" + detectNetWorkFailCount.getCount()+",netType:"+detectNetWorkFailCount.getNetType());
//        setDetectNetWorkFailCount(detectNetWorkFailCount.getNetType(), detectNetWorkFailCount.getCount());
//    }
//    /**
//     * 获取删除链路结果
//     * @param deleteLinkEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(DeleteLinkEvent deleteLinkEvent) {
//        LogUtils.d(TAG,"onEvent: deleteLinkEvent结果 :" + deleteLinkEvent.getResult()+",netType:"+deleteLinkEvent.getNetType());
//        setDeleteLink(deleteLinkEvent.getNetType(), deleteLinkEvent.getResult());
//    }
//
//    /**
//     * 获取添加链路结果
//     * @param addLinkEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(AddLinkEvent addLinkEvent) {
//        tunPostion =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//        LogUtils.d(TAG,"onEvent: addLinkEvent结果 :" + addLinkEvent.getResult()+",netType:"+addLinkEvent.getNetType()+",tunPostion："+tunPostion);
//        setAddLink(addLinkEvent.getNetType(), addLinkEvent.getResult());
//    }
//    /**
//     * 获取MP异常上报
//     * @param mpExceptionEvent
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
//    public void onEvent(MpExceptionEvent mpExceptionEvent) {
//        LogUtils.d(TAG,"onEvent: mpExceptionEvent结果 :" + mpExceptionEvent.getResult()+",netType:"+mpExceptionEvent.getNetType());
//        setMpException(mpExceptionEvent.getNetType(), mpExceptionEvent.getResult());
//    }
//    private void setInitConfig(boolean status) {
//        if(status) {
//            tv_initConfig_result.setText("true");
//        } else {
//            tv_initConfig_result.setText("false");
//        }
//    }
//    private void setEnableDoubleStatus(int netType, int result) {
//        switch (netType){
//            case NetworkLink_MCWILL:
//                if(result != DEFAULT_VALUE) {
//                    tv_m_enabledouble_result.setText(result+"");
//                } else{
//                    tv_m_enabledouble_result.setText("--");
//                }
//
//                break;
//            case NetworkLink_WIFI:
//                if(result != DEFAULT_VALUE) {
//                    tv_wlan_enabledouble_result.setText(result+"");
//                } else{
//                    tv_wlan_enabledouble_result.setText("--");
//                }
//                break;
//            case NetworkLink_4G:
//                if(result != DEFAULT_VALUE) {
//                    tv_g_enabledouble_result.setText(result+"");
//                } else{
//                    tv_g_enabledouble_result.setText("--");
//                }
//                break;
//        }
//    }
//    private void setDisableDoubleStatus(int result) {
//        if(result == 0) {
//            setEnableDoubleStatus(NetworkLink_MCWILL, DEFAULT_VALUE);
//            setEnableDoubleStatus(NetworkLink_WIFI, DEFAULT_VALUE);
//            setEnableDoubleStatus(NetworkLink_4G, DEFAULT_VALUE);
//        }
//    }
//    private void setDetectNetWork(int netType, int result) {
//        switch (netType){
//            case NetworkLink_MCWILL:
//                if(result != DEFAULT_VALUE) {
//                    tv_m_detect_result.setText(result+"");
//                } else{
//                    tv_m_detect_result.setText("--");
//                }
//                break;
//            case NetworkLink_WIFI:
//                if(result != DEFAULT_VALUE) {
//                    tv_wlan_detect_result.setText(result+"");
//                } else{
//                    tv_wlan_detect_result.setText("--");
//                }
//                break;
//            case NetworkLink_4G:
//                if(result != DEFAULT_VALUE) {
//                    tv_g_detect_result.setText(result+"");
//                } else{
//                    tv_g_detect_result.setText("--");
//                }
//                break;
//        }
//    }
//    private void setDetectNetWorkFailCount(int netType, int result) {
//        switch (netType){
//            case NetworkLink_MCWILL:
//                if(result != DEFAULT_VALUE) {
//                    tv_m_detect_failurecount.setText(result+"");
//                } else{
//                    tv_m_detect_failurecount.setText("--");
//                }
//                break;
//            case NetworkLink_WIFI:
//                if(result != DEFAULT_VALUE) {
//                    tv_wlan_detect_failurecount.setText(result+"");
//                } else{
//                    tv_wlan_detect_failurecount.setText("--");
//                }
//                break;
//            case NetworkLink_4G:
//                if(result != DEFAULT_VALUE) {
//                    tv_g_detect_failurecount.setText(result+"");
//                } else{
//                    tv_g_detect_failurecount.setText("--");
//                }
//                break;
//        }
//    }
//    private void setDeleteLink(int netType, int result) {
//        LogUtils.d(TAG,"setDeleteLink netType:" + netType+",result:"+result);
//        switch (netType){
//            case NetworkLink_MCWILL:
//                tv_m_delet_result.setText(result+"");
//                break;
//            case NetworkLink_WIFI:
//                tv_wlan_delet_result.setText(result+"");
//                break;
//            case NetworkLink_4G:
//                tv_g_delet_result.setText(result+"");
//                break;
//        }
//        setDetectNetWork(netType, DEFAULT_VALUE);
//        setDetectNetWorkFailCount(netType, DEFAULT_VALUE);
//        setAddLink(netType, DEFAULT_VALUE);
////        tunPostion =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
////        if(tunPostion == netType) {
////            SPUtils.getInstance().putInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
////        }
//
//    }
//    private void setAddLink(int netType, int result) {
//        switch (netType){
//            case NetworkLink_MCWILL:
//                if(result != DEFAULT_VALUE) {
//                    tunPostion =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//                    if(tunPostion == NetworkLink_MCWILL){
////                        if(tunPostion == -1){
////                            SPUtils.getInstance().putInt(AppConstants.FaultDiagnosisData.TUN_POSITION, NetworkLink_MCWILL);
////                        }
//                        tv_add_m_result.setText(result+" (tun)");
//                    } else {
//                        tv_add_m_result.setText(result+"");
//                    }
//                    setMpException(NetworkLink_MCWILL, DEFAULT_VALUE);
//                    tv_m_delet_result.setText("--");
//                } else{
//                    tv_add_m_result.setText("--");
//                }
//                break;
//            case NetworkLink_WIFI:
//                if(result != DEFAULT_VALUE) {
//                    tunPostion =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//                    if(tunPostion == NetworkLink_WIFI){
////                        if(tunPostion == -1) {
////                            SPUtils.getInstance().putInt(AppConstants.FaultDiagnosisData.TUN_POSITION, NetworkLink_WIFI);
////                        }
//                        tv_add_wifi_result.setText(result+" (tun)");
//                    } else {
//                        tv_add_wifi_result.setText(result+"");
//                    }
//                    setMpException(NetworkLink_WIFI, DEFAULT_VALUE);
//                    tv_wlan_delet_result.setText("--");
//                } else{
//                    tv_add_wifi_result.setText("--");
//                }
//                break;
//            case NetworkLink_4G:
//                if(result != DEFAULT_VALUE) {
//                    tunPostion =  SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//                    if(tunPostion == NetworkLink_4G){
////                        if(tunPostion == -1) {
////                            SPUtils.getInstance().putInt(AppConstants.FaultDiagnosisData.TUN_POSITION, NetworkLink_4G);
////                        }
//                        tv_add_g_result.setText(result+" (tun)");
//                    } else {
//                        tv_add_g_result.setText(result+"");
//                    }
//                    setMpException(NetworkLink_4G, DEFAULT_VALUE);
//                    tv_g_delet_result.setText("--");
//                } else{
//                    tv_add_g_result.setText("--");
//                }
//                break;
//        }
//    }
//    private void setMpException(int netType, int result) {
//        int getPos = -1;
//        if(result == EXCEPTION_CODE_3) {
//            getPos= SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//            netType = getPos;
//            LogUtils.d(TAG,"setMpException result is -3 ,netType:" +netType);
//            if(netType != -1) {
//                setDetectNetWork(netType, DEFAULT_VALUE);
//                setDetectNetWorkFailCount(netType, DEFAULT_VALUE);
//                setAddLink(netType, DEFAULT_VALUE);
//            } else{
//                setDetectNetWork(NetworkLink_MCWILL, DEFAULT_VALUE);
//                setDetectNetWork(NetworkLink_WIFI, DEFAULT_VALUE);
//                setDetectNetWork(NetworkLink_4G, DEFAULT_VALUE);
//                setDetectNetWorkFailCount(NetworkLink_MCWILL, DEFAULT_VALUE);
//                setDetectNetWorkFailCount(NetworkLink_WIFI, DEFAULT_VALUE);
//                setDetectNetWorkFailCount(NetworkLink_4G, DEFAULT_VALUE);
//                setAddLink(NetworkLink_MCWILL, DEFAULT_VALUE);
//                setAddLink(NetworkLink_WIFI, DEFAULT_VALUE);
//                setAddLink(NetworkLink_4G, DEFAULT_VALUE);
//                int pre_network = SPUtils.getInstance().getInt(AppConstants.FaultDiagnosisData.EXCEPTION_PRE_NETWORK, -1);
//                LogUtils.d(TAG,"setMpException pre_network：" +pre_network);
//                netType = pre_network;
//            }
//
////            SPUtils.getInstance().putInt(AppConstants.FaultDiagnosisData.TUN_POSITION, -1);
//        }
//        if(result == EXCEPTION_CODE_5 || result == EXCEPTION_CODE_6){
//            LogUtils.d(TAG,"onEvent: 激活和空闲状态不显示！");
//            return;
//        }
//        switch (netType){
//            case NetworkLink_MCWILL:
//                if(result != DEFAULT_VALUE) {
//                    tv_m_mpexception_result.setText(result+"");
//                } else{
//                    tv_m_mpexception_result.setText("--");
//                }
//                break;
//            case NetworkLink_WIFI:
//                if(result != DEFAULT_VALUE) {
//                    tv_wlan_mpexception_result.setText(result+"");
//                } else{
//                    tv_wlan_mpexception_result.setText("--");
//                }
//                break;
//            case NetworkLink_4G:
//                if(result != DEFAULT_VALUE) {
//                    tv_g_mpexception_result.setText(result+"");
//                } else{
//                    tv_g_mpexception_result.setText("--");
//                }
//                break;
//        }
//    }
//
//}
