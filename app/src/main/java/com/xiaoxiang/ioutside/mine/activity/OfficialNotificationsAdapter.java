package com.xiaoxiang.ioutside.mine.activity;

import android.view.View;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.adapter.NotificationBaseAdapter;
import com.xiaoxiang.ioutside.mine.model.OfficialNotifications;

import java.util.List;

/**
 * Created by 15119 on 2016/7/24.
 */
public class OfficialNotificationsAdapter extends NotificationBaseAdapter<OfficialNotifications.DataBean.ListBean> {

    public OfficialNotificationsAdapter(List<OfficialNotifications.DataBean.ListBean> dataSet) {
        super(dataSet);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        OfficialNotifications.DataBean.ListBean item = getDataSet().get(position);

        holder.tvName.setText("官方通知");

        holder.tvQuote.setVisibility(View.VISIBLE);
        holder.tvQuote.setText(item.getMessageDescript());

        holder.tvTime.setText(item.getTime());

        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.ivPhoto);
        Glide.with(holder.itemView.getContext()).load(R.drawable.head_ele).into(holder.ivAvatar);

    }
}
