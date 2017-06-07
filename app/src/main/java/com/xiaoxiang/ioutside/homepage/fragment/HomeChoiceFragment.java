package com.xiaoxiang.ioutside.homepage.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.activity.ArticleCommentActivity;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.homepage.adapter.HomeChoiceAdapter;
import com.xiaoxiang.ioutside.homepage.model.BannerInfo;
import com.xiaoxiang.ioutside.homepage.model.Essay;
import com.xiaoxiang.ioutside.homepage.model.GBannerInfoList;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GEssay;
import com.xiaoxiang.ioutside.util.ACache;
import com.xiaoxiang.ioutside.util.NetworkUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * Created by 10648 on 2016/4/17 0017.
 */
public class HomeChoiceFragment extends Fragment implements Constants, OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG = "HomeChoiceFragment";
    private static final int COMMENT_REQUEST = 1;
    private static final int DETAIL_REQUEST = 2;
    private View view;
    private SwipeRefreshLayout swipeRefresh_choice;
    private RecyclerView recyclerView_choice;
    private HomeChoiceAdapter mAdapter;
    private List<Integer> ids = new ArrayList<>();
    private int pageSize = 5;
    private int pageNo = 1;
    private String token;
    private String fileToken;
    private boolean add = false;
    private boolean stopRefresh=false;//停止刷新表示
    private boolean stopRefresh1=false;//停止刷新表示
    private ACache mCache;
    //设置备用的banner图片
//    private int imgs[] = {R.drawable.banner_img_1, R.drawable.banner_img_2, R.drawable.banner_img_3, R.drawable.banner_img_4};//轮播图片

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.article_choice_fragment, container, false);
        token = getActivity().getIntent().getStringExtra("token");//从activity里面获取
        CachedInfo mCachedInfo = MyApplication.getInstance().getCachedInfo();
        if (mCachedInfo != null) {
            fileToken = mCachedInfo.getToken();//得到文件中存取的token
            Log.d(TAG, "fileToken=" + fileToken);
        }
        mCache = ACache.get(getContext());

        return view;
    }
    //初始化banner数据源
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh_choice = (SwipeRefreshLayout) view.findViewById(R.id.choice_swipeRefresh);
        recyclerView_choice = (RecyclerView) view.findViewById(R.id.recycler_choice);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_choice.setLayoutManager(mLayoutManager);
        recyclerView_choice.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView_choice.setHasFixedSize(true);
        mAdapter = new HomeChoiceAdapter(getActivity(), token);//把数据传过去
        recyclerView_choice.setAdapter(mAdapter);
        if (token != null) {//MainActivity的token不为空
            initData();
        } else if (token == null && fileToken == null) {
            initData();//同时都为空

        } else {
            token = fileToken;//文件读取的token给当前的token
            initData();
        }
        initEvent();
    }

    private void initData() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    stopRefresh=false;//停止刷新标识初始化
                    stopRefresh1=false;//停止刷新
//                    banner数据
                    //            获取到轮播图数据
                    ApiInterImpl api = new ApiInterImpl();
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
                    okHttpManager.getStringAsyn(api.getSalonBannerInfo(), new OkHttpManager.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            super.onError(request, e);
                            ToastUtils.show("网络出现错误");
                        }
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            mCache.put("banner", response);
                            Type objectType = new TypeToken<BaseResponse<GBannerInfoList>>() {
                            }.getType();
                            Gson gson = new Gson();
                            BaseResponse bannerResponse = gson.fromJson(response, objectType);
                            GBannerInfoList gBannerInfoList = (GBannerInfoList) bannerResponse.getData();
                            List<BannerInfo> bannerInfoList = gBannerInfoList.getList();//得到banner中的list数据
                            List<String> bannerPhotos = new ArrayList<>();//网络获取banner
                            List<String> titles = new ArrayList<>();
                            List<String> bannerUrls = new ArrayList<>();
                            if (bannerInfoList != null && bannerInfoList.size() > 0) {
                                for (BannerInfo bannerInfo : bannerInfoList) {
                                    bannerPhotos.add(bannerInfo.getPhoto());
                                    titles.add(bannerInfo.getTitle());
                                    bannerUrls.add(bannerInfo.getHref());
                                }
                                mAdapter.setBannerData(bannerPhotos, titles, bannerUrls);

                            }
                            stopRefresh=true;
                            if (stopRefresh&&stopRefresh1) {
                                swipeRefresh_choice.setRefreshing(false);
                            }
                        }
                    });
