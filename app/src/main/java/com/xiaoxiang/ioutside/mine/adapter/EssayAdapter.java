package com.xiaoxiang.ioutside.mine.adapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.mine.model.CollectedEssay;

import java.util.List;

/**
 * Created by 15119 on 2016/6/15.
 */
public class EssayAdapter extends CollectionAdapter<CollectedEssay> {

    @Override
    public void bindViewHolder(NormalViewHolder viewHolder, CollectedEssay data) {

        CollectionAdapter.ViewHolder holder = (CollectionAdapter.ViewHolder) viewHolder;

        ImageLoader.getInstance().displayImage(data.getPhoto(), holder.ivPhoto);
        ImageLoader.getInstance().displayImage(data.getSubjectPhoto(), holder.ivAvatar);
        holder.tvCountLiked.setText(String.valueOf(data.getLikedCount()));
        holder.tvCountComment.setText(String.valueOf(data.getCommentCount()));
        setLikedState(holder.ivLike, data.isLiked());
        holder.tvName.setText(data.getSubjectName());
        holder.tvTitle.setText(data.getTitle());
    }

    public EssayAdapter(List<CollectedEssay> datas) {
        super(datas);
    }
}
