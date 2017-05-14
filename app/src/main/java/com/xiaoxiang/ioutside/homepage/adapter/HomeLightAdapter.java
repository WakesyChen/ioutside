package com.xiaoxiang.ioutside.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.model.Video;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/4/26,0026.
 */
public class HomeLightAdapter extends RecyclerView.Adapter<HomeLightAdapter.LightViewHolder> {
    private ArrayList<Video> list=new ArrayList<>();
    private Context context;
    //得到数组
    public ArrayList<Video> getDataSet() {
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int position,Video item) {
        list.add(position, item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public LightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_light_item,parent,false);
        LightViewHolder viewHolder=new LightViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LightViewHolder holder, final int position) {
        Video video=list.get(position);
//        holder.light_background.setImageResource(R.drawable.account_bitmap);
//        ImageLoader.getInstance().displayImage(video.getPhoto(), holder.light_background);
        Glide.with(context).load(video.getPhoto()).into(holder.light_background);
        holder.light_title.setText(video.getTitle());
        holder.light_commentCount.setText(video.getCommentCount() + "");
        holder.light_likedCount.setText(video.getLikedCount() + "");
        holder.light_thoughts.setText(video.getRecommendReason());
        holder.light_page.setText((position + 1) + "/10");

        if (video.isLiked()==false)
            holder.light_zan.setImageResource(R.drawable.zan_normal);
        else
            holder.light_zan.setImageResource(R.drawable.zan_pressed);

        holder.light_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onZanClick(holder.light_zan, position);
                }
            }
        });

        holder.light_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onCommentClick(holder.light_comment, position);
                }
            }
        });

        holder.light_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onShareClick(holder.light_share, position);
                }
            }
        });


    }

    public class LightViewHolder extends RecyclerView.ViewHolder {
        private ImageView light_background;
        private ImageView light_play;
        private TextView light_title;
        private TextView light_thoughts;
        private ImageView light_comment;
        private TextView light_commentCount;
        private TextView light_likedCount;
        private ImageView light_zan;
        private ImageView light_share;
        private TextView light_page;
        public LightViewHolder(View view) {
            super(view);
            light_background=(ImageView)view.findViewById(R.id.light_background);
            light_play= (ImageView) view.findViewById(R.id.light_play);
            light_title=(TextView)view.findViewById(R.id.light_title);
            light_thoughts=(TextView)view.findViewById(R.id.light_thoughts);
            light_comment=(ImageView)view.findViewById(R.id.light_comment);
            light_commentCount=(TextView)view.findViewById(R.id.light_commentCount);
            light_likedCount=(TextView)view.findViewById(R.id.light_likedCount);
            light_zan=(ImageView)view.findViewById(R.id.light_zan);
            light_share=(ImageView)view.findViewById(R.id.light_share);
            light_page=(TextView)view.findViewById(R.id.light_page);
            light_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        int position = getLayoutPosition();
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }

    }

    //定义接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onZanClick(View view, int position);
        void onCommentClick(View view, int position);
        void onShareClick(View view,int position);
    }

    //声明接口变量
    private OnItemClickListener mOnItemClickListener;

    //初始化
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
}
