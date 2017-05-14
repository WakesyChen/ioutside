package com.xiaoxiang.ioutside.activities.fragment;

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

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.activity.ActivityFilterActivity;
import com.xiaoxiang.ioutside.activities.activity.DetailActivity;
import com.xiaoxiang.ioutside.activities.activity.SubjectActivitiesActivity;
import com.xiaoxiang.ioutside.activities.adapter.RecommendActivitiesAdapter;
import com.xiaoxiang.ioutside.activities.retrofit.Bean;
import com.xiaoxiang.ioutside.activities.retrofit.Query;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.BannerLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.HEAD;

/**
 * Created by lwenkun on 16/8/30.
 */
public class ActivitiesFragment extends Fragment implements OnItemClickListener,
        RecommendActivitiesAdapter.OnBannerItemClickListener {

    @Bind(R.id.rv_activities)
    RecyclerView rvActivities;

    private Bean.Banner.Data mHeaderData;
    private RecommendActivitiesAdapter mRecommendActivitiesAdapter;
    private static final String TAG = "ActivitiesFragment";
    private String mToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_activities, container, false);
        ButterKnife.bind(this, view);
        init();
        loadData();
        return view;
    }

    private void init() {
        mToken = getActivity().getIntent().getStringExtra("token");

        rvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendActivitiesAdapter = new RecommendActivitiesAdapter(null, new ArrayList<>());
        mRecommendActivitiesAdapter.setOnItemClickListener(this);
        mRecommendActivitiesAdapter.setOnSectionClickListener(new RecommendActivitiesAdapter.OnSectionClickListener() {
            @Override
            public void onEditorChoiceSelected() {
                Intent i = new Intent(getActivity(), ActivityFilterActivity.class);
                mToken = getActivity().getIntent().getStringExtra("token");
                i.putExtra("type", 1);
                i.putExtra("token", mToken);
                startActivity(i);
            }

            @Override
            public void onTrainingSeleted() {
                Intent i = new Intent(getActivity(), ActivityFilterActivity.class);
                i.putExtra("type", 2);
                i.putExtra("token", mToken);
                startActivity(i);
            }
        });
        mRecommendActivitiesAdapter.setOnBannerItemClickListener(this);
        rvActivities.setAdapter(mRecommendActivitiesAdapter);
    }

    @Override
    public void onItemClick(BannerLayout b, int position) {
         viewActivityDetail(mHeaderData.list.get(position).activityId);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_large_photo:
                viewSubjectActivities(mRecommendActivitiesAdapter.getDataSet().get(position - 1));
                break;
            case R.id.tv_first_text:
                viewActivityDetail(mRecommendActivitiesAdapter.getDataSet().
                        get(position - 1).activityList.get(0).activityId);
                break;
            case R.id.tv_second_text:
                viewActivityDetail(mRecommendActivitiesAdapter.getDataSet().
                        get(position - 1).activityList.get(1).activityId);
                break;
            case R.id.tv_third_text:
                viewActivityDetail(mRecommendActivitiesAdapter.getDataSet().
                        get(position - 1).activityList.get(2).activityId);
                break;
        }
    }

    private void viewSubjectActivities(Bean.RecommendActivitySubject.Data.ActivitySubject activitySubject) {
        Intent viewSubjectActivities = new Intent(getActivity(), SubjectActivitiesActivity.class);
        viewSubjectActivities.putExtra("subjectPhoto", activitySubject.subjectPhoto);
        viewSubjectActivities.putExtra("subjectTitle", activitySubject.subjectTitle);
        viewSubjectActivities.putExtra("subjectDescription", activitySubject.subjectContent);
        viewSubjectActivities.putExtra("subjectId", activitySubject.subjectId);
        startActivity(viewSubjectActivities);
    }

    private void viewActivityDetail(int activityId) {
        Intent viewActivityDetail = new Intent(getActivity(), DetailActivity.class);
        viewActivityDetail.putExtra("activityId", activityId);
        viewActivityDetail.putExtra("token", mToken);
        startActivity(viewActivityDetail);


    }

    private void loadData() {

        Query.getInstance().activityBanner()
                .map(banner -> banner.data)
                .subscribe(data -> {
                    Log.d(getTag(), data.list.toString());
                    mHeaderData = data;
                    Log.i(TAG, "loadData: "+data.list+"size:"+data.list.size());
                    mRecommendActivitiesAdapter.setHeaderData(data.list);
                }, Throwable::printStackTrace);

        Query.getInstance().recommendActivitySubject(3, 5, 1)
                .map(recommendActivitySubject -> recommendActivitySubject.data)
                .subscribe(data -> mRecommendActivitiesAdapter.replaceItems(data.activitySubjects),
                        Throwable::printStackTrace);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
