package com.flyzebra.linkmanager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.flyzebra.linkmanager.fragment.FaultDiagnosisFragment;
//import com.flyzebra.linkmanager.fragment.LinkWatcherFragment;
//import com.flyzebra.linkmanager.fragment.ParamSheetFragment;

/**
 * @Author: __ Weiyi.Lee  liweiyi@cootf.com
 * @Package: _ com.octopus.linkmanager
 * @DESC: ____
 * @Time: ____ created at-2018-09-18 16:30
 */
public class LinkManager extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvLinkWatcher;
    private TextView mTvParamSheet;
    private TextView mTvFaultDiagnosis;
    private ViewPager mVpLinkData;
    private ImageView mIvIndicator;//浮标
//    private LinkWatcherFragment mLinkWatcherFragment;
//    private ParamSheetFragment mParamSheetFragment;
//    private FaultDiagnosisFragment mFaultDiagnosisFragment;
    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private int currentIndex;
    private int screenWidth;
    private int deviation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_sheet);

        initView();
        initListener();
        initIndicatorWidth();
    }

    private void initView() {
        mTvLinkWatcher = (TextView) findViewById(R.id.tv_link_watcher);
        mTvParamSheet = (TextView) findViewById(R.id.tv_param_sheet);
        mVpLinkData = (ViewPager) findViewById(R.id.vp_link_data);
        mIvIndicator = (ImageView) findViewById(R.id.iv_indicator);
        mTvFaultDiagnosis = (TextView)findViewById(R.id.tv_fault_diagnosis);
        deviation = dip2px(10);

//        mLinkWatcherFragment = new LinkWatcherFragment();
//        mParamSheetFragment = new ParamSheetFragment();
//        mFaultDiagnosisFragment = new FaultDiagnosisFragment();
//        mFragmentList.add(mLinkWatcherFragment);
//        mFragmentList.add(mParamSheetFragment);
//        mFragmentList.add(mFaultDiagnosisFragment);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
//        mVpLinkData.setAdapter(mFragmentAdapter);
//        mVpLinkData.setCurrentItem(0);
    }

    private void initListener() {
        mTvLinkWatcher.setOnClickListener(this);
        mTvParamSheet.setOnClickListener(this);
        mTvFaultDiagnosis.setOnClickListener(this);
        /**
         * ViewPager添加页面替换监听器
         */
        mVpLinkData.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIvIndicator.getLayoutParams();

                /*
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记2个页面,
                 * 从左到右分别为0,1
                 * 0->1; 1->0
                 */
                if (currentIndex == 0 && position == 0) {// 0->1
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 2) + currentIndex * (screenWidth / 2)) + deviation;

                } else if (currentIndex == 1 && position == 0) {// 1->0
                    lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 2) + currentIndex * (screenWidth / 2)) + deviation;
                }
                mIvIndicator.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTvLinkWatcher.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorWhite));
                        mTvParamSheet.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        mTvFaultDiagnosis.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        break;
                    case 1:
                        mTvLinkWatcher.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        mTvParamSheet.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorWhite));
                        mTvFaultDiagnosis.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        break;
                    case 2:
                        mTvLinkWatcher.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        mTvParamSheet.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorBlack));
                        mTvFaultDiagnosis.setTextColor(ContextCompat.getColor(LinkManager.this, R.color.colorWhite));
                        break;

                }
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_link_watcher:
                clickTitle(0, R.color.colorBlack, R.color.colorWhite, R.color.colorBlack);
                break;
            case R.id.tv_param_sheet:
                clickTitle(1, R.color.colorWhite, R.color.colorBlack, R.color.colorBlack);
                break;
            case R.id.tv_fault_diagnosis:
                clickTitle(2, R.color.colorBlack, R.color.colorBlack, R.color.colorWhite);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * @param viewPagerItem VP游标
     * @param colorStateOne 颜色状态1
     * @param colorStateTwo 颜色状态2
     */
    private void clickTitle(int viewPagerItem, int colorStateOne, int colorStateTwo, int colorStateThree) {
        mVpLinkData.setCurrentItem(viewPagerItem);
        mTvParamSheet.setTextColor(ContextCompat.getColor(this, colorStateOne));
        mTvLinkWatcher.setTextColor(ContextCompat.getColor(this, colorStateTwo));
        mTvFaultDiagnosis.setTextColor(ContextCompat.getColor(this, colorStateThree));
    }

    /**
     * 设置滑动条的宽度为屏幕的1/2(根据Tab的个数而定)
     */
    private void initIndicatorWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIvIndicator.getLayoutParams();
        lp.width = screenWidth / 2 - deviation * 2;
        mIvIndicator.setLayoutParams(lp);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = LinkManager.this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class FragmentAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
