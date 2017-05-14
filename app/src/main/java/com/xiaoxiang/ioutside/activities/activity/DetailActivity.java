package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.adapter.QaRecyclerAdapter;
import com.xiaoxiang.ioutside.activities.adapter.RecyclerTimeAdapter;
import com.xiaoxiang.ioutside.activities.adapter.TypeBaseAdapter;
import com.xiaoxiang.ioutside.activities.model.ActivityDetail;
import com.xiaoxiang.ioutside.activities.model.GActivityDetail;
import com.xiaoxiang.ioutside.activities.model.GActivityStandard;
import com.xiaoxiang.ioutside.activities.model.GModuleId;
import com.xiaoxiang.ioutside.activities.model.GQaData;
import com.xiaoxiang.ioutside.activities.model.ModuleIds;
import com.xiaoxiang.ioutside.activities.model.OrderInfor;
import com.xiaoxiang.ioutside.activities.model.QaData;
import com.xiaoxiang.ioutside.activities.model.StandardList;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.common.methods.UmengShare;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.NetworkUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/15.
 */
public class DetailActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.activity_detail_back)
    ImageView detail_back;
    @Bind(R.id.activity_detail_share)
    ImageView detail_share;
    @Bind(R.id.activity_qa_recyclerView)
    RecyclerView qa_recyclerView;
    @Bind(R.id.btn_collect)
    ImageView btn_collect;
    @Bind(R.id.btn_signNow)
    TextView btn_signNow;
    @Bind(R.id.tv_activity_notfound)
    TextView tv_activity_notfound;
//    @Bind(R.id.activity_refresh)
//    SwipeRefreshLayout swipeRefreshLayout;
    private QaRecyclerAdapter mAdapter;
    private int activityId = 141;//活动id,172,162,141, 68
    private int pageNo = 1;
    private int pageSize = 10;
    private String token;
    private String filetoken;
    private ActivityDetail activityDetail;//活动详情
    private List<Integer>activityModuleIds;//活动模块Id
    private List<String>activityModuleNames;//活动模块Id
    private boolean stopRefresh;//停止刷新标识
    private boolean stopRefresh1;//停止刷新标识
    private boolean stopRefresh2;//停止刷新标识
    private  boolean isCollected;//收藏状态
    //    private List<String> photolist=new ArrayList<>();//banner轮播图
    private static final String TAG = "DetailActivity";
//    popwindow相关
    private View popView;//popwindow视图
    private PopupWindow popwindow;
    private ImageView month_decre;
    private ImageView month_add;
    private TextView activity_time;
    private RecyclerView recyclerView_choose_time;
    private GridView gridView_choose_type;
    private TextView choose_done;
//    private List<String>typelist;//活动类型的数据源
    private TypeBaseAdapter typeAdapter;
    private RecyclerTimeAdapter recyclerTimeAdapter;
    private int year;//选择的时间
    private int month;
    private int day=1;
    private String week;
    private int timeIndex;//选择的时间position
    private int typeIndex;//选择的类型position
    private int currentYear;//当前时间
    private int currentMonth;
    private int standardId;//活动规格id
    private String sellerId;//活动商家id
    private OrderInfor orderInfor;//此页传递到下页的订单信息

    private boolean isChoosed=false;//标识是否已经选择出行日期
    private    OkHttpManager okHttpManager;
    private ApiInterImpl api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getIntentData();

        if (token==null) {
            noLogin();
        }
