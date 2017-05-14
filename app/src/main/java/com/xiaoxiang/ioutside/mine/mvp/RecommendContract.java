package com.xiaoxiang.ioutside.mine.mvp;

import com.xiaoxiang.ioutside.mine.model.RecommendPeople;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;

import java.util.List;

/**
 * Created by 15119 on 2016/6/28.
 */
public interface RecommendContract {

    interface View extends BaseView<Presenter> {

        void showRecommendTopicSelected(int position);
        void showRecommendTopicUnSelected(int position);
        void showLoadRecommendTopics(int position);
        void hideLoadRecommendTopics(int position);

        void showRecommendPeopleSelected(int position);
        void showRecommendPeopleUnSelected(int position);
        void showLoadingRecommendPeople(int position);
        void hideLoadingRecommendPeople(int position);

        void showRecommendPeople(List<RecommendPeople.DataBean.ListBean> recommendPeople);
        void showRecommendTopics(List<RecommendTopic.DataBean.ListBean> recommendTopic);

        void showToast(String msg);

        void showWaitingDialog(String msg);
        void hideWaitingDialog();

    }

    interface Presenter extends BasePresenter<View> {

        void postAll(RecommendDataSource.DataPostedCallback callback, String token);

        void loadRecommendTopics();
        void loadRecommendPeople();

        void refreshRecommendPeople();
        void refreshRecommendTopics();

        void postFollowedPeople(RecommendDataSource.DataPostedCallback callback, String token);
        void postInterestedTopics(RecommendDataSource.DataPostedCallback callback, String token);

        void addInterestedTopic(int position);
        void addFollowedPerson(int position);

        void removeTopic(int position);
        void removePerson(int position);
    }

}
