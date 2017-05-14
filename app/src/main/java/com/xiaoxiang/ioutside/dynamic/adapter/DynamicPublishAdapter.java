package com.xiaoxiang.ioutside.dynamic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/6/3,0003.
 */
public class DynamicPublishAdapter extends RecyclerView.Adapter<DynamicPublishAdapter.DynamicViewHolder> {
    private ArrayList<String> list=new ArrayList<>();
    public DynamicPublishAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(DynamicViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage("file://"+list.get(position),holder.imageView);
    }

    @Override
    public DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item, parent, false);
        DynamicViewHolder viewHolder = new DynamicViewHolder(view);
        return viewHolder;
    }

    public class DynamicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public DynamicViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.dynamic_item_image);
        }

    }
}
