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
 * Created by lwenkun on 16/9/5.
 */
public class SubjectActivitiesAdapter extends PullAddMoreAdapter<Bean.SubjectActivities.Data.Item> {

    private Header mHeader;

    public SubjectActivitiesAdapter(Header header, List<Bean.SubjectActivities.Data.Item> dataSet) {
        super(dataSet);
        mHeader = header;
        setHasHeader(true);
    }

    public static class Header {
        public Header(String photo, String text) {
            this.photo = photo;
            this.text = text;
        }
        private String photo;
        private String text;
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_activity, parent, false);
        return new NormalViewHolder(view, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(PullAddMoreAdapter.NormalViewHolder holder, int position) {
        NormalViewHolder viewHolder = (NormalViewHolder) holder;

        Bean.SubjectActivities.Data.Item item = getDataSet().get(position);

        viewHolder.mTvTitle.setText(item.subTitle);
        viewHolder.mTvSubTitle.setText(item.title);
        viewHolder.mTvDescription.setText(item.content);
        viewHolder.mTvTime.setText(item.startDate);
        viewHolder.mTvWhere.setText(item.startPlace);
        viewHolder.mTvTag.setText(item.subType);

        Glide.with(viewHolder.itemView.getContext()).load(item.photo).into(viewHolder.mIvPhoto);
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_subject_activities, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected void bindHeaderViewHolder(PullAddMoreAdapter.HeaderViewHolder holder) {
        if (mHeader == null) return;

        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

        Glide.with(viewHolder.itemView.getContext()).load(mHeader.photo).into(viewHolder.mIvPhoto);
        viewHolder.mTvDescription.setText(mHeader.text);

    }

    public static class HeaderViewHolder extends PullAddMoreAdapter.HeaderViewHolder {

        private ImageView mIvPhoto;
        private TextView mTvDescription;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvDescription = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }

    public void setHeader(Header header) {
        mHeader = header;
        notifyDataSetChanged();
    }

    public static class NormalViewHolder extends PullAddMoreAdapter.NormalViewHolder implements View.OnClickListener {

        private TextView mTvTitle;
        private ImageView mIvPhoto;
        private TextView mTvDescription;
        private TextView mTvSubTitle;
        private TextView mTvWhere;
        private TextView mTvTime;
        private TextView mTvTag;

        private OnItemClickListener mOnItemClickListener;

        public NormalViewHolder(View itemView, OnItemClickListener l) {

            super(itemView);

            mOnItemClickListener = l;
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            mTvWhere = (TextView) itemView.findViewById(R.id.tv_where);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvTag = (TextView) itemView.findViewById(R.id.tv_tag);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }




}
