package com.xiaoxiang.ioutside.common.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.mine.adapter.CollectionAdapter;
import com.xiaoxiang.ioutside.mine.model.Recommend;

import java.util.List;

/**
 * Created by 15119 on 2016/7/14.
 */
public class RecommendAdapter extends CollectionAdapter <Recommend> {

    @Override
    public void bindViewHolder(NormalViewHolder viewHolder, Recommend data) {

        CollectionAdapter.ViewHolder holder = (CollectionAdapter.ViewHolder) viewHolder;

        holder.layHeader.setVisibility(View.GONE);
        Glide.with(holder.itemView.getContext()).load(data.getPhoto()).into(holder.ivPhoto);
        ImageLoader.getInstance().displayImage(data.getPhoto(), holder.ivPhoto);
        holder.tvTitle.setText(data.getTitle());
        holder.tvTime.setText(data.getRecommendTime());
        holder.tvCountComment.setText(String.valueOf(data.getCommentCount()));
        holder.tvCountLiked.setText(String.valueOf(data.getLikedCount()));
    }

    public RecommendAdapter (List<Recommend> dataSet) {
        super(dataSet);
    }

}
