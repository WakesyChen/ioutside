package com.xiaoxiang.ioutside.mine.mvp;

import com.xiaoxiang.ioutside.mine.model.RecommendPeople;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by 15119 on 2016/6/29.
 */
public class RecommendPresenter implements RecommendContract.Presenter {

    private WeakReference<RecommendContract.View> mViewRef;
    private RecommendDataSource mRepository;

    public RecommendPresenter(RecommendContract.View view, RecommendDataSource repository) {
        setView(view);
        view.setPresenter(this);
        mRepository = repository;
    }

    @Override
    public void loadRecommendTopics() {

        mRepository.getRecommendTopics(new RecommendDataSource.GetRecommendTopicsCallback() {
            @Override
            public void onRecommendTopicsLoaded(List<RecommendTopic.DataBean.ListBean> topics) {
                if (isViewAttached()) {
                    getView().showRecommendTopics(topics);
                }
            }

            @Override
            public void onError() {
                if (isViewAttached()) {

                }
            }
        });
    }

    @Override
    public void loadRecommendPeople() {
        mRepository.getRecommendPeople(new RecommendDataSource.GetRecommendPeopleCallback() {
            @Override
            public void onRecommendPeopleLoaded(List<RecommendPeople.DataBean.ListBean> people) {

                if (isViewAttached()) {
                    getView().showRecommendPeople(people);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void refreshRecommendPeople() {

    }

    @Override
    public void refreshRecommendTopics() {

    }

    @Override
    public void postFollowedPeople(RecommendDataSource.DataPostedCallback callback, String token) {
        mRepository.postFollowedPeople(callback, token);
    }

    @Override
    public void postInterestedTopics(RecommendDataSource.DataPostedCallback callback, String token) {
        mRepository.postInterestedTopics(callback, token);
    }

    @Override
    public void postAll(final RecommendDataSource.DataPostedCallback callback, String token) {

        if (isViewAttached()) {
            getView().showWaitingDialog("请稍后...");
        }

        RecommendDataSource.DataPostedCallback callbackWrapper = new RecommendDataSource.DataPostedCallback() {

            @Override
            public void onSuccess() {
                if (isViewAttached()) {
                    getView().hideWaitingDialog();
                }
                callback.onSuccess();
            }

            @Override
            public void onFailed() {
                if (isViewAttached()) {
                    getView().hideWaitingDialog();
                }
            }
        };

        postFollowedPeople(callbackWrapper, token);

        postInterestedTopics(callbackWrapper, token);

    }

    @Override
    public void start() {
        loadRecommendPeople();
        loadRecommendTopics();
    }

    @Override
    public void setView(RecommendContract.View view) {
        mViewRef = new WeakReference<>(view);
    }

    @Override
    public void addInterestedTopic(int position) {
        mRepository.addInterestedTopic(position);
        if (isViewAttached()) {
            getView().showRecommendTopicSelected(position);
        }
    }

    @Override
    public void addFollowedPerson(int position) {
        mRepository.addFollowedPerson(position);
        if (isViewAttached()) {
            getView().showRecommendPeopleSelected(position);
        }
    }

    @Override
    public void removeTopic(int position) {
        mRepository.removeTopic(position);
        if (isViewAttached()) {
            getView().showRecommendTopicUnSelected(position);
        }
    }

    @Override
    public void removePerson(int position) {
        mRepository.removePerson(position);
        if (isViewAttached()) {
            getView().showRecommendPeopleUnSelected(position);
        }
    }

    @Override
    public RecommendContract.View getView() {
        return mViewRef.get();
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }


}
