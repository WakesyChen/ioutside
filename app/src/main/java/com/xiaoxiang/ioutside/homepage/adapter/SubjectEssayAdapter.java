package com.xiaoxiang.ioutside.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.model.SubjectEssay;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/7/12.
 */
public class SubjectEssayAdapter extends PullAddMoreAdapter<SubjectEssay> {

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SubjectEssayAdapter(List<SubjectEssay> dataSet) {
        super(dataSet);
    }

    public static class ViewHolder extends NormalViewHolder implements View.OnClickListener {

        private ImageView ivPhoto;
        private TextView tvTitleEssay;
        private TextView tvTime;
        private TextView tvLikeNum;
        private TextView tvCommentNum;

        private OnItemClickListener mListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            mListener = onItemClickListener;

            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvTitleEssay = (TextView) itemView.findViewById(R.id.tv_title_essay);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvLikeNum = (TextView) itemView.findViewById(R.id.tv_like_num);
            tvCommentNum = (TextView) itemView.findViewById(R.id.tv_comment_num);

            //监听点击 item 的事件
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_subject_essay, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        SubjectEssay item = getDataSet().get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(viewHolder.itemView.getContext()).load(item.getPhoto()).into(viewHolder.ivPhoto);
        viewHolder.tvTitleEssay.setText(item.getTitle());
        viewHolder.tvCommentNum.setText(String.valueOf(item.getCommentCount()));
        viewHolder.tvLikeNum.setText(String.valueOf(item.getLikedCount()));
        viewHolder.tvTime.setText(item.getRecommendTime());
    }
}
