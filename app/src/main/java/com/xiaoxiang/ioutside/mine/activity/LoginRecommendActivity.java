package com.xiaoxiang.ioutside.mine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.activity.MainActivity;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.adapter.RecommendPeopleAdapter;
import com.xiaoxiang.ioutside.mine.adapter.RecommendTopicsAdapter;
import com.xiaoxiang.ioutside.mine.model.RecommendPeople;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;
import com.xiaoxiang.ioutside.mine.mvp.RecommendContract;
import com.xiaoxiang.ioutside.mine.mvp.RecommendDataSource;
import com.xiaoxiang.ioutside.mine.mvp.RecommendPresenter;
import com.xiaoxiang.ioutside.mine.mvp.RecommendRepository;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/6/28.
 */
public class LoginRecommendActivity extends AppCompatActivity implements RecommendContract.View {

    @Bind(R.id.rv_recommend_topics)
    RecyclerView rvRecommendTopics;
    @Bind(R.id.rv_recommend_people)
    RecyclerView rvRecommendPeople;
    @Bind(R.id.iv_enter_app)
    ImageView ivEnterApp;

    private ProgressDialog waitDialog;

    private int sucesss = 0;

    private List<RecommendPeople.DataBean.ListBean> mRecommendPeople;
    private List<RecommendTopic.DataBean.ListBean> mRecommendTopics;

    private RecommendTopicsAdapter mRecommendTopicAdapter;
    private RecommendPeopleAdapter mRecommendPeopleAdapter;

    private RecommendContract.Presenter mPresenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_recommend);
        ButterKnife.bind(this);

        mRecommendPeople = new ArrayList<>();
        mRecommendPeopleAdapter = new RecommendPeopleAdapter(mRecommendPeople);
        mRecommendPeopleAdapter.setOnItemClickListener(mOnItemClickListener);

        mRecommendTopics = new ArrayList<>();
        mRecommendTopicAdapter = new RecommendTopicsAdapter(mRecommendTopics);
        mRecommendTopicAdapter.setOnItemClickListener(mOnItemClickListener);
        initView();

        new RecommendPresenter(this, new RecommendRepository()).start();
    }

    public void initView() {

        GridLayoutManager topicLayoutManager = new GridLayoutManager(this, 3);
        rvRecommendTopics.setLayoutManager(topicLayoutManager);
        rvRecommendTopics.setAdapter(mRecommendTopicAdapter);

        GridLayoutManager peopleLayoutManager = new GridLayoutManager(this, 2);
        peopleLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        rvRecommendPeople.setLayoutManager(peopleLayoutManager);
        rvRecommendPeople.setAdapter(mRecommendPeopleAdapter);

    }

    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.layout_topic:
                    if (v.isSelected()) {
                        mRecommendTopicAdapter.getSelectState().put(position, true);
                        mPresenter.addInterestedTopic(position);
                    } else {
                        mRecommendTopicAdapter.getSelectState().put(position, false);
                        mPresenter.removeTopic(position);
                    }
                    break;
                case R.id.layout_user:
                    if (v.isSelected()) {
                        mRecommendPeopleAdapter.getSelectState().put(position, true);
                        mPresenter.addFollowedPerson(position);
                    } else {
                        mRecommendPeopleAdapter.getSelectState().put(position, false);
                        mPresenter.removePerson(position);
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.iv_enter_app})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_enter_app :
                String token = getIntent().getStringExtra("token");
                mPresenter.postAll(mCallback, token);
        }
    }

    RecommendDataSource.DataPostedCallback mCallback = new RecommendDataSource.DataPostedCallback() {
        @Override
        public void onSuccess() {
            sucesss ++;
            Log.d("success", sucesss + "");
            if (sucesss == 2) {
                hideWaitingDialog();
                Intent i = new Intent(LoginRecommendActivity.this, MainActivity.class);
                startActivity(i);
            }
            Log.d("success", sucesss + "");
        }

        @Override
        public void onFailed() {
            showToast("关注推荐失败，请检查网络设置后重试");
            sucesss = 0;
        }
    };

    @Override
    public void showRecommendTopicSelected(int position) {
        View itemView = rvRecommendTopics.getLayoutManager().findViewByPosition(position);
        ImageView ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        ivCheck.setVisibility(View.VISIBLE);

    }

    @Override
    public void showRecommendTopicUnSelected(int position) {
        View itemView = rvRecommendTopics.getLayoutManager().findViewByPosition(position);
        ImageView ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        ivCheck.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadRecommendTopics(int position) {

    }

    @Override
    public void hideLoadRecommendTopics(int position) {

    }



    @Override
    public void showRecommendPeopleSelected(int position) {
        View itemView = rvRecommendPeople.getLayoutManager().findViewByPosition(position);
        ImageView ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        ivCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecommendPeopleUnSelected(int position) {
        View itemView = rvRecommendPeople.getLayoutManager().findViewByPosition(position);
        ImageView ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        ivCheck.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadingRecommendPeople(int position) {

    }

    @Override
    public void hideLoadingRecommendPeople(int position) {

    }

    @Override
    public void setPresenter(RecommendContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRecommendPeople(List<RecommendPeople.DataBean.ListBean> recommendPeople) {
        mRecommendPeopleAdapter.replaceItems(recommendPeople);
    }

    @Override
    public void showRecommendTopics(List<RecommendTopic.DataBean.ListBean> recommendTopic) {
        mRecommendTopicAdapter.replaceItems(recommendTopic);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void showWaitingDialog(String msg) {
        if (waitDialog == null) {
            waitDialog = new ProgressDialog(this);
        }
        waitDialog.setMessage(msg);
        waitDialog.show();
    }

    @Override
    public void hideWaitingDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }
}
