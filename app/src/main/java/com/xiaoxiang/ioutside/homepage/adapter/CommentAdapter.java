package com.xiaoxiang.ioutside.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.model.CommentList;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by zhang on 2016/5/3/0003.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<CommentList> list=new ArrayList<>();

    //得到数组
    public List<CommentList> getDataSet() {
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int pageSize,List<CommentList> newList){
        if (pageSize<list.size()){//只移除前五个
            for (int i=0;i<pageSize;i++){
                    list.remove(i);
            }
        }
        else list.clear();//否则全部清空
        list.addAll(newList);
        notifyDataSetChanged();
    }

    //添加单个数据集
    public void addItem(CommentList item) {
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
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {
        String publishTime=list.get(position).getCommentTime();
        String content=list.get(position).getContent();
        String photo=list.get(position).getUserPhoto();
        String userName=list.get(position).getUserName();
        String receiverName=list.get(position).getReceiverName();
        if (receiverName==null){
            holder.comment_other_userName.setVisibility(View.GONE);
            holder.comment_textView.setVisibility(View.GONE);
            holder.comment_dialog.setVisibility(View.GONE);
        }else {
            holder.comment_other_userName.setVisibility(View.VISIBLE);
            holder.comment_textView.setVisibility(View.VISIBLE);
            holder.comment_dialog.setVisibility(View.VISIBLE);
            holder.comment_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onCommentDialogClick(holder.comment_dialog,position);
                    }
                }
            });

            holder.comment_other_userName.setText(receiverName);

            holder.comment_other_userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onOtherUserNameClick(holder.comment_other_userName,position);
                    }
                }
            });
        }
        holder.comment_userPhoto.setImageResource(R.drawable.account_bitmap);
        holder.comment_userName.setText(userName);
        holder.comment_publishTime.setText(publishTime);
        holder.comment_content.setText(content);
        ImageLoader.getInstance().displayImage(photo, holder.comment_userPhoto);

        holder.comment_userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onUserInfoClick(holder.comment_userPhoto, position);
            }
        });

        holder.comment_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onUserInfoClick(holder.comment_userName,position);
                }
            }
        });

    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        CommentViewHolder commentViewHolder=new CommentViewHolder(view);
        return commentViewHolder;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private ImageView comment_userPhoto;
        private TextView comment_userName;
        private TextView comment_publishTime;
        private TextView comment_content;
        private TextView comment_textView;
        private TextView comment_other_userName;
        private TextView comment_dialog;
        public CommentViewHolder(View view){
            super(view);
            comment_userPhoto=(ImageView)view.findViewById(R.id.comment_userPhoto);
            comment_userName=(TextView)view.findViewById(R.id.comment_userName);
            comment_publishTime=(TextView)view.findViewById(R.id.comment_publishtime);
            comment_content=(TextView)view.findViewById(R.id.comment_content);
            comment_textView=(TextView)view.findViewById(R.id.comment_textView);
            comment_other_userName=(TextView)view.findViewById(R.id.comment_other_userName);
            comment_dialog=(TextView)view.findViewById(R.id.comment_dialog);
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
        void onCommentDialogClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

}
