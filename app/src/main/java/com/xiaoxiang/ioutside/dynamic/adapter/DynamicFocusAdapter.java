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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.dynamic.model.ThumbListOfObservedUser;
import com.xiaoxiang.ioutside.util.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2016/4/25,0025.
 */
public class DynamicFocusAdapter extends RecyclerView.Adapter<DynamicFocusAdapter.ThumbUserViewHolder> {
    private List<ThumbListOfObservedUser> list = new ArrayList<>();
    public Context context;
    public ACache mCache;

    public DynamicFocusAdapter(Context context) {
        this.context = context;
        mCache=ACache.get(context);
    }

    //得到数组
    public List<ThumbListOfObservedUser> getDataSet() {
        return list;
    }

    //添加数据集到头部
    public void addItemToHead(int position, ThumbListOfObservedUser item) {
        list.add(position, item);
        notifyItemChanged(position);
    }

    //添加单个数据集
    public void addItem(ThumbListOfObservedUser item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ThumbUserViewHolder holder, final int position) {
        final ThumbListOfObservedUser thumbListOfObservedUser = list.get(position);
        boolean isLiked = thumbListOfObservedUser.isLiked();
        final String video = thumbListOfObservedUser.getVideo();
        holder.userPhoto.setImageResource(R.drawable.account_bitmap);
        holder.focus_photo.setImageResource(R.drawable.account_bitmap);
        ImageLoader.getInstance().displayImage(thumbListOfObservedUser.getUserPhoto(), holder.userPhoto);

        holder.userName.setText(thumbListOfObservedUser.getUserName());
        holder.likeCount.setText(thumbListOfObservedUser.getLikedCount() + "");
        holder.commentCount.setText(thumbListOfObservedUser.getCommentCount() + "");
        holder.focus_time.setText(thumbListOfObservedUser.getPublishTime().substring(0, 10));

        if (thumbListOfObservedUser.getThoughts().toString().length()>45){
            holder.focus_text.setText(thumbListOfObservedUser.getThoughts().substring(0,45)+"...");
        }
        else {
            holder.focus_text.setText(thumbListOfObservedUser.getThoughts());
        }

        if (video != null&&video.length()!=0) {//判断视频是否为空，不为空显示视频的第一帧图片
            holder.focus_playVideo.setVisibility(View.VISIBLE);
            if (mCache.getAsBitmap(video)!=null){
                holder.focus_photo.setImageBitmap(mCache.getAsBitmap(video));
            }else {
                LongAsyncTask longAsyncTask=new LongAsyncTask(context,holder.focus_photo,video);
                longAsyncTask.execute();
            }

        } else {
            holder.focus_playVideo.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(thumbListOfObservedUser.getPhotoList().get(0), holder.focus_photo);
        }
        if (isLiked == false&&holder.function_zan!=null) {
            holder.function_zan.setImageResource(R.drawable.zan_normal);
        } else {
            holder.function_zan.setImageResource(R.drawable.zan_pressed);
        }
        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onUserInfoClick(holder.userPhoto, position);
                }
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onUserInfoClick(holder.userPhoto, position);
                }
            }
        });

        holder.function_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onZanClick(holder.function_zan, position);
                }
            }
        });

        holder.function_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onShareClick(holder.function_share, position);
                }
            }
        });

    }

    /**
     * 创建新View，被LayoutInflater所调用
     */
    @Override
    public ThumbUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_focus_item, parent, false);
        ThumbUserViewHolder thumbUserViewHolder = new ThumbUserViewHolder(view);
        return thumbUserViewHolder;
    }

    public class ThumbUserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userPhoto;//用户头像
        public TextView userName;//用户昵称
        public TextView focus_time;
        public ImageView focus_photo;//文章中的图片
        public ImageView focus_playVideo;//文章中的视频
        public TextView focus_text;//文章的感想
        public ImageView function_zan;//点赞
        public TextView likeCount;//被点赞的次数
        public ImageView function_comment;//评论
        public TextView commentCount;//评论量
        public ImageView function_share;//分享

        public ThumbUserViewHolder(View view) {
            super(view);
            userPhoto = (CircleImageView) view.findViewById(R.id.focus_headUser);
            userName = (TextView) view.findViewById(R.id.focus_username);
            focus_time = (TextView) view.findViewById(R.id.focus_publishTime);
            focus_photo = (ImageView) view.findViewById(R.id.focus_middle_image);
            focus_playVideo=(ImageView)view.findViewById(R.id.focus_playVideo);
            focus_text = (TextView) view.findViewById(R.id.focus_middle_text);
            function_zan = (ImageView) view.findViewById(R.id.focus_function_zan);
            likeCount = (TextView) view.findViewById(R.id.focus_likedCount);
            function_comment = (ImageView) view.findViewById(R.id.focus_function_comment);
            commentCount = (TextView) view.findViewById(R.id.focus_commentCount);
            function_share = (ImageView) view.findViewById(R.id.focus_function_share);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, getLayoutPosition());
                    }
                }
            });
        }
    }

    //定义接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onUserInfoClick(View view,int position);
        void onZanClick(View view, int position);
        void onShareClick(View view, int position);
    }

    //声明接口变量
    private OnItemClickListener mOnItemClickListener;

    //初始化
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
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
        private Context context;
        private ACache mCache;
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
            mCache.put(video,bitmap);
            focus_photo.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            bitmap=createVideoThumbnail(video,750,750);
            return bitmap;
        }
    }



}
