package com.xiaoxiang.ioutside.mine.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.homepage.model.Subject;

import java.util.List;

/**
 * Created by 15119 on 2016/7/20.
 */
public class ArrowSubjectItemAdapter extends BaseSubjectItemAdapter {

    public ArrowSubjectItemAdapter(List<Subject> mylist) {
        super(mylist);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        Subject mySub = getDataSet().get(position);
        holder.tvName.setText(mySub.getTitle());
        holder.ivEnter.setVisibility(View.VISIBLE);
        holder.ivSubscribe.setVisibility(View.GONE);
        holder.introduction.setText(mySub.getIntroduction());
        Glide.with(holder.itemView.getContext()).load(mySub.getPhoto()).into(holder.ivPhoto);
    }
}
