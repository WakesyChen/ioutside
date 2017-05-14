package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.dynamic.activity.FocusActivity;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.homepage.adapter.SimplePagerAdapter;
import com.xiaoxiang.ioutside.mine.adapter.CommentItemsAdapter;
import com.xiaoxiang.ioutside.mine.adapter.CommentedItemsAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.model.CommentItems;
import com.xiaoxiang.ioutside.mine.model.CommentedItems;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 15119 on 2016/7/22.
 */
public class CommentNotificationActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.rv_comment)
    RecyclerView rvComment;
    @Bind(R.id.rv_commented)
    RecyclerView rvCommented;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.tabs)
    TabLayout tabLayout;

    private String mToken;
    private CommentItemsAdapter mCommentItemsAdapter;
    private CommentedItemsAdapter mCommentedItemsAdapter;

    private final int INDEX_COMMENT = 0x00;
    private final int INDEX_COMMENTED = 0x01;

    private int mCurrentPageIndex;

    private final String URL_COMMENT = "http://www.ioutside.com/xiaoxiang-backend/message/get-send-comment-message-list";
    private final String URL_COMMENTED = "http://www.ioutside.com/xiaoxiang-backend/message/get-receive-comment-message-list";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commented_notification);
        ButterKnife.bind(this);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        mToken = getIntent().getStringExtra("token");
        mCommentedItemsAdapter = new CommentedItemsAdapter(new ArrayList<CommentedItems.DataBean.ListBean>());
        mCommentItemsAdapter = new CommentItemsAdapter(new ArrayList<CommentItems.DataBean.ListBean>());
        mCommentItemsAdapter.setOnItemClickListener(this);
        mCommentedItemsAdapter.setOnItemClickListener(this);
    }

    private void initView() {

        tvTitle.setText("评论");

        rvComment.setAdapter(mCommentItemsAdapter);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvCommented.setAdapter(mCommentedItemsAdapter);
        rvCommented.setLayoutManager(new LinearLayoutManager(this));

        vpContent.setAdapter(new SimplePagerAdapter(Arrays.asList((View) rvComment, rvCommented)));
        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
            }
        });

        srlRefresh.setOnRefreshListener(this);

        tabLayout.addTab(tabLayout.newTab().setText("我的评论"));
        tabLayout.addTab(tabLayout.newTab().setText("评论我的"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpContent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_avatar :
                if (mCurrentPageIndex == INDEX_COMMENT) {
                    viewUserProfile(mCommentItemsAdapter.getDataSet().get(position).getUserId());
                } else {
                    viewUserProfile(mCommentedItemsAdapter.getDataSet().get(position).getUserId());
                }

                break;
            case R.id.item_notification_aio:
                if (mCurrentPageIndex == INDEX_COMMENT) {
                    viewCollectedItems(mCommentItemsAdapter.getDataSet().get(position).getId(),
                            mCommentItemsAdapter.getDataSet().get(position).getType());
                } else {
                    viewCollectedItems(mCommentedItemsAdapter.getDataSet().get(position).getId(),
                    mCommentedItemsAdapter.getDataSet().get(position).getType());
                }
                break;
        }
    }

    private void viewUserProfile(int id) {
        Intent i = new Intent(this, OtherPersonActivity.class);
        i.putExtra("userID", id);
        i.putExtra("token", mToken);
        startActivity(i);
    }

    private void viewCollectedItems(int id, int type) {
        Intent i = new Intent();
        if (type == 1) {
            i.setClass(this, ArticleDetailActivity.class);
        } else {
            i.setClass(this, FocusActivity.class);
        }
        i.putExtra("id", String.valueOf(id));
        i.putExtra("token", mToken);
        startActivity(i);

    }

    private void loadData() {
        loadCommentItems();
        loadCommentedItems();
    }

    private void loadCommentItems() {

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                srlRefresh.setRefreshing(false);

                Gson gson = new Gson();
                CommentItems commentItems = gson.fromJson(response, new TypeToken<CommentItems>() {
                }.getType());

                if (!commentItems.isSuccess()) {
                    ToastUtils.show("服务器错误");
                    return;
                }

                List<CommentItems.DataBean.ListBean> commentItemList = commentItems.getData().getList();

                if (commentItemList != null && !commentItemList.isEmpty()) {
                    mCommentItemsAdapter.replaceItems(commentItemList);
                }

            }

            @Override
            public void onError(Request request) {
                srlRefresh.setRefreshing(false);
                ToastUtils.show("网络似乎有点问题");
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("token", mToken);
        params.put("pageNo", "1");
        params.put("pageSize", "10");

        Request request = new Request.Builder()
                .url(URL_COMMENT)
                .params(params)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void loadCommentedItems() {
        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                srlRefresh.setRefreshing(false);

                Gson gson = new Gson();
                CommentedItems commentedItems = gson.fromJson(response, new TypeToken<CommentedItems>() {
                }.getType());

                if (!commentedItems.isSuccess()) {
                    ToastUtils.show("服务器错误");
                    return;
                }

                List<CommentedItems.DataBean.ListBean> commentedItemList = commentedItems.getData().getList();

                if (commentedItemList != null && !commentedItemList.isEmpty()) {
                    mCommentedItemsAdapter.replaceItems(commentedItemList);
                }

            }

            @Override
            public void onError(Request request) {
                srlRefresh.setRefreshing(false);
                ToastUtils.show("网络似乎有点问题");
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("token", mToken);
        params.put("pageNo", "1");
        params.put("pageSize", "10");

        Request request = new Request.Builder()
                .url(URL_COMMENTED)
                .params(params)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    @Override
    public void onRefresh() {
        if (mCurrentPageIndex == INDEX_COMMENT)
            loadCommentItems();
        else if (mCurrentPageIndex == INDEX_COMMENTED)
            loadCommentedItems();
    }
}