//        Log.i(TAG, "onCreate: token"+token);//d29GscwLNnlm5MQ9j58AmEtnlY8ZPA4uaJFikfxiI2zTR9zCIMjsPE3gIXLl/EOA
        initView();
        initData();
        initEvent();
    }



    private void initView() {



//        popwindow相关
        popView= LayoutInflater.from(this).inflate(R.layout.activity_detail_popwindow,null);
        month_decre= (ImageView) popView.findViewById(R.id.month_decre);
        month_add= (ImageView) popView.findViewById(R.id.month_add);
        activity_time= (TextView) popView.findViewById(R.id.activity_time);
        recyclerView_choose_time= (RecyclerView) popView.findViewById(R.id.recyclerview_choose_time);
        gridView_choose_type= (GridView) popView.findViewById(R.id.gridview_type);
        choose_done= (TextView) popView.findViewById(R.id.choose_done);



    }


    private void initEvent() {

        //      活动时间表，根据活动规格设置数据源
        recyclerTimeAdapter=new RecyclerTimeAdapter();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_choose_time.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView_choose_time.setHasFixedSize(true);
        recyclerView_choose_time.setLayoutManager(linearLayoutManager);
        recyclerView_choose_time.setAdapter(recyclerTimeAdapter);
        recyclerTimeAdapter.setIndex(0);

//      活动规格列表,点击规格弹出对应的时间选择
            typeAdapter=new TypeBaseAdapter(DetailActivity.this);
            gridView_choose_type.setAdapter(typeAdapter);

        gridView_choose_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeIndex=position;
                recyclerTimeAdapter.setIndex(position);
                recyclerView_choose_time.setAdapter(recyclerTimeAdapter);

                int size=gridView_choose_type.getChildCount();
                for (int i=0;i<size;i++) {// 先初始化所有item背景
                    View itemview=gridView_choose_type.getChildAt(i);
                    TextView type= (TextView) itemview.findViewById(R.id.standard_item_name);
                    type.setBackgroundResource(R.drawable.typeunchoosed);
                }
                TextView type= (TextView) view.findViewById(R.id.standard_item_name);
                type.setBackgroundResource(R.drawable.typechoosed);
            }
        });
