package com.xiaoxiang.ioutside.dynamic.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.dynamic.model.ThumbListDetail;
import com.xiaoxiang.ioutside.model.CommentList;
import com.xiaoxiang.ioutside.util.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2016/6/30,0030.
 */
public class FocusDetailAdapter extends RecyclerView.Adapter<FocusDetailAdapter.FocusDetailViewHolder> {
    private static final String TAG=FocusDetailAdapter.class.getSimpleName();
    private ThumbListDetail thumbListDetail;
    private List<CommentList> list=new ArrayList<>();
    private ArrayList<Integer> tags=new ArrayList<>();//存标签
    private Context context;
    private ACache mCache;
    private static final int HEADER=1;
    private static final int NORMAL=2;
    private static final int FOOTER=3;

    public FocusDetailAdapter(Activity context) {
        this.context = context;
        mCache=ACache.get(context);
    }

    public List<CommentList> getCommentList() {
        return list;
    }

    public ThumbListDetail getThumbListDetail() {
        return thumbListDetail;
    }

    //添加单个数据集
    public void addThumbItem(ThumbListDetail item) {
        thumbListDetail=item;
        notifyDataSetChanged();
    }

    //添加单个数据集
    public void addCommentItemToHead(int pageSize,List<CommentList> newList) {
        if(pageSize<list.size()){
            for (int i=0;i<pageSize;i++){
                list.remove(i);
            }
        }
        else list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public  void addCommentItem(CommentList commentList){
        list.add(commentList);
        notifyDataSetChanged();
    }

    public void removeCommentItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size()+2;
    }

