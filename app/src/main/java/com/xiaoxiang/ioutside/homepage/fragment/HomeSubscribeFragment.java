package com.xiaoxiang.ioutside.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.homepage.activity.SubjectDetailActivity;
import com.xiaoxiang.ioutside.homepage.adapter.HomeSubscribeAdapter;
import com.xiaoxiang.ioutside.homepage.model.MySubject;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMySubject;
import com.xiaoxiang.ioutside.util.ACache;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 10648 on 2016/4/17 0017.
 */
public class HomeSubscribeFragment extends Fragment implements Constants, OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG = "HomeSubscribeFragment";
    private View view;
    private TextView subscribe_hint;
    private SwipeRefreshLayout swipeRefresh_subscribe;
    private RecyclerView recyclerView_subscribe;
    private HomeSubscribeAdapter mAdapter;
    private int pageSize = 5;
    private int pageNo = 1;
    private String token;
    private String fileToken;
    private boolean add = false;
    private ACache mCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.article_subscribe_fragment, container, false);
        token = getActivity().getIntent().getStringExtra("token");
        CachedInfo cachedInfo = MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
            fileToken = cachedInfo.getToken();
            Log.d(TAG, "fileToken=" + fileToken);
        }
        mCache = ACache.get(getContext());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh_subscribe = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_subscribe);
        recyclerView_subscribe = (RecyclerView) view.findViewById(R.id.recycler_subscribe);
        subscribe_hint = (TextView) view.findViewById(R.id.subscribe_hint);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_subscribe.setLayoutManager(mLayoutManager);
        recyclerView_subscribe.setHasFixedSize(true);
        recyclerView_subscribe.setItemAnimator(new NoAlphaItemAnimator());
        mAdapter = new HomeSubscribeAdapter();
        recyclerView_subscribe.setAdapter(mAdapter);
        if (token != null) {
            initSubjectData();
        } else if (token == null && fileToken == null) {
            // Toast.makeText(getActivity(),"请登录后查看!",Toast.LENGTH_SHORT).show();
            subscribe_hint.setVisibility(View.VISIBLE);
        } else {
            token = fileToken;
            initSubjectData();
        }
        initEvent();
    }

    private void initSubjectData() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            //下拉刷新的监听
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "开始刷新");
                    int pageNo = 1;
                    ApiInterImpl api = new ApiInterImpl();
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
                    okHttpManager.getStringAsyn(api.getArticleListOfUserObservedSubject(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(HomeSubscribeFragment.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Log.i(TAG, "订阅的数据得到了刷新");
                            Log.i(TAG, "  订阅的json数据" + response);
                            mCache.put("subscribe", response, 1000 * 7200);
                            Type objectType = new TypeToken<BaseResponse<GMySubject>>() {
                            }.getType();
                            Gson gson = new Gson();
                            BaseResponse attentionResponse = gson.fromJson(response, objectType);
                            GMySubject gSubject = (GMySubject) attentionResponse.getData();
                            List<MySubject> list = gSubject.getList();
                            if (list != null && list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    MySubject mySubject = list.get(i);

                                    if (mAdapter.getDataSet().size() >= pageSize) {
                                        for (int a = 0; a < pageSize; a++) {
                                            if ((mAdapter.getDataSet().get(a).getId() == mySubject.getId())) {
                                                add = true;
                                            }
                                        }
                                    }
                                    if (add == false) {
                                        mAdapter.addItemToHead(i, mySubject);
                                    } else {
                                        add = false;
                                    }
                                }
                            } else {
                                subscribe_hint.setVisibility(View.VISIBLE);

                            }
                            swipeRefresh_subscribe.setRefreshing(false);
                        }
                    });
                }
            };
            swipeRefresh_subscribe.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh_subscribe.setRefreshing(true);
                }
            });

            listener.onRefresh();
            swipeRefresh_subscribe.setOnRefreshListener(listener);

            //上拉刷新
            recyclerView_subscribe.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView_subscribe.getLayoutManager().getChildCount() > 0) {
                        View lastChildView = recyclerView_subscribe.getLayoutManager().getChildAt(recyclerView_subscribe.getLayoutManager().getChildCount() - 1);
                        int lastPosition = recyclerView_subscribe.getLayoutManager().getPosition(lastChildView);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_subscribe.getLayoutManager().getItemCount() - 1) {
                            pageNo++;
                            OkHttpManager okHttpManager = OkHttpManager.getInstance();
                            ApiInterImpl api = new ApiInterImpl();
                            okHttpManager.getStringAsyn(api.getArticleListOfUserObservedSubject(pageSize, pageNo, token), new OkHttpManager.ResultCallback<String>(HomeSubscribeFragment.this) {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.d(TAG, "error");
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response); // 通用的逻辑在 super 方法里处理完了
                                    Type objectType = new TypeToken<BaseResponse<GMySubject>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    BaseResponse attentionResponse = gson.fromJson(response, objectType);
                                    GMySubject gSubject = (GMySubject) attentionResponse.getData();
                                    List<MySubject> list = gSubject.getList();
                                    if (list != null && list.size() != 0) {
                                        for (MySubject mySubject : list) {
                                            mAdapter.addItem(mySubject);
                                        }
                                    }
                                    swipeRefresh_subscribe.setRefreshing(false);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } else {
            String response = mCache.getAsString("subscribe");
            if (response.length() != 0 && response != null) {
                Type objectType = new TypeToken<BaseResponse<GMySubject>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse attentionResponse = gson.fromJson(response, objectType);
                GMySubject gSubject = (GMySubject) attentionResponse.getData();
                List<MySubject> list = gSubject.getList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        MySubject mySubject = list.get(i);
                        mAdapter.addItemToHead(i, mySubject);
                    }
                }
                swipeRefresh_subscribe.setRefreshing(false);
            }
        }
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new HomeSubscribeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = mAdapter.getDataSet().get(position).getId();
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);//这里要传入文章的id，不然不知道是哪篇文章
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onSubjectClick(View view, int position) {
                //要跳转到专题
                int subjectId = mAdapter.getDataSet().get(position).getSubjectID();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SubjectDetailActivity.class);
                intent.putExtra("subjectID", subjectId);
                intent.putExtra("observed", true);//没有关注的字段，但是订阅的专题肯定是true
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onSubjectClick2(View view, int position) {
                //要跳转到专题,暂不做跳转
//                int subjectId = mAdapter.getDataSet().get(position).getSubjectID();
//                int columnistSubjectID = mAdapter.getDataSet().get(position).getColumnistSubjectID();
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), SubjectDetailActivity.class);
//                intent.putExtra("columnistSubjectID", columnistSubjectID);
//                intent.putExtra("observed", true);//没有关注的字段，但是订阅的专题肯定是true
//                intent.putExtra("token", token);
//                startActivity(intent);

            }
        });
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