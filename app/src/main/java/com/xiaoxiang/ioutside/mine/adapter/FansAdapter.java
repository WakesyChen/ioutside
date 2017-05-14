package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.mine.model.Fan;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public class FansAdapter extends RecyclerView.Adapter<FansAdapter.FansHolder>{
    private final String TAG=getClass().getSimpleName();
    public ArrayList<Fan> mylist=new ArrayList<>();
    private View view;
    private FansHolder fansHolder;


    public FansAdapter(ArrayList<Fan> mylist){
        this.mylist=mylist;
    }


    @Override
    public FansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fans_item,parent,false);
        fansHolder=new FansHolder(view);
        return fansHolder;
    }


    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(Fan item) {
        mylist.add(item);
        notifyDataSetChanged();
    }


    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(ArrayList<Fan> items) {
        mylist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(Fan item) {
        mylist.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(ArrayList<Fan> items) {
        mylist.addAll(0, items);
        notifyDataSetChanged();
    }

    /**
     * 移除某个数据
     *
     * @param position
     */
    public void remove(int position) {
        mylist.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除某个数据项
     *
     * @param item
     */
    public void remove(Fan item) {
        mylist.remove(item);
        notifyDataSetChanged();
    }

    /**
     * clearData all data
     */
    public void clear() {
        mylist.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取指定位置的数据项
     *
     * @param position
     * @return
     */
    public Fan getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }



    @Override
    public void onBindViewHolder(final FansHolder fansHolder, final int position) {
        Fan fan=mylist.get(position);
        fansHolder.name.setText(fan.getName());
        fansHolder.addr.setText(fan.getAddress());
        char sexchar = fan.getSex();
        if (sexchar == 'u') {
            fansHolder.sex.setText("不确定");
        } else if (sexchar == 'm') {
            fansHolder.sex.setText( "男");
        } else if (sexchar == 'w') {
            fansHolder.sex.setText("女");
        }
        if(fan.getPhoto()==null){
            fansHolder.userPhoto.setImageResource(R.drawable.defoulthead);
        }else{
            ImageLoader.getInstance().displayImage(fan.getPhoto(), fansHolder.userPhoto);
        }
        if(fan.isObserved()){
            fansHolder.observe.setText("取消关注");
            fansHolder.observe.setTextSize(8.0f);
            fansHolder.obserLay.setBackgroundResource(R.drawable.fans_exit);
        }else{
            fansHolder.observe.setText("关注");
            fansHolder.observe.setTextSize(12.0f);
            fansHolder.obserLay.setBackgroundResource(R.drawable.fans_add);
        }

        fansHolder.level.setText(fansHolder.level.getContext().getString(R.string.tv_level, fan.getLevel()));

        fansHolder.userPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemPhotoClick(view,position);
                }
            }
        });

        fansHolder.obserLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemObserClick(fansHolder.obserLay,position);
                }
            }
        });
    }

    public ArrayList<Fan> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<Fan> mylist) {
        this.mylist = mylist;
    }

    public  class FansHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView sex;
        public TextView addr;
        public TextView level;
        public CircleImageView userPhoto;
        public RelativeLayout obserLay;
        public TextView observe;

        public FansHolder(View view){
            super(view);
            userPhoto=(CircleImageView)view.findViewById(R.id.cirimg_head_fans);
            name=(TextView)view.findViewById(R.id.tv_name_fans);
            sex=(TextView)view.findViewById(R.id.tv_sex_fans);
            addr=(TextView)view.findViewById(R.id.tv_addr_fans);
            level=(TextView)view.findViewById(R.id.tv_level_fans);
            obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe);
            observe=(TextView)view.findViewById(R.id.tv_addobserve_fans);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }

    public interface ItemClickListener{
       /* void onItemUserInfo(View view,int position);//点击用户头像跟用户昵称
        void onItemFocusStatus(View view,int position);//点击关注按钮
        void onItemZanStatus(View view,int position);//点赞是一种
        void onItemShare(View view,int position);
        void onItemClick(View view,int position);*/
        void onItemClick(View view, int position);
        void onItemPhotoClick(View view, int position);
        void onItemObserClick(View view, int position);

    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
