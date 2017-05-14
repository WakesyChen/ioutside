package com.xiaoxiang.ioutside.mine.adapter;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.model.CollectedItems;

import java.util.List;

/**
 * Created by 15119 on 2016/7/23.
 */
public class CollectedItemsAdapter extends NotificationBaseAdapter<CollectedItems.DataBean.ListBean> {

    public CollectedItemsAdapter(List<CollectedItems.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        CollectedItems.DataBean.ListBean item = getDataSet().get(position);

//        holder.tvOriginalComment.setVisibility(View.GONE);
//        holder.llOriginalComment.setVisibility(View.GONE);

        holder.tvName.setText(item.getUserName());
        holder.tvTime.setText(item.getTime());
        holder.tvWhat.setText(item.getMessageDescript());
        Glide.with(holder.itemView.getContext()).load(item.getUserPhoto()).into(holder.ivAvatar);
        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.ivPhoto);

    }


}
