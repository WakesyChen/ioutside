package com.xiaoxiang.ioutside.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.circle.model.UserCircles;
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;
import com.xiaoxiang.ioutside.mine.widget.IndicatorLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wakesy on 2016/9/18.
 */
public class CommunityAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> bannerData;
    private List<UserCircles.Circle> datalist;
    public final static int TYPE_HEADER = 0;
    public final static int TYPE_NORMAL = 1;
    public final static int NOTIFY_LOGIN = 3;
    public final static int NOTIFY_TOADD = 4;
    private TextView notifyText;
    private static final String TAG = "CommunityAdapter";
    private String urls[]={"http://img1.imgtn.bdimg.com/it/u=2903919484,2781795919&fm=21&gp=0.jpg"
            };


    public CommunityAdapter() {
        bannerData=new ArrayList<>();
        bannerData.add(urls[0]);
    }

    public void setData(List<UserCircles.Circle> datas){
        datalist=datas;
        notifyDataSetChanged();
    }

    public void addData(List<UserCircles.Circle> datas){
        if (datalist!=null&&datas!=null&&datas.size()>0) {
            datalist.addAll(datas);
            notifyDataSetChanged();
        }

    }

    public List<UserCircles.Circle> getData(){
        return   datalist;

    }

    public void hideNotyfy(){

        Log.i(TAG, "onBindViewHolder: textview2---"+notifyText);
        notifyText.setVisibility(View.GONE);

    }
    //    当用户未登录或者没有选择圈子的提示

    public void showNotify(int state){
        Log.i(TAG, "onBindViewHolder: textview3"+notifyText);

        String content;
        notifyText.setVisibility(View.VISIBLE);
        content=(state==NOTIFY_LOGIN)?"哦哦，好像还没登录哦，请先登录吧~":"没有圈子怎么行？快去找点圈子吧！";
        notifyText.setText(content);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View headView = LayoutInflater.from(context).inflate(R.layout.circle_community_item_header, parent, false);
        View normalView = LayoutInflater.from(context).inflate(R.layout.circle_community_item_normal, parent, false);

        if (viewType == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(headView);
            return headerViewHolder;
        } else  {
            NormalViewHolder normalViewHolder = new NormalViewHolder(normalView);
            return normalViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder|position==0) {
            ((HeaderViewHolder) holder).community_bannerlayout.setViewUrls(bannerData);
            ((HeaderViewHolder) holder).community_bannerlayout.onDataUpdated();
            //设置指示器布局,根据数据源动态增加圆点
            ((HeaderViewHolder) holder).community_indicatorLayout.setupWithBanner(((HeaderViewHolder) holder).community_bannerlayout);
            notifyText=((HeaderViewHolder) holder).community_notify;
            Log.i(TAG, "onBindViewHolder: textview1—--"+((HeaderViewHolder) holder).community_notify);
            Log.i(TAG, "onBindViewHolder: textview1----"+notifyText);


        } else {
            if (datalist!=null&&datalist.size()>0) {
                Glide.with(context).load(datalist.get(position-1).getPhoto()).into(((NormalViewHolder) holder).community_item_img);
                ((NormalViewHolder) holder).community_item_title.setText(datalist.get(position - 1).getTitle());
                ((NormalViewHolder) holder).community_item_noteNum.setText(datalist.get(position - 1).getPostNum()+"帖");
            }
        }

    }

    @Override
    public int getItemCount() {
        if (datalist != null ) {
            return datalist.size() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }  else {
            return TYPE_NORMAL;
        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout community_item;
        private ImageView community_item_img;
        private TextView community_item_title;
        private TextView community_item_noteNum;

        public NormalViewHolder(View itemView) {
            super(itemView);
            community_item = (RelativeLayout) itemView.findViewById(R.id.community_item);
            community_item_img = (ImageView) itemView.findViewById(R.id.community_item_img);
            community_item_title = (TextView) itemView.findViewById(R.id.community_item_title);
            community_item_noteNum = (TextView) itemView.findViewById(R.id.community_item_noteNum);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private BannerLayout community_bannerlayout;
        private IndicatorLayout community_indicatorLayout;

        private RelativeLayout community_mine;
        private TextView community_notify;
        private LinearLayout four_group_ll;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            community_bannerlayout = (BannerLayout) itemView.findViewById(R.id.community_bannerlayout);
            community_indicatorLayout = (IndicatorLayout) itemView.findViewById(R.id.community_indicatorLayout);
            four_group_ll= (LinearLayout) itemView.findViewById(R.id.four_group_ll);
            community_mine = (RelativeLayout) itemView.findViewById(R.id.community_mine);
            community_notify= (TextView) itemView.findViewById(R.id.community_notify_loginOrAdd);
            community_bannerlayout.setOnBannerChangeListener(new BannerLayout.OnBannerChangeListener() {
                @Override
                public void onBannerScrolled(int position) {

                }

                @Override
                public void onItemClick(int position) {
                    if (onClickListener != null) {
                        onClickListener.onBannerClick(position);
                    }

                }
            });
//            设置四个小组的点击事件
            for (int i = 0; i < four_group_ll.getChildCount(); i++) {
                final int position = i;
                four_group_ll.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onFourGroupClick(position);
                    }
                });
            }
        }


    }



    public interface OnClickListener {

        void onBannerClick(int position);
        void onFourGroupClick(int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

    }

}
