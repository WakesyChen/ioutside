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
 * Created by Wakesy on 2016/8/14.
 */
public class DynamicMultiple extends RecyclerView.Adapter<DynamicMultiple.MultipleViewholder>{
    private List<TypeList>list=new ArrayList<>();

    public List<TypeList> getDataSet (){
        return  list;

    }
    public void addItem(TypeList typeList) {
        list.add(typeList);
        notifyDataSetChanged();
    }
    @Override
    public MultipleViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_multiple_itemtag,null);

        MultipleViewholder viewholder=new MultipleViewholder(view);
        return viewholder;
    }

    public void onBindViewHolder(MultipleViewholder holder, int position) {
            holder.dynamic_multiple_textview.setText(list.get(position).getTypeName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MultipleViewholder extends RecyclerView.ViewHolder{
        private TextView  dynamic_multiple_textview;

        public MultipleViewholder(final View itemView) {
            super(itemView);
            dynamic_multiple_textview = (TextView) itemView.findViewById(R.id.dynamic_multiple_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListerner!=null) {
                        onItemClickListerner.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }


    public interface OnItemClickListerner {

       void onItemClick(View view ,int position);
    }

    public void setOnClickListerner(OnItemClickListerner onItemClickListerner){
        this.onItemClickListerner=onItemClickListerner;
    }
    private OnItemClickListerner onItemClickListerner;
}