    @Override
    public void onBindViewHolder(final FocusDetailViewHolder holder, final int position) {
        if ((position!=0)&&(position!=list.size()+1)&&(NORMAL==holder.viewType)){
            if (list.size()!=0){
                CommentList commentList=list.get(position-1);
                ImageLoader.getInstance().displayImage(commentList.getUserPhoto(), holder.comment_userPhoto);
                holder.comment_publishtime.setText(commentList.getCommentTime().toString());
                holder.comment_content.setText(commentList.getContent());
                holder.comment_userName.setText(commentList.getUserName());
                holder.comment_userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onCommentUserInfoClick(holder.comment_userName, position);
                        }
                    }
                });
                holder.comment_userPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onCommentUserInfoClick(holder.comment_userPhoto, position);
                        }
                    }
                });
                String receiverName=commentList.getReceiverName();
                if (receiverName==null){
                    holder.comment_dialog.setVisibility(View.GONE);
                    holder.comment_textView.setVisibility(View.GONE);
                    holder.comment_other_userName.setVisibility(View.GONE);
                }
                else {
                    holder.comment_other_userName.setVisibility(View.VISIBLE);
                    holder.comment_dialog.setVisibility(View.VISIBLE);
                    holder.comment_textView.setVisibility(View.VISIBLE);
                    holder.comment_other_userName.setText(receiverName);
                    holder.comment_other_userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener!=null){
                                onItemClickListener.onCommentOtherUserNameClick(holder.comment_other_userName, position);
                            }
                        }
                    });
                    holder.comment_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener!=null){
                                onItemClickListener.onCommentDialogClick(holder.comment_dialog,position);
                            }
                        }
                    });
                }
            }
        }

        if ((position==0)&&(HEADER==holder.viewType)) {
            if (thumbListDetail != null) {
                ImageLoader.getInstance().displayImage(thumbListDetail.getUserPhoto(),holder.focus_header_userPhoto);
                holder.focus_header_username.setText(thumbListDetail.getUserName());
                holder.focus_header_thoughts.setText(thumbListDetail.getThoughts());
                holder.focus_header_time.setText(thumbListDetail.getPublishTime().toString().substring(0,11));
                boolean isObserved=thumbListDetail.isObserved();
                if (isObserved==false)
                    holder.focus_header_attention.setImageResource(R.drawable.add_focus);
                else
                    holder.focus_header_attention.setImageResource(R.drawable.has_focus);

                String video=thumbListDetail.getVideo();
                ArrayList<String> photoList = thumbListDetail.getPhotoList();
                if (video==null){
                    if (photoList.size() == 1) {
                        holder.focus_header_image.setVisibility(View.VISIBLE);
                        holder.focus_header_viewPager.setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(photoList.get(0), holder.focus_header_image);
                        Log.i(TAG, "imageView加载了图片");
                    }
                    else {
                        holder.focus_header_image.setVisibility(View.GONE);
                        holder.focus_header_viewPager.setVisibility(View.VISIBLE);
                        FocusPageAdapter mPageAdapter=new FocusPageAdapter(photoList);
                        holder.focus_header_viewPager.setAdapter(mPageAdapter);
                        holder.focus_header_viewPager.setCurrentItem(0);
                        Log.i(TAG, "viewPager加载了图片");
                    }
                }
                else {
                    holder.focus_header_image.setVisibility(View.VISIBLE);
                    holder.focus_header_viewPager.setVisibility(View.GONE);
                    holder.focus_header_play.setVisibility(View.VISIBLE);
                    if (mCache.getAsBitmap(video)!=null){
                        holder.focus_header_image.setImageBitmap(mCache.getAsBitmap(video));
                    }
                    else {
                        //后台获取线程
                        LongAsyncTask longAsyncTask=new LongAsyncTask(context,holder.focus_header_image,video);
                        longAsyncTask.execute();
                    }
                    holder.focus_header_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener!=null){
                                onItemClickListener.onPlayClick(holder.focus_header_image,position);
                            }
                        }
                    });
                }

                ArrayList<String> footprintList = thumbListDetail.getFootprintTypeList();
                for (int i = 0; i < footprintList.size(); i++) {//动态添加标签
                    if (i == 0) {
                        TextView textView = new TextView(context);
                        textView.setId(View.generateViewId());
                        tags.add(textView.getId());
                        textView.setText(footprintList.get(i));
                        textView.setGravity(Gravity.CENTER);
                        textView.setBackground(context.getResources().getDrawable(R.drawable.tag_background));
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        layoutParams.rightMargin = 30;
                        holder.focus_header_relativeLayout3.addView(textView, layoutParams);
                    }
                    if (i == 1) {
                        TextView textView = new TextView(context);
                        textView.setId(View.generateViewId());
                        tags.add(textView.getId());
                        textView.setText(footprintList.get(i));
                        textView.setGravity(Gravity.CENTER);
                        textView.setBackground(context.getResources().getDrawable(R.drawable.tag_background));
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.RIGHT_OF, tags.get(0));
                        layoutParams.rightMargin = 30;
                        holder.focus_header_relativeLayout3.addView(textView, layoutParams);
                    }
                    if (i == 2) {
                        TextView textView = new TextView(context);
                        textView.setId(View.generateViewId());
                        textView.setText(footprintList.get(i));
                        textView.setGravity(Gravity.CENTER);
                        textView.setBackground(context.getResources().getDrawable(R.drawable.tag_background));
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.RIGHT_OF, tags.get(1));
                        layoutParams.rightMargin = 30;
                        holder.focus_header_relativeLayout3.addView(textView, layoutParams);
                    }
                }

                holder.focus_header_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onBackClick(holder.focus_header_back,position);
                        }
                    }
                });

                holder.focus_header_function_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onShareClick(holder.focus_header_function_share, position);
                        }
                    }
                });

                holder.focus_header_userPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onUserInfoClick(holder.focus_header_userPhoto, position);
                        }
                    }
                });

                holder.focus_header_username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onUserInfoClick(holder.focus_header_username, position);
                        }
                    }
                });

                holder.focus_header_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onFocusClick(holder.focus_header_attention,position);
                        }
                    }
                });

            }
        }

        if ((position==list.size()+1)&&(FOOTER==holder.viewType)){

        }
    }

    @Override
    public FocusDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FocusDetailViewHolder holder;
        if (viewType==HEADER){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_detail_header,parent,false);
            holder=new FocusDetailViewHolder(view,HEADER);
            return holder;
        }
        else if (viewType==NORMAL){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
            holder=new FocusDetailViewHolder(view,NORMAL);
            return holder;
        }else {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_detail_footer,parent,false);
            holder=new FocusDetailViewHolder(view,FOOTER);
            return holder;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return HEADER;
        }
        else if (position==list.size()+1){
            return FOOTER;
        }
        else {
            return NORMAL;
        }
    }

    public class FocusDetailViewHolder extends RecyclerView.ViewHolder{
        ///////header
        public ImageView focus_header_back;
        public ImageView focus_header_function_share;
        public CircleImageView focus_header_userPhoto;
        public TextView focus_header_username;
        public ImageView focus_header_attention;
        public ViewPager focus_header_viewPager;
        public ImageView focus_header_image;
        public ImageView focus_header_play;
        public TextView focus_header_thoughts;
        public TextView focus_header_time;
        private RelativeLayout focus_header_relativeLayout3;
        /////normal
        public CircleImageView comment_userPhoto;
        public TextView comment_userName;
        public TextView comment_publishtime;
        public TextView comment_content;
        public TextView comment_textView;
        public TextView comment_other_userName;
        public TextView comment_dialog;
        public int viewType;

        public FocusDetailViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            if (viewType==HEADER){
                focus_header_back=(ImageView)view.findViewById(R.id.focus_header_back);
                focus_header_function_share=(ImageView)view.findViewById(R.id.focus_header_function_share);
                focus_header_userPhoto=(CircleImageView)view.findViewById(R.id.focus_header_userPhoto);
                focus_header_username=(TextView)view.findViewById(R.id.focus_header_username);
                focus_header_attention=(ImageView)view.findViewById(R.id.focus_header_attention);
                focus_header_viewPager=(ViewPager)view.findViewById(R.id.focus_header_viewPager);
                focus_header_viewPager.setOffscreenPageLimit(9);//设置缓存的页面数量
                focus_header_image=(ImageView)view.findViewById(R.id.focus_header_image);
                focus_header_play=(ImageView)view.findViewById(R.id.focus_header_play);
                focus_header_thoughts=(TextView)view.findViewById(R.id.focus_header_thoughts);
                focus_header_time=(TextView)view.findViewById(R.id.focus_header_time);
                focus_header_relativeLayout3=(RelativeLayout)view.findViewById(R.id.focus_header_relativeLayout3);
            }
            else if (viewType==NORMAL){
                comment_userPhoto=(CircleImageView)view.findViewById(R.id.comment_userPhoto);
                comment_userName=(TextView)view.findViewById(R.id.comment_userName);
                comment_publishtime=(TextView)view.findViewById(R.id.comment_publishtime);
                comment_content=(TextView)view.findViewById(R.id.comment_content);
                comment_textView=(TextView)view.findViewById(R.id.comment_textView);
                comment_other_userName=(TextView)view.findViewById(R.id.comment_other_userName);
                comment_dialog=(TextView)view.findViewById(R.id.comment_dialog);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            onItemClickListener.onCommentItemClick(v,getLayoutPosition());
                        }
                    }
                });
            }else {

            }
        }
    }

    private class LongAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView focus_header_image;
        private String video;
        private Bitmap bitmap=null;
        private ACache mCache;
        public LongAsyncTask(Context context,ImageView focus_header_image,String video) {
            this.focus_header_image=focus_header_image;
            this.video=video;
            mCache=ACache.get(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mCache.getAsBitmap(video)==null){
                mCache.put(video,bitmap);
            }
            focus_header_image.setImageBitmap(bitmap);
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

    public class FocusPageAdapter extends PagerAdapter {
        private ArrayList<String> photoList;
        public FocusPageAdapter(ArrayList<String> photoList) {
            this.photoList = photoList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(photoList.get(position), view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public interface OnItemClickListener{
        void onBackClick(View view,int position);
        void onShareClick(View view,int position);
        void onUserInfoClick(View view,int position);
        void onFocusClick(View view,int position);
        void onPlayClick(View view,int position);
        void onCommentUserInfoClick(View view,int position);
        void onCommentItemClick(View view,int position);//评论本身的item
        void onCommentDialogClick(View view,int position);//评论对话
        void onCommentOtherUserNameClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;
}
