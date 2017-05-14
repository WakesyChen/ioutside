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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.activity.RecommendActivity;
import com.xiaoxiang.ioutside.mine.model.Dynamic;
import com.xiaoxiang.ioutside.mine.widget.Adapter.BaseRecvAdapter;
import com.xiaoxiang.ioutside.mine.widget.Adapter.RecyclerViewHolder;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GDynamicList;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class MyDynamicActivity extends Activity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private String TAG = getClass().getSimpleName();

    @Bind(R.id.tv_remend_mydyna)
    TextView tvRemendMydyna;
    @Bind(R.id.recv_mydyna)
    RecyclerView recvMydyna;
    @Bind(R.id.swrf_mydyna)
    SwipeRefreshLayout swrfMydyna;
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;

    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse mDynamicRe;
    private BaseRecvAdapter mDynaAdapter;
    //测试用token
    private String token;
    private int userId;

    private int CURRENT_PAGE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        ButterKnife.bind(this);
        token = getIntent().getStringExtra("token");
        userId=getIntent().getIntExtra("userId",-1);
        Log.d(TAG, "token" + token);
        tvTitleSetting.setText("动态");
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        recvMydyna.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "运行到这里了");
        loadDynamic();
    }

    public void loadDynamic() {
        if (NetworkUtil.isNetworkConnected(this)) {
            //绑定adapter的一些处理
            //为recyclerview绑定数据
            mDynaAdapter = new BaseRecvAdapter<Dynamic>(R.layout.my_dynamic_item) {
                @Override
                public void onBindData(RecyclerViewHolder viewHolder, int position, Dynamic item) {
                    viewHolder.setText(R.id.my_dynamic_intro, item.getThoughts());
                    viewHolder.setText(R.id.my_dynamic_time, item.getPublishTime());
                    viewHolder.setText(R.id.my_dynamic_title, item.getTitle());
                    viewHolder.setDrawableLoader(R.id.my_dynamic_src, item.getPhotoList().get(0), ImageLoader.getInstance());
                }
            };
            mDynaAdapter.setOnItemClickListener(new BaseRecvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    switch (v.getId()) {
                        case R.id.lay_mydyna_item:
                            Dynamic dynamic=(Dynamic) mDynaAdapter.getmDataSet().get(position);
                            int id=dynamic.getId();
                            Intent intent=new Intent(MyDynamicActivity.this, RecommendActivity.class);
                            intent.putExtra("id",id+"");
                            startActivity(intent);
                            break;
                    }
                }
            });
            recvMydyna.setAdapter(mDynaAdapter);

            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                String mydynaIn="";
                @Override
                public void onRefresh() {
                    if (token!=null&&userId==-1){                               //已登录看自己
                         mydynaIn = apiImpl.getMyDynamicIn(10, 1, token);
                    }else if(token!=null){                                      //已登录看别人
                         mydynaIn = apiImpl.getOtherDynamicIn(10, 1, userId);
                    }
                    Log.d(TAG, mydynaIn);
                    mOkHttpManager.getStringAsyn(mydynaIn, new OkHttpManager.ResultCallback<String>(MyDynamicActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            Type objectType = new TypeToken<BaseResponse<GDynamicList>>() {
                            }.getType();
                            mDynamicRe = gson.fromJson(response, objectType);
                            Log.d(TAG, "数据转型成功");
                            GDynamicList gDynamicList = (GDynamicList) mDynamicRe.getData();
                            final ArrayList<Dynamic> mDynamics = gDynamicList.getList();
                            Log.d(TAG, mDynamics.toString());
                            for (Dynamic dynamic : mDynamics) {
                                if (!dynamic.isChildItem(mDynaAdapter.getmDataSet())) {
                                    mDynaAdapter.addItemToHead(dynamic);
                                }
                            }
                            swrfMydyna.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMydyna.post(new Runnable() {
                @Override
                public void run() {
                    swrfMydyna.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMydyna.setOnRefreshListener(listener);


            //上拉加载
            recvMydyna.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvMydyna.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recvMydyna.getLayoutManager().getChildAt(recvMydyna.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvMydyna.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvMydyna.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String mydynaIn = apiImpl.getMyDynamicIn(10, CURRENT_PAGE+1, token);
                            Log.d(TAG, mydynaIn);
                            mOkHttpManager.getStringAsyn(mydynaIn, new OkHttpManager.ResultCallback<String>(MyDynamicActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }


                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type objectType = new TypeToken<BaseResponse<GDynamicList>>() {
                                    }.getType();
                                    mDynamicRe = gson.fromJson(response, objectType);
                                    Log.d(TAG, "数据转型成功");
                                    GDynamicList gDynamicList = (GDynamicList) mDynamicRe.getData();
                                    final ArrayList<Dynamic> mDynamics = gDynamicList.getList();
                                    Log.d(TAG, mDynamics.toString());
                                    for (Dynamic dynamic : mDynamics) {
                                        if (!dynamic.isChildItem(mDynaAdapter.getmDataSet())) {
                                            mDynaAdapter.addItem(dynamic);
                                        }
                                    }
                                    swrfMydyna.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE++;
                        }
                    }
                }
            });

        } else {
            tvRemendMydyna.setVisibility(View.VISIBLE);
            swrfMydyna.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onCommonError(int errorCode) {
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyDynamicActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyDynamicActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyDynamicActivity.this,LoginActivity.class);
            startActivity(in);
        }

    }
}
