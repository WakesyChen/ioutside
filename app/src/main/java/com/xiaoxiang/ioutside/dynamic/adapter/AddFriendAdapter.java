package com.xiaoxiang.ioutside.dynamic.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.dynamic.model.FriendList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/5/29,0029.
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{
    private List<FriendList> list=new ArrayList<>();
    //得到数组
    private static final String TAG = "AddFriendAdapter";
    public List<FriendList> getDataSet() {
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int position,FriendList item) {
        list.add(position, item);
        notifyDataSetChanged();
    }

    //添加单个数据集
    public void addItem(FriendList item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onImageClick(View view, int position);
        void onFocusClick(View view, int position);
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final AddFriendViewHolder holder, final int position) {
        FriendList friendList=list.get(position);
        holder.friend_username.setText(friendList.getName());//用户昵称
        //设置默认的图片
        holder.friend_userPhoto.setImageResource(R.drawable.account_bitmap);
        holder.friend_photo.setImageResource(R.drawable.account_bitmap);
        holder.friend_photo1.setImageResource(R.drawable.account_bitmap);
        holder.friend_photo2.setImageResource(R.drawable.account_bitmap);

        ImageLoader.getInstance().displayImage(friendList.getUserPhoto(), holder.friend_userPhoto);//用户头像

        Log.i(TAG, " friendList.getFootprintList().size():"+friendList.getFootprintList().size());

        //footprintlist中图片的个数不都是3，要动态获取赋值；
         List<ImageView> HolderImgs=new ArrayList<>();//用来装holder的图片
            HolderImgs.add(holder.friend_photo);
            HolderImgs.add(holder.friend_photo1);
            HolderImgs.add(holder.friend_photo2);
        int footPrintCount=friendList.getFootprintList().size();
        for (int i = 0; i < footPrintCount; i++) {
            ImageLoader.getInstance().displayImage(friendList.getFootprintList().get(i).getPhoto(), HolderImgs.get(i));

        }
        //写死会崩溃
//        ImageLoader.getInstance().displayImage(friendList.getFootprintList().get(0).getPhoto(), holder.friend_photo);
//        ImageLoader.getInstance().displayImage(friendList.getFootprintList().get(1).getPhoto(), holder.friend_photo1);
//        ImageLoader.getInstance().displayImage(friendList.getFootprintList().get(2).getPhoto(),holder.friend_photo2);

        //点击头像
        holder.friend_userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onImageClick(holder.friend_userPhoto, position);
                }
            }
        });

        //点击关注
        holder.friend_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onFocusClick(v,position);
                }
            }
        });

    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_friend_item,parent,false);
        AddFriendViewHolder holder=new AddFriendViewHolder(itemView);
        return holder;
    }

    public class AddFriendViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView friend_userPhoto;
        public TextView friend_username;
        public ImageView friend_focus;
        public ImageView friend_photo;
        public ImageView friend_photo1;
        public ImageView friend_photo2;
        public AddFriendViewHolder(View view){
            super(view);
            friend_userPhoto=(CircleImageView)view.findViewById(R.id.friend_userPhoto);
            friend_username=(TextView)view.findViewById(R.id.friend_username);
            friend_focus=(ImageView)view.findViewById(R.id.friend_focus);
            friend_photo=(ImageView)view.findViewById(R.id.friend_photo);
            friend_photo1=(ImageView)view.findViewById(R.id.friend_photo1);
            friend_photo2=(ImageView)view.findViewById(R.id.friend_photo2);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }

}
