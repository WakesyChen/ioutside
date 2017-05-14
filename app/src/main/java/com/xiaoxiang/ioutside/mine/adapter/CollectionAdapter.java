package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/6/15.
 */
public abstract class CollectionAdapter<T> extends PullAddMoreAdapter<T> {


    public CollectionAdapter(List<T> dataSet) {
        super(dataSet);
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection, parent, false);
        return new ViewHolder(v, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        T data = getDataSet().get(position);

        bindViewHolder(holder, data);
    }

    public abstract void bindViewHolder(NormalViewHolder holder, T data);



    protected void setLikedState(ImageView v, boolean isObserved) {

        if (isObserved) {
            v.setSelected(true);
        } else {
            v.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 0 : getDataSet().size();
    }

    public static class ViewHolder extends NormalViewHolder implements View.OnClickListener {

        public ImageView ivAvatar;
        public ImageView ivPhoto;
        public TextView tvName;
        public TextView tvTitle;
        public TextView tvCountLiked;
        public TextView tvCountComment;
        public ImageView ivLike;
        public View layHeader;
        public TextView tvTime;

        private OnItemClickListener mListener;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            mListener = listener;
            layHeader = itemView.findViewById(R.id.lay_header);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCountComment = (TextView) itemView.findViewById(R.id.tv_count_comment);
            tvCountLiked = (TextView) itemView.findViewById(R.id.tv_count_liked);
            ivLike = (ImageView) itemView.findViewById(R.id.iv_liked);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            //给 item 及其内控件添加点击事件
            itemView.setOnClickListener(this);
            ivAvatar.setOnClickListener(this);
            tvName.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(v, getLayoutPosition());
        }

    }


}
