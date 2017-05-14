package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.Dynamic;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicHolder>{
    private final String TAG=getClass().getSimpleName();
    public ArrayList<Dynamic> mylist=new ArrayList<>();
    private View view;
    private DynamicHolder dynamicHolder;


    public DynamicAdapter(ArrayList<Dynamic> mylist){
        this.mylist=mylist;
    }


    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dynamic_item,parent,false);
        dynamicHolder=new DynamicHolder(view);
        return dynamicHolder;
    }


    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(Dynamic item) {
        mylist.add(item);
        notifyDataSetChanged();
    }


    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(ArrayList<Dynamic> items) {
        mylist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(Dynamic item) {
        mylist.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(ArrayList<Dynamic> items) {
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
    public void remove(Dynamic item) {
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
    public Dynamic getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }



    @Override
    public void onBindViewHolder(final DynamicHolder dynamicHolder, final int position) {
        Dynamic dynamic=mylist.get(position);
        dynamicHolder.time.setText(dynamic.getPublishTime());
        dynamicHolder.title.setText(dynamic.getTitle());
        ImageLoader.getInstance().displayImage(dynamic.getPhotoList().get(0), dynamicHolder.photo);

    }

    public ArrayList<Dynamic> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<Dynamic> mylist) {
        this.mylist = mylist;
    }

    public  class DynamicHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public ImageView photo;
        public TextView title;
        public TextView intro;

        public DynamicHolder(View view){
            super(view);
            photo=(ImageView)view.findViewById(R.id.my_dynamic_src);
            time=(TextView)view.findViewById(R.id.my_dynamic_time);
            title=(TextView)view.findViewById(R.id.my_dynamic_title);
            intro=(TextView)view.findViewById(R.id.my_dynamic_intro);
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

    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
