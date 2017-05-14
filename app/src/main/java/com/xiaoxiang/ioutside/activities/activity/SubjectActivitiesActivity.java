package com.xiaoxiang.ioutside.activities.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.adapter.SubjectActivitiesAdapter;
import com.xiaoxiang.ioutside.activities.retrofit.Query;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.TitleLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectActivitiesActivity extends AppCompatActivity implements OnItemClickListener {

    @Bind(R.id.title_layout)
    TitleLayout titleLayout;
    @Bind(R.id.rv_subject_activities)
    RecyclerView rvSubjectActivities;

    private String mToken;
    private int subjectId;
    private SubjectActivitiesAdapter mSubjectActivitiesAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_activities);
        ButterKnife.bind(this);

        init();
        loadData();
    }

    private void init() {
        mToken = getIntent().getStringExtra("token");
        subjectId = getIntent().getIntExtra("subjectId", -1);
        String title = getIntent().getStringExtra("subjectTitle");

        titleLayout.setTitleText(title);

        Log.d(getClass().getName(), "subjectId-->" + subjectId);

        String subjectPhoto = getIntent().getStringExtra("subjectPhoto");
        String subjectDescription = getIntent().getStringExtra("subjectDescription");

        SubjectActivitiesAdapter.Header header = new SubjectActivitiesAdapter.Header(subjectPhoto, subjectDescription);

        mSubjectActivitiesAdpater = new SubjectActivitiesAdapter(header, new ArrayList<>());
        mSubjectActivitiesAdpater.setOnItemClickListener(this);
        rvSubjectActivities.setLayoutManager(new LinearLayoutManager(this));
        rvSubjectActivities.setAdapter(mSubjectActivitiesAdpater);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_subject_activity:
                viewActivityDetail(mSubjectActivitiesAdpater.getDataSet().get(position - 1).activityId);
        }
    }

    private void viewActivityDetail(int activityId) {
        Intent viewActivityDetail = new Intent(this, DetailActivity.class);
        viewActivityDetail.putExtra("token", mToken);
        viewActivityDetail.putExtra("activityId", activityId);
        startActivity(viewActivityDetail);
    }

    private void loadData() {
        Query.getInstance().subjectActivities(subjectId, 10, 1)
                .map(activities -> activities.data)
                .subscribe(data -> mSubjectActivitiesAdpater.replaceItems(data.activities));
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }



}
