package com.xiaoxiang.ioutside.mine.adapter;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.model.CollectedFootPrint;

import java.util.List;


/**
 * Created by 15119 on 2016/6/16.
 */
public class DiscoveryAdapter extends CollectionAdapter<CollectedFootPrint> {

    @Override
    public void bindViewHolder(NormalViewHolder viewHolder, CollectedFootPrint data) {

        CollectionAdapter.ViewHolder holder = (CollectionAdapter.ViewHolder) viewHolder;

        Glide.with(holder.itemView.getContext()).load(data.getPhotoList()[0]).into(holder.ivPhoto);
        Glide.with(holder.itemView.getContext()).load(data.getUserPhoto()).into(holder.ivAvatar);

        holder.tvCountLiked.setText(String.valueOf(data.getLikedCount()));
        holder.tvCountComment.setText(String.valueOf(data.getCommentCount()));
        setLikedState(holder.ivLike, data.isLiked());
        holder.tvName.setText(data.getUserName());
        holder.tvTitle.setText(data.getThoughts());
    }

    public DiscoveryAdapter(List<CollectedFootPrint> datas) {
        super(datas);
    }

}
