package com.xiaoxiang.ioutside.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.ActivityDetail;
import com.xiaoxiang.ioutside.activities.model.QaData;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;
import com.xiaoxiang.ioutside.mine.widget.IndicatorLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wakesy on 2016/8/15.
 */
public class QaRecyclerAdapter extends RecyclerView.Adapter<QaRecyclerAdapter.QaViewHodler> {
  private  static final int HEADER=1;
  private  static final int NORMAL=2;
    private static final String TAG = "QaRecyclerAdapter";
    private List<QaData> list;//得到qa数据源
    public List<String > photolist;
    private TextView tv_type;//显示时间和类型
    private TextView detail_number;
    private ActivityDetail activityDetail;

    public TextView getTv_type() {
        return tv_type;
    }

    public void setTv_type(TextView tv_type) {
        this.tv_type = tv_type;
    }

    public void addItem(QaData item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void setListData(List<QaData> list) {
        this.list=list;
        notifyDataSetChanged();

    }
    public List<QaData>  getListData() {
       return  list;

    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return NORMAL;
        }
    }
    public void setHeaderData(ActivityDetail activityDetail) {
        this.activityDetail = activityDetail;
        if (activityDetail !=null) {
            photolist= activityDetail.getPhotoList();//获取图片数据
        }

    }
    public ActivityDetail getHeaderData() {
       return activityDetail;

    }

    public void setRemainNum(TextView detail_number) {
        this.detail_number=detail_number;

    }
    public TextView getRemainNum() {
        return  detail_number;
    }
    public void onBindViewHolder(QaViewHodler holder, int position) {
//
        if (position == 0 && holder.type == HEADER) {//轮播图等信息
            if (photolist!=null&&photolist.size()>0) {
                holder.detail_banner.setViewUrls(photolist);
                holder.detail_banner.onDataUpdated();
                holder.detail_banner_indicator.setupWithBanner(holder.detail_banner);
            }
            if (activityDetail !=null) {
                holder.deatil_title.setText(activityDetail.getTitle()+"");
                holder.detail_number.setText("名额剩余"+ activityDetail.getRemainNum()+"人");

                setRemainNum( holder.detail_number);
                holder.detail_priceNow.setText("¥"+ activityDetail.getDiscountPrice()+"");
                holder.detail_priceBefore.setText("¥"+ activityDetail.getPrice()+"");
                holder.detail_from.setText(activityDetail.getSellerName()+"");
                holder.detail_startPlace.setText(activityDetail.getStartPlace()+"出发");
                holder.detail_type.setText(activityDetail.getStartDate()+" "+ activityDetail.getStandard());
                setTv_type(holder.detail_type);
//                选择规格
                holder.detail_chooseType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailClickListener!=null) {
                            mOnDetailClickListener.onTypeClick();//通过接口给主界面赋值给textview
                        }
                    }
                });
//                行程介绍
                holder.detail_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailClickListener!=null) {
                            mOnDetailClickListener.onIntroClick();
                        }
                    }
                });
//                费用说明
                holder.detail_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailClickListener!=null) {
                            mOnDetailClickListener.onPriceClick();
                        }
                    }
                });
                //               报名须知
                holder.detail_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailClickListener!=null) {
                            mOnDetailClickListener.onSignKnoClick();
                        }
                    }
                });
                //              体验评价
                holder.detail_img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailClickListener!=null) {
                            mOnDetailClickListener.onCommentClick();
                        }
                    }
                });
            }


        } else {
            if (list != null && list.size() > 0) {
                QaData qa = list.get(position - 1);
                holder.qa_tv_q.setText(qa.getQuestion());
                holder.qa_tv_a.setText(qa.getAnswer());
                holder.qa_tv_time.setText(qa.getAddTime());
            } else {
                holder.qa_tv_q.setText("面包会有的");
                holder.qa_tv_a.setText("牛奶也会有的");
                holder.qa_tv_time.setText("2015-12-12");
            }
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + 1;
        } else {
            return  1;
        }
    }

    @Override
    public QaViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity3_deatil_header, null);
            QaViewHodler holder = new QaViewHodler(view, HEADER);
            return holder;
        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_qa_item, null);
//            必须添加父类，不然item显示不全
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_qa_item, parent,false);
            QaViewHodler holder1 = new QaViewHodler(view, NORMAL);

            return holder1;
        }
    }

    public class QaViewHodler extends RecyclerView.ViewHolder {
//        头布局控件
        private BannerLayout detail_banner;
        private IndicatorLayout detail_banner_indicator;

        private TextView deatil_title;
        private TextView detail_number;//活动剩余人数
        private TextView detail_priceNow;
        private TextView detail_priceBefore;
        private TextView detail_from;
        private TextView detail_startPlace;
        private TextView detail_type;
        private LinearLayout detail_chooseType;//下页箭头去选择规格
        private LinearLayout detail_img1,detail_img2,detail_img3,detail_img4;//四个图文布局

//        item布局
        private TextView qa_tv_q;//问
        private TextView qa_tv_a;
        private TextView qa_tv_time;
        private int type;
        public QaViewHodler(View view,int type) {
            super(view);
            this.type=type;
            if (type == HEADER) {
                detail_banner = (BannerLayout) view.findViewById(R.id.activity_detail_banner);
                detail_banner_indicator = (IndicatorLayout) view.findViewById(R.id.activity_banner_indicator);

                deatil_title = (TextView) view.findViewById(R.id.activity_detail_title);
                detail_number = (TextView) view.findViewById(R.id.activity_detail_num);
                detail_priceNow = (TextView) view.findViewById(R.id.activity_detail_priceNow);
                detail_priceBefore = (TextView) view.findViewById(R.id.activity_detail_priceBefore);
                detail_from = (TextView) view.findViewById(R.id.activity_detail_from);
                detail_startPlace = (TextView) view.findViewById(R.id.activity_detail_startPlace);
                detail_type = (TextView) view.findViewById(R.id.activity_detail_type);
                detail_chooseType = (LinearLayout) view.findViewById(R.id.activity_detail_chooseType);
                detail_img1 = (LinearLayout) view.findViewById(R.id.acitvity_detail_img1);
                detail_img2 = (LinearLayout) view.findViewById(R.id.acitvity_detail_img2);
                detail_img3 = (LinearLayout) view.findViewById(R.id.acitvity_detail_img3);
                detail_img4 = (LinearLayout) view.findViewById(R.id.acitvity_detail_img4);

            } else {
                qa_tv_q= (TextView) view.findViewById(R.id.qa_tv_q);
                qa_tv_a= (TextView) view.findViewById(R.id.qa_tv_a);
                qa_tv_time= (TextView) view.findViewById(R.id.qa_tv_time);
            }
        }
    }

    //    定义接口
    public interface OnDetailClickListener {
        void onTypeClick(); //选择时间规格
        void onIntroClick();//行程介绍
        void onPriceClick();//费用说明
        void onSignKnoClick();//报名须知
        void onCommentClick();//体验评价

    }
    public void setOnDetailClickListener(OnDetailClickListener mOnDetailClickListener) {
        this.mOnDetailClickListener=mOnDetailClickListener;
    }
    private OnDetailClickListener mOnDetailClickListener;
}