//        时间选择的点击事件
        recyclerTimeAdapter.setOnItemClickListener(new RecyclerTimeAdapter.OnItemClickListener() {
            @Override
            public void onclick(View view, int position) {
                timeIndex=position;
                LinearLayout activity_item_recycler;
                    int size=recyclerView_choose_time.getChildCount();
//                先初始化所有item背景
                for (int i = 0; i < size; i++) {
                    View itemview=recyclerView_choose_time.getChildAt(i);
                   activity_item_recycler= (LinearLayout) itemview.findViewById(R.id.activity_item_recycler);
                    activity_item_recycler.setBackgroundResource(R.drawable.timeunchoose_bg);
                }
//                点击的item背景变黄
               activity_item_recycler= (LinearLayout) view.findViewById(R.id.activity_item_recycler);
                    activity_item_recycler.setBackgroundResource(R.drawable.timechoosed_bg);

            }
        });


        qa_recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        qa_recyclerView.setHasFixedSize(true);
        qa_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        qa_recyclerView.setAdapter(mAdapter);



        detail_back.setOnClickListener(this);
        detail_share.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        btn_signNow.setOnClickListener(this);

        month_decre.setOnClickListener(this);
        month_add.setOnClickListener(this);
        choose_done.setOnClickListener(this);
    }

    private void initData() {
        orderInfor=new OrderInfor();
        orderInfor.setRemainNum(0);

        //            首次加载当前月份的活动
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        currentYear=year;
        currentMonth=month;



//        主适配器里面的接口回调
        mAdapter = new QaRecyclerAdapter();
        mAdapter.setOnDetailClickListener(new QaRecyclerAdapter.OnDetailClickListener() {
            @Override
            public void onTypeClick() {//选择时间规格类型
                popWindChooseType();//选择规格的弹出框

            }


            @Override
            public void onIntroClick() {//活动介绍
                if (activityModuleNames!=null&&activityModuleNames.size()>=1) {
                    String title=activityModuleNames.get(0);
                    int moduleId=activityModuleIds.get(0);
                    String url="http://ioutside.com/xiaoxiang-backend/activity/descript-modules/"+moduleId+".html";
                    Intent intent=new Intent(DetailActivity.this,ModuleWebActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }

            }


            @Override
            public void onPriceClick() {//费用须知
                if (activityModuleNames!=null&&activityModuleNames.size()>=2) {
                    String title=activityModuleNames.get(1);
                    int moduleId=activityModuleIds.get(1);
                    String url="http://ioutside.com/xiaoxiang-backend/activity/descript-modules/"+moduleId+".html";
                    Intent intent=new Intent(DetailActivity.this,ModuleWebActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }
            }

            @Override
            public void onSignKnoClick() {//报名须知
                if (activityModuleNames!=null&&activityModuleNames.size()>=3) {
                    String title=activityModuleNames.get(2);
                    int moduleId=activityModuleIds.get(2);
                    String url="http://ioutside.com/xiaoxiang-backend/activity/descript-modules/"+moduleId+".html";
                    Intent intent=new Intent(DetailActivity.this,ModuleWebActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }
            }

            @Override
            public void onCommentClick() {//体验评论
                if (activityModuleNames!=null&&activityModuleNames.size()>=4) {
                    String title=activityModuleNames.get(3);
                    int moduleId=activityModuleIds.get(3);
                    String url="http://ioutside.com/xiaoxiang-backend/activity/descript-modules/"+moduleId+".html";
                    Intent intent=new Intent(DetailActivity.this,ModuleWebActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }
            }
        });
//        获得活动详情数据
            onRefresh();
//        SwipeRefreshLayout.OnRefreshListener onRefreshListener=new SwipeRefreshLayout.OnRefreshListener() {
//            @Override

//        };
//
//        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
////        swipeRefreshLayout.listener到此
//        setSwipeRefreshStyle();
//        swipeRefreshLayout.post(new Runnable() {//首页一加载就刷新
//            @Override
//            public void run() {
//           swipeRefreshLayout.setRefreshing(true);
//            }
//        });
//        onRefreshListener.onRefresh();





    }




    public void onRefresh() {


        if (NetworkUtil.isNetworkConnected(DetailActivity.this)) {
           okHttpManager=OkHttpManager.getInstance();
            api=new ApiInterImpl();

//        获取活动详情数据源

            okHttpManager.getStringAsyn(api.getActivityDetail(activityId, token), new OkHttpManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);
                    ToastUtils.show("网络有点问题哦");
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.i(TAG, "activityDetail:"+response);
                    Gson gson = new Gson();
                    Type type = new TypeToken<GActivityDetail>() {
                    }.getType();
                    GActivityDetail gActivityDetail = gson.fromJson(response, type);
                    activityDetail = gActivityDetail.getData().getActivity();
                    if (activityDetail != null) {
                        mAdapter.setHeaderData(activityDetail);//把数据源传给适配器
                        isCollected = activityDetail.isCollected();//获取收藏状态

//                                    订单数据
                        orderInfor.setTitle(activityDetail.getTitle()+"");
                        orderInfor.setSellerPhone(activityDetail.getSellerPhone()+"");
                        orderInfor.setStartPlace(activityDetail.getStartPlace()+"");
                        orderInfor.setDiscountPrice(activityDetail.getDiscountPrice());
                        orderInfor.setActivityId(activityDetail.getActivityId());
                        orderInfor.setSellerID(activityDetail.getSellerId());

                        //        收藏状态
                        if (activityDetail != null && activityDetail.isCollected()) {
                            btn_collect.setImageResource(R.drawable.activity_collect);
                        } else {
                            btn_collect.setImageResource(R.drawable.activity_uncollect);

                        }
                    } else {//没有此活动，隐藏控件
                        tv_activity_notfound.setVisibility(View.VISIBLE);
                        qa_recyclerView.setVisibility(View.GONE);
                        detail_share.setVisibility(View.GONE);
                        btn_signNow.setEnabled(false);
                    }
//          此时获取到活动详情，再设置QA适配器
                getQaData();

                }

            });




//            通过ActivityId获取到ActivityModuleId


            okHttpManager.getStringAsyn(api.getActivityModuleId(activityId),new OkHttpManager.ResultCallback<String>(){

                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);
                    ToastUtils.show("网络有点问题哦");

                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.i(TAG, "onResponse: "+response);
                    Log.i(TAG, "token:"+token);
                    Gson gson=new Gson();
                    Type type=new TypeToken<GModuleId>(){}.getType();
                    GModuleId gModuleId=gson.fromJson(response,type);
                    ModuleIds moduleIds=  gModuleId.getData();
                    if (moduleIds!=null&&moduleIds.getList().size()>0) {
                        activityModuleIds=new ArrayList<Integer>();//加入moudleId
                        activityModuleNames=new ArrayList<String>();//加入moudleNames
                        for (int i=0;i<moduleIds.getList().size();i++) {
                            activityModuleIds.add(moduleIds.getList().get(i).getId());
                            activityModuleNames.add(moduleIds.getList().get(i).getName());
                        }
                    }


                }
            });




        }


    }

    private void getQaData() {
        //          获取QA数据源

        okHttpManager.getStringAsyn(api.getActivityQa(activityId, pageNo, pageSize), new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                ToastUtils.show("网络出现故障");
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onResponse: Json" + response);
                Gson gson = new Gson();
                Type type = new TypeToken<GQaData>() {
                }.getType();
                GQaData GQaData = gson.fromJson(response, type);
                List<QaData> qaListItemList = GQaData.getData().getList();

                mAdapter.setListData(qaListItemList);
                qa_recyclerView.setAdapter(mAdapter);


            }
        });




    }

    //    四个按键的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_detail_back://返回
                finish();
                break;

            //分享,参数，photo,title,content,url;
            case R.id.activity_detail_share:
            {
                String photo = activityDetail.getPhotoList().get(0);
                String title = activityDetail.getTitle();
                String content = activityDetail.getContent();
                int moduleId = 2;
                if (activityModuleIds != null && activityModuleIds.size() > 0) {
                    moduleId = activityModuleIds.get(0);
                }//活动描述的url

//                String url1 = "http://ioutside.com/xiaoxiang-backend/activity/descript-modules/" + moduleId + ".html";
                String url="http://ioutside.com/xiaoxiang-backend/activity-share?activityID="+activityId;
                UmengShare.setShareContent(DetailActivity.this, title, url, photo, content);

                ApiInterImpl api = new ApiInterImpl();
                OkHttpManager okHttpManager = OkHttpManager.getInstance();
//              第三个参数  类型-->1:微信;2:qq;3:微博,暂时写2
                okHttpManager.getStringAsyn(api.shareActivityDetail(activityId, token, 2), new OkHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        ToastUtils.show("网络有点问题哦");
                    }

                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        //   暂不做处理

                    }
                });
            }
                break;
            //收藏
            case R.id.btn_collect: {
                if (activityDetail != null) {
                    ApiInterImpl api = new ApiInterImpl();
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
//                    当前是未收藏
                    if (isCollected == false) {
                        okHttpManager.postAsyn(api.getColleted(activityId, token), new OkHttpManager.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                super.onError(request, e);
                                ToastUtils.show("网络有点问题哦");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                boolean isSuccess = true;
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    isSuccess = jsonObject.getBoolean("success");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (isSuccess) {//访问成功切换状态
                                    ToastUtils.show("收藏成功！");
                                    btn_collect.setImageResource(R.drawable.activity_collect);
                                    isCollected=true;//本地置为true;
                                }
                            }

                        });

                    }
