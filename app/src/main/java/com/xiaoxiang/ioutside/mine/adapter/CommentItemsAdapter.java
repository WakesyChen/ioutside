package com.xiaoxiang.ioutside.mine.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.model.CommentItems;

import java.util.List;

/**
 * Created by 15119 on 2016/7/24.
 */
public class CommentItemsAdapter extends NotificationBaseAdapter<CommentItems.DataBean.ListBean> {

    public CommentItemsAdapter(List<CommentItems.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        CommentItems.DataBean.ListBean item = getDataSet().get(position);

        holder.tvAction.setVisibility(View.VISIBLE);
        holder.tvQuote.setVisibility(View.VISIBLE);

        holder.tvName.setVisibility(View.INVISIBLE);
        holder.tvQuote.setText(item.getContent());
        holder.tvTime.setText(item.getTime());

        if (item.getType() == 1) {
            holder.tvWhat.setText("文章");
        } else {
            holder.tvWhat.setText("动态");
        }

        Glide.with(holder.itemView.getContext()).load(item.getUserPhoto()).into(holder.ivAvatar);
        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.ivPhoto);

    }
}
