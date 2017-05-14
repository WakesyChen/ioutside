package com.xiaoxiang.ioutside.mine.adapter;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.model.LikedItems;

import java.util.List;

/**
 * Created by 15119 on 2016/7/23.
 */
public class LikedItemsAdapter extends NotificationBaseAdapter<LikedItems.DataBean.ListBean> {

    public LikedItemsAdapter(List<LikedItems.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        LikedItems.DataBean.ListBean item = getDataSet().get(position);

        holder.tvName.setText(item.getUserName());
        holder.tvTime.setText(item.getTime());

//        if (TextUtils.isEmpty(item.getMessageDescript().trim()))
//        holder.tvOriginalComment.setText(item.getMessageDescript());
        holder.tvWhat.setText(item.getMessageDescript());
//        holder.tvWho.setText(item.getUserName());

        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.ivPhoto);
        Glide.with(holder.itemView.getContext()).load(item.getUserPhoto()).into(holder.ivAvatar);

    }
}