//                  item数据
                    Log.i(TAG, "下拉刷新");
                    int pageNo = 1;
                    ApiInterImpl api1 = new ApiInterImpl();
                    OkHttpManager mOkHttpManager1 = OkHttpManager.getInstance();
                    mOkHttpManager1.getStringAsyn(api1.getWellChosenList(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(HomeChoiceFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "onError");
                            Toast.makeText(getActivity(), "网络有点问题!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, "精选的数据得到了刷新");
                            mCache.put("choice", response, 1000 * 7200);
                            Type objectType = new TypeToken<BaseResponse<GEssay>>() {
                            }.getType();
                            Gson gson = new Gson();
                            BaseResponse ChoiceBaseResponse = gson.fromJson(response, objectType);//最外层的类
                            GEssay mGEssay = (GEssay) ChoiceBaseResponse.getData();//返回的是data类对象
                            List<Essay> list = mGEssay.getList();//返回的是下一层的list
                            if (list != null && list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    Essay essay = list.get(i);
                                    int id = essay.getId();
                                    ids.add(i, id);

                                    if (mAdapter.getDataSet().size() >= pageSize) {
                                        for (int a = 0; a < pageSize; a++) {//前5个的id是否与第一个id相同
                                            if ((mAdapter.getDataSet().get(a).getId() == essay.getId())) {
                                                add = true;
                                            }
                                        }
                                    }
                                    if (add == false)
                                        mAdapter.addItemToHead(i, essay);//在此处给mAdapter添加数据源

                                    else
                                        add = false;

                                }
                            }
                            stopRefresh1=true;
                            if (stopRefresh&&stopRefresh1) {
                                swipeRefresh_choice.setRefreshing(false);
                            }
                        }
                    });
                }
            };
            //开始下拉刷新
            swipeRefresh_choice.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh_choice.setRefreshing(true);
                }
            });

            listener.onRefresh();
            swipeRefresh_choice.setOnRefreshListener(listener);
            //上拉加载
            recyclerView_choice.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView_choice.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recyclerView_choice.getLayoutManager().getChildAt(recyclerView_choice.getLayoutManager().getChildCount() - 1);//position要减去head再减1
                        int lastPosition = recyclerView_choice.getLayoutManager().getPosition(lastChildView);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_choice.getLayoutManager().getItemCount() - 1) {
                            pageNo++;
                            ApiInterImpl api = new ApiInterImpl();
                            OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                            mOkHttpManager.getStringAsyn(api.getWellChosenList(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(HomeChoiceFragment.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "onError");
                                    Toast.makeText(getContext(), "网络有点问题!", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Type objectType = new TypeToken<BaseResponse<GEssay>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    BaseResponse ChoiceBaseResponse = gson.fromJson(response, objectType);//最外层的类
                                    GEssay mGEssay = (GEssay) ChoiceBaseResponse.getData();//返回的是data类对象
                                    List<Essay> list = mGEssay.getList();//返回的是下一层的list
                                    if (list != null && list.size() != 0) {
                                        for (Essay essay : list) {
                                            int id = essay.getId();
                                            ids.add(id);
                                            if (!mAdapter.getDataSet().contains(essay)) {
                                                mAdapter.addItem(essay);
                                            }
                                        }
                                    }
                                    stopRefresh1=true;
                                    if (stopRefresh1&&stopRefresh) {
                                        swipeRefresh_choice.setRefreshing(false);
                                    }
                                }
                            });
                        }
                    }
                }
            });


        } else {
            String bannerRespone = mCache.getAsString("banner");
            if (!TextUtils.isEmpty(bannerRespone)) {
                Type objectType = new TypeToken<BaseResponse<GBannerInfoList>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse bannerResponse = gson.fromJson(bannerRespone, objectType);
                GBannerInfoList gBannerInfoList = (GBannerInfoList) bannerResponse.getData();
                List<BannerInfo> bannerInfoList = gBannerInfoList.getList();//得到banner中的list数据

                List<String> bannerPhotos = new ArrayList<>();//网络获取banner
                List<String> titles = new ArrayList<>();
                List<String> bannerUrls = new ArrayList<>();
                if (bannerInfoList != null && bannerInfoList.size() > 0) {
                    for (BannerInfo bannerInfo : bannerInfoList) {
                        bannerPhotos.add(bannerInfo.getPhoto());
                        titles.add(bannerInfo.getTitle());
                        bannerUrls.add(bannerInfo.getHref());
                    }
                    mAdapter.setBannerData(bannerPhotos, titles, bannerUrls);

                }


            }

            String response = mCache.getAsString("choice");
            if (!TextUtils.isEmpty(response) && response != null) {
                Type objectType = new TypeToken<BaseResponse<GEssay>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse ChoiceBaseResponse = gson.fromJson(response, objectType);//最外层的类
                GEssay mGEssay = (GEssay) ChoiceBaseResponse.getData();
                List<Essay> list = mGEssay.getList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Essay essay = list.get(i);
                        mAdapter.addItemToHead(i, essay);
                    }
                }
                swipeRefresh_choice.setRefreshing(false);
            }
        }
    }

    private void initEvent() {
        //接口回调
        mAdapter.setOnItemClickListener(new HomeChoiceAdapter.OnItemClickListener() {

            //跳转到评论页面
            @Override
            public void onCommentClick(View view, int position) {
                Essay essay = mAdapter.getDataSet().get(position - 1);//-1是因为增加了banner item
                int id = essay.getId();
                int commentCount = essay.getCommentCount();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ArticleCommentActivity.class);
                intent.putExtra("id", String.valueOf(id));//传入文章id和评论量
                intent.putExtra("commentCount", String.valueOf(commentCount));
                intent.putExtra("token", token);
                startActivityForResult(intent, COMMENT_REQUEST);
            }

            //跳转到文章详情,采用回调是要处理文章详情里面的点赞跟评论
            @Override
            public void onItemClick(View view, int position) {
                Essay essay = mAdapter.getDataSet().get(position - 1);//-1是因为增加了banner item
                int id = essay.getId();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ArticleDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));//将文章的id传入文章详情
                intent.putExtra("token", token);
                Log.i(TAG, "onItemClick: id" + id);
                startActivityForResult(intent, DETAIL_REQUEST);
            }

            //点赞的接口回调
            public void onZanClick(final View view, final int position) {
                final Essay essay = mAdapter.getDataSet().get(position - 1);
                boolean isLike = essay.isLiked();//关键是这里的tokenLike.get(position)没变
                int id = essay.getId();
                ApiInterImpl api = new ApiInterImpl();
                OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                if (token == null) {
                    noLogin();
                } else {
                    if (isLike == false) {
                        mOkHttpManager.getStringAsyn(api.likeArticle(id, token), new OkHttpManager.ResultCallback<String>(HomeChoiceFragment.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.i(TAG, "onError");
                                Toast.makeText(getActivity(), "网络有点问题", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.i(TAG, "onResponse:点赞：" + position);
                                essay.setLiked(true);
                                int likeCount = essay.getLikedCount();
                                likeCount++;
                                essay.setLikedCount(likeCount);
                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    } else {
                        //取消赞
                        mOkHttpManager.getStringAsyn(api.cancelLikeArticle(id, token), new OkHttpManager.ResultCallback<String>(HomeChoiceFragment.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.i(TAG, "onError：");
                                Toast.makeText(getActivity(), "网络有点问题", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                final Essay essay = mAdapter.getDataSet().get(position - 1);
                                Log.i(TAG, "onResponse：取消赞时：" + position);
                                essay.setLiked(false);

                                int likeCount = essay.getLikedCount();
                                likeCount--;
                                essay.setLikedCount(likeCount);
                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                }
            }

            @Override
            public void onUserInfoClick(View view, int position) {
                //添加了头布局，所对应的position要减1
                Essay essay = mAdapter.getDataSet().get(position - 1);
                int userId = essay.getUserID();
                boolean isObserved = essay.isObserved();
                Intent intentInfo = new Intent();
                intentInfo.setClass(getActivity(), OtherPersonActivity.class);
                intentInfo.putExtra("userID", userId);//要传入什么信息
                intentInfo.putExtra("observed", isObserved);
                intentInfo.putExtra("token", token);
                startActivity(intentInfo);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ArticleDetailActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            int likeCount = bundle.getInt("likeCount");
            int commentCount = bundle.getInt("commentCount");
            int id = bundle.getInt("id");
            int position = findPositionById(id);
            Essay essay = mAdapter.getDataSet().get(position - 1);
            if (likeCount > essay.getLikedCount()) {
                //文章详情点赞，文章列表没有被点赞，文章列表要加1
                essay.setLiked(true);
                essay.setLikedCount(likeCount);
                mAdapter.notifyItemChanged(position);
            } else if (likeCount < essay.getLikedCount()) {
                //文章详情取消点赞，文章列表被点赞，文章列表要减1
                essay.setLiked(false);
                essay.setLikedCount(likeCount);
                mAdapter.notifyItemChanged(position);
            }
            if (commentCount != essay.getCommentCount()) {
                //文章详情数量不相等，文章列表的评论量加1
                essay.setCommentCount(commentCount);
                mAdapter.notifyItemChanged(position);
            }
        } else if (resultCode == ArticleCommentActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();

            int _commentCount = bundle.getInt("commentCount");
            int id = bundle.getInt("id");
            int position = findPositionById(id);
            Essay essay = mAdapter.getDataSet().get(position - 1);
            if (essay.getCommentCount() != _commentCount) {
                essay.setCommentCount(_commentCount);
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    private int findPositionById(int id) {
        int position = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (id == ids.get(i)) {
                //增加了Banner，position从1开始
                position = i + 1;
                break;
            }
        }
        return position;
    }

    public void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(getActivity(), "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        } else if (errorCode == TOKEN_OVERTIME) {
            Toast.makeText(getActivity(), "你的登录信息已过期，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        } else if (errorCode == SERVER_ERROR) {
            Toast.makeText(getActivity(), "服务器内部错误，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getActivity(), LoginActivity.class);
            startActivity(in);
        }
    }
}