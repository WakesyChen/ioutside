package com.xiaoxiang.ioutside.dynamic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.dynamic.model.TypeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/7/13,0013.
 */
public class DynamicHotAdapter extends RecyclerView.Adapter<DynamicHotAdapter.TagViewHolder> {
private List<TypeList> list=new ArrayList<>();

    //得到数组
    public List<TypeList> getDataSet() {
        return list;
    }

    //添加单个数据集
    public void addItem(List<TypeList> newList) {
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.dynamic_hot_textView.setText(list.get(position).getTypeName());
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_hot_item,parent,false);
        TagViewHolder holder=new TagViewHolder(view);
        return holder;
    }

    //48d8937be8daa99334eb29845ff245cd
    public class TagViewHolder extends RecyclerView.ViewHolder{
        public TextView dynamic_hot_textView;
        public TagViewHolder(View view) {
            super(view);
            dynamic_hot_textView=(TextView)view.findViewById(R.id.dynamic_hot_textView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;
}