//                    当前是已经收藏
                    else {
                        okHttpManager.postAsyn(api.getCancelColleted(activityId, token), new OkHttpManager.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                super.onError(request, e);
                                ToastUtils.show("网络有点问题哦");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                boolean isSuccess = true;
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    isSuccess = jsonObject.getBoolean("success");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (isSuccess) {//访问成功切换状态
                                    ToastUtils.show("取消收藏");
                                    isCollected=false;//本地置为false;
                                    btn_collect.setImageResource(R.drawable.activity_uncollect);
                                }
                            }

                        });

                    }
                }
            }

                break;
            //立即报名
            case R.id.btn_signNow:
                if (token == null) {
                    noLogin();
                } else {
                    if (isChoosed) {
                        if (orderInfor.getRemainNum() < 1) {
                            ToastUtils.show("sorry，此活动暂无名额了哦");
                        } else {
                            Intent intent = new Intent(this, OrderPostActivity.class);
                            intent.putExtra("orderInfor", orderInfor);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                    } else {
                        ToastUtils.show("请先选择出行日期，规格！");
                    }
                }

                break;

            case R.id.month_decre:
                if (year==currentYear) {
                    if (month == currentMonth) {
                        ToastUtils.show("不能选择之前的活动");
                    } else {
                        month--;
                    }
                }
                if (year>currentYear) {
                    if (month == 1) {
                        month = 12;
                        year--;
                    } else {
                        month--;
                    }
                }
                activity_time.setText(year+"年"+month+"月");
                updateChooseInfo(activityId,getStartDate(year,month,day),getEndDate(year,month,day));
                //      活动时间表，根据活动规格设置数据源
                gridView_choose_type.setAdapter(typeAdapter);
                recyclerTimeAdapter.setIndex(0);//设置默认的第一个数据
                if (gridView_choose_type.getChildCount()>0) {
                    View itemview=gridView_choose_type.getChildAt(0);
                    TextView type= (TextView) itemview.findViewById(R.id.standard_item_name);
                    type.setBackgroundResource(R.drawable.typeunchoosed);
                }
                recyclerView_choose_time.setAdapter(recyclerTimeAdapter);
                break;

            case R.id.month_add:

                month++;
                if (month==13) {
                    month=1;
                    year++;
                }
                activity_time.setText(year+"年"+month+"月");
                updateChooseInfo(activityId,getStartDate(year,month,day),getEndDate(year,month,day));
                //      活动时间表，根据活动规格设置数据源

                gridView_choose_type.setAdapter(typeAdapter);
                recyclerTimeAdapter.setIndex(0);//设置默认的第一个数据
                if (gridView_choose_type.getChildCount()>0) {

                    View itemview=gridView_choose_type.getChildAt(0);
                    TextView type= (TextView) itemview.findViewById(R.id.standard_item_name);
                    type.setBackgroundResource(R.drawable.typeunchoosed);
                }
                recyclerView_choose_time.setAdapter(recyclerTimeAdapter);

                break;

            case R.id.choose_done://选择时间规格完成
                TextView tv_type=mAdapter.getTv_type();
                if (recyclerTimeAdapter.getStandardLists() != null && recyclerTimeAdapter.getStandardLists().size() > 0 &&
                        recyclerTimeAdapter.getStandardLists().get(typeIndex).getTimeList() != null &&
                        recyclerTimeAdapter.getStandardLists().get(typeIndex).getTimeList().size() > 0) {

                    String time = recyclerTimeAdapter.getStandardLists().get(typeIndex).getTimeList().get(timeIndex).getStartDate();
                    String type = typeAdapter.getTypelistData().get(typeIndex);
                    int remainNum = recyclerTimeAdapter.getStandardLists().get(typeIndex).getTimeList().get(timeIndex).getRemainNum();
//                    得到活动规格id以及活动时间
                    standardId=recyclerTimeAdapter.getStandardLists().get(typeIndex).getStandardId();
//设置剩余名额
                    mAdapter.getRemainNum().setText("名额剩余"+ remainNum+"人");

                    orderInfor.setActivitySpecID(standardId);
                    tv_type.setText(time + " " + type);
                    orderInfor.setStartDate(time+"");//设置订单的时间，剩余名额，类型
                    orderInfor.setRemainNum(remainNum);
                    orderInfor.setContent(type+"");

                    isChoosed=true;//选择完毕

                } else {
                    return;
                }
                popwindow.dismiss();


                break;
        }
    }



    private void popWindChooseType() {

        Log.i(TAG, "popWindChooseType: time:"+getStartDate(year,month,day));
        
        activity_time.setText(year+"年"+month+"月");
        String startDate=getStartDate(year,month,day);
        String endDate=getEndDate(year,month,day);

        updateChooseInfo(activityId,startDate,endDate);//    根据月份获取对应活动规格

        popwindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popwindow.setFocusable(true);
        popwindow.setOutsideTouchable(true);
       popwindow.setBackgroundDrawable(new BitmapDrawable());
        popwindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
        popwindow.setOutsideTouchable(false);

//        //        当弹出框消失，初始化数据
//        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                month=currentMonth;
//                year=currentYear;
//                activity_time.setText(year+"年"+month+"月");
//
//            }
//        });


    }

