package com.xiaoxiang.ioutside.dynamic.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.dynamic.model.ThumbList;
import com.xiaoxiang.ioutside.util.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2016/4/26,0026.
 */
public class DynamicRecommendAdapter extends RecyclerView.Adapter<DynamicRecommendAdapter.ThumbViewHolder>{
    public Context context;
    public List<ThumbList> list=new ArrayList<>();
    public ACache mCache;
    public DynamicRecommendAdapter(Context context) {
        this.context = context;
        mCache=ACache.get(context);
    }

    //得到数组
    public List<ThumbList> getDataSet() {
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int position,ThumbList item) {
        list.add(position, item);
        notifyItemChanged(position);
    }

    //添加单个数据集
    public void addItem(ThumbList item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ThumbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_recommend_item, parent, false);
        ThumbViewHolder thumbViewHolder = new ThumbViewHolder(view);
        return thumbViewHolder;
    }

    @Override
    public void onBindViewHolder(final ThumbViewHolder holder, final int position) {
        ThumbList thumbList=list.get(position);
        ArrayList <String>  photos=thumbList.getPhotoList();
        String photo=photos.get(0);
        boolean observed=thumbList.isObserved();
        boolean isLiked=thumbList.isLiked();
//        holder.middle_image.setImageResource(R.drawable.account_bitmap);
        holder.recommend_headUser.setImageResource(R.drawable.account_bitmap);
        holder.recommend_username.setText(thumbList.getUserName() + "");
        holder.commentCount.setText(thumbList.getCommentCount() + "");
        holder.likeCount.setText(thumbList.getLikedCount()+"");
        holder.recommend_publishTime.setText(thumbList.getPublishTime().substring(0, 10));
//        ImageLoader.getInstance().displayImage(thumbList.getUserPhoto(), holder.recommend_headUser);
        Glide.with(context).load(thumbList.getUserPhoto()).into(holder.recommend_headUser);
        if (thumbList.getVideo()==null){
            holder.recommend_playVideo.setVisibility(View.GONE);
            Glide.with(context).load(photo).into(holder.middle_image);//只加载第一张图片
//            ImageLoader.getInstance().displayImage(photo, holder.middle_image);
        }
        else {
            holder.recommend_playVideo.setVisibility(View.VISIBLE);
            if (mCache.getAsBitmap(thumbList.getVideo())!=null){
                holder.middle_image.setImageBitmap(mCache.getAsBitmap(thumbList.getVideo()));
            }else {
                LongAsyncTask longAsyncTask=new LongAsyncTask(context,holder.middle_image,thumbList.getVideo());
                longAsyncTask.execute();
            }
        }
        if (thumbList.getThoughts().toString().length()>45)
            holder.middle_text.setText(thumbList.getThoughts().substring(0,45)+"...");

        else
            holder.middle_text.setText(thumbList.getThoughts());

        if (observed==false)
            holder.recommend_attention.setImageResource(R.drawable.add_focus);
        else
            holder.recommend_attention.setImageResource(R.drawable.has_focus);

        if (isLiked==false)
            holder.function_zan.setImageResource(R.drawable.zan_normal);

        else
            holder.function_zan.setImageResource(R.drawable.zan_pressed);


        holder.recommend_headUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserInfoClick(holder.recommend_headUser, position);
                }
            }
        });

        holder.recommend_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onUserInfoClick(holder.recommend_username,position);
                }
            }
        });

        holder.recommend_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onFocusClick(holder.recommend_attention, position);
                }
            }
        });

        holder.function_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onZanClick(holder.function_zan, position);
                }
            }
        });

        holder.function_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onShareClick(holder.function_share,position);
                }
            }
        });

    }

    public class ThumbViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView recommend_headUser;
        public TextView recommend_username;
        public ImageView recommend_attention;
        public ImageView middle_image;
        private ImageView recommend_playVideo;
        public TextView middle_text;
        public TextView recommend_publishTime;
        public ImageView function_zan;
        public TextView likeCount;
        public ImageView function_comment;
        public TextView commentCount;
        public ImageView function_share;
        public ThumbViewHolder(View view) {
            super(view);
            recommend_headUser=(CircleImageView)view.findViewById(R.id.recommend_headUser);
            recommend_username=(TextView)view.findViewById(R.id.recommend_username);
            recommend_attention=(ImageView)view.findViewById(R.id.recommend_attention);
            middle_image=(ImageView)view.findViewById(R.id.recommend_middle_image);
            recommend_playVideo=(ImageView)view.findViewById(R.id.recommend_playVideo);
            middle_text=(TextView)view.findViewById(R.id.recommend_middle_text);
            recommend_publishTime=(TextView)view.findViewById(R.id.recommend_publishTime);
            function_zan=(ImageView)view.findViewById(R.id.recommend_function_zan);
            likeCount=(TextView)view.findViewById(R.id.recommend_likedCount);
            function_comment=(ImageView)view.findViewById(R.id.recommend_function_comment);
            commentCount=(TextView)view.findViewById(R.id.recommend_commentCount);
            function_share=(ImageView)view.findViewById(R.id.recommend_function_share);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private  Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    private class LongAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView focus_photo;
        private String video;
        private Bitmap bitmap=null;
        private ACache mCache;
        private Context context;
        public LongAsyncTask(Context context,ImageView focus_photo,String video) {
            this.context=context;
            this.focus_photo=focus_photo;
            this.video=video;
        }

        @Override
        protected void onPreExecute() {
            mCache=ACache.get(context);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mCache.put(video, bitmap);
            focus_photo.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
//<<<<<<< HEAD:app/src/main/java/com/xiaoxiang/ioutside/discovery/adapter/DiscoveryRecommendAdapter.java
//            bitmap = createVideoThumbnail(video,400,400);
//=======
            bitmap=createVideoThumbnail(video,750,750);
//>>>>>>> c6899f105fe33c452538b437bcb7eab20a133c41:app/src/main/java/com/xiaoxiang/ioutside/dynamic/adapter/DynamicRecommendAdapter.java
            return bitmap;
        }
    }

    public interface ItemClickListener {
        void onUserInfoClick(View view,int position);
        void onFocusClick(View view, int position);//点击关注按钮
        void onZanClick(View view, int position);//点赞
        void onItemClick(View view, int position);
        void onShareClick(View view ,int position);
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
