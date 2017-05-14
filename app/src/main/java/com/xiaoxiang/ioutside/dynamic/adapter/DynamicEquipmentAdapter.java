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
 * Created by zhang on 2016/6/9,0009.
 */
public class DynamicEquipmentAdapter extends RecyclerView.Adapter<DynamicEquipmentAdapter.DynamicTypeViewHolder> {
    private ArrayList<TypeList> list=new ArrayList<>();

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
    public DynamicTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_equipment_item,parent,false);
        DynamicTypeViewHolder holder=new DynamicTypeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DynamicTypeViewHolder holder, final int position) {
        holder.dynamic_equipment_textView.setText(list.get(position).getTypeName());
    }


    public class DynamicTypeViewHolder extends RecyclerView.ViewHolder{
        public TextView dynamic_equipment_textView;
        public DynamicTypeViewHolder(View view){
            super(view);
            dynamic_equipment_textView=(TextView)view.findViewById(R.id.dynamic_equipment_textView);
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
