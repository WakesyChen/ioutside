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
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.homepage.activity.SubjectDetailActivity;
import com.xiaoxiang.ioutside.mine.adapter.EssayAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.model.CollectedEssay;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyCollEssayList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by 15119 on 2016/6/15.
 */
public class EssayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private String TAG = getClass().getSimpleName();
    private List<CollectedEssay> collectedEssays;
    private EssayAdapter essayAdapter;
    private String token;
    private int CURRENT_PAGE = 1;

    public static EssayFragment instantiate(String token) {
        Bundle args = new Bundle();
        args.putString("token", token);
        EssayFragment f = new EssayFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getArguments().getString("token");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        onRefresh();
        return rootView;
    }

    private void initView() {
        srlRefresh.setOnRefreshListener(this);
        collectedEssays = new ArrayList<>();
        essayAdapter = new EssayAdapter(collectedEssays);
        essayAdapter.setOnItemClickListener(this);
        rvContent.setAdapter(essayAdapter);
        rvContent.addOnScrollListener(mOnScrollListener);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_my_collection:
                webView(position);
                break;
            case R.id.iv_avatar:
            case R.id.tv_name:
                topic(position);
                break;
        }
    }

    private void webView(int position) {
        CollectedEssay essay = collectedEssays.get(position);
        Intent webView = new Intent(this.getActivity(), ArticleDetailActivity.class);
        webView.putExtra("token", token);
        webView.putExtra("id", String.valueOf(essay.getId()));
        startActivity(webView);
    }

    private void topic(int position){
        CollectedEssay essay = collectedEssays.get(position);
        Intent topic = new Intent(this.getActivity(), SubjectDetailActivity.class);
        topic.putExtra("subjectID", essay.getSubjectID());
        topic.putExtra("token", token);
        startActivity(topic);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (rvContent.getLayoutManager().getChildCount() > 0) {
                View lastChildView = rvContent.getLayoutManager().getChildAt(rvContent.getLayoutManager().getChildCount() - 1);
                int lastPosition = rvContent.getLayoutManager().getPosition(lastChildView);
                //进行判断
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == rvContent.getLayoutManager().getItemCount() - 1) {
                    //这里请求新的数据
                    String url = new ApiInterImpl().getMyCollectEssayIn(10, CURRENT_PAGE + 1, token);
                    Log.d(TAG, "page数为" + CURRENT_PAGE);
                    Log.d(TAG, "mycollessIn" + url);
                    OkHttpManager.getInstance().getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse<GMyCollEssayList>>() {

                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }


                        @Override
                        public void onResponse(BaseResponse<GMyCollEssayList> response) {
                            super.onResponse(response);

                            GMyCollEssayList gCollEssayList = response.getData();

                            if (gCollEssayList == null) return;

                            final ArrayList<CollectedEssay> mEssayList = gCollEssayList.getList();
                            Log.d(TAG, mEssayList.toString());

                            for (CollectedEssay essay : mEssayList) {
                                if (isNewEssay(essay)) {
                                    essayAdapter.addItem(essay);
                                }
                            }

                            srlRefresh.setRefreshing(false);
                        }
                    });
                    CURRENT_PAGE++;
                }
            }
        }

        @Override
        public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };



    private boolean isNewEssay(CollectedEssay essay) {
        for(CollectedEssay c : collectedEssays) {
            if (essay.getId() == c.getId()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRefresh() {
        String url = new ApiInterImpl().getMyCollectEssayIn(10, 1, token);
        Log.d(TAG, url);
        OkHttpManager.getInstance().getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse<GMyCollEssayList>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "error");
            }


            @Override
            public void onResponse(BaseResponse<GMyCollEssayList> response) {
                super.onResponse(response);

                GMyCollEssayList gCollEssayList = response.getData();
                if (gCollEssayList == null) return;
                final ArrayList<CollectedEssay> mEssayList = gCollEssayList.getList();
                Log.d(TAG, mEssayList.toString());
                for (CollectedEssay essay : mEssayList) {
                    if (isNewEssay(essay)) {
                        essayAdapter.addItemToHead(essay);
                    }
                }
                if (srlRefresh != null)
                srlRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
