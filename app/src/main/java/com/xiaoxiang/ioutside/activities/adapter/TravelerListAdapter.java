package com.xiaoxiang.ioutside.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.TravelerInfor;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/24.
 */
public class TravelerListAdapter extends BaseAdapter {
    private List<TravelerInfor> travelerList;


    public List<TravelerInfor> getTravelerList() {
        return travelerList;
    }

    public void setTravelerList(List<TravelerInfor> travelerList) {
        this.travelerList = travelerList;
    }
//      添加出行人
    public void addItem(TravelerInfor traveler) {
        travelerList.add(traveler);
        notifyDataSetChanged();
    }
//    删除联系人
    public void deleteItem(int position) {
        travelerList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return travelerList.size();
    }

    @Override
    public Object getItem(int position) {
        return travelerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 holder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_person, null);
            holder = new ViewHolder2(convertView);
            convertView.setTag(holder);
        } else {

            holder= (ViewHolder2) convertView.getTag();
        }
        holder.name.setText(travelerList.get(position).getName());
        holder.phone.setText("电话 "+travelerList.get(position).getPhone());
        holder.personID.setText("身份证 "+travelerList.get(position).getPersonID());

        return convertView;
    }

    public class ViewHolder2 {
        TextView name;
        TextView phone;
        TextView personID;
        public ViewHolder2(View view ) {
            name= (TextView) view.findViewById(R.id.traveler_name);
            phone= (TextView) view.findViewById(R.id.traveler_phone);
            personID= (TextView) view.findViewById(R.id.traveler_personID);

        }




    }

}
