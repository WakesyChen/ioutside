package com.xiaoxiang.ioutside.dynamic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.dynamic.model.LHB;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/8/8.
 */
public class LHBAdapter extends PullAddMoreAdapter<LHB.DataBean.ListBean> {

    public LHBAdapter(List<LHB.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lhb, parent, false);
        return new ViewHolder(itemView, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        LHB.DataBean.ListBean item = getDataSet().get(position);

        viewHolder.tvName.setText(item.getName());
        viewHolder.tvExperience.setText(item.getExperiences());
        viewHolder.ivObserve.setSelected(item.isObserved());

        Glide.with(viewHolder.itemView.getContext()).load(item.getPhoto()).into(viewHolder.ivAvatar);
    }

    static class ViewHolder extends NormalViewHolder implements View.OnClickListener{

        OnItemClickListener mListener;

        ImageView ivAvatar;
        ImageView ivObserve;
        TextView tvName;
        TextView tvExperience;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            ivAvatar = (ImageView) itemView.findViewById(R.id.civ_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvExperience = (TextView) itemView.findViewById(R.id.tv_experience);
            ivObserve = (ImageView) itemView.findViewById(R.id.iv_observe);

            itemView.setOnClickListener(this);
            ivObserve.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}
