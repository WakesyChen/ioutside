package com.xiaoxiang.ioutside.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.Api;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.dynamic.activity.FocusActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.mine.adapter.DiscoveryAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.model.CollectedFootPrint;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyCollFootList;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by 15119 on 2016/6/15.
 */
public class DiscoveryFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private int CURRENT_PAGE2 = 1;
    private String token;
    private String TAG = getClass().getSimpleName();

    private List<CollectedFootPrint> footPrints;

    private DiscoveryAdapter discoveryAdapter;

    public static DiscoveryFragment instantiate(String token) {
        DiscoveryFragment f = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getArguments().getString("token");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, v);
        initView();
        getFootPrints();
        return v;
    }

    public void initView() {
        srlRefresh.setOnRefreshListener(this);
        footPrints = new ArrayList<>();
        discoveryAdapter = new DiscoveryAdapter(footPrints);
        discoveryAdapter.setOnItemClickListener(this);
        rvContent.setAdapter(discoveryAdapter);
        rvContent.addOnScrollListener(mOnScrollListener);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_avatar:
            case R.id.tv_name:
                authorProfile(position);
                break;
            case R.id.item_my_collection:
                webView(position);
                break;
        }
    }

    private void authorProfile(int position) {
        CollectedFootPrint footPrint = footPrints.get(position);
        Intent authorProfile = new Intent(this.getActivity(), OtherPersonActivity.class);
        authorProfile.putExtra("token", token);
        authorProfile.putExtra("userID", footPrint.getUserID());
        startActivity(authorProfile);
    }

    private void webView(int position) {
        CollectedFootPrint footPrint = footPrints.get(position);
        Intent webView = new Intent(this.getActivity(), FocusActivity.class);
        webView.putExtra("token", token);
        webView.putExtra("id", String.valueOf(footPrint.getId()));
        startActivity(webView);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (rvContent.getLayoutManager().getChildCount() > 0) {
                View lastChildView = rvContent.getLayoutManager().getChildAt(rvContent.getLayoutManager().getChildCount() - 1);
                int lastPosition = rvContent.getLayoutManager().getPosition(lastChildView);
                //进行判断
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == rvContent.getLayoutManager().getItemCount() - 1) {
                    //这里请求新的数据
                    String url = new ApiInterImpl().getMyCollectFootPrintIn(2, CURRENT_PAGE2 + 1, token);
                    Log.d(TAG, "page数为" + CURRENT_PAGE2);
                    Log.d(TAG, "mycollfootIn" + url);
                    OkHttpManager.getInstance().getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse<GMyCollFootList>>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(BaseResponse<GMyCollFootList> response) {
                            super.onResponse(response);

                            Log.d(TAG, "数据转型成功");
                            GMyCollFootList gCollFootList = response.getData();
                            ArrayList<CollectedFootPrint> mFootList = gCollFootList.getList();
                            Log.d(TAG, mFootList.toString());

                            for (CollectedFootPrint footPrint : mFootList) {
                                if (isNewFootPrint(footPrint)) {
                                    discoveryAdapter.addItem(footPrint);
                                }
                            }
                            srlRefresh.setRefreshing(false);
                        }
                    });
                    CURRENT_PAGE2++;
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        getFootPrints();
    }

    public void getFootPrints() {

        Api api = new ApiInterImpl();
        OkHttpManager.getInstance().getStringAsyn(api.getMyCollectFootPrintIn(10, 1, token),
                new OkHttpManager.ResultCallback<BaseResponse<GMyCollFootList>>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络有点问题哦");
                        Log.d(TAG, "error");
                    }

                    @Override
                    public void onResponse(BaseResponse<GMyCollFootList> response) {
                        super.onResponse(response);
                        GMyCollFootList gCollFootList = response.getData();
                        if (gCollFootList != null) {
                            ArrayList<CollectedFootPrint> mFootList = gCollFootList.getList();
                            Log.d(TAG, mFootList.toString());
                            for (CollectedFootPrint footPrint : mFootList) {
                                if (isNewFootPrint(footPrint)) {
                                    discoveryAdapter.addItemToHead(footPrint);
                                }
                            }
                        }
                        if (srlRefresh != null)
                        srlRefresh.setRefreshing(false);
                    }
                });
    }

    private boolean isNewFootPrint(CollectedFootPrint footPrint) {
        for(CollectedFootPrint c : footPrints) {
            if (footPrint.getId() == c.getId()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
