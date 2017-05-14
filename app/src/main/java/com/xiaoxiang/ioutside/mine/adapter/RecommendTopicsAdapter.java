package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15119 on 2016/6/28.
 */
public class RecommendTopicsAdapter extends BaseAdapter<RecommendTopic.DataBean.ListBean, RecyclerView.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private Map<Integer, Boolean> mSelectState = new HashMap<>();

    public RecommendTopicsAdapter(List<RecommendTopic.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public Map<Integer, Boolean> getSelectState() {
        return mSelectState;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_topics, parent, false);

        return new ViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RecommendTopic.DataBean.ListBean item = getDataSet().get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if (position % 3 == 0) {
            ((RelativeLayout)viewHolder.itemView).setGravity(Gravity.LEFT);
        } else if (position % 3 == 2) {
            ((RelativeLayout)viewHolder.itemView).setGravity(Gravity.RIGHT);
        }

        ImageLoader.getInstance().displayImage(item.getPhoto(), viewHolder.ivCover);
        viewHolder.tvTitle.setText(item.getTitle());

        if (mSelectState.containsKey(position) && mSelectState.get(position)) {
            viewHolder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        ImageView ivCheck;
        TextView tvTitle;

        public ViewHolder(final View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchSelectState(v);
                    onItemClickListener.onItemClick(v, getLayoutPosition());
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
