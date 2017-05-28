package com.xiaoxiang.ioutside.circle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.Api;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.circle.adapter.CommunityAdapter;
import com.xiaoxiang.ioutside.circle.model.UserCircles;
import com.xiaoxiang.ioutside.homepage.activity.DividerGridItemDecoration;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.DividerItemDecoration;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/9/17.
 */
public class CommunityFragment extends Fragment {
    @Bind(R.id.circle_recyclerview)
    RecyclerView circle_recyclerview;
    private CommunityAdapter mAdapter;
    private List<UserCircles.Circle> datalist;
    private RecyclerView.LayoutManager layoutManager;
    public final static int STATE_NORMAL = 0;
    private final static int STATE_LOAD = 1;
    private int current_state = STATE_NORMAL;
    private int pageNo = 1;
    private int pageSize = 10;
    private int groupType;
    private String token;
    private int lastVisibleItemPosition;
//    token:d29GscwLNnlm5MQ9j58AmEtnlY8ZPA4uaJFikfxiI2zVJEhJYFs1eA9oPPfJ2IGh

    private static final String TAG = "CommunityFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circle_community, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public CommunityFragment() {
        token = CircleFragment.token;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initEvent();
    }


    //    上拉加载更多
    private void loadMore() {
        circle_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 ==
                                mAdapter.getItemCount()) {
                    current_state = STATE_LOAD;
                    pageNo++;
                    Log.i(TAG, "onScrollStateChanged: " + lastVisibleItemPosition);
                    getCircleList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();

                }
            }
        });


    }

    private void initEvent() {
         loadMore();
        mAdapter.setOnClickListener(new CommunityAdapter.OnClickListener() {
            @Override
            public void onBannerClick(int position) {
                ToastUtils.show("点击了" + position);
                Intent intent = new Intent(getActivity(), QAofVActivity.class);
                startActivity(intent);


            }

 //            1:综合小组，2：陆上小组，3水上小组，4：用户小组，5：大V问答
            @Override
            public void onFourGroupClick(int position) {
                groupType = position + 1;
                ToastUtils.show("第几个小组" + groupType);
                Intent intent=new Intent(getActivity(),CircleTypeActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("groupType",groupType);
                startActivity(intent);
//                joinCircle();
            }
        });

    }

    //    加入圈子
    private void joinCircle() {

        OkHttpManager okHttpManager = OkHttpManager.getInstance();
        ApiInterImpl api = new ApiInterImpl();
        okHttpManager.postAsyn(api.getCircleListByGroupID(token, groupType, pageNo, 100), new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG, "onCircleResponse: " + response);
                Log.i(TAG, "token " + token);

            }
        });


    }

    private void initData() {
        mAdapter = new CommunityAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        circle_recyclerview.setLayoutManager(layoutManager);
        circle_recyclerview.setHasFixedSize(true);
        circle_recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        circle_recyclerview.setAdapter(mAdapter);

//        首次加载数据
        current_state = STATE_NORMAL;
        pageNo=1;
        getCircleList();

    }

    private void getCircleList() {
        if (!TextUtils.isEmpty(token)) {
//            mAdapter.hideNotyfy();//bug点，TextView居然为空！！！

            OkHttpManager okHttpManager = OkHttpManager.getInstance();
            ApiInterImpl api = new ApiInterImpl();
            okHttpManager.getStringAsyn(api.getUsersCircle(token, pageNo, pageSize), new OkHttpManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    super.onError(request, e);
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.i(TAG, "onResponse: " + response);

                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseResponse<UserCircles>>() {
                    }.getType();
                    BaseResponse<UserCircles> baseResponse = gson.fromJson(response, type);
                    if (baseResponse.isSuccess()) {
                        datalist = baseResponse.getData().getList();
                        addTemporaryData(datalist);
                        showData(datalist);
                    } else {
                        ToastUtils.show("请求失败" + baseResponse.getErrorMessage());
                    }

                }
            });
        } else {
            ToastUtils.show("请先登录哦");
//            mAdapter.showNotify(CommunityAdapter.NOTIFY_LOGIN);


        }


    }

    private void showData(List<UserCircles.Circle> datalist) {

        switch (current_state) {
            case STATE_NORMAL:
                if (datalist != null && datalist.size() > 0) {
                    mAdapter.setData(datalist);
                    circle_recyclerview.setAdapter(mAdapter);

                } else {
                    mAdapter.showNotify(CommunityAdapter.NOTIFY_TOADD);
                }

                break;
            case STATE_LOAD:
                if (datalist != null && datalist.size() > 0) {
                    int position = mAdapter.getData().size();
                    mAdapter.addData(datalist);
                    ToastUtils.show("数据有" + mAdapter.getData().size() + "条");
                    circle_recyclerview.scrollToPosition(position);
                } else {
                    ToastUtils.show("暂无更多数据了");
                    pageNo--;//下页无数据了，页数退一页
                }

                break;


        }


    }

    //    装填临时数据
    private void addTemporaryData(List<UserCircles.Circle> datalist) {
        List<UserCircles.Circle> tempData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserCircles.Circle circle = new UserCircles.Circle();
            circle.setTitle("铁人三项");
            circle.setPostNum(112);
            circle.setPhoto("http://img5.imgtn.bdimg.com/it/u=891966115,192832720&fm=21&gp=0.jpg");
            tempData.add(circle);
        }
        if (datalist == null) {
            datalist = new ArrayList<>();
        }
        datalist.addAll(tempData);


    }
}
