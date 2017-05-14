package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.model.Subject;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public class MoreSubAdapter extends RecyclerView.Adapter<MoreSubAdapter.MoreSubHolder>{
    private final String TAG=getClass().getSimpleName();
    public ArrayList<Subject> mylist=new ArrayList<>();
    private View view;
    private MoreSubHolder moreSubHolder;


    public MoreSubAdapter(ArrayList<Subject> mylist){
        this.mylist=mylist;
    }


    @Override
    public MoreSubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.more_sub_item,parent,false);
        moreSubHolder=new MoreSubHolder(view);
        return moreSubHolder;
    }


    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(Subject item) {
        mylist.add(item);
        notifyDataSetChanged();
    }


    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(ArrayList<Subject> items) {
        mylist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(Subject item) {
        mylist.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(ArrayList<Subject> items) {
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
    public void remove(Subject item) {
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
    public Subject getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }



    @Override
    public void onBindViewHolder(final MoreSubHolder moreSubHolder, final int position) {
        Subject moreSub=mylist.get(position);
        moreSubHolder.name.setText(moreSub.getTitle());
        moreSubHolder.intro.setText(moreSub.getIntroduction());
        ImageLoader.getInstance().displayImage(moreSub.getPhoto(),moreSubHolder.photo);
//        if(moreSub.isObserved()){
//            moreSubHolder.obserBtn.setBackgroundResource(R.drawable.observed_sub);
//            moreSubHolder.obserBtn.setTextSize(10f);
//            moreSubHolder.obserBtn.setText("已关注");
//        }else{
//            moreSubHolder.obserBtn.setBackgroundResource(R.drawable.observe_sub);
//            moreSubHolder.obserBtn.setText("关注");
//            moreSubHolder.obserBtn.setTextSize(13.3f);
//        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });

//        moreSubHolder.obserBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(onItemClickListener!=null){
//                    onItemClickListener.onItemObserveClick(moreSubHolder.obserBtn,position);
//                }
//            }
//        });
    }

    public ArrayList<Subject> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<Subject> mylist) {
        this.mylist = mylist;
    }

    public  class MoreSubHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView intro;
        public ImageView photo;
      //  public Button obserBtn;

        public MoreSubHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.tv_title_sub);
            intro=(TextView)view.findViewById(R.id.tv_intro_sub);
            photo=(ImageView)view.findViewById(R.id.img_photo_sub);
          //  obserBtn=(Button)view.findViewById(R.id.btn_observe_sub);
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
         /*void onItemUserInfo(View view,int position);//点击用户头像跟用户昵称
         void onItemFocusStatus(View view,int position);//点击关注按钮
         void onItemZanStatus(View view,int position);//点赞是一种*/
         void onItemObserveClick(View view, int position);
         void onItemClick(View view, int position);


    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
