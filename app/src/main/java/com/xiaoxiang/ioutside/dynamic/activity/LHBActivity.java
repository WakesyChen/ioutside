package com.xiaoxiang.ioutside.dynamic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.dynamic.adapter.LHBAdapter;
import com.xiaoxiang.ioutside.dynamic.model.LHB;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.mine.adapter.BaseAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;
import com.xiaoxiang.ioutside.mine.widget.EndlessRecyclerOnScrollListener;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 龙虎榜
 * Created by 15119 on 2016/8/6.
 */
public class LHBActivity extends AppCompatActivity implements OnItemClickListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tl_title)
    TabLayout tlTitle;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    private final int pageSize = 10;

    private final String TAG = "LHBActivity";

    private int mCurrentPage;
    private int mCurrentPosition;

    private final int ACTION_FIRST_LOAD = 0;
    private final int ACTION_LOAD_MORE = 1;
    private final int ACTION_UPDATE = 2;

    private RecyclerView rvWater;
    private RecyclerView rvLand;

    SwipeRefreshLayout refreshWater;
    SwipeRefreshLayout refreshLand;

    private String mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhb);
        ButterKnife.bind(this);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        mToken = getIntent().getStringExtra("token");
    }

    private void initView() {

        refreshWater = new SwipeRefreshLayout(this);
        refreshLand = new SwipeRefreshLayout(this);

        rvLand = new RecyclerView(this);
        rvLand.setLayoutManager(new LinearLayoutManager(this));
        LHBAdapter landAdapter = new LHBAdapter(new ArrayList<LHB.DataBean.ListBean>());
        landAdapter.setOnItemClickListener(this);
        rvLand.setAdapter(landAdapter);
        rvLand.addOnScrollListener(new EndlessRecyclerOnScrollListener(rvLand) {
            @Override
            public void onLoadMore(int current_page) {
                loadLandExpert(ACTION_LOAD_MORE, pageSize, current_page);
            }
        });

        rvWater = new RecyclerView(this);
        rvWater.setLayoutManager(new LinearLayoutManager(this));
        LHBAdapter waterAdapter = new LHBAdapter(new ArrayList<LHB.DataBean.ListBean>());
        waterAdapter.setOnItemClickListener(this);
        rvWater.setAdapter(waterAdapter);
        rvWater.addOnScrollListener(new EndlessRecyclerOnScrollListener(rvWater) {
            @Override
            public void onLoadMore(int current_page) {
                loadWaterExpert(ACTION_LOAD_MORE, pageSize, current_page);
            }
        });

        refreshWater.addView(rvWater);
        refreshWater.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWaterExpert(ACTION_UPDATE,((BaseAdapter)rvWater.getAdapter()).getDataSet().size(), 1);
            }
        });

        refreshLand.addView(rvLand);
        refreshLand.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLandExpert(ACTION_UPDATE, ((BaseAdapter)rvLand.getAdapter()).getDataSet().size(), 1);
            }
        });

        List<View> viewList = Arrays.asList((View)refreshWater, refreshLand);

        vpContent.setAdapter(new TitlePagerAdapter(viewList, new String[]{"水上", "陆上"}));
        vpContent.addOnPageChangeListener(this);
        tlTitle.setupWithViewPager(vpContent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(View v, int position) {
        mCurrentPosition = position;
        LHB.DataBean.ListBean item;
        item = mCurrentPage == 0 ?
                ((LHBAdapter)rvWater.getAdapter()).getDataSet().get(position) :
                ((LHBAdapter)rvLand.getAdapter()).getDataSet().get(position);

        switch (v.getId()) {
            case R.id.item_lhb :
                viewUserProfile(item.getId());
                break;
            case R.id.iv_observe:
                onObserveButtonClick((ImageView) v, item);
                break;
        }
    }

    private void viewUserProfile(int id) {
        Intent viewUserProfile = new Intent(this, OtherPersonActivity.class);
        viewUserProfile.putExtra("token", mToken);
        viewUserProfile.putExtra("userID", id);
        startActivityForResult(viewUserProfile, REQUEST_USER_PROFILE);
    }

    private void loadData() {
        loadLandExpert(ACTION_FIRST_LOAD, pageSize, 1);
        loadWaterExpert(ACTION_FIRST_LOAD, pageSize, 1);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void loadWaterExpert(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/user/get-water-expert-list";

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {

                Log.d(TAG, response);
                stopRefreshing(refreshWater);

                Gson gson = new Gson();
                LHB lhb = gson.fromJson(response, LHB.class);

                if (!lhb.isSuccess()) return;

                List<LHB.DataBean.ListBean> lhbUsers = lhb.getData().getList();

                LHBAdapter adapter = (LHBAdapter) rvWater.getAdapter();

                if (lhbUsers != null) {
                    updateList(action, adapter, lhbUsers);
                }
            }

            @Override
            public void onError(Request request) {
                stopRefreshing(refreshWater);
                ToastUtils.show("请检查网设置");
            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .addParam("pageNo", String.valueOf(pageNo))
                .addParam("pageSize", String.valueOf(pageSize))
                .addParam("token", mToken)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void loadLandExpert(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/user/get-land-expert-list";

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, response);

                stopRefreshing(refreshLand);

                Gson gson = new Gson();
                LHB lhb = gson.fromJson(response, LHB.class);

                if (!lhb.isSuccess()) return;

                List<LHB.DataBean.ListBean> lhbUsers = lhb.getData().getList();

                LHBAdapter adapter = (LHBAdapter) rvLand.getAdapter();

                if (lhbUsers != null) {
                    updateList(action, adapter, lhbUsers);
                }
            }

            @Override
            public void onError(Request request) {
                stopRefreshing(refreshLand);

                ToastUtils.show("请检查网设置");
            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .addParam("pageNo", String.valueOf(pageNo))
                .addParam("pageSize", String.valueOf(pageSize))
                .addParam("token", mToken).method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void stopRefreshing(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onObserveButtonClick(final ImageView v, final LHB.DataBean.ListBean item) {

        if (mToken == null) {
            ToastUtils.show("请先登陆");
            return;
        }

        if (item == null) return;

        String baseUrl;

        if (item.isObserved()) {
            baseUrl = new ApiInterImpl().cancelObserveUser(item.getId(), mToken);
        } else {
            baseUrl = new ApiInterImpl().observeUser(item.getId(), mToken);
        }

        Log.d(TAG, "baseurl -->" + baseUrl);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "observe response --> " + response);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);

                if (! baseResponse.isSuccess()) return;

                if (! item.isObserved()) {
                    ToastUtils.show("已关注");
                    item.setObserved(true);
                    v.setSelected(true);
                } else {
                    ToastUtils.show("已取消关注");
                    item.setObserved(false);
                    v.setSelected(false);
                }
            }

            @Override
            public void onError(Request request) {
                ToastUtils.show("请检查网络设置");
            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private <T> void updateList(int action, PullAddMoreAdapter<T> addMoreAdapter, List<T> newList) {

        addMoreAdapter.setHasMoreData(false);

        switch (action) {
            case ACTION_FIRST_LOAD:
                addMoreAdapter.addItems(newList);
                break;
            case ACTION_LOAD_MORE:
                addMoreAdapter.addItems(newList);
                break;
            case ACTION_UPDATE:
                addMoreAdapter.replaceItems(newList);
                break;
        }
    }

    private final int REQUEST_USER_PROFILE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_USER_PROFILE && resultCode == RESULT_OK) {
            LHB.DataBean.ListBean item;
            if (mCurrentPage == 0) {
                item = ((LHBAdapter)rvWater.getAdapter()).getDataSet().get(mCurrentPosition);
                item.setObserved(data.getBooleanExtra("followState", false));
                rvWater.getAdapter().notifyDataSetChanged();
            } else {
                item = ((LHBAdapter)rvLand.getAdapter()).getDataSet().get(mCurrentPosition);
                item.setObserved(data.getBooleanExtra("followState", false));
                rvLand.getAdapter().notifyDataSetChanged();
            }
        }
    }


}
