package com.xiaoxiang.ioutside.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.model.MySubject;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/4/26,0026.
 */
public class HomeSubscribeAdapter extends RecyclerView.Adapter<HomeSubscribeAdapter.AttentionViewHolder> {
    private ArrayList<MySubject> list=new ArrayList<>();
    //得到数组
    public ArrayList<MySubject> getDataSet() {
        return list;
    }
    private Context context;
    //添加数据集到头部
    public void addItemToHead(int position,MySubject item) {
        list.add(position, item);
        notifyItemChanged(position);
    }
    //添加单个数据集
    public void addItem(MySubject item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final AttentionViewHolder holder, final int position) {
        MySubject mySubject =list.get(position);
//        holder.attention_image.setImageResource(R.drawable.account_bitmap);
//        ImageLoader.getInstance().displayImage(mySubject.getPhoto(), holder.attention_image);
        Glide.with(context).load(mySubject.getPhoto()).into(holder.attention_image);

        holder.attention_subject.setText("" + mySubject.getSubjectName());//专题标签
        String extraTag = mySubject.getColumnistSubjectName();
        if (TextUtils.isEmpty(extraTag)) {
            holder.attention_subject2.setVisibility(View.GONE);
        }
        else {
            holder.attention_subject2.setText(extraTag+"");//专题标签
            holder.attention_subject2.setVisibility(View.VISIBLE);
        }


        holder.attention_title.setText(mySubject.getTitle()+"");

        holder.attention_viewCount.setText(""+mySubject.getViewCount());

        //点击专题
        holder.attention_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onItemClickListener!=null) {
                    onItemClickListener.onSubjectClick(holder.attention_subject, position);
                }
            }
        });
        holder.attention_subject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null) {
                    onItemClickListener.onSubjectClick2(holder.attention_subject2,position);
                }
            }
        });

    }

    @Override
    public AttentionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_subscribe_item,parent,false);
        AttentionViewHolder attentionViewHolder=new AttentionViewHolder(view);
        return attentionViewHolder;
    }

    public class AttentionViewHolder extends RecyclerView.ViewHolder{
        private TextView attention_subject;//专题
        private TextView attention_subject2;//专题2
        private ImageView attention_image;//文章图片
        private TextView attention_title;//文章标题
        private TextView attention_viewCount;
        public AttentionViewHolder(View view){
            super(view);
            attention_subject=(TextView)view.findViewById(R.id.attention_subject);
            attention_subject2=(TextView)view.findViewById(R.id.attention_subject2);
            attention_image=(ImageView)view.findViewById(R.id.attention_image);
            attention_title=(TextView)view.findViewById(R.id.attention_title);
            attention_viewCount=(TextView)view.findViewById(R.id.attention_viewCount);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getPosition());
                    }
                }
            });
        }
    }

    public interface ItemClickListener{
        void onSubjectClick(View view, int position);
        void onSubjectClick2(View view, int position);//专栏2
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