//    根据月份获取对应活动规格
    private void updateChooseInfo(int activityId, String startDate, String endDate) {

        ApiInterImpl api=new ApiInterImpl();
        OkHttpManager okHttpManager=OkHttpManager.getInstance();

        okHttpManager.getStringAsyn(api.getActivityTypeByMonth(activityId,startDate,endDate),new OkHttpManager.ResultCallback<String>(){

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "updateChooseInfo: "+response);
              Type type=new TypeToken<BaseResponse<GActivityStandard>>(){}.getType();
                Gson gson=new Gson();
                BaseResponse baseResponse=gson.fromJson(response,type);//获得最外层的类
                GActivityStandard gActivityStandard= (GActivityStandard) baseResponse.getData();//获得data类
                List<StandardList> standardLists=gActivityStandard.getList();

                List<String> typelist=new ArrayList<String>();
                if (standardLists != null && standardLists.size() > 0) {


                    //添加gridview类型数据源
                    for (StandardList standard : standardLists) {
                        typelist.add(standard.getContent());

                    }
                    typeAdapter.setTypelistData(typelist);

                    recyclerTimeAdapter.setStandardLists(standardLists);


                } else {
                    typeAdapter.setTypelistData(null);

                    recyclerTimeAdapter.setStandardLists(null);
                }

            }
        });

    }

    //
    public String  getStartDate(int year, int month, int day) {
        return year+"-"+month+"-"+day;
    }
    public String  getEndDate(int year, int month, int day) {
        return year+"-"+(month+1)+"-"+day;
    }


    public void getIntentData() {
        token=getIntent().getStringExtra("token");
        activityId=getIntent().getIntExtra("activityId",activityId);//给个默认的活动id
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null) {
            filetoken= cachedInfo.getToken();
        }
        if (token==null) {
            token=filetoken;
        }

    }
    public void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }


}
