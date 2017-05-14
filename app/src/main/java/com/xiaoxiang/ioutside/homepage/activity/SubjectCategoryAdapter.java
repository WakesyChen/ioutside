package com.xiaoxiang.ioutside.homepage.activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.fragment.SubMainFragment;
import com.xiaoxiang.ioutside.mine.adapter.BaseAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;

import java.util.List;

/**
 * Created by 15119 on 2016/7/16.
 */
public class SubjectCategoryAdapter extends BaseAdapter<SubMainFragment.SubjectCategoryItem, RecyclerView.ViewHolder> {

    private static final String TAG = "SubjectCategoryAdapter";

    public SubjectCategoryAdapter(List<SubMainFragment.SubjectCategoryItem> dataSet) {
        super(dataSet);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_category, parent, false);
        return new ViewHolder(v, getOnItemClickListener());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SubMainFragment.SubjectCategoryItem item = getDataSet().get(position);

        ViewHolder viewHolder = (ViewHolder) holder;

        Glide.with(viewHolder.itemView.getContext()).load(item.getDrawableResourceId()).into(viewHolder.rivCover);

        viewHolder.tvTitle.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 0 : getDataSet().size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = "ViewHolder";

        private ImageView rivCover;
        private TextView tvTitle;
        private OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            rivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(this);

            mOnItemClickListener = l;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) {
                Log.d(TAG, "--> " + "mOnItemClickListener is null");
                return;
            }
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
