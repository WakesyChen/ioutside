package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by 15119 on 2016/6/15.
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> dataSet;

    private OnItemClickListener mListener;

    public BaseAdapter(List<T> dataSet) {
        this.dataSet = dataSet;
    }

    public void addItem(T item) {
        dataSet.add(item);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    public void addItems(List<T> items) {
        if (dataSet != null && items != null)
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItemToHead(T item) {
        if (dataSet != null && item != null)
        dataSet.add(0, item);
        notifyDataSetChanged();
    }

    public void addItemsToHead(List<T> items) {
        if (dataSet != null && items != null)
        dataSet.addAll(0, items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (dataSet != null)
        dataSet.remove(position);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        if (dataSet != null && item != null)
        dataSet.remove(item);
        notifyDataSetChanged();
    }

    public void clear() {
        if (dataSet != null)
        dataSet.clear();
        notifyDataSetChanged();
    }

    public void replaceItems(List<T> dataSet) {
        if (this.dataSet != null && dataSet != null)
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public List<T> getDataSet() {
        return dataSet;
    }

}
