package com.xiaoxiang.ioutside.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.model.CommentDialogList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/7/5,0005.
 */
public class CommentDialogAdapter extends RecyclerView.Adapter<CommentDialogAdapter.CommentDialogViewHolder> {
    private List<CommentDialogList>  list=new ArrayList<>();

    public List<CommentDialogList> getDataSet(){
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int pageSize,List<CommentDialogList> newList) {
        if (pageSize<list.size()){
            for (int i=0;i<pageSize;i++){
                list.remove(i);
            }
        }
        else list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    //添加单个数据集
    public void addItem(CommentDialogList item) {
        list.add(item);
        notifyDataSetChanged();
    }

    //删除单个数据集
    public void removeItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final CommentDialogViewHolder holder,final int position) {
        String publishTime=list.get(position).getCommentTime();
        String content=list.get(position).getContent();
        String photo=list.get(position).getUserPhoto();
        String userName=list.get(position).getUserName();
        String receiverName=list.get(position).getReceiverName();
        if (receiverName==null){
            holder.comment_dialog_other_userName.setVisibility(View.GONE);
            holder.comment_dialog_textView.setVisibility(View.GONE);

        }else {
            holder.comment_dialog_textView.setVisibility(View.VISIBLE);
            holder.comment_dialog_other_userName.setVisibility(View.VISIBLE);
            holder.comment_dialog_other_userName.setText(receiverName);

            holder.comment_dialog_other_userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onOtherUserNameClick(holder.comment_dialog_other_userName,position);
                    }
                }
            });
        }
        holder.comment_dialog_userPhoto.setImageResource(R.drawable.account_bitmap);
        holder.comment_dialog_userName.setText(userName);
        holder.comment_dialog_publishTime.setText(publishTime);
        holder.comment_dialog_content.setText(content);
        ImageLoader.getInstance().displayImage(photo, holder.comment_dialog_userPhoto);

        holder.comment_dialog_userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onUserInfoClick(holder.comment_dialog_userPhoto, position);
            }
        });

        holder.comment_dialog_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onUserInfoClick(holder.comment_dialog_userName,position);
                }
            }
        });
    }

    @Override
    public CommentDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_dialog_item,parent,false);
        CommentDialogViewHolder holder=new CommentDialogViewHolder(view);
        return holder;
    }

    public class CommentDialogViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView comment_dialog_userPhoto;
        private TextView comment_dialog_userName;
        private TextView comment_dialog_textView;
        private TextView comment_dialog_other_userName;
        private TextView comment_dialog_publishTime;
        private TextView comment_dialog_content;
        public CommentDialogViewHolder(View view){
            super(view);
            comment_dialog_userPhoto=(CircleImageView)view.findViewById(R.id.comment_dialog_userPhoto);
            comment_dialog_userName=(TextView)view.findViewById(R.id.comment_dialog_userName);
            comment_dialog_textView=(TextView)view.findViewById(R.id.comment_dialog_textView);
            comment_dialog_other_userName=(TextView)view.findViewById(R.id.comment_dialog_other_userName);
            comment_dialog_publishTime=(TextView)view.findViewById(R.id.comment_dialog_publishtime);
            comment_dialog_content=(TextView)view.findViewById(R.id.comment_dialog_content);
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

    public interface OnItemClickListener{
        void onUserInfoClick(View view,int position);
        void onItemClick(View view,int position);
        void onOtherUserNameClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;
}
