package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.common.adapter.DynamicAdapter;
import com.xiaoxiang.ioutside.common.adapter.RecommendAdapter;
import com.xiaoxiang.ioutside.common.adapter.UserPreviewAdapter;
import com.xiaoxiang.ioutside.dynamic.activity.FocusActivity;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;
import com.xiaoxiang.ioutside.mine.adapter.BaseAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.adapter.UserProfilePagerAdapter;
import com.xiaoxiang.ioutside.mine.model.Dynamic;
import com.xiaoxiang.ioutside.mine.model.Fan;
import com.xiaoxiang.ioutside.mine.model.OtherPerson;
import com.xiaoxiang.ioutside.mine.model.Recommend;
import com.xiaoxiang.ioutside.mine.mvp.PullAddMoreAdapter;
import com.xiaoxiang.ioutside.mine.widget.EndlessRecyclerOnScrollListener;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GDynamicList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GFansList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMyRecomList;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GOtherPerson;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherPersonActivity extends AppCompatActivity
        implements OnItemClickListener, ViewPager.OnPageChangeListener {

    private String TAG = getClass().getSimpleName();

    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_level)
    TextView tvLevel;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.iv_follow)
    ImageView ivFollow;
    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;

    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout refreshLayout;

    private List<BaseAdapter> adapters;
    private List<View> recyclerViews;

    private List<Page> mPages;

    private final int PAGE_NUM = 4;
    private int mCurrentPosition;

    private final int ACTION_UPDATE = 0x00;
    private final int ACTION_FIRST_LOAD = 0x01;
    private final int ACTION_LOAD_MORE = 0x02;

    private final int pageSize = 10;

    private String token;
    private int userID;

    private OtherPerson person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        //获取用户的 id
        userID = getIntent().getIntExtra("userID", -1);
        //之前都是这样写的，可能后面要改，最好是每次应用启动从本地磁盘取，然后放在一个静态的全局变量中
        token = getIntent().getStringExtra("token");
        Log.d(TAG, "token --> " + token);
        initData();
        initView();
        loadData();
    }

    @OnClick({R.id.iv_follow})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_follow:
                    onObserveButtonClick();
                break;
        }
    }

    public void onObserveButtonClick() {

        if (token == null) {
            ToastUtils.show("请先登陆");
            return;
        }

        if (person == null) return;

        String baseUrl;

        if (person.isObserved()) {
            baseUrl = new ApiInterImpl().cancelObserveUser(userID, token);
        } else {
            baseUrl = new ApiInterImpl().observeUser(userID, token);
        }

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "observe response --> " + response);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);

                if (! baseResponse.isSuccess()) return;

                if (! person.isObserved()) {
                    ToastUtils.show("已关注");
                    person.setObserved(true);
                    ivFollow.setSelected(true);
                } else {
                    ToastUtils.show("已取消关注");
                    person.setObserved(false);
                    ivFollow.setSelected(false);
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



    private void loadData() {
        loadUserProfileData();
        loadFans(ACTION_FIRST_LOAD, mPages.get(3).pageSize, mPages.get(3).pageNo);
        loadDynamicList(ACTION_FIRST_LOAD, mPages.get(1).pageSize, mPages.get(1).pageNo);
        loadRecommendList(ACTION_FIRST_LOAD, mPages.get(0).pageSize, mPages.get(0).pageNo);
        loadIdols(ACTION_FIRST_LOAD, mPages.get(2).pageSize, mPages.get(2).pageNo);
    }

    private void initData() {

        mPages = new ArrayList<>();

        RecommendAdapter recommendAdapter = new RecommendAdapter(new ArrayList<Recommend>());
        DynamicAdapter dynamicAdapter = new DynamicAdapter(new ArrayList<Dynamic>());

        //这里到时候要改，不过现在先这样写
        UserPreviewAdapter followAdapter = new UserPreviewAdapter(new ArrayList<Fan>());
        UserPreviewAdapter fansAdapter = new UserPreviewAdapter(new ArrayList<Fan>());

        adapters = Arrays.asList((BaseAdapter) recommendAdapter, dynamicAdapter, followAdapter, fansAdapter);

        for (BaseAdapter adapter : adapters) {
            adapter.setOnItemClickListener(this);
        }
    }

    private void initView() {

        recyclerViews = new ArrayList<>();

        for (int i = 0; i < PAGE_NUM; i++) {
            RecyclerView temp = new RecyclerView(this);
            temp.setAdapter(adapters.get(i));
            temp.setLayoutManager(new LinearLayoutManager(this));
            temp.addOnScrollListener(new EndlessRecyclerOnScrollListener(temp) {
                @Override
                public void onLoadMore(int current_page) {
                    mPages.get(mCurrentPosition).pageNo++;
                    loadDataAt(mCurrentPosition, ACTION_LOAD_MORE,
                            mPages.get(mCurrentPosition).pageSize, mPages.get(mCurrentPosition).pageNo);
                }
            });

            recyclerViews.add(temp);
        }

        //refresh list at specific position
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
            public void onRefresh() {
                loadDataAt(mCurrentPosition, ACTION_UPDATE,
                        ((BaseAdapter)mPages.get(mCurrentPosition).list.getAdapter()).getDataSet().size(), 1);
            }
        });

        String[] titles = new String[]{"推荐", "动态", "关注", "粉丝"};

        vpContent.setAdapter(new UserProfilePagerAdapter(recyclerViews, titles));
        vpContent.addOnPageChangeListener(this);

        tabs.setupWithViewPager(vpContent);

        createPages();
    }

    /**
     * create page info for each page
     */
    private void createPages() {
        for (int i = 0; i < 4; i++) {
            mPages.add(new Page((RecyclerView)recyclerViews.get(i), pageSize, 1));
        }
    }

    private void loadDataAt(int currentPosition, int action, int pageSize, int pageNo) {
        switch (currentPosition) {
            case 0 :
                loadRecommendList(action, pageSize, pageNo);
                break;
            case 1 :
                loadDynamicList(action, pageSize, pageNo);
                break;
            case 2:
                loadIdols(action, pageSize, pageNo);
                break;
            case 3:
                loadFans(action, pageSize, pageNo);
                break;
        }
    }


    /**
     * load user data
     */
    private void loadUserProfileData() {
        String baseUrl;

        if (token == null) {
            baseUrl = new ApiInterImpl().getOtherPersonIn(userID);
        } else {
            baseUrl = new ApiInterImpl().getOtherPersonWithToken(userID, token);
        }


        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                //Gson 可以做成单例模式
                Gson gson = new Gson();
                Log.d(TAG, "user profile data--> " + response);
                BaseResponse<GOtherPerson> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GOtherPerson>>(){}.getType());
                if (! baseResponse.isSuccess()) return;

                if ((person = baseResponse.getData().getUser()) != null) {
                    tvUserName.setText(person.getName());
                    tvIntroduction.setText(person.getExperiences());
                    tvLevel.setText(getString(R.string.tv_level, person.getLevel()));
                    ImageLoader.getInstance().displayImage(person.getPhoto(), civAvatar);
                    ivFollow.setSelected(person.isObserved());
                }
            }

            @Override
            public void onError(Request request) {

            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }


    private void loadRecommendList(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/article/get-others-recommend-list?pageNo=" + pageNo
                + "&pageSize=" + pageSize + "&othersID=" + userID;

        Log.d(TAG, "recommend list -->" + baseUrl);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                refreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                BaseResponse<GMyRecomList> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GMyRecomList>>(){}.getType());

                if (! baseResponse.isSuccess()) return;

                GMyRecomList data = baseResponse.getData();

                if (data != null) {
                    List<Recommend> recommendList = data.getList();
                    RecommendAdapter adapter = (RecommendAdapter) adapters.get(0);
                    updateList(action, adapter, recommendList);
                }
            }

            @Override
            public void onError(Request request) {
                refreshLayout.setRefreshing(false);

            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }


    private <T, VH extends RecyclerView.ViewHolder> void updateList(
            int action, BaseAdapter<T, VH> adapter, List<T> newList) {

        ((PullAddMoreAdapter)mPages.get(mCurrentPosition).list.getAdapter()).setHasMoreData(false);

        switch (action) {
            case ACTION_FIRST_LOAD:
                adapter.addItems(newList);
                break;
            case ACTION_LOAD_MORE:
                adapter.addItems(newList);
                break;
            case ACTION_UPDATE:
                adapter.replaceItems(newList);
                break;
        }
    }

    private void loadDynamicList(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/footprint/get-others-footprint-list?othersID=" + userID
                + "&pageNo=" + pageNo + "&pageSize=" + pageSize;

      //  String baseUrl = new ApiInterImpl().getOtherDynamicIn(10, 1, userID);

        Log.d("url", baseUrl);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                refreshLayout.setRefreshing(false);

                Log.d("---->", response);
                Gson gson = new Gson();
                BaseResponse<GDynamicList> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GDynamicList>>() {
                        }.getType());

                if (!baseResponse.isSuccess()) return;

                List<Dynamic> dynamicList = baseResponse.getData().getList();
                if (dynamicList != null) {
                    DynamicAdapter adapter = (DynamicAdapter)adapters.get(1);
                    updateList(action, adapter, dynamicList);
                }
            }

            @Override
            public void onError(Request request) {
                refreshLayout.setRefreshing(false);
            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .callback(callback)
                .method(Request.METHOD_GET)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            //如果点击的是前面两个页面
            case R.id.item_my_collection :
                if (mCurrentPosition == 0) {
                    //点击第一个页面的 item
                    viewRecommend((Recommend) adapters.get(0).getDataSet().get(position));
                } else if (mCurrentPosition == 1) {
                    //点解第二个页面的 item
                    viewDynamic((Dynamic) adapters.get(1).getDataSet().get(position));
                }
                break;
            //如果点击的是后面的两个页面
            case R.id.item_user_preview :
                //查看用户的信息
                viewUserProfile((Fan) adapters.get(mCurrentPosition).getDataSet().get(position));
                break;
        }
    }

    private void loadIdols(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/observe/get-others-observer-list.do?othersID="
                + userID + "&pageNo=" + pageNo + "&pageSize=" + pageSize;

       // String baseUrl = new ApiInterImpl().getOtherObserIn(10, 1, userID);

        Log.d(TAG, "Idols url -->" + baseUrl);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                refreshLayout.setRefreshing(false);

                Gson gson = new Gson();
                BaseResponse<GFansList> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GFansList>>(){}.getType());

                if (!baseResponse.isSuccess()) return;
                List<Fan> observers = baseResponse.getData().getList();
                if (observers != null) {
                    UserPreviewAdapter adapter = (UserPreviewAdapter)adapters.get(2);
                    updateList(action, adapter, observers);
                }
            }

            @Override
            public void onError(Request request) {
                refreshLayout.setRefreshing(false);

            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }


    private void loadFans(final int action, int pageSize, int pageNo) {

        String baseUrl = "http://ioutside.com/xiaoxiang-backend/observe/get-others-fans-list.do?othersID="
                + userID + "&pageNo=" + pageNo+"&pageSize=" + pageSize;

        //String baseUrl = new ApiInterImpl().getOtherFansIn(10, 1, userID);

        Log.d(TAG, "fans url -->" + baseUrl);

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {
                refreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                BaseResponse<GFansList> baseResponse = gson.fromJson(response,
                        new TypeToken<BaseResponse<GFansList>>(){}.getType());

                if (!baseResponse.isSuccess()) return;
                List<Fan> fans = baseResponse.getData().getList();
                if (fans != null) {
                    UserPreviewAdapter adapter = (UserPreviewAdapter)adapters.get(3);
                    updateList(action, adapter, fans);
                }
            }

            @Override
            public void onError(Request request) {
                refreshLayout.setRefreshing(false);

            }
        };

        Request request = new Request.Builder()
                .url(baseUrl)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void viewRecommend(Recommend item) {
        webView(item.getId());
    }

    private void viewUserProfile(Fan fan) {
        Intent viewUserProfile = new Intent(this, OtherPersonActivity.class);
        viewUserProfile.putExtra("token", token);
        viewUserProfile.putExtra("userID", fan.getId());
        startActivity(viewUserProfile);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 查看动态
     * @param dynamic
     */
    private void viewDynamic(Dynamic dynamic) {
        Intent viewDynamic = new Intent(this, FocusActivity.class);
        viewDynamic.putExtra("id", String.valueOf(dynamic.getId()));
        viewDynamic.putExtra("token", token);
        startActivity(viewDynamic);
    }

    private void webView(int articleID) {
        Intent viewArticle = new Intent(this, ArticleDetailActivity.class);
        viewArticle.putExtra("id", String.valueOf(articleID));
        viewArticle.putExtra("token", token);
        startActivity(viewArticle);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        Log.d("position", "-->" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class Page {

        RecyclerView list;
        int pageSize;
        int pageNo;

        public Page(RecyclerView list, int pageSize, int pageNo) {
            this.list = list;
            this.pageSize = pageSize;
            this.pageNo = pageNo;
        }
    }

    @Override
    public void onBackPressed() {
        //you have to do this in order to notify previous activity if this subject has been subscribed
        Intent intent = new Intent();
        intent.putExtra("followState", ivFollow.isSelected());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}