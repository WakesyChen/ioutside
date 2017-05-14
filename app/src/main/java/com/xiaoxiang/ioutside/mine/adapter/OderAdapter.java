package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.OderEntry;

import java.util.List;

/**
 * Created by lwenkun on 16/8/29.
 */
public class OderAdapter extends BaseAdapter<OderEntry, OderAdapter.ViewHolder> {

    public OderAdapter(List<OderEntry> dataSet) {
        super(dataSet);
    }

    @Override
    public OderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oder_entry, parent, false);
        return new ViewHolder(itemView, getOnItemClickListener());
    }

    @Override
    public void onBindViewHolder(OderAdapter.ViewHolder holder, int position) {
        OderEntry item = getDataSet().get(position);

        holder.mIvImage.setImageResource(item.resourceId);
        holder.mTvText.setText(item.text);
    }

    @Override
    public int getItemCount() {
        return getDataSet() == null ? 0 : getDataSet().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTvText;
        private ImageView mIvImage;
        private OnItemClickListener mListener;

        public ViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);

            mListener = l;
            mTvText = (TextView) itemView.findViewById(R.id.tv_text);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}
