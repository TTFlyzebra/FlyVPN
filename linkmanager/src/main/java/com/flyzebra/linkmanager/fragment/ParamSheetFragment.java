//package com.flyzebra.linkmanager.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.Switch;
//
//import com.cootf.commonlib.config.LinkConfig;
//import com.cootf.commonlib.utils.LogUtils;
//import com.cootf.commonlib.utils.SysPropUtils;
//import com.octopus.linkmanager.R;
//
//import androidx.fragment.app.Fragment;
//
///**
// * @Author: __ Weiyi.Lee  liweiyi@cootf.com
// * @Package: _ com.flyzebra.linkmanager.fragment
// * @DESC: ____ 参数表
// * @Time: ____ created at-2018-09-20 14:41
// */
//public class ParamSheetFragment extends Fragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
//    private static final String TAG = ParamSheetFragment.class.getSimpleName();
//    private Button mBtnSubmit;
//    private Button mBtnReset;
//    private EditText mEtMcwillLevelPassValue;
//    private EditText mEtManualMcwillPacketLoss;
//    private EditText mEtMcwillRatePassValue;
//    private EditText mEtMcwillShakeTime;
//    private EditText mEtMcwillCostWeight;
//    private EditText mEtMcwillForbidTime;
//
//    private EditText mEtWifiLevelPassValue;
//    private EditText mEtWifiRatePassValue;
//    private EditText mEtManualWifiPacketLoss;
//    private EditText mEtWifiShakeTime;
//    private EditText mEtMcwillDetectPeriod;
//    private EditText mEtWifiCostWeight;
//    private EditText mEtWifiForbidTime;
//
//    private EditText mEtSimLevelPassValue;
//    private EditText mEtSimRatePassValue;
//    private EditText mEtManualSimPacketLoss;
//    private EditText mEtSimShakeTime;
//    private EditText mEtSimCostWeight;
//    private EditText mEtSimForbidTime;
//
//    private EditText mEtMpMagIp;
//    private EditText mEtMpMagDns;
//    private EditText mEtMpMagDns2;
//    private Switch mMpcLogSwitch;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.fragment_param_sheet, container, false);
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView(getView());
//        initListener();
//    }
//
//    private void initView(View view) {
//        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
//        mBtnReset = (Button) view.findViewById(R.id.btn_reset);
//        mEtMcwillLevelPassValue = (EditText) view.findViewById(R.id.et_mcwill_level_pass_value);
//        mEtManualMcwillPacketLoss = (EditText) view.findViewById(R.id.et_manual_mcwill_packet_loss);
//        mEtMcwillRatePassValue = (EditText) view.findViewById(R.id.et_mcwill_rate_pass_value);
//        mEtMcwillShakeTime = (EditText) view.findViewById(R.id.et_mcwill_shake_time);
//        mEtMcwillDetectPeriod = (EditText) view.findViewById(R.id.et_mcwill_detect_period);
//        mEtMcwillCostWeight = (EditText) view.findViewById(R.id.et_mcwill_cost_weight);
//        mEtMcwillForbidTime = (EditText) view.findViewById(R.id.et_mcwill_forbid_time);
//
//        mEtWifiLevelPassValue = (EditText) view.findViewById(R.id.et_wifi_level_pass_value);
//        mEtWifiRatePassValue = (EditText) view.findViewById(R.id.et_wifi_rate_pass_value);
//        mEtManualWifiPacketLoss = (EditText) view.findViewById(R.id.et_manual_wifi_packet_loss);
//        mEtWifiShakeTime = (EditText) view.findViewById(R.id.et_wifi_shake_time);
//        mEtWifiCostWeight = (EditText) view.findViewById(R.id.et_wifi_cost_weight);
//        mEtWifiForbidTime = (EditText) view.findViewById(R.id.et_wifi_forbid_time);
//
//        mEtSimLevelPassValue = (EditText) view.findViewById(R.id.et_sim_level_pass_value);
//        mEtSimRatePassValue = (EditText) view.findViewById(R.id.et_sim_rate_pass_value);
//        mEtManualSimPacketLoss = (EditText) view.findViewById(R.id.et_manual_sim_packet_loss);
//        mEtSimShakeTime = (EditText) view.findViewById(R.id.et_sim_shake_time);
//        mEtSimCostWeight = (EditText) view.findViewById(R.id.et_sim_cost_weight);
//        mEtSimForbidTime = (EditText) view.findViewById(R.id.et_sim_forbid_time);
//        mEtMpMagIp = (EditText) view.findViewById(R.id.et_mp_magip_value);
//        mEtMpMagDns = (EditText) view.findViewById(R.id.et_mp_magDns_value);
//        mEtMpMagDns2 = (EditText)view.findViewById(R.id.et_mp_magDns2_value);
//        mMpcLogSwitch = (Switch) view.findViewById(R.id.mpc_log_switch);
//        mMpcLogSwitch.setChecked(LinkConfig.getInstance().getMpcLog());
//        setMagText();
//    }
//
//    private void setMagText(){
//        mEtMpMagIp.setHint(LinkConfig.getInstance().getMagIp());
//        mEtMpMagDns.setHint(LinkConfig.getInstance().getMagDns());
//        mEtMpMagDns2.setHint(LinkConfig.getInstance().getMagDns2());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setMagText();
//    }
//
//    private void initListener() {
//        mBtnSubmit.setOnClickListener(this);
//        mBtnReset.setOnClickListener(this);
//        mMpcLogSwitch.setOnCheckedChangeListener(this);
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        LogUtils.d(TAG, "Mpc Log Switch onCheckedChanged...isChecked="+isChecked);
//        Intent intent = new Intent("ACTION_MAIN_SERVICE_RESP_UI_OPT");
//        intent.putExtra("KEY_OPT_NET_TYPE",7);
//        if(isChecked){
//            intent.putExtra("KEY_OPT_CODE",1);
//            LinkConfig.getInstance().setMpcLog(true);
//            SysPropUtils.set("persist.sys.mag.log","true");
//        }else{
//            intent.putExtra("KEY_OPT_CODE",0);
//            LinkConfig.getInstance().setMpcLog(false);
//            SysPropUtils.set("persist.sys.mag.log","false");
//        }
//        this.getActivity().sendBroadcast(intent);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_submit:
//                submit();
//                break;
//            case R.id.btn_reset:
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void submit() {
//        LogUtils.d(TAG, "submit enter...");
//        try {
//            /*//手动设置M网丢包率阀值
//            String mcwillRatePassValue = mEtMcwillRatePassValue.getText().toString().trim();
//            int mcwillPingPassRate = "".equals(mcwillRatePassValue) ? -1 : Integer.parseInt(mcwillRatePassValue);
//            LinkConfig.getInstance().setMcwillRatePassValue(mcwillPingPassRate);
//            LogUtils.d(TAG, "mcwill ping pass rate = " + LinkConfig.getInstance().getMcwillPingDelayPassValue());
//            //手动设置M网丢包率
//            String manualMcwillPacketLoss = mEtManualMcwillPacketLoss.getText().toString().trim();
//            int mcwillPacketLoss = "".equals(manualMcwillPacketLoss) ? -1 : Integer.parseInt(manualMcwillPacketLoss);
//            LinkConfig.getInstance().setManualMcwillPing(mcwillPacketLoss);
//            LogUtils.d(TAG, "manual mcwill ping packet loss = " + LinkConfig.getInstance().getManualMcwillPingDelay());
//            //手动设置M网定时测速间隔,默认60秒而非-1
//            String manualMcwillDetectPeriod = mEtMcwillDetectPeriod.getText().toString().trim();
//            int mcwillDetectPeriod = "".equals(manualMcwillDetectPeriod) ? (60 * 1000) : Integer.parseInt(manualMcwillDetectPeriod);
//            LinkConfig.getInstance().setMcwillSpeedDetectPeriod(mcwillDetectPeriod);
//            LogUtils.d(TAG, "manual mcwill detect period = " + LinkConfig.getInstance().getMcwillSpeedDetectPeriod());
//
//            //wifi丢包率阀值
//            String wifiRatePassValue = mEtWifiRatePassValue.getText().toString().trim();
//            int wifiPingPassRate = "".equals(wifiRatePassValue) ? -1 : Integer.parseInt(wifiRatePassValue);
//            LinkConfig.getInstance().setWifiPingDelayPassValue(wifiPingPassRate);
//            LogUtils.d(TAG, "wifi ping pass rate = " + LinkConfig.getInstance().getWifiPingDelayPassValue());
//            //手动设置wifi丢包率
//            String manualWifiPacketLoss = mEtManualWifiPacketLoss.getText().toString().trim();
//            int wifiPacketLoss = "".equals(manualWifiPacketLoss) ? -1 : Integer.parseInt(manualWifiPacketLoss);
//            LinkConfig.getInstance().setManualWifiPingDelay(wifiPacketLoss);
//            LogUtils.d(TAG, "manual wifi ping packet loss = " + LinkConfig.getInstance().getManualWifiPingDelay());
//
//            //G网丢包率阀值
//            String simRatePassValue = mEtSimRatePassValue.getText().toString().trim();
//            int simPingPassRate = "".equals(simRatePassValue) ? -1 : Integer.parseInt(simRatePassValue);
//            LinkConfig.getInstance().setSimRatePassValue(simPingPassRate);
//            LogUtils.d(TAG, "sim ping pass rate = " + LinkConfig.getInstance().getSimPingDelayPassValue());
//            //手动设置G网丢包率
//            String manualSimPacketLoss = mEtManualSimPacketLoss.getText().toString().trim();
//            int simPacketLoss = "".equals(manualSimPacketLoss) ? -1 : Integer.parseInt(manualSimPacketLoss);
//            LinkConfig.getInstance().setManualSimPing(simPacketLoss);
//            LogUtils.d(TAG, "manual sim ping packet loss = " + LinkConfig.getInstance().getManualSimPingDelay());*/
//
//            //多流环境Mag配置
//            String magIp = mEtMpMagIp.getText().toString().trim();
//            magIp = "".equals(magIp) ? (LinkConfig.getInstance().getMagIp()) : magIp;
//            String magDns = mEtMpMagDns.getText().toString().trim();
//            magDns = "".equals(magDns) ? (LinkConfig.getInstance().getMagDns()) : magDns;
//            String magDns2 = mEtMpMagDns2.getText().toString().trim();
//            magDns2 = "".equals(magDns2) ? (LinkConfig.getInstance().getMagDns2()) : magDns2;
//
//
//            if((!magIp.equals(LinkConfig.getInstance().getMagIp())) || (!magDns.equals(LinkConfig.getInstance().getMagDns())) || (!magDns2.equals(LinkConfig.getInstance().getMagDns2()))){
//                SysPropUtils.set("persist.sys.mag.ip",magIp);
//                SysPropUtils.set("persist.sys.mag.dns",magDns);
//                SysPropUtils.set("persist.sys.mag.dns2",magDns2);
//                LinkConfig.getInstance().setMagIp(magIp);
//                LogUtils.d(TAG, "mp mag ip = " + LinkConfig.getInstance().getMagIp());
//                LinkConfig.getInstance().setMagDns(magDns);
//                LinkConfig.getInstance().setMagDns2(magDns2);
//                LogUtils.d(TAG, "mp mag dns = " + LinkConfig.getInstance().getMagDns()+",dns2:"+LinkConfig.getInstance().getMagDns2());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
