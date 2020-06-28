package com.flyzebra.linkmanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyzebra.linkmanager.fragment.FaultDiagnosisFragment;
import com.flyzebra.linkmanager.fragment.LinkWatcherFragment;
import com.flyzebra.linkmanager.fragment.ParamSheetFragment;
import com.flyzebra.linkmanager.receiver.MainReceiver;
import com.flyzebra.linkmanager.view.FlyTableView;
import com.flyzebra.utils.FlyLog;

import java.util.ArrayList;
import java.util.List;

import xinwei.com.mpapp.aidl.IServiceAidl;

public class LinkActivity extends AppCompatActivity implements FlyTableView.OnItemClickListener, ViewPager.OnPageChangeListener {
    private String titles[] = new String[]{"链路监控状态", "参数表", "错误诊断"};
    private List<Fragment> mFMlist = new ArrayList<>();
    private MyPagerAdpter myPagerAdpter;
    private FlyTableView mFlyTableView;
    private ViewPager mViewPager;
    private IServiceAidl mMpService = null;

    ServiceConnection mMpServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FlyLog.d( "MpService onServiceConnected.");
            mMpService = IServiceAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            FlyLog.d( "MpService onServiceDisconnected.");
            mMpService = null;
            FlyLog.d("service is disconnected, reconnect mpapp service.");
            try{
                bindMpService();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindMpService();

        mFlyTableView = findViewById(R.id.ac_main_ftab);
        mViewPager = findViewById(R.id.ac_main_vp);

        mFMlist.add(new LinkWatcherFragment());
        mFMlist.add(new ParamSheetFragment());
        mFMlist.add(new FaultDiagnosisFragment());

        myPagerAdpter = new MyPagerAdpter(getSupportFragmentManager(),1);
        mViewPager.setAdapter(myPagerAdpter);
        mViewPager.addOnPageChangeListener(this);

        mFlyTableView.setTitles(titles);
        mFlyTableView.setFocusPos(0);
        mFlyTableView.setOnItemClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mMpServiceConnection);
    }

    @Override
    public void onItemClick(View v, int pos) {
        pos = Math.max(pos,0);
        pos = Math.min(pos,mFMlist.size()-1);
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mFlyTableView.setFocusPos(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdpter extends FragmentStatePagerAdapter{
        public MyPagerAdpter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            return mFMlist.get(position);
        }

        @Override
        public int getCount() {
            return mFMlist==null?0:mFMlist.size();
        }

    }

    private void bindMpService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("xinwei.com.mpapp", "xinwei.com.mpapp.MainService"));
        bindService(intent, mMpServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void openMultipleStreams(){
        if(mMpService==null){
            FlyLog.e("openMultipleStreams ---> MP service is null , return !!!");
            return;
        }
        try{
            mMpService.openMultipleStreams(null,0,null,0,null,null,null);
        }catch (RemoteException e){
            FlyLog.d("openMultipleStreams failed , cause : " + e.getMessage());
        }
    }

    public void closeMultipleStreams(){
        if(mMpService==null){
            FlyLog.e("closeMultipleStreams ---> MP service is null , return !!!");
            return;
        }
        try{
            mMpService.closeMultipleStreams();
        }catch (RemoteException e){
            FlyLog.d("closeMultipleStreams failed , cause : " + e.getMessage());
        }
    }

    private MainReceiver myReceiver = new MainReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            FlyLog.d("onReceive");
        }
    };
}
