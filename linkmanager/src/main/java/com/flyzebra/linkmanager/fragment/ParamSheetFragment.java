package com.flyzebra.linkmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.flyzebra.linkmanager.R;
import com.flyzebra.utils.SystemPropTools;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: __ Weiyi.Lee  liweiyi@cootf.com
 * @Package: _ com.flyzebra.linkmanager.fragment
 * @DESC: ____ 参数表
 * @Time: ____ created at-2018-09-20 14:41
 */
public class ParamSheetFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = ParamSheetFragment.class.getSimpleName();
    private Button mBtnSubmit;
    private Button mBtnReset;
    private EditText mEtMcwillLevelPassValue;
    private EditText mEtManualMcwillPacketLoss;
    private EditText mEtMcwillRatePassValue;
    private EditText mEtMcwillShakeTime;
    private EditText mEtMcwillCostWeight;
    private EditText mEtMcwillForbidTime;

    private EditText mEtWifiLevelPassValue;
    private EditText mEtWifiRatePassValue;
    private EditText mEtManualWifiPacketLoss;
    private EditText mEtWifiShakeTime;
    private EditText mEtMcwillDetectPeriod;
    private EditText mEtWifiCostWeight;
    private EditText mEtWifiForbidTime;

    private EditText mEtSimLevelPassValue;
    private EditText mEtSimRatePassValue;
    private EditText mEtManualSimPacketLoss;
    private EditText mEtSimShakeTime;
    private EditText mEtSimCostWeight;
    private EditText mEtSimForbidTime;

    private EditText mEtMpMagIp;
    private EditText mEtMpMagDns;
    private EditText mEtMpMagDns2;
    private Switch mMpcLogSwitch;
    private String mSetMAG;
    private String mSetDNS1;
    private String mSetDNS2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_param_sheet, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        initListener();
    }

    private void initView(View view) {
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        mBtnReset = (Button) view.findViewById(R.id.btn_reset);
        mEtMcwillLevelPassValue = (EditText) view.findViewById(R.id.et_mcwill_level_pass_value);
        mEtManualMcwillPacketLoss = (EditText) view.findViewById(R.id.et_manual_mcwill_packet_loss);
        mEtMcwillRatePassValue = (EditText) view.findViewById(R.id.et_mcwill_rate_pass_value);
        mEtMcwillShakeTime = (EditText) view.findViewById(R.id.et_mcwill_shake_time);
        mEtMcwillDetectPeriod = (EditText) view.findViewById(R.id.et_mcwill_detect_period);
        mEtMcwillCostWeight = (EditText) view.findViewById(R.id.et_mcwill_cost_weight);
        mEtMcwillForbidTime = (EditText) view.findViewById(R.id.et_mcwill_forbid_time);

        mEtWifiLevelPassValue = (EditText) view.findViewById(R.id.et_wifi_level_pass_value);
        mEtWifiRatePassValue = (EditText) view.findViewById(R.id.et_wifi_rate_pass_value);
        mEtManualWifiPacketLoss = (EditText) view.findViewById(R.id.et_manual_wifi_packet_loss);
        mEtWifiShakeTime = (EditText) view.findViewById(R.id.et_wifi_shake_time);
        mEtWifiCostWeight = (EditText) view.findViewById(R.id.et_wifi_cost_weight);
        mEtWifiForbidTime = (EditText) view.findViewById(R.id.et_wifi_forbid_time);

        mEtSimLevelPassValue = (EditText) view.findViewById(R.id.et_sim_level_pass_value);
        mEtSimRatePassValue = (EditText) view.findViewById(R.id.et_sim_rate_pass_value);
        mEtManualSimPacketLoss = (EditText) view.findViewById(R.id.et_manual_sim_packet_loss);
        mEtSimShakeTime = (EditText) view.findViewById(R.id.et_sim_shake_time);
        mEtSimCostWeight = (EditText) view.findViewById(R.id.et_sim_cost_weight);
        mEtSimForbidTime = (EditText) view.findViewById(R.id.et_sim_forbid_time);
        mEtMpMagIp = (EditText) view.findViewById(R.id.et_mp_magip_value);
        mEtMpMagDns = (EditText) view.findViewById(R.id.et_mp_magDns_value);
        mEtMpMagDns2 = (EditText) view.findViewById(R.id.et_mp_magDns2_value);
        mMpcLogSwitch = (Switch) view.findViewById(R.id.mpc_log_switch);
        mMpcLogSwitch.setChecked(SystemPropTools.getBoolen("persist.sys.mag.log",false));
        mSetMAG = SystemPropTools.get("persist.sys.mag.ip","210.12.248.82");;
        mSetDNS1 = SystemPropTools.get("persist.sys.mag.dns","202.106.0.20");
        mSetDNS2 = SystemPropTools.get("persist.sys.mag.dns2","8.8.8.8");
        mEtMpMagIp.setHint(mSetMAG);
        mEtMpMagIp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String text = ((EditText)v).getText().toString();
                    if(!TextUtils.isEmpty(text)){
                        if(!isIpString(text)){
                            Toast.makeText(getActivity(),"无效的IP地址",Toast.LENGTH_SHORT).show();
                            ((EditText)v).setText("");
                        }
                    }
                }
            }
        });
        mEtMpMagDns.setHint(mSetDNS1);
        mEtMpMagDns2.setHint(mSetDNS2);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListener() {
        mBtnSubmit.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        mMpcLogSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent intent = new Intent("ACTION_MAIN_SERVICE_RESP_UI_OPT");
        intent.putExtra("KEY_OPT_NET_TYPE",7);
        if(isChecked){
            intent.putExtra("KEY_OPT_CODE",1);
            SystemPropTools.set("persist.sys.mag.log","true");
        }else{
            intent.putExtra("KEY_OPT_CODE",0);
            SystemPropTools.set("persist.sys.mag.log","false");
        }
        this.getActivity().sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String newMAG = mEtMpMagIp.getText().toString();
                String newDNS1 = mEtMpMagIp.getText().toString();
                String newDNS2 = mEtMpMagIp.getText().toString();
                SystemPropTools.set("persist.sys.mag.ip",newMAG);
                SystemPropTools.set("persist.sys.mag.dns",newDNS1);
                SystemPropTools.set("persist.sys.mag.dns2",newDNS2);
                mEtMpMagIp.setHint(mEtMpMagIp.getText().toString());
                mEtMpMagDns.setHint(mEtMpMagIp.getText().toString());
                mEtMpMagDns2.setHint(mEtMpMagIp.getText().toString());
            case R.id.btn_reset:
                mEtMpMagIp.setText("");
                mEtMpMagDns.setText("");
                mEtMpMagDns2.setText("");
                break;
        }
    }

    boolean isIpString(String arg0){
        boolean is=true;
        try {
            InetAddress ia=InetAddress.getByName("arg0");
        } catch (UnknownHostException e) {
            is=false;
        }
        return is;
    }

}
