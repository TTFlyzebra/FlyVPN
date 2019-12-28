package com.flyzebra.linkmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flyzebra.linkmanager.R;
import com.flyzebra.utils.FlyLog;

import androidx.fragment.app.Fragment;


/**
 * @Author: zhoujinyan
 * @Time: 2019-08-12
 */
public class FaultDiagnosisFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = FaultDiagnosisFragment.class.getSimpleName();
    private View rootView;
    private TextView tv_initConfig_result;
    private TextView tv_m_enabledouble_result;
    private TextView tv_wlan_enabledouble_result;
    private TextView tv_g_enabledouble_result;
    private TextView tv_m_detect_result;
    private TextView tv_wlan_detect_result;
    private TextView tv_g_detect_result;
    private TextView tv_m_detect_failurecount;
    private TextView tv_wlan_detect_failurecount;
    private TextView tv_g_detect_failurecount;
    private TextView tv_m_delet_result;
    private TextView tv_wlan_delet_result;
    private TextView tv_g_delet_result;
    private TextView tv_add_m_result;
    private TextView tv_add_wifi_result;
    private TextView tv_add_g_result;
    private EditText et_ping_network;
    private TextView tv_m_mpexception_result;
    private TextView tv_wlan_mpexception_result;
    private TextView tv_g_mpexception_result;
    private Button btn_ping;
    private Spinner spinner;
    private int spinnerPos = 0;
    private TextView tv_ping_result;
    private boolean isPinging = false;
    private int tunPostion = -1;

    private boolean isPortTest = false;
    private Button mSnedPortCmd;
    private EditText mFullInIp,mFullInPort;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fault_diagnosis_layout, container, false);
        FlyLog.d("onCreateView");
        return rootView;
}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        tv_initConfig_result = (TextView)rootView.findViewById(R.id.tv_initConfig_result);
        tv_m_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_m_enabledouble_result);
        tv_wlan_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_wlan_enabledouble_result);
        tv_g_enabledouble_result = (TextView)rootView.findViewById(R.id.tv_g_enabledouble_result);
        tv_m_detect_result = (TextView)rootView.findViewById(R.id.tv_m_detect_result);
        tv_wlan_detect_result = (TextView)rootView.findViewById(R.id.tv_wlan_detect_result);
        tv_g_detect_result = (TextView)rootView.findViewById(R.id.tv_g_detect_result);
        tv_m_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_m_detect_failurecount);
        tv_wlan_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_wlan_detect_failurecount);
        tv_g_detect_failurecount = (TextView)rootView.findViewById(R.id.tv_g_detect_failurecount);
        tv_m_delet_result = (TextView)rootView.findViewById(R.id.tv_m_delet_result);
        tv_wlan_delet_result = (TextView)rootView.findViewById(R.id.tv_wlan_delet_result);
        tv_g_delet_result = (TextView)rootView.findViewById(R.id.tv_g_delet_result);
        tv_add_m_result = (TextView)rootView.findViewById(R.id.tv_add_m_result);
        tv_add_wifi_result = (TextView)rootView.findViewById(R.id.tv_add_wifi_result);
        tv_add_g_result = (TextView)rootView.findViewById(R.id.tv_add_g_result);
        tv_m_mpexception_result = (TextView)rootView.findViewById(R.id.tv_m_mpexception_result);
        tv_wlan_mpexception_result = (TextView)rootView.findViewById(R.id.tv_wlan_mpexception_result);
        tv_g_mpexception_result = (TextView)rootView.findViewById(R.id.tv_g_mpexception_result);
        et_ping_network = (EditText) rootView.findViewById(R.id.et_ping_network);
        btn_ping = (Button) rootView.findViewById(R.id.btn_ping);
        spinner = (Spinner)rootView.findViewById(R.id.spinner);
        tv_ping_result = (TextView)rootView.findViewById(R.id.tv_ping_result);
        mSnedPortCmd = (Button) rootView.findViewById(R.id.btn_send_port_test);
        mFullInIp = (EditText) rootView.findViewById(R.id.et_fill_in_ip_address);
        mFullInPort = (EditText) rootView.findViewById(R.id.et_fill_in_port);

    }

    private void initListener() {
        btn_ping.setOnClickListener(this);
        mSnedPortCmd.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FlyLog.d("onItemSelected position:" + position+",id:"+id);
                spinnerPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FlyLog.d("onNothingSelected parent:" +parent);
            }
        });
    }
    private void initData() {
        FlyLog.d("initData");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPinging = false;
        isPortTest = false;
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (HermesEventBus.getDefault().isRegistered(this)) {
//            HermesEventBus.getDefault().unregister(this);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ping:
                FlyLog.d("btn_ping");
                if(!isPinging) {
                    isPinging = true;
                    tv_ping_result.setText("");
                    String ip = et_ping_network.getText().toString().trim();
                    sendShellCmd(getAppointedNetworkAdapter(spinnerPos), ip);
                }
                break;
            case R.id.btn_send_port_test:
                FlyLog.d("btn_port_test");
                if(!isPortTest) {
                    String ip = mFullInIp.getText().toString();
                    String port = mFullInPort.getText().toString();
                    FlyLog.d("ip = " + ip + "  port = " + port);
                    tv_ping_result.setText("");
                    if(!ip.isEmpty() && port.isEmpty()){
                        sendShellCmd2(ip, port);
                    }else{
                        Toast mToast = Toast.makeText(getContext(), "请检查ip地址和端口是否输入正确(例:ip地址 14.215.177.39 端口 80)", Toast.LENGTH_LONG);
                        mToast.show();
                    }
                }

                break;
            default:
                break;
        }
    }

    private void sendShellCmd2(String ip, String port) {
        String command = "curl "+ip+":" + port;
        FlyLog.d("sendShellCmd command:" + command);
        isPortTest = true;
    }

    private void sendShellCmd(String pointNet, String ip) {
        String command = "ping "+pointNet+" -c 4 -w 10 " + ip;
        FlyLog.d("sendShellCmd command:" + command);
    }

    private String getAppointedNetworkAdapter(int pos) {
        String pointStr = "";
        switch (pos){
            case 0:
                pointStr ="";
                break;
            case 1:
                pointStr = "-I mcwill";
                break;
            case 2:
                pointStr ="-I wlan0";
                break;
            case 3:
                pointStr ="-I rmnet_data0";
                break;
        }
        return pointStr;
    }
    

}
