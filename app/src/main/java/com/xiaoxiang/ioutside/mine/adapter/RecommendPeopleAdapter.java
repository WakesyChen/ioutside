package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.RecommendPeople;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15119 on 2016/6/29.
 */
public class RecommendPeopleAdapter extends BaseAdapter<RecommendPeople.DataBean.ListBean, RecyclerView.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private Map<Integer, Boolean> mSelectState = new HashMap<>();

    public RecommendPeopleAdapter(List<RecommendPeople.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_people, parent, false);

//        int widthSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
//        int heightSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
//
//        itemView.measure(widthSpec, heightSpec);
//
//        parent.getLayoutParams().height = itemView.getMeasuredHeight() * 2;
//        parent.requestLayout();

        return new ViewHolder(itemView, mOnItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendPeople.DataBean.ListBean item = getDataSet().get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        ImageLoader.getInstance().displayImage(item.getPhoto(), viewHolder.ivAvatar);
        viewHolder.tvName.setText(item.getName());
        if (mSelectState.containsKey(position) && mSelectState.get(position)) {
            viewHolder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivCheck.setVisibility(View.INVISIBLE);
        }

    }

    public Map<Integer, Boolean> getSelectState() {
        return mSelectState;
    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 0 : getDataSet().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivCheck;
        private ImageView ivAvatar;
        private OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mOnItemClickListener = listener;
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchSelectState(v);
                    mOnItemClickListener.onItemClick(v, getLayoutPosition());
                }
            });
        }

        private void switchSelectState(View v) {
            if (v.isSelected()) {
                v.setSelected(false);
            } else {
                v.setSelected(true);
            }
        }
    }

}
