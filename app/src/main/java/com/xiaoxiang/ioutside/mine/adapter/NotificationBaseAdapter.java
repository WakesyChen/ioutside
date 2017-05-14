package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

import java.util.List;

/**
 * Created by 15119 on 2016/7/23.
 */
public abstract class NotificationBaseAdapter<T> extends BaseAdapter<T, RecyclerView.ViewHolder> {

    public NotificationBaseAdapter(List<T> dataSet) {
        super(dataSet);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_aio, parent, false);
        return new ViewHolder(itemView, getOnItemClickListener());
    }

    public abstract void bindViewHolder(NotificationBaseAdapter.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolder((NotificationBaseAdapter.ViewHolder)holder, position);
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public OnItemClickListener mListener;
        public LinearLayout llOriginalComment;
        public ImageView ivAvatar;

        public TextView tvOriginalComment;
        public TextView tvWhat;
        public TextView tvQuote;
        public ImageView ivPhoto;
        public TextView tvTime;
        public TextView tvName;
        public TextView tvAction;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            mListener= listener;
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvWhat = (TextView) itemView.findViewById(R.id.tv_what);

            llOriginalComment = (LinearLayout) itemView.findViewById(R.id.ll_original_comment);
            tvOriginalComment = (TextView) itemView.findViewById(R.id.tv_original_comment);
            tvQuote = (TextView) itemView.findViewById(R.id.tv_quote);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAction = (TextView) itemView.findViewById(R.id.tv_action);

            ivAvatar.setOnClickListener(this);
            tvName.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}
