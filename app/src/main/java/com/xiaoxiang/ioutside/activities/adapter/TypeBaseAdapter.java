package com.xiaoxiang.ioutside.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/19.
 */

//    ActivityDetail 下 popwindow里面的gridview的适配器
public class TypeBaseAdapter extends BaseAdapter {

    private List<String>typelist;
    private Context context;
    public TypeBaseAdapter(Context context) {
        this.context=context;

    }

    public void setTypelistData(List<String >typelist){
       this.typelist=typelist;
        notifyDataSetChanged();

    }
    public List<String> getTypelistData() {
        return typelist;
    }
    @Override
    public int getCount() {
        if (typelist==null) {
            return 0;
        }
        return typelist.size();
    }

    @Override
    public Object getItem(int position) {
        if (typelist==null&&typelist.size()==0) {
            return  null;
        }
        return typelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(R.layout.standard_item_typechoose,null);
        TextView textView= (TextView) view.findViewById(R.id.standard_item_name);
        textView.setText(typelist.get(position));

        return view;
    }
}
