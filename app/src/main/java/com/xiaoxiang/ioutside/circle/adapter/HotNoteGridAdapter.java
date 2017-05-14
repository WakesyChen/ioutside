package com.xiaoxiang.ioutside.circle.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;

import java.util.List;


/**
 * Created by Wakesy on 2016/9/25.
 */
public class HotNoteGridAdapter extends BaseAdapter{

    private List<String> datalist;

    public HotNoteGridAdapter(List<String> datalist) {
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size()>6?6:datalist.size();//最多加载6张
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hotnote_grid_item,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.hotnote_grid_item_img);
        Glide.with(parent.getContext()).load(datalist.get(position)).into(imageView);
        return view;
    }


}
