package com.xiaoxiang.ioutside.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.StandardList;
import com.xiaoxiang.ioutside.activities.model.TimeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wakesy on 2016/8/19.
 */
public class RecyclerTimeAdapter extends RecyclerView.Adapter<RecyclerTimeAdapter.MyViewHolder> {
    private List<StandardList> standardLists = new ArrayList<>();
    private int index;//标识加载第几个规格的时间列表

    public void setStandardLists(List<StandardList> standardLists) {
        this.standardLists = standardLists;
        notifyDataSetChanged();
    }

    public List<StandardList> getStandardLists() {
        return standardLists;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standard_item_timechoose, null);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        List<TimeList> timeLists = standardLists.get(index).getTimeList();
        holder.activity_time.setText(timeLists.get(position).getStartDate());
        holder.activity_price.setText("¥" + standardLists.get(index).getDiscountPrice());
        holder.activity_remainNum.setText("还剩" + timeLists.get(position).getRemainNum() + "位");
        holder.activity_item_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onclick(v, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (standardLists != null && standardLists.size() > 0) {
            List<TimeList> timeLists = standardLists.get(index).getTimeList();
            if (timeLists != null && timeLists.size() > 0) {
                return standardLists.get(index).getTimeList().size();
            }

        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView activity_time;
        private TextView activity_price;
        private TextView activity_remainNum;
        private LinearLayout activity_item_recycler;

        public MyViewHolder(View view) {
            super(view);
            activity_time = (TextView) view.findViewById(R.id.activity_time1);
            activity_price = (TextView) view.findViewById(R.id.activity_price);
            activity_remainNum = (TextView) view.findViewById(R.id.activity_remainNumber);
            activity_item_recycler = (LinearLayout) view.findViewById(R.id.activity_item_recycler);
        }


    }

    //      自定义点击事件接口
    public interface OnItemClickListener {
        void onclick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
