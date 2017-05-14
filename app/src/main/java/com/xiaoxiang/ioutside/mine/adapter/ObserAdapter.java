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
import com.xiaoxiang.ioutside.mine.model.Observer;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public class ObserAdapter extends RecyclerView.Adapter<ObserAdapter.ObserverHolder>{
    private final String TAG=getClass().getSimpleName();
    public ArrayList<Observer> mylist=new ArrayList<>();
    private View view;
    private ObserverHolder obserHolder;


    public ObserAdapter(ArrayList<Observer> mylist){
        this.mylist=mylist;
    }


    @Override
    public ObserverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_observer,parent,false);
        obserHolder=new ObserverHolder(view);
        return obserHolder;
    }


    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(Observer item) {
        mylist.add(item);
        notifyDataSetChanged();
    }


    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(ArrayList<Observer> items) {
        mylist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(Observer item) {
        mylist.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(ArrayList<Observer> items) {
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
    public void remove(Observer item) {
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
    public Observer getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }



    @Override
    public void onBindViewHolder(final ObserverHolder obserHolder, final int position) {
        Observer observer=mylist.get(position);
        obserHolder.name.setText(observer.getName());
        obserHolder.addr.setText(observer.getAddress());
        char sexchar = observer.getSex();
        if (sexchar == 'u') {
            obserHolder.sex.setText("不确定");
        } else if (sexchar == 'm') {
            obserHolder.sex.setText( "男");
        } else if (sexchar == 'w') {
            obserHolder.sex.setText("女");
        }
        if(observer.getPhoto()==null){
            obserHolder.userPhoto.setImageResource(R.drawable.defoulthead);
        }else{
            ImageLoader.getInstance().displayImage(observer.getPhoto(),obserHolder.userPhoto);
        }

        ImageLoader.getInstance().displayImage(observer.getPhoto(), obserHolder.userPhoto);
        if(observer.isObserved()){
            obserHolder.observe.setText("取消关注");
            obserHolder.observe.setTextSize(8.0f);
            obserHolder.obserLay.setBackgroundResource(R.drawable.observer_exit);
        }else{
            obserHolder.observe.setText("关注");
            obserHolder.observe.setTextSize(12.0f);
            obserHolder.obserLay.setBackgroundResource(R.drawable.observer);
        }

        obserHolder.level.setText(obserHolder.level.getContext().getString(R.string.tv_level, observer.getLevel()));

        obserHolder.userPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemPhotoClick(obserHolder.userPhoto,position);
                }
            }
        });

        obserHolder.obserLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemObserClick(obserHolder.obserLay,position);
                }
            }
        });
    }

    public ArrayList<Observer> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<Observer> mylist) {
        this.mylist = mylist;
    }

    public  class ObserverHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView sex;
        public TextView addr;
        public TextView level;
        public CircleImageView userPhoto;
        public RelativeLayout obserLay;
        public TextView observe;

        public ObserverHolder(View view){
            super(view);
            userPhoto=(CircleImageView)view.findViewById(R.id.cirimg_head_obsers);
            name=(TextView)view.findViewById(R.id.tv_name_obsers);
            sex=(TextView)view.findViewById(R.id.tv_sex_obsers);
            addr=(TextView)view.findViewById(R.id.tv_addr_obsers);
            level=(TextView)view.findViewById(R.id.tv_level_obsers);
            obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe_obsers);
            observe=(TextView)view.findViewById(R.id.tv_addobserve_obsers);
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
        void onItemClick(View view, int position);
        void onItemPhotoClick(View view, int position);
        void onItemObserClick(View view, int position);

    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
