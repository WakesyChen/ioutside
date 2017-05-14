package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.mine.model.Recommend;
import com.xiaoxiang.ioutside.mine.widget.Adapter.BaseRecvAdapter;
import com.xiaoxiang.ioutside.mine.widget.Adapter.RecyclerViewHolder;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyRecomList;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class MyEssayActivity extends Activity  implements Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    @Bind(R.id.tv_nothis_myessay)
    TextView tvNothing;
    @Bind(R.id.tv_remend_myessay)
    TextView tvRemendMyessay;
    @Bind(R.id.swrf_myessay)
    SwipeRefreshLayout swrfMyessay;
    private String TAG = getClass().getSimpleName();

    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.recv_myessay)
    RecyclerView recvMyEssay;
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.top_one_article)
    TextView tvMyRecom;
    @Bind(R.id.top_one_topic)
    TextView tvMyOrigin;

    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse mRecomRe;

    private BaseRecvAdapter mRecomAdapter;
    //测试用token
    private String token;
    private int userId;
    private ArrayList<Recommend> mRecomEssays;

    private int CURRENT_PAGE=1;
    private int CURRENT_PAGE2=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_essay);
        ButterKnife.bind(this);
        token = getIntent().getStringExtra("token");
        userId=getIntent().getIntExtra("userId",-1);
        Log.d(TAG, "token" + token);
        tvTitleSetting.setText("我的文章");
        tvMyRecom.setText("我的推荐");
        tvMyOrigin.setText("我的原创");
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        recvMyEssay.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "运行到这里了");
        loadMyRecom();
    }

    public void loadMyRecom() {
        if (NetworkUtil.isNetworkConnected(this)) {
            //绑定adapter的一些处理
            mRecomAdapter = new BaseRecvAdapter<Recommend>(R.layout.item_my_collection) {
                @Override
                public void onBindData(RecyclerViewHolder viewHolder, int position, Recommend item) {
                    viewHolder.itemView.findViewById(R.id.lay_header).setVisibility(View.GONE);
                    viewHolder.setText(R.id.tv_title, item.getTitle());
                    ImageView ivPhoto = (ImageView) viewHolder.findViewById(R.id.iv_photo);
                    Glide.with(MyEssayActivity.this).load(item.getPhoto()).into(ivPhoto);
//                    viewHolder.setDrawableLoader(R.id.iv_photo, item.getPhoto(), ImageLoader.getInstance());
                    viewHolder.setText(R.id.tv_count_liked, item.getLikedCount());
                    viewHolder.setText(R.id.tv_count_comment, item.getCommentCount());
                }
            };
            mRecomAdapter.setOnItemClickListener(new BaseRecvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    switch (v.getId()) {
                        case R.id.item_my_collection:
                            Recommend essay=(Recommend) mRecomAdapter.getmDataSet().get(position);
                            int id=essay.getId();
                            Intent intent=new Intent(MyEssayActivity.this, ArticleDetailActivity.class);
                            intent.putExtra("id",id+"");
                            startActivity(intent);
                            break;
                    }
                }
            });
            recvMyEssay.setAdapter(mRecomAdapter);
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                String myRecom="";
                @Override
                public void onRefresh() {
                    if (token!=null&&userId==-1){                               //已登录看自己
                        myRecom = apiImpl.getMyReCommendIn(10, 1, token);
                    }else if(token!=null){                                      //已登录看别人
                        myRecom=apiImpl.getOtherRecommendIn(10,1,userId);
                    }
                    Log.d(TAG, myRecom);
                    mOkHttpManager.getStringAsyn(myRecom, new OkHttpManager.ResultCallback<String>(MyEssayActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");

                            //super.onError(request, e);

                            // 这里处理非通用的逻辑

                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response); // 通用的逻辑在 super 方法里处理完了

                            // 这里处理非通用的逻辑
                            Log.d(TAG, response);
                            Type ob = new TypeToken<BaseResponse<GMyRecomList>>() {
                            }.getType();
                            mRecomRe = gson.fromJson(response, ob);
                            GMyRecomList gMyRecomList = (GMyRecomList) mRecomRe.getData();
                            mRecomEssays = gMyRecomList.getList();
                            Log.d(TAG, mRecomEssays.toString());
                            for (Recommend recommend : mRecomEssays) {
                                if (!recommend.isChildItem(mRecomAdapter.getmDataSet())) {
                                    mRecomAdapter.addItemToHead(recommend);
                                }
                            }
                            swrfMyessay.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMyessay.post(new Runnable() {
                @Override
                public void run() {
                    swrfMyessay.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMyessay.setOnRefreshListener(listener);


            //上拉加载
            recvMyEssay.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvMyEssay.getLayoutManager().getChildCount()>0) {
                        View lastChildView = recvMyEssay.getLayoutManager().getChildAt(recvMyEssay.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvMyEssay.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvMyEssay.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String myRecom="";
                            if (token!=null&&userId==-1){                               //已登录看自己
                                myRecom= apiImpl.getMyReCommendIn(10, CURRENT_PAGE+1, token);
                            }else if(token!=null){                                      //已登录看别人
                                myRecom=apiImpl.getOtherRecommendIn(10,CURRENT_PAGE+1,userId);
                            }
                            Log.d(TAG, myRecom);
                            mOkHttpManager.getStringAsyn(myRecom, new OkHttpManager.ResultCallback<String>(MyEssayActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type ob = new TypeToken<BaseResponse<GMyRecomList>>() {
                                    }.getType();
                                    mRecomRe = gson.fromJson(response, ob);
                                    GMyRecomList gMyRecomList = (GMyRecomList) mRecomRe.getData();
                                    mRecomEssays = gMyRecomList.getList();
                                    Log.d(TAG, mRecomEssays.toString());
                                    for (Recommend recommend : mRecomEssays) {
                                        if (!recommend.isChildItem(mRecomAdapter.getmDataSet())) {
                                            mRecomAdapter.addItem(recommend);
                                        }
                                    }
                                    swrfMyessay.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE++;
                        }
                    }
                }
            });

        } else {
            tvRemendMyessay.setVisibility(View.VISIBLE);
            swrfMyessay.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.top_one_article, R.id.top_one_topic, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_one_article:
                if(recvMyEssay.getVisibility()==View.GONE){
                    recvMyEssay.setVisibility(View.VISIBLE);
                }
                tvNothing.setVisibility(View.GONE);
                tvMyRecom.setTextColor(getResources().getColor(R.color.tv_secondtop));
                tvMyOrigin.setTextColor(getResources().getColor(R.color.tv_secondtop2));
                loadMyRecom();
                break;
            case R.id.top_one_topic:
                tvMyRecom.setTextColor(getResources().getColor(R.color.tv_secondtop2));
                tvMyOrigin.setTextColor(getResources().getColor(R.color.tv_secondtop));
                recvMyEssay.setVisibility(View.GONE);
                tvNothing.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyEssayActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyEssayActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyEssayActivity.this,LoginActivity.class);
            startActivity(in);
        }
    }
}
