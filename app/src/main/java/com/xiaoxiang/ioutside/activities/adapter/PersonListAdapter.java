package com.xiaoxiang.ioutside.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.PersonInfor;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/24.
 */
public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonHolder> {
        private List<PersonInfor> travelerInforList;
    private static final String TAG = "PersonListAdapter";
    public List<PersonInfor> getTravelerInforList() {
        return travelerInforList;
    }
    private RadioButton personlist_rbtn;
    public void setTravelerInforList(List<PersonInfor> travelerInforList) {
        this.travelerInforList = travelerInforList;
    }

    public void setPersonlistRbtn(RadioButton personlist_rbtn) {
        this.personlist_rbtn=personlist_rbtn;
    }
    public RadioButton getPersonlistRbtn() {
        return  personlist_rbtn;
    }

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.personlist_recycler_item,parent,false);
        PersonHolder holder=new PersonHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, final int position) {
        if (travelerInforList != null && travelerInforList.get(position) != null) {
//            Log.i(TAG, "onBindViewHolder: "+"name:"+travelerInforList.get(position).getName());
//            Log.i(TAG, "onBindViewHolder: "+"phone:"+travelerInforList.get(position).getPhone());
            setPersonlistRbtn(holder.personlist_rbtn);
            holder.personlist_name.setText(travelerInforList.get(position).getName() + "");
            holder.personlist_phone.setText(travelerInforList.get(position).getPhone()+"");
            holder.personlist_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onModifyClick(position);
                    }
                }
            });
            holder.personlist_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onItemclick(v,position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (travelerInforList != null) {
            return travelerInforList.size();
        } else {

            return 0;
        }
    }



    public class PersonHolder extends RecyclerView.ViewHolder {
        private RadioButton personlist_rbtn;
        private TextView personlist_name;
        private TextView personlist_phone;
        private ImageView personlist_modify;
        private LinearLayout personlist_item;
        public PersonHolder(View itemView) {
            super(itemView);
            personlist_rbtn= (RadioButton) itemView.findViewById(R.id.personlist_rbtn);
            personlist_name= (TextView) itemView.findViewById(R.id.personlist_name);
            personlist_phone= (TextView) itemView.findViewById(R.id.personlist_phone);
            personlist_modify= (ImageView) itemView.findViewById(R.id.personlist_modify);
            personlist_item= (LinearLayout) itemView.findViewById(R.id.personlist_item);
        }
    }

    public interface OnItemClickListener {
        void onItemclick(View view,int position);
        void onModifyClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;

    }
    private OnItemClickListener onItemClickListener;

}
