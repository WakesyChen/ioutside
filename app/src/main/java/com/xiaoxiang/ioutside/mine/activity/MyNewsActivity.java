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
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.activity.RecommendActivity;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.mine.adapter.DynaNewsAdapter;
import com.xiaoxiang.ioutside.mine.adapter.EssayNewsAdapter;
import com.xiaoxiang.ioutside.mine.model.MyDynamicNews;
import com.xiaoxiang.ioutside.mine.model.MyEssayNews;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyDynaNewsList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyEssayNewsList;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class MyNewsActivity extends Activity implements View.OnClickListener,
        Constants,OkHttpManager.ResultCallback.CommonErrorListener  {

    @Bind(R.id.swrf_mynews)
    SwipeRefreshLayout swrfMynews;
    @Bind(R.id.tv_remend_mynews)
    TextView tvRemendMynews;
    private String TAG = getClass().getSimpleName();


    @Bind(R.id.top_one_article)
    TextView topEssayNews;
    @Bind(R.id.top_one_topic)
    TextView topDynamicNews;
    @Bind(R.id.recv_newslist)
    RecyclerView recvNewslist;
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;


    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse mEssayNewsRe;
    private BaseResponse mDynamicNewsRe;

    private EssayNewsAdapter mEssayAdapter;
    private ArrayList<MyEssayNews> essayNewList;
    private ArrayList<MyDynamicNews> dynaNewList;
    private DynaNewsAdapter mDynaAdapter;
    //测试用token
    private String token;

    private int CURRENT_PAGE = 1;
    private int CURRENT_PAGE2 = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news_old);
        ButterKnife.bind(this);
        tvTitleSetting.setText("我的消息");
        topEssayNews.setText("文章");
        topDynamicNews.setText("动态");
        token = getIntent().getStringExtra("token");
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        recvNewslist.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "运行到这里了");
        essayNewList=new ArrayList<>();
        dynaNewList=new ArrayList<>();
        mEssayAdapter=new EssayNewsAdapter(essayNewList);
        mDynaAdapter=new DynaNewsAdapter(dynaNewList);

        loadEssayNews();
    }

    public void loadEssayNews() {
        if (NetworkUtil.isNetworkConnected(this)) {
            mEssayAdapter.setOnItemClickListener(new EssayNewsAdapter.ItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemPhotoClick(View view, int position) {
                    MyEssayNews news=mEssayAdapter.getMylist().get(position);
                    int otherID=news.getUserId();
                    Intent intent=new Intent(MyNewsActivity.this, OtherPersonActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("userID",otherID);
                    startActivity(intent);
                }

                @Override
                public void onItemEssayClick(View view, int position) {
                    MyEssayNews news1=mEssayAdapter.getMylist().get(position);
                    int essayId=news1.getId();
                    Intent in=new Intent(MyNewsActivity.this,ArticleDetailActivity.class);
                    in.putExtra("id",essayId+"");
                    in.putExtra("token",token);
                    startActivity(in);
                }
            });

            recvNewslist.setAdapter(mEssayAdapter);
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG, "开始刷新");
                    String essaylistIn = apiImpl.getMyEssayNewsIn(1, 2, token);
                    Log.d(TAG, essaylistIn);
                    mOkHttpManager.getStringAsyn(essaylistIn, new OkHttpManager.ResultCallback<String>(MyNewsActivity.this) {

                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            Type objectType = new TypeToken<BaseResponse<GMyEssayNewsList>>() {
                            }.getType();
                            mEssayNewsRe = gson.fromJson(response, objectType);
                            Log.d(TAG, "数据转型成功");
                            GMyEssayNewsList gMyEssayNewsList = (GMyEssayNewsList) mEssayNewsRe.getData();
                            final ArrayList<MyEssayNews> mEssayNewsList = gMyEssayNewsList.getList();
                            Log.d(TAG, mEssayNewsList.toString());
                            for (MyEssayNews news : mEssayNewsList) {
                                if (!news.isChildItem(mEssayAdapter.getMylist())) {
                                    mEssayAdapter.addItemToHead(news);
                                }
                            }
                            swrfMynews.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMynews.post(new Runnable() {
                @Override
                public void run() {
                    swrfMynews.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMynews.setOnRefreshListener(listener);

            //上拉加载
            recvNewslist.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvNewslist.getLayoutManager().getChildCount()> 0) {
                        View lastChildView = recvNewslist.getLayoutManager().getChildAt(recvNewslist.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvNewslist.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvNewslist.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String myessaynewsIn = apiImpl.getMyEssayNewsIn(CURRENT_PAGE + 1,10, token);
                            Log.d(TAG, "mycollessIn" + myessaynewsIn);
                            mOkHttpManager.getStringAsyn(myessaynewsIn, new OkHttpManager.ResultCallback<String>(MyNewsActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }


                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type objectType = new TypeToken<BaseResponse<GMyEssayNewsList>>() {
                                    }.getType();
                                    mEssayNewsRe = gson.fromJson(response, objectType);
                                    Log.d(TAG, "数据转型成功");
                                    GMyEssayNewsList gMyEssayNewsList = (GMyEssayNewsList) mEssayNewsRe.getData();
                                    final ArrayList<MyEssayNews> mEssayNewsList = gMyEssayNewsList.getList();
                                    Log.d(TAG, mEssayNewsList.toString());

                                    for (MyEssayNews news : mEssayNewsList) {
                                        if (!news.isChildItem(mEssayAdapter.getMylist())) {
                                            mEssayAdapter.addItem(news);
                                        }
                                    }
                                    swrfMynews.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE++;
                        }
                    }
                }
            });

        } else {
            tvRemendMynews.setVisibility(View.VISIBLE);
            swrfMynews.setVisibility(View.GONE);
        }

    }

    public void loadDynamicNews() {

        if (NetworkUtil.isNetworkConnected(this)) {
            //绑定adapter的一些处理
            mDynaAdapter.setOnItemClickListener(new DynaNewsAdapter.ItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemPhotoClick(View view, int position) {
                    MyDynamicNews news=mDynaAdapter.getMylist().get(position);
                    int otherID=news.getUserId();
                    Intent intent=new Intent(MyNewsActivity.this, OtherPersonActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("userID",otherID);
                    startActivity(intent);
                }

                @Override
                public void onItemEssayClick(View view, int position) {
                    MyEssayNews news1=mEssayAdapter.getMylist().get(position);
                    int essayId=news1.getId();
                    Intent in=new Intent(MyNewsActivity.this,RecommendActivity.class);
                    in.putExtra("id",essayId+"");
                    in.putExtra("token",token);
                    startActivity(in);
                }
            });


            recvNewslist.setAdapter(mDynaAdapter);
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG, "开始刷新");
                    String dynamiclistIn = apiImpl.getMyDynamicNewsIn(1, 10, token);
                    Log.d(TAG, dynamiclistIn);
                    mOkHttpManager.getStringAsyn(dynamiclistIn, new OkHttpManager.ResultCallback<String>(MyNewsActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.d(TAG, response);
                            Type objectType = new TypeToken<BaseResponse<GMyDynaNewsList>>() {
                            }.getType();
                            mDynamicNewsRe = gson.fromJson(response, objectType);
                            Log.d(TAG, "数据转型成功");
                            GMyDynaNewsList gMyDynaNewsList = (GMyDynaNewsList) mDynamicNewsRe.getData();
                            final ArrayList<MyDynamicNews> mDynamicList = gMyDynaNewsList.getList();
                            Log.d(TAG, mDynamicList.toString());

                            for (MyDynamicNews dynamicNews:mDynamicList) {
                                if (dynamicNews.isChildItem(mDynaAdapter.getMylist())) {
                                    mDynaAdapter.addItemToHead(dynamicNews);
                                }
                            }
                            swrfMynews.setRefreshing(false);
                        }
                    });
                }
            };
            //实现自动加载
            swrfMynews.post(new Runnable() {
                @Override
                public void run() {
                    swrfMynews.setRefreshing(true);
                }
            });
            listener.onRefresh();
            swrfMynews.setOnRefreshListener(listener);


            //上拉加载
            recvNewslist.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recvNewslist.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recvNewslist.getLayoutManager().getChildAt(recvNewslist.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recvNewslist.getLayoutManager().getPosition(lastChildView);
                        //进行判断
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && lastPosition == recvNewslist.getLayoutManager().getItemCount() - 1) {
                            //这里请求新的数据
                            String mydynewsIn = apiImpl.getMyDynamicNewsIn(CURRENT_PAGE2 + 1,10, token);
                            Log.d(TAG, "page数为" + CURRENT_PAGE2 );
                            Log.d(TAG, "mycollessIn" + mydynewsIn);
                            mOkHttpManager.getStringAsyn(mydynewsIn, new OkHttpManager.ResultCallback<String>(MyNewsActivity.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }


                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Log.d(TAG, response);
                                    Type objectType = new TypeToken<BaseResponse<GMyDynaNewsList>>() {
                                    }.getType();
                                    mDynamicNewsRe = gson.fromJson(response, objectType);
                                    Log.d(TAG, "数据转型成功");
                                    GMyDynaNewsList gMyDynaNewsList = (GMyDynaNewsList) mDynamicNewsRe.getData();
                                    final ArrayList<MyDynamicNews> mDynamicList = gMyDynaNewsList.getList();
                                    Log.d(TAG, mDynamicList.toString());

                                    for (MyDynamicNews dynamicNews:mDynamicList) {
                                        if (!dynamicNews.isChildItem(mDynaAdapter.getMylist())) {
                                            mDynaAdapter.addItem(dynamicNews);
                                        }
                                    }
                                    swrfMynews.setRefreshing(false);
                                }
                            });
                            CURRENT_PAGE2++;
                        }
                    }
                }
            });

        } else {
            tvRemendMynews.setVisibility(View.VISIBLE);
            swrfMynews.setVisibility(View.GONE);
        }

    }


    @OnClick({R.id.iv_back, R.id.top_one_article, R.id.top_one_topic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.top_one_article:
                topEssayNews.setTextColor(getResources().getColor(R.color.tv_secondtop));
                topDynamicNews.setTextColor(getResources().getColor(R.color.tv_secondtop2));
                loadEssayNews();
                break;
            case R.id.top_one_topic:
                topDynamicNews.setTextColor(getResources().getColor(R.color.tv_secondtop));
                topEssayNews.setTextColor(getResources().getColor(R.color.tv_secondtop2));
                loadDynamicNews();
                break;
        }
    }

    @Override
    public void onCommonError(int errorCode) {
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyNewsActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(MyNewsActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MyNewsActivity.this,LoginActivity.class);
            startActivity(in);
        }
    }

}
