package com.xiaoxiang.ioutside.mine.adapter;

import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.model.CommentedItems;

import java.util.List;

/**
 * Created by 15119 on 2016/7/24.
 */
public class CommentedItemsAdapter extends NotificationBaseAdapter<CommentedItems.DataBean.ListBean> {

    public CommentedItemsAdapter(List<CommentedItems.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        CommentedItems.DataBean.ListBean item = getDataSet().get(position);

        holder.tvQuote.setVisibility(View.VISIBLE);

        holder.tvName.setText(item.getUserName());
        holder.tvQuote.setText(item.getContent());
        holder.tvTime.setText(item.getTime());
        holder.tvWhat.setText(item.getMessageDescript());
        if (TextUtils.isEmpty(item.getOriginContent())) {
            holder.llOriginalComment.setVisibility(View.VISIBLE);
            holder.tvOriginalComment.setText(item.getOriginContent());
        } else {
            holder.llOriginalComment.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView.getContext()).load(item.getUserPhoto()).into(holder.ivAvatar);
        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.ivPhoto);
    }

}
