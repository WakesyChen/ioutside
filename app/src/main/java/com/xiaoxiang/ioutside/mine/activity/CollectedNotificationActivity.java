package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.dynamic.activity.FocusActivity;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.mine.adapter.CollectedItemsAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.model.CollectedItems;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 15119 on 2016/7/22.
 */
public class CollectedNotificationActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.tv_default)
    TextView tvDefault;

    private CollectedItemsAdapter mAdapter;
    private String mToken;

    private String TAG = "CollectedNotificationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_notification);
        ButterKnife.bind(this);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        mToken = getIntent().getStringExtra("token");
        mAdapter = new CollectedItemsAdapter(new ArrayList<CollectedItems.DataBean.ListBean>());
        mAdapter.setOnItemClickListener(this);
    }

    private void initView() {
        tvTitle.setText("被收藏");
        rvContent.setAdapter(mAdapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        srlRefresh.setOnRefreshListener(this);
    }

    private void loadData() {

        String url = "http://www.ioutside.com/xiaoxiang-backend/message/get-collect-message-list.do";

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                srlRefresh.setRefreshing(false);

                Log.d(TAG, "laodData url --> " + response);
                Gson gson = new Gson();
                CollectedItems baseResponse = gson.fromJson(response,
                        new TypeToken<CollectedItems>() {
                        }.getType());

                if (!baseResponse.isSuccess()) {
                    ToastUtils.show("无法获取服务器数据");
                    return;
                }

                List<CollectedItems.DataBean.ListBean> comments = baseResponse.getData().getList();
                if (comments != null && comments.size() != 0) {
                    mAdapter.replaceItems(comments);
                }

                if (mAdapter.getDataSet().size() == 0) {
                    srlRefresh.setVisibility(View.GONE);
                    tvDefault.setVisibility(View.VISIBLE);
                } else {
                    srlRefresh.setVisibility(View.VISIBLE);
                    tvDefault.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Request request) {
                srlRefresh.setRefreshing(false);
                ToastUtils.show("网络似乎有点问题");
            }
        };

        Map<String, String> map = new HashMap<>();
        map.put("pageNo", "1");
        map.put("pageSize", "10");
        map.put("token", mToken);

        Request request = new Request.Builder()
                .url(url)
                .params(map)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_avatar :
                viewUserProfile(mAdapter.getDataSet().get(position));
                break;
            case R.id.item_notification_aio:
                viewCollectedItems(mAdapter.getDataSet().get(position));
                break;
        }
    }

    private void viewUserProfile(CollectedItems.DataBean.ListBean item) {
        Intent i = new Intent(this, OtherPersonActivity.class);
        i.putExtra("userID", item.getUserId());
        i.putExtra("token", mToken);
        startActivity(i);
    }

    private void viewCollectedItems(CollectedItems.DataBean.ListBean item) {
        Intent i = new Intent();
        if (item.getType() == 1) {
            i.setClass(this, ArticleDetailActivity.class);
        } else {
            i.setClass(this, FocusActivity.class);
        }
        i.putExtra("id", String.valueOf(item.getId()));
        i.putExtra("token", mToken);
        startActivity(i);

    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
