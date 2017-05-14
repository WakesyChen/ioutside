package com.xiaoxiang.ioutside.mine.mvp;

import com.xiaoxiang.ioutside.mine.model.RecommendPeople;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;

import java.util.List;

/**
 * Created by 15119 on 2016/6/28.
 */
public interface RecommendDataSource {

    interface GetRecommendPeopleCallback {
        void onRecommendPeopleLoaded(List<RecommendPeople.DataBean.ListBean> people);
        void onError();
    }

    interface GetRecommendTopicsCallback {
        void onRecommendTopicsLoaded(List<RecommendTopic.DataBean.ListBean> topics);
        void onError();
    }

    interface DataPostedCallback {
        void onSuccess();
        void onFailed();
    }

    void getRecommendPeople(GetRecommendPeopleCallback callback);
    void getRecommendTopics(GetRecommendTopicsCallback callback);

    void addInterestedTopic(int topicId);
    void addFollowedPerson(int userId);

    void removeTopic(int topicId);
    void removePerson(int userId);

    void postFollowedPeople(DataPostedCallback callback, String token);
    void postInterestedTopics(DataPostedCallback callback, String token);

}
