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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.mine.adapter.FansAdapter;
import com.xiaoxiang.ioutside.mine.model.Fan;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GFansList;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class MyFansActivity extends Activity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private String TAG = getClass().getSimpleName();

    @Bind(R.id.tv_remend_myfans)
    TextView tvRemendMyfans;
    @Bind(R.id.recv_myfans)
    RecyclerView recvMyfans;
    @Bind(R.id.swrf_myfans)
    SwipeRefreshLayout swrfMyfans;
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;


    private ApiInterImpl api;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse mFansRe;
    private String token;
    private int userId;
    private FansAdapter fansAdapter;
    private ArrayList<Fan> fanList;

    private int CURRENT_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        token = getIntent().getStringExtra("token");
        userId=getIntent().getIntExtra("userId",-1);
        Log.d(TAG, "token" + token);
        tvTitleSetting.setText("我的粉丝");
        gson = new Gson();
        api = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        recvMyfans.setLayoutManager(new LinearLayoutManager(this));
        fanList=new ArrayList<>();
        fansAdapter=new FansAdapter(fanList);
        loadFansList();
    }

    public void loadFansList() {
        if (NetworkUtil.isNetworkConnected(this)) {
            //绑定adapter的一些处理
            fansAdapter.setOnItemClickListener(new FansAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemPhotoClick(View view, int position) {
                    Fan fan=fansAdapter.getMylist().get(position);
                    int otherID=fan.getId();
                    Intent intent=new Intent(MyFansActivity.this, OtherPersonActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("userID",otherID);
                    startActivity(intent);
                }

                @Override
                public void onItemObserClick(View view, int position) {
                    Log.d(TAG,position+"");
                    Fan fans=fansAdapter.getMylist().get(position);
                    if(token==null){
                        Intent in=new Intent(MyFansActivity.this,LoginActivity.class);
                        startActivity(in);
                    }else {
                        boolean observed = fans.isObserved();
                        if (!observed) {
                            fans.setObserved(true);
                            RelativeLayout obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe);
                            TextView observe=(TextView)view.findViewById(R.id.tv_addobserve_fans);
                            observe.setText("取消关注");
                            observe.setTextSize(8.0f);
                            obserLay.setBackgroundResource(R.drawable.fans_exit);
                            String obin = api.getAddObserIn(fans.getId(), token);
                            Log.d(TAG,obin);
                            mOkHttpManager.getStringAsyn(obin, new OkHttpManager.ResultCallback<BaseResponse>(MyFansActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }

                                @Override
                                public void onResponse(BaseResponse response) {
                                    super.onResponse(response);
                                    if (response.isSuccess()) {
                                        Toast.makeText(MyFansActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MyFansActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            fans.setObserved(false);
                            RelativeLayout obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe);
                            TextView observe=(TextView)view.findViewById(R.id.tv_addobserve_fans);
                            observe.setText("关注");
                            observe.setTextSize(12.0f);
                            obserLay.setBackgroundResource(R.drawable.fans_add);
                            String canob = api.getCancelObserIn(fans.getId(), token);
                            Log.d(TAG,canob);
                            mOkHttpManager.getStringAsyn(canob, new OkHttpManager.ResultCallback<BaseResponse>(MyFansActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }

                                @Override
                                public void onResponse(BaseResponse response) {
                                    super.onResponse(response);
                                    if (response.isSuccess()) {
                                        Toast.makeText(MyFansActivity.this, "取消关注成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MyFansActivity.this, "取消关注失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });


            recvMyfans.setAdapter(fansAdapter);
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    String myfansIn="";
                    if(token==null&&userId!=-1){                                //未登录看别人
                        myfansIn = api.getOtherFansIn(10, 1, userId);
                    }
                    if (token!=null&&userId==-1){                               //已登录看自己
                        myfansIn = api.getFansInterIn(10, 1, token);
                    }else if(token!=null&&userId!=-1){                          //已登录看别人
                        myfansIn=api.getOtherFansIn(10,1,userId,token);
                    }
                    Log.d(TAG, myfansIn);
                    mOkHttpManager.getStringAsyn(myfansIn, new OkHttpManager.ResultCallback<String>(MyFansActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(MyFansActivity.this, "网络有点问题哦！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "error");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            Type objectType = new TypeToken<BaseResponse<GFansList>>() {
                            }.getType();
                            mFansRe = gson.fromJson(response, objectType);
                            Log.d(TAG, "数据转型成功");
                            GFansList fans = (GFansList) mFansRe.getData();
                            final ArrayList<Fan> mFanList = fans.getList();
                            Log.d(TAG, mFanList.toString());
                            for (Fan fan : mFanList) {
                                if (!fan.isChildItem(fansAdapter.getMylist())) {
                                    fansAdapter.addItemToHead(fan);
                                }
                            }
                            swrfMyfans.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMyfans.post(new Runnable() {
                @Override
                public void run() {
                    swrfMyfans.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMyfans.setOnRefreshListener(listener);


            //上拉加载
            recvMyfans.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvMyfans.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recvMyfans.getLayoutManager().getChildAt(recvMyfans.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvMyfans.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvMyfans.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String moreIn="";
                            if(token==null&&userId!=-1){                                //未登录看别人
                                moreIn = api.getOtherFansIn(10, CURRENT_PAGE+1, userId);
                            }
                            if (token!=null&&userId==-1){                               //已登录看自己
                                moreIn = api.getFansInterIn(10, CURRENT_PAGE+1, token);
                            }else if(token!=null&&userId!=-1){                          //已登录看别人
                                moreIn=api.getOtherFansIn(10,CURRENT_PAGE+1,userId,token);
                            }
                            Log.d(TAG, moreIn);
                            mOkHttpManager.getStringAsyn(moreIn, new OkHttpManager.ResultCallback<String>(MyFansActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Toast.makeText(MyFansActivity.this, "网络有点问题哦！", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "error");
                                }


                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type objectType = new TypeToken<BaseResponse<GFansList>>() {
                                    }.getType();
                                    mFansRe = gson.fromJson(response, objectType);
                                    Log.d(TAG, "数据转型成功");
                                    GFansList fans = (GFansList) mFansRe.getData();
                                    final ArrayList<Fan> mFanList = fans.getList();
                                    Log.d(TAG, mFanList.toString());
                                    for (Fan fan : mFanList) {
                                        if (!fan.isChildItem(fansAdapter.getMylist())) {
                                            fansAdapter.addItem(fan);
                                        }
                                    }
                                    swrfMyfans.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE++;
                        }
                    }
                }
            });

        } else {
            tvRemendMyfans.setVisibility(View.VISIBLE);
            swrfMyfans.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onCommonError(int errorCode) {
        if (errorCode == USER_OUTLINE) {
            Log.d(TAG,"被踢掉的errorcode"+errorCode);
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyFansActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyFansActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyFansActivity.this,LoginActivity.class);
            startActivity(in);
        }
    }
}