package com.xiaoxiang.ioutside.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.circle.model.CircleType;
import com.xiaoxiang.ioutside.circle.model.HotNotes;
import com.xiaoxiang.ioutside.common.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wakesy on 2016/9/25.
 */
public class CircleTypeAdapter extends RecyclerView.Adapter {
    private List<CircleType.ListBean> datalist;
    public final static int TYPE_NORMAL=0;
    public final static int TYPE_FOOTER=1;
    private Context context;
    private int circleId=1;
    private static final String TAG = "CircleTypeAdapter";
    public CircleTypeAdapter() {
        datalist=new ArrayList<>();
    }

    public List<CircleType.ListBean> getData() {
        return datalist;
    }

    public void setData(List<CircleType.ListBean> datas) {
        datalist=datas;
        notifyDataSetChanged();
    }
    public void clearData(){
        datalist.clear();
        notifyDataSetChanged();
    }

    public void addData(List<CircleType.ListBean> datas) {
            addData(0,datas);

    }
    public void addData(int position,List<CircleType.ListBean> datas) {
        if (datas!=null&&datas.size()>0) {
            datalist.addAll(position,datas);
            notifyDataSetChanged();
        }

    }



    @Override
    public int getItemCount() {

        return (datalist != null &&datalist.size()>0)? datalist.size()+1:0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1== getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return  TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == TYPE_NORMAL) {

            View view = LayoutInflater.from(context).inflate(R.layout.circle_type_item, parent, false);
            return new NormalViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.circle_bigv_recy_footer, parent, false);
            return new FooterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            if (datalist != null && datalist.size() > 0) {
                Glide.with(context).load(datalist.get(position).getPhoto()).into( ((NormalViewHolder) holder).circle_type_itemImg);
                ((NormalViewHolder) holder).circle_type_itemTitle.setText(datalist.get(position).getTitle());
                ((NormalViewHolder) holder).circle_type_itemIntroduce.setText(datalist.get(position).getIntroduction());

                ((NormalViewHolder) holder).circle_type_itemJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null) {
                            onItemClickListener.onJoinClick(datalist.get(position).getId());
                        }

                    }
                });

            }
        } else {


        }



    }



    class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView circle_type_itemImg;
        private TextView circle_type_itemTitle;
        private TextView circle_type_itemIntroduce;
        private Button circle_type_itemJoin;




        public NormalViewHolder(View itemView) {
            super(itemView);
            circle_type_itemImg = (ImageView) itemView.findViewById(R.id.circle_type_itemImg);
            circle_type_itemTitle = (TextView) itemView.findViewById(R.id.circle_type_itemTitle);
            circle_type_itemIntroduce = (TextView) itemView.findViewById(R.id.circle_type_itemIntroduce);
            circle_type_itemJoin = (Button) itemView.findViewById(R.id.circle_type_itemJoin);


        }


    }
   public class FooterViewHolder extends RecyclerView.ViewHolder {

       public FooterViewHolder(View itemView) {
           super(itemView);

       }
   }

 public interface OnItemClickListener{

     void onJoinClick(int circleId);

 }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;



    }

}
