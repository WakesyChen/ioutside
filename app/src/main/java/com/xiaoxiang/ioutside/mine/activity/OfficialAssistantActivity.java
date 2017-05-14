package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.model.OfficialNotifications;
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
public class OfficialAssistantActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.tv_default)
    TextView tvDefault;

    private String mToken;

    private OfficialNotificationsAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_notification);
        ButterKnife.bind(this);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        Intent data = getIntent();
        mToken = data.getStringExtra("token");

        mAdapter = new OfficialNotificationsAdapter(new ArrayList<OfficialNotifications.DataBean.ListBean>());
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mAdapter);
    }

    private void initView() {
        tvTitle.setText("官方小助手");
        srlRefresh.setOnRefreshListener(this);
    }

    private void loadData() {

        String url = "http://www.ioutside.com/xiaoxiang-backend/message/get-official-message-list";

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                srlRefresh.setRefreshing(false);
                Gson gson = new Gson();
                OfficialNotifications baseResponse = gson.fromJson(response,
                        new TypeToken<OfficialNotifications>(){}.getType());

                if (!baseResponse.isSuccess()) {
                    ToastUtils.show("无法获取服务器数据");
                    return;
                }

                List<OfficialNotifications.DataBean.ListBean> notifications = baseResponse.getData().getList();
                if (notifications != null && notifications.size() != 0) {
                    mAdapter.replaceItems(notifications);
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
    public void onRefresh() {
        loadData();
    }
}
