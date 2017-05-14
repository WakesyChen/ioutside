package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.model.Subject;
import com.xiaoxiang.ioutside.mine.adapter.BaseSubjectItemAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.adapter.SubscribeSubjectItemAdapter;
import com.xiaoxiang.ioutside.mine.widget.EndlessRecyclerOnScrollListener;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GSubList;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * input data :
 * String token : unique string for each user;
 * String url : url with which we get subject list form server;
 * String title : title of this page.
 */
public class MoreSubjectsActivity extends Activity implements
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    private String TAG = "MoreSubjectActivity";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_subjects)
    RecyclerView rvSubjects;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;

    private String mTitle;
    private String mUrl;
    private String mToken;

    private int pageNo = 1;
    private final int PAGE_SIZE = 10;

    private final int ACTION_LOAD_MORE = 0;
    private final int ACTION_UPDATE = 1;
    private final int ACTION_FIRST_LOAD = 2;

    public int REQUEST_MORE_SUBJECTS = 0x01;

    private int mTempPosition;

    private BaseSubjectItemAdapter mSubjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_sub);
        ButterKnife.bind(this);
        initData();
        initView();
        loadSubjects(ACTION_FIRST_LOAD, pageNo, PAGE_SIZE);
    }

    private void initData() {

        mTitle = getIntent().getStringExtra("title");

        mToken = getIntent().getStringExtra("token");
        mUrl = getIntent().getStringExtra("url");

        if (mToken != null) {
            mUrl += "&token=" + mToken;
        }

        List<Subject> subjects = new ArrayList<>();
        mSubjectAdapter = new SubscribeSubjectItemAdapter(subjects);
        mSubjectAdapter.setOnItemClickListener(this);
    }

    private void initView() {
        tvTitle.setText(mTitle);
        rvSubjects.setAdapter(mSubjectAdapter);
        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        rvSubjects.addOnScrollListener(new EndlessRecyclerOnScrollListener(rvSubjects) {
            @Override
            public void onLoadMore(int current_page) {
                pageNo ++ ;
                loadSubjects(ACTION_LOAD_MORE, pageNo, PAGE_SIZE);
            }
        });
        srlRefresh.setOnRefreshListener(this);
    }

    private void loadSubjects(final int action, int pageNo, final int pageSize) {
        Log.d(TAG, "pageNo --> " + pageNo + " , pageSize --> " + pageSize);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                srlRefresh.setRefreshing(false);
                Log.d(TAG, response);

                Gson gson = new Gson();
                BaseResponse<GSubList> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GSubList>>() {
                        }.getType());

                if (!baseResponse.isSuccess()) return;

                List<Subject> subjects = baseResponse.getData().getList();
                if (subjects != null) {
                    switch (action) {
                        case ACTION_FIRST_LOAD :
                            mSubjectAdapter.replaceItems(subjects);
                            break;
                        case ACTION_LOAD_MORE :
                            mSubjectAdapter.addItems(subjects);
                            if (subjects.size() < pageSize) {
                                mSubjectAdapter.setHasMoreData(false);
                            }
                            break;
                        case ACTION_UPDATE :
                            mSubjectAdapter.replaceItems(subjects);
                            break;
                    }
                }
            }

            @Override
            public void onError(Request request) {
                srlRefresh.setRefreshing(false);
            }
        };

        String url = mUrl + "&pageSize=" + pageSize + "&pageNo=" + pageNo;

        Log.d(TAG, "mUrl --> " + mUrl);

        Request request = new Request.Builder()
                .url(url)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    @Override
    public void onRefresh() {
        loadSubjects(ACTION_UPDATE, 1, mSubjectAdapter.getItemCount() - 1);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_subject:
                mTempPosition = position;
                viewSubjectDetail(mSubjectAdapter.getDataSet().get(position));
                break;
            case R.id.iv_subscribe:
                subscribe(mSubjectAdapter.getDataSet().get(position));
                break;
        }
    }

    private void subscribe(final Subject subject) {

        if (mToken == null) {
            ToastUtils.show("请先登录");
        }

        ApiInterImpl api = new ApiInterImpl();

        String url;
        if (subject.isObserved()) {
            url = api.getUnObserSubIn(subject.getId(), mToken);
        } else {
            url = api.getObserSubIn(subject.getId(), mToken);
        }

        Log.d(TAG, "subscribe url --> " + url);

        HttpUtil.Callback callback = new HttpUtil.Callback() {

            @Override
            public void onSuccess(String response) {

                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, new TypeToken<BaseResponse>() {
                }.getType());
                if (!baseResponse.isSuccess()) return;

                if (subject.isObserved()) {
                    subject.setObserved(false);
                    mSubjectAdapter.notifyDataSetChanged();
                } else {
                    subject.setObserved(true);
                    mSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Request request) {
                if (subject.isObserved()) {
                    ToastUtils.show("取消订阅失败，请检查网络");
                } else {
                    ToastUtils.show("订阅失败，请检查网络");
                }

            }
        };

        Request request = new Request.Builder()
                .url(url)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void viewSubjectDetail(Subject subject) {
        Intent viewSubjectDetail = new Intent(this, SubjectDetailActivity.class);
        viewSubjectDetail.putExtra("subjectID", subject.getId());
        viewSubjectDetail.putExtra("token", mToken);
        startActivityForResult(viewSubjectDetail, REQUEST_MORE_SUBJECTS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSubjectAdapter != null) {
            mSubjectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_MORE_SUBJECTS) {
            if (data == null) return;
            boolean state = data.getBooleanExtra("subscribeState", false);

            Subject subject = mSubjectAdapter.getDataSet().get(mTempPosition);
            subject.setObserved(state);
            mSubjectAdapter.notifyDataSetChanged();
        }
    }
}
