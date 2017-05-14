package com.xiaoxiang.ioutside.homepage.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.homepage.activity.ItemWebVIew;
import com.xiaoxiang.ioutside.homepage.fragment.HomeChoiceFragment;
import com.xiaoxiang.ioutside.homepage.model.Essay;
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;
import com.xiaoxiang.ioutside.mine.widget.IndicatorLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wakesy on 2016/4/18 0018.
 */
public class HomeChoiceAdapter extends RecyclerView.Adapter<HomeChoiceAdapter.ChoiceViewHolder> implements BannerLayout.OnBannerChangeListener {
    private static final String TAG = "HomeChoiceAdapter";
    private static final int HEADER=1;
    private static final int NORMAL=2;
    private Context context;
    private ArrayList<Essay> list=new ArrayList<>();
    private FragmentActivity activity;
    private String token;//登录状态
//    轮播图片对应的跳转界面

    private List<String> bannerPhotos;//网络获取banner
    private List<String> titles;
    private List<String> bannerUrls;
    public HomeChoiceAdapter( FragmentActivity activity,String token) {
        this.activity=activity;
        this.token=token;
        notifyDataSetChanged();
    }
    public ArrayList<Essay> getDataSet() {
        return list;
    }

    //设置banner数据
    public void setBannerData(List<String> bannerPhotos, List<String> titles,List<String> bannerUrls){
        this.bannerPhotos=bannerPhotos;
        this.titles=titles;
        this.bannerUrls=bannerUrls;


    }
    //添加数据集到头部
    public void addItemToHead(int position,Essay item) {
        list.add(position,item);
        notifyItemChanged(position);
    }

    //添加单个数据集
    public void addItem(Essay item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
        return HEADER;
        else
            return NORMAL;
    }

    @Override
    public int getItemCount(){
        return list.size()+1;
    }



    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if (viewType == HEADER) {//
            context=parent.getContext();
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.homechoice_head_banner, parent, false);
            ChoiceViewHolder holder = new ChoiceViewHolder(itemView, HEADER);

            return holder;
        } else {
            Log.i(TAG, "onCreateViewHolder: normal");

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_choice_item, parent, false);
            ChoiceViewHolder  holder = new ChoiceViewHolder(itemView, NORMAL);
            return holder;
        }
    }


    @Override
    public void onBindViewHolder(final ChoiceViewHolder holder, final int position) {
        if (position != 0  && NORMAL == holder.itemType) {

        Essay essay = list.get(position-1);//bug出现点，不减1下标越界
        boolean liked = essay.isLiked();
        boolean original = essay.isOriginal();

//
//        holder.choice_userPhoto.setImageResource(R.drawable.account_bitmap);
//        holder.choice_image.setImageResource(R.drawable.account_bitmap);
            Glide.with(activity).load(essay.getPhoto()).into(holder.choice_image);
            Glide.with(activity).load(essay.getUserPhoto()).into(holder.choice_userPhoto);
//        ImageLoader.getInstance().displayImage(essay.getPhoto(), holder.choice_image);
//        ImageLoader.getInstance().displayImage(essay.getUserPhoto(), holder.choice_userPhoto);
        holder.choice_userName.setText(essay.getUserName());
        holder.choice_title.setText(essay.getTitle() + "");
        holder.choice_likedCount.setText(essay.getLikedCount() + "");
        holder.choice_commentCount.setText(essay.getCommentCount() + "");
        holder.choice_publishTime.setText(essay.getRecommendTime().substring(0, 10));
        if (Html.fromHtml(essay.getContent()).length() > 35)
            holder.choice_content.setText(Html.fromHtml(essay.getContent()).toString().substring(0, 35) + "...");
        else
            holder.choice_content.setText(Html.fromHtml(essay.getContent()));

        if (original == true)
            holder.choice_recommend.setText("原创");

        else
            holder.choice_recommend.setText("推荐");

        if (liked == false)
            holder.choice_function_zan.setImageResource(R.drawable.zan_normal);

        else
            holder.choice_function_zan.setImageResource(R.drawable.zan_pressed);


        //点赞
        holder.choice_function_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onZanClick(holder.choice_function_zan, position);//适配器增加了一个head，position+1，要剪掉
                }
            }
        });

        //评论
        holder.choice_function_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onCommentClick(holder.choice_function_comment, position);
                }
            }
        });

        //头像
        holder.choice_userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onUserInfoClick(holder.choice_userPhoto, position);//适配器增加了一个head，数据源对应的数据postion要减1
                }
            }
        });

        //昵称
        holder.choice_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onUserInfoClick(holder.choice_userName, position);
                }
            }
        });
    }
        //增加轮播图片的头布局
    if (position==0&&HEADER==holder.itemType){
//        给轮播布局设置监听事件、数据源
        holder.bannerLayout.setOnBannerChangeListener(HomeChoiceAdapter.this);
//       holder.bannerLayout.setViewRes(imgIdlist);//设置本地图片
        if (bannerPhotos != null && bannerPhotos.size() > 0) {
            holder.bannerLayout.setViewUrls(bannerPhotos);//设置网络图片
            holder. bannerLayout.onDataUpdated();
            //设置指示器布局,根据数据源动态增加圆点
            holder.indicatorLayout.setupWithBanner(holder.bannerLayout);
        }







    }
    }


    //bannerlayout的滑动监听
    @Override
    public void onBannerScrolled(int position) {

    }
