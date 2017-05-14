package com.xiaoxiang.ioutside.homepage.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.AppBarStateChangeListener;
import com.xiaoxiang.ioutside.homepage.adapter.SimplePagerAdapter;
import com.xiaoxiang.ioutside.homepage.adapter.SubjectEssayAdapter;
import com.xiaoxiang.ioutside.homepage.model.ChildSub;
import com.xiaoxiang.ioutside.homepage.model.SubjectContent;
import com.xiaoxiang.ioutside.homepage.model.SubjectEssay;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.adapter.BaseAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.EndlessRecyclerOnScrollListener;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GChildSubEssayList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GChildSubList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GSubCon;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/7/12.
 */
public class SubjectDetailActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.srl_refresh_items)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.tv_observer_num)
    TextView tvObserverNum;
    @Bind(R.id.tv_essay_num)
    TextView tvEssayNum;
    @Bind(R.id.iv_subscribe)
    ImageView ivSubscribe;
    @Bind(R.id.tv_exp_subcon)
    TextView tvIntroduction;
    @Bind(R.id.vp_subject_essay)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TabLayout tabs;

    private List<Page> mPageList;

    private final int ACTION_UPDATE = 0;
    private final int ACTION_ADD_MORE = 1;
    private final int ACTION_FIRST_LOAD = 2;

    private List<ChildSub> mChildSubs;

    private int subjectID;
    private String token;

    private final int pageSize = 10;

    private int mCurrentPage;

    private final String TAG = "SubjectDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_content);
        ButterKnife.bind(this);
        subjectID = getIntent().getIntExtra("subjectID", -1);
        token = getIntent().getStringExtra("token");
        init();
        initView();
        loadData();
    }

    private void init() {
        mPageList = new ArrayList<>();
    }

    private void initView() {

        final int color[] = new int[]{Color.parseColor("#44444444"), Color.parseColor("#00000000")};
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d(TAG, "appbar state --> " + state.name());
                if (state == State.COLLAPSED) {
                    toolbar.setNavigationIcon(R.drawable.ic_back_collapse);
                    toolbar.setBackground(null);
                    tvTitle.setTextColor(Color.DKGRAY);
                } else if (state == State.EXPANDED) {
                    toolbar.setNavigationIcon(R.drawable.ic_back_expand);
                    toolbar.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, color));
                    tvTitle.setTextColor(Color.WHITE);
                } else if (state == State.IDLE) {

                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_child_subject_essay_root_view:
                SubjectEssay item = ((SubjectEssayAdapter) mPageList.get(mCurrentPage)
                        .list.getAdapter())
                        .getDataSet().get(position);
                webView(item);
                break;

        }
    }

    private void webView(SubjectEssay item) {
        final int id = item.getId();
        final String token = this.token;
        Intent webView = new Intent(this, ArticleDetailActivity.class);
        webView.putExtra("token", token);
        webView.putExtra("id", id + "");
        startActivity(webView);
    }

    @OnClick({R.id.iv_subscribe})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_subscribe:
                onObserveButtonClick();
        }
    }

    private void onObserveButtonClick() {

        ApiInterImpl apiInter = new ApiInterImpl();

        String baseUrl;
        if (TextUtils.isEmpty(token)) {
            showLoginDialog();
            ToastUtils.show("请先登录");
            return;
        }

        if (ivSubscribe.isSelected()) {
            baseUrl = apiInter.getUnObserSubIn(subjectID, token);
        } else {
            baseUrl = apiInter.getObserSubIn(subjectID, token);
        }

        Log.d(TAG, "onObserveButtonClick base url --> " + baseUrl);

        HttpUtil.Callback2 callback = new HttpUtil.Callback2<BaseResponse>() {

            @Override
            public void onSuccess2(BaseResponse response) {
                if (!response.isSuccess()) return;

                if (ivSubscribe.isSelected()) {
                    ivSubscribe.setSelected(false);
                    ToastUtils.show("已取消订阅");
                } else {
                    ivSubscribe.setSelected(true);
                    ToastUtils.show("已订阅");
                }
            }

            @Override
            public void onSuccess(String response) {
//                Log.d(TAG, "onObserveButtonClick response --> " + response);
//                Gson gson = new Gson();
//                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
//
//                if (!baseResponse.isSuccess()) return;
//
//                if (ivSubscribe.isSelected()) {
//                    ivSubscribe.setSelected(false);
//                    ToastUtils.show("已取消订阅");
//                } else {
//                    ivSubscribe.setSelected(true);
//                    ToastUtils.show("已订阅");
//                }

            }

            @Override
            public void onError(Request request) {
                ToastUtils.show("订阅失败，请检查网络设置");
            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void showLoginDialog() {
        new AlertDialog.Builder(this)
                .setMessage("更多功能请登录后使用")
                .setTitle("需要登录")
                .setPositiveButton("登录/注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        login();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void login() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    /**
     * if you want the size of a view, please get in this method
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 将toolbar的title居中显示
        toolbar.setCollapsible(false);
        tvTitle.setTranslationX(-tvTitle.getLeft() / 2);

    }

    @Override
    public void onRefresh() {
        loadEssayList(mCurrentPage, ACTION_UPDATE,
                ((SubjectEssayAdapter) mPageList.get(mCurrentPage).list.getAdapter()).getDataSet() .size(), 1);
    }

    private void loadData() {
        loadChildSubject();
        loadParentSubjectData();
    }

    /**
     * get info of parent subject
     */
    private void loadParentSubjectData() {

        String url;
        if (token != null) {
            url = "http://ioutside.com/xiaoxiang-backend/article/get-subject-detail.do?subjectID="
                    + subjectID + "&token=" + token;
        } else {
            url = "http://ioutside.com/xiaoxiang-backend/article/get-subject-detail.do?subjectID="
                    + subjectID;
        }

        Log.d(TAG, "load parent subject data url --> " + url);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                BaseResponse<GSubCon> baseResponse =
                        gson.fromJson(response, new TypeToken<BaseResponse<GSubCon>>() {
                        }.getType());
                if (!baseResponse.isSuccess()) return;

                SubjectContent subjectContent;
                if ((subjectContent = baseResponse.getData().getSubject()) != null) {
                    tvTitle.setText(subjectContent.getTitle());
                    ImageLoader.getInstance().displayImage(subjectContent.getPhoto(), ivPhoto);
                    ivSubscribe.setSelected(subjectContent.isObserved());
                    tvEssayNum.setText(getString(R.string.tv_essay, subjectContent.getArticleNum()));
                    tvObserverNum.setText(getString(R.string.tv_followed, subjectContent.getObservedNum()));
                    tvIntroduction.setText(subjectContent.getIntroduction());
                }
            }

            @Override
            public void onError(Request request) {
                ToastUtils.show("加载专题信息失败");
            }
        };

        Request request = new Request.Builder()
                .url(url)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    /**
     * load child subject
     */
    private void loadChildSubject() {
        String baseUrl = new ApiInterImpl().getChildSubIn(subjectID);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, response);
                Gson gson = new Gson();
                BaseResponse<GChildSubList> result = gson.fromJson(response,
                        new TypeToken<BaseResponse<GChildSubList>>() {
                        }.getType());

                if (result.isSuccess()) {
                    List<ChildSub> childSubs;
                    if ((childSubs = result.getData().getList()) != null) {
                        mChildSubs = childSubs;
                    }
                }
                setupPages();
                setupTabs();
                loadEssayLists();
            }

            @Override
            public void onError(Request request) {

            }

        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void setupPages() {

        List<View> viewList = new ArrayList<>();

        for (int i = 0; i < mChildSubs.size(); i++) {
            RecyclerView list = new RecyclerView(this);
            BaseAdapter subjectEssayAdapter = new SubjectEssayAdapter(new ArrayList<SubjectEssay>());
            subjectEssayAdapter.setOnItemClickListener(this);
            list.setAdapter(subjectEssayAdapter);
            list.setLayoutManager(new LinearLayoutManager(this));

            mPageList.add(new Page(pageSize, 1, list, mChildSubs.get(i)));
            list.addOnScrollListener(new EndlessRecyclerOnScrollListener(list) {
                @Override
                    public void onLoadMore(int current_page) {
                    mPageList.get(mCurrentPage).pageNo++;
                    loadEssayList(mCurrentPage, ACTION_ADD_MORE, pageSize, mPageList.get(mCurrentPage).pageNo);
                }
            });
            viewList.add(list);
        }

        mViewPager.setAdapter(new SimplePagerAdapter(viewList));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
            }
        });
    }


    /**
     * set up tabs
     */
    private void setupTabs() {
        // if do this, tab layout will create as many tabs as pages. So you should update text
        // of the tabs but not add tabs to the tab layout
        tabs.setupWithViewPager(mViewPager);

        for (int i = 0; i < mChildSubs.size(); i++) {
            tabs.getTabAt(i).setText(mChildSubs.get(i).getTitle());
        }
    }

    /**
     * load essay list of child subject
     */
    private void loadEssayLists() {
        for (int i = 0; i < mChildSubs.size(); i++) {
            loadEssayList(i, ACTION_FIRST_LOAD, mPageList.get(i).pageSize, mPageList.get(i).pageNo);
        }
    }

    /**
     * update essay list of child subject at the specific position
     * @param position position of child subjects，consistent with position of the pager
     */
    private void loadEssayList(final int position, final int action, final int pageSize, int pageNo) {

        if (mChildSubs == null || mChildSubs.size() == 0) {
            return;
        }

        String url = "http://ioutside.com/xiaoxiang-backend/article/get-article-list-under-subject.do?subjectID="
                + mChildSubs.get(position).getId()
                + "&pageSize=" + pageSize + "&pageNo=" + pageNo;

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                swipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                BaseResponse<GChildSubEssayList> result =
                        gson.fromJson(response,
                                new TypeToken<BaseResponse<GChildSubEssayList>>() {
                                }.getType());

                //if our request is not successful, we have nothing to do
                if (!result.isSuccess()) return;

                SubjectEssayAdapter adapter = (SubjectEssayAdapter) mPageList
                        .get(position).list.getAdapter();

                //data that current page hold
                List<SubjectEssay> oldSubjectEssayList = adapter.getDataSet();
                //data retrieve from server
                List<SubjectEssay> newSubjectEssayList = result.getData().getArticleList();
                //if there is no data available, do nothing
                if (newSubjectEssayList == null) return;

                switch (action) {
                    case ACTION_ADD_MORE:
                        adapter.addItems(newSubjectEssayList);
                        if (newSubjectEssayList.size() < pageSize) {
                            ((SubjectEssayAdapter)mPageList.get(position).list.getAdapter())
                                    .setHasMoreData(false);
                        }
                        break;
                    case ACTION_FIRST_LOAD:
                        adapter.addItems(newSubjectEssayList);
                        break;
                    case ACTION_UPDATE:
                        if (!newSubjectEssayList.equals(oldSubjectEssayList)) {
                            Log.d(TAG, "item replaced");
                            adapter.replaceItems(newSubjectEssayList);
                        }
                        break;
                }

            }

            @Override
            public void onError(Request request) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };


        Request request = new Request.Builder()
                .url(url)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }



    /**
     * remember position of current page
     */


    @Override
    public void onBackPressed() {
        //you have to do this in order to notify previous activity if this subject has been subscribed
        Intent intent = new Intent();
        intent.putExtra("subscribeState", ivSubscribe.isSelected());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    /**
     * class that used for holding the data to do with current page
     */
    class Page {
        int pageSize;
        int pageNo;
        RecyclerView list;
        ChildSub subInfo;

        public Page(int pageSize, int pageNo,
                    RecyclerView list, ChildSub subInfo) {
            this.pageSize = pageSize;
            this.pageNo = pageNo;
            this.list = list;
            this.subInfo = subInfo;
        }
    }

}
