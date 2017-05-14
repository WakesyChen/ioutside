package com.xiaoxiang.ioutside.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.homepage.activity.MoreSubjectsActivity;
import com.xiaoxiang.ioutside.homepage.activity.SubjectCategoryAdapter;
import com.xiaoxiang.ioutside.homepage.activity.SubjectDetailActivity;
import com.xiaoxiang.ioutside.homepage.model.Subject;
import com.xiaoxiang.ioutside.mine.activity.ExpandableMoreSubjectsActivity;
import com.xiaoxiang.ioutside.mine.adapter.ArrowSubjectItemAdapter;
import com.xiaoxiang.ioutside.mine.adapter.BaseSubjectItemAdapter;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;
import com.xiaoxiang.ioutside.mine.widget.IndicatorLayout;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GSubList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by oubin6666 on 2016/5/8.
 */
public class SubMainFragment extends Fragment
        implements BannerLayout.OnBannerChangeListener, OnItemClickListener {

    @Bind(R.id.tv_banner_title)
    TextView tvBannerTitle;
    @Bind(R.id.banner)
    BannerLayout bannerLayout;
    @Bind(R.id.indicator_layout)
    IndicatorLayout indicatorLayout;
    @Bind(R.id.rv_my_subjects)
    RecyclerView rvMySubjects;
    @Bind(R.id.tv_hint)
    TextView tvHint;

    @Bind(R.id.rv_subject_category)
    RecyclerView rvSubjectCategory;


    private String TAG = getClass().getSimpleName();
    private String token;
    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse hotSubRe;
    private BaseResponse mySubRe;
    private ArrayList<Subject> hotSubList;
    private ArrayList<Subject> mySubList;
    private BaseSubjectItemAdapter mSubAdapter;
    private CachedInfo cachedInfo;
    private String token2;

    private List<SubjectCategoryItem> mSubjectCategoryItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sub_main, container, false);
        ButterKnife.bind(this, view);
        init();
        initView();
        loadMySubjects();
        loadHotSub();
        return view;
    }


    private void initView() {

        if (indicatorLayout == null) Log.d(TAG, "indicatorLayout is null");
        indicatorLayout.setupWithBanner(bannerLayout);

        mySubList = new ArrayList<>();
        mSubAdapter = new ArrowSubjectItemAdapter(mySubList);
        mSubAdapter.setOnItemClickListener(this);

        mSubAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Log.d(TAG, "position --> " + position);

                Intent in = new Intent(getActivity(), SubjectDetailActivity.class);
                in.putExtra("token", token);
                in.putExtra("subjectID", mySubList.get(position).getId());
                Log.d(TAG, "点击的position是" + position);
                startActivity(in);
            }

        });

        rvMySubjects.setAdapter(mSubAdapter);

        mSubjectCategoryItems = getSubjectCategoryItems();
        SubjectCategoryAdapter subjectCategoryAdapter = new SubjectCategoryAdapter(mSubjectCategoryItems);
        subjectCategoryAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSubjectCategory.setAdapter(subjectCategoryAdapter);
        rvSubjectCategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_subject_category:
                if (position == 0 || position == 1 || position  == 2) {
                    openExpandableSubjectCategory(mSubjectCategoryItems.get(position));
                } else {
                    openSubjectCategory(mSubjectCategoryItems.get(position));
                }
                break;
        }
    }

    private void openExpandableSubjectCategory(SubjectCategoryItem item) {
        Intent intent = new Intent(getActivity(), ExpandableMoreSubjectsActivity.class);
        intent.putExtra("url", item.getUrl());
        intent.putExtra("token", token);
        intent.putExtra("title", item.getTitle());
        startActivity(intent);
    }

    private void openSubjectCategory(SubjectCategoryItem item) {
        Intent intent = new Intent(getActivity(), MoreSubjectsActivity.class);
        intent.putExtra("url", item.getUrl());
        intent.putExtra("token", token);
        intent.putExtra("title", item.getTitle());
        startActivity(intent);
    }

    public void init() {
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        rvMySubjects.setLayoutManager(new LinearLayoutManager(getActivity()));
        cachedInfo = MyApplication.getInstance().getCachedInfo();
        token = getActivity().getIntent().getStringExtra("token");
    }


    public void loadMySubjects() {

        if (cachedInfo != null) {
            token2 = cachedInfo.getToken();
        }
        if (token == null && token2 != null) {
            token = token2;
            Log.d(TAG, token);
        }

        if (token == null) {
            return;
        }

        //获取数据
        String mSubsIn = apiImpl.getMySubjectIn(50, 1, token);
        mOkHttpManager.getStringAsyn(mSubsIn, new OkHttpManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onResponse(String response) {

                super.onResponse(response);
                Log.d(TAG, response);

                Type ob = new TypeToken<BaseResponse<GSubList>>() {
                }.getType();

                mySubRe = gson.fromJson(response, ob);
                GSubList gSubList = (GSubList) mySubRe.getData();
                if (gSubList != null) {
                    Log.d(TAG, "我的专题是：" + mySubList.toString());
                    mSubAdapter.clear();
                    mSubAdapter.addItems(gSubList.getList());
                }

                //if no subjects subscribed,tell the user to subscribe some
                if (tvHint == null) return;
                if (mSubAdapter.getDataSet().size() == 0) tvHint.setVisibility(View.VISIBLE);
                else tvHint.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //reload my subjects
        loadMySubjects();
    }

    public void loadHotSub() {
        final String hotSubIn = apiImpl.getRecomSubjectIn(5, 1);
        Log.d(TAG, hotSubIn);
        mOkHttpManager.getStringAsyn(hotSubIn, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Type ob = new TypeToken<BaseResponse<GSubList>>() {
                }.getType();
                hotSubRe = gson.fromJson(response, ob);
                GSubList gSubList = (GSubList) hotSubRe.getData();
                if (gSubList == null) return;
                hotSubList = gSubList.getList();
                bannerLayout.setOnBannerChangeListener(SubMainFragment.this);
                bannerLayout.setViewUrls(Arrays.asList(retrievePhotoUrls(hotSubList)));
                bannerLayout.onDataUpdated();
            }
        });
    }


    @Override
    public void onBannerScrolled(int position) {
        tvBannerTitle.setText(hotSubList.get(position).getTitle());
    }

    @Override
    public void onItemClick(int position) {
        Intent in = new Intent(getActivity(), SubjectDetailActivity.class);
        in.putExtra("token", token);
        in.putExtra("subjectID", hotSubList.get(position).getId());
        Log.d(TAG, "点击的position是" + position);
        in.putExtra("observed", hotSubList.get(position).isObserved());
        startActivity(in);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private String[] retrievePhotoUrls(ArrayList<Subject> subjects) {
        String[] photoUrls = new String[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            photoUrls[i] = subjects.get(i).getPhoto();
        }
        return photoUrls;
    }

    public static class SubjectCategoryItem {

        private String title;
        private int drawableResourceId;
        private String url;

        public SubjectCategoryItem(String title, int drawableResourceId, String url) {
            this.title = title;
            this.drawableResourceId = drawableResourceId;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public int getDrawableResourceId() {
            return drawableResourceId;
        }

        public String getUrl() {
            return url;
        }

    }

    /**
     * get five subject categories
     *
     * @return what you want to get calling this method
     */
    private List<SubjectCategoryItem> getSubjectCategoryItems() {

        List<SubjectCategoryItem> subjectCategoryItems = new ArrayList<>();

        SubjectCategoryItem special = new SubjectCategoryItem("专栏作家", R.drawable.src_subject_special, "http://ioutside.com/xiaoxiang-backend/article/get-special-article-subject-list?type=2");
        SubjectCategoryItem first = new SubjectCategoryItem("第一次系列", R.drawable.src_subject_first, "http://ioutside.com/xiaoxiang-backend/article/get-special-article-subject-list?type=1");
        SubjectCategoryItem water = new SubjectCategoryItem("水上", R.drawable.src_subject_water, "http://ioutside.com/xiaoxiang-backend/article/get-child-subject-type-list?parentType=2");
        SubjectCategoryItem land = new SubjectCategoryItem("陆上", R.drawable.src_subject_land, "http://ioutside.com/xiaoxiang-backend/article/get-child-subject-type-list?parentType=1");
        SubjectCategoryItem all = new SubjectCategoryItem("综合", R.drawable.src_subject_all, "http://ioutside.com/xiaoxiang-backend/article/get-child-subject-type-list?parentType=0");

        subjectCategoryItems.add(all);
        subjectCategoryItems.add(land);
        subjectCategoryItems.add(water);
        subjectCategoryItems.add(special);
        subjectCategoryItems.add(first);

        return subjectCategoryItems;
    }


}