//bannerlayout的点击监听，跳转到对应的界面
    @Override
    public void onItemClick(int position) {
//        获取当前图片position
                int index=position%titles.size();
                String photo=bannerPhotos.get(index);
                String url=bannerUrls.get(index);
                String title=titles.get(index);
                Intent intent=new Intent(context, ItemWebVIew.class);
                intent.putExtra("token",token);
                intent.putExtra("photo",photo);
                intent.putExtra("index",index);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                context.startActivity(intent);


    }

    class ChoiceViewHolder extends RecyclerView.ViewHolder{

//        Header的控件
        public BannerLayout bannerLayout;
        public IndicatorLayout indicatorLayout;

//        Normal
        public CircleImageView choice_userPhoto;
        public TextView choice_userName;
        public TextView choice_recommend;
        public ImageView choice_image;
        public TextView choice_title;
        public TextView choice_content;
        public ImageView choice_function_zan;
        public TextView choice_commentCount;
        public ImageView choice_function_comment;
        public TextView choice_likedCount;
        private TextView choice_publishTime;
        public int itemType;
        public ChoiceViewHolder(View view,int itemType){
            super(view);
            this.itemType=itemType;
            if (itemType==HEADER) {
                bannerLayout= (BannerLayout) view.findViewById(R.id.choice_bannerlayout);
                indicatorLayout= (IndicatorLayout) view.findViewById(R.id.choice_indicatorLayout);

            } else  {
                choice_userPhoto = (CircleImageView) view.findViewById(R.id.choice_userPhoto);
                choice_userName = (TextView) view.findViewById(R.id.choice_userName);
                choice_recommend = (TextView) view.findViewById(R.id.choice_recommend);
                choice_image = (ImageView) view.findViewById(R.id.choice_image);
                choice_title = (TextView) view.findViewById(R.id.choice_text);
                choice_content = (TextView) view.findViewById(R.id.choice_content);
                choice_function_zan = (ImageView) view.findViewById(R.id.choice_function_zan);
                choice_likedCount = (TextView) view.findViewById(R.id.choice_likedCount);
                choice_function_comment = (ImageView) view.findViewById(R.id.choice_function_comment);
                choice_commentCount = (TextView) view.findViewById(R.id.choice_commentCount);
                choice_publishTime = (TextView) view.findViewById(R.id.choice_publishTime);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, getLayoutPosition());
                        }
                    }
                });
            }

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onZanClick(View view, int position);
        void onCommentClick(View view, int position);
        void onUserInfoClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

}
