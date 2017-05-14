package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.xiaoxiang.ioutside.mine.adapter.ObserAdapter;
import com.xiaoxiang.ioutside.mine.model.Observer;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GObserverList;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class MyObserverActivity extends AppCompatActivity
        implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    @Bind(R.id.tv_remend_myobser)
    TextView tvRemendMyobser;
    @Bind(R.id.recv_myobser)
    RecyclerView recvMyobser;
    @Bind(R.id.swrf_myobser)
    SwipeRefreshLayout swrfMyobser;
    private String TAG = getClass().getSimpleName();
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;

    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse mObserversRe;
    private String token;
    private int userId;
    private ObserAdapter obserAdapter;
    private ArrayList<Observer> obserList;
    private int CURRENT_PAGE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_observer);
        ButterKnife.bind(this);
        token = getIntent().getStringExtra("token");
        userId=getIntent().getIntExtra("userId",-1);
        Log.d(TAG, "token" + token);
        tvTitleSetting.setText("我的关注");
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        recvMyobser.setLayoutManager(new LinearLayoutManager(this));
        obserList=new ArrayList<>();
        obserAdapter=new ObserAdapter(obserList);
        loadObserver();
    }

    public void loadObserver() {
        if (NetworkUtil.isNetworkConnected(this)) {
            //绑定adapter的一些处理
            obserAdapter.setOnItemClickListener(new ObserAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemPhotoClick(View view, int position) {
                    Observer observer= obserAdapter.getMylist().get(position);
                    int userId=observer.getId();
                    Intent intent1=new Intent(MyObserverActivity.this,OtherPersonActivity.class);
                    intent1.putExtra("token",token);
                    intent1.putExtra("userID",userId);
                    startActivity(intent1);
                }

                @Override
                public void onItemObserClick(View view, int position) {
                    Observer observer1=obserAdapter.getMylist().get(position);
                    if(token==null){
                        Intent in=new Intent(MyObserverActivity.this,LoginActivity.class);
                        startActivity(in);
                    }else{
                        boolean observed=observer1.isObserved();
                        if(!observed){
                            observer1.setObserved(true);
                            RelativeLayout obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe_obsers);
                            TextView observe=(TextView)view.findViewById(R.id.tv_addobserve_obsers);
                            observe.setText("取消关注");
                            observe.setTextSize(8.0f);
                            obserLay.setBackgroundResource(R.drawable.observer_exit);
                            String obin=apiImpl.getAddObserIn(observer1.getId(),token);
                            Log.d(TAG,obin);
                            mOkHttpManager.getStringAsyn(obin, new OkHttpManager.ResultCallback<BaseResponse>(MyObserverActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG,"error");
                                }
                                @Override
                                public void onResponse(BaseResponse response) {
                                    super.onResponse(response);
                                    if (response.isSuccess()){
                                        Toast.makeText(MyObserverActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MyObserverActivity.this,"关注失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            observer1.setObserved(false);
                            RelativeLayout obserLay=(RelativeLayout)view.findViewById(R.id.lay_observe_obsers);
                            TextView observe=(TextView)view.findViewById(R.id.tv_addobserve_obsers);
                            observe.setText("关注");
                            observe.setTextSize(12.0f);
                            obserLay.setBackgroundResource(R.drawable.observer);
                            String canob=apiImpl.getCancelObserIn(observer1.getId(),token);
                            Log.d(TAG,canob);
                            mOkHttpManager.getStringAsyn(canob, new OkHttpManager.ResultCallback<BaseResponse>(MyObserverActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG,"error");
                                }

                                @Override
                                public void onResponse(BaseResponse response) {
                                    super.onResponse(response);
                                    if(response.isSuccess()){
                                        Toast.makeText(MyObserverActivity.this,"取消关注成功！",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MyObserverActivity.this,"取消关注失败！",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });

            recvMyobser.setAdapter(obserAdapter);

            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    String myObserverIn="";
                    if(token==null&&userId!=-1){                                //未登录看别人
                        myObserverIn = apiImpl.getOtherObserIn(10, 1, userId);
                    }
                    if (token!=null&&userId==-1){                               //已登录看自己
                        myObserverIn = apiImpl.getObserverIn(10, 1, token);
                    }else if(token!=null&&userId!=-1){                          //已登录看别人
                        myObserverIn=apiImpl.getOtherObserIn(10,1,userId,token);
                    }
                    Log.d(TAG, myObserverIn);
                    mOkHttpManager.getStringAsyn(myObserverIn, new OkHttpManager.ResultCallback<String>(MyObserverActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(MyObserverActivity.this, "网络有点问题哦！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            Type objectType = new TypeToken<BaseResponse<GObserverList>>() {
                            }.getType();
                            mObserversRe = gson.fromJson(response, objectType);
                            Log.d(TAG, "数据转型成功");
                            GObserverList observers = (GObserverList) mObserversRe.getData();
                            final ArrayList<Observer> mObserverList = observers.getList();
                            Log.d(TAG, mObserverList.toString());
                            for (Observer observer : mObserverList) {
                                if (!observer.isChildItem(obserAdapter.getMylist())) {
                                    obserAdapter.addItemToHead(observer);
                                }
                            }
                            swrfMyobser.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMyobser.post(new Runnable() {
                @Override
                public void run() {
                    swrfMyobser.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMyobser.setOnRefreshListener(listener);


            //上拉加载
            recvMyobser.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvMyobser.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recvMyobser.getLayoutManager().getChildAt(recvMyobser.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvMyobser.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvMyobser.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String moreIn="";
                            if(token==null&&userId!=-1){                                //未登录看别人
                                moreIn = apiImpl.getOtherObserIn(10, CURRENT_PAGE+1, userId);
                            }
                            if (token!=null&&userId==-1){                               //已登录看自己
                                moreIn = apiImpl.getObserverIn(10, CURRENT_PAGE+1, token);
                            }else if(token!=null&&userId!=-1){                          //已登录看别人
                                moreIn=apiImpl.getOtherObserIn(10,CURRENT_PAGE+1,userId,token);
                            }
                            Log.d(TAG, moreIn);
                            mOkHttpManager.getStringAsyn(moreIn, new OkHttpManager.ResultCallback<String>(MyObserverActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Toast.makeText(MyObserverActivity.this, "网络有点问题哦！", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "error");
                                }


                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type objectType = new TypeToken<BaseResponse<GObserverList>>() {
                                    }.getType();
                                    mObserversRe = gson.fromJson(response, objectType);
                                    Log.d(TAG, "数据转型成功");
                                    GObserverList observers = (GObserverList) mObserversRe.getData();
                                    final ArrayList<Observer> mObserverList = observers.getList();
                                    Log.d(TAG, mObserverList.toString());
                                    for (Observer observer : mObserverList) {
                                        if (!observer.isChildItem(obserAdapter.getMylist())) {
                                            obserAdapter.addItem(observer);
                                        }
                                    }
                                    swrfMyobser.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE++;
                        }
                    }
                }
            });

        } else {
            tvRemendMyobser.setVisibility(View.VISIBLE);
            swrfMyobser.setVisibility(View.GONE);
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
            Intent in=new Intent(MyObserverActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyObserverActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyObserverActivity.this,LoginActivity.class);
            startActivity(in);
        }
    }
}
