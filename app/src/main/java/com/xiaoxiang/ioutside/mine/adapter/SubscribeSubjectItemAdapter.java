package com.xiaoxiang.ioutside.mine.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.homepage.model.Subject;

import java.util.List;

/**
 * Created by 15119 on 2016/7/20.
 */
public class SubscribeSubjectItemAdapter extends BaseSubjectItemAdapter {

    public SubscribeSubjectItemAdapter(List<Subject> mylist) {
        super(mylist);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        Subject subject = getDataSet().get(position);
        holder.tvName.setText(subject.getTitle());
        holder.introduction.setText(subject.getIntroduction());
        holder.ivEnter.setVisibility(View.GONE);
        Glide.with(holder.itemView.getContext()).load(subject.getPhoto()).into(holder.ivPhoto);
        holder.ivSubscribe.setVisibility(View.VISIBLE);
        holder.ivSubscribe.setSelected(subject.isObserved());

    }
}
