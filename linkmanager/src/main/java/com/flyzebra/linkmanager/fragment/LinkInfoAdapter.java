//package com.flyzebra.linkmanager.fragment;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.Space;
//import android.widget.TextView;
//
//
//import java.util.List;
//
//import xinwei.com.mpapp.R;
//
///**
// * @Author: __ Weiyi.Lee  liweiyi@cootf.com
// * @Package: _ com.flyzebra.linkmanager.fragment
// * @DESC: ____
// * @Time: ____ created at-2018/9/25 11:16
// */
//public class LinkInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private static String TAG = "LinkInfoAdapter";
//    private Context mContext;
//    private String[] mProperties;
//    private List<String> mValueList;
//    private static final int MCWILL_INFO_START = 10; //modify by wangyongya 20190417 for MP : add item (MP状态)
//    private static final int SIM_INFO_START = 20;//modify by wangyongya 20190417 for MP : add item (MP状态)
//
//
//    public LinkInfoAdapter(Context context, List<String> valueList) {
//        this.mContext = context;
//        this.mValueList = valueList;
//
//        if (mProperties == null) {
//            mProperties = context.getResources().getStringArray(R.array.link_info_array);
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_link_watcher, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof MyViewHolder) {
//            final MyViewHolder myViewHolder = (MyViewHolder) holder;
//            if (mValueList != null) {
//                myViewHolder.mTvName.setText(mProperties[position]);
//                myViewHolder.mTvProperty.setText(mValueList.get(position));
//                //分割
//                if (position == MCWILL_INFO_START - 1 || position == SIM_INFO_START - 1) {
//                    myViewHolder.mSpaceDivider.setVisibility(View.VISIBLE);
//                } else {
//                    myViewHolder.mSpaceDivider.setVisibility(View.GONE);
//                }
//                //第一格的线
//                if (position == MCWILL_INFO_START || position == SIM_INFO_START) {
//                    myViewHolder.mViewHead.setVisibility(View.VISIBLE);
//                } else {
//                    myViewHolder.mViewHead.setVisibility(View.GONE);
//                }
//            }
//        }
//    }
//
//    /**
//     * 设置属性值
//     *
//     * @param values
//     */
//    public void setPropertyValues(List<String> values) {
//        this.mValueList = values;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mProperties.length;
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        private View mViewHead;
//        private TextView mTvName, mTvProperty;
//        private CheckBox mCbIsShow;
//        private Space mSpaceDivider;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
//            mTvProperty = (TextView) itemView.findViewById(R.id.tv_property);
//            mCbIsShow = (CheckBox) itemView.findViewById(R.id.cb_is_show);
//            mSpaceDivider = (Space) itemView.findViewById(R.id.space_divider);
//            mViewHead = itemView.findViewById(R.id.view_head);
//        }
//    }
//}