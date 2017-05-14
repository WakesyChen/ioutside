package com.xiaoxiang.ioutside.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.model.Fan;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/7/14.
 */
public class UserPreviewAdapter extends PullAddMoreAdapter<Fan> {

    private OnItemClickListener mListener;

    public UserPreviewAdapter(List<Fan> dataSet) {
        super(dataSet);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_preview, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        Fan item = getDataSet().get(position);

        Glide.with(viewHolder.itemView.getContext()).load(item.getPhoto()).into(viewHolder.civAvatar);
        viewHolder.tvName.setText(item.getName());

        if ('w' == item.getSex()) {
            viewHolder.ivGender.setImageResource(R.drawable.src_ic_female);
        } else if ('m' == item.getSex()) {
            viewHolder.ivGender.setImageResource(R.drawable.src_ic_male);
        } else {
            viewHolder.ivGender.setImageBitmap(null);
        }
        viewHolder.tvLevel.setText(viewHolder.tvLevel.getContext().getString(R.string.tv_level, item.getLevel()));
    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 0 : getDataSet().size();
    }

    private static class ViewHolder extends NormalViewHolder implements View.OnClickListener {

        private OnItemClickListener l;

        private ImageView civAvatar;
        private TextView tvName;
        private TextView tvLevel;
        private ImageView ivGender;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            l = listener;
            civAvatar = (ImageView) itemView.findViewById(R.id.civ_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
            ivGender = (ImageView) itemView.findViewById(R.id.iv_gender);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (l == null) return;
            switch (v.getId()) {
                case R.id.item_user_preview :
                    l.onItemClick(v, getLayoutPosition());
                    break;
            }
        }
    }

}
