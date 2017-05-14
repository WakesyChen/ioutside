package com.xiaoxiang.ioutside.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.retrofit.Bean;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by lwenkun on 16/9/6.
 */
public class FilterActivitiesAdapter extends PullAddMoreAdapter<Bean.FilterActivities.Data.Item> {

    public FilterActivitiesAdapter(List<Bean.FilterActivities.Data.Item> dataSet) {
        super(dataSet);
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activities_or_training, parent, false);
        return new NormalViewHolder(itemView, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(PullAddMoreAdapter.NormalViewHolder holder, int position) {
        NormalViewHolder viewHolder = (NormalViewHolder) holder;
        Bean.FilterActivities.Data.Item item = getDataSet().get(position);

        Glide.with(viewHolder.itemView.getContext()).load(item.photo).into(viewHolder.ivPhoto);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvWhere.setText(item.startPlace);
        viewHolder.tvDate.setText(item.startDate);
        viewHolder.tvPrice.setText(String.valueOf(viewHolder.tvPrice.getContext().getString(R.string.price, item.price)));
    }

    public static class NormalViewHolder extends  PullAddMoreAdapter.NormalViewHolder implements View.OnClickListener{

        private ImageView ivPhoto;
        private TextView tvTitle;
        private TextView tvWhere;
        private TextView tvDate;
        private TextView tvPrice;
        private OnItemClickListener mOnItemClickListener;

        public NormalViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            mOnItemClickListener = l;
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvWhere = (TextView) itemView.findViewById(R.id.tv_where);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
