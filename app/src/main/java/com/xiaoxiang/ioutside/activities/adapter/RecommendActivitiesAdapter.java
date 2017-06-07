package com.xiaoxiang.ioutside.activities.adapter;

import android.content.Context;
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
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;
import com.xiaoxiang.ioutside.mine.widget.IndicatorLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwenkun on 16/9/1.
 */
public class RecommendActivitiesAdapter extends PullAddMoreAdapter<Bean.RecommendActivitySubject.Data.ActivitySubject> {

    private List<Bean.Banner.Data.Item> mHeaderData;

    private OnBannerItemClickListener mOnBannerItemClickListener;

    private OnSectionClickListener mOnSectionClickListener;

    public RecommendActivitiesAdapter(List<Bean.Banner.Data.Item> banners,
                                      List<Bean.RecommendActivitySubject.Data.ActivitySubject> activitySubjects) {
        super(activitySubjects);
        mHeaderData = banners;
        setHasHeader(true);
    }

    public interface OnBannerItemClickListener {
        void onItemClick(BannerLayout b, int position);
    }

    public interface OnSectionClickListener {
        void onEditorChoiceSelected();
        void onTrainingSeleted();
    }

    public void setOnSectionClickListener(OnSectionClickListener l) {
        mOnSectionClickListener = l;
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener l) {
        mOnBannerItemClickListener = l;
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_activities, parent, false);
        return new NormalViewHolder(view, getOnItemClickListener());
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_activity_fragment, parent, false);
        return new HeaderViewHolder(view, getOnItemClickListener());
    }

    @Override
    protected void bindNormalViewHolder(PullAddMoreAdapter.NormalViewHolder holder, int position) {
        NormalViewHolder viewHolder = (NormalViewHolder) holder;
        Bean.RecommendActivitySubject.Data.ActivitySubject item = getDataSet().get(position);

        Context context = viewHolder.itemView.getContext();
        Glide.with(context).load(item.subjectPhoto).into(viewHolder.mIvLargePhoto);
        Glide.with(context).load(item.activityList.get(0).photo).into(viewHolder.mIvImage1);

        Glide.with(context).load(item.activityList.get(0).photo).into(viewHolder.mIvImage2);
        Glide.with(context).load(item.activityList.get(0).photo).into(viewHolder.mIvImage3);
        viewHolder.mIvImage2.setEnabled(false);
        viewHolder.mIvImage3.setEnabled(false);

        viewHolder.mTvText1.setText(item.activityList.get(0).subTitle);
//

    }

    @Override
    protected void bindHeaderViewHolder(PullAddMoreAdapter.HeaderViewHolder holder) {
        if (mHeaderData == null) return;
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

        if (viewHolder.mBanner == null) return;
        viewHolder.mBanner.setViewUrls(getBannerPhotos(mHeaderData));
        viewHolder.indicatorLayout.setupWithBanner(viewHolder.mBanner);
        viewHolder.mBanner.setOnBannerChangeListener(new BannerLayout.OnBannerChangeListener() {
            @Override
            public void onBannerScrolled(int position) {
                viewHolder.mTvTitle.setText(mHeaderData.get(position).title);
            }

            @Override
            public void onItemClick(int position) {
                if (mOnBannerItemClickListener != null) {
                    mOnBannerItemClickListener.onItemClick(viewHolder.mBanner, position);
                }
            }
        });
    }

    public void setHeaderData(List<Bean.Banner.Data.Item> headerData) {
        mHeaderData = headerData;
        notifyDataSetChanged();
    }

    private List<String> getBannerPhotos(List<Bean.Banner.Data.Item> banners) {
        List<String> result = new ArrayList<>();
        for (Bean.Banner.Data.Item item : banners) {
            result.add(item.photo);
        }
        return result;
    }

    public class HeaderViewHolder extends PullAddMoreAdapter.HeaderViewHolder implements View.OnClickListener{
        private BannerLayout mBanner;
        private TextView mTvTitle;
        private IndicatorLayout indicatorLayout;
        private ImageView mIvActivities;
        private ImageView mIvCertificate;

        private OnItemClickListener mOnItemClickListener;

        public HeaderViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            mOnItemClickListener = l;
            mBanner = (BannerLayout) itemView.findViewById(R.id.banner);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            indicatorLayout = (IndicatorLayout) itemView.findViewById(R.id.indicator_layout);
            mIvActivities = (ImageView) itemView.findViewById(R.id.iv_activities);
            mIvCertificate = (ImageView) itemView.findViewById(R.id.iv_certificate);

            mIvActivities.setOnClickListener(this);
            mIvCertificate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getLayoutPosition());

            if (v.getId() == R.id.iv_activities && mOnSectionClickListener != null) {
                mOnSectionClickListener.onEditorChoiceSelected();
            } else if (v.getId() == R.id.iv_certificate && mOnSectionClickListener != null) {
                mOnSectionClickListener.onTrainingSeleted();
            }
        }
    }

    public static class NormalViewHolder extends PullAddMoreAdapter.NormalViewHolder implements View.OnClickListener{

        private OnItemClickListener mOnItemClickListener;
        private ImageView mIvLargePhoto;
        private ImageView mIvImage1;
        private ImageView mIvImage2;
        private ImageView mIvImage3;
        private TextView mTvText1;
        private TextView mTvText2;
        private TextView mTvText3;

        public NormalViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            mOnItemClickListener = l;
            mIvLargePhoto = (ImageView) itemView.findViewById(R.id.iv_large_photo);
            mIvImage1 = (ImageView) itemView.findViewById(R.id.iv_first_Image);
            mIvImage2 = (ImageView) itemView.findViewById(R.id.iv_second_image);
            mIvImage3 = (ImageView) itemView.findViewById(R.id.iv_third_image);
            mTvText1 = (TextView) itemView.findViewById(R.id.tv_first_text);
            mTvText2 = (TextView) itemView.findViewById(R.id.tv_second_text);
            mTvText3 = (TextView) itemView.findViewById(R.id.tv_third_text);

            mTvText1.setOnClickListener(this);
            mTvText2.setOnClickListener(this);
            mTvText3.setOnClickListener(this);
            mIvLargePhoto.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

}
